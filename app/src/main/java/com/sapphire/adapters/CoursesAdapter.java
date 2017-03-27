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
import com.sapphire.models.CoursesData;
import java.util.ArrayList;

public class CoursesAdapter extends BaseExpandableListAdapter {

    public interface OnRootCoursesClickListener{
        void onRootCoursesClick(int groupPosition, int childPosition);
    }

    public interface OnOpenCoursesClickListener{
        void onOpenCoursesClick(int groupPosition, int childPosition);
    }

    public interface OnListCoursesClickListener{
        void onListCoursesClick(int groupPosition, int childPosition);
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

        View border = convertView.findViewById(R.id.border);
        if (childPosition < mGroups.get(groupPosition).getSubCourses().size()-1) {
            border.setVisibility(View.VISIBLE);
        } else {
            border.setVisibility(View.GONE);
        }

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(coursesData.getName());

        String textCourse = "";
        if (!coursesData.getQuizScore().equals("")) {
            textCourse = Sapphire.getInstance().getResources().getString(R.string.text_quiz_score);
            textCourse = textCourse + ": " + coursesData.getQuizScore();
        }
        if (!textCourse.equals("")) {
            textCourse = textCourse + "<br>";
        }
        textCourse = textCourse + Sapphire.getInstance().getResources().getString(R.string.text_course);
        textCourse = textCourse + ": ";
        if (coursesData.getCoursePassed()) {
            textCourse = textCourse + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            textCourse = textCourse + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }
        textCourse = textCourse + "<br>" + Sapphire.getInstance().getResources().getString(R.string.text_quiz);
        textCourse = textCourse + ": ";
        if (coursesData.getQuizPassed()) {
            textCourse = textCourse + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            textCourse = textCourse + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }

        TextView description = (TextView) convertView.findViewById(R.id.text_description);
        description.setTypeface(typeFace);
        description.setText(Html.fromHtml(textCourse));

        View root = convertView.findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnRootCoursesClickListener)mContext).onRootCoursesClick(groupPosition, childPosition);
            }
        });

        Button open = (Button) convertView.findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnOpenCoursesClickListener)mContext).onOpenCoursesClick(groupPosition, childPosition);
            }
        });

        Button list = (Button) convertView.findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnListCoursesClickListener)mContext).onListCoursesClick(groupPosition, childPosition);
            }
        });

        list.setEnabled(coursesData.getQuizEnabled());

        open.setTypeface(typeFace);
        open.setText(Html.fromHtml("&#"+Environment.IcoPlay+";"));

        list.setTypeface(typeFace);
        list.setText(Html.fromHtml("&#"+Environment.IcoList+";"));

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