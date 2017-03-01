package com.sapphire.activities.workplaceInspection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.adapters.SpinPrioritisAdapter;
import com.sapphire.adapters.SpinStatusesAdapter;
import com.sapphire.api.WorkplaceInspectionItemAddAction;
import com.sapphire.logic.ItemPriorityData;
import com.sapphire.logic.ItemStatusData;
import com.sapphire.logic.UserInfo;
import com.sapphire.logic.WorkplaceInspectionItemData;
import java.util.ArrayList;

public class WorkplaceInspectionItemActivity extends BaseActivity implements WorkplaceInspectionItemAddAction.RequestWorkplaceInspectionItemAdd{
    private String workplaceInspectionItemId = "";
    private String workplaceInspectionId = "";
    private ProgressDialog pd;
    private EditText name;
    private EditText description;
    private View text_name_error;
    private View text_name;
    private String nameOld = "";
    private String descriptionOld = "";
    private int severityOld = 0;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private EditText severity;
    private Spinner spinnerStatus;
    private ArrayList<ItemStatusData> statuses;
    private SpinStatusesAdapter adapterStatus;
    private boolean clickSpinner = false;
    private String statusId = "";
    private String statusIdOld = "";
    private Spinner spinnerPriority;
    private ArrayList<ItemPriorityData> prioritis;
    private SpinPrioritisAdapter adapterPriority;
    private String priorityId = "";
    private String priorityIdOld = "";
    private EditText status;
    private EditText priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workplace_inspection_item);
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

        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        text_name_error = findViewById(R.id.text_name_error);
        text_name = findViewById(R.id.text_name);
        severity = (EditText) findViewById(R.id.severity);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        status = (EditText) findViewById(R.id.status);
        priority = (EditText) findViewById(R.id.priority);

        statuses = new ArrayList<>();
        statuses.addAll(UserInfo.getUserInfo().getItemStatusDatas());

        adapterStatus = new SpinStatusesAdapter(this, R.layout.spinner_list_item_black);
        spinnerStatus.setAdapter(adapterStatus);
        adapterStatus.setValues(statuses);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                clickSpinner = true;
                spinnerStatus.performClick();
            }
        });
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                status.setText(statuses.get(position).getName());
                statusId = statuses.get(position).getWorkplaceInspectionItemStatusId();
                clickSpinner = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prioritis = new ArrayList<>();
        prioritis.addAll(UserInfo.getUserInfo().getItemPriorityDatas());

        adapterPriority = new SpinPrioritisAdapter(this, R.layout.spinner_list_item_black);
        spinnerPriority.setAdapter(adapterPriority);
        adapterPriority.setValues(prioritis);
        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                clickSpinner = true;
                spinnerPriority.performClick();
            }
        });
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                priority.setText(prioritis.get(position).getName());
                priorityId = prioritis.get(position).getWorkplaceInspectionItemPriorityId();
                clickSpinner = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextWatcher inputTextWatcher = new TextWatch();
        name.addTextChangedListener(inputTextWatcher);

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int severityNew = 0;
                if (!severity.getText().toString().equals("")) {
                    severityNew = Integer.valueOf(severity.getText().toString());
                }
                if (workplaceInspectionItemId.equals("") || !nameOld.equals(name.getText().toString())
                        || !descriptionOld.equals(description.getText().toString())
                        || severityOld != severityNew
                        || !statusIdOld.equals(statusId)
                        || !priorityIdOld.equals(priorityId)) {
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

        Intent intent = getIntent();
        workplaceInspectionItemId = intent.getStringExtra("workplaceInspectionItemId");
        if (workplaceInspectionItemId == null) {
            workplaceInspectionItemId = "";
        }
        workplaceInspectionId = intent.getStringExtra("workplaceInspectionId");
        if (workplaceInspectionId == null) {
            workplaceInspectionId = "";
        }
        if (!workplaceInspectionItemId.equals("")) {
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            name.setText(nameOld);
            description.setText(descriptionOld);
            int severityInt = intent.getIntExtra("severity", 0);
            if (severityInt != 0) {
                severityOld = severityInt;
                severity.setText(String.valueOf(severityInt));
            }
            statusIdOld = intent.getStringExtra("workplaceInspectionItemStatusId");
            priorityIdOld = intent.getStringExtra("workplaceInspectionItemPriorityId");
            statusId = statusIdOld;
            priorityId = priorityIdOld;

            if (!statusIdOld.equals("")) {
                int statusPosition = 0;
                for (int i = 0; i < statuses.size(); i ++) {
                    if (statuses.get(i).getWorkplaceInspectionItemStatusId().equals(statusIdOld)) {
                        statusPosition = i;
                        break;
                    }
                }
                status.setText(statuses.get(statusPosition).getName());
                final int finalStatusPosition = statusPosition;
                status.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spinnerStatus.setSelection(finalStatusPosition,false);
                    }
                }, 10);
            }
            if (!priorityIdOld.equals("")) {
                int priorityPosition = 0;
                for (int i = 0; i < prioritis.size(); i ++) {
                    if (prioritis.get(i).getWorkplaceInspectionItemPriorityId().equals(priorityIdOld)) {
                        priorityPosition = i;
                        break;
                    }
                }
                priority.setText(prioritis.get(priorityPosition).getName());
                final int finalPriorityPosition = priorityPosition;
                priority.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spinnerPriority.setSelection(finalPriorityPosition,false);
                    }
                }, 10);
            }
            priority.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //status.setText(getResources().getString(R.string.text_history1));
                    //spinnerPeriod.setSelection(0,false);
                }
            }, 10);
        }

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

            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setName(name.getText().toString());
            workplaceInspectionItemData.setDescription(description.getText().toString());
            workplaceInspectionItemData.setWorkplaceInspectionItemId(workplaceInspectionItemId);
            workplaceInspectionItemData.setWorkplaceInspectionId(workplaceInspectionId);
            workplaceInspectionItemData.setSeverity(severity.getText().toString());
            workplaceInspectionItemData.setStatus(new ItemStatusData(statusId));
            workplaceInspectionItemData.setPriority(new ItemPriorityData(priorityId));

            new WorkplaceInspectionItemAddAction(WorkplaceInspectionItemActivity.this, workplaceInspectionItemData, true, 0).execute();
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
        int severityNew = 0;
        if (!severity.getText().toString().equals("")) {
            severityNew = Integer.valueOf(severity.getText().toString());
        }
        if (!nameOld.equals(name.getText().toString())
            || !descriptionOld.equals(description.getText().toString())
            || severityOld != severityNew
            || !statusIdOld.equals(statusId)
            || !priorityIdOld.equals(priorityId)) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionItemAdd(String result, boolean neddclosepd, int ihms) {
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
