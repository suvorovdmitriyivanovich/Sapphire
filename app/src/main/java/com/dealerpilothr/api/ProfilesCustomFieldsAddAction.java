package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.models.ResponseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfilesCustomFieldsAddAction extends AsyncTask{

    public interface RequestProfilesCustomFieldsAdd {
        public void onRequestProfilesCustomFieldsAdd(String result);
    }

    private Context mContext;
    private ProfileData data = new ProfileData();

    public ProfilesCustomFieldsAddAction(Context context, ProfileData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.ProfilesCustomFieldsURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfileId", data.getProfileId());
            jsonObject.put("CustomField1", data.getCustomField1());
            jsonObject.put("CustomField2", data.getCustomField2());
            jsonObject.put("CustomField3", data.getCustomField3());
            jsonObject.put("CustomField4", data.getCustomField4());
            jsonObject.put("CustomField5", data.getCustomField5());
            jsonObject.put("CustomField6", data.getCustomField6());
            jsonObject.put("CustomField7", data.getCustomField7());
            jsonObject.put("CustomField8", data.getCustomField8());
            jsonObject.put("CustomField9", data.getCustomField9());
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
            ((RequestProfilesCustomFieldsAdd) mContext).onRequestProfilesCustomFieldsAdd(resultData);
        }
    }
}
