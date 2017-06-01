package com.dealerpilothr.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.R;

import java.util.ArrayList;

public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.ViewHolder> {

    public interface OnRootAssignmentsClickListener{
        void onRootAssignmentsClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        CheckBox presence;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            presence = (CheckBox) itemView.findViewById(R.id.presence);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<ProfileData> listData;
    private Context context;
    private Typeface typeFace;
    private boolean readonly = false;

    public AssignmentsAdapter(Context context, boolean readonly) {
        this.context = context;
        listData = new ArrayList<ProfileData>();
        typeFace = Typeface.createFromAsset(Dealerpilothr.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        this.readonly = readonly;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_check, parent, false);
        return new AssignmentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ProfileData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        holder.text_name.setText(data.getFullName());
        holder.presence.setChecked(data.getPresence());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readonly) {
                    return;
                }
                if (context instanceof OnRootAssignmentsClickListener) {
                    ((OnRootAssignmentsClickListener) context).onRootAssignmentsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.presence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (data.getPresence() == isChecked || readonly) {
                    return;
                }
                if (context instanceof OnRootAssignmentsClickListener) {
                    ((OnRootAssignmentsClickListener) context).onRootAssignmentsClick(holder.getAdapterPosition());
                }
            }
        });

        if (readonly) {
            holder.presence.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        if(listData != null)
            return listData.size();
        else
            return 0;
    }

    public void setListArray(ArrayList<ProfileData> list){
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }
}
