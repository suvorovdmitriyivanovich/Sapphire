package com.sapphire.activities.course;

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
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.MenuFragment;
import com.sapphire.activities.RightFragment;
import com.sapphire.adapters.CoursesAdapter;
import com.sapphire.api.CoursesAction;
import com.sapphire.api.PunchesAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.CoursesData;
import java.util.ArrayList;

public class CoursesActivity extends BaseActivity implements CoursesAdapter.OnRootCoursesClickListener,
                                                             CoursesAdapter.OnOpenCoursesClickListener,
                                                             CoursesAdapter.OnListCoursesClickListener,
                                                             CoursesAction.RequestCourses,
                                                             CoursesAction.RequestCoursesData,
                                                             UpdateAction.RequestUpdate,
                                                             PunchesAddAction.RequestPunchesAdd{

    private BroadcastReceiver br;
    private ArrayList<CoursesData> coursesDatas;
    private CoursesAdapter adapter;
    private ProgressDialog pd;
    private ExpandableListView courseslist;
    private View text_courses_no;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private DrawerLayout drawerLayout;

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

        courseslist = (ExpandableListView) findViewById(R.id.courseslist);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        courseslist.setIndicatorBounds((width - GetPixelFromDips(50)), (width - GetPixelFromDips(20)));

        adapter = new CoursesAdapter(this, false);
        courseslist.setAdapter(adapter);

        text_courses_no = findViewById(R.id.text_courses_no);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(CoursesActivity.this);
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
        if (coursesDatas == null || coursesDatas.size() == 0) {
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
        new CoursesAction(CoursesActivity.this, false).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
