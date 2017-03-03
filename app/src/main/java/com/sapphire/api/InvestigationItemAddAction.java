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
import com.sapphire.logic.InvestigationItemData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class InvestigationItemAddAction extends AsyncTask{

    public interface RequestInvestigationItemAdd {
        public void onRequestInvestigationItemAdd(String result);
    }

    private Context mContext;
    private InvestigationItemData data;
    private boolean neddclosepd;
    private int iData;

    public InvestigationItemAddAction(Context context, InvestigationItemData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.InvestigationItemsURL;

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!data.getInvestigationItemId().equals("")) {
                jsonObject.put("InvestigationItemId", data.getInvestigationItemId());
            }
            jsonObject.put("InvestigationId", data.getInvestigationId());
            jsonObject.put("Name", data.getName());
            jsonObject.put("Description", data.getDescription());
            jsonObject.put("Date", data.getDateStringApi());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!data.getInvestigationItemId().equals("")) {
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
            ((RequestInvestigationItemAdd) mContext).onRequestInvestigationItemAdd(resultData);
        }
    }
}
