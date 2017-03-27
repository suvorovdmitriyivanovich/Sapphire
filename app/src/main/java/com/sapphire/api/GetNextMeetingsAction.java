package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetNextMeetingsAction extends AsyncTask{

    public interface RequestNextMeetings {
        public void onRequestNextMeetings(String result, String nextDate);
    }

    private Context mContext;
    private Long dateCur;
    private String nextDate = "-";

    public GetNextMeetingsAction(Context context, Long dateCur) {
        this.mContext = context;
        this.dateCur = dateCur;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        String dateCurString = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date thisdate = new Date();
            if (dateCur != 0l) {
                thisdate.setTime(dateCur);
            }
            dateCurString = format.format(thisdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String urlstring = Environment.SERVER + Environment.MeetingsURL + "/Next?date="+dateCurString;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            if (data.length() == 1) {
                try {
                    format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date newdate = format.parse(data.getString(0));
                    format = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");
                    nextDate = format.format(newdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            result = "OK";
        } else {
            ArrayList<ErrorMessageData> errorMessageDatas = responseData.getErrorMessages();
            if (errorMessageDatas == null || errorMessageDatas.size() == 0) {
                result = responseData.getHttpStatusMessage();
            } else {
                for (int y=0; y < errorMessageDatas.size(); y++) {
                    if (!result.equals("")) {
                        result = result + ". ";
                    }
                    result = errorMessageDatas.get(y).getName();
                }
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            ((RequestNextMeetings) mContext).onRequestNextMeetings(resultData, nextDate);
        }
    }
}
