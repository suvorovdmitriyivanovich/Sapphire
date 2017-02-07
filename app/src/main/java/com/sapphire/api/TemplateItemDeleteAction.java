package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;

import java.util.ArrayList;

public class TemplateItemDeleteAction extends AsyncTask{

    public interface RequestTemplateItemDelete {
        public void onRequestTemplateItemDelete(String result);
    }

    private Context mContext;
    private String workplaceInspectionTemplateItemId;

    public TemplateItemDeleteAction(Context context, String workplaceInspectionTemplateItemId) {
        this.mContext = context;
        this.workplaceInspectionTemplateItemId = workplaceInspectionTemplateItemId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.WorkplaceInspectionTemplateItemsURL + "?model%5B%5D="+workplaceInspectionTemplateItemId;

        SharedPreferences sPref = mContext.getSharedPreferences("GlobalPreferences", mContext.MODE_PRIVATE);

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"DELETE",sPref.getString("AUTHTOKEN","")));

        String result = "";

        if (responseData.getSuccess()) {
            /*
            JSONArray data = responseData.getData();
            policiesDatas = new ArrayList<PoliciesData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    if (!data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    policiesDatas.add(new PoliciesData(data.getJSONObject(y)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int y=0; y < data.length(); y++) {
                try {
                    if (data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    PoliciesData policiesData = new PoliciesData(data.getJSONObject(y));
                    for (int z=0; z < policiesDatas.size(); z++) {
                        if (policiesDatas.get(z).getId().equals(policiesData.getParentId())) {
                            policiesDatas.get(z).getSubPolicies().add(policiesData);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            */

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
            ((RequestTemplateItemDelete) mContext).onRequestTemplateItemDelete(resultData);
        }
    }
}
