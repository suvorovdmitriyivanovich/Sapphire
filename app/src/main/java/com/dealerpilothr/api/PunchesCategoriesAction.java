package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.PunchesCategoryData;
import com.dealerpilothr.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PunchesCategoriesAction extends AsyncTask{

    public interface RequestPunchesCategories {
        public void onRequestPunchesCategories(String result, ArrayList<PunchesCategoryData> datas);
    }

    private Context mContext;
    private ArrayList<PunchesCategoryData> datas;

    public PunchesCategoriesAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.PunchesCategoriesURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET",UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            datas = new ArrayList<PunchesCategoryData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    datas.add(new PunchesCategoryData(data.getJSONObject(y)));
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
            ((RequestPunchesCategories) mContext).onRequestPunchesCategories(resultData, datas);
        }
    }
}
