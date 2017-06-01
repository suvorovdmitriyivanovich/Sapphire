package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.MemberData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.WorkplaceInspectionData;
import com.dealerpilothr.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class WorkplaceInspectionAddAction extends AsyncTask{

    public interface RequestWorkplaceInspectionAdd {
        public void onRequestWorkplaceInspectionAdd(String result);
    }

    public interface RequestWorkplaceInspectionAddData {
        public void onRequestWorkplaceInspectionAddData(WorkplaceInspectionData workplaceInspectionData);
    }

    private Context mContext;
    private String workplaceInspectionId;
    private String name;
    private String description;
    private String date = "";
    private boolean posted = false;
    private WorkplaceInspectionData workplaceInspectionData = new WorkplaceInspectionData();
    private ArrayList<MemberData> datasTeam;
    private int type = 0;

    public WorkplaceInspectionAddAction(Context context, String workplaceInspectionId, String name, String description, Long dateLong, boolean posted, ArrayList<MemberData> datasTeam, int type) {
        this.mContext = context;
        this.workplaceInspectionId = workplaceInspectionId;
        this.name = name;
        this.description = description;
        this.posted = posted;
        this.datasTeam = datasTeam;
        this.type = type;

        if (dateLong != 0l) {
            this.date = DateOperations.getDateServer(dateLong);
        }
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionsURL;

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!workplaceInspectionId.equals("")) {
                jsonObject.put("WorkplaceInspectionId", workplaceInspectionId);
            }
            jsonObject.put("Name", name);
            jsonObject.put("Description", description);
            jsonObject.put("Date", date);
            jsonObject.put("PostedOnBoard", posted);
            //jsonObject.put("OrganizationId", posted);
            if (type == 1) {
                jsonObject.put("Completed", false);
                jsonObject.put("Inspected", false);
            } else if (type == 2) {
                jsonObject.put("Completed", true);
                jsonObject.put("Inspected", true);
            }

            JSONArray jsonArrayProfile = new JSONArray();
            for (MemberData item: datasTeam) {
                //if (!item.getPresence()) {
                //    continue;
                //}
                JSONObject jsonObjectProfile = new JSONObject();
                jsonObjectProfile.put("WorkplaceInspectionProfileId", item.getWorkplaceInspectionProfileId());
                jsonObjectProfile.put("WorkplaceInspectionId", item.getWorkplaceInspectionId());
                jsonObjectProfile.put("ProfileId", item.getProfile().getProfileId());
                jsonObjectProfile.put("Name", item.getName());
                jsonArrayProfile.put(jsonObjectProfile);
            }
            if (jsonArrayProfile.length() != 0) {
                jsonObject.put("Profiles", jsonArrayProfile);
            }

            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!workplaceInspectionId.equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            try {
                workplaceInspectionData = new WorkplaceInspectionData(responseData.getData().getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (workplaceInspectionData.getWorkplaceInspectionId().equals("")) {
                result = Dealerpilothr.getInstance().getResources().getString(R.string.unknown_error);
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
            if (resultData.equals("OK")) {
                ((RequestWorkplaceInspectionAddData) mContext).onRequestWorkplaceInspectionAddData(workplaceInspectionData);
            } else {
                ((RequestWorkplaceInspectionAdd) mContext).onRequestWorkplaceInspectionAdd(resultData);
            }
        }
    }
}
