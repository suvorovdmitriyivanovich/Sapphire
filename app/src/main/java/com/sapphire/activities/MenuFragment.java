package com.sapphire.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.attendance.AttendancesActivity;
import com.sapphire.activities.course.CoursesActivity;
import com.sapphire.activities.discipline.DisciplinesActivity;
import com.sapphire.activities.document.DocumentsActivity;
import com.sapphire.activities.investigation.InvestigationsActivity;
import com.sapphire.activities.meeting.MeetingsActivity;
import com.sapphire.activities.performance.PerformancesActivity;
import com.sapphire.activities.policy.PoliciesActivity;
import com.sapphire.activities.safety.SafetisActivity;
import com.sapphire.activities.template.TemplatesActivity;
import com.sapphire.activities.timeOffRequest.TimeOffRequestsActivity;
import com.sapphire.activities.workplaceInspection.WorkplaceInspectionsActivity;
import com.sapphire.adapters.MenuAdapter;
import com.sapphire.db.DBHelper;
import com.sapphire.models.NavigationMenuData;
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

        navigationMenuDatas = DBHelper.getInstance(Sapphire.getInstance()).getNavigationMenus();
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
        return (int) (pixels * scale + 0.5f);
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