package com.sapphire.activities.organizationStructure;

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
import android.widget.Toast;

import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.MenuFragment;
import com.sapphire.activities.RightFragment;
import com.sapphire.adapters.organizationStructure.OrganizationStructureAdapter;
import com.sapphire.api.OrganizationStructureAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.OrganizationStructureData;
import com.sapphire.logic.UserInfo;

import java.util.ArrayList;

public class OrganizationStructureActivity extends BaseActivity implements OrganizationStructureAdapter.OnRootClickListener,
                                                                           OrganizationStructureAdapter.OnAddClickListener,
                                                                           OrganizationStructureAction.RequestOrganizationStructures,
                                                                           OrganizationStructureAction.RequestOrganizationStructuresData {

    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.organizationStructure.OrganizationStructureActivity";
    BroadcastReceiver br;
    private ProgressDialog pd;
    private OrganizationStructureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_organization_structure);

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
        pd.show();

        RecyclerView organizations = (RecyclerView) findViewById(R.id.organizations);
        organizations.setNestedScrollingEnabled(false);
        organizations.setLayoutManager(new LinearLayoutManager(OrganizationStructureActivity.this));
        adapter = new OrganizationStructureAdapter(OrganizationStructureActivity.this);
        organizations.setAdapter(adapter);

        new OrganizationStructureAction(OrganizationStructureActivity.this).execute();
    }

    @Override
    public void onRequestOrganizationStructure(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestOrganizationStructuresData(ArrayList<OrganizationStructureData> organizationStructureDatas) {
        UserInfo.getUserInfo().setOrganizationStructureDatas(organizationStructureDatas);
        adapter.setData(organizationStructureDatas);

        pd.hide();
    }

    @Override
    public void onAddClick(int position) {
        Intent intent = new Intent(OrganizationStructureActivity.this, OrganizationStructureItemActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        startActivity(intent);
    }

    @Override
    public void onRootClick(int position) {
        if(adapter.getDataItem(position).getSubOrganizationStructures().isEmpty()) {
            return;
        }
        Intent intent = new Intent(OrganizationStructureActivity.this, OrganizationStructureListActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        startActivity(intent);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
