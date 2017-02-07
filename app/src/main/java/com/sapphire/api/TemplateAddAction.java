package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.db.DBHelper;
import com.sapphire.logic.AccountData;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NavigationMenuData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TemplateAddAction extends AsyncTask{

    public interface RequestTemplateAdd {
        public void onRequestTemplateAdd(String result);
    }

    private Context mContext;
    private String workplaceInspectionTemplateId;
    private String name;
    private String description;

    public TemplateAddAction(Context context, String workplaceInspectionTemplateId, String name, String description) {
        this.mContext = context;
        this.workplaceInspectionTemplateId = workplaceInspectionTemplateId;
        this.name = name;
        this.description = description;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionTemplatesURL;

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!workplaceInspectionTemplateId.equals("")) {
                jsonObject.put("WorkplaceInspectionTemplateId", workplaceInspectionTemplateId);
            }
            jsonObject.put("Name", name);
            jsonObject.put("Description", description);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!workplaceInspectionTemplateId.equals("")) {
            method = "PUT";
        }

        SharedPreferences sPref = mContext.getSharedPreferences("GlobalPreferences", mContext.MODE_PRIVATE);

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method,sPref.getString("AUTHTOKEN","")));

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
            ((RequestTemplateAdd) mContext).onRequestTemplateAdd(resultData);
        }
    }
}
