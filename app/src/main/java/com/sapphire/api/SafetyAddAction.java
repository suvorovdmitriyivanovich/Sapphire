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
import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SafetyAddAction extends AsyncTask{

    public interface RequestSafetyAdd {
        public void onRequestSafetyAdd(String result, SafetyData data);
    }

    private Context mContext;
    private String id;
    private String name;
    private String supplier;
    private String notes;
    private String date = "";
    private String fileId = "";
    private SafetyData data = new SafetyData();

    public SafetyAddAction(Context context, String id, String name, String supplier, String notes, Long dateLong, String fileId) {
        this.mContext = context;
        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.notes = notes;
        this.fileId = fileId;

        if (dateLong != 0l) {
            this.date = DateOperations.getDateServer(dateLong);
        }
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.SafetisURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!id.equals("")) {
                jsonObject.put("FileId", fileId);
                jsonObject.put("SafetyDataSheetId", id);
                jsonObject.put("OrganizationId", userInfo.getCurrentOrganization().getOrganizationId());
                jsonObject.put("UploadDate",  DateOperations.getDateServer(System.currentTimeMillis()));
            }
            jsonObject.put("Name", name);
            jsonObject.put("Notes", notes);
            jsonObject.put("RenewalDate", date);
            jsonObject.put("Supplier", supplier);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!id.equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, userInfo.getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            try {
                data = new SafetyData(responseData.getData().getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (data.getSafetyDataSheetId().equals("")) {
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
            ((RequestSafetyAdd) mContext).onRequestSafetyAdd(resultData, data);
        }
    }
}
