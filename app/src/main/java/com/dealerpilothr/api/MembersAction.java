package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class MembersAction extends AsyncTask{

    public interface RequestMembers {
        public void onRequestMembers(String result, ArrayList<ProfileData> datas);
    }

    private Context mContext;
    private ArrayList<ProfileData> datas;

    public MembersAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.MembersURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET",UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            datas = new ArrayList<ProfileData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    datas.add(new ProfileData(data.getJSONObject(y)));
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
            ((RequestMembers) mContext).onRequestMembers(resultData, datas);
        }
    }
}
