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
import com.sapphire.adapters.CoursesAdapter;
import com.sapphire.api.CoursesAction;
import com.sapphire.logic.CoursesData;
import java.util.ArrayList;

public class CoursesActivity extends AppCompatActivity implements CoursesAdapter.OnRootClickListener,
                                                                  CoursesAdapter.OnOpenClickListener,
                                                                  CoursesAdapter.OnListClickListener,
                                                                  CoursesAction.RequestCourses,
                                                                  CoursesAction.RequestCoursesData {
    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.CoursesActivity";
    BroadcastReceiver br;
    private ArrayList<CoursesData> coursesDatas;
    private CoursesAdapter adapter;
    ProgressDialog pd;
    private ExpandableListView courseslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_courses);

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

        courseslist = (ExpandableListView) findViewById(R.id.courseslist);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        courseslist.setIndicatorBounds((width - GetPixelFromDips(50)), (width - GetPixelFromDips(20)));

        adapter = new CoursesAdapter(this);
        courseslist.setAdapter(adapter);
    }

    @Override
    public void onRootClick(int groupPosition, int childPosition) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenClick(int groupPosition, int childPosition) {
        Intent intent = new Intent(CoursesActivity.this, CourseActivity.class);
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
    public void onListClick(int groupPosition, int childPosition) {
        Intent intent = new Intent(CoursesActivity.this, QuizActivity.class);
        CoursesData coursesData = coursesDatas.get(groupPosition).getSubCourses().get(childPosition);
        intent.putExtra("name", coursesData.getName());
        intent.putExtra("quizeId", coursesData.getQuizId());
        /*
        intent.putExtra("acknowledged", coursesData.getIsAcknowledged());
        intent.putExtra("duration", (coursesData.getDuration().getHours() * 60 * 60) + (coursesData.getDuration().getMinutes() * 60) + coursesData.getDuration().getSeconds());
        intent.putExtra("id", coursesData.getId());
        */
        startActivity(intent);
    }

    @Override
    public void onRequestCourses(String result) {
        pd.hide();
    }

    @Override
    public void onRequestCoursesData(ArrayList<CoursesData> coursesDatas) {
        this.coursesDatas = coursesDatas;
        adapter.setData(coursesDatas);

        //if (current == 1) {
            for (int i = 0; i < coursesDatas.size(); i++) {
                courseslist.expandGroup(i);
            }
        //} else {
        //    for (int i = 0; i < coursesDatas.size(); i++) {
        //        courseslist.collapseGroup(i);
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
        new CoursesAction(CoursesActivity.this).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
