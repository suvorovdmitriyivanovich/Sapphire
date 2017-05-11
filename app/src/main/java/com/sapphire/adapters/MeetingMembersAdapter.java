package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.models.MemberData;
import java.util.ArrayList;

public class MeetingMembersAdapter extends RecyclerView.Adapter<MeetingMembersAdapter.ViewHolder> {

    public interface OnRootMeetingMembersClickListener{
        void onRootMeetingMembersClick(int position);
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

    private ArrayList<MemberData> listData;
    private Context context;
    private Typeface typeFace;
    private boolean readonly = false;

    public MeetingMembersAdapter(Context context, boolean readonly) {
        this.context = context;
        listData = new ArrayList<MemberData>();
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        this.readonly = readonly;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_check, parent, false);
        return new MeetingMembersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final MemberData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        if (data.getProfile().getFullName().equals("")) {
            holder.text_name.setText(data.getName());
        } else {
            holder.text_name.setText(data.getProfile().getFullName());
        }
        holder.presence.setChecked(data.getPresence());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readonly) {
                    return;
                }
                if (context instanceof OnRootMeetingMembersClickListener) {
                    ((OnRootMeetingMembersClickListener) context).onRootMeetingMembersClick(holder.getAdapterPosition());
                }
            }
        });

        holder.presence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (data.getPresence() == isChecked || readonly) {
                    return;
                }
                if (context instanceof OnRootMeetingMembersClickListener) {
                    ((OnRootMeetingMembersClickListener) context).onRootMeetingMembersClick(holder.getAdapterPosition());
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

    public void setListArray(ArrayList<MemberData> list){
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }
}
