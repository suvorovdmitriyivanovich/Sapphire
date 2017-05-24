package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.ResponseData;
import com.sapphire.models.WorkplaceInspectionData;
import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AssignTasksAction extends AsyncTask{

    public interface RequestAssignTasks {
        public void onRequestAssignTasks(String result, WorkplaceInspectionData workplaceInspectionData);
    }

    private Context mContext;
    private WorkplaceInspectionData workplaceInspectionData = new WorkplaceInspectionData();

    public AssignTasksAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionsURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("Completed", userInfo.getWorkplaceInspection().getCompleted());
            jsonObject.put("Completed", true);
            jsonObject.put("Date", DateOperations.getDateServer(userInfo.getWorkplaceInspection().getDate()));
            jsonObject.put("Description", userInfo.getWorkplaceInspection().getDescription());
            jsonObject.put("Files", userInfo.getWorkplaceInspection().getFilesJson());
            jsonObject.put("Inspected", true);
            jsonObject.put("Items", userInfo.getWorkplaceInspection().getItemsJson());
            jsonObject.put("Name", userInfo.getWorkplaceInspection().getName());
            jsonObject.put("OrganizationId", userInfo.getCurrentOrganization().getOrganizationId());
            jsonObject.put("PostedOnBoard", userInfo.getWorkplaceInspection().getPostedOnBoard());
            jsonObject.put("Task", userInfo.getWorkplaceInspection().getTaskJson());
            jsonObject.put("WorkplaceInspectionId", userInfo.getWorkplaceInspection().getWorkplaceInspectionId());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "PUT";

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            try {
                workplaceInspectionData = new WorkplaceInspectionData(responseData.getData().getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (workplaceInspectionData.getWorkplaceInspectionId().equals("")) {
                result = Sapphire.getInstance().getResources().getString(R.string.unknown_error);
            } else {
                result = "OK";
            }
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
            ((RequestAssignTasks) mContext).onRequestAssignTasks(resultData, workplaceInspectionData);
        }
    }
}
