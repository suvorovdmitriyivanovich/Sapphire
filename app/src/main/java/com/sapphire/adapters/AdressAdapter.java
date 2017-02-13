package com.sapphire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.logic.AdressData;
import java.util.ArrayList;

public class AdressAdapter extends BaseAdapter {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    private ArrayList<AdressData> listData;
    private Context context;
    private static LayoutInflater inflater = null;

    public AdressAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listData = new ArrayList<AdressData>();
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
        final AdressData data = listData.get(position);

        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.adress_view, null);

        View root = view.findViewById(R.id.root);

        TextView text_name = (TextView) view.findViewById(R.id.text_name);
        text_name.setText(data.getName());

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChecked(data);
                ((OnRootClickListener)context).onRootClick(position);
            }
        });

        return view;
    }

    public void onClickChecked(AdressData data) {
        notifyDataSetChanged();
    }

    public void setListArray(ArrayList<AdressData> list){
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }
}
