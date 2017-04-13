package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.DayData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DayItemsAdapter extends RecyclerView.Adapter<DayItemsAdapter.ViewHolder> {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(int position);
    }

    public interface OnChangeClickListener{
        void onChangeClick(int position, String text);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        EditText count;
        Button delete;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            count = (EditText) itemView.findViewById(R.id.count);
            delete = (Button) itemView.findViewById(R.id.delete);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<DayData> listData;
    private Context context;
    private Typeface typeFace;
    private boolean notchange = false;
    private ArrayList<Integer> textWatchers = new ArrayList<Integer>();
    private boolean edit = false;

    public DayItemsAdapter(Context context, boolean edit) {
        this.context = context;
        listData = new ArrayList<DayData>();
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        this.edit = edit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_dayitems, parent, false);
        return new DayItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        DayData data = listData.get(position);

        holder.border.setVisibility(View.VISIBLE);

        if (edit && textWatchers.indexOf(position) == -1) {
            TextWatcher inputTextWatcher = new TextWatch(position);
            holder.count.addTextChangedListener(inputTextWatcher);
            textWatchers.add(position);
        }

        holder.text_name.setText(data.getDateString());
        notchange = true;
        if (data.getAmmount() == 0d) {
            holder.count.setText("");
        } else {
            holder.count.setText(String.valueOf(data.getAmmount()));
        }

        /*
        holder.count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (position >= listData.size() || notchange) {
                    notchange = false;
                    return;
                }
                if (context instanceof OnChangeClickListener) {
                    ((OnChangeClickListener) context).onChangeClick(holder.getAdapterPosition(), s.toString());
                }
            }
        });*/

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

        if (!edit) {
            holder.delete.setVisibility(View.GONE);
            holder.count.setFocusable(false);
        }
    }

    private class TextWatch implements TextWatcher {
        private int position = 0;
        public TextWatch(int position){
            super();
            this.position = position;
        }

        public void afterTextChanged(Editable s) {
            if (position >= listData.size() || notchange) {
                notchange = false;
                return;
            }
            listData.get(position).setAmmount(s.toString());
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
        textWatchers.remove(Integer.valueOf(position));
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
        if (exist) {
            return;
        }
        listData.add(data);
        sort();
        notifyDataSetChanged();
    }

    public void addDatas(ArrayList<DayData> days){
        boolean exist = false;
        /*
        for (DayData item: listData) {
            if (item.getDateString().equals(data.getDateString())) {
                exist = true;
                break;
            }
        }
        if (exist) {
            return;
        }
        */
        listData.addAll(days);
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
