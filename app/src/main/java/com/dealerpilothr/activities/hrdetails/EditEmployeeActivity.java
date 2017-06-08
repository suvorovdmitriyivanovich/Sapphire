package com.dealerpilothr.activities.hrdetails;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.R;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.api.ProfilesAdditionalInformationAddAction;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.utils.GeneralOperations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditEmployeeActivity extends BaseActivity implements ProfilesAdditionalInformationAddAction.RequestProfilesAdditionalInformationAdd,
                                                                  UpdateAction.RequestUpdate {
    private ProgressDialog pd;
    private EditText birthday;
    private EditText sin_number;
    private EditText driver_license;
    private EditText driver_expiry;
    private TextView text_sin_number_error;
    private Long birthdayOld = 0l;
    private String sin_numberOld = "";
    private String driver_licenseOld = "";
    private Long driver_expiryOld = 0l;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private boolean isCheckSin_number = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_birthday;
    private TextView text_sin_number;
    private TextView text_driver_license;
    private TextView text_driver_expiry;
    //private Animation animationErrorDown;
    //private Animation animationErrorUpSin_number;
    //private boolean showErrorSin_number = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showBirthday = true;
    private boolean showSin_number = true;
    private boolean showDriver_license = true;
    private boolean showDriver_expiry = true;
    private TextView text_birthday_hint;
    private TextView text_sin_number_hint;
    private TextView text_driver_license_hint;
    private TextView text_driver_expiry_hint;
    private Long birthdayNew = 0l;
    private Long driver_expiryNew = 0l;
    private View image_birthday;
    private View image_driver_expiry;
    private static SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    private int myYear = cal.get(Calendar.YEAR);
    private int myMonth = cal.get(Calendar.MONTH);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private int pressType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_employee);

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

                update();
            }
        });

        birthday = (EditText) findViewById(R.id.birthday);
        image_birthday = findViewById(R.id.image_birthday);
        sin_number = (EditText) findViewById(R.id.sin_number);
        driver_license = (EditText) findViewById(R.id.driver_license);
        driver_expiry = (EditText) findViewById(R.id.driver_expiry);
        image_driver_expiry = findViewById(R.id.image_driver_expiry);
        text_sin_number_error = (TextView) findViewById(R.id.text_sin_number_error);
        text_birthday = (TextView) findViewById(R.id.text_birthday);
        text_sin_number = (TextView) findViewById(R.id.text_sin_number);
        text_driver_license = (TextView) findViewById(R.id.text_driver_license);
        text_driver_expiry = (TextView) findViewById(R.id.text_driver_expiry);
        text_birthday_hint = (TextView) findViewById(R.id.text_birthday_hint);
        text_sin_number_hint = (TextView) findViewById(R.id.text_sin_number_hint);
        text_driver_license_hint = (TextView) findViewById(R.id.text_driver_license_hint);
        text_driver_expiry_hint = (TextView) findViewById(R.id.text_driver_expiry_hint);

        //animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        //animationErrorUpSin_number = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        //animationErrorUpSin_number.setAnimationListener(animationErrorUpSin_numberListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        sin_number.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        driver_license.addTextChangedListener(inputTextWatcher);

        format = new SimpleDateFormat("dd.MM.yyyy");

        Intent intent = getIntent();

        readonly = intent.getBooleanExtra("readonly", false);
        birthdayOld = intent.getLongExtra("birthday", 0l);
        birthdayNew = birthdayOld;
        sin_numberOld = intent.getStringExtra("sin_number");
        if (sin_numberOld == null) {
            sin_numberOld = "";
        }
        driver_licenseOld = intent.getStringExtra("driver_license");
        if (driver_licenseOld == null) {
            driver_licenseOld = "";
        }
        driver_expiryOld = intent.getLongExtra("driver_expiry", 0l);
        driver_expiryNew = driver_expiryOld;

        sin_number.setText(sin_numberOld);
        driver_license.setText(driver_licenseOld);

        if (birthdayOld != 0l) {
            try {
                Date thisdaten = new Date();
                thisdaten.setTime(birthdayOld);
                String datet = format.format(thisdaten);
                birthday.setText(datet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (driver_expiryOld != 0l) {
            try {
                Date thisdaten = new Date();
                thisdaten.setTime(driver_expiryOld);
                String datet = format.format(thisdaten);
                driver_expiry.setText(datet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (birthday.getText().length() != 0) {
            showBirthday = false;
        }
        if (sin_number.getText().length() != 0) {
            showSin_number = false;
        }
        if (driver_license.getText().length() != 0) {
            showDriver_license = false;
        }
        if (driver_expiry.getText().length() != 0) {
            showDriver_expiry = false;
        }

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(1);
            }
        });

        image_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                choiseDate(1);
            }
        });

        driver_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(2);
            }
        });

        image_driver_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                choiseDate(2);
            }
        });

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (!sin_numberOld.equals(sin_number.getText().toString())
                    || !driver_licenseOld.equals(driver_license.getText().toString())
                    || !birthdayOld.equals(birthdayNew)
                    || !driver_expiryOld.equals(driver_expiryNew)) {
                    update();
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

        sin_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && sin_number.getText().length() == 0 && showSin_number) {
                    text_sin_number_hint.setVisibility(View.GONE);
                    text_sin_number.setVisibility(View.VISIBLE);
                    showSin_number = false;
                    text_sin_number.startAnimation(animationUp);
                } else if (!hasFocus && sin_number.getText().length() == 0 && !showSin_number) {
                    text_sin_number.setVisibility(View.INVISIBLE);
                    showSin_number = true;
                    text_sin_number_hint.setVisibility(View.VISIBLE);
                    text_sin_number_hint.startAnimation(animationDown);
                }
            }
        });

        driver_license.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && driver_license.getText().length() == 0 && showDriver_license) {
                    text_driver_license_hint.setVisibility(View.GONE);
                    text_driver_license.setVisibility(View.VISIBLE);
                    showDriver_license = false;
                    text_driver_license.startAnimation(animationUp);
                } else if (!hasFocus && driver_license.getText().length() == 0 && !showDriver_license) {
                    text_driver_license.setVisibility(View.INVISIBLE);
                    showDriver_license = true;
                    text_driver_license_hint.setVisibility(View.VISIBLE);
                    text_driver_license_hint.startAnimation(animationDown);
                }
            }
        });

        updateViews();

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(EditEmployeeActivity.this);
            }
        });

        UpdateBottom();

        View scrollView = findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

        if (readonly) {
            button_ok.setVisibility(View.GONE);
            birthday.setFocusable(false);
            sin_number.setFocusable(false);
            driver_license.setFocusable(false);
            driver_expiry.setFocusable(false);
            image_birthday.setClickable(false);
            image_driver_expiry.setClickable(false);
        }
    }

    /*
    Animation.AnimationListener animationErrorUpSin_numberListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_sin_number_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };
    */

    private void UpdateBottom() {
        if (Dealerpilothr.getInstance().getNeedUpdate()) {
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

        if (type == 1) {
            if (birthday.getText().length() == 0 && showBirthday) {
                text_birthday_hint.setVisibility(View.GONE);
                text_birthday.setVisibility(View.VISIBLE);
                showBirthday = false;
                text_birthday.startAnimation(animationUp);
            }
        } else {
            if (driver_expiry.getText().length() == 0 && showDriver_expiry) {
                text_driver_expiry_hint.setVisibility(View.GONE);
                text_driver_expiry.setVisibility(View.VISIBLE);
                showDriver_expiry = false;
                text_driver_expiry.startAnimation(animationUp);
            }
        }

        Date dateD = null;
        try {
            if (type == 1) {
                dateD = format.parse(birthday.getText().toString());
            } else {
                dateD = format.parse(driver_expiry.getText().toString());
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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener,
            DatePickerDialog.OnCancelListener {

        private EditEmployeeActivity act;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            //final Calendar c = Calendar.getInstance();
            //int year = c.get(Calendar.YEAR);
            //int month = c.get(Calendar.MONTH);
            //int day = c.get(Calendar.DAY_OF_MONTH);

            act = (EditEmployeeActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onCancel(DialogInterface dialog) {
            if (act.pressType == 1) {
                if (act.birthday.getText().length() == 0 && !act.showBirthday) {
                    act.text_birthday.setVisibility(View.INVISIBLE);
                    act.showBirthday = true;
                    act.text_birthday_hint.setVisibility(View.VISIBLE);
                    act.text_birthday_hint.startAnimation(act.animationDown);
                }
            } else {
                if (act.driver_expiry.getText().length() == 0 && !act.showDriver_expiry) {
                    act.text_driver_expiry.setVisibility(View.INVISIBLE);
                    act.showDriver_expiry = true;
                    act.text_driver_expiry_hint.setVisibility(View.VISIBLE);
                    act.text_driver_expiry_hint.startAnimation(act.animationDown);
                }
            }
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
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
                act.birthday.setText("" + mDay + "." + mMonth + "." + act.myYear);
            } else {
                act.driver_expiry.setText("" + mDay + "." + mMonth + "." + act.myYear);
            }

            String dateNewstr = "";
            if (act.pressType == 1) {
                dateNewstr = act.birthday.getText().toString();
            } else {
                dateNewstr = act.driver_expiry.getText().toString();
            }
            if (!dateNewstr.equals("")) {
                try {
                    Date dateD = format.parse(dateNewstr);
                    if (act.pressType == 1) {
                        act.birthdayNew = dateD.getTime();
                    } else {
                        act.driver_expiryNew = dateD.getTime();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            act.updateViews();
        }
    }

    private void update() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (!GeneralOperations.isSinNumberValid(sin_number.getText().toString(), true)) {
            isCheckSin_number = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            ProfileData data = new ProfileData();
            data.setBirthday(birthdayNew);
            data.setSINNumber(sin_number.getText().toString());
            data.setDriverLicenseNumber(driver_license.getText().toString());
            data.setDriverLicenseNumberExpire(driver_expiryNew);
            data.setProfileId(UserInfo.getUserInfo().getProfile().getProfileId());

            new ProfilesAdditionalInformationAddAction(EditEmployeeActivity.this, data).execute();
        }
    }

    private class TextWatch implements TextWatcher {
        private int type;

        public TextWatch(int type){
            super();
            this.type = type;
        }

        public void afterTextChanged(Editable s) {
            if (type == 1) {
                isCheckSin_number = true;
            }
            updateViews();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    private void updateViews() {
        if (isCheckSin_number && !GeneralOperations.isSinNumberValid(sin_number.getText().toString(), true)) {
            //text_sin_number_error.setVisibility(View.VISIBLE);
            sin_number.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_sin_number.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_sin_number_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_sin_number_error.setTextColor(ContextCompat.getColor(this, R.color.red));
            sin_number.setTextColor(ContextCompat.getColor(this, R.color.red));
            //if (!showErrorSin_number) {
            //    showErrorSin_number = true;
            //    text_sin_number_error.startAnimation(animationErrorDown);
            //}
        } else if (!sin_number.getText().toString().equals("")) {
            text_sin_number.setVisibility(View.VISIBLE);
            sin_number.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_sin_number.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_sin_number_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_sin_number_hint.setVisibility(View.GONE);
            text_sin_number_error.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            sin_number.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            //if (showErrorSin_number) {
            //    showErrorSin_number = false;
            //    text_sin_number_error.startAnimation(animationErrorUpSin_number);
            //}
        } else {
            sin_number.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_sin_number.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_sin_number_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_sin_number_error.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            sin_number.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
        }
        if (!birthday.getText().toString().equals("")) {
            text_birthday.setVisibility(View.VISIBLE);
            text_birthday_hint.setVisibility(View.GONE);
        }
        if (!driver_license.getText().toString().equals("")) {
            text_driver_license.setVisibility(View.VISIBLE);
            text_driver_license_hint.setVisibility(View.GONE);
        }
        if (!driver_expiry.getText().toString().equals("")) {
            text_driver_expiry.setVisibility(View.VISIBLE);
            text_driver_expiry_hint.setVisibility(View.GONE);
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void exit() {
        if (readonly) {
            finish();
            return;
        }
        if (!sin_numberOld.equals(sin_number.getText().toString())
                || !driver_licenseOld.equals(driver_license.getText().toString())
                || !birthdayOld.equals(birthdayNew)
                || !driver_expiryOld.equals(driver_expiryNew)) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestProfilesAdditionalInformationAdd(String result) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            Dealerpilothr.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
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
    protected void onPause() {
        super.onPause();
        text_birthday.clearAnimation();
        text_sin_number.clearAnimation();
        text_driver_license.clearAnimation();
        text_driver_expiry.clearAnimation();
        text_sin_number_error.clearAnimation();
        text_birthday_hint.clearAnimation();
        text_sin_number_hint.clearAnimation();
        text_driver_license_hint.clearAnimation();
        text_driver_expiry_hint.clearAnimation();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sin_number.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        unregisterReceiver(br);
    }
}
