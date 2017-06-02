package com.dealerpilothr.activities.meeting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.FilesActivity;
import com.dealerpilothr.activities.MenuFragment;
import com.dealerpilothr.activities.RightFragment;
import com.dealerpilothr.activities.policy.PdfActivity;
import com.dealerpilothr.api.MeetingDeleteAction;
import com.dealerpilothr.api.MeetingsAction;
import com.dealerpilothr.api.MembersAction;
import com.dealerpilothr.api.PunchesAddAction;
import com.dealerpilothr.api.TemplatesAction;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.MemberData;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.models.TemplateData;
import com.dealerpilothr.R;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.adapters.MeetingsAdapter;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.MeetingData;
import java.util.ArrayList;

public class MeetingsActivity extends BaseActivity implements MeetingsAdapter.OnRootMeetingsClickListener,
                                                              MeetingsAdapter.OnOpenMeetingsClickListener,
                                                              MeetingsAdapter.OnDeleteMeetingsClickListener,
                                                              MeetingsAdapter.OnFilesMeetingsClickListener,
                                                              MeetingsAdapter.OnReportMeetingsClickListener,
                                                              TemplatesAction.RequestTemplates,
                                                              TemplatesAction.RequestTemplatesData,
                                                              MeetingsAction.RequestMeetings,
                                                              MeetingDeleteAction.RequestMeetingDelete,
                                                              MembersAction.RequestMembers,
                                                              UpdateAction.RequestUpdate,
                                                              PunchesAddAction.RequestPunchesAdd {

    private BroadcastReceiver br;
    private ArrayList<MeetingData> datas;
    private MeetingsAdapter adapter;
    private ProgressDialog pd;
    private RecyclerView list;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;
    private View text_no;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private DrawerLayout drawerLayout;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meetings);

        Intent intent = getIntent();
        String urlroute = intent.getStringExtra("urlroute");
        if (urlroute != null && !urlroute.equals("")) {
            UserInfo userInfo = UserInfo.getUserInfo();
            if (userInfo.getGlobalAppRoleAppSecurities().getSecurityMode(urlroute, "").equals("fullAccess")) {
                edit = true;
            }
        }

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
            }
        });

        button_send_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();

                pd.show();

                new MeetingDeleteAction(MeetingsActivity.this, datas.get(currentPosition).getMeetingId()).execute();
            }
        });

        View menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        View exit = findViewById(R.id.delete);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingsActivity.this, MeetingActivity.class);
                startActivity(intent);
            }
        });

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(Environment.PARAM_TASK);

                if (putreqwest.equals("updateleftmenu")) {
                    try {
                        Fragment fragment = new MenuFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();
                    } catch (Exception e) {}
                } else if (putreqwest.equals("updaterightmenu")) {
                    try {
                        Fragment fragmentRight = new RightFragment(pd);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.nav_right, fragmentRight).commit();
                    } catch (Exception e) {}
                } else if (putreqwest.equals("updatebottom")) {
                    UpdateBottom();
                } else if (putreqwest.equals("update")) {
                    pd.show();
                    new MeetingsAction(MeetingsActivity.this, false).execute();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(MeetingsActivity.this));

        adapter = new MeetingsAdapter(this, false, edit);
        list.setAdapter(adapter);

        text_no = findViewById(R.id.text_no);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(MeetingsActivity.this);
            }
        });

        UpdateBottom();

        if (!edit) {
            add.setVisibility(View.GONE);
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

    public void updateVisibility() {
        if (datas == null || datas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRootMeetingsClick(int position) {
        Intent intent = new Intent(MeetingsActivity.this, MeetingActivity.class);
        MeetingData data = datas.get(position);
        intent.putExtra("readonly", true);
        intent.putExtra("name", data.getName());
        intent.putExtra("location", data.getLocation());
        intent.putExtra("date", data.getMeetingDate());
        intent.putExtra("dateend", data.getEndTime());
        intent.putExtra("id", data.getMeetingId());
        intent.putExtra("posted", data.getPosted());
        intent.putExtra("completed", data.getCompleted());
        UserInfo userInfo = UserInfo.getUserInfo();
        ArrayList<MemberData> memberDatas = new ArrayList<MemberData>();
        for (MemberData itemProfile: data.getMembers()) {
            memberDatas.add(itemProfile);
        }
        for (MemberData itemProfile: data.getProfiles()) {
            MemberData memberData = itemProfile;
            memberData.setIsProfile(true);
            memberDatas.add(memberData);
        }
        userInfo.setMembers(memberDatas);
        userInfo.setTopics(data.getTopics());
        startActivity(intent);
    }

    @Override
    public void onOpenMeetingsClick(int position) {
        Intent intent = new Intent(MeetingsActivity.this, MeetingActivity.class);
        MeetingData data = datas.get(position);
        intent.putExtra("name", data.getName());
        intent.putExtra("location", data.getLocation());
        intent.putExtra("date", data.getMeetingDate());
        intent.putExtra("dateend", data.getEndTime());
        intent.putExtra("id", data.getMeetingId());
        intent.putExtra("posted", data.getPosted());
        intent.putExtra("completed", data.getCompleted());
        UserInfo userInfo = UserInfo.getUserInfo();
        ArrayList<MemberData> memberDatas = new ArrayList<MemberData>();
        for (MemberData itemProfile: data.getMembers()) {
            memberDatas.add(itemProfile);
        }
        for (MemberData itemProfile: data.getProfiles()) {
            MemberData memberData = itemProfile;
            memberData.setIsProfile(true);
            memberDatas.add(memberData);
        }
        userInfo.setMembers(memberDatas);
        userInfo.setTopics(data.getTopics());
        startActivity(intent);
    }

    @Override
    public void onDeleteMeetingsClick(int position) {
        currentPosition = position;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onFilesMeetingsClick(int position) {
        Intent intent = new Intent(MeetingsActivity.this, FilesActivity.class);
        MeetingData meetingData = datas.get(position);
        intent.putExtra("name", meetingData.getName());
        intent.putExtra("id", meetingData.getMeetingId());
        intent.putExtra("url", Environment.MeetingsFilesURL);
        intent.putExtra("nameField", "MeetingId");
        //intent.putExtra("readonly", meetingData.getCompleted());

        UserInfo.getUserInfo().setFileDatas(meetingData.getFiles());
        startActivity(intent);
    }

    @Override
    public void onReportMeetingsClick(int position) {
        Intent intent = new Intent(MeetingsActivity.this, PdfActivity.class);
        MeetingData data = datas.get(position);
        intent.putExtra("name", data.getName());
        intent.putExtra("acknowledged", true);
        intent.putExtra("id", data.getMeetingId());
        if (!data.getCustomReportId().equals("")) {
            intent.putExtra("fileId", data.getCustomReportId());
        } else {
            intent.putExtra("fileId", data.getMeetingId());
            intent.putExtra("url", Environment.MeetingsReportURL);
        }
        intent.putExtra("nolog", true);
        startActivity(intent);
    }

    @Override
    public void onRequestMeetings(String result, ArrayList<MeetingData> datas) {
        if (!result.equals("OK")) {
            updateVisibility();
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
            this.datas = datas;
            adapter.setData(datas);

            updateVisibility();

            new TemplatesAction(MeetingsActivity.this, getResources().getString(R.string.text_meetings_templates)).execute();
        }
    }

    @Override
    public void onRequestMeetingDelete(String result) {
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
            new MeetingsAction(MeetingsActivity.this, false).execute();
        }
    }

    @Override
    public void onRequestTemplates(String result) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    @Override
    public void onRequestTemplatesData(ArrayList<TemplateData> templatesDatas, String type) {
        UserInfo.getUserInfo().setTemplateMeetingDatas(templatesDatas);

        new MembersAction(MeetingsActivity.this).execute();

        //pd.hide();
    }

    @Override
    public void onRequestMembers(String result, ArrayList<ProfileData> datas) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            ArrayList<MemberData> datasMembers = new ArrayList<MemberData>();
            for (ProfileData item: datas) {
                MemberData memberData = new MemberData();
                memberData.setProfile(item);

                datasMembers.add(memberData);
            }
            UserInfo.getUserInfo().setAllMembers(datasMembers);
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

    @Override
    public void onRequestPunchesAdd(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(Dealerpilothr.getInstance(), result,
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
            drawerLayout.closeDrawers();
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

        try {
            Fragment fragment = new MenuFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();

            Fragment fragmentRight = new RightFragment(pd);
            fragmentManager.beginTransaction().replace(R.id.nav_right, fragmentRight).commit();
        } catch (Exception e) {}

        pd.show();
        new MeetingsAction(MeetingsActivity.this, false).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
