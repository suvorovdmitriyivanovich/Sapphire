package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private TextView text_organization_error;
    private View text_name_error;
    private TextView text_pass_error;
    private View text_organization;
    private View text_name;
    private View text_pass;
    private TextView text_error;
    private boolean isCheckOrganization = false;
    private boolean isCheckName = false;
    private boolean isCheckPass = false;
    private Animation animationErrorDown;
    private Animation animationErrorUpOrganization;
    private Animation animationErrorUpName;
    private Animation animationErrorUpPassword;
    private boolean showErrorOrganization = false;
    private boolean showErrorName = false;
    private boolean showErrorPassword = false;
    private Animation animationDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sPref = getSharedPreferences("GlobalPreferences", MODE_PRIVATE);
        ed = sPref.edit();

        organization = (EditText) findViewById(R.id.organization);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.password);
        text_organization_error = (TextView) findViewById(R.id.text_organization_error);
        text_name_error = findViewById(R.id.text_name_error);
        text_pass_error = (TextView) findViewById(R.id.text_password_error);
        text_organization = findViewById(R.id.text_organization);
        text_name = findViewById(R.id.text_name);
        text_pass = findViewById(R.id.text_password);
        text_error = (TextView) findViewById(R.id.text_error);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpOrganization = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpName = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpPassword = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpOrganization.setAnimationListener(animationErrorUpOrganizationListener);
        animationErrorUpName.setAnimationListener(animationErrorUpNameListener);
        animationErrorUpPassword.setAnimationListener(animationErrorUpPasswordListener);

        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        organization.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        name.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(3);
        pass.addTextChangedListener(inputTextWatcher);

        if (!sPref.getString(ORGANIZATION, "").equals("")) {
            organization.setText(sPref.getString(ORGANIZATION, ""));
        }
        if (!sPref.getString(USER, "").equals("")) {
            name.setText(sPref.getString(USER, ""));
        }
        if (!sPref.getString(PASS, "").equals("")) {
            pass.setText(sPref.getString(PASS, ""));
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
            text_organization.setVisibility(View.INVISIBLE);
            //text_organization.startAnimation(animationDown);
            if (!showErrorOrganization) {
                showErrorOrganization = true;
                text_organization_error.startAnimation(animationErrorDown);
            }
        } else {
            //text_organization_error.setVisibility(View.GONE);
            text_organization.setVisibility(View.VISIBLE);
            if (showErrorOrganization) {
                showErrorOrganization = false;
                text_organization_error.startAnimation(animationErrorUpOrganization);
            }
        }
        if (isCheckName && name.getText().toString().equals("")) {
            text_name_error.setVisibility(View.VISIBLE);
            //text_name.setVisibility(View.GONE);
            if (!showErrorName) {
                showErrorName = true;
                text_name_error.startAnimation(animationErrorDown);
            }
        } else {
            //text_name_error.setVisibility(View.GONE);
            //text_name.setVisibility(View.VISIBLE);
            if (showErrorName) {
                showErrorName = false;
                text_name_error.startAnimation(animationErrorUpName);
            }
        }
        if (isCheckPass && (pass.getText().toString().equals("") || pass.getText().length() < 6)) {
            text_pass_error.setVisibility(View.VISIBLE);
            if (!showErrorPassword) {
                showErrorPassword = true;
                text_pass_error.startAnimation(animationErrorDown);
            }
        } else {
            //text_pass_error.setVisibility(View.GONE);
            if (showErrorPassword) {
                showErrorPassword = false;
                text_pass_error.startAnimation(animationErrorUpPassword);
            }
        }
        if (isCheckPass && pass.getText().toString().equals("")) {
            //text_pass.setVisibility(View.GONE);
        } else {
            //text_pass.setVisibility(View.VISIBLE);
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
