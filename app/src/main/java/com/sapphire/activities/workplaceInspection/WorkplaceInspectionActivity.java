package com.sapphire.activities.workplaceInspection;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.FilesActivity;
import com.sapphire.adapters.SpinTemplatesAdapter;
import com.sapphire.adapters.WorkplaceInspectionItemsAdapter;
import com.sapphire.api.GetTemplateAction;
import com.sapphire.api.GetWorkplaceInspectionAction;
import com.sapphire.api.WorkplaceInspectionItemAddAction;
import com.sapphire.api.WorkplaceInspectionItemDeleteAction;
import com.sapphire.api.WorkplaceInspectionAddAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.TemplateData;
import com.sapphire.logic.TemplateItemData;
import com.sapphire.logic.UserInfo;
import com.sapphire.logic.WorkplaceInspectionData;
import com.sapphire.logic.WorkplaceInspectionItemData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WorkplaceInspectionActivity extends BaseActivity implements GetTemplateAction.RequestTemplate,
                                                              GetTemplateAction.RequestTemplateData,
                                                              GetWorkplaceInspectionAction.RequestWorkplaceInspection,
                                                              GetWorkplaceInspectionAction.RequestWorkplaceInspectionData,
                                                              WorkplaceInspectionItemsAdapter.OnRootClickListener,
                                                              WorkplaceInspectionItemsAdapter.OnOpenClickListener,
                                                              WorkplaceInspectionItemsAdapter.OnDeleteClickListener,
                                                              WorkplaceInspectionItemsAdapter.OnFilesClickListener,
                                                              WorkplaceInspectionItemDeleteAction.RequestWorkplaceInspectionItemDelete,
                                                              WorkplaceInspectionAddAction.RequestWorkplaceInspectionAdd,
                                                              WorkplaceInspectionAddAction.RequestWorkplaceInspectionAddData,
                                                              WorkplaceInspectionItemAddAction.RequestWorkplaceInspectionItemAdd{
    private String workplaceInspectionId = "";
    ProgressDialog pd;
    private ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas = new ArrayList<WorkplaceInspectionItemData>();
    private RecyclerView itemlist;
    private WorkplaceInspectionItemsAdapter adapter;
    private EditText name;
    private EditText date;
    private EditText description;
    private View image_date_group;
    private View text_name_error;
    private View text_date_error;
    private View text_name;
    private View text_date;
    private CheckBox posted;
    private int pressType = 0;
    private String nameOld = "";
    private String descriptionOld = "";
    private Long dateOld = 0l;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private boolean deleteItem = false;
    private int currentPosition = 0;
    private static SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    int myYear = cal.get(Calendar.YEAR);
    int myMonth = cal.get(Calendar.MONTH);
    int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private EditText template;
    private Spinner spinnerTemplate;
    private ArrayList<TemplateData> templates;
    private SpinTemplatesAdapter adapterTemplate;
    private boolean clickSpinner = false;
    private String templateId = "";
    private View text_template;
    private Long dateNew;
    private boolean postedOld = false;
    private WorkplaceInspectionData workplaceInspectionData = new WorkplaceInspectionData();
    private View text_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workplace_inspection);

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
                    pd.show();

                    new WorkplaceInspectionItemDeleteAction(WorkplaceInspectionActivity.this, workplaceInspectionItemDatas.get(currentPosition).getWorkplaceInspectionItemId()).execute();
                } else {
                    updateWorkplaceInspection(0);
                }
            }
        });

        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        image_date_group = findViewById(R.id.image_date_group);
        description = (EditText) findViewById(R.id.description);
        text_name_error = findViewById(R.id.text_name_error);
        text_date_error = findViewById(R.id.text_date_error);
        text_name = findViewById(R.id.text_name);
        text_date = findViewById(R.id.text_date);
        template = (EditText) findViewById(R.id.template);
        spinnerTemplate = (Spinner) findViewById(R.id.spinnerTemplate);
        text_template = findViewById(R.id.text_template);
        posted = (CheckBox) findViewById(R.id.posted);

        format = new SimpleDateFormat("dd.MM.yyyy");

        Intent intent = getIntent();
        workplaceInspectionId = intent.getStringExtra("workplaceInspectionId");
        if (workplaceInspectionId == null) {
            workplaceInspectionId = "";
        }
        if (!workplaceInspectionId.equals("")) {
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            dateOld = intent.getLongExtra("date", 0l);
            dateNew = dateOld;
            name.setText(nameOld);
            description.setText(descriptionOld);
            postedOld = intent.getBooleanExtra("posted", false);
            posted.setChecked(postedOld);

            if (dateOld != 0l) {
                try {
                    Date thisdaten = new Date();
                    thisdaten.setTime(dateOld);
                    String datet = format.format(thisdaten);
                    date.setText(datet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            template.setVisibility(View.GONE);
            text_template.setVisibility(View.GONE);
        }

        if (workplaceInspectionId.equals("")) {
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
                    templateId = templates.get(position).getWorkplaceInspectionTemplateId();
                    clickSpinner = false;

                    if (!templateId.equals("")) {
                        pd.show();
                        new GetTemplateAction(WorkplaceInspectionActivity.this, templateId).execute();
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
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseDate();
            }
        });

        image_date_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseDate();
            }
        });

        TextWatcher inputTextWatcher = new TextWatch();
        name.addTextChangedListener(inputTextWatcher);

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (workplaceInspectionId.equals("") || !nameOld.equals(name.getText().toString())
                        || !descriptionOld.equals(description.getText().toString())
                        || !dateOld.equals(dateNew)
                        || postedOld != posted.isChecked()) {
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

                if (name.getText().toString().equals("") || date.getText().toString().equals("")) {
                    return;
                }

                if (!nameOld.equals(name.getText().toString())
                        || !descriptionOld.equals(description.getText().toString())
                        || !dateOld.equals(dateNew)
                        || postedOld != posted.isChecked()) {
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
        itemlist.setLayoutManager(new LinearLayoutManager(WorkplaceInspectionActivity.this));

        adapter = new WorkplaceInspectionItemsAdapter(this);
        itemlist.setAdapter(adapter);

        updateViews();

        text_no = findViewById(R.id.text_no);

        updateVisibility();
    }

    public void updateVisibility() {
        if (workplaceInspectionItemDatas == null || workplaceInspectionItemDatas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            itemlist.setVisibility(View.GONE);
        } else {
            itemlist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
    }

    private void choiseDate() {
        hideSoftKeyboard();

        Date dateD = null;
        try {
            dateD = format.parse(date.getText().toString());
        } catch (ParseException e) {
            dateD = new Date();
            e.printStackTrace();
        }

        cal.setTime(dateD);

        myYear = cal.get(Calendar.YEAR);
        myMonth = cal.get(Calendar.MONTH);
        myDay = cal.get(Calendar.DAY_OF_MONTH);

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            //final Calendar c = Calendar.getInstance();
            //int year = c.get(Calendar.YEAR);
            //int month = c.get(Calendar.MONTH);
            //int day = c.get(Calendar.DAY_OF_MONTH);

            WorkplaceInspectionActivity act = (WorkplaceInspectionActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            WorkplaceInspectionActivity act = (WorkplaceInspectionActivity) getActivity();
            act.myYear = year;
            act.myMonth = month;
            act.myDay = day;
            String mMonth = "";
            String mDay = "";
            if ((act.myDay+"").length() == 1) {
                mDay = "0" + act.myDay;
            } else {
                mDay = act.myDay + "";
            }
            if (((act.myMonth+1)+"").length() == 1) {
                mMonth = "0" + (act.myMonth+1);
            } else {
                mMonth = (act.myMonth+1)+"";
            }

            act.date.setText("" + mDay + "." + mMonth + "." + act.myYear);

            Date dateD = null;
            String dateNewstr = act.date.getText().toString();
            if (!dateNewstr.equals("")) {
                try {
                    dateD = format.parse(dateNewstr);
                    act.dateNew = dateD.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            act.updateViews();
        }
    }

    private void updateWorkplaceInspection(int type) {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("") || date.getText().toString().equals("")) {
            allOk = false;
        }

        if (allOk) {
            pd.show();

            pressType = type;

            new WorkplaceInspectionAddAction(WorkplaceInspectionActivity.this, workplaceInspectionId, name.getText().toString(), description.getText().toString(), dateNew, posted.isChecked()).execute();
        }
    }

    private class TextWatch implements TextWatcher {
        public TextWatch(){
            super();
        }

        public void afterTextChanged(Editable s) {
            updateViews();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    private void updateViews() {
        if (name.getText().toString().equals("")) {
            text_name_error.setVisibility(View.VISIBLE);
            text_name.setVisibility(View.GONE);
        } else {
            text_name_error.setVisibility(View.GONE);
            text_name.setVisibility(View.VISIBLE);
        }
        if (date.getText().toString().equals("")) {
            text_date_error.setVisibility(View.VISIBLE);
            text_date.setVisibility(View.GONE);
        } else {
            text_date_error.setVisibility(View.GONE);
            text_date.setVisibility(View.VISIBLE);
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void exit() {
        if (!nameOld.equals(name.getText().toString())
                || !descriptionOld.equals(description.getText().toString())
                || !dateOld.equals(dateNew)
                || postedOld != posted.isChecked()) {
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
        Intent intent = new Intent(WorkplaceInspectionActivity.this, WorkplaceInspectionItemActivity.class);
        intent.putExtra("workplaceInspectionId", workplaceInspectionId);
        startActivity(intent);
    }

    @Override
    public void onRequestTemplate(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
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

        this.workplaceInspectionItemDatas = list;
        adapter.setListArray(list);
        pd.hide();
    }

    @Override
    public void onRequestWorkplaceInspection(String result) {
        pressType = 0;
        updateVisibility();
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionData(ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas) {
        this.workplaceInspectionItemDatas = workplaceInspectionItemDatas;
        adapter.setListArray(workplaceInspectionItemDatas);
        updateVisibility();

        pd.hide();
        if (pressType == 2) {
            pressType = 0;
            openItem();
        }
    }

    @Override
    public void onRootClick(int position) {
        hideSoftKeyboard();
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
        if (workplaceInspectionId.equals("")) {
            updateWorkplaceInspection(2);
        } else {
            openItem();
        }
    }

    private void openItem() {
        Intent intent = new Intent(WorkplaceInspectionActivity.this, WorkplaceInspectionItemActivity.class);
        WorkplaceInspectionItemData workplaceInspectionItemData = workplaceInspectionItemDatas.get(currentPosition);
        intent.putExtra("name", workplaceInspectionItemData.getName());
        intent.putExtra("description", workplaceInspectionItemData.getDescription());
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
        } else {
            new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionAddData(WorkplaceInspectionData workplaceInspectionData) {
        this.workplaceInspectionData = workplaceInspectionData;
        nameOld = name.getText().toString();
        descriptionOld = description.getText().toString();
        postedOld = posted.isChecked();
        dateOld = workplaceInspectionData.getDate();
        dateNew = dateOld;
        if (workplaceInspectionId.equals("") && workplaceInspectionItemDatas.size() > 0) {
            workplaceInspectionId = workplaceInspectionData.getWorkplaceInspectionId();

            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setName(workplaceInspectionItemDatas.get(0).getName());
            workplaceInspectionItemData.setDescription(workplaceInspectionItemDatas.get(0).getDescription());
            workplaceInspectionItemData.setWorkplaceInspectionId(workplaceInspectionId);
            workplaceInspectionItemData.setSeverity(workplaceInspectionItemDatas.get(0).getSeverity());
            workplaceInspectionItemData.setStatus(workplaceInspectionItemDatas.get(0).getStatus());
            workplaceInspectionItemData.setPriority(workplaceInspectionItemDatas.get(0).getPriority());

            new WorkplaceInspectionItemAddAction(WorkplaceInspectionActivity.this, workplaceInspectionItemData, workplaceInspectionItemDatas.size() == 1, 0).execute();
        } else {
            workplaceInspectionId = workplaceInspectionData.getWorkplaceInspectionId();

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
            new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionItemAdd(String result, boolean neddclosepd, int ihms) {
        if (!result.equals("OK")) {
            pd.hide();

            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else if (neddclosepd) {
            pd.hide();

            requestWorkplaceInspectionAddData();
        } else if (ihms < workplaceInspectionItemDatas.size()) {
            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setName(workplaceInspectionItemDatas.get(ihms).getName());
            workplaceInspectionItemData.setDescription(workplaceInspectionItemDatas.get(ihms).getDescription());
            workplaceInspectionItemData.setWorkplaceInspectionId(workplaceInspectionId);
            workplaceInspectionItemData.setSeverity(workplaceInspectionItemDatas.get(ihms).getSeverity());
            workplaceInspectionItemData.setStatus(workplaceInspectionItemDatas.get(ihms).getStatus());
            workplaceInspectionItemData.setPriority(workplaceInspectionItemDatas.get(ihms).getPriority());

            new WorkplaceInspectionItemAddAction(WorkplaceInspectionActivity.this, workplaceInspectionItemData, ihms == workplaceInspectionItemDatas.size()-1, ihms).execute();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionAdd(String result) {
        pd.hide();

        pressType = 0;
        Toast.makeText(getBaseContext(), result,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFilesClick(int position) {
        Intent intent = new Intent(WorkplaceInspectionActivity.this, FilesActivity.class);
        WorkplaceInspectionItemData workplaceInspectionItemData = workplaceInspectionItemDatas.get(position);
        intent.putExtra("name", workplaceInspectionItemData.getName());
        intent.putExtra("id", workplaceInspectionItemData.getWorkplaceInspectionItemId());
        intent.putExtra("url", Environment.WorkplaceInspectionsItemsFilesURL);
        intent.putExtra("nameField", "WorkplaceInspectionItemId");
        UserInfo.getUserInfo().setFileDatas(workplaceInspectionItemData.getFiles());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!workplaceInspectionId.equals("")) {
            pd.show();
            new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
