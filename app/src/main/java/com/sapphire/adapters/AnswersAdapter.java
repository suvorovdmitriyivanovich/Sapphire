package com.sapphire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.models.AnswerData;
import java.util.ArrayList;

public class AnswersAdapter extends BaseAdapter {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    private ArrayList<AnswerData> listData;
    private Context context;
    private static LayoutInflater inflater = null;

    public AnswersAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listData = new ArrayList<AnswerData>();
    }

    @Override
    public int getCount() {
        if(listData != null) {
            return listData.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AnswerData data = listData.get(position);

        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.answer_view, null);

        View root = view.findViewById(R.id.root);

        TextView text_name = (TextView) view.findViewById(R.id.text_name);
        text_name.setText(data.getName());

        final CheckBox check = (CheckBox) view.findViewById(R.id.check);
        final RadioButton radio = (RadioButton) view.findViewById(R.id.radio);

        if (data.getCategory().getName().equals("single answer")) {
            check.setVisibility(View.GONE);
            radio.setVisibility(View.VISIBLE);
            radio.setChecked(data.getChecked());
        } else {
            radio.setVisibility(View.GONE);
            check.setVisibility(View.VISIBLE);
            check.setChecked(data.getChecked());
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChecked(data);
                ((OnRootClickListener)context).onRootClick(position);
            }
        });

        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChecked(data);
                ((OnRootClickListener)context).onRootClick(position);
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChecked(data);
                ((OnRootClickListener)context).onRootClick(position);
            }
        });

        return view;
    }

    public void onClickChecked(AnswerData data) {
        if (data.getCategory().getName().equals("single answer")) {
            for (int i = 0; i < listData.size(); i++) {
                listData.get(i).setChecked(false);
            }
            data.setChecked(true);
        } else {
            data.setChecked(!data.getChecked());
        }
        notifyDataSetChanged();
    }

    public void setListArray(ArrayList<AnswerData> list){
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }
}
