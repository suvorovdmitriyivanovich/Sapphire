package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.PolicyData;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class PoliciesAction extends AsyncTask{

    public interface RequestPolicies {
        public void onRequestPolicies(String result);
    }

    public interface RequestPoliciesData {
        public void onRequestPoliciesData(ArrayList<PolicyData> policiesDatas);
    }

    private Context mContext;
    private ArrayList<PolicyData> policiesDatas;
    private boolean onlyOutstanding;

    public PoliciesAction(Context context, boolean onlyOutstanding) {
        this.mContext = context;
        this.onlyOutstanding = onlyOutstanding;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.PoliciesCurrentURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthTokenFirst()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            policiesDatas = new ArrayList<PolicyData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    if (!data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    policiesDatas.add(new PolicyData(data.getJSONObject(y)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int y=0; y < data.length(); y++) {
                try {
                    if (data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    PolicyData policiesData = new PolicyData(data.getJSONObject(y));
                    if (onlyOutstanding && policiesData.getIsAcknowledged()) {
                        continue;
                    }
                    for (int z=0; z < policiesDatas.size(); z++) {
                        if (policiesDatas.get(z).getPolicyId().equals(policiesData.getParentId())) {
                            policiesDatas.get(z).getSubPolicies().add(policiesData);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (onlyOutstanding) {
                ArrayList<PolicyData> policiesDatasRemove = new ArrayList<PolicyData>();
                for (PolicyData item: policiesDatas) {
                    if (item.getSubPolicies().size() == 0) {
                        policiesDatasRemove.add(item);
                    }
                }
                for (PolicyData item: policiesDatasRemove) {
                    policiesDatas.remove(item);
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
                ((RequestPoliciesData) mContext).onRequestPoliciesData(policiesDatas);
            } else {
                ((RequestPolicies) mContext).onRequestPolicies(resultData);
            }
        }
    }
}
