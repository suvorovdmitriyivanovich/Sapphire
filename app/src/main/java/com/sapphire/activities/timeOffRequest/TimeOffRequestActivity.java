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
import com.sapphire.activities.MultichoiseDaysActivity;
import com.sapphire.adapters.DayItemsAdapter;
import com.sapphire.adapters.SpinAttendanceCodesAdapter;
import com.sapphire.adapters.SpinTimeBanksAdapter;
import com.sapphire.api.TimeOffRequestAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.AttendanceCodeData;
import com.sapphire.models.DayData;
import com.sapphire.models.TimeBankData;
import com.sapphire.models.TimeOffRequestData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TimeOffRequestActivity extends BaseActivity implements DayItemsAdapter.OnRootClickListener,
                                                                    DayItemsAdapter.OnDeleteClickListener,
                                                                    DayItemsAdapter.OnChangeClickListener,
                                                                    TimeOffRequestAddAction.RequestTimeOffRequestAdd,
                                                                    UpdateAction.RequestUpdate{
    private String id = "";
    private ProgressDialog pd;
    private RecyclerView itemlist;
    private DayItemsAdapter adapter;
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
    private String attendanceIdOld = "";
    private View text_attendance;
    private String timeBankId = "";
    private View text_timeBank;
    private TimeOffRequestData data = new TimeOffRequestData();
    private View text_no;
    private boolean isCheckAttendance = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private UserInfo userInfo;
    private boolean isCheckDays = false;
    private View text_days_error;
    private ArrayList<DayData> datas = new ArrayList<DayData>();
    private View text_attendance_error;
    private String statusId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_off_request);

        userInfo = UserInfo.getUserInfo();
        userInfo.setDays(null);

        itemlist = (RecyclerView) findViewById(R.id.itemlist);
        itemlist.setNestedScrollingEnabled(false);
        itemlist.setLayoutManager(new LinearLayoutManager(TimeOffRequestActivity.this));

        //adapter = new DayItemsAdapter(this, !readonly);
        //itemlist.setAdapter(adapter);

        text_no = findViewById(R.id.text_no);

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
                    //adapter = new DayItemsAdapter(TimeOffRequestActivity.this, !readonly);
                    //itemlist.setAdapter(adapter);
                    //adapter.setListArray(datas);
                    updateVisibility();
                    updateViews();
                } else {
                    updateTimeOffRequest();
                }
            }
        });

        text_attendance_error = findViewById(R.id.text_attendance_error);
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
            statusId = intent.getStringExtra("statusId");
            attendanceIdOld = intent.getStringExtra("attendanceCodeId");
            attendanceId = attendanceIdOld;
            if (userInfo.getAllDays() != null) {
                for (DayData item: userInfo.getAllDays()) {
                    DayData dayData = new DayData();
                    dayData.setAmmount(item.getAmmount());
                    dayData.setDate(item.getDate());
                    dayData.setTimeoffRequestDayId(item.getTimeoffRequestDayId());
                    dayData.setTimeoffRequestId(item.getTimeoffRequestId());

                    datas.add(dayData);
                }
                sort();
                adapter = null;
                adapter = new DayItemsAdapter(this, !readonly);
                itemlist.setAdapter(adapter);
                adapter.setListArray(datas);
                isCheckDays = true;
                updateVisibility();
                updateViews();
            }
        }

        attendances = new ArrayList<>();
        attendances.addAll(UserInfo.getUserInfo().getAttendanceCodeDatas());

        if (!attendanceIdOld.equals("")) {
            int attendancePosition = 0;
            for (int i = 0; i < attendances.size(); i ++) {
                if (attendances.get(i).getAttendanceCodeId().equals(attendanceIdOld)) {
                    attendancePosition = i;
                    break;
                }
            }
            attendance.setText(attendances.get(attendancePosition).getName());
            final int finalAttendancePosition = attendancePosition;
            attendance.postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinnerAttendance.setSelection(finalAttendancePosition,false);
                }
            }, 10);
        }

        adapterAttendance = new SpinAttendanceCodesAdapter(this, R.layout.spinner_list_item_black);
        spinnerAttendance.setAdapter(adapterAttendance);
        adapterAttendance.setValues(attendances);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
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
                text_attendance.setVisibility(View.VISIBLE);
                text_attendance_error.setVisibility(View.GONE);
                clickSpinner = false;
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
                if (readonly) {
                    return;
                }
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

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (id.equals("") || notEquals()) {
                    updateTimeOffRequest();
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

    private void updateTimeOffRequest() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (attendance.getText().toString().equals("") || datas.size() == 0) {
            isCheckAttendance = true;
            isCheckDays = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            TimeOffRequestData timeOffRequestData =  new TimeOffRequestData();
            timeOffRequestData.setTimeoffRequestId(id);
            timeOffRequestData.setTimeBankName(timeBank.getText().toString());
            timeOffRequestData.setAttendanceCodeId(attendanceId);
            if (statusId.equals("")) {
                timeOffRequestData.setTimeoffRequestStatusId(Environment.TimeOffRequestAddId);
            } else {
                timeOffRequestData.setTimeoffRequestStatusId(statusId);
            }
            timeOffRequestData.setDays(datas);

            new TimeOffRequestAddAction(TimeOffRequestActivity.this, timeOffRequestData).execute();
        }
    }

    private boolean notEquals() {
        boolean rezult = !attendanceIdOld.equals(attendanceId);

        if (!rezult) {
            ArrayList<DayData> dayDatas = userInfo.getAllDays();
            if ((dayDatas == null && datas.size() != 0) || dayDatas.size() != datas.size()) {
                rezult = true;
            } else {
                for (int i=0; i < datas.size(); i++) {
                    if (dayDatas == null || dayDatas.size() <= i) {
                        rezult = true;
                        break;
                    } else {
                        if (!datas.get(i).getDateString().equals(dayDatas.get(i).getDateString())
                                || datas.get(i).getAmmount() != dayDatas.get(i).getAmmount()) {
                            rezult = true;
                            break;
                        }
                    }
                }
            }
        }

        return rezult;
    }

    private void updateViews() {
        if (isCheckAttendance && attendance.getText().toString().equals("")) {
            text_attendance_error.setVisibility(View.VISIBLE);
            text_attendance.setVisibility(View.GONE);
        } else {
            text_attendance_error.setVisibility(View.GONE);
            text_attendance.setVisibility(View.VISIBLE);
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
        if (notEquals()) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
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
    public void onRequestTimeOffRequestAdd(String result, TimeOffRequestData data) {
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
            finish();
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
            adapter = new DayItemsAdapter(this, !readonly);
            itemlist.setAdapter(adapter);
            adapter.setListArray(datas);
            userInfo.setDays(null);
            isCheckDays = true;
            updateVisibility();
            updateViews();
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
