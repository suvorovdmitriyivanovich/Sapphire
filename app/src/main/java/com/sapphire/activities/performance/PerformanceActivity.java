package com.sapphire.activities.performance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.api.PerformanceAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.PerformanceData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PerformanceActivity extends BaseActivity implements PerformanceAddAction.RequestPerformanceAdd,
                                                                 UpdateAction.RequestUpdate{
    private String id = "";
    private ProgressDialog pd;
    private EditText name;
    private EditText datePosted;
    private EditText renewalDate;
    private View image_date_group;
    private View image_renewal_date_group;
    private View text_name_error;
    private View text_date_error;
    private View text_name;
    private View text_date;
    private int pressType = 0;
    private String nameOld = "";
    private Long datePostedOld = 0l;
    private Long renewalDateOld = 0l;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private static SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    private int myYear = cal.get(Calendar.YEAR);
    private int myMonth = cal.get(Calendar.MONTH);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private Long datePostedNew = 0l;
    private Long renewalDateNew = 0l;
    private boolean isCheckName = false;
    private boolean isCheckDate = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_performance);

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

            }
        });
        dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        button_cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
                finish();
            }
        });

        button_send_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();

                updateWorkplaceInspection();
            }
        });

        name = (EditText) findViewById(R.id.name);
        datePosted = (EditText) findViewById(R.id.datePosted);
        renewalDate = (EditText) findViewById(R.id.renewalDate);
        image_date_group = findViewById(R.id.image_date_group);
        image_renewal_date_group = findViewById(R.id.image_renewal_date_group);
        text_name_error = findViewById(R.id.text_name_error);
        text_date_error = findViewById(R.id.text_date_error);
        text_name = findViewById(R.id.text_name);
        text_date = findViewById(R.id.text_date_posted);

        format = new SimpleDateFormat("dd.MM.yyyy");

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        if (!id.equals("")) {
            nameOld = intent.getStringExtra("name");
            datePostedOld = intent.getLongExtra("datePosted", 0l);
            datePostedNew = datePostedOld;
            renewalDateOld = intent.getLongExtra("renewalDate", 0l);
            renewalDateNew = renewalDateOld;
            name.setText(nameOld);

            if (datePostedOld != 0l) {
                try {
                    Date thisdaten = new Date();
                    thisdaten.setTime(datePostedOld);
                    String datet = format.format(thisdaten);
                    datePosted.setText(datet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (renewalDateOld != 0l) {
                try {
                    Date thisdaten = new Date();
                    thisdaten.setTime(renewalDateOld);
                    String datet = format.format(thisdaten);
                    renewalDate.setText(datet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        datePosted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseDate(1);
            }
        });

        image_date_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseDate(1);
            }
        });

        renewalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseDate(2);
            }
        });

        image_renewal_date_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseDate(2);
            }
        });

        TextWatcher inputTextWatcher = new TextWatch();
        name.addTextChangedListener(inputTextWatcher);

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (id.equals("") || !nameOld.equals(name.getText().toString())
                        || !datePostedOld.equals(datePostedNew)
                        || !renewalDateOld.equals(renewalDateNew)) {
                    updateWorkplaceInspection();
                } else {
                    finish();
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

                new UpdateAction(PerformanceActivity.this);
            }
        });

        UpdateBottom();
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

    private void choiseDate(int type) {
        hideSoftKeyboard();

        pressType = type;
        Date dateD = null;
        try {
            if (type == 1) {
                dateD = format.parse(datePosted.getText().toString());
            } else {
                dateD = format.parse(renewalDate.getText().toString());
            }
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

            PerformanceActivity act = (PerformanceActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            PerformanceActivity act = (PerformanceActivity) getActivity();
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

            if (act.pressType == 1) {
                act.datePosted.setText("" + mDay + "." + mMonth + "." + act.myYear);
            } else {
                act.renewalDate.setText("" + mDay + "." + mMonth + "." + act.myYear);
            }

            String dateNewstr = "";
            if (act.pressType == 1) {
                dateNewstr = act.datePosted.getText().toString();
            } else {
                dateNewstr = act.renewalDate.getText().toString();
            }
            if (!dateNewstr.equals("")) {
                try {
                    Date dateD = format.parse(dateNewstr);
                    if (act.pressType == 1) {
                        act.datePostedNew = dateD.getTime();
                    } else {
                        act.renewalDateNew = dateD.getTime();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            act.isCheckDate = true;

            act.updateViews();
        }
    }

    private void updateWorkplaceInspection() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("") || datePosted.getText().toString().equals("")) {
            isCheckName = true;
            isCheckDate = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            PerformanceData data = new PerformanceData();
            data.setName(name.getText().toString());
            data.setDatePosted(datePostedNew);
            data.setRenewalDate(renewalDateNew);
            data.setPerformanceEvaluationId(id);
            data.setProfileId(UserInfo.getUserInfo().getProfile().getProfileId());

            new PerformanceAddAction(PerformanceActivity.this, data).execute();
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
        if (isCheckDate && datePosted.getText().toString().equals("")) {
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
                || !datePostedOld.equals(datePostedNew)
                || !renewalDateOld.equals(renewalDateNew)) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPerformanceAdd(String result) {
        if (!result.equals("OK")) {
            pd.hide();

            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
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
