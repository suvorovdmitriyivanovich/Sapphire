package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.InvestigationData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvestigationAddAction extends AsyncTask{

    public interface RequestInvestigationAdd {
        public void onRequestInvestigationAdd(String result, InvestigationData data);
    }

    private Context mContext;
    private String id;
    private String name;
    private String description;
    private String date = "";
    private InvestigationData data = new InvestigationData();
    //private boolean me = false;

    public InvestigationAddAction(Context context, String id, String name, String description, Long dateLong) {
        this.mContext = context;
        this.id = id;
        this.name = name;
        this.description = description;
        //this.me = me;

        if (dateLong != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date thisdaten = new Date();
                thisdaten.setTime(dateLong);
                String datet = format.format(thisdaten);
                this.date = datet;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.InvestigationsURL;
        /*
        if (me) {
            urlstring = urlstring + "/AddToProfile";
        }
        */

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!id.equals("")) {
                jsonObject.put("InvestigationId", id);
            }
            jsonObject.put("Name", name);
            jsonObject.put("Description", description);
            jsonObject.put("Date", date);
            //jsonObject.put("OrganizationId", posted);
            /*
            if (me) {
                jsonObject.put("ProfileId", userInfo.getProfile().getProfileId());
            }
            */
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!id.equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, userInfo.getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            try {
                data = new InvestigationData(responseData.getData().getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (data.getInvestigationId().equals("")) {
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
            ((RequestInvestigationAdd) mContext).onRequestInvestigationAdd(resultData, data);
        }
    }
}
