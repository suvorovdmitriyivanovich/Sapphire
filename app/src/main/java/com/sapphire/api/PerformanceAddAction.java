package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.PerformanceData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PerformanceAddAction extends AsyncTask{

    public interface RequestPerformanceAdd {
        public void onRequestPerformanceAdd(String result);
    }

    private Context mContext;
    private PerformanceData data = new PerformanceData();

    public PerformanceAddAction(Context context, PerformanceData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.PerformanceEvaluations;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!data.getPerformanceEvaluationId().equals("")) {
                jsonObject.put("PerformanceEvaluationId", data.getPerformanceEvaluationId());
                //jsonObject.put("OrganizationId", userInfo.getCurrentOrganization().getOrganizationId());
            }
            jsonObject.put("Name", data.getName());
            jsonObject.put("DatePosted", data.getDatePostedServer());
            jsonObject.put("RenewalDate", data.getRenewalDateServer());
            jsonObject.put("ProfileId", data.getProfileId());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!data.getPerformanceEvaluationId().equals("")) {
            method = "PUT";
        }

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
            ((RequestPerformanceAdd) mContext).onRequestPerformanceAdd(resultData);
        }
    }
}
