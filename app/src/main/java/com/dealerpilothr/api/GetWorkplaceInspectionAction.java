package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.WorkplaceInspectionItemData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GetWorkplaceInspectionAction extends AsyncTask{

    public interface RequestWorkplaceInspection {
        public void onRequestWorkplaceInspection(String result, ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas);
    }

    private Context mContext;
    private ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas = new ArrayList<WorkplaceInspectionItemData>();
    private String workplaceInspectionId = "";

    public GetWorkplaceInspectionAction(Context context, String workplaceInspectionId) {
        this.mContext = context;
        this.workplaceInspectionId = workplaceInspectionId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionItemsURL + "?$filter=WorkplaceInspectionId%20eq%20guid'"+workplaceInspectionId+"'&%24orderby=WorkplaceInspectionItemId";

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            workplaceInspectionItemDatas = new ArrayList<WorkplaceInspectionItemData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    workplaceInspectionItemDatas.add(new WorkplaceInspectionItemData(data.getJSONObject(y)));
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
            ((RequestWorkplaceInspection) mContext).onRequestWorkplaceInspection(resultData, workplaceInspectionItemDatas);
        }
    }
}
