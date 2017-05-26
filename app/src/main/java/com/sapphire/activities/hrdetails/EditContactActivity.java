package com.sapphire.activities.hrdetails;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.api.ProfilesContactInformationAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ProfileData;
import com.sapphire.utils.GeneralOperations;

public class EditContactActivity extends BaseActivity implements ProfilesContactInformationAddAction.RequestProfilesContactInformationAdd,
                                                                 UpdateAction.RequestUpdate{
    private ProgressDialog pd;
    private EditText primary;
    private EditText secondary;
    private EditText home;
    private EditText cell;
    private CheckBox notification_primary;
    private CheckBox notification_secondary;
    private TextView text_primary_error;
    private TextView text_secondary_error;
    private String primaryOld = "";
    private String secondaryOld = "";
    private String homeOld = "";
    private String cellOld = "";
    private boolean notification_primaryOld = false;
    private boolean notification_secondaryOld = false;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private boolean isCheckPrimary = false;
    private boolean isCheckSecondary = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_primary;
    private TextView text_secondary;
    private TextView text_home;
    private TextView text_cell;
    private Animation animationErrorDown;
    private Animation animationErrorUpPrimary;
    private Animation animationErrorUpSecondary;
    private boolean showErrorPrimary = false;
    private boolean showErrorSecondary = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showPrimary = true;
    private boolean showSecondary = true;
    private boolean showHome = true;
    private boolean showCell = true;
    private TextView text_primary_hint;
    private TextView text_secondary_hint;
    private TextView text_home_hint;
    private TextView text_cell_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_contact);

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

        primary = (EditText) findViewById(R.id.primary);
        secondary = (EditText) findViewById(R.id.secondary);
        home = (EditText) findViewById(R.id.home);
        cell = (EditText) findViewById(R.id.cell);
        notification_primary = (CheckBox) findViewById(R.id.notification_primary);
        notification_secondary = (CheckBox) findViewById(R.id.notification_secondary);
        text_primary_error = (TextView) findViewById(R.id.text_primary_error);
        text_secondary_error = (TextView) findViewById(R.id.text_secondary_error);
        text_primary = (TextView) findViewById(R.id.text_primary);
        text_secondary = (TextView) findViewById(R.id.text_secondary);
        text_home = (TextView) findViewById(R.id.text_home);
        text_cell = (TextView) findViewById(R.id.text_cell);
        text_primary_hint = (TextView) findViewById(R.id.text_primary_hint);
        text_secondary_hint = (TextView) findViewById(R.id.text_secondary_hint);
        text_home_hint = (TextView) findViewById(R.id.text_home_hint);
        text_cell_hint = (TextView) findViewById(R.id.text_cell_hint);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpPrimary = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpSecondary = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpPrimary.setAnimationListener(animationErrorUpPrimaryListener);
        animationErrorUpSecondary.setAnimationListener(animationErrorUpSecondaryListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        primary.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        secondary.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(3);
        home.addTextChangedListener(inputTextWatcher);
        cell.addTextChangedListener(inputTextWatcher);

        Intent intent = getIntent();

        readonly = intent.getBooleanExtra("readonly", false);
        primaryOld = intent.getStringExtra("primary");
        secondaryOld = intent.getStringExtra("secondary");
        homeOld = intent.getStringExtra("home");
        cellOld = intent.getStringExtra("cell");
        notification_primaryOld = intent.getBooleanExtra("primaryEmailAllowNotification", false);
        notification_secondaryOld = intent.getBooleanExtra("secondaryEmailAllowNotification", false);

        primary.setText(primaryOld);
        secondary.setText(secondaryOld);
        home.setText(homeOld);
        cell.setText(cellOld);
        notification_primary.setChecked(notification_primaryOld);
        notification_secondary.setChecked(notification_secondaryOld);

        if (primary.getText().length() != 0) {
            showPrimary = false;
        }
        if (secondary.getText().length() != 0) {
            showSecondary = false;
        }
        if (home.getText().length() != 0) {
            showHome = false;
        }
        if (cell.getText().length() != 0) {
            showCell = false;
        }

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (!primaryOld.equals(primary.getText().toString())
                    || !secondaryOld.equals(secondary.getText().toString())
                    || !homeOld.equals(home.getText().toString())
                    || !cellOld.equals(cell.getText().toString())
                    || notification_primaryOld != notification_primary.isChecked()
                    || notification_secondaryOld != notification_secondary.isChecked()) {
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

        primary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && primary.getText().length() == 0 && showPrimary) {
                    text_primary_hint.setVisibility(View.GONE);
                    text_primary.setVisibility(View.VISIBLE);
                    showPrimary = false;
                    text_primary.startAnimation(animationUp);
                } else if (!hasFocus && primary.getText().length() == 0 && !showPrimary) {
                    text_primary.setVisibility(View.INVISIBLE);
                    showPrimary = true;
                    text_primary_hint.setVisibility(View.VISIBLE);
                    text_primary_hint.startAnimation(animationDown);
                }
            }
        });

        secondary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && secondary.getText().length() == 0 && showSecondary) {
                    text_secondary_hint.setVisibility(View.GONE);
                    text_secondary.setVisibility(View.VISIBLE);
                    showSecondary = false;
                    text_secondary.startAnimation(animationUp);
                } else if (!hasFocus && secondary.getText().length() == 0 && !showSecondary) {
                    text_secondary.setVisibility(View.INVISIBLE);
                    showSecondary = true;
                    text_secondary_hint.setVisibility(View.VISIBLE);
                    text_secondary_hint.startAnimation(animationDown);
                }
            }
        });

        home.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && home.getText().length() == 0 && showHome) {
                    text_home_hint.setVisibility(View.GONE);
                    text_home.setVisibility(View.VISIBLE);
                    showHome = false;
                    text_home.startAnimation(animationUp);
                } else if (!hasFocus && home.getText().length() == 0 && !showHome) {
                    text_home.setVisibility(View.INVISIBLE);
                    showHome = true;
                    text_home_hint.setVisibility(View.VISIBLE);
                    text_home_hint.startAnimation(animationDown);
                }
            }
        });

        cell.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && cell.getText().length() == 0 && showCell) {
                    text_cell_hint.setVisibility(View.GONE);
                    text_cell.setVisibility(View.VISIBLE);
                    showCell = false;
                    text_cell.startAnimation(animationUp);
                } else if (!hasFocus && cell.getText().length() == 0 && !showCell) {
                    text_cell.setVisibility(View.INVISIBLE);
                    showCell = true;
                    text_cell_hint.setVisibility(View.VISIBLE);
                    text_cell_hint.startAnimation(animationDown);
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

                new UpdateAction(EditContactActivity.this);
            }
        });

        UpdateBottom();

        if (readonly) {
            button_ok.setVisibility(View.GONE);
            primary.setFocusable(false);
            secondary.setFocusable(false);
            home.setFocusable(false);
            cell.setFocusable(false);
            notification_primary.setClickable(false);
            notification_secondary.setClickable(false);
        }
    }

    Animation.AnimationListener animationErrorUpPrimaryListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_primary_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    Animation.AnimationListener animationErrorUpSecondaryListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_secondary_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    private void UpdateBottom() {
        if (Sapphire.getInstance().getNeedUpdate()) {
            par_nointernet_group.height = GetPixelFromDips(56);
        } else {
            par_nointernet_group.height = 0;
        }
        nointernet_group.setLayoutParams(par_nointernet_group);
        nointernet_group.requestLayout();
    }

    private void update() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (!GeneralOperations.isEmailValid(primary.getText().toString(), true) || !GeneralOperations.isEmailValid(secondary.getText().toString(), true)
            || primary.getText().toString().equals(secondary.getText().toString())) {
            isCheckPrimary = true;
            isCheckSecondary = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            ProfileData data = new ProfileData();
            data.setEmail(primary.getText().toString());
            data.setSecondaryEmail(secondary.getText().toString());
            data.setHomePhoneNumber(home.getText().toString());
            data.setCellPhoneNumber(cell.getText().toString());
            data.setPrimaryEmailAllowNotification(notification_primary.isChecked());
            data.setSecondaryEmailAllowNotification(notification_secondary.isChecked());
            data.setProfileId(UserInfo.getUserInfo().getProfile().getProfileId());

            new ProfilesContactInformationAddAction(EditContactActivity.this, data).execute();
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
                isCheckPrimary = true;
            } else if (type == 2) {
                isCheckSecondary = true;
            }
            updateViews();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    private void updateViews() {
        if (isCheckPrimary && (!GeneralOperations.isEmailValid(primary.getText().toString(), true)
                               || primary.getText().toString().equals(secondary.getText().toString()))) {
            if (primary.getText().toString().equals(secondary.getText().toString())) {
                text_primary_error.setText(getResources().getString(R.string.text_emails_equals));
            } else {
                text_primary_error.setText(getResources().getString(R.string.text_not_valid_email));
            }
            text_primary_error.setVisibility(View.VISIBLE);
            primary.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_primary.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_primary_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorPrimary) {
                showErrorPrimary = true;
                text_primary_error.startAnimation(animationErrorDown);
            }
        } else if (GeneralOperations.isEmailValid(primary.getText().toString(), true)) {
            text_primary.setVisibility(View.VISIBLE);
            primary.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_primary.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_primary_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_primary_hint.setVisibility(View.GONE);
            if (showErrorPrimary) {
                showErrorPrimary = false;
                text_primary_error.startAnimation(animationErrorUpPrimary);
            }
        }
        if (isCheckSecondary && (!GeneralOperations.isEmailValid(secondary.getText().toString(), true)
                                 || primary.getText().toString().equals(secondary.getText().toString()))) {
            if (primary.getText().toString().equals(secondary.getText().toString())) {
                text_secondary_error.setText(getResources().getString(R.string.text_emails_equals));
            } else {
                text_secondary_error.setText(getResources().getString(R.string.text_not_valid_email));
            }
            text_secondary_error.setVisibility(View.VISIBLE);
            secondary.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_secondary.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_secondary_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorSecondary) {
                showErrorSecondary = true;
                text_secondary_error.startAnimation(animationErrorDown);
            }
        } else if (GeneralOperations.isEmailValid(secondary.getText().toString(), true)) {
            text_secondary.setVisibility(View.VISIBLE);
            secondary.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_secondary.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_secondary_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_secondary_hint.setVisibility(View.GONE);
            if (showErrorSecondary) {
                showErrorSecondary = false;
                text_secondary_error.startAnimation(animationErrorUpSecondary);
            }
        }
        if (!home.getText().toString().equals("")) {
            text_home.setVisibility(View.VISIBLE);
            text_home_hint.setVisibility(View.GONE);
        }
        if (!cell.getText().toString().equals("")) {
            text_cell.setVisibility(View.VISIBLE);
            text_cell_hint.setVisibility(View.GONE);
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
        if (!primaryOld.equals(primary.getText().toString())
                || !secondaryOld.equals(secondary.getText().toString())
                || !homeOld.equals(home.getText().toString())
                || !cellOld.equals(cell.getText().toString())
                || notification_primaryOld != notification_primary.isChecked()
                || notification_secondaryOld != notification_secondary.isChecked()) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestProfilesContactInformationAdd(String result) {
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        text_primary.clearAnimation();
        text_secondary.clearAnimation();
        text_home.clearAnimation();
        text_cell.clearAnimation();
        text_primary_error.clearAnimation();
        text_secondary_error.clearAnimation();
        text_primary_hint.clearAnimation();
        text_secondary_hint.clearAnimation();
        text_home_hint.clearAnimation();
        text_cell_hint.clearAnimation();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        primary.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        secondary.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        unregisterReceiver(br);
    }
}
