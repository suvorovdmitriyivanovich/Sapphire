package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ResponseData;
import com.dealerpilothr.R;
import com.dealerpilothr.logic.UserInfo;
import java.util.ArrayList;

public class TemplateItemDeleteAction extends AsyncTask{

    public interface RequestTemplateItemDelete {
        public void onRequestTemplateItemDelete(String result);
    }

    private Context mContext;
    private String templateItemId;
    private String typeId;

    public TemplateItemDeleteAction(Context context, String templateItemId, String typeId) {
        this.mContext = context;
        this.templateItemId = templateItemId;
        this.typeId = typeId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER;
        if (typeId.equals(Dealerpilothr.getInstance().getResources().getString(R.string.text_meetings_templates))) {
            urlstring = urlstring + Environment.TopicTemplateItemsURL + "?model%5B%5D="+templateItemId;
        } else {
            urlstring = urlstring + Environment.WorkplaceInspectionTemplateItemsURL + "?model%5B%5D="+templateItemId;
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"DELETE", UserInfo.getUserInfo().getAuthToken()));

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
