package com.sapphire.activities.investigation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.FilesActivity;
import com.sapphire.adapters.InvestigationItemsAdapter;
import com.sapphire.api.GetInvestigationAction;
import com.sapphire.api.InvestigationAddAction;
import com.sapphire.api.InvestigationItemDeleteAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.InvestigationData;
import com.sapphire.logic.InvestigationItemData;
import com.sapphire.logic.UserInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InvestigationActivity extends BaseActivity implements GetInvestigationAction.RequestInvestigation,
                                                                   InvestigationItemsAdapter.OnRootInvestigationClickListener,
                                                                   InvestigationItemsAdapter.OnOpenInvestigationClickListener,
                                                                   InvestigationItemsAdapter.OnDeleteInvestigationClickListener,
                                                                   InvestigationItemsAdapter.OnFilesInvestigationClickListener,
                                                                   InvestigationItemDeleteAction.RequestInvestigationItemDelete,
                                                                   InvestigationAddAction.RequestInvestigationAdd{
    private String id = "";
    private ProgressDialog pd;
    private ArrayList<InvestigationItemData> datas = new ArrayList<InvestigationItemData>();
    private RecyclerView itemlist;
    private InvestigationItemsAdapter adapter;
    private EditText name;
    private EditText date;
    private EditText description;
    private View image_date_group;
    private View text_name_error;
    private View text_date_error;
    private View text_name;
    private View text_date;
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
    private int myYear = cal.get(Calendar.YEAR);
    private int myMonth = cal.get(Calendar.MONTH);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private int myHour = cal.get(Calendar.HOUR_OF_DAY);
    private int myMinute = cal.get(Calendar.MINUTE);
    private Long dateNew;
    private InvestigationData data = new InvestigationData();
    private EditText time;
    private View image_time_group;
    private boolean me = false;
    private View text_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_investigation);

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

                    new InvestigationItemDeleteAction(InvestigationActivity.this, datas.get(currentPosition).getInvestigationItemId()).execute();
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
        time = (EditText) findViewById(R.id.time);
        image_time_group = findViewById(R.id.image_time_group);

        format = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");

        Intent intent = getIntent();
        me = intent.getBooleanExtra("me", false);
        id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        if (!id.equals("")) {
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            dateOld = intent.getLongExtra("date", 0l);
            dateNew = dateOld;
            name.setText(nameOld);
            description.setText(descriptionOld);

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
                hideSoftKeyboard();

                if (id.equals("") || !nameOld.equals(name.getText().toString())
                        || !descriptionOld.equals(description.getText().toString())
                        || !dateOld.equals(dateNew)) {
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
                        || !dateOld.equals(dateNew)) {
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
        itemlist.setLayoutManager(new LinearLayoutManager(InvestigationActivity.this));

        adapter = new InvestigationItemsAdapter(this);
        itemlist.setAdapter(adapter);

        updateViews();

        text_no = findViewById(R.id.text_no);

        updateVisibility();
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

            InvestigationActivity act = (InvestigationActivity) getActivity();

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, act.myHour, act.myMinute, false);
                    //DateFormat.is24HourFormat(getActivity()));
            //.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            InvestigationActivity act = (InvestigationActivity) getActivity();

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

            InvestigationActivity act = (InvestigationActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            InvestigationActivity act = (InvestigationActivity) getActivity();
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

    private void updateWorkplaceInspection(int type) {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("") || date.getText().toString().equals("")) {
            allOk = false;
        }

        if (allOk) {
            pd.show();

            pressType = type;

            new InvestigationAddAction(InvestigationActivity.this, id, name.getText().toString(), description.getText().toString(), dateNew).execute();
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
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    private void addItem() {
        hideSoftKeyboard();
        Intent intent = new Intent(InvestigationActivity.this, InvestigationItemActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onRequestInvestigation(String result, ArrayList<InvestigationItemData> datas) {
        if (!result.equals("OK")) {
            pressType = 0;
            updateVisibility();
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            this.datas = datas;
            adapter.setListArray(datas);
            updateVisibility();
            pd.hide();
            if (pressType == 2) {
                pressType = 0;
                openItem();
            }
        }
    }

    @Override
    public void onRootInvestigationClick(int position) {
        hideSoftKeyboard();
    }

    @Override
    public void onDeleteInvestigationClick(int position) {
        hideSoftKeyboard();

        currentPosition = position;
        deleteItem = true;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onOpenInvestigationClick(int position) {
        hideSoftKeyboard();
        currentPosition = position;
        if (id.equals("")) {
            updateWorkplaceInspection(2);
        } else {
            openItem();
        }
    }

    private void openItem() {
        Intent intent = new Intent(InvestigationActivity.this, InvestigationItemActivity.class);
        InvestigationItemData data = datas.get(currentPosition);
        intent.putExtra("name", data.getName());
        intent.putExtra("description", data.getDescription());
        intent.putExtra("itemId", data.getInvestigationItemId());
        intent.putExtra("id", data.getInvestigationId());
        intent.putExtra("date", data.getDate());
        startActivity(intent);
    }

    @Override
    public void onRequestInvestigationItemDelete(String result) {
        dialog_confirm.dismiss();
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            new GetInvestigationAction(InvestigationActivity.this, id).execute();
        }
    }

    @Override
    public void onRequestInvestigationAdd(String result, InvestigationData data) {
        if (!result.equals("OK")) {
            pd.hide();

            pressType = 0;
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            this.data = data;
            nameOld = name.getText().toString();
            descriptionOld = description.getText().toString();
            dateOld = data.getDate();
            dateNew = dateOld;
            if (id.equals("") && datas.size() > 0) {
                id = data.getInvestigationId();

                InvestigationItemData itemData = new InvestigationItemData();
                itemData.setName(datas.get(0).getName());
                itemData.setDescription(datas.get(0).getDescription());
                itemData.setInvestigationId(id);
                itemData.setDate(datas.get(0).getDate());

                //new WorkplaceInspectionItemAddAction(InvestigationActivity.this, itemData).execute();
            } else {
                id = data.getInvestigationId();

                pd.hide();
                requestWorkplaceInspectionAddData();
            }
        }
    }

    private void requestWorkplaceInspectionAddData() {
        if (pressType == 1) {
            pressType = 0;
            addItem();
        } else if (pressType == 2) {
            pd.show();
            new GetInvestigationAction(InvestigationActivity.this, id).execute();
        } else {
            finish();
        }
    }

    @Override
    public void onFilesInvestigationClick(int position) {
        Intent intent = new Intent(InvestigationActivity.this, FilesActivity.class);
        InvestigationItemData data = datas.get(position);
        intent.putExtra("name", data.getName());
        intent.putExtra("id", data.getInvestigationItemId());
        intent.putExtra("url", Environment.InvestigationsItemsFilesURL);
        intent.putExtra("nameField", "InvestigationItemId");
        UserInfo.getUserInfo().setFileDatas(data.getFiles());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!id.equals("")) {
            pd.show();
            new GetInvestigationAction(InvestigationActivity.this, id).execute();
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
