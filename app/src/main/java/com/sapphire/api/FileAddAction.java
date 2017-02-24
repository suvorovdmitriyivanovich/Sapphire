package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.TemplateData;
import com.sapphire.logic.UserInfo;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class FileAddAction extends AsyncTask{

    public interface RequestFileAdd {
        public void onRequestFileAdd(String result);
    }

    private Context mContext;
    private String fileId;
    private String id;
    private String url;
    private String nameField;
    private TemplateData templateData = new TemplateData();

    public FileAddAction(Context context, String fileId, String id, String url, String nameField) {
        this.mContext = context;
        this.fileId = fileId;
        this.id = id;
        this.url = url;
        this.nameField = nameField;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + url;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(nameField, id);
            jsonObject.put("FileId", fileId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonObject.toString(),0,true,method, UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            try {
                templateData = new TemplateData(responseData.getData().getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (templateData.getWorkplaceInspectionTemplateId().equals("")) {
                result = Sapphire.getInstance().getResources().getString(R.string.unknown_error);
            } else {
                result = "OK";
            }
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
            ((RequestFileAdd) mContext).onRequestFileAdd(resultData);
        }
    }
}
