package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;

import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.TemplateItemData;
import com.sapphire.logic.UserInfo;
import com.sapphire.logic.WorkplaceInspectionItemData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GetWorkplaceInspectionAction extends AsyncTask{

    public interface RequestWorkplaceInspection {
        public void onRequestWorkplaceInspection(String result);
    }

    public interface RequestWorkplaceInspectionData {
        public void onRequestWorkplaceInspectionData(ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas);
    }

    private Context mContext;
    private ArrayList<WorkplaceInspectionItemData> workplaceInspectionItemDatas;
    private String workplaceInspectionId = "";

    public GetWorkplaceInspectionAction(Context context, String workplaceInspectionId) {
        this.mContext = context;
        this.workplaceInspectionId = workplaceInspectionId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionItemsURL + "?$filter=WorkplaceInspectionId%20eq%20guid'"+workplaceInspectionId+"'";;

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
            if (resultData.equals("OK")) {
                ((RequestWorkplaceInspectionData) mContext).onRequestWorkplaceInspectionData(workplaceInspectionItemDatas);
            } else {
                ((RequestWorkplaceInspection) mContext).onRequestWorkplaceInspection(resultData);
            }
        }
    }
}
