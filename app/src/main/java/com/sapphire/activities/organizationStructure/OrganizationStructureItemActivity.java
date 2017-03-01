package com.sapphire.activities.organizationStructure;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.api.OrganizationStructureItemAddAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.OrganizationStructureData;
import com.sapphire.logic.UserInfo;

public class OrganizationStructureItemActivity extends BaseActivity implements OrganizationStructureItemAddAction.RequestOrganizationStructureItemAdd {

    private OrganizationStructureData organizationStructureData;
    private String parrentId = "";
    private EditText name;
    private EditText description;
    private CheckBox isCurrent;
    private CheckBox isPosition;
    private ProgressDialog pd;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private View text_name_error;
    private View text_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_organization_structure);

        Intent intent = getIntent();
        String id = intent.getStringExtra(Environment.ID);
        if(id != null && !id.isEmpty()) {
            organizationStructureData = UserInfo.getUserInfo().getOrganizationStructureDataById(id);
        } else {
            organizationStructureData = new OrganizationStructureData();
        }
        parrentId = intent.getStringExtra("parrentId");

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
                saveChanged();
            }
        });

        TextView text_header = (TextView) findViewById(R.id.text_header);
        text_header.setText(organizationStructureData.getName());

        name = (EditText) findViewById(R.id.name);
        text_name_error = findViewById(R.id.text_name_error);
        text_name = findViewById(R.id.text_name);
        description = (EditText) findViewById(R.id.description);
        isCurrent = (CheckBox) findViewById(R.id.current);
        isPosition = (CheckBox) findViewById(R.id.position);
        isPosition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCurrent.setVisibility(View.GONE);
                } else {
                    isCurrent.setVisibility(View.VISIBLE);
                }
            }
        });

        name.setText(organizationStructureData.getName());
        description.setText(organizationStructureData.getDescription());
        isPosition.setChecked(organizationStructureData.getIsPosition());
        isCurrent.setChecked(organizationStructureData.getIsCurrent());

        if (organizationStructureData.getIsPosition()) {
            isCurrent.setVisibility(View.GONE);
        }

        TextWatcher inputTextWatcher = new TextWatch();
        name.addTextChangedListener(inputTextWatcher);

        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                exit();
            }
        });

        View ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (organizationStructureData.getId().equals("") || !organizationStructureData.getName().equals(name.getText().toString())
                        || !organizationStructureData.getDescription().equals(description.getText().toString())
                        || organizationStructureData.getIsPosition() != isPosition.isChecked()
                        || organizationStructureData.getIsCurrent() != isCurrent.isChecked()) {
                    saveChanged();
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

        updateViews();
    }

    private void saveChanged() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("")) {
            allOk = false;
        }

        if (allOk) {
            pd.show();

            OrganizationStructureData organizationStructureData = new OrganizationStructureData();
            organizationStructureData.setName(name.getText().toString());
            organizationStructureData.setDescription(description.getText().toString());
            organizationStructureData.setId(organizationStructureData.getId());
            organizationStructureData.setIsPosition(isPosition.isChecked());
            organizationStructureData.setIsCurrent(isCurrent.isChecked());

            new OrganizationStructureItemAddAction(OrganizationStructureItemActivity.this, organizationStructureData, parrentId).execute();
        }
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
        if (name.getText().toString().equals("")) {
            text_name_error.setVisibility(View.VISIBLE);
            text_name.setVisibility(View.GONE);
        } else {
            text_name_error.setVisibility(View.GONE);
            text_name.setVisibility(View.VISIBLE);
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void exit() {
        if (!organizationStructureData.getName().equals(name.getText().toString())
                || !organizationStructureData.getDescription().equals(description.getText().toString())
                || organizationStructureData.getIsPosition() != isPosition.isChecked()
                || organizationStructureData.getIsCurrent() != isCurrent.isChecked()) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestOrganizationStructureItemAdd(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
