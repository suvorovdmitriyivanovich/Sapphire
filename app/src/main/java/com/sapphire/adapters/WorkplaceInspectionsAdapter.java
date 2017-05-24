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
import com.sapphire.models.WorkplaceInspectionData;
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

    public interface OnAssignWorkplaceInspectionsClickListener{
        void onAssignWorkplaceInspectionsClick(int position);
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
        Button assign;
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
            assign = (Button) itemView.findViewById(R.id.assign);
            report = (Button) itemView.findViewById(R.id.report);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<WorkplaceInspectionData> mData;
    private Context mContext;
    private Typeface typeFace;
    private boolean isDashboard = false;
    private boolean edit = false;

    public WorkplaceInspectionsAdapter(Context context, boolean isDashboard, boolean edit) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<WorkplaceInspectionData>();
        this.isDashboard = isDashboard;
        this.edit = edit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_workplace, parent, false);
        return new WorkplaceInspectionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        WorkplaceInspectionData data = mData.get(position);

        if (position < mData.size()-1 || !isDashboard) {
            holder.border.setVisibility(View.VISIBLE);
        } else {
            holder.border.setVisibility(View.GONE);
        }

        holder.text_name.setText(data.getName());

        String description = "";
        if (data.getDate() != 0l) {
            description = description + data.getDateString();
        }
        if (!description.equals("")) {
            description = description + "<br>";
        }
        description = description + Sapphire.getInstance().getResources().getString(R.string.text_inspected);
        description = description + ": ";
        if (data.getInspected()) {
            description = description + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            description = description + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }
        description = description + "<br>" + Sapphire.getInstance().getResources().getString(R.string.text_completed);
        description = description + ": ";
        if (data.getCompleted()) {
            description = description + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            description = description + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }
        description = description + "<br>" + Sapphire.getInstance().getResources().getString(R.string.text_posted);
        description = description + ": ";
        if (data.getPostedOnBoard()) {
            description = description + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            description = description + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }

        holder.text_date.setTypeface(typeFace);
        holder.text_date.setText(Html.fromHtml(description));

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnOpenWorkplaceInspectionsClickListener) {
                    ((OnOpenWorkplaceInspectionsClickListener) mContext).onOpenWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDeleteWorkplaceInspectionsClickListener) {
                    ((OnDeleteWorkplaceInspectionsClickListener) mContext).onDeleteWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.files.setTypeface(typeFace);
        holder.files.setText(Html.fromHtml("&#"+Environment.IcoFiles+";"));
        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnFilesWorkplaceInspectionsClickListener) {
                    ((OnFilesWorkplaceInspectionsClickListener) mContext).onFilesWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.assign.setTypeface(typeFace);
        if (data.getInspected()) {
            holder.assign.setText(Html.fromHtml("&#"+Environment.IcoLock+";"));
        } else {
            holder.assign.setText(Html.fromHtml("&#"+Environment.IcoAssign+";"));
        }
        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnAssignWorkplaceInspectionsClickListener) {
                    ((OnAssignWorkplaceInspectionsClickListener) mContext).onAssignWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.report.setTypeface(typeFace);
        holder.report.setText(Html.fromHtml("&#"+Environment.IcoReport+";"));
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnReportWorkplaceInspectionsClickListener) {
                    ((OnReportWorkplaceInspectionsClickListener) mContext).onReportWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootWorkplaceInspectionsClickListener) {
                    ((OnRootWorkplaceInspectionsClickListener) mContext).onRootWorkplaceInspectionsClick(holder.getAdapterPosition());
                }
            }
        });

        if (isDashboard) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.files.setVisibility(View.GONE);
        } else {
            holder.open.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.files.setVisibility(View.VISIBLE);
        }

        if (!edit) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.files.setVisibility(View.GONE);
            holder.assign.setVisibility(View.GONE);
        }

        holder.report.setEnabled(data.getInspected());
        holder.open.setEnabled(!data.getInspected());
        holder.delete.setEnabled(!data.getInspected());
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