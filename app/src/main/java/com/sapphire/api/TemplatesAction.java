package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.TemplateData;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class TemplatesAction extends AsyncTask{

    public interface RequestTemplates {
        public void onRequestTemplates(String result);
    }

    public interface RequestTemplatesData {
        public void onRequestTemplatesData(ArrayList<TemplateData> coursesDatas);
    }

    private Context mContext;
    private ArrayList<TemplateData> templatesDatas;

    public TemplatesAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionTemplatesURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            templatesDatas = new ArrayList<TemplateData>();
            templatesDatas.add(new TemplateData("Workplace Inspection Templates"));
            for (int y=0; y < data.length(); y++) {
                try {
                    TemplateData templateData = new TemplateData(data.getJSONObject(y));
                    templatesDatas.get(0).getSubTemplates().add(templateData);
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
                ((RequestTemplatesData) mContext).onRequestTemplatesData(templatesDatas);
            } else {
                ((RequestTemplates) mContext).onRequestTemplates(resultData);
            }
        }
    }
}
