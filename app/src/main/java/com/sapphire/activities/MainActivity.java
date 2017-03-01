package com.sapphire.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.Sapphire;
import com.sapphire.R;
import com.sapphire.activities.workplaceInspection.WorkplaceInspectionActivity;
import com.sapphire.adapters.CoursesAdapter;
import com.sapphire.adapters.PoliciesAdapter;
import com.sapphire.adapters.WorkplaceInspectionsAdapter;
import com.sapphire.api.CoursesAction;
import com.sapphire.api.ItemPrioritiesAction;
import com.sapphire.api.ItemStatusesAction;
import com.sapphire.api.PoliciesAction;
import com.sapphire.api.TemplatesAction;
import com.sapphire.api.WorkplaceInspectionDeleteAction;
import com.sapphire.api.WorkplaceInspectionsAction;
import com.sapphire.logic.CoursesData;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ItemPriorityData;
import com.sapphire.logic.ItemStatusData;
import com.sapphire.logic.PoliciesData;
import com.sapphire.logic.TemplateData;
import com.sapphire.logic.UserInfo;
import com.sapphire.logic.WorkplaceInspectionData;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements CoursesAdapter.OnRootCoursesClickListener,
                                                          CoursesAdapter.OnOpenCoursesClickListener,
                                                          CoursesAdapter.OnListCoursesClickListener,
                                                          CoursesAction.RequestCourses,
                                                          CoursesAction.RequestCoursesData,
                                                          PoliciesAdapter.OnRootPoliciesClickListener,
                                                          PoliciesAdapter.OnOpenPoliciesClickListener,
                                                          PoliciesAction.RequestPolicies,
                                                          PoliciesAction.RequestPoliciesData,
                                                          WorkplaceInspectionsAdapter.OnRootWorkplaceInspectionsClickListener,
                                                          WorkplaceInspectionsAdapter.OnOpenWorkplaceInspectionsClickListener,
                                                          WorkplaceInspectionsAdapter.OnDeleteWorkplaceInspectionsClickListener,
                                                          WorkplaceInspectionsAdapter.OnFilesWorkplaceInspectionsClickListener,
                                                          WorkplaceInspectionsAction.RequestWorkplaceInspections,
                                                          WorkplaceInspectionsAction.RequestWorkplaceInspectionsData,
                                                          WorkplaceInspectionDeleteAction.RequestWorkplaceInspectionDelete,
                                                          ItemPrioritiesAction.RequestItemPriorities,
                                                          ItemPrioritiesAction.RequestItemPrioritiesData,
                                                          ItemStatusesAction.RequestItemStatuses,
                                                          ItemStatusesAction.RequestItemStatusesData,
                                                          TemplatesAction.RequestTemplates,
                                                          TemplatesAction.RequestTemplatesData{
    private static long back_pressed;
    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;
    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.MainActivity";
    private BroadcastReceiver br;
    private ProgressDialog pd;
    private ArrayList<CoursesData> coursesDatas = new ArrayList<CoursesData>();
    private CoursesAdapter adapterCourses;
    private ExpandableListView courseslist;
    private ArrayList<PoliciesData> policiesDatas = new ArrayList<PoliciesData>();
    private PoliciesAdapter adapterPolicies;
    private ExpandableListView policieslist;
    private ArrayList<WorkplaceInspectionData> workplaceInspectionDatas = new ArrayList<WorkplaceInspectionData>();
    private WorkplaceInspectionsAdapter adapterWorkplaceInspections;
    private RecyclerView workplaceinspectionslist;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;
    private View text_courses_no;
    private View text_policies_no;
    private View text_workplace_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sPref = getSharedPreferences("GlobalPreferences", MODE_PRIVATE);
        ed = sPref.edit();

        setContentView(R.layout.activity_main);

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

        courseslist = (ExpandableListView) findViewById(R.id.courseslist);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        courseslist.setIndicatorBounds((width - GetPixelFromDips(80)), (width - GetPixelFromDips(50)));

        adapterCourses = new CoursesAdapter(this);
        courseslist.setAdapter(adapterCourses);

        policieslist = (ExpandableListView) findViewById(R.id.policieslist);

        policieslist.setIndicatorBounds((width - GetPixelFromDips(80)), (width - GetPixelFromDips(50)));

        adapterPolicies = new PoliciesAdapter(this);
        policieslist.setAdapter(adapterPolicies);

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

                new WorkplaceInspectionDeleteAction(MainActivity.this, workplaceInspectionDatas.get(currentPosition).getWorkplaceInspectionId()).execute();
            }
        });

        workplaceinspectionslist = (RecyclerView) findViewById(R.id.workplaceinspectionslist);
        workplaceinspectionslist.setNestedScrollingEnabled(false);
        workplaceinspectionslist.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        adapterWorkplaceInspections = new WorkplaceInspectionsAdapter(this, true);
        workplaceinspectionslist.setAdapter(adapterWorkplaceInspections);

        text_courses_no = findViewById(R.id.text_courses_no);
        text_policies_no = findViewById(R.id.text_policies_no);
        text_workplace_no = findViewById(R.id.text_workplace_no);


    }

    public void updateVisibility() {
        if (coursesDatas.size() == 0) {
            text_courses_no.setVisibility(View.VISIBLE);
            courseslist.setVisibility(View.GONE);
        } else {
            courseslist.setVisibility(View.VISIBLE);
            text_courses_no.setVisibility(View.GONE);
        }
        if (policiesDatas.size() == 0) {
            text_policies_no.setVisibility(View.VISIBLE);
            policieslist.setVisibility(View.GONE);
        } else {
            policieslist.setVisibility(View.VISIBLE);
            text_policies_no.setVisibility(View.GONE);
        }
        if (workplaceInspectionDatas.size() == 0) {
            text_workplace_no.setVisibility(View.VISIBLE);
            workplaceinspectionslist.setVisibility(View.GONE);
        } else {
            workplaceinspectionslist.setVisibility(View.VISIBLE);
            text_workplace_no.setVisibility(View.GONE);
        }
    }

    // Метод установки размера списка по высоте относительно экрана, нужен для правильного отображения списков
    public void justifyListViewHeightBasedOnChildrenCourses(ExpandableListView listView) {
        if (adapterCourses == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapterCourses.getGroupCount(); i++) {
            View listItem = adapterCourses.getGroupView(i, false, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 10;
            for (int y = 0; y < adapterCourses.getChildrenCount(i); y++) {
                View childItem = adapterCourses.getChildView(i, y, false, null, listView);
                childItem.measure(0, 0);
                totalHeight += childItem.getMeasuredHeight() + 10;
            }
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        //par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        par.height = totalHeight;

        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    // Метод установки размера списка по высоте относительно экрана, нужен для правильного отображения списков
    public void justifyListViewHeightBasedOnChildrenPolicies(ExpandableListView listView) {
        if (adapterPolicies == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapterPolicies.getGroupCount(); i++) {
            View listItem = adapterPolicies.getGroupView(i, false, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 10;
            for (int y = 0; y < adapterPolicies.getChildrenCount(i); y++) {
                View childItem = adapterPolicies.getChildView(i, y, false, null, listView);
                childItem.measure(0, 0);
                totalHeight += childItem.getMeasuredHeight() + 10;
            }
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        //par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        par.height = totalHeight;

        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    @Override
    public void onRootCoursesClick(int groupPosition, int childPosition) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenCoursesClick(int groupPosition, int childPosition) {
        Intent intent = new Intent(MainActivity.this, CourseActivity.class);
        CoursesData coursesData = coursesDatas.get(groupPosition).getSubCourses().get(childPosition);
        intent.putExtra("name", coursesData.getName());
        intent.putExtra("courseId", coursesData.getCourseFileId());
        /*
        intent.putExtra("acknowledged", coursesData.getIsAcknowledged());
        intent.putExtra("duration", (coursesData.getDuration().getHours() * 60 * 60) + (coursesData.getDuration().getMinutes() * 60) + coursesData.getDuration().getSeconds());
        intent.putExtra("id", coursesData.getId());
        */
        startActivity(intent);
    }

    @Override
    public void onListCoursesClick(int groupPosition, int childPosition) {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        CoursesData coursesData = coursesDatas.get(groupPosition).getSubCourses().get(childPosition);
        intent.putExtra("name", coursesData.getName());
        intent.putExtra("quizeId", coursesData.getQuizId());
        intent.putExtra("duration", coursesData.getDuration());
        startActivity(intent);
    }

    @Override
    public void onRequestCourses(String result) {
        updateVisibility();
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestCoursesData(ArrayList<CoursesData> coursesDatas) {
        this.coursesDatas = coursesDatas;
        adapterCourses.setData(coursesDatas);

        //if (current == 1) {
        for (int i = 0; i < coursesDatas.size(); i++) {
            courseslist.expandGroup(i);
        }
        //} else {
        //    for (int i = 0; i < coursesDatas.size(); i++) {
        //        courseslist.collapseGroup(i);
        //    }
        //}

        updateVisibility();

        justifyListViewHeightBasedOnChildrenCourses(courseslist);

        //pd.hide();
        new PoliciesAction(MainActivity.this, true).execute();
    }

    @Override
    public void onRootPoliciesClick(int groupPosition, int childPosition) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenPoliciesClick(int groupPosition, int childPosition) {
        Intent intent = new Intent(MainActivity.this, PdfActivity.class);
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
        adapterPolicies.setData(policiesDatas);

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

        justifyListViewHeightBasedOnChildrenPolicies(policieslist);

        //pd.hide();
        new WorkplaceInspectionsAction(MainActivity.this).execute();
    }

    @Override
    public void onRootWorkplaceInspectionsClick(int position) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenWorkplaceInspectionsClick(int position) {
        Intent intent = new Intent(MainActivity.this, WorkplaceInspectionActivity.class);
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
        Intent intent = new Intent(MainActivity.this, FilesActivity.class);
        WorkplaceInspectionData workplaceInspectionData = workplaceInspectionDatas.get(position);
        intent.putExtra("name", workplaceInspectionData.getName());
        intent.putExtra("id", workplaceInspectionData.getWorkplaceInspectionId());
        intent.putExtra("url", Environment.WorkplaceInspectionsFilesURL);
        intent.putExtra("nameField", "WorkplaceInspectionId");
        //TODO временно
        //ArrayList<FileData> fileDatas = new ArrayList<FileData>();
        //fileDatas.add(new FileData("3d5fdd39-9c53-7c30-cddb-d8cbb6a1d14e"));
        //fileDatas.add(new FileData("3d5fdd39-e859-716a-9f6f-52029ada550c"));
        UserInfo.getUserInfo().setFileDatas(workplaceInspectionData.getFiles());
        startActivity(intent);
    }

    @Override
    public void onRequestWorkplaceInspections(String result) {
        updateVisibility();
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
            new WorkplaceInspectionsAction(MainActivity.this).execute();
        }
    }

    @Override
    public void onRequestWorkplaceInspectionsData(ArrayList<WorkplaceInspectionData> workplaceInspectionDatas) {
        this.workplaceInspectionDatas = workplaceInspectionDatas;
        adapterWorkplaceInspections.setData(workplaceInspectionDatas);

        updateVisibility();

        new TemplatesAction(MainActivity.this).execute();

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

        new ItemPrioritiesAction(MainActivity.this).execute();

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

        new ItemStatusesAction(MainActivity.this).execute();

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
        new CoursesAction(MainActivity.this, true).execute();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();

            Sapphire.exit(this);
        } else
            Toast.makeText(getBaseContext(), R.string.text_again_exit,
                    Toast.LENGTH_LONG).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
