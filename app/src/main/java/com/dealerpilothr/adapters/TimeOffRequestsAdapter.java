package com.dealerpilothr.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.TimeOffRequestData;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.UserInfo;

import java.util.ArrayList;

public class TimeOffRequestsAdapter extends RecyclerView.Adapter<TimeOffRequestsAdapter.ViewHolder> {

    public interface OnRootTimeOffRequestsClickListener{
        void onRootTimeOffRequestsClick(int position);
    }

    public interface OnOpenTimeOffRequestsClickListener{
        void onOpenTimeOffRequestsClick(int position);
    }

    public interface OnDeleteTimeOffRequestsClickListener{
        void onDeleteTimeOffRequestsClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_date;
        Button open;
        Button delete;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_date = (TextView) itemView.findViewById(R.id.text_description);
            open = (Button) itemView.findViewById(R.id.open);
            delete = (Button) itemView.findViewById(R.id.delete);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<TimeOffRequestData> mData;
    private Context mContext;
    private Typeface typeFace;
    private boolean edit = false;

    public TimeOffRequestsAdapter(Context context, boolean edit) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Dealerpilothr.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<TimeOffRequestData>();
        this.edit = edit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_full, parent, false);
        return new TimeOffRequestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        TimeOffRequestData data = mData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(UserInfo.getUserInfo().getAttendanceCodeName(data.getAttendanceCodeId()));
        holder.text_date.setText(data.getRequestDateString());

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+ Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnOpenTimeOffRequestsClickListener) {
                    ((OnOpenTimeOffRequestsClickListener) mContext).onOpenTimeOffRequestsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDeleteTimeOffRequestsClickListener) {
                    ((OnDeleteTimeOffRequestsClickListener) mContext).onDeleteTimeOffRequestsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootTimeOffRequestsClickListener) {
                    ((OnRootTimeOffRequestsClickListener) mContext).onRootTimeOffRequestsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.open.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.VISIBLE);

        if (!edit) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        else
            return 0;
    }

    public void setData(ArrayList<TimeOffRequestData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public TimeOffRequestData getDataItem(int position) {
        return mData.get(position);
    }
}