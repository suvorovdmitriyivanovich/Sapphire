package com.dealerpilothr.activities.organizationStructure;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.adapters.MeetingMembersAdapter;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.MemberData;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.R;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.logic.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChooseOrganizationStructureActivity extends BaseActivity implements MeetingMembersAdapter.OnRootMeetingMembersClickListener,
        UpdateAction.RequestUpdate {
    private RecyclerView list;
    private View text_no;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean readonly = false;
    private ArrayList<MemberData> datas = new ArrayList<MemberData>();
    private MeetingMembersAdapter adapter;
    private UserInfo userInfo;
    private ProgressDialog pd;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private boolean excludeMembers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_organization_structure);

        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        View button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notEquals()) {
                    save();
                } else {
                    finish();
                }
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        userInfo = UserInfo.getUserInfo();

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
                save();
            }
        });

        Intent intent = getIntent();
        excludeMembers = intent.getBooleanExtra("excludeMembers", false);

        /*
        datas.clear();
        for (ProfileData item: userInfo.getCurrentOrganizationStructures()) {
            MemberData memberData = new MemberData();
            memberData.setProfile(item);

            datas.add(memberData);
        }
        */
        datas.clear();
        /*
        for (MemberData item: userInfo.getUpdateMembers()) {
            MemberData memberData = new MemberData();
            memberData.setPresence(item.getPresence());
            memberData.setProfile(item.getProfile());
            memberData.setMeetingMemberId(item.getMeetingMemberId());

            datas.add(memberData);
        }
        */
        for (ProfileData item: userInfo.getCurrentOrganizationStructures()) {
            if (excludeMembers) {
                boolean needContinue = false;
                for (MemberData itemProfile: userInfo.getChooseMembers()) {
                    if (item.getProfileId().equals(itemProfile.getProfile().getProfileId())) {
                        needContinue = true;
                        break;
                    }
                }
                if (needContinue) {
                    continue;
                }
            }
            MemberData memberData = new MemberData();
            memberData.setProfile(item);
            memberData.setName(item.getName());
            for (MemberData itemProfile: userInfo.getUpdateMembers()) {
                if (item.getProfileId().equals(itemProfile.getProfile().getProfileId())) {
                    //memberData.setPresence(itemProfile.getPresence());
                    memberData.setPresence(true);
                    break;
                }
            }
            datas.add(memberData);
        }
        sort();

        list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(ChooseOrganizationStructureActivity.this));

        adapter = new MeetingMembersAdapter(this, readonly, false);
        list.setAdapter(adapter);

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

        text_no = findViewById(R.id.text_no);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(ChooseOrganizationStructureActivity.this);
            }
        });

        UpdateBottom();

        if (readonly) {
            button_ok.setVisibility(View.GONE);
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

    private void sort() {
        Collections.sort(datas, new Comparator<MemberData>() {
            public int compare(MemberData o1, MemberData o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    @Override
    public void onRootMeetingMembersClick(int position) {

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

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private boolean notEquals() {
        if (readonly) {
            return false;
        }

        boolean rezult = false;

        ArrayList<MemberData> memberDatas = userInfo.getUpdateMembers();
        int num = 0;
        for (int i=0; i < datas.size(); i++) {
            if (!datas.get(i).getPresence()) {
                continue;
            }
            if (memberDatas.size() <= num) {
                rezult = true;
                break;
            } else {
                if (!datas.get(i).getProfile().getProfileId().equals(memberDatas.get(num).getProfile().getProfileId())) {
                    rezult = true;
                    break;
                }
            }
            num++;
        }
        if (!rezult && memberDatas.size() != num) {
            rezult = true;
        }

        return rezult;
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

    private void save() {
        ArrayList<MemberData> memberDatas = new ArrayList<MemberData>();
        for (MemberData item: datas) {
            if (!item.getPresence()) {
                continue;
            }
            memberDatas.add(item);
        }

        userInfo.setUpdateMembers(memberDatas);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
