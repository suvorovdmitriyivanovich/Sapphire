package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.DocumentData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.ErrorMessageData;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class DocumentsAction extends AsyncTask{

    public interface RequestDocuments {
        public void onRequestDocuments(String result, ArrayList<DocumentData> datas);
    }

    private Context mContext;
    private ArrayList<DocumentData> datas;

    public DocumentsAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String filter = "?$filter=ProfileId%20eq%20guid'"+userInfo.getProfile().getProfileId()+"'";

        String urlstring = Environment.SERVER + Environment.DocumentsURL + filter;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", userInfo.getAuthTokenFirst()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            datas = new ArrayList<DocumentData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    datas.add(new DocumentData(data.getJSONObject(y)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            ((RequestDocuments) mContext).onRequestDocuments(resultData, datas);
        }
    }
}
