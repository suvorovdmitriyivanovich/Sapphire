package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.InvestigationData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class InvestigationsAction extends AsyncTask{

    public interface RequestInvestigations {
        public void onRequestInvestigations(String result, ArrayList<InvestigationData> investigationDatas);
    }

    private Context mContext;
    private ArrayList<InvestigationData> investigationDatas;
    private boolean me = false;

    public InvestigationsAction(Context context, boolean me) {
        this.mContext = context;
        this.me = me;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String authToken = userInfo.getAuthToken();

        String filter = "";
        if (me) {
            filter = "?$filter=Profiles/any(profile:%20profile/ProfileId%20eq%20guid'"+userInfo.getProfile().getProfileId()+"')";
            authToken = userInfo.getAuthTokenFirst();
        } else {
            filter = "?$filter=OrganizationId%20eq%20guid'"+userInfo.getCurrentOrganization().getOrganizationId()+"'";
        }

        String urlstring = Environment.SERVER + Environment.InvestigationsCurrentURL + filter;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", authToken));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            investigationDatas = new ArrayList<InvestigationData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    investigationDatas.add(new InvestigationData(data.getJSONObject(y)));
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
            ((RequestInvestigations) mContext).onRequestInvestigations(resultData, investigationDatas);
        }
    }
}
