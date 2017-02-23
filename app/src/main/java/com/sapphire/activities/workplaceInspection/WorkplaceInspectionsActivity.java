package com.sapphire.activities.workplaceInspection;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.MenuFragment;
import com.sapphire.activities.RightFragment;
import com.sapphire.adapters.WorkplaceInspectionsAdapter;
import com.sapphire.api.ItemPrioritiesAction;
import com.sapphire.api.ItemStatusesAction;
import com.sapphire.api.TemplatesAction;
import com.sapphire.api.WorkplaceInspectionDeleteAction;
import com.sapphire.api.WorkplaceInspectionsAction;
import com.sapphire.logic.ItemPriorityData;
import com.sapphire.logic.ItemStatusData;
import com.sapphire.logic.TemplateData;
import com.sapphire.logic.UserInfo;
import com.sapphire.logic.WorkplaceInspectionData;
import java.util.ArrayList;

public class WorkplaceInspectionsActivity extends BaseActivity implements WorkplaceInspectionsAdapter.OnRootClickListener,
                                                                          WorkplaceInspectionsAdapter.OnOpenClickListener,
                                                                          WorkplaceInspectionsAdapter.OnDeleteClickListener,
                                                                          TemplatesAction.RequestTemplates,
                                                                          TemplatesAction.RequestTemplatesData,
                                                                          WorkplaceInspectionsAction.RequestWorkplaceInspections,
                                                                          WorkplaceInspectionsAction.RequestWorkplaceInspectionsData,
                                                                          WorkplaceInspectionDeleteAction.RequestWorkplaceInspectionDelete,
                                                                          ItemPrioritiesAction.RequestItemPriorities,
                                                                          ItemPrioritiesAction.RequestItemPrioritiesData,
                                                                          ItemStatusesAction.RequestItemStatuses,
                                                                          ItemStatusesAction.RequestItemStatusesData{
    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.workplaceInspection.WorkplaceInspectionsActivity";
    BroadcastReceiver br;
    private ArrayList<WorkplaceInspectionData> workplaceInspectionDatas;
    private WorkplaceInspectionsAdapter adapter;
    ProgressDialog pd;
    private RecyclerView workplaceinspectionslist;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workplace_inspections);

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

                new WorkplaceInspectionDeleteAction(WorkplaceInspectionsActivity.this, workplaceInspectionDatas.get(currentPosition).getWorkplaceInspectionId()).execute();
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
                Intent intent = new Intent(WorkplaceInspectionsActivity.this, WorkplaceInspectionActivity.class);
                startActivity(intent);
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

        workplaceinspectionslist = (RecyclerView) findViewById(R.id.workplaceinspectionslist);
        workplaceinspectionslist.setNestedScrollingEnabled(false);
        workplaceinspectionslist.setLayoutManager(new LinearLayoutManager(WorkplaceInspectionsActivity.this));

        adapter = new WorkplaceInspectionsAdapter(this);
        workplaceinspectionslist.setAdapter(adapter);
    }

    @Override
    public void onRootClick(int position) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenClick(int position) {
        Intent intent = new Intent(WorkplaceInspectionsActivity.this, WorkplaceInspectionActivity.class);
        WorkplaceInspectionData templateData = workplaceInspectionDatas.get(position);
        intent.putExtra("name", templateData.getName());
        intent.putExtra("description", templateData.getDescription());
        intent.putExtra("date", templateData.getDate());
        intent.putExtra("workplaceInspectionId", templateData.getWorkplaceInspectionId());
        intent.putExtra("posted", templateData.getPosted());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        currentPosition = position;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onRequestWorkplaceInspections(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionDelete(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            new WorkplaceInspectionsAction(WorkplaceInspectionsActivity.this).execute();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionsData(ArrayList<WorkplaceInspectionData> workplaceInspectionDatas) {
        this.workplaceInspectionDatas = workplaceInspectionDatas;
        adapter.setData(workplaceInspectionDatas);

        new TemplatesAction(WorkplaceInspectionsActivity.this).execute();

        //pd.hide();
    }

    @Override
    public void onRequestTemplates(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestTemplatesData(ArrayList<TemplateData> templatesDatas) {
        UserInfo.getUserInfo().setTemplateDatas(templatesDatas);

        new ItemPrioritiesAction(WorkplaceInspectionsActivity.this).execute();

        //pd.hide();
    }

    @Override
    public void onRequestItemPriorities(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestItemPrioritiesData(ArrayList<ItemPriorityData> itemPriorityDatas) {
        UserInfo.getUserInfo().setItemPriorityDatas(itemPriorityDatas);

        new ItemStatusesAction(WorkplaceInspectionsActivity.this).execute();

        //pd.hide();
    }

    @Override
    public void onRequestItemStatuses(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestItemStatusesData(ArrayList<ItemStatusData> itemStatusDatas) {
        UserInfo.getUserInfo().setItemStatusDatas(itemStatusDatas);

        pd.hide();
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
        new WorkplaceInspectionsAction(WorkplaceInspectionsActivity.this).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
