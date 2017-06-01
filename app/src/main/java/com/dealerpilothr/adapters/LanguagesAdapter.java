package com.dealerpilothr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.RightFragment;
import com.dealerpilothr.models.LanguageData;
import java.util.ArrayList;

public class LanguagesAdapter extends BaseAdapter {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    private ArrayList<LanguageData> listData;
    private RightFragment context;
    private static LayoutInflater inflater = null;

    public LanguagesAdapter(RightFragment context, ArrayList<LanguageData> list) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listData = list;
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
        final LanguageData data = listData.get(position);

        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.language_view, null);

        View root = view.findViewById(R.id.root);

        TextView text_name = (TextView) view.findViewById(R.id.text_name);
        ImageView language_ico = (ImageView) view.findViewById(R.id.language_ico);
        ImageView check = (ImageView) view.findViewById(R.id.check);

        if (data.getName().equals("en")) {
            text_name.setText(Dealerpilothr.getInstance().getResources().getString(R.string.english));
            language_ico.setImageResource(R.drawable.english);
        } else if (data.getName().equals("fr")) {
            text_name.setText(Dealerpilothr.getInstance().getResources().getString(R.string.french));
            language_ico.setImageResource(R.drawable.french);
        }

        if (data.getChecked()) {
            check.setVisibility(View.VISIBLE);
        } else {
            check.setVisibility(View.GONE);
        }

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChecked(data);
                ((OnRootClickListener)context).onRootClick(position);
            }
        });

        return view;
    }

    public void onClickChecked(LanguageData data) {
        for (int i = 0; i < listData.size(); i++) {
            listData.get(i).setChecked(false);
        }
        data.setChecked(true);
        notifyDataSetChanged();
    }
}
