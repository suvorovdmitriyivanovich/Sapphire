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
import com.sapphire.models.FileData;

import java.util.ArrayList;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    public interface OnDownloadClickListener{
        void onDownloadClick(int position);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_description;
        Button download;
        Button delete;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_description = (TextView) itemView.findViewById(R.id.text_description);
            download = (Button) itemView.findViewById(R.id.open);
            delete = (Button) itemView.findViewById(R.id.delete);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<FileData> listData;
    private Context mContext;
    private Typeface typeFace;

    public FilesAdapter(Context context) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        listData = new ArrayList<FileData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_full, parent, false);
        return new FilesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        FileData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());
        holder.text_description.setText(data.getSizeKbString() + " Kb");

        holder.download.setTypeface(typeFace);
        holder.download.setText(Html.fromHtml("&#"+Environment.IcoDownload+";"));
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDownloadClickListener) {
                    ((OnDownloadClickListener) mContext).onDownloadClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDeleteClickListener) {
                    ((OnDeleteClickListener) mContext).onDeleteClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootClickListener) {
                    ((OnRootClickListener) mContext).onRootClick(holder.getAdapterPosition());
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

    public void setData(ArrayList<FileData> datas) {
        listData.clear();
        listData.addAll(datas);
        notifyDataSetChanged();
    }
}