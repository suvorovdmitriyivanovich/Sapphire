package com.sapphire.activities.meeting;

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
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.sapphire.adapters.TopicsAdapter;
import com.sapphire.adapters.MeetingMembersAdapter;
import com.sapphire.adapters.SpinTemplatesAdapter;
import com.sapphire.api.GetNextMeetingsAction;
import com.sapphire.api.GetNextWorkplaceInspectionsAction;
import com.sapphire.api.MeetingAddAction;
import com.sapphire.api.EventAddAction;
import com.sapphire.api.GetTemplateAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.MeetingData;
import com.sapphire.models.MemberData;
import com.sapphire.models.TopicData;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.TemplateData;
import com.sapphire.models.TemplateItemData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MeetingActivity extends BaseActivity implements MeetingMembersAdapter.OnRootMeetingMembersClickListener,
                                                             TopicsAdapter.OnRootTopicsClickListener,
                                                             TopicsAdapter.OnOpenTopicsClickListener,
                                                             TopicsAdapter.OnDeleteTopicsClickListener,
                                                             MeetingAddAction.RequestMeetingAdd,
                                                             EventAddAction.RequestEventAdd,
                                                             GetTemplateAction.RequestTemplate,
                                                             GetTemplateAction.RequestTemplateData,
                                                             GetNextMeetingsAction.RequestNextMeetings,
                                                             GetNextWorkplaceInspectionsAction.RequestNextWorkplaceInspections,
                                                             UpdateAction.RequestUpdate{
    private String id = "";
    private ProgressDialog pd;
    private ArrayList<MemberData> datas = new ArrayList<MemberData>();
    private ArrayList<TopicData> datasTopic = new ArrayList<TopicData>();
    private RecyclerView memberslist;
    private RecyclerView topicslist;
    private MeetingMembersAdapter adapter;
    private TopicsAdapter adapterTopics;
    private EditText name;
    private EditText date;
    private EditText location;
    private View text_name_error;
    private View text_location_error;
    private View text_date_error;
    private View text_topics_error;
    private String nameOld = "";
    private String locationOld = "";
    private boolean postedOld = false;
    private Long dateOld = 0l;
    private Long dateendOld = 0l;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private boolean deleteItem = false;
    private int currentPosition = 0;
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
    private EditText endtime;
    private View text_no;
    private TextView text_topics_no;
    private boolean thisStart = false;
    private CheckBox posted;
    private UserInfo userInfo;
    private EditText template;
    private Spinner spinnerTemplate;
    private ArrayList<TemplateData> templates;
    private SpinTemplatesAdapter adapterTemplate;
    private boolean clickSpinner = false;
    private String templateId = "";
    private View text_template;
    private String nextMeeting = "-";
    private String nextWorkplaceInspection = "-";
    private TextView nextDate;
    private boolean isCheckName = false;
    private boolean isCheckLocation = false;
    private boolean isCheckDate = false;
    private boolean isCheckTopic = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_name;
    private TextView text_location;
    private TextView text_date;
    private TextView text_time;
    private TextView text_endtime;
    private Animation animationErrorDown;
    private Animation animationErrorUpName;
    private Animation animationErrorUpLocation;
    private Animation animationErrorUpDate;
    private boolean showErrorName = false;
    private boolean showErrorLocation = false;
    private boolean showErrorDate = false;
    private boolean showErrorTopics = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showName = true;
    private boolean showLocation = true;
    private boolean showDate = true;
    private boolean showTime = true;
    private boolean showEndtime = true;
    private TextView text_name_hint;
    private TextView text_location_hint;
    private TextView text_date_hint;
    private TextView text_time_hint;
    private TextView text_endtime_hint;
    private TextView text_topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meeting);

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
                    adapterTopics.deleteItem(currentPosition);
                    datasTopic.remove(currentPosition);
                    updateVisibility();
                } else {
                    updateWorkplaceInspection(0);
                }
            }
        });

        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        location = (EditText) findViewById(R.id.location);
        text_name_error = findViewById(R.id.text_name_error);
        text_location_error = findViewById(R.id.text_location_error);
        text_date_error = findViewById(R.id.text_date_error);
        time = (EditText) findViewById(R.id.time);
        endtime = (EditText) findViewById(R.id.endtime);
        posted = (CheckBox) findViewById(R.id.posted);
        template = (EditText) findViewById(R.id.template);
        spinnerTemplate = (Spinner) findViewById(R.id.spinnerTemplate);
        text_template = findViewById(R.id.text_template);
        nextDate = (TextView) findViewById(R.id.nextDate);
        text_topics_error = findViewById(R.id.text_topics_error);
        text_name = (TextView) findViewById(R.id.text_name);
        text_location = (TextView) findViewById(R.id.text_location);
        text_date = (TextView) findViewById(R.id.text_date);
        text_time = (TextView) findViewById(R.id.text_start_time);
        text_endtime = (TextView) findViewById(R.id.text_end_time);
        text_name_hint = (TextView) findViewById(R.id.text_name_hint);
        text_location_hint = (TextView) findViewById(R.id.text_location_hint);
        text_date_hint = (TextView) findViewById(R.id.text_date_hint);
        text_time_hint = (TextView) findViewById(R.id.text_time_hint);
        text_endtime_hint = (TextView) findViewById(R.id.text_endtime_hint);
        text_topics = (TextView) findViewById(R.id.text_topics);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpName = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpLocation = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpDate = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpName.setAnimationListener(animationErrorUpNameListener);
        animationErrorUpLocation.setAnimationListener(animationErrorUpLocationListener);
        animationErrorUpDate.setAnimationListener(animationErrorUpDateListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        name.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        location.addTextChangedListener(inputTextWatcher);

        format = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");

        topicslist = (RecyclerView) findViewById(R.id.topicslist);
        topicslist.setNestedScrollingEnabled(false);
        topicslist.setLayoutManager(new LinearLayoutManager(MeetingActivity.this));

        text_no = findViewById(R.id.text_no);
        text_topics_no = (TextView) findViewById(R.id.text_topics_no);

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                updateWorkplaceInspection(1);
            }
        });

        View save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (id.equals("") || notEquals()) {
                    updateWorkplaceInspection(0);
                } else {
                    finish();
                }
            }
        });

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                hideSoftKeyboard();
                Intent intent = new Intent(MeetingActivity.this, TopicActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        userInfo = UserInfo.getUserInfo();
        userInfo.setTopic(null);
        if (!id.equals("")) {
            readonly = intent.getBooleanExtra("readonly", false);
            nameOld = intent.getStringExtra("name");
            locationOld = intent.getStringExtra("location");
            dateOld = intent.getLongExtra("date", 0l);
            dateendOld = intent.getLongExtra("dateend", 0l);
            dateNew = dateOld;
            dateendNew = dateendOld;
            postedOld = intent.getBooleanExtra("posted", false);
            name.setText(nameOld);
            location.setText(locationOld);
            posted.setChecked(postedOld);
            if (intent.getBooleanExtra("completed", false)) {
                button_ok.setVisibility(View.GONE);
            }

            datas.clear();
            for (MemberData item: userInfo.getMembers()) {
                MemberData memberData = new MemberData();
                memberData.setPresence(item.getPresence());
                memberData.setProfile(item.getProfile());
                memberData.setMeetingMemberId(item.getMeetingMemberId());

                datas.add(memberData);
            }

            datasTopic.clear();
            for (TopicData item: userInfo.getTopics()) {
                TopicData topicData = new TopicData();
                topicData.setDescription(item.getDescription());
                topicData.setName(item.getName());
                topicData.setCompleted(item.getCompleted());
                topicData.setMeetingTopicId(item.getMeetingTopicId());

                datasTopic.add(topicData);
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
                    endtime.setText(datet.substring(11));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            template.setVisibility(View.GONE);
            text_template.setVisibility(View.GONE);

            if (name.getText().length() != 0) {
                showName = false;
            }
            if (location.getText().length() != 0) {
                showLocation = false;
            }
            if (date.getText().length() != 0) {
                showDate = false;
            }
            if (time.getText().length() != 0) {
                showTime = false;
            }
            if (endtime.getText().length() != 0) {
                showEndtime = false;
            }
        } else {
            datas = userInfo.getAllMembers();
        }

        adapterTopics = new TopicsAdapter(this, readonly);
        topicslist.setAdapter(adapterTopics);

        if (id.equals("")) {
            templates = new ArrayList<>();
            templates.addAll(UserInfo.getUserInfo().getTemplateMeetingDatas());

            adapterTemplate = new SpinTemplatesAdapter(this, R.layout.spinner_list_item_black);
            spinnerTemplate.setAdapter(adapterTemplate);
            adapterTemplate.setValues(templates);
            template.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSoftKeyboard();
                    clickSpinner = true;
                    spinnerTemplate.performClick();
                }
            });
            spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!clickSpinner) {
                        return;
                    }
                    template.setText(templates.get(position).getName());
                    templateId = templates.get(position).getTemplateId();
                    clickSpinner = false;

                    if (!templateId.equals("")) {
                        pd.show();
                        new GetTemplateAction(MeetingActivity.this, templateId, getResources().getString(R.string.text_meetings_templates)).execute();
                    } else {
                        ArrayList<TopicData> topicDatas = new ArrayList<TopicData>();
                        for (TopicData item: datasTopic) {
                            if (item.getIsTemplate()) {
                                continue;
                            }
                            topicDatas.add(item);
                        }

                        datasTopic = topicDatas;
                        adapterTopics.setListArray(topicDatas);
                        updateVisibility();
                    }
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
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseDate();
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

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (readonly) {
                    return;
                }
                choiseTime(false);
            }
        });

        View root = findViewById(R.id.rootLayout);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        memberslist = (RecyclerView) findViewById(R.id.memberslist);
        memberslist.setNestedScrollingEnabled(false);
        memberslist.setLayoutManager(new LinearLayoutManager(MeetingActivity.this));

        adapter = new MeetingMembersAdapter(this, readonly);
        memberslist.setAdapter(adapter);

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

        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && location.getText().length() == 0 && showLocation) {
                    text_location_hint.setVisibility(View.GONE);
                    text_location.setVisibility(View.VISIBLE);
                    showLocation = false;
                    text_location.startAnimation(animationUp);
                } else if (!hasFocus && location.getText().length() == 0 && !showLocation) {
                    text_location.setVisibility(View.INVISIBLE);
                    showLocation = true;
                    text_location_hint.setVisibility(View.VISIBLE);
                    text_location_hint.startAnimation(animationDown);
                }
            }
        });

        updateViews();

        adapter.setListArray(datas);
        adapterTopics.setListArray(datasTopic);

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

        pd.show();
        new GetNextMeetingsAction(MeetingActivity.this, dateNew).execute();

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(MeetingActivity.this);
            }
        });

        updateVisibility();

        UpdateBottom();

        if (readonly) {
            add.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            button_ok.setVisibility(View.GONE);
            name.setFocusable(false);
            location.setFocusable(false);
            date.setFocusable(false);
            time.setFocusable(false);
            endtime.setFocusable(false);
            posted.setClickable(false);
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

    Animation.AnimationListener animationErrorUpLocationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation arg0) {
            text_location_error.setVisibility(View.INVISIBLE);
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
                || !locationOld.equals(location.getText().toString())
                || !dateOld.equals(dateNew)
                || !dateendOld.equals(dateendNew);

        if (!rezult) {
            ArrayList<MemberData> memberDatas = userInfo.getMembers();
            if (memberDatas.size() == 0) {
                memberDatas = userInfo.getAllMembers();
            }
            if (memberDatas.size() != datas.size()) {
                rezult = true;
            } else {
                for (int i=0; i < datas.size(); i++) {
                    if (memberDatas.size() <= i) {
                        rezult = true;
                        break;
                    } else {
                        if (!datas.get(i).getMeetingMemberId().equals(memberDatas.get(i).getMeetingMemberId())
                            || datas.get(i).getPresence() != memberDatas.get(i).getPresence()) {
                            rezult = true;
                            break;
                        }
                    }
                }
            }
        }

        if (!rezult) {
            if (userInfo.getTopics().size() != datasTopic.size()) {
                rezult = true;
            } else {
                for (int i=0; i < datasTopic.size(); i++) {
                    if (userInfo.getTopics().size() <= i) {
                        rezult = true;
                        break;
                    } else {
                        if (!datasTopic.get(i).getMeetingTopicId().equals(userInfo.getTopics().get(i).getMeetingTopicId())
                            || !datasTopic.get(i).getDescription().equals(userInfo.getTopics().get(i).getDescription())
                            || !datasTopic.get(i).getName().equals(userInfo.getTopics().get(i).getName())
                            || datasTopic.get(i).getCompleted() != userInfo.getTopics().get(i).getCompleted()) {
                            rezult = true;
                            break;
                        }
                    }
                }
            }
        }

        return rezult;
    }

    private void updateNextDate() {
        nextDate.setText(Html.fromHtml(getResources().getString(R.string.text_next_meeting) + ": " + nextMeeting + "<br><br>" + getResources().getString(R.string.text_next_workplace_inspection) + ": " + nextWorkplaceInspection));
    }

    private void updateVisibility() {
        if (datas == null || datas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            memberslist.setVisibility(View.GONE);
        } else {
            memberslist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
        if (datasTopic == null || datasTopic.size() == 0) {
            text_topics_no.setVisibility(View.VISIBLE);
            topicslist.setVisibility(View.GONE);
        } else {
            topicslist.setVisibility(View.VISIBLE);
            text_topics_no.setVisibility(View.GONE);
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
            if (endtime.getText().length() == 0 && showEndtime) {
                text_endtime_hint.setVisibility(View.GONE);
                text_endtime.setVisibility(View.VISIBLE);
                showEndtime= false;
                text_endtime.startAnimation(animationUp);
            }
        }

        Date date = null;
        try {
            if (thisStart) {
                date = format.parse("01.01.1980 " + time.getText().toString() + ":00");
            } else {
                date = format.parse("01.01.1980 " + endtime.getText().toString() + ":00");
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

        private MeetingActivity act;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            //final Calendar c = Calendar.getInstance();
            //int hour = c.get(Calendar.HOUR_OF_DAY);
            //int minute = c.get(Calendar.MINUTE);

            act = (MeetingActivity) getActivity();

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
                if (act.endtime.getText().length() == 0 && !act.showEndtime) {
                    act.text_endtime.setVisibility(View.INVISIBLE);
                    act.showEndtime = true;
                    act.text_endtime_hint.setVisibility(View.VISIBLE);
                    act.text_endtime_hint.startAnimation(act.animationDown);
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
                act.endtime.setText("" + mHour + ":" + mMinute + " " + ampm);
            }

            if (act.thisStart) {
                if (act.endtime.getText().toString().equals("")) {
                    act.endtime.setText("" + mHour + ":" + mMinute + " " + ampm);
                    act.text_endtime.setVisibility(View.VISIBLE);
                }
            } else {
                if (act.time.getText().toString().equals("")) {
                    act.time.setText("" + mHour + ":" + mMinute + " " + ampm);
                    act.text_time.setVisibility(View.VISIBLE);
                }
            }

            Date dateD = null;
            if (act.date.getText().toString().equals("")) {
                dateD = new Date();
                String datet = format.format(dateD);
                act.date.setText(datet.substring(0,10));
                act.text_date.setVisibility(View.VISIBLE);
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

            String dateendNewstr = act.date.getText().toString() + " " + act.endtime.getText().toString();
            if (!dateendNewstr.equals("")) {
                try {
                    dateD = format.parse(dateendNewstr);
                    act.dateendNew = dateD.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            act.updateViews();

            if (act.thisStart) {
                act.pd.show();
                new GetNextMeetingsAction(act, act.dateNew).execute();
            }
        }
    }

    private void choiseDate() {
        hideSoftKeyboard();

        if (date.getText().length() == 0 && showDate) {
            text_date_hint.setVisibility(View.GONE);
            text_date.setVisibility(View.VISIBLE);
            showDate = false;
            text_date.startAnimation(animationUp);
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

        private MeetingActivity act;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            //final Calendar c = Calendar.getInstance();
            //int year = c.get(Calendar.YEAR);
            //int month = c.get(Calendar.MONTH);
            //int day = c.get(Calendar.DAY_OF_MONTH);

            act = (MeetingActivity) getActivity();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, act.myYear, act.myMonth, act.myDay);
        }

        public void onCancel(DialogInterface dialog) {
            if (act.date.getText().length() == 0 && !act.showDate) {
                act.text_date.setVisibility(View.INVISIBLE);
                act.showDate = true;
                act.text_date_hint.setVisibility(View.VISIBLE);
                act.text_date_hint.startAnimation(act.animationDown);
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

            act.date.setText("" + mDay + "." + mMonth + "." + act.myYear);

            Date dateD = null;
            if (act.time.getText().toString().equals("")) {
                dateD = new Date();
                String datet = format.format(dateD);
                act.time.setText(datet.substring(11));
                act.text_time.setVisibility(View.VISIBLE);
            }
            if (act.endtime.getText().toString().equals("")) {
                dateD = new Date();
                String datet = format.format(dateD);
                act.endtime.setText(datet.substring(11));
                act.text_endtime.setVisibility(View.VISIBLE);
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

            String dateendNewstr = act.date.getText().toString() + " " + act.endtime.getText().toString();
            if (!dateendNewstr.equals("")) {
                try {
                    dateD = format.parse(dateendNewstr);
                    act.dateendNew = dateD.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            act.isCheckDate = true;

            act.updateViews();

            if (act.thisStart) {
                act.pd.show();
                new GetNextMeetingsAction(act, act.dateNew).execute();
            }
        }
    }

    private void updateWorkplaceInspection(int type) {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("")
                || location.getText().toString().equals("")
                || date.getText().toString().equals("")
                || datasTopic.size() == 0) {
            isCheckName = true;
            isCheckLocation = true;
            isCheckDate = true;
            isCheckTopic = true;
            updateViews();
            allOk = false;
        }

        if (allOk) {
            pd.show();

            if (type == 0) {
                MeetingData meetingData = new MeetingData();
                meetingData.setName(name.getText().toString());
                meetingData.setEndTime(dateendNew);
                meetingData.setLocation(location.getText().toString());
                meetingData.setMeetingDate(dateNew);
                meetingData.setPosted(posted.isChecked());
                meetingData.setMeetingId(id);
                meetingData.setMembers(datas);
                meetingData.setTopics(datasTopic);

                new MeetingAddAction(MeetingActivity.this, meetingData).execute();
            } else {
                MeetingData meetingData = new MeetingData();
                meetingData.setName(name.getText().toString());
                meetingData.setEndTime(dateendNew);
                meetingData.setLocation(location.getText().toString());
                meetingData.setMeetingDate(dateNew);
                meetingData.setPosted(posted.isChecked());
                meetingData.setMeetingId(id);
                meetingData.setMembers(datas);
                meetingData.setTopics(datasTopic);

                new EventAddAction(MeetingActivity.this).execute();
            }
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
            } else {
                isCheckLocation = true;
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
        if (isCheckLocation && location.getText().toString().equals("")) {
            text_location_error.setVisibility(View.VISIBLE);
            location.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_location.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_location_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorLocation) {
                showErrorLocation = true;
                text_location_error.startAnimation(animationErrorDown);
            }
        } else if (!location.getText().toString().equals("")) {
            text_location.setVisibility(View.VISIBLE);
            location.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_location.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_location_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_location_hint.setVisibility(View.GONE);
            if (showErrorLocation) {
                showErrorLocation = false;
                text_location_error.startAnimation(animationErrorUpLocation);
            }
        }
        if (isCheckDate && date.getText().toString().equals("")) {
            text_date_error.setVisibility(View.VISIBLE);
            date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            time.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            endtime.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
            text_date.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_date_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_time.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_time_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_endtime.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_endtime_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorDate) {
                showErrorDate = true;
                text_date_error.startAnimation(animationErrorDown);
            }
        } else if (!date.getText().toString().equals("")) {
            text_date.setVisibility(View.VISIBLE);
            date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            time.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            endtime.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            text_date.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_date_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_date_hint.setVisibility(View.GONE);
            text_time.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_time_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_time_hint.setVisibility(View.GONE);
            text_endtime.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_endtime_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_endtime_hint.setVisibility(View.GONE);
            if (showErrorDate) {
                showErrorDate = false;
                text_date_error.startAnimation(animationErrorUpDate);
            }
        }
        if (isCheckTopic && datasTopic.size() == 0) {
            text_topics_error.setVisibility(View.VISIBLE);
            text_topics.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_topics_no.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorTopics) {
                showErrorTopics = true;
                text_topics_error.startAnimation(animationErrorDown);
            }
        } else if (datasTopic.size() != 0) {
            text_topics.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_topics_no.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_topics_error.setVisibility(View.GONE);
            showErrorTopics = false;
            /*
            if (showErrorTopics) {
                showErrorTopics = false;
                text_topics_error.startAnimation(animationErrorUpTopics);
            }
            */
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
    public void onRootTopicsClick(int position) {
        hideSoftKeyboard();
    }

    @Override
    public void onOpenTopicsClick(int position) {
        hideSoftKeyboard();

        Intent intent = new Intent(MeetingActivity.this, TopicActivity.class);
        TopicData topicData = datasTopic.get(position);
        intent.putExtra("name", topicData.getName());
        intent.putExtra("description", topicData.getDescription());
        intent.putExtra("completed", topicData.getCompleted());
        intent.putExtra("id", topicData.getMeetingTopicId());
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onDeleteTopicsClick(int position) {
        hideSoftKeyboard();

        currentPosition = position;
        deleteItem = true;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onRequestTemplate(String result) {
        ArrayList<TopicData> topicDatas = new ArrayList<TopicData>();
        for (TopicData item: datasTopic) {
            if (item.getIsTemplate()) {
                continue;
            }
            topicDatas.add(item);
        }

        datasTopic = topicDatas;
        adapterTopics.setListArray(topicDatas);
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
        ArrayList<TopicData> topicDatas = new ArrayList<TopicData>();
        for (TemplateItemData item: templatesItemDatas) {
            TopicData topicData = new TopicData();
            topicData.setDescription(item.getDescription());
            topicData.setName(item.getName());
            topicData.setIsTemplate(true);
            topicDatas.add(topicData);
        }
        for (TopicData item: datasTopic) {
            if (item.getIsTemplate()) {
                continue;
            }
            topicDatas.add(item);
        }

        datasTopic = topicDatas;
        adapterTopics.setListArray(topicDatas);
        updateVisibility();
        pd.hide();
    }

    @Override
    public void onRootMeetingMembersClick(int position) {
        hideSoftKeyboard();
        //datas.get(position).setPresence(!datas.get(position).getPresence());
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestMeetingAdd(String result) {
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
    public void onRequestEventAdd(String result) {
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
            MeetingData meetingData = new MeetingData();
            meetingData.setName(name.getText().toString());
            meetingData.setEndTime(dateendNew);
            meetingData.setLocation(location.getText().toString());
            meetingData.setMeetingDate(dateNew);
            meetingData.setPosted(posted.isChecked());
            meetingData.setMeetingId(id);
            meetingData.setMembers(datas);
            meetingData.setTopics(datasTopic);
            meetingData.setCompleted(true);

            new MeetingAddAction(MeetingActivity.this, meetingData).execute();
        }
    }

    @Override
    public void onRequestNextMeetings(String result, String nextDate) {
        if (!result.equals("OK")) {
            nextMeeting = "-";
            nextWorkplaceInspection = "-";
            updateNextDate();
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
            nextMeeting = nextDate;
            new GetNextWorkplaceInspectionsAction(MeetingActivity.this, dateNew).execute();
        }
    }

    @Override
    public void onRequestNextWorkplaceInspections(String result, String nextDate) {
        pd.hide();
        if (!result.equals("OK")) {
            nextWorkplaceInspection = "-";
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
            nextWorkplaceInspection = nextDate;
        }
        updateNextDate();
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

        if (userInfo.getTopic() != null) {
            if (userInfo.getPosition() == -1) {
                datasTopic.add(userInfo.getTopic());
                adapterTopics.addTopic(userInfo.getTopic());
            } else {
                TopicData topicData = datasTopic.get(userInfo.getPosition());
                topicData.setCompleted(userInfo.getTopic().getCompleted());
                topicData.setName(userInfo.getTopic().getName());
                topicData.setDescription(userInfo.getTopic().getDescription());
                adapterTopics.notifyDataSetChanged();
            }
            userInfo.setTopic(null);
            userInfo.setPosition(-1);
            isCheckTopic = true;
            updateVisibility();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        text_name.clearAnimation();
        text_location.clearAnimation();
        text_date.clearAnimation();
        text_time.clearAnimation();
        text_endtime.clearAnimation();
        text_name_error.clearAnimation();
        text_location_error.clearAnimation();
        text_date_error.clearAnimation();
        text_name_hint.clearAnimation();
        text_location_hint.clearAnimation();
        text_date_hint.clearAnimation();
        text_time_hint.clearAnimation();
        text_endtime_hint.clearAnimation();
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
        endtime.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        unregisterReceiver(br);
    }
}
