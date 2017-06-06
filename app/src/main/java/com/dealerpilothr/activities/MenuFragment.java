package com.dealerpilothr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.dealerpilothr.activities.document.DocumentsActivity;
import com.dealerpilothr.activities.performance.PerformancesActivity;
import com.dealerpilothr.activities.workplaceInspection.WorkplaceInspectionsActivity;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.attendance.AttendancesActivity;
import com.dealerpilothr.activities.course.CoursesActivity;
import com.dealerpilothr.activities.discipline.DisciplinesActivity;
import com.dealerpilothr.activities.hrdetails.ProfileActivity;
import com.dealerpilothr.activities.investigation.InvestigationsActivity;
import com.dealerpilothr.activities.meeting.MeetingsActivity;
import com.dealerpilothr.activities.policy.PoliciesActivity;
import com.dealerpilothr.activities.safety.SafetisActivity;
import com.dealerpilothr.activities.template.TemplatesActivity;
import com.dealerpilothr.activities.timeOffRequest.TimeOffRequestsActivity;
import com.dealerpilothr.adapters.MenuAdapter;
import com.dealerpilothr.db.DBHelper;
import com.dealerpilothr.models.NavigationMenuData;
import java.util.ArrayList;

public class MenuFragment extends Fragment implements MenuAdapter.OnRootClickListener {
    private View rootView;
    private DrawerLayout drawerLayout;
    private ArrayList<NavigationMenuData> navigationMenuDatas;
    private MenuAdapter adapter;

    public MenuFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.left_menu, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);

        navigationMenuDatas = DBHelper.getInstance(Dealerpilothr.getInstance()).getNavigationMenus();
        final ExpandableListView menulist = (ExpandableListView) rootView.findViewById(R.id.menulist);

        /*
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels-(dm.widthPixels/100*15);
        //int width = dm.widthPixels;

        try {
            menulist.setIndicatorBounds((width - GetPixelFromDips(60)), (width - GetPixelFromDips(30)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        adapter = new MenuAdapter(this, navigationMenuDatas);
        menulist.setAdapter(adapter);

        View header_root = rootView.findViewById(R.id.header_root);
        header_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                if (getActivity().getClass() != MainActivity.class) {
                    getActivity().finish();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f); //123
    }

    @Override
    public void onRootClick(int groupPosition, int childPosition) {
        drawerLayout.closeDrawers();
        if (getActivity().getClass() != MainActivity.class) {
            getActivity().finish();
        }
        String urlRoute = "";
        if (navigationMenuDatas.get(groupPosition).getSubMenus().size() == 0) {
            urlRoute = navigationMenuDatas.get(groupPosition).getUrlRoute();
        } else {
            urlRoute = navigationMenuDatas.get(groupPosition).getSubMenus().get(childPosition).getUrlRoute();
        }
        if (urlRoute.equals("/me/my-policies-and-handbooks")) {
            Intent intent = new Intent(getActivity(), PoliciesActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-training-courses")) {
            Intent intent = new Intent(getActivity(), CoursesActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/configuration/health-and-safety")) {
            Intent intent = new Intent(getActivity(), TemplatesActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-calendar")) {
            Intent intent = new Intent(getActivity(), EventCalendarActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-hr-details")) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/health-and-safety/workplace-inspections-reports")) {
            Intent intent = new Intent(getActivity(), WorkplaceInspectionsActivity.class);
            intent.putExtra("urlroute", urlRoute);
            startActivity(intent);
        } else if (urlRoute.equals("/health-and-safety/investigations")) {
            Intent intent = new Intent(getActivity(), InvestigationsActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-investigations")) {
            Intent intent = new Intent(getActivity(), InvestigationsActivity.class);
            intent.putExtra("me", true);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-contacts")) {
            Intent intent = new Intent(getActivity(), MyContactsActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/health-and-safety/members")) {
            Intent intent = new Intent(getActivity(), MembersActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/health-and-safety/meetings")) {
            Intent intent = new Intent(getActivity(), MeetingsActivity.class);
            intent.putExtra("urlroute", urlRoute);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-performance")) {
            Intent intent = new Intent(getActivity(), PerformancesActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-discipline")) {
            Intent intent = new Intent(getActivity(), DisciplinesActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-documents")) {
            Intent intent = new Intent(getActivity(), DocumentsActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-timeoff-requests")) {
            Intent intent = new Intent(getActivity(), TimeOffRequestsActivity.class);
            intent.putExtra("urlroute", urlRoute);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-time-banks")) {
            Intent intent = new Intent(getActivity(), TimeBanksActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-attendance")) {
            Intent intent = new Intent(getActivity(), AttendancesActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/bulletin-board")) {
            Intent intent = new Intent(getActivity(), BulletinActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-accidents")) {
            Intent intent = new Intent(getActivity(), AccidentsActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/health-and-safety/safety-data-sheets")) {
            Intent intent = new Intent(getActivity(), SafetisActivity.class);
            intent.putExtra("urlroute", urlRoute);
            startActivity(intent);
        }
    }
}