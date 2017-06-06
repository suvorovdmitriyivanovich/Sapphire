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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.R;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.adapters.SpinCountriesAdapter;
import com.dealerpilothr.adapters.SpinProvincesAdapter;
import com.dealerpilothr.api.ProfilesAdressAddAction;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.AdressData;
import com.dealerpilothr.models.CountryData;
import com.dealerpilothr.models.RegionData;

import java.util.ArrayList;

public class EditAdressActivity extends BaseActivity implements ProfilesAdressAddAction.RequestProfilesAdressAdd,
                                                                UpdateAction.RequestUpdate {
    private ProgressDialog pd;
    private EditText adress;
    private EditText city;
    private EditText postal;
    private String adressOld = "";
    private String cityOld = "";
    private String postalOld = "";
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_adress;
    private TextView text_city;
    private TextView text_postal;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showAdress = true;
    private boolean showCity = true;
    private boolean showPostal = true;
    private TextView text_adress_hint;
    private TextView text_city_hint;
    private TextView text_postal_hint;
    private Spinner spinnerCountry;
    private Spinner spinnerProvince;
    private ArrayList<CountryData> countris;
    private ArrayList<RegionData> provinces;
    private SpinCountriesAdapter adapterCountry;
    private SpinProvincesAdapter adapterProvince;
    private String countryOld = "";
    private String provinceOld = "";
    private String countryId = "";
    private String provinceId = "";
    private EditText country;
    private EditText province;
    private boolean isCheckCountry = false;
    private boolean isCheckProvince = false;
    private TextView text_country;
    private TextView text_province;
    private boolean clickSpinner = false;
    private String adressId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_adress);

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

        adress = (EditText) findViewById(R.id.adress);
        city = (EditText) findViewById(R.id.city);
        postal = (EditText) findViewById(R.id.postal);
        text_adress= (TextView) findViewById(R.id.text_adress);
        text_city = (TextView) findViewById(R.id.text_city);
        text_postal = (TextView) findViewById(R.id.text_postal);
        text_adress_hint = (TextView) findViewById(R.id.text_adress_hint);
        text_city_hint = (TextView) findViewById(R.id.text_city_hint);
        text_postal_hint = (TextView) findViewById(R.id.text_postal_hint);
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        spinnerProvince = (Spinner) findViewById(R.id.spinnerProvince);
        country = (EditText) findViewById(R.id.country);
        province = (EditText) findViewById(R.id.province);
        text_country = (TextView) findViewById(R.id.text_country);
        text_province = (TextView) findViewById(R.id.text_province);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch();
        adress.addTextChangedListener(inputTextWatcher);
        city.addTextChangedListener(inputTextWatcher);
        postal.addTextChangedListener(inputTextWatcher);

        countris = new ArrayList<>();
        countris.add(new CountryData());
        countris.addAll(UserInfo.getUserInfo().getCountryDatas());

        adapterCountry = new SpinCountriesAdapter(this, R.layout.spinner_list_item_black);
        spinnerCountry.setAdapter(adapterCountry);
        adapterCountry.setValues(countris);
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    //if (true) {
                    return;
                }
                clickSpinner = true;
                spinnerCountry.performClick();
            }
        });
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                country.setText(countris.get(position).getName());
                countryId = countris.get(position).getCountryId();
                province.setText("");
                provinceId = "";
                province.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spinnerProvince.setSelection(0,false);
                    }
                }, 10);
                provinces.clear();
                provinces.add(new RegionData());
                provinces.addAll(countris.get(position).getRegions());
                adapterProvince.setValues(provinces);
                adapterProvince.notifyDataSetChanged();
                updateViews();
                clickSpinner = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        provinces = new ArrayList<>();

        adapterProvince = new SpinProvincesAdapter(this, R.layout.spinner_list_item_black);
        spinnerProvince.setAdapter(adapterProvince);
        adapterProvince.setValues(provinces);
        province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    //if (true) {
                    return;
                }
                clickSpinner = true;
                spinnerProvince.performClick();
            }
        });
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                province.setText(provinces.get(position).getName());
                provinceId = provinces.get(position).getRegionId();
                updateViews();
                clickSpinner = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent = getIntent();

        adressId = intent.getStringExtra("adressId");
        if (adressId == null) {
            adressId = "";
        }
        readonly = intent.getBooleanExtra("readonly", false);
        adressOld = intent.getStringExtra("adress");
        if (adressOld == null) {
            adressOld = "";
        }
        cityOld = intent.getStringExtra("city");
        if (cityOld == null) {
            cityOld = "";
        }
        postalOld = intent.getStringExtra("postal");
        if (postalOld == null) {
            postalOld = "";
        }
        countryOld = intent.getStringExtra("country");
        if (countryOld == null) {
            countryOld = "";
        }
        provinceOld = intent.getStringExtra("province");
        if (provinceOld == null) {
            provinceOld = "";
        }

        adress.setText(adressOld);
        city.setText(cityOld);
        postal.setText(postalOld);

        if (adress.getText().length() != 0) {
            showAdress = false;
        }
        if (city.getText().length() != 0) {
            showCity = false;
        }
        if (postal.getText().length() != 0) {
            showPostal = false;
        }

        if (!countryOld.equals("")) {
            int countryPosition = 0;
            for (int i = 0; i < countris.size(); i ++) {
                if (countris.get(i).getCountryId().equals(countryOld)) {
                    provinces.clear();
                    provinces.add(new RegionData());
                    provinces.addAll(countris.get(i).getRegions());
                    adapterProvince.setValues(provinces);
                    adapterProvince.notifyDataSetChanged();
                    countryPosition = i;
                    break;
                }
            }
            country.setText(countris.get(countryPosition).getName());
            countryId = countryOld;
            final int finalCountryPosition = countryPosition;
            country.postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinnerCountry.setSelection(finalCountryPosition,false);
                }
            }, 10);
        }

        if (!provinceOld.equals("")) {
            int provincePosition = 0;
            for (int i = 0; i < provinces.size(); i ++) {
                if (provinces.get(i).getRegionId().equals(provinceOld)) {
                    provincePosition = i;
                    break;
                }
            }
            province.setText(provinces.get(provincePosition).getName());
            provinceId = provinceOld;
            final int finalProvincePosition = provincePosition;
            province.postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinnerProvince.setSelection(finalProvincePosition,false);
                }
            }, 10);
        }

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (!adressOld.equals(adress.getText().toString())
                    || !cityOld.equals(city.getText().toString())
                    || !postalOld.equals(postal.getText().toString())
                    || !countryOld.equals(countryId)
                    || !provinceOld.equals(provinceId)) {
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

        adress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && adress.getText().length() == 0 && showAdress) {
                    text_adress_hint.setVisibility(View.GONE);
                    text_adress.setVisibility(View.VISIBLE);
                    showAdress = false;
                    text_adress.startAnimation(animationUp);
                } else if (!hasFocus && adress.getText().length() == 0 && !showAdress) {
                    text_adress.setVisibility(View.INVISIBLE);
                    showAdress = true;
                    text_adress_hint.setVisibility(View.VISIBLE);
                    text_adress_hint.startAnimation(animationDown);
                }
            }
        });

        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && city.getText().length() == 0 && showCity) {
                    text_city_hint.setVisibility(View.GONE);
                    text_city.setVisibility(View.VISIBLE);
                    showCity = false;
                    text_city.startAnimation(animationUp);
                } else if (!hasFocus && city.getText().length() == 0 && !showCity) {
                    text_city.setVisibility(View.INVISIBLE);
                    showCity = true;
                    text_city_hint.setVisibility(View.VISIBLE);
                    text_city_hint.startAnimation(animationDown);
                }
            }
        });

        postal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && postal.getText().length() == 0 && showPostal) {
                    text_postal_hint.setVisibility(View.GONE);
                    text_postal.setVisibility(View.VISIBLE);
                    showPostal = false;
                    text_postal.startAnimation(animationUp);
                } else if (!hasFocus && postal.getText().length() == 0 && !showPostal) {
                    text_postal.setVisibility(View.INVISIBLE);
                    showPostal = true;
                    text_postal_hint.setVisibility(View.VISIBLE);
                    text_postal_hint.startAnimation(animationDown);
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

                new UpdateAction(EditAdressActivity.this);
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
            adress.setFocusable(false);
            city.setFocusable(false);
            postal.setFocusable(false);
            country.setFocusable(false);
            province.setFocusable(false);
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

        AdressData data = new AdressData();
        data.setAddressLine1(adress.getText().toString());
        data.setCountry(country.getText().toString());
        data.setCountryId(countryId);
        data.setRegion(province.getText().toString());
        data.setRegionId(provinceId);
        data.setCity(city.getText().toString());
        data.setPostalCode(postal.getText().toString());
        data.setAddressId(adressId);
        data.setIsPrimary(true);

        new ProfilesAdressAddAction(EditAdressActivity.this, data).execute();
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
        if (!adress.getText().toString().equals("")) {
            text_adress.setVisibility(View.VISIBLE);
            text_adress_hint.setVisibility(View.GONE);
        }
        if (!city.getText().toString().equals("")) {
            text_city.setVisibility(View.VISIBLE);
            text_city_hint.setVisibility(View.GONE);
        }
        if (!postal.getText().toString().equals("")) {
            text_postal.setVisibility(View.VISIBLE);
            text_postal_hint.setVisibility(View.GONE);
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
        if (!adressOld.equals(adress.getText().toString())
            || !cityOld.equals(city.getText().toString())
            || !postalOld.equals(postal.getText().toString())
            || !countryOld.equals(countryId)
            || !provinceOld.equals(provinceId)) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestProfilesAdressAdd(String result) {
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
        text_adress.clearAnimation();
        text_city.clearAnimation();
        text_postal.clearAnimation();
        text_adress_hint.clearAnimation();
        text_city_hint.clearAnimation();
        text_postal_hint.clearAnimation();
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
