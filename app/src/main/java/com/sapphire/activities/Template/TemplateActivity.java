package com.sapphire.activities.template;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.adapters.ItemsAdapter;
import com.sapphire.api.GetTemplateAction;
import com.sapphire.api.TemplateAddAction;
import com.sapphire.api.TemplateItemDeleteAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.TemplateData;
import com.sapphire.models.TemplateItemData;
import java.util.ArrayList;

public class TemplateActivity extends BaseActivity implements GetTemplateAction.RequestTemplate,
                                                              GetTemplateAction.RequestTemplateData,
                                                              ItemsAdapter.OnRootClickListener,
                                                              ItemsAdapter.OnOpenClickListener,
                                                              ItemsAdapter.OnDeleteClickListener,
                                                              TemplateItemDeleteAction.RequestTemplateItemDelete,
                                                              TemplateAddAction.RequestTemplateAdd,
                                                              TemplateAddAction.RequestTemplateAddData,
                                                              UpdateAction.RequestUpdate{
    private String templateId = "";
    ProgressDialog pd;
    private ArrayList<TemplateItemData> templateItemDatas;
    private RecyclerView itemlist;
    private ItemsAdapter adapter;
    private EditText name;
    private EditText description;
    private View text_name_error;
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
    private boolean isCheckName = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_name;
    private TextView text_description;
    private Animation animationErrorDown;
    private Animation animationErrorUpName;
    private boolean showErrorName = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showName = true;
    private boolean showDescription = true;
    private TextView text_name_hint;
    private TextView text_description_hint;
    private TextView text_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_template);

        text_header = (TextView) findViewById(R.id.text_header);

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
        text_name = (TextView) findViewById(R.id.text_name);
        text_name_hint = (TextView) findViewById(R.id.text_name_hint);
        text_description = (TextView) findViewById(R.id.text_description);
        text_description_hint = (TextView) findViewById(R.id.text_description_hint);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpName = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpName.setAnimationListener(animationErrorUpNameListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        name.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        description.addTextChangedListener(inputTextWatcher);

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
                    isCheckName = true;
                    updateViews();
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
            readonly = intent.getBooleanExtra("readonly", false);
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            name.setText(nameOld);
            description.setText(descriptionOld);
            if (name.getText().length() != 0) {
                showName = false;
            }
            if (description.getText().length() != 0) {
                showDescription = false;
            }
        }

        if (typeId.equals(getResources().getString(R.string.text_workplace_templates))) {
            text_header.setText(getResources().getString(R.string.text_template));
        } else if (typeId.equals(getResources().getString(R.string.text_meetings_templates))) {
            text_header.setText(getResources().getString(R.string.text_meeting_template));
        }

        itemlist = (RecyclerView) findViewById(R.id.itemlist);
        itemlist.setNestedScrollingEnabled(false);
        itemlist.setLayoutManager(new LinearLayoutManager(TemplateActivity.this));

        adapter = new ItemsAdapter(this, readonly);
        itemlist.setAdapter(adapter);

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

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && description.getText().length() == 0 && showDescription) {
                    text_description_hint.setVisibility(View.GONE);
                    text_description.setVisibility(View.VISIBLE);
                    showDescription = false;
                    text_description.startAnimation(animationUp);
                } else if (!hasFocus && description.getText().length() == 0 && !showDescription) {
                    text_description.setVisibility(View.INVISIBLE);
                    showDescription = true;
                    text_description_hint.setVisibility(View.VISIBLE);
                    text_description_hint.startAnimation(animationDown);
                }
            }
        });

        updateViews();

        text_no = findViewById(R.id.text_no);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(TemplateActivity.this);
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
            add.setVisibility(View.GONE);
            button_ok.setVisibility(View.GONE);
            name.setFocusable(false);
            description.setFocusable(false);
        }
    }

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

    private void UpdateBottom() {
        if (Sapphire.getInstance().getNeedUpdate()) {
            par_nointernet_group.height = GetPixelFromDips(56);
        } else {
            par_nointernet_group.height = 0;
        }
        nointernet_group.setLayoutParams(par_nointernet_group);
        nointernet_group.requestLayout();
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
            isCheckName = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            pressAdd = add;
            new TemplateAddAction(TemplateActivity.this, templateId, name.getText().toString(), description.getText().toString(), typeId).execute();
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
                isCheckName = true;
            }
            updateViews();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    private void updateViews() {
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
        if (!description.getText().toString().equals("")) {
            text_description.setVisibility(View.VISIBLE);
            text_description_hint.setVisibility(View.GONE);
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
        Intent intent = new Intent(TemplateActivity.this, TemplateItemActivity.class);
        TemplateItemData templateItemData = templateItemDatas.get(position);
        intent.putExtra("readonly", true);
        intent.putExtra("name", templateItemData.getName());
        intent.putExtra("description", templateItemData.getDescription());
        intent.putExtra("templateItemId", templateItemData.getTemplateItemId());
        intent.putExtra("templateId", templateItemData.getTemplateId());
        intent.putExtra("typeId", typeId);
        startActivity(intent);
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

        if (!templateId.equals("")) {
            pd.show();
            new GetTemplateAction(TemplateActivity.this, templateId, typeId).execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        text_name.clearAnimation();
        text_description.clearAnimation();
        text_name_error.clearAnimation();
        text_name_hint.clearAnimation();
        text_description_hint.clearAnimation();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        name.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        description.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        unregisterReceiver(br);
    }
}
