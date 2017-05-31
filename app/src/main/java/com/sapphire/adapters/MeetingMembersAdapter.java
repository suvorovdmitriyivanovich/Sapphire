package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.MemberData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MeetingMembersAdapter extends RecyclerView.Adapter<MeetingMembersAdapter.ViewHolder> {

    public interface OnRootMeetingMembersClickListener{
        void onRootMeetingMembersClick(int position);
    }

    public interface OnDeleteMeetingMembersClickListener{
        void onDeleteMeetingMembersClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        CheckBox presence;
        Button delete;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            presence = (CheckBox) itemView.findViewById(R.id.presence);
            delete = (Button) itemView.findViewById(R.id.delete);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<MemberData> listData;
    private Context context;
    private Typeface typeFace;
    private boolean readonly = false;
    private boolean onDelete = false;

    public MeetingMembersAdapter(Context context, boolean readonly, boolean onDelete) {
        this.context = context;
        listData = new ArrayList<MemberData>();
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        this.readonly = readonly;
        this.onDelete = onDelete;
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

        if (onDelete) {
            holder.presence.setVisibility(View.GONE);
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.presence.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.GONE);
            holder.presence.setChecked(data.getPresence());
        }

        if (data.getProfile().getFullName().equals("")) {
            holder.text_name.setText(data.getName());
        } else {
            holder.text_name.setText(data.getProfile().getFullName());
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readonly || onDelete) {
                    return;
                }
                data.setPresence(!data.getPresence());
                holder.presence.setChecked(data.getPresence());
                if (context instanceof OnRootMeetingMembersClickListener) {
                    ((OnRootMeetingMembersClickListener) context).onRootMeetingMembersClick(holder.getAdapterPosition());
                }
            }
        });

        if (onDelete) {
            holder.delete.setTypeface(typeFace);
            holder.delete.setText(Html.fromHtml("&#" + Environment.IcoDelete + ";"));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof OnDeleteMeetingMembersClickListener) {
                        ((OnDeleteMeetingMembersClickListener) context).onDeleteMeetingMembersClick(holder.getAdapterPosition());
                    }
                }
            });
        } else {
            holder.presence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (data.getPresence() == isChecked || readonly) {
                        return;
                    }
                    data.setPresence(!data.getPresence());
                    holder.presence.setChecked(data.getPresence());
                    if (context instanceof OnRootMeetingMembersClickListener) {
                        ((OnRootMeetingMembersClickListener) context).onRootMeetingMembersClick(holder.getAdapterPosition());
                    }
                }
            });
        }

        if (readonly) {
            holder.presence.setClickable(false);
            holder.delete.setVisibility(View.GONE);
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
        //sort();
        notifyDataSetChanged();
    }

    public void remove(int position){
        listData.remove(position);
        notifyDataSetChanged();
    }

    private void sort() {
        Collections.sort(listData, new Comparator<MemberData>() {
            public int compare(MemberData o1, MemberData o2) {
                return o1.getProfile().getName().compareTo(o2.getProfile().getName());
            }
        });
    }
}