package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.LinkTaskData;
import com.sapphire.models.ResponseData;
import com.sapphire.models.WorkplaceInspectionItemData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class TaskManagementLinksAction extends AsyncTask{

    public interface RequestTaskManagementLinks {
        public void onRequestTaskManagementLinks(String result, ArrayList<LinkTaskData> datas);
    }

    private Context mContext;
    private ArrayList<LinkTaskData> datas = new ArrayList<LinkTaskData>();
    private ArrayList<WorkplaceInspectionItemData> itemDatas = new ArrayList<WorkplaceInspectionItemData>();

    public TaskManagementLinksAction(Context context, ArrayList<WorkplaceInspectionItemData> itemDatas) {
        this.mContext = context;
        this.itemDatas = itemDatas;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        String models = "";
        for (WorkplaceInspectionItemData item : itemDatas) {
            if (item.getWorkplaceInspectionItemId().equals("")) {
                continue;
            }
            if (!models.equals("")) {
                models = models + "&";
            }
            models = models + "model%5B%5D=" + item.getWorkplaceInspectionItemId();
        }

        if (models.equals("")) {
            return "OK";
        }

        String urlstring = Environment.SERVER + Environment.TaskManagementLinksURL + "?" + models;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            datas = new ArrayList<LinkTaskData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    datas.add(new LinkTaskData(data.getJSONObject(y)));
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
            ((RequestTaskManagementLinks) mContext).onRequestTaskManagementLinks(resultData, datas);
        }
    }
}
