package com.sapphire.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.models.ContactData;
import java.util.ArrayList;

public class AdressAdapter extends RecyclerView.Adapter<AdressAdapter.ViewHolder> {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    public interface OnOpenClickListener{
        void onOpenClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_detail;
        Button open;
        View root;
        View border;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_detail = (TextView) itemView.findViewById(R.id.text_detail);
            open = (Button) itemView.findViewById(R.id.open);
            root = itemView.findViewById(R.id.root);
            border = itemView.findViewById(R.id.border);
        }
    }

    private Context mContext;
    private ArrayList<ContactData> listData;
    private boolean full = false;

    public AdressAdapter(Context mContext, boolean full) {
        this.mContext = mContext;
        this.listData = new ArrayList<ContactData>();
        this.full = full;
    }

    public void setData(ArrayList<ContactData> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adress_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnRootClickListener) {
                    ((OnRootClickListener) mContext).onRootClick(viewHolder.getAdapterPosition());
                }
            }
        });

        ContactData data = (ContactData) listData.get(position);

        if (position < listData.size()-1) {
            viewHolder.border.setVisibility(View.VISIBLE);
        } else {
            viewHolder.border.setVisibility(View.GONE);
        }

        String name = "";
        if (data.getIsPrimary()) {
            name = Sapphire.getInstance().getResources().getString(R.string.text_primary_residence);
        } else {
            name = Sapphire.getInstance().getResources().getString(R.string.text_secondary_residence);
        }

        String detail = "";
        if (!data.getName().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + Sapphire.getInstance().getResources().getString(R.string.name) + "</b>: " + data.getName();
        }
        if (full) {
            if (!data.getRelationship().equals("")) {
                if (!detail.equals("")) {
                    detail = detail + "<br>";
                }
                detail = detail + "<b>" + Sapphire.getInstance().getResources().getString(R.string.text_relationship) + "</b>: " + data.getRelationship();
            }
            if (!data.getPhone1().equals("")) {
                if (!detail.equals("")) {
                    detail = detail + "<br>";
                }
                detail = detail + "<b>" + Sapphire.getInstance().getResources().getString(R.string.text_primary_phone) + "</b>: " + data.getPhone1();
            }
            if (!data.getPhone2().equals("")) {
                if (!detail.equals("")) {
                    detail = detail + "<br>";
                }
                detail = detail + "<b>" + Sapphire.getInstance().getResources().getString(R.string.text_additional_phone) + "</b>: " + data.getPhone2();
            }
            if (!data.getEmail1().equals("")) {
                if (!detail.equals("")) {
                    detail = detail + "<br>";
                }
                detail = detail + "<b>" + Sapphire.getInstance().getResources().getString(R.string.text_email) + "</b>: " + data.getEmail1();
            }
            if (!data.getNote().getText().equals("")) {
                if (!detail.equals("")) {
                    detail = detail + "<br>";
                }
                detail = detail + "<b>" + Sapphire.getInstance().getResources().getString(R.string.text_notes) + "</b>: " + data.getNote().getText();
            }
        }
        if (!data.getAddress().getAddress().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + Sapphire.getInstance().getResources().getString(R.string.text_adress) + "</b>: " + data.getAddress().getAddress();
        }

        viewHolder.text_name.setText(name);
        viewHolder.text_detail.setText(Html.fromHtml(detail));
    }

    @Override
    public int getItemCount() {
        if(listData != null)
            return listData.size();
        else
            return 0;
    }

    public ContactData getAdress(int position) {
        return listData.get(position);
    }
}
