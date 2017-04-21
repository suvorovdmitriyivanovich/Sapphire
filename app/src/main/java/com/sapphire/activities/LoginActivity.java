package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.Sapphire;
import com.sapphire.R;
import com.sapphire.api.AuthenticationsAction;
import com.sapphire.logic.UserInfo;

public class LoginActivity extends BaseActivity implements AuthenticationsAction.RequestAuthentications {
    private static long back_pressed;
    private UserInfo userInfo;
    private ProgressDialog pd;
    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;
    private final String ORGANIZATION = "ORGANIZATION";
    private final String USER = "USER";
    private final String PASS = "PASS";
    private EditText organization;
    private EditText name;
    private EditText pass;
    private View text_organization_error;
    private View text_name_error;
    private TextView text_pass_error;
    private TextView text_organization;
    private TextView text_name;
    private TextView text_pass;
    private TextView text_error;
    private boolean isCheckOrganization = false;
    private boolean isCheckName = false;
    private boolean isCheckPass = false;
    private Animation animationErrorDown;
    private Animation animationErrorUpOrganization;
    private Animation animationErrorUpName;
    private Animation animationErrorUpPass;
    private boolean showErrorOrganization = false;
    private boolean showErrorName = false;
    private boolean showErrorPass = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showOrganization = true;
    private boolean showName = true;
    private boolean showPass = true;
    private TextView text_organization_hint;
    private TextView text_name_hint;
    private TextView text_pass_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sPref = getSharedPreferences("GlobalPreferences", MODE_PRIVATE);
        ed = sPref.edit();

        organization = (EditText) findViewById(R.id.organization);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.password);
        text_organization_error = findViewById(R.id.text_organization_error);
        text_organization_hint = (TextView) findViewById(R.id.text_organization_hint);
        text_name_error = findViewById(R.id.text_name_error);
        text_name_hint = (TextView) findViewById(R.id.text_name_hint);
        text_pass_error = (TextView) findViewById(R.id.text_password_error);
        text_pass_hint = (TextView) findViewById(R.id.text_pass_hint);
        text_organization = (TextView) findViewById(R.id.text_organization);
        text_name = (TextView) findViewById(R.id.text_name);
        text_pass = (TextView) findViewById(R.id.text_password);
        text_error = (TextView) findViewById(R.id.text_error);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpOrganization = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpName = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpPass = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpOrganization.setAnimationListener(animationErrorUpOrganizationListener);
        animationErrorUpName.setAnimationListener(animationErrorUpNameListener);
        animationErrorUpPass.setAnimationListener(animationErrorUpPasswordListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        organization.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        name.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(3);
        pass.addTextChangedListener(inputTextWatcher);

        if (!sPref.getString(ORGANIZATION, "").equals("")) {
            organization.setText(sPref.getString(ORGANIZATION, ""));
            showOrganization = false;
        }
        if (!sPref.getString(USER, "").equals("")) {
            name.setText(sPref.getString(USER, ""));
            showName = false;
        }
        if (!sPref.getString(PASS, "").equals("")) {
            pass.setText(sPref.getString(PASS, ""));
            showPass = false;
        }

        userInfo = UserInfo.getUserInfo();

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        View button_signin = findViewById(R.id.button_signin);
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                boolean allOk = true;

                //userInfo.setLogin(name.getText().toString());
                //if (!userInfo.isLoginValid()) {
                //    name.setError(getResources().getString(R.string.error_login));
                //    allOk = false;
                //}

                if (organization.getText().toString().equals("")
                    || name.getText().toString().equals("")
                    || pass.getText().toString().equals("")
                    || pass.getText().length() < 6) {
                    isCheckOrganization = true;
                    isCheckName = true;
                    isCheckPass = true;
                    updateViews();
                    allOk = false;
                }

                if (allOk) {
                    text_error.setVisibility(View.GONE);
                    pd.show();

                    ed.putString(ORGANIZATION, organization.getText().toString());
                    ed.putString(USER, name.getText().toString());
                    ed.putString(PASS, pass.getText().toString());
                    ed.apply();

                    new AuthenticationsAction(LoginActivity.this, organization.getText().toString(), name.getText().toString(), pass.getText().toString()).execute();
                }
            }
        });

        View root = findViewById(R.id.root_layout);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        organization.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && organization.getText().length() == 0 && showOrganization) {
                    text_organization_hint.setVisibility(View.GONE);
                    text_organization.setVisibility(View.VISIBLE);
                    showOrganization = false;
                    text_organization.startAnimation(animationUp);
                } else if (!hasFocus && organization.getText().length() == 0 && !showOrganization) {
                    text_organization.setVisibility(View.INVISIBLE);
                    showOrganization = true;
                    text_organization_hint.setVisibility(View.VISIBLE);
                    text_organization_hint.startAnimation(animationDown);
                }
            }
        });

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

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && pass.getText().length() == 0 && showPass) {
                    text_pass_hint.setVisibility(View.GONE);
                    text_pass.setVisibility(View.VISIBLE);
                    showPass = false;
                    text_pass.startAnimation(animationUp);
                } else if (!hasFocus && pass.getText().length() == 0 && !showPass) {
                    text_pass.setVisibility(View.INVISIBLE);
                    showPass = true;
                    text_pass_hint.setVisibility(View.VISIBLE);
                    text_pass_hint.startAnimation(animationDown);
                }
            }
        });

        updateViews();
    }

    Animation.AnimationListener animationErrorUpOrganizationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_organization_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

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

    Animation.AnimationListener animationErrorUpPasswordListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_pass_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    private class TextWatch implements TextWatcher {
        private int type;

        public TextWatch(int type){
            super();
            this.type = type;
        }

        public void afterTextChanged(Editable s) {
            if (type == 1) {
                isCheckOrganization = true;
            } else if (type == 2) {
                isCheckName = true;
            } else if (type == 3) {
                isCheckPass = true;
            }
            updateViews();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    private void updateViews() {
        if (isCheckOrganization && organization.getText().toString().equals("")) {
            text_organization_error.setVisibility(View.VISIBLE);
            organization.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_organization.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_organization_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorOrganization) {
                showErrorOrganization = true;
                text_organization_error.startAnimation(animationErrorDown);
            }
        } else if (!organization.getText().toString().equals("")) {
            text_organization.setVisibility(View.VISIBLE);
            organization.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_organization.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_organization_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_organization_hint.setVisibility(View.GONE);
            if (showErrorOrganization) {
                showErrorOrganization = false;
                text_organization_error.startAnimation(animationErrorUpOrganization);
            }
        }
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
        if (isCheckPass && (pass.getText().toString().equals("") || pass.getText().length() < 6)) {
            text_pass_error.setVisibility(View.VISIBLE);
            pass.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            pass.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_pass.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_pass_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorPass) {
                showErrorPass = true;
                text_pass_error.startAnimation(animationErrorDown);
            }
        } else if (!pass.getText().toString().equals("")) {
            pass.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            pass.setTextColor(ContextCompat.getColor(this, R.color.black));
            text_pass.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_pass_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_pass_hint.setVisibility(View.GONE);
            if (showErrorPass) {
                showErrorPass = false;
                text_pass_error.startAnimation(animationErrorUpPass);
            }
        }
        if (!pass.getText().toString().equals("")) {
            text_pass.setVisibility(View.VISIBLE);
        }
        if (!pass.getText().toString().equals("") && pass.getText().length() < 6) {
            text_pass_error.setText(getResources().getString(R.string.text_password_error_lenght));
        } else {
            text_pass_error.setText(getResources().getString(R.string.text_password_error));
        }
    }

    @Override
    public void onRequestAuthentications(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd.hide();
                //ed.putString(PASS, "");
                //ed.apply();
                //pass.setText("");

                if (result.equals("OK") ||
                        (result.equals(getResources().getString(R.string.text_need_internet))) && sPref.getBoolean("INSUCCESS", false)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    text_error.setText(result);
                    text_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();

            Sapphire.exit(this);
        } else
            Toast.makeText(getBaseContext(), R.string.text_again_exit,
                    Toast.LENGTH_LONG).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
