package com.sapphire.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.models.BulletinData;
import java.util.ArrayList;

public class BulletinsAdapter extends RecyclerView.Adapter<BulletinsAdapter.ViewHolder> {

    public interface OnRootBulletinsClickListener{
        void onRootBulletinsClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView body;
        TextView date;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            body = (TextView) itemView.findViewById(R.id.body);
            date = (TextView) itemView.findViewById(R.id.date);
            item = itemView;
        }
    }

    private ArrayList<BulletinData> mData;
    private Context mContext;

    public BulletinsAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<BulletinData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_news, parent, false);
        return new BulletinsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        BulletinData data = mData.get(position);

        holder.name.setText(data.getName());
        holder.body.setText("    " + data.getBody());
        holder.date.setText(data.getDatePublishedString());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootBulletinsClickListener) {
                    ((OnRootBulletinsClickListener) mContext).onRootBulletinsClick(holder.getAdapterPosition());
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

    public void setData(ArrayList<BulletinData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public BulletinData getDataItem(int position) {
        return mData.get(position);
    }
}