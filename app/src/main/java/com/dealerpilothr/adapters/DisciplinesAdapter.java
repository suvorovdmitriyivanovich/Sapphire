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
import com.dealerpilothr.models.DisciplineData;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;

import java.util.ArrayList;

public class DisciplinesAdapter extends RecyclerView.Adapter<DisciplinesAdapter.ViewHolder> {

    public interface OnRootDisciplinesClickListener{
        void onRootDisciplinesClick(int position);
    }

    public interface OnOpenDisciplinesClickListener{
        void onOpenDisciplinesClick(int position);
    }

    public interface OnDeleteDisciplinesClickListener{
        void onDeleteDisciplinesClick(int position);
    }

    public interface OnFilesDisciplinesClickListener{
        void onFilesDisciplinesClick(int position);
    }

    public interface OnReportDisciplinesClickListener{
        void onReportDisciplinesClick(int position);
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

    private ArrayList<DisciplineData> mData;
    private Context mContext;
    private Typeface typeFace;
    private boolean edit = false;

    public DisciplinesAdapter(Context context, boolean edit) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Dealerpilothr.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<DisciplineData>();
        this.edit = edit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_files_full, parent, false);
        return new DisciplinesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        DisciplineData data = mData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());
        holder.text_date.setText(data.getDatePostedString());

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnOpenDisciplinesClickListener) {
                    ((OnOpenDisciplinesClickListener) mContext).onOpenDisciplinesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDeleteDisciplinesClickListener) {
                    ((OnDeleteDisciplinesClickListener) mContext).onDeleteDisciplinesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.files.setTypeface(typeFace);
        holder.files.setText(Html.fromHtml("&#"+Environment.IcoFiles+";"));
        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnFilesDisciplinesClickListener) {
                    ((OnFilesDisciplinesClickListener) mContext).onFilesDisciplinesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.report.setTypeface(typeFace);
        holder.report.setText(Html.fromHtml("&#"+Environment.IcoReport+";"));
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnReportDisciplinesClickListener) {
                    ((OnReportDisciplinesClickListener) mContext).onReportDisciplinesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootDisciplinesClickListener) {
                    ((OnRootDisciplinesClickListener) mContext).onRootDisciplinesClick(holder.getAdapterPosition());
                }
            }
        });

        holder.open.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.VISIBLE);
        holder.files.setVisibility(View.VISIBLE);
        holder.report.setVisibility(View.GONE);

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

    public void setData(ArrayList<DisciplineData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public DisciplineData getDataItem(int position) {
        return mData.get(position);
    }
}