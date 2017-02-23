package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.UserInfo;
import com.sapphire.logic.WorkplaceInspectionItemData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class WorkplaceInspectionItemAddAction extends AsyncTask{

    public interface RequestWorkplaceInspectionItemAdd {
        public void onRequestWorkplaceInspectionItemAdd(String result, boolean neddclosepd, int ihms);
    }

    private Context mContext;
    private WorkplaceInspectionItemData workplaceInspectionItemData;
    private boolean neddclosepd;
    private int iData;

    public WorkplaceInspectionItemAddAction(Context context, WorkplaceInspectionItemData workplaceInspectionItemData, boolean neddclose, int i) {
        this.mContext = context;
        this.workplaceInspectionItemData = workplaceInspectionItemData;
        this.neddclosepd = neddclose;
        this.iData = i+1;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionItemsURL;

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!workplaceInspectionItemData.getWorkplaceInspectionItemId().equals("")) {
                jsonObject.put("WorkplaceInspectionItemId", workplaceInspectionItemData.getWorkplaceInspectionItemId());
            }
            jsonObject.put("WorkplaceInspectionId", workplaceInspectionItemData.getWorkplaceInspectionId());
            jsonObject.put("Name", workplaceInspectionItemData.getName());
            jsonObject.put("Description", workplaceInspectionItemData.getDescription());
            jsonObject.put("Severity", workplaceInspectionItemData.getSeverity());
            if (!workplaceInspectionItemData.getStatus().getWorkplaceInspectionItemStatusId().equals("")) {
                JSONObject jsonObjectStatus = new JSONObject();
                jsonObjectStatus.put("WorkplaceInspectionItemStatusId", workplaceInspectionItemData.getStatus().getWorkplaceInspectionItemStatusId());
                jsonObject.put("Status", jsonObjectStatus);
            }
            if (!workplaceInspectionItemData.getPriority().getWorkplaceInspectionItemPriorityId().equals("")) {
                JSONObject jsonObjectPriority = new JSONObject();
                jsonObjectPriority.put("WorkplaceInspectionItemPriorityId", workplaceInspectionItemData.getPriority().getWorkplaceInspectionItemPriorityId());
                jsonObject.put("Priority", jsonObjectPriority);
            }
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!workplaceInspectionItemData.getWorkplaceInspectionItemId().equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(), 0, true, method, UserInfo.getUserInfo().getAuthToken()));

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
            ((RequestWorkplaceInspectionItemAdd) mContext).onRequestWorkplaceInspectionItemAdd(resultData, neddclosepd, iData);
        }
    }
}
