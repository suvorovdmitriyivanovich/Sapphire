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
import com.sapphire.models.TaskData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class LinkAddAction extends AsyncTask{

    public interface RequestLinkAdd {
        public void onRequestLinkAdd(String result);
    }

    private Context mContext;
    private TaskData data = new TaskData();
    private String taskId = "";
    private String linkId = "";
    private String linkTypeId = "";

    public LinkAddAction(Context context, String taskId, String linkId, String linkTypeId) {
        this.mContext = context;
        this.taskId = taskId;
        this.linkId = linkId;
        this.linkTypeId = linkTypeId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.TaskManagementLinksURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("TaskId", taskId);
            jsonObject.put("LinkId", linkId);
            jsonObject.put("LinkTypeId", linkTypeId);

            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,"POST", userInfo.getAuthToken()));

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
            ((RequestLinkAdd) mContext).onRequestLinkAdd(resultData);
        }
    }
}