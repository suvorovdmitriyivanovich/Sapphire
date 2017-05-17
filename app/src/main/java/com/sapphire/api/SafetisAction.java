package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.SafetyData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class SafetisAction extends AsyncTask{

    public interface RequestSafetis {
        public void onRequestSafetis(String result, ArrayList<SafetyData> safetyDatas);
    }

    private Context mContext;
    private ArrayList<SafetyData> safetyDatas;

    public SafetisAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String authToken = userInfo.getAuthToken();

        String urlstring = Environment.SERVER + Environment.SafetisCurrentURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", authToken));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            safetyDatas = new ArrayList<SafetyData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    safetyDatas.add(new SafetyData(data.getJSONObject(y)));
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
            ((RequestSafetis) mContext).onRequestSafetis(resultData, safetyDatas);
        }
    }
}
