package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.db.DBHelper;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.WorkplaceInspectionItemData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class WorkplaceInspectionItemAddAction extends AsyncTask{

    public interface RequestWorkplaceInspectionItemAdd {
        public void onRequestWorkplaceInspectionItemAdd(String result, boolean neddclosepd, int ihms, String workplaceInspectionItemId);
    }

    private Context mContext;
    private WorkplaceInspectionItemData workplaceInspectionItemData;
    private boolean neddclosepd;
    private int iData;
    private String workplaceInspectionItemId = "";
    private String idloc = "";

    public WorkplaceInspectionItemAddAction(Context context, WorkplaceInspectionItemData workplaceInspectionItemData, boolean neddclose, int i, String idloc) {
        this.mContext = context;
        this.workplaceInspectionItemData = workplaceInspectionItemData;
        this.neddclosepd = neddclose;
        this.iData = i+1;
        this.idloc = idloc;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionItemsURL;

        ArrayList<WorkplaceInspectionItemData> datas = new ArrayList<WorkplaceInspectionItemData>();
        if (workplaceInspectionItemData == null) {
            datas = DBHelper.getInstance(Sapphire.getInstance()).getWorkplaceInspectionItems("");
        } else {
            datas.add(workplaceInspectionItemData);
        }

        String result = "";

        for (WorkplaceInspectionItemData item: datas) {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            try {
                if (!item.getWorkplaceInspectionItemId().equals("")) {
                    jsonObject.put("WorkplaceInspectionItemId", item.getWorkplaceInspectionItemId());
                }
                jsonObject.put("WorkplaceInspectionId", item.getWorkplaceInspectionId());
                jsonObject.put("Name", item.getName());
                jsonObject.put("Description", item.getDescription());
                jsonObject.put("Comments", item.getComments());
                jsonObject.put("RecommendedActions", item.getRecommendedActions());
                jsonObject.put("Severity", item.getSeverity());
                if (!item.getStatus().getWorkplaceInspectionItemStatusId().equals("")) {
                    JSONObject jsonObjectStatus = new JSONObject();
                    jsonObjectStatus.put("WorkplaceInspectionItemStatusId", item.getStatus().getWorkplaceInspectionItemStatusId());
                    jsonObject.put("Status", jsonObjectStatus);
                }
                if (!item.getPriority().getWorkplaceInspectionItemPriorityId().equals("")) {
                    JSONObject jsonObjectPriority = new JSONObject();
                    jsonObjectPriority.put("WorkplaceInspectionItemPriorityId", item.getPriority().getWorkplaceInspectionItemPriorityId());
                    jsonObject.put("Priority", jsonObjectPriority);
                }
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String method = "POST";
            if (!item.getWorkplaceInspectionItemId().equals("")) {
                method = "PUT";
            }

            ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring, jsonArray.toString(), 0, true, method, UserInfo.getUserInfo().getAuthToken()));

            if (responseData.getSuccess() && responseData.getDataCount() == 1) {
                if (!item.getId().equals("")) {
                    DBHelper.getInstance(Sapphire.getInstance()).deleteWorkplaceInspectionItem(item.getId());
                    if (item.getId().equals(idloc)) {
                        try {
                            WorkplaceInspectionItemData data = new WorkplaceInspectionItemData((JSONObject) responseData.getData().get(0));
                            workplaceInspectionItemId = data.getWorkplaceInspectionItemId();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                result = "OK";
            } else {
                ArrayList<ErrorMessageData> errorMessageDatas = responseData.getErrorMessages();
                if (errorMessageDatas == null || errorMessageDatas.size() == 0) {
                    result = responseData.getHttpStatusMessage();
                } else {
                    for (int y = 0; y < errorMessageDatas.size(); y++) {
                        if (!result.equals("")) {
                            result = result + ". ";
                        }
                        result = errorMessageDatas.get(y).getName();
                    }
                }
                break;
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            ((RequestWorkplaceInspectionItemAdd) mContext).onRequestWorkplaceInspectionItemAdd(resultData, neddclosepd, iData, workplaceInspectionItemId);
        }
    }
}
