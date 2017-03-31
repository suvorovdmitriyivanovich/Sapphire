package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.DayData;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.OrganizationStructureData;
import com.sapphire.models.ResponseData;
import com.sapphire.models.TimeOffRequestData;
import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class TimeOffRequestAddAction extends AsyncTask{

    public interface RequestTimeOffRequestAdd {
        public void onRequestTimeOffRequestAdd(String result, TimeOffRequestData data);
    }

    private Context mContext;
    private TimeOffRequestData data = new TimeOffRequestData();

    public TimeOffRequestAddAction(Context context, TimeOffRequestData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String urlstring = Environment.SERVER + Environment.TimeOffRequestsURL;

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!data.getTimeOffRequestId().equals("")) {
                jsonObject.put("TimeoffRequestId", data.getTimeOffRequestId());
            }
            //jsonObject.put("OrganizationId", userInfo.getCurrentOrganization().getOrganizationId());
            jsonObject.put("ProfileId", userInfo.getProfile().getProfileId());
            //jsonObject.put("TimeOffRequestStatusId", "");
            //jsonObject.put("TimeBankName", data.getTimeBank().getName());
            jsonObject.put("TimeBankAccountId", data.getTimeBank().getTimeBankAccountId());
            //jsonObject.put("EmployeeName", "");
            //jsonObject.put("Department", "");
            //jsonObject.put("RequestDate", "");
            jsonObject.put("AttendanceCodeId", data.getAttendanceCode().getAttendanceCodeId());

            JSONArray jsonArrayDays = new JSONArray();

            for (DayData item: data.getDays()) {
                JSONObject jsonObjectDay = new JSONObject();
                try {
                    if (!data.getTimeOffRequestId().equals("")) {
                        jsonObjectDay.put("TimeoffRequestDayId", data.getTimeOffRequestId());
                    }
                    if (item.getDateString().equals("")) {
                        jsonObjectDay.put("TimeoffRequestId", item.getDateString());
                    }
                    jsonObjectDay.put("Date", item.getDateServer());
                    jsonObjectDay.put("Ammount", 0);

                    jsonArrayDays.put(jsonObjectDay);

                    jsonObject.put("Days", jsonArrayDays);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!data.getTimeOffRequestId().equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonObject.toString(),0,true,method, UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            try {
                data = new TimeOffRequestData(responseData.getData().getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (data.getTimeOffRequestId().equals("")) {
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
            ((RequestTimeOffRequestAdd) mContext).onRequestTimeOffRequestAdd(resultData, data);
        }
    }
}
