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
import com.dealerpilothr.models.InvestigationItemData;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;

import java.util.ArrayList;

public class InvestigationItemsAdapter extends RecyclerView.Adapter<InvestigationItemsAdapter.ViewHolder> {

    public interface OnRootInvestigationClickListener{
        void onRootInvestigationClick(int position);
    }

    public interface OnOpenInvestigationClickListener{
        void onOpenInvestigationClick(int position);
    }

    public interface OnDeleteInvestigationClickListener{
        void onDeleteInvestigationClick(int position);
    }

    public interface OnFilesInvestigationClickListener{
        void onFilesInvestigationClick(int position);
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

    private ArrayList<InvestigationItemData> listData;
    private Context context;
    private Typeface typeFace;
    private boolean readonly = false;

    public InvestigationItemsAdapter(Context context, boolean readonly) {
        this.context = context;
        listData = new ArrayList<InvestigationItemData>();
        typeFace = Typeface.createFromAsset(Dealerpilothr.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        this.readonly = readonly;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_files, parent, false);
        return new InvestigationItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        InvestigationItemData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getName());
        holder.text_description.setText(data.getDescription());

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnOpenInvestigationClickListener) {
                    ((OnOpenInvestigationClickListener) context).onOpenInvestigationClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnDeleteInvestigationClickListener) {
                    ((OnDeleteInvestigationClickListener) context).onDeleteInvestigationClick(holder.getAdapterPosition());
                }
            }
        });

        holder.files.setTypeface(typeFace);
        holder.files.setText(Html.fromHtml("&#"+Environment.IcoFiles+";"));
        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnFilesInvestigationClickListener) {
                    ((OnFilesInvestigationClickListener) context).onFilesInvestigationClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof OnRootInvestigationClickListener) {
                    ((OnRootInvestigationClickListener) context).onRootInvestigationClick(holder.getAdapterPosition());
                }
            }
        });

        if (readonly) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            //holder.files.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(listData != null)
            return listData.size();
        else
            return 0;
    }

    public void setListArray(ArrayList<InvestigationItemData> list){
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }
}
