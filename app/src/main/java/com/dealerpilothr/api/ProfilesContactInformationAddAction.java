package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ProfilesContactInformationAddAction extends AsyncTask{

    public interface RequestProfilesContactInformationAdd {
        public void onRequestProfilesContactInformationAdd(String result);
    }

    private Context mContext;
    private ProfileData data = new ProfileData();

    public ProfilesContactInformationAddAction(Context context, ProfileData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.ProfilesContactInformationURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfileId", data.getProfileId());
            jsonObject.put("HomePhoneNumber", data.getHomePhoneNumber());
            jsonObject.put("CellPhoneNumber", data.getCellPhoneNumber());
            jsonObject.put("PrimaryEmail", data.getEmail());
            jsonObject.put("SecondaryEmail", data.getSecondaryEmail());
            jsonObject.put("PrimaryEmailAllowNotification", data.getPrimaryEmailAllowNotification());
            jsonObject.put("SecondaryEmailAllowNotification", data.getSecondaryEmailAllowNotification());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "PUT";

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, userInfo.getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
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
            ((RequestProfilesContactInformationAdd) mContext).onRequestProfilesContactInformationAdd(resultData);
        }
    }
}
