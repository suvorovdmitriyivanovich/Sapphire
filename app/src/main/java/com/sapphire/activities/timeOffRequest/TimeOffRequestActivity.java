package com.sapphire.activities.timeOffRequest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.FilesActivity;
import com.sapphire.activities.LoginActivity;
import com.sapphire.activities.workplaceInspection.WorkplaceInspectionItemActivity;
import com.sapphire.adapters.SpinTemplatesAdapter;
import com.sapphire.adapters.SpinTimeBanksAdapter;
import com.sapphire.adapters.WorkplaceInspectionItemsAdapter;
import com.sapphire.api.GetTemplateAction;
import com.sapphire.api.GetWorkplaceInspectionAction;
import com.sapphire.api.TimeOffRequestAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.api.WorkplaceInspectionAddAction;
import com.sapphire.api.WorkplaceInspectionItemAddAction;
import com.sapphire.api.WorkplaceInspectionItemDeleteAction;
import com.sapphire.db.DBHelper;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.TemplateData;
import com.sapphire.models.TemplateItemData;
import com.sapphire.models.TimeBankData;
import com.sapphire.models.TimeOffRequestData;
import com.sapphire.models.WorkplaceInspectionItemData;
import java.util.ArrayList;

public class TimeOffRequestActivity extends BaseActivity implements GetTemplateAction.RequestTemplate,
                                                                    GetTemplateAction.RequestTemplateData,
                                                                    GetWorkplaceInspectionAction.RequestWorkplaceInspection,
                                                                    WorkplaceInspectionItemsAdapter.OnRootClickListener,
                                                                    WorkplaceInspectionItemsAdapter.OnOpenClickListener,
                                                                    WorkplaceInspectionItemsAdapter.OnDeleteClickListener,
                                                                    WorkplaceInspectionItemsAdapter.OnFilesClickListener,
                                                                    WorkplaceInspectionItemDeleteAction.RequestWorkplaceInspectionItemDelete,
                                                                    TimeOffRequestAddAction.RequestTimeOffRequestAdd,
                                                                    WorkplaceInspectionItemAddAction.RequestWorkplaceInspectionItemAdd,
                                                                    UpdateAction.RequestUpdate{
    private String id = "";
    private ProgressDialog pd;
    private ArrayList<WorkplaceInspectionItemData> datas = new ArrayList<WorkplaceInspectionItemData>();
    private RecyclerView itemlist;
    private WorkplaceInspectionItemsAdapter adapter;
    private EditText name;
    private View text_name_error;
    private View text_name;
    private int pressType = 0;
    private String nameOld = "";
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private boolean deleteItem = false;
    private int currentPosition = 0;
    private EditText template;
    private Spinner spinnerTemplate;
    private ArrayList<TemplateData> templates;
    private SpinTemplatesAdapter adapterTemplate;
    private EditText timeBank;
    private Spinner spinnerTimeBank;
    private ArrayList<TimeBankData> timeBanks;
    private SpinTimeBanksAdapter adapterTimeBank;
    private boolean clickSpinner = false;
    private String templateId = "";
    private View text_template;
    private String timeBankId = "";
    private View text_timeBank;
    private TimeOffRequestData data = new TimeOffRequestData();
    private View text_no;
    private boolean isCheckName = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_off_request);

        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                exit();
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        AlertDialog.Builder adb_save = new AlertDialog.Builder(this);
        adb_save.setCancelable(true);
        LinearLayout view_save = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_save, null);
        adb_save.setView(view_save);
        tittle_message = (TextView) view_save.findViewById(R.id.tittle);
        button_cancel_save = (Button) view_save.findViewById(R.id.button_cancel);
        button_send_save = (Button) view_save.findViewById(R.id.button_send);
        dialog_confirm = adb_save.create();
        dialog_confirm.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                deleteItem = false;
            }
        });
        dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                deleteItem = false;
            }
        });

        button_cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
                if (!deleteItem) {
                    finish();
                }
                deleteItem = false;
            }
        });

        button_send_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
                if (deleteItem) {
                    deleteItem = false;
                    if (datas.get(currentPosition).getWorkplaceInspectionItemId().equals("")) {
                        DBHelper.getInstance(Sapphire.getInstance()).deleteWorkplaceInspectionItem(datas.get(currentPosition).getId());
                        pd.show();
                        new GetWorkplaceInspectionAction(TimeOffRequestActivity.this, id).execute();
                    } else {
                        pd.show();

                        new WorkplaceInspectionItemDeleteAction(TimeOffRequestActivity.this, datas.get(currentPosition).getWorkplaceInspectionItemId()).execute();
                    }
                } else {
                    updateWorkplaceInspection(0);
                }
            }
        });

        name = (EditText) findViewById(R.id.name);
        text_name_error = findViewById(R.id.text_name_error);
        text_name = findViewById(R.id.text_name);
        template = (EditText) findViewById(R.id.template);
        spinnerTemplate = (Spinner) findViewById(R.id.spinnerTemplate);
        text_template = findViewById(R.id.text_template);
        timeBank = (EditText) findViewById(R.id.time_bank);
        spinnerTimeBank = (Spinner) findViewById(R.id.spinnerTime_bank);
        text_timeBank = findViewById(R.id.text_time_bank);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        if (!id.equals("")) {
            readonly = intent.getBooleanExtra("readonly", false);
            nameOld = intent.getStringExtra("name");
            name.setText(nameOld);
        }

        templates = new ArrayList<>();
        templates.addAll(UserInfo.getUserInfo().getTemplateDatas());

        adapterTemplate = new SpinTemplatesAdapter(this, R.layout.spinner_list_item_black);
        spinnerTemplate.setAdapter(adapterTemplate);
        adapterTemplate.setValues(templates);
        template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                clickSpinner = true;
                spinnerTemplate.performClick();
            }
        });
        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                template.setText(templates.get(position).getName());
                templateId = templates.get(position).getTemplateId();
                clickSpinner = false;

                if (!templateId.equals("")) {
                    pd.show();
                    new GetTemplateAction(TimeOffRequestActivity.this, templateId, getResources().getString(R.string.text_workplace_templates)).execute();
                    //} else if (!workplaceInspectionId.equals("")) {
                    //    new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
                } else {
                    adapter.setListArray(new ArrayList<WorkplaceInspectionItemData>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*
        template.postDelayed(new Runnable() {
            @Override
            public void run() {
                template.setText(getResources().getString(R.string.text_history1));
                spinnerPeriod.setSelection(0,false);
            }
        }, 10);
        */

        timeBanks = new ArrayList<>();
        timeBanks.addAll(UserInfo.getUserInfo().getTimeBankDatas());

        adapterTimeBank = new SpinTimeBanksAdapter(this, R.layout.spinner_list_item_black);
        spinnerTimeBank.setAdapter(adapterTimeBank);
        adapterTimeBank.setValues(timeBanks);
        timeBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                clickSpinner = true;
                spinnerTimeBank.performClick();
            }
        });
        spinnerTimeBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                timeBank.setText(timeBanks.get(position).getName());
                timeBankId = timeBanks.get(position).getWorkplaceInspectionItemPriorityId();
                clickSpinner = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*
        timeBank.postDelayed(new Runnable() {
            @Override
            public void run() {
                template.setText(getResources().getString(R.string.text_history1));
                spinnerPeriod.setSelection(0,false);
            }
        }, 10);
        */

        TextWatcher inputTextWatcher = new TextWatch();
        name.addTextChangedListener(inputTextWatcher);

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (id.equals("") || !nameOld.equals(name.getText().toString())) {
                    updateWorkplaceInspection(0);
                } else {
                    finish();
                }
            }
        });

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (name.getText().toString().equals("")) {
                    isCheckName = true;
                    updateViews();
                    return;
                }

                if (!nameOld.equals(name.getText().toString())) {
                    updateWorkplaceInspection(1);
                } else {
                    addItem();
                }
            }
        });

        View root = findViewById(R.id.rootLayout);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        itemlist = (RecyclerView) findViewById(R.id.itemlist);
        itemlist.setNestedScrollingEnabled(false);
        itemlist.setLayoutManager(new LinearLayoutManager(TimeOffRequestActivity.this));

        adapter = new WorkplaceInspectionItemsAdapter(this, readonly);
        itemlist.setAdapter(adapter);

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(Environment.PARAM_TASK);

                if (putreqwest.equals("updatebottom")) {
                    UpdateBottom();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        updateViews();

        text_no = findViewById(R.id.text_no);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(TimeOffRequestActivity.this);
            }
        });

        UpdateBottom();

        if (readonly) {
            add.setVisibility(View.GONE);
            button_ok.setVisibility(View.GONE);
            name.setFocusable(false);
        }
    }

    private void UpdateBottom() {
        if (Sapphire.getInstance().getNeedUpdate()) {
            par_nointernet_group.height = GetPixelFromDips(56);
        } else {
            par_nointernet_group.height = 0;
        }
        nointernet_group.setLayoutParams(par_nointernet_group);
        nointernet_group.requestLayout();
    }

    public void updateVisibility() {
        if (datas == null || datas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            itemlist.setVisibility(View.GONE);
        } else {
            itemlist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
    }

    private void updateWorkplaceInspection(int type) {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("")) {
            isCheckName = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            pressType = type;

            new WorkplaceInspectionAddAction(TimeOffRequestActivity.this, id, name.getText().toString(), "", 0l, false).execute();
        }
    }

    private class TextWatch implements TextWatcher {
        public TextWatch(){
            super();
        }

        public void afterTextChanged(Editable s) {
            isCheckName = true;
            updateViews();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    private void updateViews() {
        if (isCheckName && name.getText().toString().equals("")) {
            text_name_error.setVisibility(View.VISIBLE);
            text_name.setVisibility(View.GONE);
        } else {
            text_name_error.setVisibility(View.GONE);
            text_name.setVisibility(View.VISIBLE);
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void exit() {
        if (!nameOld.equals(name.getText().toString())) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    private void addItem() {
        hideSoftKeyboard();
        Intent intent = new Intent(TimeOffRequestActivity.this, WorkplaceInspectionItemActivity.class);
        intent.putExtra("workplaceInspectionId", id);
        startActivity(intent);
    }

    @Override
    public void onRequestTemplate(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onRequestTemplateData(ArrayList<TemplateItemData> templatesItemDatas) {
        ArrayList<WorkplaceInspectionItemData> list = new ArrayList<WorkplaceInspectionItemData>();
        for (TemplateItemData item: templatesItemDatas) {
            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setDescription(item.getDescription());
            workplaceInspectionItemData.setName(item.getName());
            workplaceInspectionItemData.setPriority(item.getPriority());
            workplaceInspectionItemData.setSeverity(item.getSeverity());
            workplaceInspectionItemData.setStatus(item.getStatus());
            list.add(workplaceInspectionItemData);
        }

        this.datas = list;
        adapter.setListArray(list);
        pd.hide();
    }

    @Override
    public void onRequestWorkplaceInspection(String result, ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas) {
        if (!result.equals("OK") && !result.equals(getResources().getString(R.string.text_need_internet))) {
            pressType = 0;
            updateVisibility();
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            return;
        }

        ArrayList<WorkplaceInspectionItemData> allDatas = new ArrayList<WorkplaceInspectionItemData>();
        ArrayList<WorkplaceInspectionItemData> datas = DBHelper.getInstance(Sapphire.getInstance()).getWorkplaceInspectionItems(id);

        boolean isExist = false;
        for (WorkplaceInspectionItemData item: workplaceInspectionItemDatas) {
            isExist = false;
            for (WorkplaceInspectionItemData item2: datas) {
                if (item.getWorkplaceInspectionItemId().equals(item2.getWorkplaceInspectionItemId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                allDatas.add(item);
            }
        }
        for (WorkplaceInspectionItemData item: datas) {
            allDatas.add(item);
        }

        this.datas = allDatas;
        adapter.setListArray(this.datas);
        updateVisibility();

        pd.hide();
        if (pressType == 2) {
            pressType = 0;
            openItem(false);
        }
    }

    @Override
    public void onRootClick(int position) {
        hideSoftKeyboard();
        currentPosition = position;
        openItem(true);
    }

    @Override
    public void onDeleteClick(int position) {
        hideSoftKeyboard();

        currentPosition = position;
        deleteItem = true;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onOpenClick(int position) {
        hideSoftKeyboard();
        currentPosition = position;
        if (id.equals("")) {
            updateWorkplaceInspection(2);
        } else {
            openItem(false);
        }
    }

    private void openItem(boolean read) {
        Intent intent = new Intent(TimeOffRequestActivity.this, WorkplaceInspectionItemActivity.class);
        WorkplaceInspectionItemData workplaceInspectionItemData = datas.get(currentPosition);
        intent.putExtra("readonly", read);
        intent.putExtra("idloc", workplaceInspectionItemData.getId());
        intent.putExtra("name", workplaceInspectionItemData.getName());
        intent.putExtra("description", workplaceInspectionItemData.getDescription());
        intent.putExtra("comments", workplaceInspectionItemData.getComments());
        intent.putExtra("recommendedActions", workplaceInspectionItemData.getRecommendedActions());
        intent.putExtra("workplaceInspectionItemId", workplaceInspectionItemData.getWorkplaceInspectionItemId());
        intent.putExtra("workplaceInspectionId", workplaceInspectionItemData.getWorkplaceInspectionId());
        intent.putExtra("severity", workplaceInspectionItemData.getSeverity());
        intent.putExtra("workplaceInspectionItemStatusId", workplaceInspectionItemData.getStatus().getWorkplaceInspectionItemStatusId());
        intent.putExtra("workplaceInspectionItemPriorityId", workplaceInspectionItemData.getPriority().getWorkplaceInspectionItemPriorityId());
        startActivity(intent);
    }

    @Override
    public void onRequestWorkplaceInspectionItemDelete(String result) {
        dialog_confirm.dismiss();
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            new GetWorkplaceInspectionAction(TimeOffRequestActivity.this, id).execute();
        }
    }

    @Override
    public void onRequestTimeOffRequestAdd(String result, TimeOffRequestData data) {
        this.data = data;
        nameOld = name.getText().toString();
        if (id.equals("") && datas.size() > 0) {
           id = data.getWorkplaceInspectionId();

            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setName(datas.get(0).getName());
            workplaceInspectionItemData.setDescription(datas.get(0).getDescription());
            workplaceInspectionItemData.setWorkplaceInspectionId(id);
            workplaceInspectionItemData.setSeverity(datas.get(0).getSeverity());
            workplaceInspectionItemData.setStatus(datas.get(0).getStatus());
            workplaceInspectionItemData.setPriority(datas.get(0).getPriority());

            new WorkplaceInspectionItemAddAction(TimeOffRequestActivity.this, workplaceInspectionItemData, datas.size() == 1, 0, "").execute();
        } else {
            id = data.getWorkplaceInspectionId();

            pd.hide();
            requestWorkplaceInspectionAddData();
        }
    }

    private void requestWorkplaceInspectionAddData() {
        if (pressType == 1) {
            pressType = 0;
            addItem();
        } else if (pressType == 2) {
            pd.show();
            new GetWorkplaceInspectionAction(TimeOffRequestActivity.this, id).execute();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionItemAdd(String result, boolean neddclosepd, int ihms, String id) {
        if (!result.equals("OK")) {
            pd.hide();

            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (neddclosepd) {
            pd.hide();

            requestWorkplaceInspectionAddData();
        } else if (ihms < datas.size()) {
            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setName(datas.get(ihms).getName());
            workplaceInspectionItemData.setDescription(datas.get(ihms).getDescription());
            workplaceInspectionItemData.setWorkplaceInspectionId(id);
            workplaceInspectionItemData.setSeverity(datas.get(ihms).getSeverity());
            workplaceInspectionItemData.setStatus(datas.get(ihms).getStatus());
            workplaceInspectionItemData.setPriority(datas.get(ihms).getPriority());

            new WorkplaceInspectionItemAddAction(TimeOffRequestActivity.this, workplaceInspectionItemData, ihms == datas.size()-1, ihms, "").execute();
        }
    }

    @Override
    public void onFilesClick(int position) {
        Intent intent = new Intent(TimeOffRequestActivity.this, FilesActivity.class);
        WorkplaceInspectionItemData workplaceInspectionItemData = datas.get(position);
        intent.putExtra("name", workplaceInspectionItemData.getName());
        intent.putExtra("id", workplaceInspectionItemData.getWorkplaceInspectionItemId());
        intent.putExtra("url", Environment.WorkplaceInspectionsItemsFilesURL);
        intent.putExtra("nameField", "WorkplaceInspectionItemId");
        UserInfo.getUserInfo().setFileDatas(workplaceInspectionItemData.getFiles());
        startActivity(intent);
    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Sapphire.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();
            pd.hide();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!id.equals("")) {
            pd.show();
            new GetWorkplaceInspectionAction(TimeOffRequestActivity.this, id).execute();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
