package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.ProfileData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GetHealthAndSafetyMemberAction extends AsyncTask{

    public interface RequestHealthAndSafetyMember {
        public void onRequestHealthAndSafetyMember(String result, ProfileData profileData);
    }

    private Context mContext;
    private ProfileData profileData = new ProfileData();

    public GetHealthAndSafetyMemberAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String filter = "?$filter=ProfileId%20eq%20guid'"+userInfo.getProfile().getProfileId()+"'";

        String urlstring = Environment.SERVER + Environment.HealthAndSafetyMemberURL + filter;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", userInfo.getAuthTokenFirst()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            for (int y=0; y < data.length(); y++) {
                try {
                    profileData = new ProfileData(data.getJSONObject(y));
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
            ((RequestHealthAndSafetyMember) mContext).onRequestHealthAndSafetyMember(resultData, profileData);
        }
    }
}
