package com.sapphire.activities.investigation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.api.InvestigationItemAddAction;
import com.sapphire.logic.InvestigationItemData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InvestigationItemActivity extends BaseActivity implements InvestigationItemAddAction.RequestInvestigationItemAdd{
    private String itemId = "";
    private String id = "";
    private ProgressDialog pd;
    private EditText name;
    private EditText description;
    private View text_name_error;
    private View text_name;
    private String nameOld = "";
    private String descriptionOld = "";
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private View image_date_group;
    private View text_date_error;
    private View text_date;
    private Long dateOld = 0l;
    private static SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    private int myYear = cal.get(Calendar.YEAR);
    private int myMonth = cal.get(Calendar.MONTH);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private int myHour = cal.get(Calendar.HOUR_OF_DAY);
    private int myMinute = cal.get(Calendar.MINUTE);
    private Long dateNew;
    private EditText time;
    private View image_time_group;
    private EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_investigation_item);
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
                saveChanged();
            }
        });

        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        text_name_error = findViewById(R.id.text_name_error);
        text_name = findViewById(R.id.text_name);
        image_date_group = findViewById(R.id.image_date_group);
        text_date_error = findViewById(R.id.text_date_error);
        text_date = findViewById(R.id.text_date);
        time = (EditText) findViewById(R.id.time);
        image_time_group = findViewById(R.id.image_time_group);
        date = (EditText) findViewById(R.id.date);

        format = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");

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

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseTime();
            }
        });

        image_time_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseTime();
            }
        });

        TextWatcher inputTextWatcher = new TextWatch();
        name.addTextChangedListener(inputTextWatcher);

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemId.equals("") || !nameOld.equals(name.getText().toString())
                        || !descriptionOld.equals(description.getText().toString())
                        || !dateOld.equals(dateNew)) {
                    saveChanged();
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

        Intent intent = getIntent();
        itemId = intent.getStringExtra("itemId");
        if (itemId == null) {
            itemId = "";
        }
        id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        if (!itemId.equals("")) {
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            name.setText(nameOld);
            description.setText(descriptionOld);
            dateOld = intent.getLongExtra("date", 0l);
            dateNew = dateOld;

            if (dateOld != 0l) {
                try {
                    Date thisdaten = new Date();
                    thisdaten.setTime(dateOld);
                    String datet = format.format(thisdaten);
                    date.setText(datet.substring(0,10));
                    time.setText(datet.substring(11));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        updateViews();
    }

    private void choiseTime() {
        hideSoftKeyboard();

        Date date = null;
        try {
            date = format.parse("01.01.1980 " + time.getText().toString() + ":00");
        } catch (ParseException e) {
            date = new Date();
            //date.setTime((long) mParphones.get(thisposition).get("datein"));
            e.printStackTrace();
        }

        cal.setTime(date);

        myHour = cal.get(Calendar.HOUR_OF_DAY);
        myMinute = cal.get(Calendar.MINUTE);

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            //final Calendar c = Calendar.getInstance();
            //int hour = c.get(Calendar.HOUR_OF_DAY);
            //int minute = c.get(Calendar.MINUTE);

            InvestigationItemActivity act = (InvestigationItemActivity) getActivity();

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, act.myHour, act.myMinute, false);
            //DateFormat.is24HourFormat(getActivity()));
            //.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            InvestigationItemActivity act = (InvestigationItemActivity) getActivity();

            String ampm = "AM";
            act.myHour = hourOfDay;
            act.myMinute = minute;
            if (hourOfDay == 0) {
                hourOfDay = 12;
            } else if (hourOfDay == 12) {
                ampm = "PM";
            } else if (hourOfDay >= 13) {
                hourOfDay = hourOfDay - 12;
                ampm = "PM";
            }

            String mHour = "";
            String mMinute = "";
            //if ((hourOfDay+"").length() == 1) {
            //    mHour = "0" + hourOfDay;
            //} else {
            mHour = hourOfDay + "";
            //}
            if ((act.myMinute+"").length() == 1) {
                mMinute = "0" + act.myMinute;
            } else {
                mMinute = act.myMinute+"";
            }

            act.time.setText("" + mHour + ":" + mMinute + " " + ampm);

            Date dateD = null;
            if (act.date.getText().toString().equals("")) {
                dateD = new Date();
                String datet = format.format(dateD);
                act.date.setText(datet.substring(0,10));
            }

            String dateNewstr = act.date.getText().toString() + " " + act.time.getText().toString();
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

            InvestigationItemActivity act = (InvestigationItemActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            InvestigationItemActivity act = (InvestigationItemActivity) getActivity();
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
            if (act.time.getText().toString().equals("")) {
                dateD = new Date();
                String datet = format.format(dateD);
                act.time.setText(datet.substring(11));
            }

            String dateNewstr = act.date.getText().toString() + " " + act.time.getText().toString();
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

    private void saveChanged() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("")) {
            allOk = false;
        }

        if (allOk) {
            pd.show();

            InvestigationItemData itemData = new InvestigationItemData();
            itemData.setName(name.getText().toString());
            itemData.setDescription(description.getText().toString());
            itemData.setInvestigationItemId(itemId);
            itemData.setInvestigationId(id);
            itemData.setDate(dateNew);

            new InvestigationItemAddAction(InvestigationItemActivity.this, itemData).execute();
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
            || !dateOld.equals(dateNew)) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestInvestigationItemAdd(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
