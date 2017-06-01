package com.dealerpilothr.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.MenuFragment;
import com.dealerpilothr.models.NavigationMenuData;
import com.dealerpilothr.R;

import java.util.ArrayList;

public class MenuAdapter extends BaseExpandableListAdapter {

    public interface OnRootClickListener{
        void onRootClick(int groupPosition, int childPosition);
    }

    private ArrayList<NavigationMenuData> mGroups;
    private MenuFragment mContext;
    private Typeface typeFace;
    private boolean isOpenGroup = false;

    public MenuAdapter(MenuFragment context, ArrayList<NavigationMenuData> groups) {
        mContext = context;
        mGroups = groups;
        typeFace = Typeface.createFromAsset(Dealerpilothr.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).getSubMenus().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).getSubMenus().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view_white, null);
        }

        ImageView ico = (ImageView) convertView.findViewById(R.id.ico);

        if (isExpanded){
            //Изменяем что-нибудь, если текущая Group раскрыта
            ico.setImageResource(R.drawable.ico_up);
            if (mGroups.get(groupPosition).getSubMenus().size() == 0) {
                if (!isOpenGroup) {
                    isOpenGroup = true;
                    ((OnRootClickListener) mContext).onRootClick(groupPosition, 0);
                }
            }
        } else{
            //Изменяем что-нибудь, если текущая Group скрыта
            ico.setImageResource(R.drawable.ico_down);
        }

        if (mGroups.get(groupPosition).getSubMenus().size() == 0) {
            ico.setVisibility(View.GONE);
        } else {
            ico.setVisibility(View.VISIBLE);
        }

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(mGroups.get(groupPosition).getName());

        /*
        if (mGroups.get(groupPosition).getSubMenus().size() == 0) {
            View root = convertView.findViewById(R.id.root);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnRootClickListener) mContext).onRootClick(groupPosition, 0);
                }
            });
        }
        */

        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_view, null);
        }

        NavigationMenuData navigationMenuData = mGroups.get(groupPosition).getSubMenus().get(childPosition);

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(navigationMenuData.getName());

        TextView ico = (TextView) convertView.findViewById(R.id.ico);
        if (navigationMenuData.getUnicodeIcon().equals("")) {
            ico.setVisibility(View.GONE);
        } else {
            ico.setVisibility(View.VISIBLE);
            ico.setTypeface(typeFace);
            try {
                ico.setText(Html.fromHtml("&#"+Integer.parseInt(navigationMenuData.getUnicodeIcon(), 16)+";"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        View root = convertView.findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnRootClickListener)mContext).onRootClick(groupPosition, childPosition);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}