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
import com.sapphire.logic.AdressData;
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

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_detail = (TextView) itemView.findViewById(R.id.text_detail);
            open = (Button) itemView.findViewById(R.id.open);
            root = itemView.findViewById(R.id.root);
        }
    }

    private Context mContext;
    private ArrayList<AdressData> listData;
    private Typeface typeFace;

    public AdressAdapter(Context mContext) {
        this.mContext = mContext;
        this.listData = new ArrayList<AdressData>();
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
    }

    public void setData(ArrayList<AdressData> list) {
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
                ((OnRootClickListener)mContext).onRootClick(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnOpenClickListener)mContext).onOpenClick(viewHolder.getAdapterPosition());
            }
        });

        AdressData data = (AdressData) listData.get(position);

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
            detail = detail + Sapphire.getInstance().getResources().getString(R.string.name) + ": " + data.getName();
        }
        if (!data.getAddress().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + Sapphire.getInstance().getResources().getString(R.string.text_adress) + ": " + data.getAddress();
        }
        if (!data.getPhone1().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + Sapphire.getInstance().getResources().getString(R.string.text_phone) + " 1: " + data.getPhone1();
        }
        if (!data.getPhone2().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + Sapphire.getInstance().getResources().getString(R.string.text_phone) + " 2: " + data.getPhone2();
        }
        if (!data.getEmail1().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + Sapphire.getInstance().getResources().getString(R.string.text_email) + " 1: " + data.getEmail1();
        }
        if (!data.getEmail2().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + Sapphire.getInstance().getResources().getString(R.string.text_email) + " 2: " + data.getEmail2();
        }

        viewHolder.text_name.setText(name);
        viewHolder.text_detail.setText(Html.fromHtml(detail));

        viewHolder.open.setTypeface(typeFace);
        viewHolder.open.setText(Html.fromHtml("&#61504;"));
    }

    @Override
    public int getItemCount() {
        if(listData != null)
            return listData.size();
        else
            return 0;
    }

    public AdressData getAdress(int position) {
        return listData.get(position);
    }
}
