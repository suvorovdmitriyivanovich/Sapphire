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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.FilesActivity;
import com.sapphire.activities.MenuFragment;
import com.sapphire.activities.RightFragment;
import com.sapphire.adapters.WorkplaceInspectionsAdapter;
import com.sapphire.api.ItemPrioritiesAction;
import com.sapphire.api.ItemStatusesAction;
import com.sapphire.api.MembersAction;
import com.sapphire.api.PunchesAddAction;
import com.sapphire.api.TaskManagementParametersAction;
import com.sapphire.api.TemplatesAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.api.WorkplaceInspectionDeleteAction;
import com.sapphire.api.WorkplaceInspectionsAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ItemPriorityData;
import com.sapphire.models.ItemStatusData;
import com.sapphire.models.ParameterData;
import com.sapphire.models.ProfileData;
import com.sapphire.models.TemplateData;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.WorkplaceInspectionData;
import java.util.ArrayList;

public class WorkplaceInspectionsActivity extends BaseActivity implements WorkplaceInspectionsAdapter.OnRootWorkplaceInspectionsClickListener,
                                                                          WorkplaceInspectionsAdapter.OnOpenWorkplaceInspectionsClickListener,
                                                                          WorkplaceInspectionsAdapter.OnDeleteWorkplaceInspectionsClickListener,
                                                                          WorkplaceInspectionsAdapter.OnFilesWorkplaceInspectionsClickListener,
                                                                          WorkplaceInspectionsAdapter.OnAssignWorkplaceInspectionsClickListener,
                                                                          TemplatesAction.RequestTemplates,
                                                                          TemplatesAction.RequestTemplatesData,
                                                                          WorkplaceInspectionsAction.RequestWorkplaceInspections,
                                                                          WorkplaceInspectionsAction.RequestWorkplaceInspectionsData,
                                                                          WorkplaceInspectionDeleteAction.RequestWorkplaceInspectionDelete,
                                                                          ItemPrioritiesAction.RequestItemPriorities,
                                                                          ItemPrioritiesAction.RequestItemPrioritiesData,
                                                                          ItemStatusesAction.RequestItemStatuses,
                                                                          ItemStatusesAction.RequestItemStatusesData,
                                                                          TaskManagementParametersAction.RequestTaskManagementParameters,
                                                                          MembersAction.RequestMembers,
                                                                          UpdateAction.RequestUpdate,
                                                                          PunchesAddAction.RequestPunchesAdd{

    private BroadcastReceiver br;
    private ArrayList<WorkplaceInspectionData> workplaceInspectionDatas;
    private WorkplaceInspectionsAdapter adapter;
    private ProgressDialog pd;
    private RecyclerView workplaceinspectionslist;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;
    private View text_workplace_no;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private DrawerLayout drawerLayout;

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

        workplaceinspectionslist = (RecyclerView) findViewById(R.id.workplaceinspectionslist);
        workplaceinspectionslist.setNestedScrollingEnabled(false);
        workplaceinspectionslist.setLayoutManager(new LinearLayoutManager(WorkplaceInspectionsActivity.this));

        adapter = new WorkplaceInspectionsAdapter(this, false);
        workplaceinspectionslist.setAdapter(adapter);

        text_workplace_no = findViewById(R.id.text_workplace_no);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(WorkplaceInspectionsActivity.this);
            }
        });

        UpdateBottom();
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
        if (workplaceInspectionDatas == null || workplaceInspectionDatas.size() == 0) {
            text_workplace_no.setVisibility(View.VISIBLE);
            workplaceinspectionslist.setVisibility(View.GONE);
        } else {
            workplaceinspectionslist.setVisibility(View.VISIBLE);
            text_workplace_no.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRootWorkplaceInspectionsClick(int position) {
        Intent intent = new Intent(WorkplaceInspectionsActivity.this, WorkplaceInspectionActivity.class);
        WorkplaceInspectionData workplaceInspectionData = workplaceInspectionDatas.get(position);
        intent.putExtra("readonly", true);
        intent.putExtra("name", workplaceInspectionData.getName());
        intent.putExtra("description", workplaceInspectionData.getDescription());
        intent.putExtra("date", workplaceInspectionData.getDate());
        intent.putExtra("workplaceInspectionId", workplaceInspectionData.getWorkplaceInspectionId());
        intent.putExtra("posted", workplaceInspectionData.getPostedOnBoard());
        startActivity(intent);
    }

    @Override
    public void onOpenWorkplaceInspectionsClick(int position) {
        Intent intent = new Intent(WorkplaceInspectionsActivity.this, WorkplaceInspectionActivity.class);
        WorkplaceInspectionData workplaceInspectionData = workplaceInspectionDatas.get(position);
        intent.putExtra("name", workplaceInspectionData.getName());
        intent.putExtra("description", workplaceInspectionData.getDescription());
        intent.putExtra("date", workplaceInspectionData.getDate());
        intent.putExtra("workplaceInspectionId", workplaceInspectionData.getWorkplaceInspectionId());
        intent.putExtra("posted", workplaceInspectionData.getPostedOnBoard());
        startActivity(intent);
    }

    @Override
    public void onDeleteWorkplaceInspectionsClick(int position) {
        currentPosition = position;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onFilesWorkplaceInspectionsClick(int position) {
        Intent intent = new Intent(WorkplaceInspectionsActivity.this, FilesActivity.class);
        WorkplaceInspectionData workplaceInspectionData = workplaceInspectionDatas.get(position);
        intent.putExtra("name", workplaceInspectionData.getName());
        intent.putExtra("id", workplaceInspectionData.getWorkplaceInspectionId());
        intent.putExtra("url", Environment.WorkplaceInspectionsFilesURL);
        intent.putExtra("nameField", "WorkplaceInspectionId");

        UserInfo.getUserInfo().setFileDatas(workplaceInspectionData.getFiles());
        startActivity(intent);
    }

    @Override
    public void onAssignWorkplaceInspectionsClick(int position) {
        Intent intent = new Intent(WorkplaceInspectionsActivity.this, AssignActivity.class);
        WorkplaceInspectionData workplaceInspectionData = workplaceInspectionDatas.get(position);
        intent.putExtra("name", workplaceInspectionData.getName());
        intent.putExtra("description", workplaceInspectionData.getDescription());
        intent.putExtra("date", workplaceInspectionData.getDate());
        intent.putExtra("workplaceInspectionId", workplaceInspectionData.getWorkplaceInspectionId());
        intent.putExtra("inspected", workplaceInspectionData.getInspected());

        UserInfo.getUserInfo().setWorkplaceInspection(workplaceInspectionData);

        startActivity(intent);
    }

    @Override
    public void onRequestWorkplaceInspections(String result) {
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
    public void onRequestWorkplaceInspectionDelete(String result) {
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
            new WorkplaceInspectionsAction(WorkplaceInspectionsActivity.this, false).execute();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionsData(ArrayList<WorkplaceInspectionData> workplaceInspectionDatas) {
        this.workplaceInspectionDatas = workplaceInspectionDatas;
        adapter.setData(workplaceInspectionDatas);

        updateVisibility();

        new TemplatesAction(WorkplaceInspectionsActivity.this, getResources().getString(R.string.text_workplace_templates)).execute();

        //pd.hide();
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
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    @Override
    public void onRequestTemplatesData(ArrayList<TemplateData> templatesDatas, String type) {
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
    public void onRequestItemStatusesData(ArrayList<ItemStatusData> itemStatusDatas) {
        UserInfo.getUserInfo().setItemStatusDatas(itemStatusDatas);

        new TaskManagementParametersAction(WorkplaceInspectionsActivity.this).execute();

        //pd.hide();
    }

    @Override
    public void onRequestTaskManagementParameters(String result, ArrayList<ParameterData> parameterDatas) {
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
            UserInfo.getUserInfo().setParameterDatas(parameterDatas);

            new MembersAction(WorkplaceInspectionsActivity.this).execute();
        }
    }

    @Override
    public void onRequestMembers(String result, ArrayList<ProfileData> profilesDatas) {
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
            UserInfo.getUserInfo().setAllAssignedProfiles(profilesDatas);
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
        new WorkplaceInspectionsAction(WorkplaceInspectionsActivity.this, false).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
