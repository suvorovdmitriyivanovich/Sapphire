package com.dealerpilothr.activities.workplaceInspection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
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

import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.ItemPriorityData;
import com.dealerpilothr.models.ItemStatusData;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.adapters.SpinPrioritisAdapter;
import com.dealerpilothr.adapters.SpinStatusesAdapter;
import com.dealerpilothr.adapters.SpinStringAdapter;
import com.dealerpilothr.api.WorkplaceInspectionItemAddAction;
import com.dealerpilothr.db.DBHelper;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.WorkplaceInspectionItemData;
import java.util.ArrayList;

public class WorkplaceInspectionItemActivity extends BaseActivity implements WorkplaceInspectionItemAddAction.RequestWorkplaceInspectionItemAdd,
        UpdateAction.RequestUpdate {
    private String idloc = "";
    private String workplaceInspectionItemId = "";
    private String workplaceInspectionId = "";
    private ProgressDialog pd;
    private EditText name;
    private EditText description;
    private EditText comments;
    private EditText recommendedActions;
    private View text_name_error;
    private String nameOld = "";
    private String descriptionOld = "";
    private String commentsOld = "";
    private String recommendedActionsOld = "";
    private int severityOld = 0;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private EditText severity;
    private Spinner spinnerStatus;
    private ArrayList<ItemStatusData> statuses;
    private SpinStatusesAdapter adapterStatus;
    private Spinner spinnerSeverity;
    private ArrayList<String> severitis;
    private SpinStringAdapter adapterSeverity;
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
    private boolean isCheckName = false;
    private boolean isCheckStatus = false;
    private View text_status_error;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private WorkplaceInspectionItemData workplaceInspectionItemData;
    private BroadcastReceiver br;
    private TextView text_nointernet;
    private TextView text_setinternet;
    private boolean setUpdateAll = false;
    private boolean readonly = false;
    private TextView text_name;
    private TextView text_description;
    private TextView text_comments;
    private TextView text_recommended_actions;
    private Animation animationErrorDown;
    private Animation animationErrorUpName;
    private Animation animationErrorUpStatus;
    private boolean showErrorName = false;
    private boolean showErrorStatus = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showName = true;
    private boolean showDescription = true;
    private boolean showComments = true;
    private boolean showRecommendedActions = true;
    private TextView text_name_hint;
    private TextView text_description_hint;
    private TextView text_comments_hint;
    private TextView text_recommended_actions_hint;
    private TextView text_status;

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
        comments = (EditText) findViewById(R.id.comments);
        recommendedActions = (EditText) findViewById(R.id.recommendedActions);
        text_name_error = findViewById(R.id.text_name_error);
        text_status_error = findViewById(R.id.text_status_error);
        severity = (EditText) findViewById(R.id.severity);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        spinnerSeverity = (Spinner) findViewById(R.id.spinnerSeverity);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        status = (EditText) findViewById(R.id.status);
        priority = (EditText) findViewById(R.id.priority);
        text_name = (TextView) findViewById(R.id.text_name);
        text_status = (TextView) findViewById(R.id.text_status);
        text_description = (TextView) findViewById(R.id.text_description);
        text_comments = (TextView) findViewById(R.id.text_comments);
        text_recommended_actions = (TextView) findViewById(R.id.text_recommended_actions);
        text_name_hint = (TextView) findViewById(R.id.text_name_hint);
        text_description_hint = (TextView) findViewById(R.id.text_description_hint);
        text_comments_hint = (TextView) findViewById(R.id.text_comments_hint);
        text_recommended_actions_hint = (TextView) findViewById(R.id.text_recommended_actions_hint);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpName = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpStatus = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpName.setAnimationListener(animationErrorUpNameListener);
        animationErrorUpStatus.setAnimationListener(animationErrorUpStatusListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        name.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        description.addTextChangedListener(inputTextWatcher);
        comments.addTextChangedListener(inputTextWatcher);
        recommendedActions.addTextChangedListener(inputTextWatcher);

        statuses = new ArrayList<>();
        statuses.addAll(UserInfo.getUserInfo().getItemStatusDatas());

        adapterStatus = new SpinStatusesAdapter(this, R.layout.spinner_list_item_black);
        spinnerStatus.setAdapter(adapterStatus);
        adapterStatus.setValues(statuses);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                //if (true) {
                    return;
                }
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
                updateViews();
                clickSpinner = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        severitis = new ArrayList<>();
        severitis.add("");
        severitis.add("1");
        severitis.add("2");
        severitis.add("3");
        severitis.add("4");
        severitis.add("5");

        adapterSeverity = new SpinStringAdapter(this, R.layout.spinner_list_item_black);
        spinnerSeverity.setAdapter(adapterSeverity);
        adapterSeverity.setValues(severitis);
        severity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                //if (true) {
                    return;
                }
                clickSpinner = true;
                spinnerSeverity.performClick();
            }
        });
        spinnerSeverity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                severity.setText(severitis.get(position));
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
                if (readonly) {
                //if (true) {
                    return;
                }
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

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int severityNew = 0;
                if (!severity.getText().toString().equals("")) {
                    severityNew = Integer.valueOf(severity.getText().toString());
                }
                if ((workplaceInspectionItemId.equals("") && idloc.equals("")) || !nameOld.equals(name.getText().toString())
                        || !descriptionOld.equals(description.getText().toString())
                        || !commentsOld.equals(comments.getText().toString())
                        || !recommendedActionsOld.equals(recommendedActions.getText().toString())
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
        idloc = intent.getStringExtra("idloc");
        if (idloc == null) {
            idloc = "";
        }
        if (!workplaceInspectionItemId.equals("") || !idloc.equals("")) {
            readonly = intent.getBooleanExtra("readonly", false);
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            commentsOld = intent.getStringExtra("comments");
            recommendedActionsOld = intent.getStringExtra("recommendedActions");
            name.setText(nameOld);
            description.setText(descriptionOld);
            comments.setText(commentsOld);
            recommendedActions.setText(recommendedActionsOld);
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
            /*
            priority.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //status.setText(getResources().getString(R.string.text_history1));
                    //spinnerPeriod.setSelection(0,false);
                }
            }, 10);
            */
            if (name.getText().length() != 0) {
                showName = false;
            }
            if (description.getText().length() != 0) {
                showDescription = false;
            }
            if (comments.getText().length() != 0) {
                showComments = false;
            }
            if (recommendedActions.getText().length() != 0) {
                showRecommendedActions = false;
            }
        }

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        text_nointernet = (TextView) findViewById(R.id.text_nointernet);
        text_setinternet = (TextView) findViewById(R.id.text_setinternet);
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setUpdateAll) {
                    pd.show();

                    new UpdateAction(WorkplaceInspectionItemActivity.this);
                } else {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
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

        comments.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && comments.getText().length() == 0 && showComments) {
                    text_comments_hint.setVisibility(View.GONE);
                    text_comments.setVisibility(View.VISIBLE);
                    showComments = false;
                    text_comments.startAnimation(animationUp);
                } else if (!hasFocus && comments.getText().length() == 0 && !showComments) {
                    text_comments.setVisibility(View.INVISIBLE);
                    showComments = true;
                    text_comments_hint.setVisibility(View.VISIBLE);
                    text_comments_hint.startAnimation(animationDown);
                }
            }
        });

        recommendedActions.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && recommendedActions.getText().length() == 0 && showRecommendedActions) {
                    text_recommended_actions_hint.setVisibility(View.GONE);
                    text_recommended_actions.setVisibility(View.VISIBLE);
                    showRecommendedActions = false;
                    text_recommended_actions.startAnimation(animationUp);
                } else if (!hasFocus && recommendedActions.getText().length() == 0 && !showRecommendedActions) {
                    text_recommended_actions.setVisibility(View.INVISIBLE);
                    showRecommendedActions = true;
                    text_recommended_actions_hint.setVisibility(View.VISIBLE);
                    text_recommended_actions_hint.startAnimation(animationDown);
                }
            }
        });

        name.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        description.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        status.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        severity.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        priority.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        comments.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        recommendedActions.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);

        updateViews();

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
            name.setFocusable(false);
            description.setFocusable(false);
            button_ok.setVisibility(View.GONE);
            status.setFocusable(false);
            severity.setFocusable(false);
            priority.setFocusable(false);
            comments.setFocusable(false);
            recommendedActions.setFocusable(false);
        }
        //status.setFocusable(false);
        //severity.setFocusable(false);
        //priority.setFocusable(false);
        //comments.setFocusable(false);
        //recommendedActions.setFocusable(false);
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

    Animation.AnimationListener animationErrorUpStatusListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_status_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    private void UpdateBottom() {
        text_nointernet.setText(getResources().getString(R.string.text_need_internet));
        text_setinternet.setText(getResources().getString(R.string.text_setinternet));
        setUpdateAll = false;
        if (NetRequests.getNetRequests().isOnline(false)) {
            if (Dealerpilothr.getInstance().getNeedUpdate()) {
                setUpdateAll = true;
                text_nointernet.setText(getResources().getString(R.string.text_exits_nosynchronize));
                text_setinternet.setText(getResources().getString(R.string.text_synchronize));
                par_nointernet_group.height = GetPixelFromDips(56);
            } else {
                par_nointernet_group.height = 0;
            }
        } else {
            par_nointernet_group.height = GetPixelFromDips(56);
        }
        nointernet_group.setLayoutParams(par_nointernet_group);
        nointernet_group.requestLayout();
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void saveChanged() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("")) {
            isCheckName = true;
            updateViews();
            allOk = false;
        }

        if (status.getText().toString().equals("")) {
            isCheckStatus = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setId(idloc);
            workplaceInspectionItemData.setName(name.getText().toString());
            workplaceInspectionItemData.setDescription(description.getText().toString());
            workplaceInspectionItemData.setComments(comments.getText().toString());
            workplaceInspectionItemData.setRecommendedActions(recommendedActions.getText().toString());
            workplaceInspectionItemData.setWorkplaceInspectionItemId(workplaceInspectionItemId);
            workplaceInspectionItemData.setWorkplaceInspectionId(workplaceInspectionId);
            workplaceInspectionItemData.setSeverity(severity.getText().toString());
            workplaceInspectionItemData.setStatus(new ItemStatusData(statusId));
            workplaceInspectionItemData.setPriority(new ItemPriorityData(priorityId));

            new WorkplaceInspectionItemAddAction(WorkplaceInspectionItemActivity.this, workplaceInspectionItemData, true, 0, "").execute();
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
        if (isCheckStatus && status.getText().toString().equals("")) {
            text_status_error.setVisibility(View.VISIBLE);
            status.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_status.setTextColor(ContextCompat.getColor(this, R.color.red));
            status.setHintTextColor(ContextCompat.getColor(this, R.color.red));
            status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_dropdown_red, 0);
            if (!showErrorStatus) {
                showErrorStatus = true;
                text_status_error.startAnimation(animationErrorDown);
            }
        } else if (!status.getText().toString().equals("")) {
            status.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_status.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            status.setHintTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_dropdown, 0);
            if (showErrorStatus) {
                showErrorStatus = false;
                text_status_error.startAnimation(animationErrorUpStatus);
            }
        }
        if (!description.getText().toString().equals("")) {
            text_description.setVisibility(View.VISIBLE);
            text_description_hint.setVisibility(View.GONE);
        }
        if (!comments.getText().toString().equals("")) {
            text_comments.setVisibility(View.VISIBLE);
            text_comments_hint.setVisibility(View.GONE);
        }
        if (!recommendedActions.getText().toString().equals("")) {
            text_recommended_actions.setVisibility(View.VISIBLE);
            text_recommended_actions_hint.setVisibility(View.GONE);
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
        int severityNew = 0;
        if (!severity.getText().toString().equals("")) {
            severityNew = Integer.valueOf(severity.getText().toString());
        }
        if (!nameOld.equals(name.getText().toString())
            || !descriptionOld.equals(description.getText().toString())
            || !commentsOld.equals(comments.getText().toString())
            || !recommendedActionsOld.equals(recommendedActions.getText().toString())
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
    public void onRequestWorkplaceInspectionItemAdd(String result, boolean neddclosepd, int ihms, String workplaceInspectionItemId) {
        pd.hide();
        if (!result.equals("OK")) {
            if (result.equals(getResources().getString(R.string.text_need_internet))) {
                if (!workplaceInspectionItemData.getId().equals("")) {
                    DBHelper.getInstance(Dealerpilothr.getInstance()).changeWorkplaceInspectionItem(workplaceInspectionItemData);
                } else {
                    DBHelper.getInstance(Dealerpilothr.getInstance()).addWorkplaceInspectionItem(workplaceInspectionItemData);
                }
                finish();
            } else {
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
            }
        } else {
            if (!workplaceInspectionItemData.getId().equals("")) {
                DBHelper.getInstance(Dealerpilothr.getInstance()).deleteWorkplaceInspectionItem(workplaceInspectionItemData.getId());
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            this.workplaceInspectionItemId = workplaceInspectionItemId;

            Dealerpilothr.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();

            pd.hide();
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
    protected void onPause() {
        super.onPause();
        text_name.clearAnimation();
        text_description.clearAnimation();
        text_comments.clearAnimation();
        text_recommended_actions.clearAnimation();
        text_name_error.clearAnimation();
        text_status_error.clearAnimation();
        text_name_hint.clearAnimation();
        text_description_hint.clearAnimation();
        text_comments_hint.clearAnimation();
        text_recommended_actions_hint.clearAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        name.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        status.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        unregisterReceiver(br);
    }
}
