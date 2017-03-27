package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.DisciplineData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DisciplineAddAction extends AsyncTask{

    public interface RequestDisciplineAdd {
        public void onRequestDisciplineAdd(String result);
    }

    private Context mContext;
    private DisciplineData data = new DisciplineData();

    public DisciplineAddAction(Context context, DisciplineData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.Disciplines;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!data.getDisciplineId().equals("")) {
                jsonObject.put("DisciplineId", data.getDisciplineId());
                //jsonObject.put("OrganizationId", userInfo.getCurrentOrganization().getOrganizationId());
            }
            jsonObject.put("Name", data.getName());
            jsonObject.put("Notes", data.getNotes());
            jsonObject.put("DatePosted", data.getDatePostedServer());
            jsonObject.put("ProfileId", data.getProfileId());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!data.getDisciplineId().equals("")) {
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
            ((RequestDisciplineAdd) mContext).onRequestDisciplineAdd(resultData);
        }
    }
}
