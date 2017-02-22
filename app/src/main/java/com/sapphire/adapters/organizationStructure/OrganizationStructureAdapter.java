package com.sapphire.adapters.organizationStructure;

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
import com.sapphire.logic.OrganizationStructureData;

import java.util.ArrayList;

public class OrganizationStructureAdapter extends RecyclerView.Adapter<OrganizationStructureAdapter.ViewHolder> {

    public interface OnRootClickListener {
        void onRootClick(int position);
    }

    public interface OnAddClickListener {
        void onAddClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        Button add;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            add = (Button) itemView.findViewById(R.id.add);
            item = itemView;
        }
    }

    private Context mContext;
    private ArrayList<OrganizationStructureData> data;
    private Typeface typeFace;

    public OrganizationStructureAdapter(Context context) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_organization, parent, false);
        return new OrganizationStructureAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        OrganizationStructureData organizationStructureData = data.get(position);

        holder.text_name.setText(organizationStructureData.getName());

        holder.add.setTypeface(typeFace);
        holder.add.setText(Html.fromHtml("&#61543;"));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnAddClickListener) {
                    ((OnAddClickListener) mContext).onAddClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootClickListener) {
                    ((OnRootClickListener) mContext).onRootClick(holder.getAdapterPosition());
                }
                else {
                    //TODO generate error dialog
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        else
            return 0;
    }

    public void setData(ArrayList<OrganizationStructureData> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public OrganizationStructureData getDataItem(int position) {
        return data.get(position);
    }
}