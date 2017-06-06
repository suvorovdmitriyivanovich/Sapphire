package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.AdressData;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.models.ResponseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfilesAdressAddAction extends AsyncTask{

    public interface RequestProfilesAdressAdd {
        public void onRequestProfilesAdressAdd(String result);
    }

    private Context mContext;
    private AdressData data = new AdressData();

    public ProfilesAdressAddAction(Context context, AdressData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.AddressesURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AddressId", data.getAddressId());
            jsonObject.put("AddressLine1", data.getAddressLine1());
            jsonObject.put("AddressLine2", data.getAddressLine2());
            jsonObject.put("City", data.getCity());
            jsonObject.put("Country", data.getCountry());
            jsonObject.put("CountryId", data.getCountryId());
            jsonObject.put("IsPrimary", data.getIsPrimary());
            jsonObject.put("PostalCode", data.getPostalCode());
            jsonObject.put("Region", data.getRegion());
            jsonObject.put("RegionId", data.getRegionId());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "PUT";

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, userInfo.getAuthToken()));

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
            ((RequestProfilesAdressAdd) mContext).onRequestProfilesAdressAdd(resultData);
        }
    }
}
