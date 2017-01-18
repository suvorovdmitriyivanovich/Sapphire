package com.sapphire.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.sapphire.Sapphire;
import com.sapphire.R;
import com.sapphire.api.AutorizationAction;
import com.sapphire.logic.UserInfo;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AutorizationAction.RequestAutorization {
    private static long back_pressed;
    private UserInfo userInfo;
    ProgressDialog pd;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    final String USER = "USER";
    final String PASS = "PASS";
    private EditText name;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sPref = getSharedPreferences("GlobalPreferences", MODE_PRIVATE);
        ed = sPref.edit();

        name = (EditText) findViewById(R.id.Name);
        pass = (EditText) findViewById(R.id.Password);

        name.setText(sPref.getString(USER, ""));
        pass.setText(sPref.getString(PASS, ""));

        userInfo = UserInfo.getUserInfo();

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        View button_group = findViewById(R.id.button_group);
        button_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allOk = true;

                userInfo.setLogin(name.getText().toString());

                if (!userInfo.isLoginValid()) {
                    name.setError(getResources().getString(R.string.error_login));
                    allOk = false;
                }

                if (pass.getText().toString().equals("")) {
                    pass.setError(getResources().getString(R.string.error_pass));
                    allOk = false;
                }

                if (allOk) {
                    pd.show();
                    ed.putString(USER, userInfo.getLogin());
                    ed.putString(PASS, pass.getText().toString());
                    ed.apply();
                    new AutorizationAction(LoginActivity.this, name.getText().toString(), pass.getText().toString()).execute();
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

        if (name.getText().toString().equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
            } else {
                PhoneAccessGranted();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PhoneAccessGranted();
            } else {
                //Toast.makeText(this, getResources().getString(R.string.text_error_contacts), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void PhoneAccessGranted() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            String numsim = telephonyManager.getLine1Number();
            numsim = numsim.replace("+", "");

            String country = "";
            try {
                country = telephonyManager.getSimCountryIso();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (numsim.length() == 10 || numsim.length() == 11) {
                if (numsim.length() == 10) {
                    if (country.equals("ua")) {
                        numsim = "38" + numsim;
                    } else if (country.equals("ru")) {
                        numsim = "7" + numsim;
                    } else if (country.equals("en")) {
                        numsim = "1" + numsim;
                    }
                } else {
                    if (country.equals("ua")) {
                        numsim = "3" + numsim;
                    }
                }
            }

            name.setText(numsim);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestAutorization(final String result) {
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
