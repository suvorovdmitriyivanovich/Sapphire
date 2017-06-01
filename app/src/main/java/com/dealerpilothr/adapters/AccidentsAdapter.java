package com.dealerpilothr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dealerpilothr.R;
import com.dealerpilothr.models.AccidentData;
import java.util.ArrayList;

public class AccidentsAdapter extends RecyclerView.Adapter<AccidentsAdapter.ViewHolder> {

    public interface OnRootAccidentsClickListener{
        void onRootAccidentsClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_description;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_description = (TextView) itemView.findViewById(R.id.text_description);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<AccidentData> mData;
    private Context mContext;

    public AccidentsAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<AccidentData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_text, parent, false);
        return new AccidentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        AccidentData data = mData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());
        holder.text_description.setText(data.getDateString());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootAccidentsClickListener) {
                    ((OnRootAccidentsClickListener) mContext).onRootAccidentsClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        else
            return 0;
    }

    public void setData(ArrayList<AccidentData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public AccidentData getDataItem(int position) {
        return mData.get(position);
    }
}