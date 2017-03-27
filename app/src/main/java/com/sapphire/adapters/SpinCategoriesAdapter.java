package com.sapphire.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.sapphire.models.CategoryData;
import java.util.ArrayList;

public class SpinCategoriesAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<CategoryData> values;
    private int textViewResourceId;

    public SpinCategoriesAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        this.values = new ArrayList<CategoryData>();
        this.textViewResourceId = textViewResourceId;
    }

    public int getCount(){
        return values.size();
    }

    public String getItem(int position){
        return values.get(position).getName();
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, textViewResourceId, null);
        TextView label = (TextView) convertView;
        label.setText(values.get(position).getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, textViewResourceId, null);
        TextView label = (TextView) convertView;
        label.setText(values.get(position).getName());
        return convertView;
    }

    public void setValues(ArrayList<CategoryData> values) {
        this.values.clear();
        this.values.addAll(values);
        this.notifyDataSetChanged();
    }
}