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
import com.sapphire.logic.Environment;
import com.sapphire.models.SafetyData;
import java.util.ArrayList;

public class SafetisAdapter extends RecyclerView.Adapter<SafetisAdapter.ViewHolder> {

    public interface OnRootSafetisClickListener{
        void onRootSafetisClick(int position);
    }

    public interface OnOpenSafetisClickListener{
        void onOpenSafetisClick(int position);
    }

    public interface OnDeleteSafetisClickListener{
        void onDeleteSafetisClick(int position);
    }

    public interface OnFilesSafetisClickListener{
        void onFilesSafetisClick(int position);
    }

    public interface OnReportSafetisClickListener{
        void onReportSafetisClick(int position);
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

    private ArrayList<SafetyData> mData;
    private Context mContext;
    private Typeface typeFace;
    private boolean isDashboard = false;
    private boolean edit = false;

    public SafetisAdapter(Context context, boolean isDashboard, boolean edit) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<SafetyData>();
        this.isDashboard = isDashboard;
        this.edit = edit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_files_full, parent, false);
        return new SafetisAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        SafetyData data = mData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());
        holder.text_date.setText(data.getRenewalDateString());

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnOpenSafetisClickListener) {
                    ((OnOpenSafetisClickListener) mContext).onOpenSafetisClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDeleteSafetisClickListener) {
                    ((OnDeleteSafetisClickListener) mContext).onDeleteSafetisClick(holder.getAdapterPosition());
                }
            }
        });

        holder.files.setTypeface(typeFace);
        holder.files.setText(Html.fromHtml("&#"+Environment.IcoFiles+";"));
        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnFilesSafetisClickListener) {
                    ((OnFilesSafetisClickListener) mContext).onFilesSafetisClick(holder.getAdapterPosition());
                }
            }
        });

        holder.report.setTypeface(typeFace);
        holder.report.setText(Html.fromHtml("&#"+Environment.IcoReport+";"));
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnReportSafetisClickListener) {
                    ((OnReportSafetisClickListener) mContext).onReportSafetisClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootSafetisClickListener) {
                    ((OnRootSafetisClickListener) mContext).onRootSafetisClick(holder.getAdapterPosition());
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
            holder.report.setVisibility(View.VISIBLE);
        }

        if (!edit) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

        holder.report.setEnabled(!data.getFileId().equals(""));
    }

    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        else
            return 0;
    }

    public void setData(ArrayList<SafetyData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public SafetyData getDataItem(int position) {
        return mData.get(position);
    }
}