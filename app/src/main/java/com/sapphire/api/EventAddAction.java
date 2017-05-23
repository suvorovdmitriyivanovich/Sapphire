package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.MeetingData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import java.util.ArrayList;

public class EventAddAction extends AsyncTask{

    public interface RequestEventAdd {
        public void onRequestEventAdd(String result);
    }

    private Context mContext;

    public EventAddAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.EventsURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring, jsonArray.toString(), 0, true, "PUT", userInfo.getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
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
            ((RequestEventAdd) mContext).onRequestEventAdd(resultData);
        }
    }
}
