package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class TemplateItemAddAction extends AsyncTask{

    public interface RequestTemplateItemAdd {
        public void onRequestTemplateItemAdd(String result);
    }

    private Context mContext;
    private String templateItemId;
    private String templateId;
    private String name;
    private String description;
    private String typeId;

    public TemplateItemAddAction(Context context, String templateItemId, String templateId, String name, String description, String typeId) {
        this.mContext = context;
        this.templateItemId = templateItemId;
        this.templateId = templateId;
        this.name = name;
        this.description = description;
        this.typeId = typeId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER;
        if (typeId.equals(Dealerpilothr.getInstance().getResources().getString(R.string.text_meetings_templates))) {
            urlstring = urlstring + Environment.TopicTemplateItemsURL;
        } else {
            urlstring = urlstring + Environment.WorkplaceInspectionTemplateItemsURL;
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!templateItemId.equals("")) {
                if (typeId.equals(Dealerpilothr.getInstance().getResources().getString(R.string.text_meetings_templates))) {
                    jsonObject.put("MeetingTopicTemplateItemId", templateItemId);
                } else {
                    jsonObject.put("WorkplaceInspectionTemplateItemId", templateItemId);
                }
            }
            if (typeId.equals(Dealerpilothr.getInstance().getResources().getString(R.string.text_meetings_templates))) {
                jsonObject.put("MeetingTopicTemplateId", templateId);
            } else {
                jsonObject.put("WorkplaceInspectionTemplateId", templateId);
            }
            jsonObject.put("Name", name);
            jsonObject.put("Description", description);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!templateItemId.equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, UserInfo.getUserInfo().getAuthToken()));

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
            ((RequestTemplateItemAdd) mContext).onRequestTemplateItemAdd(resultData);
        }
    }
}
