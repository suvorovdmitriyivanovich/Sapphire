package com.dealerpilothr.activities.safety;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.api.SafetyAddAction;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.SafetyData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SafetyActivity extends BaseActivity implements SafetyAddAction.RequestSafetyAdd,
        UpdateAction.RequestUpdate {
    private String id = "";
    private ProgressDialog pd;
    private EditText name;
    private EditText supplier;
    private EditText date;
    private EditText notes;
    private View image_date_group;
    private View text_name_error;
    private View text_supplier_error;
    private View text_date_error;
    private String nameOld = "";
    private String supplierOld = "";
    private String notesOld = "";
    private Long dateOld = 0l;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private boolean deleteItem = false;
    private static SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    private int myYear = cal.get(Calendar.YEAR);
    private int myMonth = cal.get(Calendar.MONTH);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private Long dateNew;
    private boolean isCheckName = false;
    private boolean isCheckSupplier = false;
    private boolean isCheckDate = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_name;
    private TextView text_supplier;
    private TextView text_date;
    private TextView text_notes;
    private Animation animationErrorDown;
    private Animation animationErrorUpName;
    private Animation animationErrorUpSupplier;
    private Animation animationErrorUpDate;
    private boolean showErrorName = false;
    private boolean showErrorSupplier = false;
    private boolean showErrorDate = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showName = true;
    private boolean showSupplier = true;
    private boolean showDate = true;
    private boolean showNotes = true;
    private TextView text_name_hint;
    private TextView text_supplier_hint;
    private TextView text_date_hint;
    private TextView text_notes_hint;
    private ImageView image_date;
    private String fileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_safety);

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
                updateSafety();
            }
        });

        name = (EditText) findViewById(R.id.name);
        supplier = (EditText) findViewById(R.id.supplier);
        date = (EditText) findViewById(R.id.date);
        image_date_group = findViewById(R.id.image_date_group);
        notes = (EditText) findViewById(R.id.notes);
        text_name_error = findViewById(R.id.text_name_error);
        text_supplier_error = findViewById(R.id.text_supplier_error);
        text_date_error = findViewById(R.id.text_date_error);
        text_name = (TextView) findViewById(R.id.text_name);
        text_supplier = (TextView) findViewById(R.id.text_supplier);
        text_date = (TextView) findViewById(R.id.text_date);
        text_notes = (TextView) findViewById(R.id.text_notes);
        text_name_hint = (TextView) findViewById(R.id.text_name_hint);
        text_supplier_hint = (TextView) findViewById(R.id.text_supplier_hint);
        text_date_hint = (TextView) findViewById(R.id.text_date_hint);
        text_notes_hint = (TextView) findViewById(R.id.text_notes_hint);
        image_date = (ImageView) findViewById(R.id.image_date);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpName = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpSupplier = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpDate = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpName.setAnimationListener(animationErrorUpNameListener);
        animationErrorUpSupplier.setAnimationListener(animationErrorUpSupplierListener);
        animationErrorUpDate.setAnimationListener(animationErrorUpDateListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        name.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        supplier.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(3);
        notes.addTextChangedListener(inputTextWatcher);

        format = new SimpleDateFormat("dd.MM.yyyy");

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        if (!id.equals("")) {
            readonly = intent.getBooleanExtra("readonly", false);
            nameOld = intent.getStringExtra("name");
            supplierOld = intent.getStringExtra("supplier");
            notesOld = intent.getStringExtra("notes");
            fileId = intent.getStringExtra("fileId");
            dateOld = intent.getLongExtra("date", 0l);
            dateNew = dateOld;
            name.setText(nameOld);
            supplier.setText(supplierOld);
            notes.setText(notesOld);

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

            if (name.getText().length() != 0) {
                showName = false;
            }
            if (supplier.getText().length() != 0) {
                showSupplier = false;
            }
            if (date.getText().length() != 0) {
                showDate = false;
            }
            if (notes.getText().length() != 0) {
                showNotes = false;
            }
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate();
            }
        });

        image_date_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                choiseDate();
            }
        });

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (id.equals("") || !nameOld.equals(name.getText().toString())
                        || !supplierOld.equals(supplier.getText().toString())
                        || !notesOld.equals(notes.getText().toString())
                        || !dateOld.equals(dateNew)) {
                    updateSafety();
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

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && name.getText().length() == 0 && showName) {
                    text_name_hint.setVisibility(View.GONE);
                    text_name.setVisibility(View.VISIBLE);
                    showName = false;
                    text_name.startAnimation(animationUp);
                } else if (!hasFocus && name.getText().length() == 0 && !showName) {
                    text_name.setVisibility(View.INVISIBLE);
                    showName = true;
                    text_name_hint.setVisibility(View.VISIBLE);
                    text_name_hint.startAnimation(animationDown);
                }
            }
        });

        supplier.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && supplier.getText().length() == 0 && showSupplier) {
                    text_supplier_hint.setVisibility(View.GONE);
                    text_supplier.setVisibility(View.VISIBLE);
                    showSupplier = false;
                    text_supplier.startAnimation(animationUp);
                } else if (!hasFocus && supplier.getText().length() == 0 && !showSupplier) {
                    text_supplier.setVisibility(View.INVISIBLE);
                    showSupplier = true;
                    text_supplier_hint.setVisibility(View.VISIBLE);
                    text_supplier_hint.startAnimation(animationDown);
                }
            }
        });

        notes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && notes.getText().length() == 0 && showNotes) {
                    text_notes_hint.setVisibility(View.GONE);
                    text_notes.setVisibility(View.VISIBLE);
                    showNotes = false;
                    text_notes.startAnimation(animationUp);
                } else if (!hasFocus && notes.getText().length() == 0 && !showNotes) {
                    text_notes.setVisibility(View.INVISIBLE);
                    showNotes = true;
                    text_notes_hint.setVisibility(View.VISIBLE);
                    text_notes_hint.startAnimation(animationDown);
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

                new UpdateAction(SafetyActivity.this);
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
            name.setFocusable(false);
            supplier.setFocusable(false);
            notes.setFocusable(false);
            date.setFocusable(false);
            image_date_group.setClickable(false);
        }
    }

    Animation.AnimationListener animationErrorUpNameListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_name_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    Animation.AnimationListener animationErrorUpSupplierListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_supplier_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    Animation.AnimationListener animationErrorUpDateListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_date_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    private void UpdateBottom() {
        if (Dealerpilothr.getInstance().getNeedUpdate()) {
            par_nointernet_group.height = GetPixelFromDips(56);
        } else {
            par_nointernet_group.height = 0;
        }
        nointernet_group.setLayoutParams(par_nointernet_group);
        nointernet_group.requestLayout();
    }

    private void choiseDate() {
        hideSoftKeyboard();

        if (date.getText().length() == 0 && showDate) {
            text_date_hint.setVisibility(View.GONE);
            text_date.setVisibility(View.VISIBLE);
            showDate = false;
            text_date.startAnimation(animationUp);
        }

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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener,
                                                                             DatePickerDialog.OnCancelListener {

        private SafetyActivity act;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            //final Calendar c = Calendar.getInstance();
            //int year = c.get(Calendar.YEAR);
            //int month = c.get(Calendar.MONTH);
            //int day = c.get(Calendar.DAY_OF_MONTH);

            act = (SafetyActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onCancel(DialogInterface dialog) {
            if (act.date.getText().length() == 0 && !act.showDate) {
                act.text_date.setVisibility(View.INVISIBLE);
                act.showDate = true;
                act.text_date_hint.setVisibility(View.VISIBLE);
                act.text_date_hint.startAnimation(act.animationDown);
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

            act.isCheckDate = true;

            act.updateViews();
        }
    }

    private void updateSafety() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("")
            || supplier.getText().toString().equals("")
            || date.getText().toString().equals("")) {
            isCheckName = true;
            isCheckSupplier = true;
            isCheckDate = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            new SafetyAddAction(SafetyActivity.this, id, name.getText().toString(), supplier.getText().toString(), notes.getText().toString(), dateNew, fileId).execute();
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
                isCheckName = true;
            } else if (type == 2) {
                isCheckSupplier = true;
            }
            updateViews();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    private void updateViews() {
        if (isCheckName && name.getText().toString().equals("")) {
            text_name_error.setVisibility(View.VISIBLE);
            name.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_name.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_name_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorName) {
                showErrorName = true;
                text_name_error.startAnimation(animationErrorDown);
            }
        } else if (!name.getText().toString().equals("")) {
            text_name.setVisibility(View.VISIBLE);
            name.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_name.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_name_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_name_hint.setVisibility(View.GONE);
            if (showErrorName) {
                showErrorName = false;
                text_name_error.startAnimation(animationErrorUpName);
            }
        }
        if (isCheckSupplier && supplier.getText().toString().equals("")) {
            text_supplier_error.setVisibility(View.VISIBLE);
            supplier.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_supplier.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_supplier_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorSupplier) {
                showErrorSupplier = true;
                text_supplier_error.startAnimation(animationErrorDown);
            }
        } else if (!supplier.getText().toString().equals("")) {
            text_supplier.setVisibility(View.VISIBLE);
            supplier.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_supplier.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_supplier_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_supplier_hint.setVisibility(View.GONE);
            if (showErrorSupplier) {
                showErrorSupplier = false;
                text_supplier_error.startAnimation(animationErrorUpSupplier);
            }
        }
        if (isCheckDate && date.getText().toString().equals("")) {
            text_date_error.setVisibility(View.VISIBLE);
            date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            image_date.setImageResource(R.drawable.date_red);
            text_date.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_date_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorDate) {
                showErrorDate = true;
                text_date_error.startAnimation(animationErrorDown);
            }
        } else if (!date.getText().toString().equals("")) {
            text_date.setVisibility(View.VISIBLE);
            date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            image_date.setImageResource(R.drawable.date);
            text_date.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_date_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_date_hint.setVisibility(View.GONE);
            if (showErrorDate) {
                showErrorDate = false;
                text_date_error.startAnimation(animationErrorUpDate);
            }
        }
        if (!notes.getText().toString().equals("")) {
            text_notes.setVisibility(View.VISIBLE);
            text_notes_hint.setVisibility(View.GONE);
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
        if (!nameOld.equals(name.getText().toString())
                || !supplierOld.equals(supplier.getText().toString())
                || !notesOld.equals(notes.getText().toString())
                || !dateOld.equals(dateNew)) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestSafetyAdd(String result, SafetyData data) {
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
            nameOld = name.getText().toString();
            supplierOld = supplier.getText().toString();
            notesOld = notes.getText().toString();
            dateOld = data.getRenewalDate();
            dateNew = dateOld;
            id = data.getSafetyDataSheetId();

            pd.hide();
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
        text_name.clearAnimation();
        text_supplier.clearAnimation();
        text_date.clearAnimation();
        text_notes.clearAnimation();
        text_name_error.clearAnimation();
        text_supplier_error.clearAnimation();
        text_date_error.clearAnimation();
        text_name_hint.clearAnimation();
        text_supplier_hint.clearAnimation();
        text_date_hint.clearAnimation();
        text_notes_hint.clearAnimation();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        name.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        supplier.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        unregisterReceiver(br);
    }
}
