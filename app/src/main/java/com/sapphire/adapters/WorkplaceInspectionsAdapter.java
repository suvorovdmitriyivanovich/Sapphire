package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.WorkplaceInspectionData;
import java.util.ArrayList;

public class WorkplaceInspectionsAdapter extends RecyclerView.Adapter<WorkplaceInspectionsAdapter.ViewHolder> {

    public interface OnRootWorkplaceInspectionsClickListener{
        void onRootWorkplaceInspectionsClick(int position);
    }

    public interface OnOpenWorkplaceInspectionsClickListener{
        void onOpenWorkplaceInspectionsClick(int position);
    }

    public interface OnDeleteWorkplaceInspectionsClickListener{
        void onDeleteWorkplaceInspectionsClick(int position);
    }

    public interface OnFilesWorkplaceInspectionsClickListener{
        void onFilesWorkplaceInspectionsClick(int position);
    }

    public interface OnReportWorkplaceInspectionsClickListener{
        void onReportWorkplaceInspectionsClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_date;
        Button open;
        Button delete;
        Button files;
        Button report;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_date = (TextView) itemView.findViewById(R.id.text_description);
            open = (Button) itemView.findViewById(R.id.open);
            delete = (Button) itemView.findViewById(R.id.delete);
            files = (Button) itemView.findViewById(R.id.files);
            report = (Button) itemView.findViewById(R.id.report);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<WorkplaceInspectionData> mData;
    private Context mContext;
    private Typeface typeFace;
    private boolean isDashboard = false;

    public WorkplaceInspectionsAdapter(Context context, boolean isDashboard) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<WorkplaceInspectionData>();
        this.isDashboard = isDashboard;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_files_full, parent, false);
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
                if (mContext instanceof OnOpenWorkplaceInspectionsClickListener) {
                    ((OnOpenWorkplaceInspectionsClickListener) mContext).onOpenWorkplaceInspectionsClick(holder.getAdapterPosition());
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
                if (mContext instanceof OnDeleteWorkplaceInspectionsClickListener) {
                    ((OnDeleteWorkplaceInspectionsClickListener) mContext).onDeleteWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });

        holder.files.setTypeface(typeFace);
        holder.files.setText(Html.fromHtml("&#61787;"));
        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnFilesWorkplaceInspectionsClickListener) {
                    ((OnFilesWorkplaceInspectionsClickListener) mContext).onFilesWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });

        holder.report.setTypeface(typeFace);
        holder.report.setText(Html.fromHtml("&#61889;"));
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnReportWorkplaceInspectionsClickListener) {
                    ((OnReportWorkplaceInspectionsClickListener) mContext).onReportWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootWorkplaceInspectionsClickListener) {
                    ((OnRootWorkplaceInspectionsClickListener) mContext).onRootWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });

        if (isDashboard) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.files.setVisibility(View.GONE);
            holder.report.setVisibility(View.VISIBLE);
        } else {
            holder.open.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.files.setVisibility(View.VISIBLE);
            holder.report.setVisibility(View.GONE);
        }
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