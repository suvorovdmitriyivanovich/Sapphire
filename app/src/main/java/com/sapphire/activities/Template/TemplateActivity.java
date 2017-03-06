package com.sapphire.activities.template;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.adapters.ItemsAdapter;
import com.sapphire.api.GetTemplateAction;
import com.sapphire.api.TemplateAddAction;
import com.sapphire.api.TemplateItemDeleteAction;
import com.sapphire.logic.TemplateData;
import com.sapphire.logic.TemplateItemData;
import java.util.ArrayList;

public class TemplateActivity extends BaseActivity implements GetTemplateAction.RequestTemplate,
                                                              GetTemplateAction.RequestTemplateData,
                                                              ItemsAdapter.OnRootClickListener,
                                                              ItemsAdapter.OnOpenClickListener,
                                                              ItemsAdapter.OnDeleteClickListener,
                                                              TemplateItemDeleteAction.RequestTemplateItemDelete,
                                                              TemplateAddAction.RequestTemplateAdd,
                                                              TemplateAddAction.RequestTemplateAddData{
    private String templateId = "";
    ProgressDialog pd;
    private ArrayList<TemplateItemData> templateItemDatas;
    private RecyclerView itemlist;
    private ItemsAdapter adapter;
    private EditText name;
    private EditText description;
    private View text_name_error;
    private View text_name;
    private boolean pressAdd = false;
    private String nameOld = "";
    private String descriptionOld = "";
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private boolean deleteItem = false;
    private int currentPosition = 0;
    private View text_no;
    private String typeId;

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
                deleteItem = false;
            }
        });
        dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                deleteItem = false;
            }
        });

        button_cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
                if (!deleteItem) {
                    finish();
                }
                deleteItem = false;
            }
        });

        button_send_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
                if (deleteItem) {
                    deleteItem = false;
                    pd.show();

                    new TemplateItemDeleteAction(TemplateActivity.this, templateItemDatas.get(currentPosition).getTemplateItemId(), typeId).execute();
                } else {
                    updateTemplate(false);
                }
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
                if (templateId.equals("") || !nameOld.equals(name.getText().toString())
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
                if (name.getText().toString().equals("")) {
                    return;
                }
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
        typeId = intent.getStringExtra("typeId");
        templateId = intent.getStringExtra("templateId");
        if (templateId == null) {
            templateId = "";
        }
        if (!templateId.equals("")) {
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            name.setText(nameOld);
            description.setText(descriptionOld);
        }

        itemlist = (RecyclerView) findViewById(R.id.itemlist);
        itemlist.setNestedScrollingEnabled(false);
        itemlist.setLayoutManager(new LinearLayoutManager(TemplateActivity.this));

        adapter = new ItemsAdapter(this);
        itemlist.setAdapter(adapter);

        updateViews();

        text_no = findViewById(R.id.text_no);

        updateVisibility();
    }

    public void updateVisibility() {
        if (templateItemDatas == null || templateItemDatas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            itemlist.setVisibility(View.GONE);
        } else {
            itemlist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
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
            new TemplateAddAction(TemplateActivity.this, templateId, name.getText().toString(), description.getText().toString(), typeId).execute();
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
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    private void addItem() {
        hideSoftKeyboard();
        Intent intent = new Intent(TemplateActivity.this, TemplateItemActivity.class);
        intent.putExtra("typeId", typeId);
        intent.putExtra("templateId", templateId);
        startActivity(intent);
    }

    @Override
    public void onRequestTemplate(String result) {
        updateVisibility();
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestTemplateData(ArrayList<TemplateItemData> templatesItemDatas) {
        this.templateItemDatas = templatesItemDatas;
        adapter.setListArray(templatesItemDatas);

        updateVisibility();

        pd.hide();
    }

    @Override
    public void onRootClick(int position) {
        hideSoftKeyboard();
    }

    @Override
    public void onDeleteClick(int position) {
        hideSoftKeyboard();

        currentPosition = position;
        deleteItem = true;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onOpenClick(int position) {
        hideSoftKeyboard();
        Intent intent = new Intent(TemplateActivity.this, TemplateItemActivity.class);
        TemplateItemData templateItemData = templateItemDatas.get(position);
        intent.putExtra("name", templateItemData.getName());
        intent.putExtra("description", templateItemData.getDescription());
        intent.putExtra("templateItemId", templateItemData.getTemplateItemId());
        intent.putExtra("templateId", templateItemData.getTemplateId());
        intent.putExtra("typeId", typeId);
        startActivity(intent);
    }

    @Override
    public void onRequestTemplateItemDelete(String result) {
        dialog_confirm.dismiss();
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            new GetTemplateAction(TemplateActivity.this, templateId, typeId).execute();
        }
    }

    @Override
    public void onRequestTemplateAddData(TemplateData templateData) {
        pd.hide();
        if (pressAdd) {
            pressAdd = false;
            nameOld = name.getText().toString();
            descriptionOld = description.getText().toString();
            templateId = templateData.getTemplateId();
            addItem();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestTemplateAdd(String result) {
        pd.hide();

        pressAdd = false;
        Toast.makeText(getBaseContext(), result,
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!templateId.equals("")) {
            pd.show();
            new GetTemplateAction(TemplateActivity.this, templateId, typeId).execute();
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
