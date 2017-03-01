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
import com.sapphire.logic.WorkplaceInspectionItemData;
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
        Button open;
        Button delete;
        Button files;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_description = (TextView) itemView.findViewById(R.id.text_description);
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

    public WorkplaceInspectionItemsAdapter(Context context) {
        this.context = context;
        listData = new ArrayList<WorkplaceInspectionItemData>();
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_files, parent, false);
        return new WorkplaceInspectionItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        WorkplaceInspectionItemData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());
        holder.text_description.setText(data.getDescription());

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#61504;"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnOpenClickListener) {
                    ((OnOpenClickListener) context).onOpenClick(holder.getAdapterPosition());
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
                if (context instanceof OnDeleteClickListener) {
                    ((OnDeleteClickListener) context).onDeleteClick(holder.getAdapterPosition());
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
                if (context instanceof OnFilesClickListener) {
                    ((OnFilesClickListener) context).onFilesClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof OnRootClickListener) {
                    ((OnRootClickListener) context).onRootClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });
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
