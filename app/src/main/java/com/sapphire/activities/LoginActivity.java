package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.sapphire.Sapphire;
import com.sapphire.R;
import com.sapphire.api.AuthenticationsAction;
import com.sapphire.logic.UserInfo;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AuthenticationsAction.RequestAuthentications {
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
    private View text_pass_error;

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
        text_pass_error = findViewById(R.id.text_password_error);

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
                    || pass.getText().toString().equals("")) {
                    allOk = false;
                }

                if (allOk) {
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

        updateErrors();
    }

    private class TextWatch implements TextWatcher {
        public TextWatch(){
            super();
        }

        public void afterTextChanged(Editable s) {
            updateErrors();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    private void updateErrors() {
        if (organization.getText().toString().equals("")) {
            text_organization_error.setVisibility(View.VISIBLE);
        } else {
            text_organization_error.setVisibility(View.GONE);
        }
        if (name.getText().toString().equals("")) {
            text_name_error.setVisibility(View.VISIBLE);
        } else {
            text_name_error.setVisibility(View.GONE);
        }
        if (pass.getText().toString().equals("")) {
            text_pass_error.setVisibility(View.VISIBLE);
        } else {
            text_pass_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestAuthentications(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String rez = "";
                String username = "";
                String userid = "";

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        username = jsonObjectData.getString("name");
                        userid = jsonObjectData.getString("id");
                        //userInfo.setUserid(5);
                        rez = "OK";
                    } else {
                        String error = jsonObject.getString("error");
                        if (error.equals("Not found")) {
                            rez = Sapphire.getInstance().getResources().getString(R.string.text_not_user);
                        } else if (error.equals("Incorrect password")) {
                            rez = Sapphire.getInstance().getResources().getString(R.string.text_incorrect_password);
                        } else {
                            rez = error;
                        }
                    }
                } catch (Exception e) {
                    rez = result;
                }

                pd.hide();
                //ed.putString(PASS, "");
                //ed.apply();
                //pass.setText("");

                Toast.makeText(getApplicationContext(),
                        rez,
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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
