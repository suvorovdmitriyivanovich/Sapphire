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
import com.sapphire.models.MeetingData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class MeetingsAction extends AsyncTask{

    public interface RequestMeetings {
        public void onRequestMeetings(String result, ArrayList<MeetingData> datas);
    }

    private Context mContext;
    private ArrayList<MeetingData> datas;
    private boolean isDashboard = false;

    public MeetingsAction(Context context, boolean isDashboard) {
        this.mContext = context;
        this.isDashboard = isDashboard;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.MeetingsURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            datas = new ArrayList<MeetingData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    MeetingData meetingData = new MeetingData(data.getJSONObject(y));
                    if (isDashboard) {
                        if (!meetingData.getPublished() || !meetingData.getPosted()) {
                            continue;
                        }
                    }
                    datas.add(meetingData);
                } catch (JSONException e) {
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
            ((RequestMeetings) mContext).onRequestMeetings(resultData, datas);
        }
    }
}
