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
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import com.sapphire.R;
import com.sapphire.adapters.TemplatesAdapter;
import com.sapphire.api.TemplatesAction;
import com.sapphire.logic.TemplateData;

import java.util.ArrayList;

public class TemplatesActivity extends AppCompatActivity implements TemplatesAdapter.OnRootClickListener,
                                                                    TemplatesAdapter.OnOpenClickListener,
                                                                    TemplatesAction.RequestTemplates,
                                                                    TemplatesAction.RequestTemplatesData {
    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.TemplatesActivity";
    BroadcastReceiver br;
    private ArrayList<TemplateData> templatesDatas;
    private TemplatesAdapter adapter;
    ProgressDialog pd;
    private ExpandableListView templateslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_templates);

        View menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.LEFT);
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

        templateslist = (ExpandableListView) findViewById(R.id.templateslist);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        templateslist.setIndicatorBounds((width - GetPixelFromDips(50)), (width - GetPixelFromDips(20)));

        adapter = new TemplatesAdapter(this);
        templateslist.setAdapter(adapter);
    }

    @Override
    public void onRootClick(int groupPosition, int childPosition) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenClick(int groupPosition, int childPosition) {
        Intent intent = new Intent(TemplatesActivity.this, TemplateActivity.class);
        TemplateData templateData = templatesDatas.get(groupPosition).getSubTemplates().get(childPosition);
        intent.putExtra("name", templateData.getName());
        intent.putExtra("workplaceInspectionTemplateId", templateData.getWorkplaceInspectionTemplateId());
        startActivity(intent);
    }

    @Override
    public void onRequestTemplates(String result) {
        pd.hide();
    }

    @Override
    public void onRequestTemplatesData(ArrayList<TemplateData> templatesDatas) {
        this.templatesDatas = templatesDatas;
        adapter.setData(templatesDatas);

        //if (current == 1) {
            for (int i = 0; i < templatesDatas.size(); i++) {
                templateslist.expandGroup(i);
            }
        //} else {
        //    for (int i = 0; i < templatesDatas.size(); i++) {
        //        templateslist.collapseGroup(i);
        //    }
        //}

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
        } catch (Exception e) {}

        pd.show();
        new TemplatesAction(TemplatesActivity.this).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
