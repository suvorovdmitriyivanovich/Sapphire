package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.CourseData;
import com.sapphire.logic.CoursesData;
import java.util.ArrayList;

public class CoursesAdapter extends BaseExpandableListAdapter {

    public interface OnRootClickListener{
        void onRootClick(int groupPosition, int childPosition);
    }

    public interface OnOpenClickListener{
        void onOpenClick(int groupPosition, int childPosition);
    }

    public interface OnListClickListener{
        void onListClick(int groupPosition, int childPosition);
    }

    private ArrayList<CoursesData> mGroups;
    private Context mContext;
    private Typeface typeFace;

    public CoursesAdapter(Context context) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mGroups = new ArrayList<CoursesData>();
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).getSubCourses().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).getSubCourses().get(childPosition);
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
            convertView = inflater.inflate(R.layout.child_courses_view, null);
        }

        CoursesData coursesData = mGroups.get(groupPosition).getSubCourses().get(childPosition);

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(coursesData.getName());

        String textCourse = Sapphire.getInstance().getResources().getString(R.string.text_duration);
        textCourse = textCourse + ": " + coursesData.getDuration();
        textCourse = textCourse + ", " + Sapphire.getInstance().getResources().getString(R.string.text_quiz_score);
        textCourse = textCourse + ": " + coursesData.getQuizScore();
        textCourse = textCourse + ", " + Sapphire.getInstance().getResources().getString(R.string.text_quiz_completed_on);
        textCourse = textCourse + ": " + coursesData.getQuizDateCompletedString();

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

        Button list = (Button) convertView.findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnListClickListener)mContext).onListClick(groupPosition, childPosition);
            }
        });

        list.setEnabled(coursesData.getQuizEnabled());

        open.setTypeface(typeFace);
        open.setText(Html.fromHtml("&#61515;"));

        list.setTypeface(typeFace);
        list.setText(Html.fromHtml("&#61498;"));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setData(ArrayList<CoursesData> groups) {
        mGroups.clear();
        mGroups.addAll(groups);
        notifyDataSetChanged();
    }
}