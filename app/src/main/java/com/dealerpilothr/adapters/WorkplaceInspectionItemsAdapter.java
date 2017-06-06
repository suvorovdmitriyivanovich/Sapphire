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
import com.dealerpilothr.models.WorkplaceInspectionItemData;
import com.dealerpilothr.R;

import java.util.ArrayList;

public class WorkplaceInspectionItemsAdapter extends RecyclerView.Adapter<WorkplaceInspectionItemsAdapter.ViewHolder> {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    public interface OnOpenClickListener{
        void onOpenClick(int position);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(int position);
    }

    public interface OnFilesClickListener{
        void onFilesClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_description;
        TextView task;
        Button open;
        Button delete;
        Button files;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_description = (TextView) itemView.findViewById(R.id.text_description);
            task = (TextView) itemView.findViewById(R.id.task);
            open = (Button) itemView.findViewById(R.id.open);
            delete = (Button) itemView.findViewById(R.id.delete);
            files = (Button) itemView.findViewById(R.id.files);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<WorkplaceInspectionItemData> listData;
    private Context context;
    private Typeface typeFace;
    private boolean readonly = false;
    private boolean assign = false;
    private boolean newItem = false;

    //public WorkplaceInspectionItemsAdapter(Context context, boolean readonly, boolean assign, boolean newItem) {
    public WorkplaceInspectionItemsAdapter(Context context, boolean readonly, boolean assign) {
        this.context = context;
        listData = new ArrayList<WorkplaceInspectionItemData>();
        typeFace = Typeface.createFromAsset(Dealerpilothr.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        this.readonly = readonly;
        this.assign = assign;
        //this.newItem = newItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_workplace_item, parent, false);
        return new WorkplaceInspectionItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        WorkplaceInspectionItemData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());

        String textDescription = "";
        if (!data.getDescription().equals("")) {
            textDescription = data.getDescription();
        }
        if (!textDescription.equals("")) {
            textDescription = textDescription + "<br>";
        }
        if (data.getStatus().getName().equals("Pass")) {
            textDescription = textDescription + "<big><font color=#16a085>&#"+ Environment.IcoOk+";</font></big>";
        } else if (data.getStatus().getName().equals("Fail")) {
            textDescription = textDescription + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }

        holder.text_description.setTypeface(typeFace);
        holder.text_description.setText(Html.fromHtml(textDescription));

        if (assign && data.getStatus().getWorkplaceInspectionItemStatusId().equals(Environment.StatusFail)) {
            String textTask = "";
            if (data.getTask().getTaskId().equals("")) {
                textTask = textTask + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
            } else {
                textTask = textTask + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big>";
            }

            holder.task.setTypeface(typeFace);
            holder.task.setText(Html.fromHtml(textTask));
            holder.task.setVisibility(View.VISIBLE);
        } else {
            holder.task.setVisibility(View.GONE);
        }

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnOpenClickListener) {
                    ((OnOpenClickListener) context).onOpenClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnDeleteClickListener) {
                    ((OnDeleteClickListener) context).onDeleteClick(holder.getAdapterPosition());
                }
            }
        });

        holder.files.setTypeface(typeFace);
        if (assign) {
            if (data.getTask().getTaskId().equals("")) {
                holder.files.setText(Html.fromHtml("&#" + Environment.IcoAddTask + ";"));
            } else {
                holder.files.setText(Html.fromHtml("&#" + Environment.IcoTask + ";"));
            }
        } else {
            holder.files.setText(Html.fromHtml("&#" + Environment.IcoFiles + ";"));
        }
        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnFilesClickListener) {
                    ((OnFilesClickListener) context).onFilesClick(holder.getAdapterPosition());
                }
            }
        });

        /*
        if (newItem) {
            holder.files.setVisibility(View.GONE);
        } else {
            holder.files.setVisibility(View.VISIBLE);
        }
        */

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (context instanceof OnRootClickListener && !newItem) {
                if (context instanceof OnRootClickListener) {
                    ((OnRootClickListener) context).onRootClick(holder.getAdapterPosition());
                }
            }
        });

        if (readonly) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            if (!assign || !data.getStatus().getWorkplaceInspectionItemStatusId().equals(Environment.StatusFail)) {
                holder.files.setVisibility(View.GONE);
            }
        }
    }

    public void deleteItem(int position){
        listData.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(listData != null)
            return listData.size();
        else
            return 0;
    }

    public void setListArray(ArrayList<WorkplaceInspectionItemData> list){
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }
}
