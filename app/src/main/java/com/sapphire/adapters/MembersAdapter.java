package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ProfileData;
import java.util.ArrayList;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    public interface OnRootMembersClickListener{
        void onRootMembersClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_description;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_description = (TextView) itemView.findViewById(R.id.text_description);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<ProfileData> mData;
    private Context mContext;
    private Typeface typeFace;
    private boolean isDashboard = false;

    public MembersAdapter(Context context, boolean isDashboard) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<ProfileData>();
        this.isDashboard = isDashboard;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_text, parent, false);
        return new MembersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ProfileData data = mData.get(position);

        if (position < mData.size()-1 || !isDashboard) {
            holder.border.setVisibility(View.VISIBLE);
        } else {
            holder.border.setVisibility(View.GONE);
        }

        String name = "";
        if (!data.getName().equals("")) {
            name = name + data.getName();
        }
        if (!data.getPosition().equals("")) {
            if (name.equals("")) {
                name = name + data.getPosition();
            } else {
                name = name + " (" + data.getPosition() + ")";
            }
        }

        String description = "";
        description = description + Sapphire.getInstance().getResources().getString(R.string.text_certified);
        description = description + ": ";
        if (data.getIsCPRCertified()) {
            description = description + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            description = description + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }
        description = description + "<br>" + Sapphire.getInstance().getResources().getString(R.string.text_aidcertified);
        description = description + ": ";
        if (data.getIsFirstAidCertified()) {
            description = description + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            description = description + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }
        description = description + "<br>" + Sapphire.getInstance().getResources().getString(R.string.text_healthcertified);
        description = description + ": ";
        if (data.getIsHealthSafetyCertified()) {
            description = description + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            description = description + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }

        holder.text_name.setText(name);
        holder.text_description.setTypeface(typeFace);
        holder.text_description.setText(Html.fromHtml(description));

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootMembersClickListener) {
                    ((OnRootMembersClickListener) mContext).onRootMembersClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        else
            return 0;
    }

    public void setData(ArrayList<ProfileData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public ProfileData getDataItem(int position) {
        return mData.get(position);
    }
}