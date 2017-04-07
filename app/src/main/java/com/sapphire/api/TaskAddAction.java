package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.TaskData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class TaskAddAction extends AsyncTask{

    public interface RequestTaskAdd {
        public void onRequestTaskAdd(String result, String id);
    }

    private Context mContext;
    private TaskData data = new TaskData();
    private String id = "";

    public TaskAddAction(Context context, TaskData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.TasksURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!data.getTaskId().equals("")) {
                jsonObject.put("TaskId", data.getTaskId());
            }
            jsonObject.put("ParentId", data.getParentId());
            jsonObject.put("TaskTypeId", data.getTaskTypeId());
            jsonObject.put("TaskCategoryId", data.getTaskCategoryId());
            jsonObject.put("Name", data.getName());
            jsonObject.put("Description", data.getDescription());
            jsonObject.put("PlannedStartDate", data.getPlannedStartDateServer());
            jsonObject.put("PlannedFinishDate", data.getPlannedFinishDateServer());
            if (!data.getTaskId().equals("")) {
                jsonObject.put("PercentComplete", data.getPercentComplete());
            }
            jsonObject.put("Priority", data.getPriority());

            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!data.getTaskId().equals("")) {
            method = "PUT";
        }

        /*
        [{"ParentId":"b54ade39-18b2-4617-6cb6-6803c0b80698","TaskTypeId":"14eddc39-49f6-16c5-ecb5-550eb6e2083a","TaskCategoryId":"14eddc39-49f6-bba3-c245-905f49be780f","Name":"2","Description":"2","PlannedStartDate":"2017-04-08T00:15:00.681Z","PlannedFinishDate":"2017-04-10T05:00:00.000Z","Priority":4}]
        */

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, userInfo.getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            try {
                JSONObject jsonData = (JSONObject) responseData.getData().get(0);
                id = jsonData.getString("TaskId");
            } catch (JSONException e) {
                e.printStackTrace();
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
            ((RequestTaskAdd) mContext).onRequestTaskAdd(resultData, id);
        }
    }
}