package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.adapters.AdressAdapter;
import com.sapphire.api.GetContactsAction;
import com.sapphire.api.PunchesAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ContactData;
import java.util.ArrayList;

public class MyContactsActivity extends BaseActivity implements GetContactsAction.RequestContactsMe,
                                                                UpdateAction.RequestUpdate,
                                                                PunchesAddAction.RequestPunchesAdd{
    private ProgressDialog pd;
    private RecyclerView emergencylist;
    private AdressAdapter adapterEmergency;
    private RecyclerView familylist;
    private AdressAdapter adapterFamily;
    private View emergency_group;
    private View family_group;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private DrawerLayout drawerLayout;
    private boolean viewEmergency = false;
    private boolean viewFamily = false;
    private boolean editEmergency = false;
    private boolean editFamily = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_contacts);

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

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        emergency_group = findViewById(R.id.emergency_group);
        family_group = findViewById(R.id.family_group);

        emergencylist = (RecyclerView) findViewById(R.id.emergencylist);
        adapterEmergency = new AdressAdapter(this, true);
        emergencylist.setAdapter(adapterEmergency);
        emergencylist.setLayoutManager(new LinearLayoutManager(this));

        familylist = (RecyclerView) findViewById(R.id.familylist);
        adapterFamily = new AdressAdapter(this, true);
        familylist.setAdapter(adapterFamily);
        familylist.setLayoutManager(new LinearLayoutManager(this));

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
                    if (editEmergency || viewEmergency || editFamily || viewFamily) {
                        boolean needEmergency = (editEmergency || viewEmergency);
                        boolean needFamily = (editFamily || viewFamily);
                        pd.show();
                        new GetContactsAction(MyContactsActivity.this, true, needEmergency, needFamily).execute();
                    }
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(MyContactsActivity.this);
            }
        });

        UpdateBottom();

        UserInfo userInfo = UserInfo.getUserInfo();
        String securityModeEmergency = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/me/my-emergency contacts", "");
        if (securityModeEmergency.equals("fullAccess")) {
            editEmergency = true;
        } else if (securityModeEmergency.equals("viewOnly")) {
            viewEmergency = true;
        }

        String securityModeFamily = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/me/my-family-members", "");
        if (securityModeFamily.equals("fullAccess")) {
            editFamily = true;
        } else if (securityModeFamily.equals("viewOnly")) {
            viewFamily = true;
        }

        if (!editEmergency && !viewEmergency) {
            View text_emergency = findViewById(R.id.text_emergency);
            text_emergency.setVisibility(View.GONE);

            View border = findViewById(R.id.border);
            border.setVisibility(View.GONE);

            emergency_group.setVisibility(View.GONE);
        }

        if (!editFamily && !viewFamily) {
            View text_family = findViewById(R.id.text_family);
            text_family.setVisibility(View.GONE);

            View border2 = findViewById(R.id.border2);
            border2.setVisibility(View.GONE);

            family_group.setVisibility(View.GONE);
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

    @Override
    public void onRequestContactsMe(String result, ArrayList<ContactData> emergencyDatas, ArrayList<ContactData> familyDatas) {
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
            if (emergencyDatas == null || emergencyDatas.size() == 0) {
                emergency_group.setVisibility(View.GONE);
            } else {
                adapterEmergency.setData(emergencyDatas);
                emergency_group.setVisibility(View.VISIBLE);
            }
            if (familyDatas == null || familyDatas.size() == 0) {
                family_group.setVisibility(View.GONE);
            } else {
                adapterFamily.setData(familyDatas);
                family_group.setVisibility(View.VISIBLE);
            }
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

        if (editEmergency || viewEmergency || editFamily || viewFamily) {
            boolean needEmergency = (editEmergency || viewEmergency);
            boolean needFamily = (editFamily || viewFamily);
            pd.show();
            new GetContactsAction(MyContactsActivity.this, true, needEmergency, needFamily).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
