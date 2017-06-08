package com.dealerpilothr.activities.hrdetails;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.R;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.api.ProfilesWorkAdditionalInformationAddAction;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.ProfileData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditWorkAdditionalActivity extends BaseActivity implements ProfilesWorkAdditionalInformationAddAction.RequestProfilesWorkAdditionalInformationAdd,
                                                                        UpdateAction.RequestUpdate {
    private ProgressDialog pd;
    private EditText vsr_number;
    private EditText vsr_expiry;
    private EditText tech_license;
    private EditText tech_expiry;
    private EditText uniform_description;
    private CheckBox uniform_allowance;
    private EditText uniform_amount;
    private EditText uniform_renewal;
    private EditText work_number;
    private EditText work_expiry;
    private String vsr_numberOld = "";
    private Long vsr_expiryOld = 0l;
    private String tech_licenseOld = "";
    private Long tech_expiryOld = 0l;
    private String uniform_descriptionOld = "";
    private boolean uniform_allowanceOld = false;
    private Double uniform_amountOld = 0d;
    private Long uniform_renewalOld = 0l;
    private String work_numberOld = "";
    private Long work_expiryOld = 0l;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_vsr_number;
    private TextView text_vsr_expiry;
    private TextView text_tech_license;
    private TextView text_tech_expiry;
    private TextView text_uniform_description;
    private TextView text_uniform_amount;
    private TextView text_uniform_renewal;
    private TextView text_work_number;
    private TextView text_work_expiry;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showVsr_number = true;
    private boolean showVsr_expiry = true;
    private boolean showTech_license = true;
    private boolean showTech_expiry = true;
    private boolean showUniform_description = true;
    private boolean showUniform_amount = true;
    private boolean showUniform_renewal = true;
    private boolean showWork_number = true;
    private boolean showWork_expiry = true;
    private TextView text_vsr_number_hint;
    private TextView text_vsr_expiry_hint;
    private TextView text_tech_license_hint;
    private TextView text_tech_expiry_hint;
    private TextView text_uniform_description_hint;
    private TextView text_uniform_amount_hint;
    private TextView text_uniform_renewal_hint;
    private TextView text_work_number_hint;
    private TextView text_work_expiry_hint;
    private Long vsr_expiryNew = 0l;
    private Long tech_expiryNew = 0l;
    private Long uniform_renewalNew = 0l;
    private Long work_expiryNew = 0l;
    private View image_vsr_expiry;
    private View image_tech_expiry;
    private View image_uniform_renewal;
    private View image_work_expiry;
    private static SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    private int myYear = cal.get(Calendar.YEAR);
    private int myMonth = cal.get(Calendar.MONTH);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private int pressType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_work_additional);

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

        vsr_number = (EditText) findViewById(R.id.vsr_number);
        vsr_expiry = (EditText) findViewById(R.id.vsr_expiry);
        tech_license = (EditText) findViewById(R.id.tech_license);
        tech_expiry = (EditText) findViewById(R.id.tech_expiry);
        uniform_description = (EditText) findViewById(R.id.uniform_description);
        uniform_allowance = (CheckBox) findViewById(R.id.uniform_allowance);
        uniform_amount = (EditText) findViewById(R.id.uniform_amount);
        uniform_renewal = (EditText) findViewById(R.id.uniform_renewal);
        work_number = (EditText) findViewById(R.id.work_number);
        work_expiry = (EditText) findViewById(R.id.work_expiry);
        image_vsr_expiry = findViewById(R.id.image_vsr_expiry);
        image_tech_expiry = findViewById(R.id.image_tech_expiry);
        image_uniform_renewal = findViewById(R.id.image_uniform_renewal);
        image_work_expiry = findViewById(R.id.image_work_expiry);
        text_vsr_number = (TextView) findViewById(R.id.text_vsr_number);
        text_vsr_expiry = (TextView) findViewById(R.id.text_vsr_expiry);
        text_tech_license = (TextView) findViewById(R.id.text_tech_license);
        text_tech_expiry = (TextView) findViewById(R.id.text_tech_expiry);
        text_uniform_description = (TextView) findViewById(R.id.text_uniform_description);
        text_uniform_amount = (TextView) findViewById(R.id.text_uniform_amount);
        text_uniform_renewal = (TextView) findViewById(R.id.text_uniform_renewal);
        text_work_number = (TextView) findViewById(R.id.text_work_number);
        text_work_expiry = (TextView) findViewById(R.id.text_work_expiry);
        text_vsr_number_hint = (TextView) findViewById(R.id.text_vsr_number_hint);
        text_vsr_expiry_hint = (TextView) findViewById(R.id.text_vsr_expiry_hint);
        text_tech_license_hint = (TextView) findViewById(R.id.text_tech_license_hint);
        text_tech_expiry_hint = (TextView) findViewById(R.id.text_tech_expiry_hint);
        text_uniform_description_hint = (TextView) findViewById(R.id.text_uniform_description_hint);
        text_uniform_amount_hint = (TextView) findViewById(R.id.text_uniform_amount_hint);
        text_uniform_renewal_hint = (TextView) findViewById(R.id.text_uniform_renewal_hint);
        text_work_number_hint = (TextView) findViewById(R.id.text_work_number_hint);
        text_work_expiry_hint = (TextView) findViewById(R.id.text_work_expiry_hint);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch();
        vsr_number.addTextChangedListener(inputTextWatcher);
        tech_license.addTextChangedListener(inputTextWatcher);
        uniform_description.addTextChangedListener(inputTextWatcher);
        uniform_amount.addTextChangedListener(inputTextWatcher);
        work_number.addTextChangedListener(inputTextWatcher);
        work_expiry.addTextChangedListener(inputTextWatcher);

        format = new SimpleDateFormat("dd.MM.yyyy");

        Intent intent = getIntent();

        readonly = intent.getBooleanExtra("readonly", false);
        vsr_numberOld = intent.getStringExtra("vsr_number");
        if (vsr_numberOld == null) {
            vsr_numberOld = "";
        }
        vsr_expiryOld = intent.getLongExtra("vsr_expiry", 0l);
        vsr_expiryNew = vsr_expiryOld;
        tech_licenseOld = intent.getStringExtra("tech_license");
        if (tech_licenseOld == null) {
            tech_licenseOld = "";
        }
        tech_expiryOld = intent.getLongExtra("tech_expiry", 0l);
        tech_expiryNew = tech_expiryOld;
        uniform_descriptionOld = intent.getStringExtra("uniform_description");
        if (uniform_descriptionOld == null) {
            uniform_descriptionOld = "";
        }
        uniform_allowanceOld = intent.getBooleanExtra("uniform_allowance", false);
        uniform_amountOld = intent.getDoubleExtra("uniform_amount", 0d);
        uniform_renewalOld = intent.getLongExtra("uniform_renewal", 0l);
        uniform_renewalNew = uniform_renewalOld;
        work_numberOld = intent.getStringExtra("work_number");
        if (work_numberOld == null) {
            work_numberOld = "";
        }
        work_expiryOld = intent.getLongExtra("work_expiry", 0l);
        work_expiryNew = work_expiryOld;

        vsr_number.setText(vsr_numberOld);
        tech_license.setText(tech_licenseOld);
        uniform_description.setText(uniform_descriptionOld);
        uniform_allowance.setChecked(uniform_allowanceOld);
        uniform_amount.setText(String.valueOf(uniform_amountOld));
        work_number.setText(work_numberOld);

        if (vsr_expiryOld != 0l) {
            try {
                Date thisdaten = new Date();
                thisdaten.setTime(vsr_expiryOld);
                String datet = format.format(thisdaten);
                vsr_expiry.setText(datet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (tech_expiryOld != 0l) {
            try {
                Date thisdaten = new Date();
                thisdaten.setTime(tech_expiryOld);
                String datet = format.format(thisdaten);
                tech_expiry.setText(datet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (uniform_renewalOld != 0l) {
            try {
                Date thisdaten = new Date();
                thisdaten.setTime(uniform_renewalOld);
                String datet = format.format(thisdaten);
                uniform_renewal.setText(datet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (work_expiryOld != 0l) {
            try {
                Date thisdaten = new Date();
                thisdaten.setTime(work_expiryOld);
                String datet = format.format(thisdaten);
                work_expiry.setText(datet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (vsr_number.getText().length() != 0) {
            showVsr_number = false;
        }
        if (vsr_expiry.getText().length() != 0) {
            showVsr_expiry = false;
        }
        if (tech_license.getText().length() != 0) {
            showTech_license = false;
        }
        if (tech_expiry.getText().length() != 0) {
            showTech_expiry = false;
        }
        if (uniform_description.getText().length() != 0) {
            showUniform_description = false;
        }
        if (uniform_amount.getText().length() != 0) {
            showUniform_amount = false;
        }
        if (uniform_renewal.getText().length() != 0) {
            showUniform_renewal = false;
        }
        if (work_number.getText().length() != 0) {
            showWork_number = false;
        }
        if (work_expiry.getText().length() != 0) {
            showWork_expiry = false;
        }

        vsr_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(1);
            }
        });

        image_vsr_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                choiseDate(1);
            }
        });

        tech_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(2);
            }
        });

        image_tech_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                choiseDate(2);
            }
        });

        uniform_renewal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(3);
            }
        });

        image_uniform_renewal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                choiseDate(3);
            }
        });

        work_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(4);
            }
        });

        image_work_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                choiseDate(4);
            }
        });

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                Double uniformAmount = 0d;
                try {
                    uniformAmount = Double.valueOf(uniform_amount.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!vsr_numberOld.equals(vsr_number.getText().toString())
                    || !vsr_expiryOld.equals(vsr_expiryNew)
                    || !tech_licenseOld.equals(tech_license.getText().toString())
                    || !tech_expiryOld.equals(tech_expiryNew)
                    || !uniform_descriptionOld.equals(uniform_description.getText().toString())
                    || uniform_allowanceOld != uniform_allowance.isChecked()
                    || !uniform_amountOld.equals(uniformAmount)
                    || !uniform_renewalOld.equals(uniform_renewalNew)
                    || !work_numberOld.equals(work_number.getText().toString())
                    || !work_expiryOld.equals(work_expiryNew)) {
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

        vsr_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && vsr_number.getText().length() == 0 && showVsr_number) {
                    text_vsr_number_hint.setVisibility(View.GONE);
                    text_vsr_number.setVisibility(View.VISIBLE);
                    showVsr_number = false;
                    text_vsr_number.startAnimation(animationUp);
                } else if (!hasFocus && vsr_number.getText().length() == 0 && !showVsr_number) {
                    text_vsr_number.setVisibility(View.INVISIBLE);
                    showVsr_number = true;
                    text_vsr_number_hint.setVisibility(View.VISIBLE);
                    text_vsr_number_hint.startAnimation(animationDown);
                }
            }
        });

        tech_license.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && tech_license.getText().length() == 0 && showTech_license) {
                    text_tech_license_hint.setVisibility(View.GONE);
                    text_tech_license.setVisibility(View.VISIBLE);
                    showTech_license = false;
                    text_tech_license.startAnimation(animationUp);
                } else if (!hasFocus && tech_license.getText().length() == 0 && !showTech_license) {
                    text_tech_license.setVisibility(View.INVISIBLE);
                    showTech_license = true;
                    text_tech_license_hint.setVisibility(View.VISIBLE);
                    text_tech_license_hint.startAnimation(animationDown);
                }
            }
        });

        uniform_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && uniform_description.getText().length() == 0 && showUniform_description) {
                    text_uniform_description_hint.setVisibility(View.GONE);
                    text_uniform_description.setVisibility(View.VISIBLE);
                    showUniform_description = false;
                    text_uniform_description.startAnimation(animationUp);
                } else if (!hasFocus && uniform_description.getText().length() == 0 && !showUniform_description) {
                    text_uniform_description.setVisibility(View.INVISIBLE);
                    showUniform_description = true;
                    text_uniform_description_hint.setVisibility(View.VISIBLE);
                    text_uniform_description_hint.startAnimation(animationDown);
                }
            }
        });

        uniform_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && uniform_amount.getText().length() == 0 && showUniform_amount) {
                    text_uniform_amount_hint.setVisibility(View.GONE);
                    text_uniform_amount.setVisibility(View.VISIBLE);
                    showUniform_amount = false;
                    text_uniform_amount.startAnimation(animationUp);
                } else if (!hasFocus && uniform_amount.getText().length() == 0 && !showUniform_amount) {
                    text_uniform_amount.setVisibility(View.INVISIBLE);
                    showUniform_amount = true;
                    text_uniform_amount_hint.setVisibility(View.VISIBLE);
                    text_uniform_amount_hint.startAnimation(animationDown);
                }
            }
        });

        work_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && work_number.getText().length() == 0 && showWork_number) {
                    text_work_number_hint.setVisibility(View.GONE);
                    text_work_number.setVisibility(View.VISIBLE);
                    showWork_number = false;
                    text_work_number.startAnimation(animationUp);
                } else if (!hasFocus && work_number.getText().length() == 0 && !showWork_number) {
                    text_work_number.setVisibility(View.INVISIBLE);
                    showWork_number = true;
                    text_work_number_hint.setVisibility(View.VISIBLE);
                    text_work_number_hint.startAnimation(animationDown);
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

                new UpdateAction(EditWorkAdditionalActivity.this);
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
            vsr_number.setFocusable(false);
            vsr_expiry.setFocusable(false);
            tech_license.setFocusable(false);
            tech_expiry.setFocusable(false);
            uniform_description.setFocusable(false);
            uniform_amount.setFocusable(false);
            uniform_renewal.setFocusable(false);
            work_number.setFocusable(false);
            work_expiry.setFocusable(false);
            image_vsr_expiry.setClickable(false);
            image_tech_expiry.setClickable(false);
            image_uniform_renewal.setClickable(false);
            image_work_expiry.setClickable(false);
        }
    }

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
            if (vsr_expiry.getText().length() == 0 && showVsr_expiry) {
                text_vsr_expiry_hint.setVisibility(View.GONE);
                text_vsr_expiry.setVisibility(View.VISIBLE);
                showVsr_expiry = false;
                text_vsr_expiry.startAnimation(animationUp);
            }
        } else if (type == 2) {
            if (tech_expiry.getText().length() == 0 && showTech_expiry) {
                text_tech_expiry_hint.setVisibility(View.GONE);
                text_tech_expiry.setVisibility(View.VISIBLE);
                showTech_expiry = false;
                text_tech_expiry.startAnimation(animationUp);
            }
        } else if (type == 3) {
            if (uniform_renewal.getText().length() == 0 && showUniform_renewal) {
                text_uniform_renewal_hint.setVisibility(View.GONE);
                text_uniform_renewal.setVisibility(View.VISIBLE);
                showUniform_renewal = false;
                text_uniform_renewal.startAnimation(animationUp);
            }
        } else if (type == 4) {
            if (work_expiry.getText().length() == 0 && showWork_expiry) {
                text_work_expiry_hint.setVisibility(View.GONE);
                text_work_expiry.setVisibility(View.VISIBLE);
                showWork_expiry = false;
                text_work_expiry.startAnimation(animationUp);
            }
        }

        Date dateD = null;
        try {
            if (type == 1) {
                dateD = format.parse(vsr_expiry.getText().toString());
            } else if (type == 2) {
                dateD = format.parse(tech_expiry.getText().toString());
            } else if (type == 3) {
                dateD = format.parse(uniform_renewal.getText().toString());
            } else if (type == 4) {
                dateD = format.parse(work_expiry.getText().toString());
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

        private EditWorkAdditionalActivity act;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            //final Calendar c = Calendar.getInstance();
            //int year = c.get(Calendar.YEAR);
            //int month = c.get(Calendar.MONTH);
            //int day = c.get(Calendar.DAY_OF_MONTH);

            act = (EditWorkAdditionalActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onCancel(DialogInterface dialog) {
            if (act.pressType == 1) {
                if (act.vsr_expiry.getText().length() == 0 && !act.showVsr_expiry) {
                    act.text_vsr_expiry.setVisibility(View.INVISIBLE);
                    act.showVsr_expiry = true;
                    act.text_vsr_expiry_hint.setVisibility(View.VISIBLE);
                    act.text_vsr_expiry_hint.startAnimation(act.animationDown);
                }
            } else if (act.pressType == 2) {
                if (act.tech_expiry.getText().length() == 0 && !act.showTech_expiry) {
                    act.text_tech_expiry.setVisibility(View.INVISIBLE);
                    act.showTech_expiry = true;
                    act.text_tech_expiry_hint.setVisibility(View.VISIBLE);
                    act.text_tech_expiry_hint.startAnimation(act.animationDown);
                }
            } else if (act.pressType == 3) {
                if (act.uniform_renewal.getText().length() == 0 && !act.showUniform_renewal) {
                    act.text_uniform_renewal.setVisibility(View.INVISIBLE);
                    act.showUniform_renewal = true;
                    act.text_uniform_renewal_hint.setVisibility(View.VISIBLE);
                    act.text_uniform_renewal_hint.startAnimation(act.animationDown);
                }
            } else if (act.pressType == 4) {
                if (act.work_expiry.getText().length() == 0 && !act.showWork_expiry) {
                    act.text_work_expiry.setVisibility(View.INVISIBLE);
                    act.showWork_expiry = true;
                    act.text_work_expiry_hint.setVisibility(View.VISIBLE);
                    act.text_work_expiry_hint.startAnimation(act.animationDown);
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
                act.vsr_expiry.setText("" + mDay + "." + mMonth + "." + act.myYear);
            } else if (act.pressType == 2) {
                act.tech_expiry.setText("" + mDay + "." + mMonth + "." + act.myYear);
            } else if (act.pressType == 3) {
                act.uniform_renewal.setText("" + mDay + "." + mMonth + "." + act.myYear);
            } else if (act.pressType == 4) {
                act.work_expiry.setText("" + mDay + "." + mMonth + "." + act.myYear);
            }

            String dateNewstr = "";
            if (act.pressType == 1) {
                dateNewstr = act.vsr_expiry.getText().toString();
            } else if (act.pressType == 2) {
                dateNewstr = act.tech_expiry.getText().toString();
            } else if (act.pressType == 3) {
                dateNewstr = act.uniform_renewal.getText().toString();
            } else if (act.pressType == 4) {
                dateNewstr = act.work_expiry.getText().toString();
            }
            if (!dateNewstr.equals("")) {
                try {
                    Date dateD = format.parse(dateNewstr);
                    if (act.pressType == 1) {
                        act.vsr_expiryNew = dateD.getTime();
                    } else if (act.pressType == 2) {
                        act.tech_expiryNew = dateD.getTime();
                    } else if (act.pressType == 3) {
                        act.uniform_renewalNew = dateD.getTime();
                    } else if (act.pressType == 4) {
                        act.work_expiryNew = dateD.getTime();
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

        pd.show();

        ProfileData data = new ProfileData();
        data.setVSRNumber(vsr_number.getText().toString());
        data.setVSRNumberExpire(vsr_expiryNew);
        data.setTechLicenseNumber(tech_license.getText().toString());
        data.setTechLicenseNumberExpire(tech_expiryNew);
        data.setUniformDescription(uniform_description.getText().toString());
        data.setUniformAllowance(uniform_allowance.isChecked());
        Double uniformAmount = 0d;
        try {
            uniformAmount = Double.valueOf(uniform_amount.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.setUniformAllowanceAmount(uniformAmount);
        data.setUniformRenewalDate(uniform_renewalNew);
        data.setWorkPermitNumber(work_number.getText().toString());
        data.setWorkPermitNumberExpire(work_expiryNew);
        data.setProfileId(UserInfo.getUserInfo().getProfile().getProfileId());

        new ProfilesWorkAdditionalInformationAddAction(EditWorkAdditionalActivity.this, data).execute();
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
        if (!vsr_number.getText().toString().equals("")) {
            text_vsr_number.setVisibility(View.VISIBLE);
            text_vsr_number_hint.setVisibility(View.GONE);
        }
        if (!vsr_expiry.getText().toString().equals("")) {
            text_vsr_expiry.setVisibility(View.VISIBLE);
            text_vsr_expiry_hint.setVisibility(View.GONE);
        }
        if (!tech_license.getText().toString().equals("")) {
            text_tech_license.setVisibility(View.VISIBLE);
            text_tech_license_hint.setVisibility(View.GONE);
        }
        if (!tech_expiry.getText().toString().equals("")) {
            text_tech_expiry.setVisibility(View.VISIBLE);
            text_tech_expiry_hint.setVisibility(View.GONE);
        }
        if (!uniform_description.getText().toString().equals("")) {
            text_uniform_description.setVisibility(View.VISIBLE);
            text_uniform_description_hint.setVisibility(View.GONE);
        }
        if (!uniform_amount.getText().toString().equals("")) {
            text_uniform_amount.setVisibility(View.VISIBLE);
            text_uniform_amount_hint.setVisibility(View.GONE);
        }
        if (!uniform_renewal.getText().toString().equals("")) {
            text_uniform_renewal.setVisibility(View.VISIBLE);
            text_uniform_renewal_hint.setVisibility(View.GONE);
        }
        if (!work_number.getText().toString().equals("")) {
            text_work_number.setVisibility(View.VISIBLE);
            text_work_number_hint.setVisibility(View.GONE);
        }
        if (!work_expiry.getText().toString().equals("")) {
            text_work_expiry.setVisibility(View.VISIBLE);
            text_work_expiry_hint.setVisibility(View.GONE);
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
        Double uniformAmount = 0d;
        try {
            uniformAmount = Double.valueOf(uniform_amount.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!vsr_numberOld.equals(vsr_number.getText().toString())
            || !vsr_expiryOld.equals(vsr_expiryNew)
            || !tech_licenseOld.equals(tech_license.getText().toString())
            || !tech_expiryOld.equals(tech_expiryNew)
            || !uniform_descriptionOld.equals(uniform_description.getText().toString())
            || uniform_allowanceOld != uniform_allowance.isChecked()
            || !uniform_amountOld.equals(uniformAmount)
            || !uniform_renewalOld.equals(uniform_renewalNew)
            || !work_numberOld.equals(work_number.getText().toString())
            || !work_expiryOld.equals(work_expiryNew)) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestProfilesWorkAdditionalInformationAdd(String result) {
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
        text_vsr_number.clearAnimation();
        text_vsr_expiry.clearAnimation();
        text_tech_license.clearAnimation();
        text_tech_expiry.clearAnimation();
        text_uniform_description.clearAnimation();
        text_uniform_amount.clearAnimation();
        text_uniform_renewal.clearAnimation();
        text_work_number.clearAnimation();
        text_work_expiry.clearAnimation();
        text_vsr_number_hint.clearAnimation();
        text_vsr_expiry_hint.clearAnimation();
        text_tech_license_hint.clearAnimation();
        text_tech_expiry_hint.clearAnimation();
        text_uniform_description_hint.clearAnimation();
        text_uniform_amount_hint.clearAnimation();
        text_uniform_renewal_hint.clearAnimation();
        text_work_number_hint.clearAnimation();
        text_work_expiry_hint.clearAnimation();
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
