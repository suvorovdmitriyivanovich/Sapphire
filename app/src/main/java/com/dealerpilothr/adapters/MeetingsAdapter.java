package com.dealerpilothr.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.R;
import com.dealerpilothr.models.MeetingData;
import java.util.ArrayList;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {

    public interface OnRootMeetingsClickListener{
        void onRootMeetingsClick(int position);
    }

    public interface OnOpenMeetingsClickListener{
        void onOpenMeetingsClick(int position);
    }

    public interface OnDeleteMeetingsClickListener{
        void onDeleteMeetingsClick(int position);
    }

    public interface OnFilesMeetingsClickListener{
        void onFilesMeetingsClick(int position);
    }

    public interface OnReportMeetingsClickListener{
        void onReportMeetingsClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_date;
        Button open;
        Button delete;
        Button files;
        Button report;
        View border;
        View item;

        ViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_date = (TextView) itemView.findViewById(R.id.text_description);
            open = (Button) itemView.findViewById(R.id.open);
            delete = (Button) itemView.findViewById(R.id.delete);
            files = (Button) itemView.findViewById(R.id.files);
            report = (Button) itemView.findViewById(R.id.report);
            border = itemView.findViewById(R.id.border);
            item = itemView;
        }
    }

    private ArrayList<MeetingData> mData;
    private Context mContext;
    private Typeface typeFace;
    private boolean isDashboard = false;
    private boolean edit = false;

    public MeetingsAdapter(Context context, boolean isDashboard, boolean edit) {
        mContext = context;
        typeFace = Typeface.createFromAsset(Dealerpilothr.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");
        mData = new ArrayList<MeetingData>();
        this.isDashboard = isDashboard;
        this.edit = edit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view_report_full, parent, false);
        return new MeetingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MeetingData data = mData.get(position);

        if (position < mData.size()-1 || !isDashboard) {
            holder.border.setVisibility(View.VISIBLE);
        } else {
            holder.border.setVisibility(View.GONE);
        }

        holder.text_name.setText(data.getName());

        String description = "";
        if (data.getMeetingDate() != 0l) {
            description = description + data.getMeetingDateStartEndString();
        }
        if (!description.equals("")) {
            description = description + "<br>";
        }
        description = description + Dealerpilothr.getInstance().getResources().getString(R.string.text_completed);
        description = description + ": ";
        if (data.getCompleted()) {
            description = description + "<big><font color=#16a085>&#"+ Environment.IcoOk+";</font></big> ";
        } else {
            description = description + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }
        description = description + "<br>" + Dealerpilothr.getInstance().getResources().getString(R.string.text_posted);
        description = description + ": ";
        if (data.getPosted()) {
            description = description + "<big><font color=#16a085>&#"+Environment.IcoOk+";</font></big> ";
        } else {
            description = description + "<big><font color=#cc3300>&#"+Environment.IcoClose+";</font></big>";
        }

        holder.text_date.setTypeface(typeFace);
        holder.text_date.setText(Html.fromHtml(description));

        holder.open.setTypeface(typeFace);
        holder.open.setText(Html.fromHtml("&#"+Environment.IcoEdit+";"));
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnOpenMeetingsClickListener) {
                    ((OnOpenMeetingsClickListener) mContext).onOpenMeetingsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setTypeface(typeFace);
        holder.delete.setText(Html.fromHtml("&#"+Environment.IcoDelete+";"));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnDeleteMeetingsClickListener) {
                    ((OnDeleteMeetingsClickListener) mContext).onDeleteMeetingsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.files.setTypeface(typeFace);
        holder.files.setText(Html.fromHtml("&#" + Environment.IcoFiles + ";"));
        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnFilesMeetingsClickListener) {
                    ((OnFilesMeetingsClickListener) mContext).onFilesMeetingsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.report.setTypeface(typeFace);
        holder.report.setText(Html.fromHtml("&#"+Environment.IcoReport+";"));
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnReportMeetingsClickListener) {
                    ((OnReportMeetingsClickListener) mContext).onReportMeetingsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof OnRootMeetingsClickListener) {
                    ((OnRootMeetingsClickListener) mContext).onRootMeetingsClick(holder.getAdapterPosition());
                }
            }
        });

        holder.report.setEnabled(data.getCompleted());

        if (!data.getCustomReportId().equals("")) {
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.report.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.button_selector_dark));
            } else {
                holder.report.setBackground(ContextCompat.getDrawable(mContext, R.drawable.button_selector_dark));
            }
        } else {
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.report.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.button_selector));
            } else {
                holder.report.setBackground(ContextCompat.getDrawable(mContext, R.drawable.button_selector));
            }
        }

        if (isDashboard) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.files.setVisibility(View.GONE);
        } else {
            holder.open.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.files.setVisibility(View.VISIBLE);
        }

        if (!edit) {
            holder.open.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        else
            return 0;
    }

    public void setData(ArrayList<MeetingData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public MeetingData getDataItem(int position) {
        return mData.get(position);
    }
}