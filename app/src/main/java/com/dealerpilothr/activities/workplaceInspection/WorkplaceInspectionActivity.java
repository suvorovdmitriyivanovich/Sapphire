package com.dealerpilothr.activities.workplaceInspection;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.MotionEvent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.activities.FilesActivity;
import com.dealerpilothr.activities.organizationStructure.ChooseOrganizationStructureActivity;
import com.dealerpilothr.adapters.MeetingMembersAdapter;
import com.dealerpilothr.adapters.SpinTemplatesAdapter;
import com.dealerpilothr.adapters.WorkplaceInspectionItemsAdapter;
import com.dealerpilothr.api.GetTemplateAction;
import com.dealerpilothr.api.GetWorkplaceInspectionAction;
import com.dealerpilothr.api.WorkplaceInspectionItemAddAction;
import com.dealerpilothr.api.WorkplaceInspectionItemDeleteAction;
import com.dealerpilothr.api.WorkplaceInspectionAddAction;
import com.dealerpilothr.db.DBHelper;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.MemberData;
import com.dealerpilothr.models.TemplateData;
import com.dealerpilothr.models.TemplateItemData;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.WorkplaceInspectionData;
import com.dealerpilothr.models.WorkplaceInspectionItemData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class WorkplaceInspectionActivity extends BaseActivity implements GetTemplateAction.RequestTemplate,
                                                                         GetTemplateAction.RequestTemplateData,
                                                                         GetWorkplaceInspectionAction.RequestWorkplaceInspection,
                                                                         WorkplaceInspectionItemsAdapter.OnRootClickListener,
                                                                         WorkplaceInspectionItemsAdapter.OnOpenClickListener,
                                                                         WorkplaceInspectionItemsAdapter.OnDeleteClickListener,
                                                                         WorkplaceInspectionItemsAdapter.OnFilesClickListener,
                                                                         WorkplaceInspectionItemDeleteAction.RequestWorkplaceInspectionItemDelete,
                                                                         WorkplaceInspectionAddAction.RequestWorkplaceInspectionAdd,
                                                                         WorkplaceInspectionAddAction.RequestWorkplaceInspectionAddData,
                                                                         WorkplaceInspectionItemAddAction.RequestWorkplaceInspectionItemAdd,
                                                                         MeetingMembersAdapter.OnRootMeetingMembersClickListener,
                                                                         MeetingMembersAdapter.OnDeleteMeetingMembersClickListener,
        UpdateAction.RequestUpdate {
    private String workplaceInspectionId = "";
    private ProgressDialog pd;
    private ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas = new ArrayList<WorkplaceInspectionItemData>();
    private RecyclerView itemlist;
    private WorkplaceInspectionItemsAdapter adapter;
    private EditText name;
    private EditText date;
    private EditText description;
    private View image_date_group;
    private View text_name_error;
    private View text_date_error;
    private CheckBox posted;
    private int pressType = 0;
    private String nameOld = "";
    private String descriptionOld = "";
    private Long dateOld = 0l;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int deleteItem = 0;
    private int currentPosition = 0;
    private static SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    int myYear = cal.get(Calendar.YEAR);
    int myMonth = cal.get(Calendar.MONTH);
    int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private EditText template;
    private Spinner spinnerTemplate;
    private ArrayList<TemplateData> templates;
    private SpinTemplatesAdapter adapterTemplate;
    private boolean clickSpinner = false;
    private String templateId = "";
    private Long dateNew;
    private boolean postedOld = false;
    private WorkplaceInspectionData workplaceInspectionData = new WorkplaceInspectionData();
    private View text_no;
    private boolean isCheckName = false;
    private boolean isCheckDate = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private TextView text_name;
    private TextView text_date;
    private TextView text_template;
    private TextView text_description;
    private Animation animationErrorDown;
    private Animation animationErrorUpName;
    private Animation animationErrorUpDate;
    private boolean showErrorName = false;
    private boolean showErrorDate = false;
    private Animation animationUp;
    private Animation animationDown;
    private boolean showName = true;
    private boolean showDate = true;
    private boolean showDescription = true;
    private TextView text_name_hint;
    private TextView text_date_hint;
    private TextView text_description_hint;
    private ImageView image_date;
    private ArrayList<MemberData> datasTeam = new ArrayList<MemberData>();
    private RecyclerView teamlist;
    private MeetingMembersAdapter adapterTeam;
    private View text_team_no;
    private UserInfo userInfo;
    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workplace_inspection);

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
                deleteItem = 0;
            }
        });
        dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                deleteItem = 0;
            }
        });

        button_cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
                if (deleteItem == 0) {
                    finish();
                }
                deleteItem = 0;
            }
        });

        button_send_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
                if (deleteItem == 1) {
                    deleteItem = 0;
                    if (workplaceInspectionItemDatas.get(currentPosition).getWorkplaceInspectionItemId().equals("")) {
                        DBHelper.getInstance(Dealerpilothr.getInstance()).deleteWorkplaceInspectionItem(workplaceInspectionItemDatas.get(currentPosition).getId());
                        pd.show();
                        new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
                    } else {
                        pd.show();

                        new WorkplaceInspectionItemDeleteAction(WorkplaceInspectionActivity.this, workplaceInspectionItemDatas.get(currentPosition).getWorkplaceInspectionItemId()).execute();
                    }
                } else if (deleteItem == 2) {
                    deleteItem = 0;
                    datasTeam.remove(currentPosition);
                    adapterTeam.deleteItem(currentPosition);
                    updateVisibility();
                } else {
                    updateWorkplaceInspection(0);
                }
            }
        });

        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        image_date_group = findViewById(R.id.image_date_group);
        description = (EditText) findViewById(R.id.description);
        text_name_error = findViewById(R.id.text_name_error);
        text_date_error = findViewById(R.id.text_date_error);
        template = (EditText) findViewById(R.id.template);
        spinnerTemplate = (Spinner) findViewById(R.id.spinnerTemplate);
        posted = (CheckBox) findViewById(R.id.posted);
        text_name = (TextView) findViewById(R.id.text_name);
        text_date = (TextView) findViewById(R.id.text_date);
        text_template = (TextView) findViewById(R.id.text_template);
        text_description = (TextView) findViewById(R.id.text_description);
        text_name_hint = (TextView) findViewById(R.id.text_name_hint);
        text_date_hint = (TextView) findViewById(R.id.text_date_hint);
        text_description_hint = (TextView) findViewById(R.id.text_description_hint);
        image_date = (ImageView) findViewById(R.id.image_date);

        animationErrorDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationErrorUpName = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        animationErrorUpDate = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        animationErrorUpName.setAnimationListener(animationErrorUpNameListener);
        animationErrorUpDate.setAnimationListener(animationErrorUpDateListener);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.translate_scale_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_scale_down);

        TextWatcher inputTextWatcher = new TextWatch(1);
        name.addTextChangedListener(inputTextWatcher);
        inputTextWatcher = new TextWatch(2);
        description.addTextChangedListener(inputTextWatcher);

        format = new SimpleDateFormat("dd.MM.yyyy");

        userInfo = UserInfo.getUserInfo();
        userInfo.setUpdateMembers(null);

        Intent intent = getIntent();
        workplaceInspectionId = intent.getStringExtra("workplaceInspectionId");
        if (workplaceInspectionId == null) {
            workplaceInspectionId = "";
        }
        if (!workplaceInspectionId.equals("")) {
            readonly = intent.getBooleanExtra("readonly", false);
            nameOld = intent.getStringExtra("name");
            descriptionOld = intent.getStringExtra("description");
            dateOld = intent.getLongExtra("date", 0l);
            dateNew = dateOld;
            name.setText(nameOld);
            description.setText(descriptionOld);
            postedOld = intent.getBooleanExtra("posted", false);
            posted.setChecked(postedOld);

            if (dateOld != 0l) {
                try {
                    Date thisdaten = new Date();
                    thisdaten.setTime(dateOld);
                    String datet = format.format(thisdaten);
                    date.setText(datet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            text_template.setVisibility(View.GONE);
            template.setVisibility(View.GONE);
            if (name.getText().length() != 0) {
                showName = false;
            }
            if (date.getText().length() != 0) {
                showDate = false;
            }
            if (description.getText().length() != 0) {
                showDescription = false;
            }

            datasTeam.clear();
            for (MemberData item : userInfo.getMembers()) {
                MemberData memberData = new MemberData();
                //memberData.setPresence(item.getPresence());
                memberData.setProfile(item.getProfile());
                memberData.setMeetingMemberId(item.getMeetingMemberId());
                memberData.setName(item.getName());

                datasTeam.add(memberData);
            }
        }
        //} else {
        //    datasTeam.clear();
        //    for (ProfileData item: userInfo.getCurrentOrganizationStructures()) {
        //        MemberData memberData = new MemberData();
        //        memberData.setProfile(item);
        //
        //        datasTeam.add(memberData);
        //    }
        //}
        sort();

        if (workplaceInspectionId.equals("")) {
            templates = new ArrayList<>();
            templates.addAll(UserInfo.getUserInfo().getTemplateDatas());

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
                        new GetTemplateAction(WorkplaceInspectionActivity.this, templateId, getResources().getString(R.string.text_workplace_templates)).execute();
                        //} else if (!workplaceInspectionId.equals("")) {
                        //    new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
                    } else {
                        adapter.setListArray(new ArrayList<WorkplaceInspectionItemData>());
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
                if (readonly) {
                    return;
                }
                choiseDate();
            }
        });

        image_date_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseDate();
            }
        });

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if (workplaceInspectionId.equals("") || notEquals()) {
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

                if (name.getText().toString().equals("") || date.getText().toString().equals("")) {
                    isCheckName = true;
                    isCheckDate = true;
                    updateViews();
                    return;
                }

                if (notEquals()) {
                    updateWorkplaceInspection(1);
                } else {
                    addItem();
                }
            }
        });

        View add_team = findViewById(R.id.add_team);
        add_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                userInfo.setUpdateMembers(datasTeam);
                Intent intent = new Intent(WorkplaceInspectionActivity.this, ChooseOrganizationStructureActivity.class);
                startActivity(intent);
            }
        });

        View root = findViewById(R.id.rootLayout);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        itemlist = (RecyclerView) findViewById(R.id.itemlist);
        itemlist.setNestedScrollingEnabled(false);
        itemlist.setLayoutManager(new LinearLayoutManager(WorkplaceInspectionActivity.this));

        adapter = new WorkplaceInspectionItemsAdapter(this, readonly, false);
        itemlist.setAdapter(adapter);

        teamlist = (RecyclerView) findViewById(R.id.teamlist);
        teamlist.setNestedScrollingEnabled(false);
        teamlist.setLayoutManager(new LinearLayoutManager(WorkplaceInspectionActivity.this));

        adapterTeam = new MeetingMembersAdapter(this, readonly, true);
        teamlist.setAdapter(adapterTeam);

        adapterTeam.setListArray(datasTeam);

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
        text_team_no = findViewById(R.id.text_team_no);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(WorkplaceInspectionActivity.this);
            }
        });

        updateVisibility();
        UpdateBottom();

        if (readonly) {
            add.setVisibility(View.GONE);
            button_ok.setVisibility(View.GONE);
            add_team.setVisibility(View.GONE);
            name.setFocusable(false);
            date.setFocusable(false);
            image_date_group.setClickable(false);
            posted.setClickable(false);
            description.setFocusable(false);
        }

        scroll = (ScrollView) findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
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

    private boolean notEquals() {
        if (readonly) {
            return false;
        }

        boolean rezult = !nameOld.equals(name.getText().toString())
                || !descriptionOld.equals(description.getText().toString())
                || !dateOld.equals(dateNew)
                || postedOld != posted.isChecked();

        if (!rezult) {
            ArrayList<MemberData> memberDatas = userInfo.getMembers();
            Collections.sort(memberDatas, new Comparator<MemberData>() {
                public int compare(MemberData o1, MemberData o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            //if (memberDatas.size() == 0) {
            //    memberDatas = userInfo.getAllMembers();
            //}
            if (memberDatas.size() != datasTeam.size()) {
                rezult = true;
            } else {
                for (int i=0; i < datasTeam.size(); i++) {
                    if (memberDatas.size() <= i) {
                        rezult = true;
                        break;
                    } else {
                        //if (!datasTeam.get(i).getMeetingMemberId().equals(memberDatas.get(i).getMeetingMemberId())
                        //        || datasTeam.get(i).getPresence() != memberDatas.get(i).getPresence()) {
                        if (!datasTeam.get(i).getProfile().getProfileId().equals(memberDatas.get(i).getProfile().getProfileId())) {
                            rezult = true;
                            break;
                        }
                    }
                }
            }
        }

        return rezult;
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

    public void updateVisibility() {
        if (workplaceInspectionItemDatas == null || workplaceInspectionItemDatas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            itemlist.setVisibility(View.GONE);
        } else {
            itemlist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
        if (datasTeam == null || datasTeam.size() == 0) {
            text_team_no.setVisibility(View.VISIBLE);
            teamlist.setVisibility(View.GONE);
        } else {
            teamlist.setVisibility(View.VISIBLE);
            text_team_no.setVisibility(View.GONE);
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
                                                                             DatePickerDialog.OnCancelListener{

        private WorkplaceInspectionActivity act;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            //final Calendar c = Calendar.getInstance();
            //int year = c.get(Calendar.YEAR);
            //int month = c.get(Calendar.MONTH);
            //int day = c.get(Calendar.DAY_OF_MONTH);

            act = (WorkplaceInspectionActivity) getActivity();

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
            String dateNewstr = act.date.getText().toString();
            if (!dateNewstr.equals("")) {
                try {
                    dateD = format.parse(dateNewstr);
                    act.dateNew = dateD.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            act.isCheckDate = true;

            act.updateViews();
        }
    }

    private void updateWorkplaceInspection(int type) {
        hideSoftKeyboard();
        boolean allOk = true;

        if (name.getText().toString().equals("") || date.getText().toString().equals("")) {
            isCheckName = true;
            isCheckDate = true;
            updateViews();
            scroll.scrollTo(0, 0);
            allOk = false;
        }

        if (allOk) {
            pd.show();

            pressType = type;

            new WorkplaceInspectionAddAction(WorkplaceInspectionActivity.this, workplaceInspectionId, name.getText().toString(), description.getText().toString(), dateNew, posted.isChecked(), datasTeam, 0).execute();
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
            image_date.setImageResource(R.drawable.date_red);
            text_date.setTextColor(ContextCompat.getColor(this, R.color.red));
            text_date_hint.setTextColor(ContextCompat.getColor(this, R.color.red));
            if (!showErrorDate) {
                showErrorDate = true;
                text_date_error.startAnimation(animationErrorDown);
            }
        } else if (!date.getText().toString().equals("")) {
            text_date.setVisibility(View.VISIBLE);
            date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
            image_date.setImageResource(R.drawable.date);
            text_date.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_date_hint.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            text_date_hint.setVisibility(View.GONE);
            if (showErrorDate) {
                showErrorDate = false;
                text_date_error.startAnimation(animationErrorUpDate);
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

    private void addItem() {
        hideSoftKeyboard();
        Intent intent = new Intent(WorkplaceInspectionActivity.this, WorkplaceInspectionItemActivity.class);
        intent.putExtra("workplaceInspectionId", workplaceInspectionId);
        startActivity(intent);
    }

    @Override
    public void onRootMeetingMembersClick(int position) {
        hideSoftKeyboard();
        //datasTeam.get(position).setPresence(!datasTeam.get(position).getPresence());
        //adapterTeam.notifyDataSetChanged();
    }

    @Override
    public void onRequestTemplate(String result) {
        pd.hide();
        scroll.scrollTo(0, 0);
        if (!result.equals("OK")) {
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
    }

    @Override
    public void onRequestTemplateData(ArrayList<TemplateItemData> templatesItemDatas) {
        ArrayList<WorkplaceInspectionItemData> list = new ArrayList<WorkplaceInspectionItemData>();
        for (TemplateItemData item: templatesItemDatas) {
            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setDescription(item.getDescription());
            workplaceInspectionItemData.setName(item.getName());
            workplaceInspectionItemData.setPriority(item.getPriority());
            workplaceInspectionItemData.setSeverity(item.getSeverity());
            workplaceInspectionItemData.setStatus(item.getStatus());
            list.add(workplaceInspectionItemData);
        }

        this.workplaceInspectionItemDatas = list;
        adapter.setListArray(list);
        updateVisibility();
        scroll.scrollTo(0, 0);
        pd.hide();
    }

    @Override
    public void onRequestWorkplaceInspection(String result, ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas) {
        if (!result.equals("OK") && !result.equals(getResources().getString(R.string.text_need_internet))) {
            pressType = 0;
            updateVisibility();
            scroll.scrollTo(0, 0);
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
            return;
        }

        ArrayList<WorkplaceInspectionItemData> allDatas = new ArrayList<WorkplaceInspectionItemData>();
        ArrayList<WorkplaceInspectionItemData> datas = DBHelper.getInstance(Dealerpilothr.getInstance()).getWorkplaceInspectionItems(workplaceInspectionId);

        boolean isExist = false;
        for (WorkplaceInspectionItemData item: workplaceInspectionItemDatas) {
            isExist = false;
            for (WorkplaceInspectionItemData item2: datas) {
                if (item.getWorkplaceInspectionItemId().equals(item2.getWorkplaceInspectionItemId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                allDatas.add(item);
            }
        }
        for (WorkplaceInspectionItemData item: datas) {
            allDatas.add(item);
        }

        this.workplaceInspectionItemDatas = allDatas;
        adapter.setListArray(this.workplaceInspectionItemDatas);
        updateVisibility();

        scroll.scrollTo(0, 0);
        pd.hide();
        if (pressType == 2) {
            pressType = 0;
            openItem(false);
        }
    }

    @Override
    public void onRootClick(int position) {
        hideSoftKeyboard();
        if (workplaceInspectionId.equals("")) {
            return;
        }
        currentPosition = position;
        openItem(true);
    }

    @Override
    public void onDeleteClick(int position) {
        hideSoftKeyboard();

        currentPosition = position;
        deleteItem = 1;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onDeleteMeetingMembersClick(int position) {
        hideSoftKeyboard();

        currentPosition = position;
        deleteItem = 2;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onOpenClick(int position) {
        hideSoftKeyboard();
        currentPosition = position;
        if (workplaceInspectionId.equals("")) {
            updateWorkplaceInspection(2);
        } else {
            openItem(false);
        }
    }

    private void openItem(boolean read) {
        Intent intent = new Intent(WorkplaceInspectionActivity.this, WorkplaceInspectionItemActivity.class);
        WorkplaceInspectionItemData workplaceInspectionItemData = workplaceInspectionItemDatas.get(currentPosition);
        intent.putExtra("readonly", read);
        intent.putExtra("idloc", workplaceInspectionItemData.getId());
        intent.putExtra("name", workplaceInspectionItemData.getName());
        intent.putExtra("description", workplaceInspectionItemData.getDescription());
        intent.putExtra("comments", workplaceInspectionItemData.getComments());
        intent.putExtra("recommendedActions", workplaceInspectionItemData.getRecommendedActions());
        intent.putExtra("workplaceInspectionItemId", workplaceInspectionItemData.getWorkplaceInspectionItemId());
        intent.putExtra("workplaceInspectionId", workplaceInspectionItemData.getWorkplaceInspectionId());
        intent.putExtra("severity", workplaceInspectionItemData.getSeverity());
        intent.putExtra("workplaceInspectionItemStatusId", workplaceInspectionItemData.getStatus().getWorkplaceInspectionItemStatusId());
        intent.putExtra("workplaceInspectionItemPriorityId", workplaceInspectionItemData.getPriority().getWorkplaceInspectionItemPriorityId());
        startActivity(intent);
    }

    @Override
    public void onRequestWorkplaceInspectionItemDelete(String result) {
        dialog_confirm.dismiss();
        if (!result.equals("OK")) {
            scroll.scrollTo(0, 0);
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
            new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionAddData(WorkplaceInspectionData workplaceInspectionData) {
        this.workplaceInspectionData = workplaceInspectionData;
        nameOld = name.getText().toString();
        descriptionOld = description.getText().toString();
        postedOld = posted.isChecked();
        dateOld = workplaceInspectionData.getDate();
        dateNew = dateOld;
        if (workplaceInspectionId.equals("") && workplaceInspectionItemDatas.size() > 0) {
            workplaceInspectionId = workplaceInspectionData.getWorkplaceInspectionId();

            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setName(workplaceInspectionItemDatas.get(0).getName());
            workplaceInspectionItemData.setDescription(workplaceInspectionItemDatas.get(0).getDescription());
            workplaceInspectionItemData.setWorkplaceInspectionId(workplaceInspectionId);
            workplaceInspectionItemData.setSeverity(workplaceInspectionItemDatas.get(0).getSeverity());
            workplaceInspectionItemData.setStatus(workplaceInspectionItemDatas.get(0).getStatus());
            workplaceInspectionItemData.setPriority(workplaceInspectionItemDatas.get(0).getPriority());

            new WorkplaceInspectionItemAddAction(WorkplaceInspectionActivity.this, workplaceInspectionItemData, workplaceInspectionItemDatas.size() == 1, 0, "").execute();
        } else {
            workplaceInspectionId = workplaceInspectionData.getWorkplaceInspectionId();

            scroll.scrollTo(0, 0);
            pd.hide();
            requestWorkplaceInspectionAddData();
        }
    }

    private void requestWorkplaceInspectionAddData() {
        if (pressType == 1) {
            pressType = 0;
            addItem();
        } else if (pressType == 2) {
            pd.show();
            new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionItemAdd(String result, boolean neddclosepd, int ihms, String id) {
        if (!result.equals("OK")) {
            scroll.scrollTo(0, 0);
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
        } else if (neddclosepd) {
            scroll.scrollTo(0, 0);
            pd.hide();

            requestWorkplaceInspectionAddData();
        } else if (ihms < workplaceInspectionItemDatas.size()) {
            WorkplaceInspectionItemData workplaceInspectionItemData = new WorkplaceInspectionItemData();
            workplaceInspectionItemData.setName(workplaceInspectionItemDatas.get(ihms).getName());
            workplaceInspectionItemData.setDescription(workplaceInspectionItemDatas.get(ihms).getDescription());
            workplaceInspectionItemData.setWorkplaceInspectionId(workplaceInspectionId);
            workplaceInspectionItemData.setSeverity(workplaceInspectionItemDatas.get(ihms).getSeverity());
            workplaceInspectionItemData.setStatus(workplaceInspectionItemDatas.get(ihms).getStatus());
            workplaceInspectionItemData.setPriority(workplaceInspectionItemDatas.get(ihms).getPriority());

            new WorkplaceInspectionItemAddAction(WorkplaceInspectionActivity.this, workplaceInspectionItemData, ihms == workplaceInspectionItemDatas.size()-1, ihms, "").execute();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionAdd(String result) {
        scroll.scrollTo(0, 0);
        pd.hide();

        pressType = 0;
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

    @Override
    public void onFilesClick(int position) {
        Intent intent = new Intent(WorkplaceInspectionActivity.this, FilesActivity.class);
        WorkplaceInspectionItemData workplaceInspectionItemData = workplaceInspectionItemDatas.get(position);
        intent.putExtra("name", workplaceInspectionItemData.getName());
        intent.putExtra("id", workplaceInspectionItemData.getWorkplaceInspectionItemId());
        intent.putExtra("url", Environment.WorkplaceInspectionsItemsFilesURL);
        intent.putExtra("nameField", "WorkplaceInspectionItemId");
        UserInfo.getUserInfo().setFileDatas(workplaceInspectionItemData.getFiles());
        startActivity(intent);
    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            scroll.scrollTo(0, 0);
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
            scroll.scrollTo(0, 0);
            pd.hide();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void sort() {
        Collections.sort(datasTeam, new Comparator<MemberData>() {
            public int compare(MemberData o1, MemberData o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userInfo.getUpdateMembers() != null) {
            datasTeam.clear();
            for (MemberData item: userInfo.getUpdateMembers()) {
                MemberData memberData = new MemberData();
                //memberData.setPresence(item.getPresence());
                memberData.setProfile(item.getProfile());
                memberData.setMeetingMemberId(item.getMeetingMemberId());
                memberData.setName(item.getName());

                datasTeam.add(memberData);
            }
            sort();
            adapterTeam.setListArray(datasTeam);
            updateVisibility();
            userInfo.setUpdateMembers(null);
        } else if (!workplaceInspectionId.equals("")) {
            pd.show();
            new GetWorkplaceInspectionAction(WorkplaceInspectionActivity.this, workplaceInspectionId).execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        text_name.clearAnimation();
        text_date.clearAnimation();
        text_description.clearAnimation();
        text_name_error.clearAnimation();
        text_date_error.clearAnimation();
        text_name_hint.clearAnimation();
        text_date_hint.clearAnimation();
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
        date.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey_dark), PorterDuff.Mode.SRC_ATOP);
        unregisterReceiver(br);
    }
}
