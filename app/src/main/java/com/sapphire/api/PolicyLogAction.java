package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.PolicyData;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PolicyLogAction extends AsyncTask{

    public interface RequestPolicyLog {
        public void onRequestPolicyLog(String result);
    }

    private Context mContext;
    private String policyId;
    private String policyStatusId;
    private ArrayList<PolicyData> policiesDatas;

    public PolicyLogAction(Context context, String policyId, String policyStatusId) {
        this.mContext = context;
        this.policyId = policyId;
        this.policyStatusId = policyStatusId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.PoliciesLogURL;

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PolicyId", policyId);
            jsonObject.put("PolicyStatusId", policyStatusId);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,"POST", UserInfo.getUserInfo().getAuthToken()));

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
            ((RequestPolicyLog) mContext).onRequestPolicyLog(resultData);
        }
    }
}
