package com.sapphire.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
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

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels-(dm.widthPixels/100*15);
        //int width = dm.widthPixels;

        try {
            menulist.setIndicatorBounds((width - GetPixelFromDips(60)), (width - GetPixelFromDips(30)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new MenuAdapter(this, navigationMenuDatas);
        menulist.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        int countmessages = DBHelper.getInstance(Sapphire.getInstance()).getMessages(0,-1).size();
        if (countmessages != 0) {
            View round_message = rootView.findViewById(R.id.round_message);
            round_message.setVisibility(View.VISIBLE);

            TextView count_messages = (TextView) rootView.findViewById(R.id.count_messages);
            if (countmessages > 99) {
                count_messages.setText("99+");
            } else {
                count_messages.setText(String.valueOf(countmessages));
            }
        }
        */
    }

    // Метод установки размера списка по высоте относительно экрана, нужен для правильного отображения списков
    public void justifyListViewHeightBasedOnChildren(ExpandableListView listView) {
        //MenuAdapter adapter = (MenuAdapter) listView.getAdapter();

        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            View listItem = adapter.getGroupView(i, false, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            for (int y = 0; y < adapter.getChildrenCount(i); y++) {
                View childItem = adapter.getChildView(i, y, false, null, listView);
                childItem.measure(0, 0);
                totalHeight += childItem.getMeasuredHeight();
            }
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        //par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        par.height = totalHeight;

        listView.setLayoutParams(par);
        listView.requestLayout();
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
        String urlRoute = navigationMenuDatas.get(groupPosition).getSubMenus().get(childPosition).getUrlRoute();
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
        } else if (urlRoute.equals("/me/my-time-off-requests")) {
            Intent intent = new Intent(getActivity(), TimeOffRequestsActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-time-banks")) {
            Intent intent = new Intent(getActivity(), TimeBanksActivity.class);
            startActivity(intent);
        } else if (urlRoute.equals("/me/my-attendance")) {
            Intent intent = new Intent(getActivity(), AttendancesActivity.class);
            startActivity(intent);
        }
    }
}
