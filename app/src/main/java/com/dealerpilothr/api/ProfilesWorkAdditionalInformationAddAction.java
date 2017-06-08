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

public class ProfilesWorkAdditionalInformationAddAction extends AsyncTask{

    public interface RequestProfilesWorkAdditionalInformationAdd {
        public void onRequestProfilesWorkAdditionalInformationAdd(String result);
    }

    private Context mContext;
    private ProfileData data = new ProfileData();

    public ProfilesWorkAdditionalInformationAddAction(Context context, ProfileData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.ProfilesAdditionalInformationURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfileId", data.getProfileId());
            jsonObject.put("VSRNumber", data.getVSRNumber());
            jsonObject.put("VSRNumberExpire", data.getVSRNumberExpireServer());
            jsonObject.put("TechLicenseNumber", data.getTechLicenseNumber());
            jsonObject.put("TechLicenseNumberExpire", data.getTechLicenseNumberExpireServer());
            jsonObject.put("UniformDescription", data.getUniformDescription());
            jsonObject.put("UniformAllowance", data.getUniformAllowance());
            jsonObject.put("UniformAllowanceAmount", data.getUniformAllowanceAmount());
            jsonObject.put("UniformRenewalDate", data.getUniformRenewalDateServer());
            jsonObject.put("WorkPermitNumber", data.getWorkPermitNumber());
            jsonObject.put("WorkPermitNumberExpire", data.getWorkPermitNumberExpireServer());
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
            ((RequestProfilesWorkAdditionalInformationAdd) mContext).onRequestProfilesWorkAdditionalInformationAdd(resultData);
        }
    }
}
