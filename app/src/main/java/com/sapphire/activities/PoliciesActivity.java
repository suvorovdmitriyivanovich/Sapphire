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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.sapphire.R;
import com.sapphire.adapters.PoliciesAdapter;
import com.sapphire.api.PoliciesAction;
import com.sapphire.logic.PoliciesData;
import java.util.ArrayList;

public class PoliciesActivity extends BaseActivity implements PoliciesAdapter.OnRootPoliciesClickListener,
                                                                   PoliciesAdapter.OnOpenPoliciesClickListener,
                                                                   PoliciesAction.RequestPolicies,
                                                                   PoliciesAction.RequestPoliciesData {
    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.PoliciesActivity";
    private BroadcastReceiver br;
    private ArrayList<PoliciesData> policiesDatas;
    private PoliciesAdapter adapter;
    private ProgressDialog pd;
    private ExpandableListView policieslist;
    private View text_policies_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_policies);

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

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(PARAM_TASK);

                if (putreqwest.equals("updateleftmenu")) {
                    try {
                        Fragment fragment = new MenuFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();
                    } catch (Exception e) {}
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        /*
        //policiesDatas = DBHelper.getInstance(Sapphire.getInstance()).getPolicies();
        policiesDatas = new ArrayList<PoliciesData>();

        PoliciesData policiesData = new PoliciesData();
        policiesData.setName("Test Policy 1");

        PoliciesData policiesData2 = new PoliciesData();
        policiesData2.setName("Test Policy 1 1 View test");
        policiesData2.setIsAcknowledged(true);
        policiesData.getSubPolicies().add(policiesData2);

        PoliciesData policiesData3 = new PoliciesData();
        policiesData3.setName("Test Policy 1 2");
        policiesData3.setIsAcknowledged(false);
        policiesData.getSubPolicies().add(policiesData3);

        policiesDatas.add(policiesData);

        PoliciesData policiesData1 = new PoliciesData();
        policiesData1.setName("Dan Policies");

        PoliciesData policiesData21 = new PoliciesData();
        policiesData21.setName("Dan Policy 2016");
        policiesData21.setIsAcknowledged(true);
        policiesData1.getSubPolicies().add(policiesData21);

        policiesDatas.add(policiesData1);
        */

        policieslist = (ExpandableListView) findViewById(R.id.policieslist);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        policieslist.setIndicatorBounds((width - GetPixelFromDips(50)), (width - GetPixelFromDips(20)));

        /*
        adapter = new PoliciesAdapter(this, policiesDatas);
        policieslist.setAdapter(adapter);
        */
        adapter = new PoliciesAdapter(this);
        policieslist.setAdapter(adapter);

        text_policies_no = findViewById(R.id.text_policies_no);
    }

    public void updateVisibility() {
        if (policiesDatas == null || policiesDatas.size() == 0) {
            text_policies_no.setVisibility(View.VISIBLE);
            policieslist.setVisibility(View.GONE);
        } else {
            policieslist.setVisibility(View.VISIBLE);
            text_policies_no.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRootPoliciesClick(int groupPosition, int childPosition) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenPoliciesClick(int groupPosition, int childPosition) {
        Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        PoliciesData policiesData = policiesDatas.get(groupPosition).getSubPolicies().get(childPosition);
        intent.putExtra("acknowledged", policiesData.getIsAcknowledged());
        intent.putExtra("name", policiesData.getName());
        intent.putExtra("fileId", policiesData.getFileId());
        intent.putExtra("duration", (policiesData.getDuration().getHours() * 60 * 60) + (policiesData.getDuration().getMinutes() * 60) + policiesData.getDuration().getSeconds());
        intent.putExtra("id", policiesData.getPolicyId());
        startActivity(intent);
    }

    @Override
    public void onRequestPolicies(String result) {
        updateVisibility();
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPoliciesData(ArrayList<PoliciesData> policiesDatas) {
        this.policiesDatas = policiesDatas;
        adapter.setData(policiesDatas);

        //if (current == 1) {
        for (int i = 0; i < policiesDatas.size(); i++) {
            policieslist.expandGroup(i);
        }
        //} else {
        //    for (int i = 0; i < policiesDatas.size(); i++) {
        //        policieslist.collapseGroup(i);
        //    }
        //}

        updateVisibility();

        pd.hide();
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

            Fragment fragmentRight = new RightFragment();
            fragmentManager.beginTransaction().replace(R.id.nav_right, fragmentRight).commit();
        } catch (Exception e) {}

        pd.show();
        new PoliciesAction(PoliciesActivity.this, false).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
