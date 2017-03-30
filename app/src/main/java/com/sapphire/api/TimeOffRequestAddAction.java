package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.ResponseData;
import com.sapphire.models.TimeOffRequestData;
import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class TimeOffRequestAddAction extends AsyncTask{

    public interface RequestTimeOffRequestAdd {
        public void onRequestTimeOffRequestAdd(String result, TimeOffRequestData data);
    }

    private Context mContext;
    private String id;
    private String name;
    private String description;
    private String date = "";
    private boolean posted = false;
    private TimeOffRequestData data = new TimeOffRequestData();

    public TimeOffRequestAddAction(Context context, String id, String name, String description, Long dateLong, boolean posted) {
        this.mContext = context;
        this.id = id;
        this.name = name;
        this.description = description;
        this.posted = posted;

        if (dateLong != 0l) {
            this.date = DateOperations.getDateServer(dateLong);
        }
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.TimeBanksURL;

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!id.equals("")) {
                jsonObject.put("id", id);
            }
            jsonObject.put("Name", name);
            jsonObject.put("Description", description);
            jsonObject.put("Date", date);
            jsonObject.put("PostedOnBoard", posted);
            //jsonObject.put("OrganizationId", posted);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!id.equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            try {
                data = new TimeOffRequestData(responseData.getData().getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (data.getWorkplaceInspectionId().equals("")) {
                result = Sapphire.getInstance().getResources().getString(R.string.unknown_error);
            } else {
                result = "OK";
            }
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
            ((RequestTimeOffRequestAdd) mContext).onRequestTimeOffRequestAdd(resultData, data);
        }
    }
}
