package com.sapphire.activities;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.adapters.ItemsAdapter;
import com.sapphire.api.GetTemplateAction;
import com.sapphire.api.TemplateAddAction;
import com.sapphire.api.TemplateItemDeleteAction;
import com.sapphire.logic.TemplateItemData;
import java.util.ArrayList;

public class TemplateActivity extends BaseActivity implements GetTemplateAction.RequestTemplate,
                                                              GetTemplateAction.RequestTemplateData,
                                                              ItemsAdapter.OnRootClickListener,
                                                              ItemsAdapter.OnOpenClickListener,
                                                              ItemsAdapter.OnDeleteClickListener,
                                                              TemplateItemDeleteAction.RequestTemplateItemDelete,
                                                              TemplateAddAction.RequestTemplateAdd{
    private String workplaceInspectionTemplateId = "";
    ProgressDialog pd;
    private ArrayList<TemplateItemData> templateItemDatas;
    private ListView itemlist;
    private ItemsAdapter adapter;
    private EditText name;
    private EditText description;
    private View text_name_error;
    private View text_name;
    private boolean pressAdd = false;
    private String nameOld = "";
    private String descriptionOld = "";
    private Dialog dialog_confirm;
    private Button button_cancel_save;
    private Button button_send_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_template);

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
        adb_save.setTitle(getResources().getString(R.string.text_save_change));
        adb_save.setCancelable(true);
        LinearLayout view_save = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_save, null);
        adb_save.setView(view_save);
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
                updateTemplate(false);
            }
        });

        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        text_name_error = findViewById(R.id.text_name_error);
        text_name = findViewById(R.id.text_name);

        TextWatcher inputTextWatcher = new TextWatch();
        name.addTextChangedListener(inputTextWatcher);

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workplaceInspectionTemplateId.equals("") || !nameOld.equals(name.getText().toString())
                        || !descriptionOld.equals(description.getText().toString())) {
                    updateTemplate(false);
                } else {
                    finish();
                }
            }
        });

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameOld.equals(name.getText().toString())
                        || !descriptionOld.equals(description.getText().toString())) {
                    updateTemplate(true);
                } else {
                    addItem();
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

        Intent intent = getIntent();
        workplaceInspectionTemplateId = intent.getStringExtra("workplaceInspectionTemplateId");
        if (workplaceInspectionTemplateId == null) {
            workplaceInspectionTemplateId = "";
        }
        if (!workplaceInspectionTemplateId.equals("")) {
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            name.setText(nameOld);
            description.setText(descriptionOld);
        }

        itemlist = (ListView) findViewById(R.id.itemlist);
        adapter = new ItemsAdapter(this);
        itemlist.setAdapter(adapter);

        updateViews();
    }

    private void updateTemplate(boolean add) {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("")) {
            allOk = false;
        }

        if (allOk) {
            pd.show();

            pressAdd = add;
            new TemplateAddAction(TemplateActivity.this, workplaceInspectionTemplateId, name.getText().toString(), description.getText().toString()).execute();
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
        if (!nameOld.equals(name.getText().toString())
                || !descriptionOld.equals(description.getText().toString())) {
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    private void addItem() {
        hideSoftKeyboard();
        Intent intent = new Intent(TemplateActivity.this, TemplateItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestTemplate(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestTemplateData(ArrayList<TemplateItemData> templatesItemDatas) {
        this.templateItemDatas = templatesItemDatas;
        adapter.setListArray(templatesItemDatas);
        pd.hide();
    }

    @Override
    public void onRootClick(int position) {
        hideSoftKeyboard();
    }

    @Override
    public void onDeleteClick(int position) {
        hideSoftKeyboard();
        pd.show();

        new TemplateItemDeleteAction(TemplateActivity.this, templateItemDatas.get(position).getWorkplaceInspectionTemplateItemId()).execute();
    }

    @Override
    public void onOpenClick(int position) {
        hideSoftKeyboard();
        Intent intent = new Intent(TemplateActivity.this, TemplateItemActivity.class);
        TemplateItemData templateItemData = templateItemDatas.get(position);
        intent.putExtra("name", templateItemData.getName());
        intent.putExtra("description", templateItemData.getDescription());
        intent.putExtra("workplaceInspectionTemplateItemId", templateItemData.getWorkplaceInspectionTemplateItemId());
        intent.putExtra("workplaceInspectionTemplateId", templateItemData.getWorkplaceInspectionTemplateId());
        startActivity(intent);
    }

    @Override
    public void onRequestTemplateItemDelete(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_SHORT).show();
        } else {
            new GetTemplateAction(TemplateActivity.this, workplaceInspectionTemplateId).execute();
        }
    }

    @Override
    public void onRequestTemplateAdd(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            pressAdd = false;
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_SHORT).show();
        } else if (pressAdd) {
            pressAdd = false;
            nameOld = name.getText().toString();
            descriptionOld = description.getText().toString();
            addItem();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!workplaceInspectionTemplateId.equals("")) {
            pd.show();
            new GetTemplateAction(TemplateActivity.this, workplaceInspectionTemplateId).execute();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
