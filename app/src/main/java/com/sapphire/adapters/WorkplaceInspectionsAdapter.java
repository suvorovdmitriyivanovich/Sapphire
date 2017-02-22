package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.WorkplaceInspectionData;
import java.util.ArrayList;

public class WorkplaceInspectionsAdapter extends RecyclerView.Adapter<WorkplaceInspectionsAdapter.ViewHolder> {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    public interface OnOpenClickListener{
        void onOpenClick(int position);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(int position);
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
            text_date = (TextView) itemView.findViewById(R.id.text_course);
            open = (Button) itemView.findViewById(R.id.open);
            delete = (Button) itemView.findViewById(R.id.delete);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<WorkplaceInspectionData> mData;
    private Context mContext;
    private Typeface typeFace;

    public WorkplaceInspectionsAdapter(Context context) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<WorkplaceInspectionData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_templates_view, parent, false);
        return new WorkplaceInspectionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        WorkplaceInspectionData data = mData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());
        holder.text_date.setText(data.getDateString());

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#61504;"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnOpenClickListener) {
                    ((OnOpenClickListener) mContext).onOpenClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#61944;"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDeleteClickListener) {
                    ((OnDeleteClickListener) mContext).onDeleteClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootClickListener) {
                    ((OnRootClickListener) mContext).onRootClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
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

    public void setData(ArrayList<WorkplaceInspectionData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public WorkplaceInspectionData getDataItem(int position) {
        return mData.get(position);
    }
}