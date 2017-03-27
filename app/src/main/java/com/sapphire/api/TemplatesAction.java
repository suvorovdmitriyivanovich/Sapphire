package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.models.TemplateData;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class TemplatesAction extends AsyncTask{

    public interface RequestTemplates {
        public void onRequestTemplates(String result);
    }

    public interface RequestTemplatesData {
        public void onRequestTemplatesData(ArrayList<TemplateData> coursesDatas, String type);
    }

    private Context mContext;
    private ArrayList<TemplateData> templatesDatas;
    private String typeId;

    public TemplatesAction(Context context, String typeId) {
        this.mContext = context;
        this.typeId = typeId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER;
        if (typeId.equals(Sapphire.getInstance().getResources().getString(R.string.text_meetings_templates))) {
            urlstring = urlstring + Environment.TopicTemplatesURL;
        } else {
            urlstring = urlstring + Environment.WorkplaceInspectionTemplatesURL;
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            templatesDatas = new ArrayList<TemplateData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    templatesDatas.add(new TemplateData(data.getJSONObject(y)));
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
                ((RequestTemplatesData) mContext).onRequestTemplatesData(templatesDatas, typeId);
            } else {
                ((RequestTemplates) mContext).onRequestTemplates(resultData);
            }
        }
    }
}
