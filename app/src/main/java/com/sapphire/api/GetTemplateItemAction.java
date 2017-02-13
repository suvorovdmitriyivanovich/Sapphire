package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.TemplateItemData;
import com.sapphire.logic.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GetTemplateItemAction extends AsyncTask{

    public interface RequestTemplateItem {
        public void onRequestTemplateItem(String result);
    }

    public interface RequestTemplateItemData {
        public void onRequestTemplateItemData(ArrayList<TemplateItemData> templateItemDatas);
    }

    private Context mContext;
    private ArrayList<TemplateItemData> templateItemDatas;
    private String templateItemId = "";

    public GetTemplateItemAction(Context context, String templateItemId) {
        this.mContext = context;
        this.templateItemId = templateItemId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionTemplateItemsURL + "?$filter=WorkplaceInspectionTemplateItemId%20eq%20guid'"+templateItemId+"'";;

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
                ((RequestTemplateItemData) mContext).onRequestTemplateItemData(templateItemDatas);
            } else {
                ((RequestTemplateItem) mContext).onRequestTemplateItem(resultData);
            }
        }
    }
}
