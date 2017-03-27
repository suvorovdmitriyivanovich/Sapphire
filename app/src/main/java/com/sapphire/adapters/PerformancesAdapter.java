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
import com.sapphire.models.PerformanceData;
import java.util.ArrayList;

public class PerformancesAdapter extends RecyclerView.Adapter<PerformancesAdapter.ViewHolder> {

    public interface OnRootPerformancesClickListener{
        void onRootPerformancesClick(int position);
    }

    public interface OnOpenPerformancesClickListener{
        void onOpenPerformancesClick(int position);
    }

    public interface OnDeletePerformancesClickListener{
        void onDeletePerformancesClick(int position);
    }

    public interface OnFilesPerformancesClickListener{
        void onFilesPerformancesClick(int position);
    }

    public interface OnReportPerformancesClickListener{
        void onReportPerformancesClick(int position);
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

    private ArrayList<PerformanceData> mData;
    private Context mContext;
    private Typeface typeFace;

    public PerformancesAdapter(Context context) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<PerformanceData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_files_full, parent, false);
        return new PerformancesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        PerformanceData data = mData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());
        holder.text_date.setText(data.getDatePostedString());

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnOpenPerformancesClickListener) {
                    ((OnOpenPerformancesClickListener) mContext).onOpenPerformancesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDeletePerformancesClickListener) {
                    ((OnDeletePerformancesClickListener) mContext).onDeletePerformancesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.files.setTypeface(typeFace);
        holder.files.setText(Html.fromHtml("&#"+Environment.IcoFiles+";"));
        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnFilesPerformancesClickListener) {
                    ((OnFilesPerformancesClickListener) mContext).onFilesPerformancesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.report.setTypeface(typeFace);
        holder.report.setText(Html.fromHtml("&#"+Environment.IcoReport+";"));
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnReportPerformancesClickListener) {
                    ((OnReportPerformancesClickListener) mContext).onReportPerformancesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootPerformancesClickListener) {
                    ((OnRootPerformancesClickListener) mContext).onRootPerformancesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.open.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.VISIBLE);
        holder.files.setVisibility(View.VISIBLE);
        holder.report.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        else
            return 0;
    }

    public void setData(ArrayList<PerformanceData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public PerformanceData getDataItem(int position) {
        return mData.get(position);
    }
}