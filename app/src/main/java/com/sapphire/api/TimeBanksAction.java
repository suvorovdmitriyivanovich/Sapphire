package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.TimeBankAccountData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class TimeBanksAction extends AsyncTask{

    public interface RequestTimeBanks {
        public void onRequestTimeBanks(String result, ArrayList<TimeBankAccountData> datas);
    }

    private Context mContext;
    private ArrayList<TimeBankAccountData> datas;

    public TimeBanksAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String filter = "?$filter=ProfileId%20eq%20guid'"+userInfo.getProfile().getProfileId()+"'";

        String urlstring = Environment.SERVER + Environment.TimeBanksURL + filter;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthTokenFirst()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            datas = new ArrayList<TimeBankAccountData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    datas.add(new TimeBankAccountData(data.getJSONObject(y)));
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
            ((RequestTimeBanks) mContext).onRequestTimeBanks(resultData, datas);
        }
    }
}
