package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ResponseData;
import com.sapphire.models.WorkplaceInspectionData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class WorkplaceInspectionsAction extends AsyncTask{

    public interface RequestWorkplaceInspections {
        public void onRequestWorkplaceInspections(String result);
    }

    public interface RequestWorkplaceInspectionsData {
        public void onRequestWorkplaceInspectionsData(ArrayList<WorkplaceInspectionData> workplaceInspectionDatas);
    }

    private Context mContext;
    private ArrayList<WorkplaceInspectionData> workplaceInspectionDatas;
    private boolean isDashboard = false;

    public WorkplaceInspectionsAction(Context context, boolean isDashboard) {
        this.mContext = context;
        this.isDashboard = isDashboard;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionsCurrentURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            workplaceInspectionDatas = new ArrayList<WorkplaceInspectionData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    WorkplaceInspectionData workplaceInspectionData = new WorkplaceInspectionData(data.getJSONObject(y));
                    if (isDashboard) {
                        if (!workplaceInspectionData.getCompleted() || !workplaceInspectionData.getPostedOnBoard()) {
                            continue;
                        }
                    }
                    workplaceInspectionDatas.add(workplaceInspectionData);
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
                ((RequestWorkplaceInspectionsData) mContext).onRequestWorkplaceInspectionsData(workplaceInspectionDatas);
            } else {
                ((RequestWorkplaceInspections) mContext).onRequestWorkplaceInspections(resultData);
            }
        }
    }
}
