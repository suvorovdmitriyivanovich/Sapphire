package com.sapphire.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.models.AttendanceData;
import java.util.ArrayList;

public class AttendancesAdapter extends RecyclerView.Adapter<AttendancesAdapter.ViewHolder> {

    public interface OnRootAttendancesClickListener{
        void onRootAttendancesClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_date;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_date = (TextView) itemView.findViewById(R.id.text_description);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<AttendanceData> mData;
    private Context mContext;

    public AttendancesAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<AttendanceData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_text, parent, false);
        return new AttendancesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        AttendanceData data = mData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getAccountName());
        holder.text_date.setText(Html.fromHtml(data.getAttendanceCode() + "<br>" + data.getValueString()));

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootAttendancesClickListener) {
                    ((OnRootAttendancesClickListener) mContext).onRootAttendancesClick(holder.getAdapterPosition());
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

    public void setData(ArrayList<AttendanceData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public AttendanceData getDataItem(int position) {
        return mData.get(position);
    }
}