package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.LinkTaskData;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.models.WorkplaceInspectionItemData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class TaskManagementLinksAction extends AsyncTask{

    public interface RequestTaskManagementLinks {
        public void onRequestTaskManagementLinks(String result, ArrayList<LinkTaskData> datas, String parentId, int type);
    }

    private Context mContext;
    private ArrayList<LinkTaskData> datas = new ArrayList<LinkTaskData>();
    private ArrayList<WorkplaceInspectionItemData> itemDatas = new ArrayList<WorkplaceInspectionItemData>();
    private String id = "";
    private String parentId = "";
    private int type = 0;

    public TaskManagementLinksAction(Context context, ArrayList<WorkplaceInspectionItemData> itemDatas, String id, int type) {
        this.mContext = context;
        this.itemDatas = itemDatas;
        this.id = id;
        this.type = type;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }

        String result = "";
        String urlstring = "";
        ResponseData responseData = null;

        if (type == 1) {
            urlstring = Environment.SERVER + Environment.TaskManagementLinksURL + "?model%5B%5D=" + id;

            responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring, "", 0, true, "GET", UserInfo.getUserInfo().getAuthToken()));

            if (responseData.getSuccess()) {
                JSONArray data = responseData.getData();
                //datas = new ArrayList<LinkTaskData>();
                for (int y = 0; y < data.length(); y++) {
                    try {
                        LinkTaskData linkTaskData = new LinkTaskData(data.getJSONObject(y));
                        //datas.add(linkTaskData);
                        parentId = linkTaskData.getTask().getTaskId();
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
                    for (int y = 0; y < errorMessageDatas.size(); y++) {
                        if (!result.equals("")) {
                            result = result + ". ";
                        }
                        result = errorMessageDatas.get(y).getName();
                    }
                }
            }
        } else {
            String models = "";
            for (WorkplaceInspectionItemData item : itemDatas) {
                if (item.getWorkplaceInspectionItemId().equals("") || !item.getStatus().getWorkplaceInspectionItemStatusId().equals(Environment.StatusFail)) {
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

            urlstring = Environment.SERVER + Environment.TaskManagementLinksURL + "?" + models;

            responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring, "", 0, true, "GET", UserInfo.getUserInfo().getAuthToken()));

            if (responseData.getSuccess()) {
                JSONArray data = responseData.getData();
                datas = new ArrayList<LinkTaskData>();
                for (int y = 0; y < data.length(); y++) {
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
                    for (int y = 0; y < errorMessageDatas.size(); y++) {
                        if (!result.equals("")) {
                            result = result + ". ";
                        }
                        result = errorMessageDatas.get(y).getName();
                    }
                }
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            ((RequestTaskManagementLinks) mContext).onRequestTaskManagementLinks(resultData, datas, parentId, type);
        }
    }
}
