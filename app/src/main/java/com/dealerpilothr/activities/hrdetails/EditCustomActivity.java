package com.dealerpilothr.activities.hrdetails;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.api.ProfilesCustomFieldsAddAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.ProfileData;

public class EditCustomActivity extends BaseActivity implements ProfilesCustomFieldsAddAction.RequestProfilesCustomFieldsAdd,
                                                                UpdateAction.RequestUpdate {
    private ProgressDialog pd;
    private EditText custom1;
    private EditText custom2;
    private EditText custom3;
    private EditText custom4;
    private EditText custom5;
    private EditText custom6;
    private EditText custom7;
    private EditText custom8;
    private EditText custom9;
    private String custom1Old = "";
    private String custom2Old = "";
    private String custom3Old = "";
    private String custom4Old = "";
    private String custom5Old = "";
    private String custom6Old = "";
    private String custom7Old = "";
    private String custom8Old = "";
    private String custom9Old = "";
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_custom1;
    private TextView text_custom2;
    private TextView text_custom3;
    private TextView text_custom4;
    private TextView text_custom5;
    private TextView text_custom6;
    private TextView text_custom7;
    private TextView text_custom8;
    private TextView text_custom9;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showCustom1 = true;
    private boolean showCustom2 = true;
    private boolean showCustom3 = true;
    private boolean showCustom4 = true;
    private boolean showCustom5 = true;
    private boolean showCustom6 = true;
    private boolean showCustom7 = true;
    private boolean showCustom8 = true;
    private boolean showCustom9 = true;
    private TextView text_custom1_hint;
    private TextView text_custom2_hint;
    private TextView text_custom3_hint;
    private TextView text_custom4_hint;
    private TextView text_custom5_hint;
    private TextView text_custom6_hint;
    private TextView text_custom7_hint;
    private TextView text_custom8_hint;
    private TextView text_custom9_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_custom);

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

        custom1 = (EditText) findViewById(R.id.custom1);
        custom2 = (EditText) findViewById(R.id.custom2);
        custom3 = (EditText) findViewById(R.id.custom3);
        custom4 = (EditText) findViewById(R.id.custom4);
        custom5 = (EditText) findViewById(R.id.custom5);
        custom6 = (EditText) findViewById(R.id.custom6);
        custom7 = (EditText) findViewById(R.id.custom7);
        custom8 = (EditText) findViewById(R.id.custom8);
        custom9 = (EditText) findViewById(R.id.custom9);
        text_custom1 = (TextView) findViewById(R.id.text_custom1);
        text_custom2 = (TextView) findViewById(R.id.text_custom2);
        text_custom3 = (TextView) findViewById(R.id.text_custom3);
        text_custom4 = (TextView) findViewById(R.id.text_custom4);
        text_custom5 = (TextView) findViewById(R.id.text_custom5);
        text_custom6 = (TextView) findViewById(R.id.text_custom6);
        text_custom7 = (TextView) findViewById(R.id.text_custom7);
        text_custom8 = (TextView) findViewById(R.id.text_custom8);
        text_custom9 = (TextView) findViewById(R.id.text_custom9);
        text_custom1_hint = (TextView) findViewById(R.id.text_custom1_hint);
        text_custom2_hint = (TextView) findViewById(R.id.text_custom2_hint);
        text_custom3_hint = (TextView) findViewById(R.id.text_custom3_hint);
        text_custom4_hint = (TextView) findViewById(R.id.text_custom4_hint);
        text_custom5_hint = (TextView) findViewById(R.id.text_custom5_hint);
        text_custom6_hint = (TextView) findViewById(R.id.text_custom6_hint);
        text_custom7_hint = (TextView) findViewById(R.id.text_custom7_hint);
        text_custom8_hint = (TextView) findViewById(R.id.text_custom8_hint);
        text_custom9_hint = (TextView) findViewById(R.id.text_custom9_hint);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch();
        custom1.addTextChangedListener(inputTextWatcher);
        custom2.addTextChangedListener(inputTextWatcher);
        custom3.addTextChangedListener(inputTextWatcher);
        custom4.addTextChangedListener(inputTextWatcher);
        custom5.addTextChangedListener(inputTextWatcher);
        custom6.addTextChangedListener(inputTextWatcher);
        custom7.addTextChangedListener(inputTextWatcher);
        custom8.addTextChangedListener(inputTextWatcher);
        custom9.addTextChangedListener(inputTextWatcher);

        Intent intent = getIntent();

        readonly = intent.getBooleanExtra("readonly", false);
        custom1Old = intent.getStringExtra("custom1");
        if (custom1Old == null) {
            custom1Old = "";
        }
        custom2Old = intent.getStringExtra("custom2");
        if (custom2Old == null) {
            custom2Old = "";
        }
        custom3Old = intent.getStringExtra("custom3");
        if (custom3Old == null) {
            custom3Old = "";
        }
        custom4Old = intent.getStringExtra("custom4");
        if (custom4Old == null) {
            custom4Old = "";
        }
        custom5Old = intent.getStringExtra("custom5");
        if (custom5Old == null) {
            custom5Old = "";
        }
        custom6Old = intent.getStringExtra("custom6");
        if (custom6Old == null) {
            custom6Old = "";
        }
        custom7Old = intent.getStringExtra("custom7");
        if (custom7Old == null) {
            custom7Old = "";
        }
        custom8Old = intent.getStringExtra("custom8");
        if (custom8Old == null) {
            custom8Old = "";
        }
        custom9Old = intent.getStringExtra("custom9");
        if (custom9Old == null) {
            custom9Old = "";
        }

        custom1.setText(custom1Old);
        custom2.setText(custom2Old);
        custom3.setText(custom3Old);
        custom4.setText(custom4Old);
        custom5.setText(custom5Old);
        custom6.setText(custom6Old);
        custom7.setText(custom7Old);
        custom8.setText(custom8Old);
        custom9.setText(custom9Old);

        if (custom1.getText().length() != 0) {
            showCustom1 = false;
        }
        if (custom2.getText().length() != 0) {
            showCustom2 = false;
        }
        if (custom3.getText().length() != 0) {
            showCustom3 = false;
        }
        if (custom4.getText().length() != 0) {
            showCustom4 = false;
        }
        if (custom5.getText().length() != 0) {
            showCustom5 = false;
        }
        if (custom6.getText().length() != 0) {
            showCustom6 = false;
        }
        if (custom7.getText().length() != 0) {
            showCustom7 = false;
        }
        if (custom8.getText().length() != 0) {
            showCustom8 = false;
        }
        if (custom9.getText().length() != 0) {
            showCustom9 = false;
        }

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (!custom1Old.equals(custom1.getText().toString())
                    || !custom2Old.equals(custom2.getText().toString())
                    || !custom3Old.equals(custom3.getText().toString())
                    || !custom4Old.equals(custom4.getText().toString())
                    || !custom5Old.equals(custom5.getText().toString())
                    || !custom6Old.equals(custom6.getText().toString())
                    || !custom7Old.equals(custom7.getText().toString())
                    || !custom8Old.equals(custom8.getText().toString())
                    || !custom9Old.equals(custom9.getText().toString())) {
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

        custom1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom1.getText().length() == 0 && showCustom1) {
                    text_custom1_hint.setVisibility(View.GONE);
                    text_custom1.setVisibility(View.VISIBLE);
                    showCustom1 = false;
                    text_custom1.startAnimation(animationUp);
                } else if (!hasFocus && custom1.getText().length() == 0 && !showCustom1) {
                    text_custom1.setVisibility(View.INVISIBLE);
                    showCustom1 = true;
                    text_custom1_hint.setVisibility(View.VISIBLE);
                    text_custom1_hint.startAnimation(animationDown);
                }
            }
        });

        custom2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom2.getText().length() == 0 && showCustom2) {
                    text_custom2_hint.setVisibility(View.GONE);
                    text_custom2.setVisibility(View.VISIBLE);
                    showCustom2 = false;
                    text_custom2.startAnimation(animationUp);
                } else if (!hasFocus && custom2.getText().length() == 0 && !showCustom2) {
                    text_custom2.setVisibility(View.INVISIBLE);
                    showCustom2 = true;
                    text_custom2_hint.setVisibility(View.VISIBLE);
                    text_custom2_hint.startAnimation(animationDown);
                }
            }
        });

        custom3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom3.getText().length() == 0 && showCustom3) {
                    text_custom3_hint.setVisibility(View.GONE);
                    text_custom3.setVisibility(View.VISIBLE);
                    showCustom3 = false;
                    text_custom3.startAnimation(animationUp);
                } else if (!hasFocus && custom3.getText().length() == 0 && !showCustom3) {
                    text_custom3.setVisibility(View.INVISIBLE);
                    showCustom3 = true;
                    text_custom3_hint.setVisibility(View.VISIBLE);
                    text_custom3_hint.startAnimation(animationDown);
                }
            }
        });

        custom4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom4.getText().length() == 0 && showCustom4) {
                    text_custom4_hint.setVisibility(View.GONE);
                    text_custom4.setVisibility(View.VISIBLE);
                    showCustom4 = false;
                    text_custom4.startAnimation(animationUp);
                } else if (!hasFocus && custom4.getText().length() == 0 && !showCustom4) {
                    text_custom4.setVisibility(View.INVISIBLE);
                    showCustom4 = true;
                    text_custom4_hint.setVisibility(View.VISIBLE);
                    text_custom4_hint.startAnimation(animationDown);
                }
            }
        });

        custom5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom5.getText().length() == 0 && showCustom5) {
                    text_custom5_hint.setVisibility(View.GONE);
                    text_custom5.setVisibility(View.VISIBLE);
                    showCustom5 = false;
                    text_custom5.startAnimation(animationUp);
                } else if (!hasFocus && custom5.getText().length() == 0 && !showCustom5) {
                    text_custom5.setVisibility(View.INVISIBLE);
                    showCustom5 = true;
                    text_custom5_hint.setVisibility(View.VISIBLE);
                    text_custom5_hint.startAnimation(animationDown);
                }
            }
        });

        custom6.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom6.getText().length() == 0 && showCustom6) {
                    text_custom6_hint.setVisibility(View.GONE);
                    text_custom6.setVisibility(View.VISIBLE);
                    showCustom6 = false;
                    text_custom6.startAnimation(animationUp);
                } else if (!hasFocus && custom6.getText().length() == 0 && !showCustom6) {
                    text_custom6.setVisibility(View.INVISIBLE);
                    showCustom6 = true;
                    text_custom6_hint.setVisibility(View.VISIBLE);
                    text_custom6_hint.startAnimation(animationDown);
                }
            }
        });

        custom7.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom7.getText().length() == 0 && showCustom7) {
                    text_custom7_hint.setVisibility(View.GONE);
                    text_custom7.setVisibility(View.VISIBLE);
                    showCustom7 = false;
                    text_custom7.startAnimation(animationUp);
                } else if (!hasFocus && custom7.getText().length() == 0 && !showCustom7) {
                    text_custom7.setVisibility(View.INVISIBLE);
                    showCustom7 = true;
                    text_custom7_hint.setVisibility(View.VISIBLE);
                    text_custom7_hint.startAnimation(animationDown);
                }
            }
        });

        custom8.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom8.getText().length() == 0 && showCustom8) {
                    text_custom8_hint.setVisibility(View.GONE);
                    text_custom8.setVisibility(View.VISIBLE);
                    showCustom8 = false;
                    text_custom8.startAnimation(animationUp);
                } else if (!hasFocus && custom8.getText().length() == 0 && !showCustom8) {
                    text_custom8.setVisibility(View.INVISIBLE);
                    showCustom8 = true;
                    text_custom8_hint.setVisibility(View.VISIBLE);
                    text_custom8_hint.startAnimation(animationDown);
                }
            }
        });

        custom9.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && custom9.getText().length() == 0 && showCustom9) {
                    text_custom9_hint.setVisibility(View.GONE);
                    text_custom9.setVisibility(View.VISIBLE);
                    showCustom9 = false;
                    text_custom9.startAnimation(animationUp);
                } else if (!hasFocus && custom9.getText().length() == 0 && !showCustom9) {
                    text_custom9.setVisibility(View.INVISIBLE);
                    showCustom9 = true;
                    text_custom9_hint.setVisibility(View.VISIBLE);
                    text_custom9_hint.startAnimation(animationDown);
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

                new UpdateAction(EditCustomActivity.this);
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
            custom1.setFocusable(false);
            custom2.setFocusable(false);
            custom3.setFocusable(false);
            custom4.setFocusable(false);
            custom5.setFocusable(false);
            custom6.setFocusable(false);
            custom7.setFocusable(false);
            custom8.setFocusable(false);
            custom9.setFocusable(false);
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

    private void update() {
        hideSoftKeyboard();

        pd.show();

        ProfileData data = new ProfileData();
        data.setCustomField1(custom1.getText().toString());
        data.setCustomField2(custom2.getText().toString());
        data.setCustomField3(custom3.getText().toString());
        data.setCustomField4(custom4.getText().toString());
        data.setCustomField5(custom5.getText().toString());
        data.setCustomField6(custom6.getText().toString());
        data.setCustomField7(custom7.getText().toString());
        data.setCustomField8(custom8.getText().toString());
        data.setCustomField9(custom9.getText().toString());
        data.setProfileId(UserInfo.getUserInfo().getProfile().getProfileId());

        new ProfilesCustomFieldsAddAction(EditCustomActivity.this, data).execute();
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
        if (!custom1.getText().toString().equals("")) {
            text_custom1.setVisibility(View.VISIBLE);
            text_custom1_hint.setVisibility(View.GONE);
        }
        if (!custom2.getText().toString().equals("")) {
            text_custom2.setVisibility(View.VISIBLE);
            text_custom2_hint.setVisibility(View.GONE);
        }
        if (!custom3.getText().toString().equals("")) {
            text_custom3.setVisibility(View.VISIBLE);
            text_custom3_hint.setVisibility(View.GONE);
        }
        if (!custom4.getText().toString().equals("")) {
            text_custom4.setVisibility(View.VISIBLE);
            text_custom4_hint.setVisibility(View.GONE);
        }
        if (!custom5.getText().toString().equals("")) {
            text_custom5.setVisibility(View.VISIBLE);
            text_custom5_hint.setVisibility(View.GONE);
        }
        if (!custom6.getText().toString().equals("")) {
            text_custom6.setVisibility(View.VISIBLE);
            text_custom6_hint.setVisibility(View.GONE);
        }
        if (!custom7.getText().toString().equals("")) {
            text_custom7.setVisibility(View.VISIBLE);
            text_custom7_hint.setVisibility(View.GONE);
        }
        if (!custom8.getText().toString().equals("")) {
            text_custom8.setVisibility(View.VISIBLE);
            text_custom8_hint.setVisibility(View.GONE);
        }
        if (!custom9.getText().toString().equals("")) {
            text_custom9.setVisibility(View.VISIBLE);
            text_custom9_hint.setVisibility(View.GONE);
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
        if (!custom1Old.equals(custom1.getText().toString())
            || !custom2Old.equals(custom2.getText().toString())
            || !custom3Old.equals(custom3.getText().toString())
            || !custom4Old.equals(custom4.getText().toString())
            || !custom5Old.equals(custom5.getText().toString())
            || !custom6Old.equals(custom6.getText().toString())
            || !custom7Old.equals(custom7.getText().toString())
            || !custom8Old.equals(custom8.getText().toString())
            || !custom9Old.equals(custom9.getText().toString())) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestProfilesCustomFieldsAdd(String result) {
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
        text_custom1.clearAnimation();
        text_custom2.clearAnimation();
        text_custom3.clearAnimation();
        text_custom4.clearAnimation();
        text_custom5.clearAnimation();
        text_custom6.clearAnimation();
        text_custom7.clearAnimation();
        text_custom8.clearAnimation();
        text_custom9.clearAnimation();
        text_custom1_hint.clearAnimation();
        text_custom2_hint.clearAnimation();
        text_custom3_hint.clearAnimation();
        text_custom4_hint.clearAnimation();
        text_custom5_hint.clearAnimation();
        text_custom6_hint.clearAnimation();
        text_custom7_hint.clearAnimation();
        text_custom8_hint.clearAnimation();
        text_custom9_hint.clearAnimation();
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
