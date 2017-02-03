package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.TemplateData;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class GetTemplateAction extends AsyncTask{

    public interface RequestTemplate {
        public void onRequestTemplate(String result);
    }

    public interface RequestTemplateData {
        public void onRequestTemplateData(ArrayList<TemplateData> templateDatas);
    }

    private Context mContext;
    private ArrayList<TemplateData> templateDatas;
    private String templateId = "";

    public GetTemplateAction(Context context, String templateId) {
        this.mContext = context;
        this.templateId = templateId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionTemplateItemsURL + "?$filter=WorkplaceInspectionTemplateId%20eq%20guid'"+templateId+"'";;

        SharedPreferences sPref = mContext.getSharedPreferences("GlobalPreferences", mContext.MODE_PRIVATE);

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET",sPref.getString("AUTHTOKEN","")));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            //templatesDatas = new ArrayList<TemplateData>();
            //templatesDatas.add(new TemplateData("Workplace Inspection Templates"));
            for (int y=0; y < data.length(); y++) {
                try {
                    TemplateData templateData = new TemplateData(data.getJSONObject(y));
                    //templatesDatas.get(0).getSubTemplates().add(templateData);
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
                ((RequestTemplateData) mContext).onRequestTemplateData(templateDatas);
            } else {
                ((RequestTemplate) mContext).onRequestTemplate(resultData);
            }
        }
    }
}
