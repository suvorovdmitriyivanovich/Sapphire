package com.sapphire.activities.workplaceInspection;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.LoginActivity;
import com.sapphire.adapters.WorkplaceInspectionItemsAdapter;
import com.sapphire.api.GetWorkplaceInspectionAction;
import com.sapphire.api.TaskManagementLinksAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.db.DBHelper;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.LinkTaskData;
import com.sapphire.models.ProfileData;
import com.sapphire.models.TaskData;
import com.sapphire.models.WorkplaceInspectionData;
import com.sapphire.models.WorkplaceInspectionItemData;
import java.util.ArrayList;

public class AssignActivity extends BaseActivity implements GetWorkplaceInspectionAction.RequestWorkplaceInspection,
                                                            TaskManagementLinksAction.RequestTaskManagementLinks,
                                                            WorkplaceInspectionItemsAdapter.OnRootClickListener,
                                                            WorkplaceInspectionItemsAdapter.OnFilesClickListener,
                                                            UpdateAction.RequestUpdate{
    private String workplaceInspectionId = "";
    private ProgressDialog pd;
    private ArrayList<WorkplaceInspectionItemData> allsDatas = new ArrayList<WorkplaceInspectionItemData>();
    private ArrayList<WorkplaceInspectionItemData> datas = new ArrayList<WorkplaceInspectionItemData>();
    private RecyclerView itemlist;
    private WorkplaceInspectionItemsAdapter adapter;
    private CheckBox onlyfailed;
    private WorkplaceInspectionData workplaceInspectionData = new WorkplaceInspectionData();
    private View text_no;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private Button assign;
    private boolean existFail = false;
    private boolean existAllTask = false;
    private String linkId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assign);

        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        onlyfailed = (CheckBox) findViewById(R.id.onlyfailed);
        onlyfailed.setChecked(true);
        onlyfailed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateOnlyFailed(isChecked);
            }
        });

        assign = (Button) findViewById(R.id.assign);
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Intent intent = getIntent();
        workplaceInspectionId = intent.getStringExtra("workplaceInspectionId");
        if (workplaceInspectionId == null) {
            workplaceInspectionId = "";
        }

        View root = findViewById(R.id.rootLayout);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemlist = (RecyclerView) findViewById(R.id.itemlist);
        itemlist.setNestedScrollingEnabled(false);
        itemlist.setLayoutManager(new LinearLayoutManager(AssignActivity.this));

        adapter = new WorkplaceInspectionItemsAdapter(this, true, true);
        itemlist.setAdapter(adapter);

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

                new UpdateAction(AssignActivity.this);
            }
        });

        UpdateBottom();
    }

    private void updateOnlyFailed(boolean isChecked) {
        if (isChecked) {
            ArrayList<WorkplaceInspectionItemData> allDatas = new ArrayList<WorkplaceInspectionItemData>();

            for (WorkplaceInspectionItemData item : allsDatas) {
                if (item.getStatus().getWorkplaceInspectionItemStatusId().equals(Environment.StatusFail)) {
                    allDatas.add(item);
                }
            }

            this.datas = allDatas;
        } else {
            this.datas = allsDatas;
        }

        adapter.setListArray(this.datas);
        updateVisibility();
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
            itemlist.setVisibility(View.GONE);
        } else {
            itemlist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
        if (allsDatas == null || allsDatas.size() == 0) {
            onlyfailed.setVisibility(View.GONE);
        } else {
            onlyfailed.setVisibility(View.VISIBLE);
        }
        if (existFail) {
            assign.setVisibility(View.VISIBLE);
        } else {
            assign.setVisibility(View.GONE);
        }
        if (existAllTask) {
            assign.setEnabled(true);
        } else {
            assign.setEnabled(false);
        }
    }

    @Override
    public void onRequestWorkplaceInspection(String result, ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas) {
        if (!result.equals("OK") && !result.equals(getResources().getString(R.string.text_need_internet))) {
            updateVisibility();
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            return;
        }

        ArrayList<WorkplaceInspectionItemData> allDatas = new ArrayList<WorkplaceInspectionItemData>();
        ArrayList<WorkplaceInspectionItemData> datas = DBHelper.getInstance(Sapphire.getInstance()).getWorkplaceInspectionItems(workplaceInspectionId);

        existFail = false;
        boolean isExist = false;
        for (WorkplaceInspectionItemData item: workplaceInspectionItemDatas) {
            if (item.getStatus().getWorkplaceInspectionItemStatusId().equals(Environment.StatusFail)) {
                existFail = true;
            }

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
            if (item.getStatus().getWorkplaceInspectionItemStatusId().equals(Environment.StatusFail)) {
                existFail = true;
            }

            allDatas.add(item);
        }

        this.allsDatas = allDatas;

        new TaskManagementLinksAction(AssignActivity.this, this.allsDatas, workplaceInspectionId).execute();

        //updateOnlyFailed(onlyfailed.isChecked());
        //
        //pd.hide();
    }

    @Override
    public void onRequestTaskManagementLinks(String result, ArrayList<LinkTaskData> datas, String linkId) {
        if (!result.equals("OK")) {
            existAllTask = false;
            allsDatas.clear();
            updateOnlyFailed(onlyfailed.isChecked());

            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        } else {
            this.linkId = linkId;
            for (LinkTaskData item: datas) {
                for (WorkplaceInspectionItemData item2: allsDatas) {
                    if (item2.getWorkplaceInspectionItemId().equals(item.getLinkId())) {
                        item2.setTask(item.getTask());
                        break;
                    }
                }
            }
            existAllTask = true;
            for (WorkplaceInspectionItemData item: allsDatas) {
                if (item.getStatus().getWorkplaceInspectionItemStatusId().equals(Environment.StatusFail) && item.getTask().getTaskId().equals("")) {
                    existAllTask = false;
                    break;
                }
            }

            updateOnlyFailed(onlyfailed.isChecked());

            pd.hide();
        }
    }

    @Override
    public void onRootClick(int position) {
        /*
        Intent intent = new Intent(AssignActivity.this, WorkplaceInspectionItemActivity.class);
        WorkplaceInspectionItemData workplaceInspectionItemData = datas.get(position);
        intent.putExtra("readonly", true);
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
        */
    }

    @Override
    public void onFilesClick(int position) {
        Intent intent = new Intent(AssignActivity.this, TaskActivity.class);
        WorkplaceInspectionItemData workplaceInspectionItemData = datas.get(position);
        intent.putExtra("readonly", false);
        intent.putExtra("idloc", workplaceInspectionItemData.getId());
        intent.putExtra("parentId", linkId);

        if (!workplaceInspectionItemData.getTask().getTaskId().equals("")) {
            TaskData taskData = workplaceInspectionItemData.getTask();
            intent.putExtra("taskId", taskData.getTaskId());
            intent.putExtra("taskTypeId", taskData.getTaskTypeId());
            intent.putExtra("percentComplete", taskData.getPercentComplete());
            intent.putExtra("name", taskData.getName());
            intent.putExtra("description", taskData.getDescription());
            intent.putExtra("categoryId", taskData.getTaskCategoryId());
            intent.putExtra("priorityId", taskData.getPriority());
            intent.putExtra("date", taskData.getPlannedStartDate());
            intent.putExtra("dateend", taskData.getPlannedFinishDate());

            UserInfo userInfo = UserInfo.getUserInfo();

            ArrayList<ProfileData> profileDatas = new ArrayList<ProfileData>();

            for (ProfileData item: userInfo.getAllAssignedProfiles()) {
                profileDatas.add(new ProfileData(item.getProfileId(), item.getName(), item.getPresence()));
            }

            for (ProfileData item: profileDatas) {
                for (ProfileData item2: taskData.getAssignedProfiles()) {
                    if (item2.getProfileId().equals(item.getProfileId())) {
                        item.setPresence(true);
                        break;
                    }
                }
            }

            userInfo.setAssignedProfiles(profileDatas);
        }
        startActivity(intent);
    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Sapphire.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
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

    @Override
    protected void onResume() {
        super.onResume();

        if (!workplaceInspectionId.equals("")) {
            pd.show();
            new GetWorkplaceInspectionAction(AssignActivity.this, workplaceInspectionId).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
