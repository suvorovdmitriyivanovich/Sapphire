package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ProfileData;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GetProfilesAction extends AsyncTask{

    public interface RequestProfiles {
        public void onRequestProfiles(String result);
    }

    public interface RequestProfilesData {
        public void onRequestProfilesData(ProfileData profileData);
    }

    private Context mContext;
    private ProfileData profileData;

    public GetProfilesAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String urlstring = Environment.SERVER + Environment.ProfilesURL + "?$filter=ProfileId%20eq%20guid'"+userInfo.getProfileId()+"'";

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", userInfo.getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            if (data.length() == 1) {
                try {
                    profileData = new ProfileData(data.getJSONObject(0));
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
            if (resultData.equals("OK")) {
                ((RequestProfilesData) mContext).onRequestProfilesData(profileData);
            } else {
                ((RequestProfiles) mContext).onRequestProfiles(resultData);
            }
        }
    }
}
