package com.sapphire.activities.workplaceInspection;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.adapters.AssignmentsAdapter;
import com.sapphire.adapters.SpinCategoriesAdapter;
import com.sapphire.adapters.SpinPrioritisAdapter;
import com.sapphire.api.LinkAddAction;
import com.sapphire.api.TaskAddAction;
import com.sapphire.api.AssignAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.CategoryData;
import com.sapphire.models.ItemPriorityData;
import com.sapphire.models.ProfileData;
import com.sapphire.models.TaskData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskActivity extends BaseActivity implements AssignmentsAdapter.OnRootAssignmentsClickListener,
                                                          TaskAddAction.RequestTaskAdd,
                                                          LinkAddAction.RequestLinkAdd,
                                                          AssignAddAction.RequestAssignAdd,
                                                          UpdateAction.RequestUpdate{
    private String id = "";
    private String parentId = "";
    private String taskTypeId = "";
    private ProgressDialog pd;
    private ArrayList<ProfileData> datas = new ArrayList<ProfileData>();
    private RecyclerView assignmentslist;
    private AssignmentsAdapter adapter;
    private EditText name;
    private EditText date;
    private EditText dateend;
    private View image_date_group;
    private View image_dateend_group;
    private View image_time_group;
    private View image_timeend_group;
    private EditText description;
    private View text_name_error;
    private View text_date_error;
    private View text_dateend_error;
    private String nameOld = "";
    private String descriptionOld = "";
    private Long dateOld = 0l;
    private Long dateendOld = 0l;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private static SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    private int myYear = cal.get(Calendar.YEAR);
    private int myMonth = cal.get(Calendar.MONTH);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private int myHour = cal.get(Calendar.HOUR_OF_DAY);
    private int myMinute = cal.get(Calendar.MINUTE);
    private Long dateNew = 0l;
    private Long dateendNew;
    private EditText time;
    private EditText timeend;
    private View text_no;
    private boolean thisStart = false;
    private UserInfo userInfo;
    private EditText category;
    private Spinner spinnerCategory;
    private ArrayList<CategoryData> categories;
    private SpinCategoriesAdapter adapterCategory;
    private boolean clickSpinner = false;
    private String categoryId = "";
    private String categoryIdOld = "";
    private EditText priority;
    private Spinner spinnerPriority;
    private ArrayList<ItemPriorityData> prioritis;
    private SpinPrioritisAdapter adapterPriority;
    private String priorityId = "";
    private String priorityIdOld = "";
    private boolean isCheckName = false;
    private boolean isCheckDate = false;
    private boolean isCheckDateend = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private Double percentComplete = 0d;
    private String linkId = "";
    private TextView text_name;
    private TextView text_description;
    private TextView text_date;
    private TextView text_time;
    private TextView text_dateend;
    private TextView text_timeend;
    private Animation animationErrorDown;
    private Animation animationErrorUpName;
    private Animation animationErrorUpDate;
    private Animation animationErrorUpDateend;
    private boolean showErrorName = false;
    private boolean showErrorDate = false;
    private boolean showErrorDateend = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showName = true;
    private boolean showDescription = true;
    private boolean showDate = true;
    private boolean showTime = true;
    private boolean showDateend = true;
    private boolean showTimeend = true;
    private TextView text_name_hint;
    private TextView text_description_hint;
    private TextView text_date_hint;
    private TextView text_time_hint;
    private TextView text_dateend_hint;
    private TextView text_timeend_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task);

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
                updateTask();
            }
        });

        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        dateend = (EditText) findViewById(R.id.dateend);
        description = (EditText) findViewById(R.id.description);
        text_name_error = findViewById(R.id.text_name_error);
        text_date_error = findViewById(R.id.text_date_error);
        text_dateend_error = findViewById(R.id.text_dateend_error);
        time = (EditText) findViewById(R.id.time);
        timeend = (EditText) findViewById(R.id.timeend);
        image_date_group = findViewById(R.id.image_date_group);
        image_dateend_group = findViewById(R.id.image_dateend_group);
        image_time_group = findViewById(R.id.image_time_group);
        image_timeend_group = findViewById(R.id.image_timeend_group);
        category = (EditText) findViewById(R.id.category);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        priority = (EditText) findViewById(R.id.priority);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        text_name = (TextView) findViewById(R.id.text_name);
        text_description = (TextView) findViewById(R.id.text_description);
        text_date = (TextView) findViewById(R.id.text_date);
        text_time = (TextView) findViewById(R.id.text_start_time);
        text_dateend = (TextView) findViewById(R.id.text_end);
        text_timeend = (TextView) findViewById(R.id.text_end_time);
        text_name_hint = (TextView) findViewById(R.id.text_name_hint);
        text_description_hint = (TextView) findViewById(R.id.text_description_hint);
        text_date_hint = (TextView) findViewById(R.id.text_date_hint);
        text_time_hint = (TextView) findViewById(R.id.text_time_hint);
        text_dateend_hint = (TextView) findViewById(R.id.text_dateend_hint);
        text_timeend_hint = (TextView) findViewById(R.id.text_timeend_hint);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpName = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpDate = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpDateend = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpName.setAnimationListener(animationErrorUpNameListener);
        animationErrorUpDate.setAnimationListener(animationErrorUpDateListener);
        animationErrorUpDateend.setAnimationListener(animationErrorUpDateendListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        name.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        description.addTextChangedListener(inputTextWatcher);

        format = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");

        userInfo = UserInfo.getUserInfo();

        text_no = findViewById(R.id.text_no);

        categories = new ArrayList<>();
        categories.add(new CategoryData());
        categories.addAll(userInfo.geParameterCategoryDatas());

        adapterCategory = new SpinCategoriesAdapter(this, R.layout.spinner_list_item_black);
        spinnerCategory.setAdapter(adapterCategory);
        adapterCategory.setValues(categories);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                clickSpinner = true;
                spinnerCategory.performClick();
            }
        });
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                category.setText(categories.get(position).getName());
                categoryId = categories.get(position).getId();
                clickSpinner = false;

                updateVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*
        template.postDelayed(new Runnable() {
            @Override
            public void run() {
                template.setText(getResources().getString(R.string.text_history1));
                spinnerPeriod.setSelection(0,false);
            }
        }, 10);
        */

        prioritis = new ArrayList<>();
        prioritis.add(new ItemPriorityData());
        prioritis.add(new ItemPriorityData("1", "Critical"));
        prioritis.add(new ItemPriorityData("2", "High"));
        prioritis.add(new ItemPriorityData("3", "Medium"));
        prioritis.add(new ItemPriorityData("4", "Low"));

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

                updateVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*
        template.postDelayed(new Runnable() {
            @Override
            public void run() {
                template.setText(getResources().getString(R.string.text_history1));
                spinnerPeriod.setSelection(0,false);
            }
        }, 10);
        */

        Intent intent = getIntent();
        id = intent.getStringExtra("taskId");
        if (id == null) {
            id = "";
        }
        parentId = intent.getStringExtra("parentId");
        if (parentId == null) {
            parentId = "";
        }
        linkId = intent.getStringExtra("linkId");
        if (linkId == null) {
            linkId = "";
        }
        if (!id.equals("")) {
            readonly = intent.getBooleanExtra("readonly", false);
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            dateOld = intent.getLongExtra("date", 0l);
            dateendOld = intent.getLongExtra("dateend", 0l);
            dateNew = dateOld;
            dateendNew = dateendOld;
            name.setText(nameOld);
            description.setText(descriptionOld);
            categoryIdOld = intent.getStringExtra("categoryId");
            priorityIdOld = intent.getStringExtra("priorityId");
            categoryId = categoryIdOld;
            priorityId = priorityIdOld;
            taskTypeId = intent.getStringExtra("taskTypeId");
            percentComplete = intent.getDoubleExtra("percentComplete", 0d);
            if (!categoryIdOld.equals("")) {
                int categoryPosition = 0;
                for (int i = 0; i < categories.size(); i ++) {
                    if (categories.get(i).getId().equals(categoryIdOld)) {
                        categoryPosition = i;
                        break;
                    }
                }
                category.setText(categories.get(categoryPosition).getName());
                final int finalCategoryPosition = categoryPosition;
                category.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spinnerCategory.setSelection(finalCategoryPosition,false);
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

            datas.clear();
            for (ProfileData item: userInfo.getAssignedProfiles()) {
                datas.add(new ProfileData(item.getProfileId(), item.getName(), item.getPresence()));
            }

            if (dateOld != 0l) {
                try {
                    Date thisdaten = new Date();
                    thisdaten.setTime(dateOld);
                    String datet = format.format(thisdaten);
                    date.setText(datet.substring(0,10));
                    time.setText(datet.substring(11));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (dateendOld != 0l) {
                try {
                    Date thisdaten = new Date();
                    thisdaten.setTime(dateendOld);
                    String datet = format.format(thisdaten);
                    dateend.setText(datet.substring(0,10));
                    timeend.setText(datet.substring(11));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (name.getText().length() != 0) {
                showName = false;
            }
            if (description.getText().length() != 0) {
                showDescription = false;
            }
            if (date.getText().length() != 0) {
                showDate = false;
            }
            if (time.getText().length() != 0) {
                showTime = false;
            }
            if (dateend.getText().length() != 0) {
                showDateend = false;
            }
            if (timeend.getText().length() != 0) {
                showTimeend = false;
            }
        } else {
            taskTypeId = Environment.TaskTypeItemId;
            datas.clear();
            for (ProfileData item: userInfo.getAllAssignedProfiles()) {
                datas.add(new ProfileData(item.getProfileId(), item.getName(), item.getPresence()));
            }
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(true);
            }
        });

        image_date_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(true);
            }
        });

        dateend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(false);
            }
        });

        image_dateend_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate(false);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseTime(true);
            }
        });

        image_time_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseTime(true);
            }
        });

        timeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseTime(false);
            }
        });

        image_timeend_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseTime(false);
            }
        });

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (id.equals("") || notEquals()) {
                    updateTask();
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

        assignmentslist = (RecyclerView) findViewById(R.id.assignmentslist);
        assignmentslist.setNestedScrollingEnabled(false);
        assignmentslist.setLayoutManager(new LinearLayoutManager(TaskActivity.this));

        adapter = new AssignmentsAdapter(this, readonly);
        assignmentslist.setAdapter(adapter);

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

        adapter.setListArray(datas);

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

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(TaskActivity.this);
            }
        });

        UpdateBottom();

        if (readonly) {
            button_ok.setVisibility(View.GONE);
            name.setFocusable(false);
            description.setFocusable(false);
            date.setFocusable(false);
            dateend.setFocusable(false);
            time.setFocusable(false);
            timeend.setFocusable(false);
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

    Animation.AnimationListener animationErrorUpDateListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_date_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    Animation.AnimationListener animationErrorUpDateendListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_dateend_error.setVisibility(View.INVISIBLE);
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

    private boolean notEquals() {
        boolean rezult = !nameOld.equals(name.getText().toString())
                || !descriptionOld.equals(description.getText().toString())
                || !dateOld.equals(dateNew)
                || !dateendOld.equals(dateendNew)
                || !categoryIdOld.equals(categoryId)
                || !priorityIdOld.equals(priorityId);

        if (!rezult) {
            ArrayList<ProfileData> profileDatas = userInfo.getAssignedProfiles();
            if (profileDatas.size() == 0) {
                profileDatas = userInfo.getAllAssignedProfiles();
            }
            if (profileDatas.size() != datas.size()) {
                rezult = true;
            } else {
                for (int i=0; i < datas.size(); i++) {
                    if (profileDatas.size() <= i) {
                        rezult = true;
                        break;
                    } else {
                        if (!datas.get(i).getProfileId().equals(profileDatas.get(i).getProfileId())
                            || datas.get(i).getPresence() != profileDatas.get(i).getPresence()) {
                            rezult = true;
                            break;
                        }
                    }
                }
            }
        }

        return rezult;
    }

    private void updateVisibility() {
        if (datas == null || datas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            assignmentslist.setVisibility(View.GONE);
        } else {
            assignmentslist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
        updateViews();
    }

    private void choiseTime(boolean thisStart) {
        hideSoftKeyboard();

        this.thisStart = thisStart;

        if (this.thisStart) {
            if (time.getText().length() == 0 && showTime) {
                text_time_hint.setVisibility(View.GONE);
                text_time.setVisibility(View.VISIBLE);
                showTime = false;
                text_time.startAnimation(animationUp);
            }
        } else {
            if (timeend.getText().length() == 0 && showTimeend) {
                text_timeend_hint.setVisibility(View.GONE);
                text_timeend.setVisibility(View.VISIBLE);
                showTimeend = false;
                text_timeend.startAnimation(animationUp);
            }
        }

        Date date = null;
        try {
            if (thisStart) {
                date = format.parse("01.01.1980 " + time.getText().toString() + ":00");
            } else {
                date = format.parse("01.01.1980 " + timeend.getText().toString() + ":00");
            }
        } catch (ParseException e) {
            date = new Date();
            //date.setTime((long) mParphones.get(thisposition).get("datein"));
            e.printStackTrace();
        }

        cal.setTime(date);

        myHour = cal.get(Calendar.HOUR_OF_DAY);
        myMinute = cal.get(Calendar.MINUTE);

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener,
                                                                             TimePickerDialog.OnCancelListener {

        private TaskActivity act;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            //final Calendar c = Calendar.getInstance();
            //int hour = c.get(Calendar.HOUR_OF_DAY);
            //int minute = c.get(Calendar.MINUTE);

            act = (TaskActivity) getActivity();

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, act.myHour, act.myMinute, false);
                    //DateFormat.is24HourFormat(getActivity()));
            //.is24HourFormat(getActivity()));
        }

        public void onCancel(DialogInterface dialog) {
            if (act.thisStart) {
                if (act.time.getText().length() == 0 && !act.showTime) {
                    act.text_time.setVisibility(View.INVISIBLE);
                    act.showTime = true;
                    act.text_time_hint.setVisibility(View.VISIBLE);
                    act.text_time_hint.startAnimation(act.animationDown);
                }
            } else {
                if (act.timeend.getText().length() == 0 && !act.showTimeend) {
                    act.text_timeend.setVisibility(View.INVISIBLE);
                    act.showTimeend = true;
                    act.text_timeend_hint.setVisibility(View.VISIBLE);
                    act.text_timeend_hint.startAnimation(act.animationDown);
                }
            }
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String ampm = "AM";
            act.myHour = hourOfDay;
            act.myMinute = minute;
            if (hourOfDay == 0) {
                hourOfDay = 12;
            } else if (hourOfDay == 12) {
                ampm = "PM";
            } else if (hourOfDay >= 13) {
                hourOfDay = hourOfDay - 12;
                ampm = "PM";
            }

            String mHour = "";
            String mMinute = "";
            //if ((hourOfDay+"").length() == 1) {
            //    mHour = "0" + hourOfDay;
            //} else {
                mHour = hourOfDay + "";
            //}
            if ((act.myMinute+"").length() == 1) {
                mMinute = "0" + act.myMinute;
            } else {
                mMinute = act.myMinute+"";
            }

            if (act.thisStart) {
                act.time.setText("" + mHour + ":" + mMinute + " " + ampm);
            } else {
                act.timeend.setText("" + mHour + ":" + mMinute + " " + ampm);
            }

            if (act.thisStart) {
                if (act.timeend.getText().toString().equals("")) {
                    act.timeend.setText("" + mHour + ":" + mMinute + " " + ampm);
                }
            } else {
                if (act.time.getText().toString().equals("")) {
                    act.time.setText("" + mHour + ":" + mMinute + " " + ampm);
                }
            }

            Date dateD = null;
            if (act.date.getText().toString().equals("")) {
                dateD = new Date();
                String datet = format.format(dateD);
                act.date.setText(datet.substring(0,10));
            }

            String dateNewstr = act.date.getText().toString() + " " + act.time.getText().toString();
            if (!dateNewstr.equals("")) {
                try {
                    dateD = format.parse(dateNewstr);
                    act.dateNew = dateD.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String dateendNewstr = act.date.getText().toString() + " " + act.timeend.getText().toString();
            if (!dateendNewstr.equals("")) {
                try {
                    dateD = format.parse(dateendNewstr);
                    act.dateendNew = dateD.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            act.updateViews();
        }
    }

    private void choiseDate(boolean thisStart) {
        hideSoftKeyboard();

        this.thisStart = thisStart;

        if (this.thisStart) {
            if (date.getText().length() == 0 && showDate) {
                text_date_hint.setVisibility(View.GONE);
                text_date.setVisibility(View.VISIBLE);
                showDate = false;
                text_date.startAnimation(animationUp);
            }
        } else {
            if (dateend.getText().length() == 0 && showDateend) {
                text_dateend_hint.setVisibility(View.GONE);
                text_dateend.setVisibility(View.VISIBLE);
                showDateend = false;
                text_dateend.startAnimation(animationUp);
            }
        }

        Date dateD = null;
        try {
            dateD = format.parse(date.getText().toString());
        } catch (ParseException e) {
            dateD = new Date();
            e.printStackTrace();
        }

        cal.setTime(dateD);

        myYear = cal.get(Calendar.YEAR);
        myMonth = cal.get(Calendar.MONTH);
        myDay = cal.get(Calendar.DAY_OF_MONTH);

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener,
                                                                             DatePickerDialog.OnCancelListener {

        private TaskActivity act;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            //final Calendar c = Calendar.getInstance();
            //int year = c.get(Calendar.YEAR);
            //int month = c.get(Calendar.MONTH);
            //int day = c.get(Calendar.DAY_OF_MONTH);

            act = (TaskActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onCancel(DialogInterface dialog) {
            if (act.thisStart) {
                if (act.date.getText().length() == 0 && !act.showDate) {
                    act.text_date.setVisibility(View.INVISIBLE);
                    act.showDate = true;
                    act.text_date_hint.setVisibility(View.VISIBLE);
                    act.text_date_hint.startAnimation(act.animationDown);
                }
            } else {
                if (act.dateend.getText().length() == 0 && !act.showDateend) {
                    act.text_dateend.setVisibility(View.INVISIBLE);
                    act.showDateend = true;
                    act.text_dateend_hint.setVisibility(View.VISIBLE);
                    act.text_dateend_hint.startAnimation(act.animationDown);
                }
            }
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            act.myYear = year;
            act.myMonth = month;
            act.myDay = day;
            String mMonth = "";
            String mDay = "";
            if ((act.myDay+"").length() == 1) {
                mDay = "0" + act.myDay;
            } else {
                mDay = act.myDay + "";
            }
            if (((act.myMonth+1)+"").length() == 1) {
                mMonth = "0" + (act.myMonth+1);
            } else {
                mMonth = (act.myMonth+1)+"";
            }

            if (act.thisStart) {
                act.date.setText("" + mDay + "." + mMonth + "." + act.myYear);
            } else {
                act.dateend.setText("" + mDay + "." + mMonth + "." + act.myYear);
            }

            Date dateD = null;
            if (act.thisStart) {
                if (act.time.getText().toString().equals("")) {
                    dateD = new Date();
                    String datet = format.format(dateD);
                    act.time.setText(datet.substring(11));
                }
            } else {
                if (act.timeend.getText().toString().equals("")) {
                    dateD = new Date();
                    String datet = format.format(dateD);
                    act.timeend.setText(datet.substring(11));
                }
            }

            if (act.thisStart) {
                String dateNewstr = act.date.getText().toString() + " " + act.time.getText().toString();
                if (!dateNewstr.equals("")) {
                    try {
                        dateD = format.parse(dateNewstr);
                        act.dateNew = dateD.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                String dateendNewstr = act.dateend.getText().toString() + " " + act.timeend.getText().toString();
                if (!dateendNewstr.equals("")) {
                    try {
                        dateD = format.parse(dateendNewstr);
                        act.dateendNew = dateD.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (act.thisStart) {
                act.isCheckDate = true;
            } else {
                act.isCheckDateend = true;
            }

            act.updateViews();
        }
    }

    private void updateTask() {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("") || date.getText().toString().equals("")
            || dateend.getText().toString().equals("")) {
            isCheckName = true;
            isCheckDate = true;
            isCheckDateend = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            TaskData taskData = new TaskData();
            taskData.setTaskId(id);
            taskData.setParentId(parentId);
            taskData.setTaskTypeId(taskTypeId);
            taskData.setTaskCategoryId(categoryId);
            taskData.setName(name.getText().toString());
            taskData.setDescription(description.getText().toString());
            taskData.setPlannedStartDate(dateNew);
            taskData.setPlannedFinishDate(dateendNew);
            taskData.setPercentComplete(percentComplete);
            try {
                taskData.setPriority(Integer.valueOf(priorityId));
            } catch (Exception e) {
                taskData.setPriority(0);
            }

            new TaskAddAction(TaskActivity.this, taskData).execute();
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
        if (isCheckDate && date.getText().toString().equals("")) {
            text_date_error.setVisibility(View.VISIBLE);
            date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            time.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_date.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_date_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_time.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_time_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorDate) {
                showErrorDate = true;
                text_date_error.startAnimation(animationErrorDown);
            }
        } else if (!date.getText().toString().equals("")) {
            text_date.setVisibility(View.VISIBLE);
            date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            time.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_date.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_date_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_date_hint.setVisibility(View.GONE);
            text_time.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_time_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_time_hint.setVisibility(View.GONE);
            if (showErrorDate) {
                showErrorDate = false;
                text_date_error.startAnimation(animationErrorUpDate);
            }
        }
        if (isCheckDateend && dateend.getText().toString().equals("")) {
            text_dateend_error.setVisibility(View.VISIBLE);
            dateend.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            timeend.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_dateend.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_dateend_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_timeend.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_timeend_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorDateend) {
                showErrorDateend = true;
                text_dateend_error.startAnimation(animationErrorDown);
            }
        } else if (!dateend.getText().toString().equals("")) {
            text_dateend.setVisibility(View.VISIBLE);
            dateend.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            timeend.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_dateend.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_dateend_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_dateend_hint.setVisibility(View.GONE);
            text_timeend.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_timeend_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_timeend_hint.setVisibility(View.GONE);
            if (showErrorDateend) {
                showErrorDateend = false;
                text_dateend_error.startAnimation(animationErrorUpDateend);
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
        if (notEquals()) {
            tittle_message.setText(getResources().getString(R.string.text_save_change));
            button_cancel_save.setText(getResources().getString(R.string.text_no_save));
            button_send_save.setText(getResources().getString(R.string.text_yes_save));
            dialog_confirm.show();
        } else {
            finish();
        }
    }

    @Override
    public void onRootAssignmentsClick(int position) {
        hideSoftKeyboard();
        datas.get(position).setPresence(!datas.get(position).getPresence());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestTaskAdd(String result, String id, String method) {
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
            this.id = id;
            if (method.equals("POST")) {
                new LinkAddAction(TaskActivity.this, id, linkId, Environment.TaskTypeAddItemId).execute();
            } else {
                assignAddAction();
            }
        }
    }

    private void assignAddAction() {
        /*
        boolean existPresence = false;

        for (ProfileData item: datas) {
            if (item.getPresence()) {
                existPresence = true;
                break;
            }
        }

        if (existPresence) {
            new AssignAddAction(TaskActivity.this, id, datas).execute();
        } else {
            pd.hide();

            finish();
        }
        */
        new AssignAddAction(TaskActivity.this, id, datas).execute();
    }

    @Override
    public void onRequestLinkAdd(String result) {
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
            assignAddAction();
        }
    }

    @Override
    public void onRequestAssignAdd(String result) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        text_name.clearAnimation();
        text_description.clearAnimation();
        text_date.clearAnimation();
        text_time.clearAnimation();
        text_dateend.clearAnimation();
        text_timeend.clearAnimation();
        text_name_error.clearAnimation();
        text_date_error.clearAnimation();
        text_dateend_error.clearAnimation();
        text_name_hint.clearAnimation();
        text_description_hint.clearAnimation();
        text_date_hint.clearAnimation();
        text_time_hint.clearAnimation();
        text_dateend_hint.clearAnimation();
        text_timeend_hint.clearAnimation();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        name.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        time.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        dateend.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        timeend.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        unregisterReceiver(br);
    }
}
