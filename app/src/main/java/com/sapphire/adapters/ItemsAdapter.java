package com.sapphire.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.TemplateItemData;
import java.util.ArrayList;

public class ItemsAdapter extends BaseAdapter {

    public interface OnRootClickListener{
        void onRootClick(int position);
    }

    public interface OnOpenClickListener{
        void onOpenClick(int position);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(int position);
    }

    private ArrayList<TemplateItemData> listData;
    private Context context;
    private static LayoutInflater inflater = null;
    private Typeface typeFace;

    public ItemsAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listData = new ArrayList<TemplateItemData>();
        typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
    }

    @Override
    public int getCount() {
        if(listData != null) {
            return listData.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TemplateItemData data = listData.get(position);

        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.items_view, null);

        View root = view.findViewById(R.id.root);

        TextView text_name = (TextView) view.findViewById(R.id.text_name);
        text_name.setText(data.getName());

        TextView text_description = (TextView) view.findViewById(R.id.text_description);
        text_description.setText(data.getDescription());

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnRootClickListener)context).onRootClick(position);
            }
        });

        Button open = (Button) view.findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnOpenClickListener)context).onOpenClick(position);
            }
        });

        Button delete = (Button) view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnDeleteClickListener)context).onDeleteClick(position);
            }
        });

        open.setTypeface(typeFace);
        open.setText(Html.fromHtml("&#61504;"));

        delete.setTypeface(typeFace);
        delete.setText(Html.fromHtml("&#61944;"));

        return view;
    }

    public void setListArray(ArrayList<TemplateItemData> list){
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }
}
