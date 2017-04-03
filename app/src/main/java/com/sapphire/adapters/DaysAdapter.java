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
import com.sapphire.logic.UserInfo;
import com.sapphire.models.DayData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_description;
        Button delete;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_description = (TextView) itemView.findViewById(R.id.text_description);
            delete = (Button) itemView.findViewById(R.id.delete);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<DayData> listData;
    private Context context;
    private Typeface typeFace;

    public DaysAdapter(Context context) {
        this.context = context;
        listData = new ArrayList<DayData>();
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_days, parent, false);
        return new DaysAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        DayData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getDateString());

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

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof OnRootClickListener) {
                    ((OnRootClickListener) context).onRootClick(holder.getAdapterPosition());
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

    public void setListArray(ArrayList<DayData> list){
        listData.clear();
        listData.addAll(list);
        sort();
        notifyDataSetChanged();
    }

    public void remove(int position){
        listData.remove(position);
        notifyDataSetChanged();
    }

    public ArrayList<DayData> getData(){
        return listData;
    }

    public void addData(DayData data){
        boolean exist = false;
        for (DayData item: listData) {
            if (item.getDateString().equals(data.getDateString())) {
                exist = true;
                break;
            }
        }
        if (!exist && UserInfo.getUserInfo().getDays() != null) {
            for (DayData item: UserInfo.getUserInfo().getDays()) {
                if (item.getDateString().equals(data.getDateString())) {
                    exist = true;
                    break;
                }
            }
        }
        if (exist) {
            return;
        }
        listData.add(data);
        sort();
        notifyDataSetChanged();
    }

    private void sort() {
        Collections.sort(listData, new Comparator<DayData>() {
            public int compare(DayData o1, DayData o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }
}
