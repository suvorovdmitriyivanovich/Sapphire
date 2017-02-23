package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;

import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.ItemPriorityData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.TemplateData;
import com.sapphire.logic.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ItemPrioritiesAction extends AsyncTask{

    public interface RequestItemPriorities {
        public void onRequestItemPriorities(String result);
    }

    public interface RequestItemPrioritiesData {
        public void onRequestItemPrioritiesData(ArrayList<ItemPriorityData> itemPriorityDatas);
    }

    private Context mContext;
    private ArrayList<ItemPriorityData> itemPriorityDatas;

    public ItemPrioritiesAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionsItemPrioritiesURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            itemPriorityDatas = new ArrayList<ItemPriorityData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    itemPriorityDatas.add(new ItemPriorityData(data.getJSONObject(y)));
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
                ((RequestItemPrioritiesData) mContext).onRequestItemPrioritiesData(itemPriorityDatas);
            } else {
                ((RequestItemPriorities) mContext).onRequestItemPriorities(resultData);
            }
        }
    }
}
