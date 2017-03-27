package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.MeetingData;
import com.sapphire.models.MemberData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ResponseData;
import com.sapphire.models.TopicData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MeetingAddAction extends AsyncTask{

    public interface RequestMeetingAdd {
        public void onRequestMeetingAdd(String result);
    }

    private Context mContext;
    private MeetingData data = new MeetingData();

    public MeetingAddAction(Context context, MeetingData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.MeetingsURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!data.getMeetingId().equals("")) {
                jsonObject.put("MeetingId", data.getMeetingId());
            }
            jsonObject.put("Name", data.getName());
            jsonObject.put("Description", data.getDescription());
            jsonObject.put("MeetingDate", data.getMeetingDateServer());
            jsonObject.put("EndTime", data.getEndTimeServer());
            jsonObject.put("Location", data.getLocation());
            jsonObject.put("Posted", data.getPosted());
            jsonObject.put("Completed", data.getCompleted());
            jsonObject.put("OrganizationId", userInfo.getCurrentOrganization().getOrganizationId());

            JSONArray jsonArrayMember = new JSONArray();
            for (MemberData item: data.getMembers()) {
                JSONObject jsonObjectMember = new JSONObject();
                jsonObjectMember.put("MeetingMemberId", item.getMeetingMemberId());
                //if (!data.getMeetingId().equals("")) {
                //    jsonObjectMember.put("MeetingId", data.getMeetingId());
                //}
                jsonObjectMember.put("Presence", item.getPresence());
                jsonObjectMember.put("Profile", item.getProfileJSONObject());
                jsonArrayMember.put(jsonObjectMember);
            }
            jsonObject.put("Members", jsonArrayMember);

            JSONArray jsonArrayTopic = new JSONArray();
            for (TopicData item: data.getTopics()) {
                JSONObject jsonObjectTopic = new JSONObject();
                jsonObjectTopic.put("MeetingTopicId", item.getMeetingTopicId());
                //if (!data.getMeetingId().equals("")) {
                //    jsonObjectTopic.put("MeetingId", data.getMeetingId());
                //}
                jsonObjectTopic.put("Name", item.getName());
                jsonObjectTopic.put("Description", item.getDescription());
                jsonArrayTopic.put(jsonObjectTopic);
            }
            jsonObject.put("Topics", jsonArrayTopic);

            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!data.getMeetingId().equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, userInfo.getAuthToken()));

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
            ((RequestMeetingAdd) mContext).onRequestMeetingAdd(resultData);
        }
    }
}
