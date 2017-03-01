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
import com.sapphire.adapters.CoursesAdapter;
import com.sapphire.api.CoursesAction;
import com.sapphire.logic.CoursesData;
import java.util.ArrayList;

public class CoursesActivity extends BaseActivity implements CoursesAdapter.OnRootCoursesClickListener,
                                                                  CoursesAdapter.OnOpenCoursesClickListener,
                                                                  CoursesAdapter.OnListCoursesClickListener,
                                                                  CoursesAction.RequestCourses,
                                                                  CoursesAction.RequestCoursesData {
    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.CoursesActivity";
    private BroadcastReceiver br;
    private ArrayList<CoursesData> coursesDatas;
    private CoursesAdapter adapter;
    private ProgressDialog pd;
    private ExpandableListView courseslist;
    private View text_courses_no;

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
        courseslist.setIndicatorBounds((width - GetPixelFromDips(50)), (width - GetPixelFromDips(20)));

        adapter = new CoursesAdapter(this);
        courseslist.setAdapter(adapter);

        text_courses_no = findViewById(R.id.text_courses_no);
    }

    public void updateVisibility() {
        if (coursesDatas.size() == 0) {
            text_courses_no.setVisibility(View.VISIBLE);
            courseslist.setVisibility(View.GONE);
        } else {
            courseslist.setVisibility(View.VISIBLE);
            text_courses_no.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRootCoursesClick(int groupPosition, int childPosition) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenCoursesClick(int groupPosition, int childPosition) {
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
    public void onListCoursesClick(int groupPosition, int childPosition) {
        Intent intent = new Intent(CoursesActivity.this, QuizActivity.class);
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
        new CoursesAction(CoursesActivity.this, false).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
