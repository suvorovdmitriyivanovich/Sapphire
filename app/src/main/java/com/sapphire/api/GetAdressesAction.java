package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.AdressData;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GetAdressesAction extends AsyncTask{

    public interface RequestAdresses {
        public void onRequestAdresses(String result, AdressData adressData);
    }

    private Context mContext;
    private AdressData adressData = new AdressData();

    public GetAdressesAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String filter = "?$filter=Profiles/any(profile:%20profile/ProfileId%20eq%20guid'"+userInfo.getProfile().getProfileId()+"')";

        String urlstring = Environment.SERVER + Environment.AddressesURL + filter;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", userInfo.getAuthTokenFirst()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            for (int y=0; y < data.length(); y++) {
                try {
                    AdressData adresData = new AdressData(data.getJSONObject(y));
                    if (!adresData.getIsPrimary()) {
                        continue;
                    }
                    adressData = adresData;
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
            ((RequestAdresses) mContext).onRequestAdresses(resultData, adressData);
        }
    }
}