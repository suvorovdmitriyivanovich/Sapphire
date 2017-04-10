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
import com.sapphire.activities.LoginActivity;
import com.sapphire.activities.MultichoiseDaysActivity;
import com.sapphire.activities.workplaceInspection.WorkplaceInspectionItemActivity;
import com.sapphire.adapters.DayItemsAdapter;
import com.sapphire.adapters.SpinAttendanceCodesAdapter;
import com.sapphire.adapters.SpinTimeBanksAdapter;
import com.sapphire.api.GetWorkplaceInspectionAction;
import com.sapphire.api.TimeOffRequestAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.api.WorkplaceInspectionItemAddAction;
import com.sapphire.api.WorkplaceInspectionItemDeleteAction;
import com.sapphire.db.DBHelper;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.AttendanceCodeData;
import com.sapphire.models.DayData;
import com.sapphire.models.TimeBankData;
import com.sapphire.models.TimeOffRequestData;
import com.sapphire.models.WorkplaceInspectionItemData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TimeOffRequestActivity extends BaseActivity implements GetWorkplaceInspectionAction.RequestWorkplaceInspection,
                                                                    DayItemsAdapter.OnRootClickListener,
                                                                    DayItemsAdapter.OnDeleteClickListener,
                                                                    DayItemsAdapter.OnChangeClickListener,
                                                                    WorkplaceInspectionItemDeleteAction.RequestWorkplaceInspectionItemDelete,
                                                                    TimeOffRequestAddAction.RequestTimeOffRequestAdd,
                                                                    WorkplaceInspectionItemAddAction.RequestWorkplaceInspectionItemAdd,
                                                                    UpdateAction.RequestUpdate{
    private String id = "";
    private ProgressDialog pd;
    private RecyclerView itemlist;
    private DayItemsAdapter adapter;
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
    private EditText attendance;
    private Spinner spinnerAttendance;
    private ArrayList<AttendanceCodeData> attendances;
    private SpinAttendanceCodesAdapter adapterAttendance;
    private EditText timeBank;
    private Spinner spinnerTimeBank;
    private ArrayList<TimeBankData> timeBanks;
    private SpinTimeBanksAdapter adapterTimeBank;
    private boolean clickSpinner = false;
    private String attendanceId = "";
    private View text_attendance;
    private String timeBankId = "";
    private View text_timeBank;
    private TimeOffRequestData data = new TimeOffRequestData();
    private View text_no;
    private boolean isCheckName = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private UserInfo userInfo;
    private boolean isCheckDays = false;
    private View text_days_error;
    private ArrayList<DayData> datas = new ArrayList<DayData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_off_request);

        userInfo = UserInfo.getUserInfo();
        userInfo.setDays(null);

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

                    adapter.remove(currentPosition);
                    datas.remove(currentPosition);
                    //adapter = null;
                    //adapter = new DayItemsAdapter(TimeOffRequestActivity.this);
                    //itemlist.setAdapter(adapter);
                    //adapter.setListArray(datas);
                    updateVisibility();
                    updateViews();
                } else {
                    updateWorkplaceInspection(0);
                }
            }
        });

        name = (EditText) findViewById(R.id.name);
        text_name_error = findViewById(R.id.text_name_error);
        text_name = findViewById(R.id.text_name);
        attendance = (EditText) findViewById(R.id.attendance);
        spinnerAttendance = (Spinner) findViewById(R.id.spinnerAttendance);
        text_attendance = findViewById(R.id.text_attendance);
        timeBank = (EditText) findViewById(R.id.time_bank);
        spinnerTimeBank = (Spinner) findViewById(R.id.spinnerTime_bank);
        text_timeBank = findViewById(R.id.text_time_bank);
        text_days_error = findViewById(R.id.text_days_error);

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

        attendances = new ArrayList<>();
        attendances.addAll(UserInfo.getUserInfo().getAttendanceCodeDatas());

        adapterAttendance = new SpinAttendanceCodesAdapter(this, R.layout.spinner_list_item_black);
        spinnerAttendance.setAdapter(adapterAttendance);
        adapterAttendance.setValues(attendances);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                clickSpinner = true;
                spinnerAttendance.performClick();
            }
        });
        spinnerAttendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                attendance.setText(attendances.get(position).getName());
                attendanceId = attendances.get(position).getAttendanceCodeId();
                clickSpinner = false;

                if (!attendanceId.equals("")) {
                    //pd.show();
                    //new GetTemplateAction(TimeOffRequestActivity.this, attendanceId, getResources().getString(R.string.text_workplace_templates)).execute();
                    //} else if (!workplaceInspectionId.equals("")) {
                    //    new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
                } else {
                    //adapter.setListArray(new ArrayList<DayData>());
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
                timeBankId = timeBanks.get(position).getTimeBankAccountId();
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

                userInfo.setDays(datas);
                Intent intent = new Intent(TimeOffRequestActivity.this, MultichoiseDaysActivity.class);
                startActivity(intent);
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

        //adapter = new DayItemsAdapter(this);
        //itemlist.setAdapter(adapter);

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

        updateVisibility();

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

        if (name.getText().toString().equals("") || datas.size() == 0) {
            isCheckName = true;
            isCheckDays = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            pressType = type;

            TimeOffRequestData timeOffRequestData =  new TimeOffRequestData();
            timeOffRequestData.setTimeOffRequestId(id);
            timeOffRequestData.setTimeBank(new TimeBankData(timeBankId));
            timeOffRequestData.setAttendanceCode(new AttendanceCodeData(attendanceId));
            timeOffRequestData.setDays(datas);

            new TimeOffRequestAddAction(TimeOffRequestActivity.this, timeOffRequestData).execute();
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
        if (isCheckDays && datas.size() == 0) {
            text_days_error.setVisibility(View.VISIBLE);
        } else {
            text_days_error.setVisibility(View.GONE);
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
    public void onRequestWorkplaceInspection(String result, ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas) {
        if (!result.equals("OK") && !result.equals(getResources().getString(R.string.text_need_internet))) {
            pressType = 0;
            updateVisibility();
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        //this.datas = allDatas;
        //adapter.setListArray(this.datas);
        updateVisibility();

        pd.hide();
        if (pressType == 2) {
            pressType = 0;
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
    public void onChangeClick(int position, String text) {
        datas.get(position).setAmmount(text);
    }

    @Override
    public void onRequestWorkplaceInspectionItemDelete(String result) {
        dialog_confirm.dismiss();
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        id = data.getTimeOffRequestId();

        pd.hide();
        requestWorkplaceInspectionAddData();
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
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else if (neddclosepd) {
            pd.hide();

            requestWorkplaceInspectionAddData();
        }
    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void sort() {
        Collections.sort(datas, new Comparator<DayData>() {
            public int compare(DayData o1, DayData o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userInfo.getDays() != null) {
            //adapter.addDatas(userInfo.getDays());
            datas.addAll(userInfo.getDays());
            sort();
            adapter = null;
            adapter = new DayItemsAdapter(this);
            itemlist.setAdapter(adapter);
            adapter.setListArray(datas);
            userInfo.setDays(null);
            isCheckDays = true;
            updateVisibility();
            updateViews();
        }

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
