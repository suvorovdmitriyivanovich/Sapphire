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
import com.sapphire.logic.TemplateData;
import java.util.ArrayList;

public class TemplatesAdapter extends BaseExpandableListAdapter {

    public interface OnRootClickListener{
        void onRootClick(int groupPosition, int childPosition);
    }

    public interface OnOpenClickListener{
        void onOpenClick(int groupPosition, int childPosition);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(int groupPosition, int childPosition);
    }

    private ArrayList<TemplateData> mGroups;
    private Context mContext;
    private Typeface typeFace;

    public TemplatesAdapter(Context context) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mGroups = new ArrayList<TemplateData>();
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).getSubTemplates().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).getSubTemplates().get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view, null);
        }

        if (isExpanded){
            //Изменяем что-нибудь, если текущая Group раскрыта
        }
        else{
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(mGroups.get(groupPosition).getName());

        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_templates_view, null);
        }

        TemplateData templateData = mGroups.get(groupPosition).getSubTemplates().get(childPosition);

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(templateData.getName());

        String textCourse = Sapphire.getInstance().getResources().getString(R.string.text_duration);
        textCourse = textCourse + ": " + templateData.getDescription();

        TextView course = (TextView) convertView.findViewById(R.id.text_course);
        course.setText(textCourse);

        View root = convertView.findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnRootClickListener)mContext).onRootClick(groupPosition, childPosition);
            }
        });

        Button open = (Button) convertView.findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnOpenClickListener)mContext).onOpenClick(groupPosition, childPosition);
            }
        });

        Button delete = (Button) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnDeleteClickListener)mContext).onDeleteClick(groupPosition, childPosition);
            }
        });

        open.setTypeface(typeFace);
        open.setText(Html.fromHtml("&#61504;"));

        delete.setTypeface(typeFace);
        delete.setText(Html.fromHtml("&#61944;"));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setData(ArrayList<TemplateData> groups) {
        mGroups.clear();
        mGroups.addAll(groups);
        notifyDataSetChanged();
    }
}