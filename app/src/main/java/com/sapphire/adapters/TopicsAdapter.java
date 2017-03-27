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
import com.sapphire.models.TopicData;
import java.util.ArrayList;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {

    public interface OnRootTopicsClickListener{
        void onRootTopicsClick(int position);
    }

    public interface OnOpenTopicsClickListener{
        void onOpenTopicsClick(int position);
    }

    public interface OnDeleteTopicsClickListener{
        void onDeleteTopicsClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button open;
        Button delete;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_name);
            open = (Button) itemView.findViewById(R.id.open);
            delete = (Button) itemView.findViewById(R.id.delete);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<TopicData> listData;
    private Context context;
    private Typeface typeFace;

    public TopicsAdapter(Context context) {
        this.context = context;
        listData = new ArrayList<TopicData>();
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_topics, parent, false);
        return new TopicsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final TopicData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.name.setText(data.getName());

        if (data.getIsTemplate()) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        } else {
            holder.open.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnOpenTopicsClickListener) {
                    ((OnOpenTopicsClickListener) context).onOpenTopicsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof OnDeleteTopicsClickListener) {
                    ((OnDeleteTopicsClickListener) context).onDeleteTopicsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof OnRootTopicsClickListener) {
                    ((OnRootTopicsClickListener) context).onRootTopicsClick(holder.getAdapterPosition());
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

    public void setListArray(ArrayList<TopicData> list){
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public void addTopic(TopicData data){
        listData.add(data);
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        listData.remove(position);
        notifyDataSetChanged();
    }
}
