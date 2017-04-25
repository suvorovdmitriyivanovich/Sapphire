package com.sapphire.activities.timeOffRequest;

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
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.MenuFragment;
import com.sapphire.activities.RightFragment;
import com.sapphire.adapters.TimeOffRequestsAdapter;
import com.sapphire.api.PunchesAddAction;
import com.sapphire.api.TimeBanksAction;
import com.sapphire.api.AttendanceCodesAction;
import com.sapphire.api.TimeOffRequestsAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.api.TimeOffRequestDeleteAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.AttendanceCodeData;
import com.sapphire.models.TimeBankAccountData;
import com.sapphire.models.TimeOffRequestData;
import java.util.ArrayList;

public class TimeOffRequestsActivity extends BaseActivity implements TimeOffRequestsAdapter.OnRootTimeOffRequestsClickListener,
                                                                     TimeOffRequestsAdapter.OnOpenTimeOffRequestsClickListener,
                                                                     TimeOffRequestsAdapter.OnDeleteTimeOffRequestsClickListener,
                                                                     TimeOffRequestsAction.RequestTimeOffRequests,
                                                                     TimeOffRequestDeleteAction.RequestTimeOffRequestDelete,
                                                                     TimeBanksAction.RequestTimeBanks,
                                                                     AttendanceCodesAction.RequestAttendanceCodes,
                                                                     UpdateAction.RequestUpdate,
                                                                     PunchesAddAction.RequestPunchesAdd{

    private BroadcastReceiver br;
    private ArrayList<TimeOffRequestData> datas;
    private TimeOffRequestsAdapter adapter;
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

        setContentView(R.layout.activity_time_off_requests);

        UserInfo.getUserInfo().setAllDays(null);

        Intent intent = getIntent();
        edit = intent.getBooleanExtra("edit", false);

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

                new TimeOffRequestDeleteAction(TimeOffRequestsActivity.this, datas.get(currentPosition).getTimeoffRequestId()).execute();
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
                Intent intent = new Intent(TimeOffRequestsActivity.this, TimeOffRequestActivity.class);

                UserInfo.getUserInfo().setDays(null);

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
                    new AttendanceCodesAction(TimeOffRequestsActivity.this).execute();
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
        list.setLayoutManager(new LinearLayoutManager(TimeOffRequestsActivity.this));

        adapter = new TimeOffRequestsAdapter(this, edit);
        list.setAdapter(adapter);

        text_no = findViewById(R.id.text_no);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(TimeOffRequestsActivity.this);
            }
        });

        UpdateBottom();

        if (!edit) {
            add.setVisibility(View.GONE);
        }
    }

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
        if (datas == null || datas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRootTimeOffRequestsClick(int position) {
        Intent intent = new Intent(TimeOffRequestsActivity.this, TimeOffRequestActivity.class);
        TimeOffRequestData data = datas.get(position);
        intent.putExtra("readonly", true);
        intent.putExtra("attendanceCodeId", data.getAttendanceCodeId());
        intent.putExtra("id", data.getTimeoffRequestId());
        intent.putExtra("statusId", data.getTimeoffRequestStatusId());

        UserInfo.getUserInfo().setAllDays(data.getDays());

        startActivity(intent);
    }

    @Override
    public void onOpenTimeOffRequestsClick(int position) {
        Intent intent = new Intent(TimeOffRequestsActivity.this, TimeOffRequestActivity.class);
        TimeOffRequestData data = datas.get(position);
        intent.putExtra("attendanceCodeId", data.getAttendanceCodeId());
        intent.putExtra("id", data.getTimeoffRequestId());
        intent.putExtra("statusId", data.getTimeoffRequestStatusId());

        UserInfo.getUserInfo().setAllDays(data.getDays());

        startActivity(intent);
    }

    @Override
    public void onDeleteTimeOffRequestsClick(int position) {
        currentPosition = position;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onRequestTimeOffRequests(String result, ArrayList<TimeOffRequestData> datas) {
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
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            this.datas = datas;
            adapter.setData(this.datas);

            updateVisibility();

            new TimeBanksAction(TimeOffRequestsActivity.this).execute();

            //pd.hide();
        }
    }

    @Override
    public void onRequestTimeOffRequestDelete(String result) {
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
            new TimeOffRequestsAction(TimeOffRequestsActivity.this).execute();
        }
    }

    @Override
    public void onRequestTimeBanks(String result, ArrayList<TimeBankAccountData> timeBankDatas) {
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
            UserInfo.getUserInfo().setTimeBankDatas(timeBankDatas);

            pd.hide();
        }
    }

    @Override
    public void onRequestAttendanceCodes(String result, ArrayList<AttendanceCodeData> attendanceCodeDatas) {
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
            UserInfo.getUserInfo().setAttendanceCodeDatas(attendanceCodeDatas);

            new TimeOffRequestsAction(TimeOffRequestsActivity.this).execute();

            //pd.hide();
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

    @Override
    public void onRequestPunchesAdd(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(Sapphire.getInstance(), result,
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
        new AttendanceCodesAction(TimeOffRequestsActivity.this).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}