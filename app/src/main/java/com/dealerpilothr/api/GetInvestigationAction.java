package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.InvestigationItemData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GetInvestigationAction extends AsyncTask{

    public interface RequestInvestigation {
        public void onRequestInvestigation(String result, ArrayList<InvestigationItemData> datas);
    }

    private Context mContext;
    private ArrayList<InvestigationItemData> datas;
    private String id = "";

    public GetInvestigationAction(Context context, String id) {
        this.mContext = context;
        this.id = id;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.InvestigationItemsURL + "?$filter=InvestigationId%20eq%20guid'"+id+"'";;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            datas = new ArrayList<InvestigationItemData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    datas.add(new InvestigationItemData(data.getJSONObject(y)));
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
            ((RequestInvestigation) mContext).onRequestInvestigation(resultData, datas);
        }
    }
}
