package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.TemplateItemData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GetTemplateAction extends AsyncTask{

    public interface RequestTemplate {
        public void onRequestTemplate(String result);
    }

    public interface RequestTemplateData {
        public void onRequestTemplateData(ArrayList<TemplateItemData> templateItemDatas);
    }

    private Context mContext;
    private ArrayList<TemplateItemData> templateItemDatas;
    private String templateId = "";
    private String typeId;

    public GetTemplateAction(Context context, String templateId, String typeId) {
        this.mContext = context;
        this.templateId = templateId;
        this.typeId = typeId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER;
        if (typeId.equals(Sapphire.getInstance().getResources().getString(R.string.text_meetings_templates))) {
            urlstring = urlstring + Environment.TopicTemplateItemsURL + "?$filter=MeetingTopicTemplateId%20eq%20guid'"+templateId+"'";
        } else {
            urlstring = urlstring + Environment.WorkplaceInspectionTemplateItemsURL + "?$filter=WorkplaceInspectionTemplateId%20eq%20guid'"+templateId+"'";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            templateItemDatas = new ArrayList<TemplateItemData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    TemplateItemData templateItemData = new TemplateItemData(data.getJSONObject(y));
                    templateItemDatas.add(templateItemData);
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
                ((RequestTemplateData) mContext).onRequestTemplateData(templateItemDatas);
            } else {
                ((RequestTemplate) mContext).onRequestTemplate(resultData);
            }
        }
    }
}
