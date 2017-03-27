package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.OrganizationData;
import com.sapphire.models.OrganizationStructureData;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class OrganizationStructureItemAddAction extends AsyncTask{

    public interface RequestOrganizationStructureItemAdd {
        public void onRequestOrganizationStructureItemAdd(String result);
    }

    private Context mContext;
    private OrganizationStructureData organizationStructureData;
    private String parrentId;

    public OrganizationStructureItemAddAction(Context context, OrganizationStructureData organizationStructureData, String parrentId) {
        this.mContext = context;
        this.organizationStructureData = organizationStructureData;
        this.parrentId = parrentId;

    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.OrganizationStructureURL;

        String organizationsIds = "";
        ArrayList<OrganizationData> organizationDatas = UserInfo.getUserInfo().getOrganizations();
        for (OrganizationData item: organizationDatas) {
            if (!organizationsIds.equals("")) {
                organizationsIds = organizationsIds + ", ";
            }
            organizationsIds = organizationsIds + "[\""+item.getOrganizationId()+"\"]";
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!organizationStructureData.getId().equals("")) {
                jsonObject.put("OrganizationStructureId", organizationStructureData.getId());
            }
            jsonObject.put("ParentId", parrentId);
            jsonObject.put("Name", organizationStructureData.getName());
            jsonObject.put("Description", organizationStructureData.getDescription());
            jsonObject.put("IsPosition", organizationStructureData.getIsPosition());
            jsonObject.put("IsCurrent", organizationStructureData.getIsCurrent());
            jsonObject.put("OrganizationsIds", organizationsIds);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!organizationStructureData.getId().equals("")) {
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
            ((RequestOrganizationStructureItemAdd) mContext).onRequestOrganizationStructureItemAdd(resultData);
        }
    }
}
