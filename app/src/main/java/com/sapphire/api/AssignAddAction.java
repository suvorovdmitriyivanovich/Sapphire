package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.ProfileData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AssignAddAction extends AsyncTask{

    public interface RequestAssignAdd {
        public void onRequestAssignAdd(String result);
    }

    private Context mContext;
    private String id;
    private ArrayList<ProfileData> profileDatas;

    public AssignAddAction(Context context, String id, ArrayList<ProfileData> profileDatas) {
        this.mContext = context;
        this.id = id;
        this.profileDatas = profileDatas;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.AssignURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("TaskId", id);

            for (ProfileData item: profileDatas) {
                if (!item.getPresence()) {
                    continue;
                }
                JSONObject jsonObjectAssignment = new JSONObject();
                jsonObjectAssignment.put("TaskId", id);
                jsonObjectAssignment.put("ProfileId", item.getProfileId());
                jsonObjectAssignment.put("Name", item.getName());
                jsonObjectAssignment.put("TaskProfileId", null);

                jsonArray.put(jsonObjectAssignment);
            }

            jsonObject.put("Assignments", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        //if (!data.getTaskId().equals("")) {
        //    method = "PUT";
        //}

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonObject.toString(),0,true,method, userInfo.getAuthToken()));

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
            ((RequestAssignAdd) mContext).onRequestAssignAdd(resultData);
        }
    }
}