package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.PolicyData;
import java.util.ArrayList;

public class PoliciesAdapter extends BaseExpandableListAdapter {

    public interface OnGroupPoliciesClickListener{
        void onGroupPoliciesClick(int groupPosition, boolean isExpanded);
    }

    public interface OnRootPoliciesClickListener{
        void onRootPoliciesClick(int groupPosition, int childPosition);
    }

    public interface OnOpenPoliciesClickListener{
        void onOpenPoliciesClick(int groupPosition, int childPosition);
    }

    private ArrayList<PolicyData> mGroups;
    private Context mContext;
    private Typeface typeFace;
    private boolean isDashboard;

    public PoliciesAdapter(Context context, boolean isDashboard) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mGroups = new ArrayList<PolicyData>();
        this.isDashboard = isDashboard;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).getSubPolicies().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).getSubPolicies().get(childPosition);
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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (isDashboard) {
                convertView = inflater.inflate(R.layout.group_view_fiksed_two, null);
            } else {
                convertView = inflater.inflate(R.layout.group_view_two, null);
            }
        }

        if (isExpanded){
            //Изменяем что-нибудь, если текущая Group раскрыта
        }
        else{
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(mGroups.get(groupPosition).getName());

        String textAcknowledged = "";
        textAcknowledged = textAcknowledged + Sapphire.getInstance().getResources().getString(R.string.text_acknowledge);
        textAcknowledged = textAcknowledged + ": ";

        boolean allAcknowledged = true;
        for(PolicyData item: mGroups.get(groupPosition).getSubPolicies()) {
            if (!item.getIsAcknowledged()) {
                allAcknowledged = false;
                break;
            }
        }

        if (allAcknowledged) {
            textAcknowledged = textAcknowledged + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            textAcknowledged = textAcknowledged + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }

        TextView description = (TextView) convertView.findViewById(R.id.text_description);
        description.setTypeface(typeFace);
        description.setText(Html.fromHtml(textAcknowledged));

        if (isDashboard) {
            View root = convertView.findViewById(R.id.root);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnGroupPoliciesClickListener) mContext).onGroupPoliciesClick(groupPosition, isExpanded);
                }
            });
        }

        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (isDashboard) {
                convertView = inflater.inflate(R.layout.child_policies_view_fiksed, null);
            } else {
                convertView = inflater.inflate(R.layout.child_policies_view, null);
            }
        }

        PolicyData data = mGroups.get(groupPosition).getSubPolicies().get(childPosition);

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(data.getName());

        String textAcknowledged = "";
        textAcknowledged = textAcknowledged + Sapphire.getInstance().getResources().getString(R.string.text_acknowledge);
        textAcknowledged = textAcknowledged + ": ";
        if (data.getIsAcknowledged()) {
            textAcknowledged = textAcknowledged + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            textAcknowledged = textAcknowledged + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }

        TextView description = (TextView) convertView.findViewById(R.id.text_description);
        description.setTypeface(typeFace);
        description.setText(Html.fromHtml(textAcknowledged));

        View border = convertView.findViewById(R.id.border);
        if (childPosition < mGroups.get(groupPosition).getSubPolicies().size()-1) {
            border.setVisibility(View.VISIBLE);
        } else {
            border.setVisibility(View.GONE);
        }

        View root = convertView.findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnRootPoliciesClickListener)mContext).onRootPoliciesClick(groupPosition, childPosition);
            }
        });

        Button open = (Button) convertView.findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnOpenPoliciesClickListener)mContext).onOpenPoliciesClick(groupPosition, childPosition);
            }
        });

        open.setTypeface(typeFace);
        open.setText(Html.fromHtml("&#"+Environment.IcoOpen+";"));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setData(ArrayList<PolicyData> groups) {
        mGroups.clear();
        mGroups.addAll(groups);
        notifyDataSetChanged();
    }
}