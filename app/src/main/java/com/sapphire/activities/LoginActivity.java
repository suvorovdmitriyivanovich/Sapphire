package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
    ProgressDialog pd;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    final String ORGANIZATION = "ORGANIZATION";
    final String USER = "USER";
    final String PASS = "PASS";
    private EditText organization;
    private EditText name;
    private EditText pass;
    private View text_organization_error;
    private View text_name_error;
    private TextView text_pass_error;
    private View text_organization;
    private View text_name;
    private View text_pass;
    private TextView text_error;

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
        text_name_error = findViewById(R.id.text_name_error);
        text_pass_error = (TextView) findViewById(R.id.text_password_error);
        text_organization = findViewById(R.id.text_organization);
        text_name = findViewById(R.id.text_name);
        text_pass = findViewById(R.id.text_password);
        text_error = (TextView) findViewById(R.id.text_error);

        TextWatcher inputTextWatcher = new TextWatch();
        organization.addTextChangedListener(inputTextWatcher);
        name.addTextChangedListener(inputTextWatcher);
        pass.addTextChangedListener(inputTextWatcher);

        organization.setText(sPref.getString(ORGANIZATION, ""));
        name.setText(sPref.getString(USER, ""));
        pass.setText(sPref.getString(PASS, ""));

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
        if (organization.getText().toString().equals("")) {
            text_organization_error.setVisibility(View.VISIBLE);
            text_organization.setVisibility(View.GONE);
        } else {
            text_organization_error.setVisibility(View.GONE);
            text_organization.setVisibility(View.VISIBLE);
        }
        if (name.getText().toString().equals("")) {
            text_name_error.setVisibility(View.VISIBLE);
            text_name.setVisibility(View.GONE);
        } else {
            text_name_error.setVisibility(View.GONE);
            text_name.setVisibility(View.VISIBLE);
        }
        if (pass.getText().toString().equals("") || pass.getText().length() < 6) {
            text_pass_error.setVisibility(View.VISIBLE);
        } else {
            text_pass_error.setVisibility(View.GONE);
        }
        if (pass.getText().toString().equals("")) {
            text_pass.setVisibility(View.GONE);
        } else {
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
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
