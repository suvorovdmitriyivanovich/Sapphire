package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.ParameterData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class TaskManagementParametersAction extends AsyncTask{

    public interface RequestTaskManagementParameters {
        public void onRequestTaskManagementParameters(String result, ArrayList<ParameterData> parameterDatas);
    }

    private Context mContext;
    private ArrayList<ParameterData> parameterDatas;

    public TaskManagementParametersAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.TaskManagementParametersURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring, "", 0, true, "GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            parameterDatas = new ArrayList<ParameterData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    parameterDatas.add(new ParameterData(data.getJSONObject(y)));
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
            ((RequestTaskManagementParameters) mContext).onRequestTaskManagementParameters(resultData, parameterDatas);
        }
    }
}
