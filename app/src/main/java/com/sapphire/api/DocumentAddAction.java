package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.DocumentData;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.ResponseData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DocumentAddAction extends AsyncTask{

    public interface RequestDocumentAdd {
        public void onRequestDocumentAdd(String result);
    }

    private Context mContext;
    private DocumentData data = new DocumentData();

    public DocumentAddAction(Context context, DocumentData data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.DocumentsURL;

        UserInfo userInfo = UserInfo.getUserInfo();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!data.getDocId().equals("")) {
                jsonObject.put("DocId", data.getDocId());
                //jsonObject.put("OrganizationId", userInfo.getCurrentOrganization().getOrganizationId());
            }
            jsonObject.put("Name", data.getName());
            jsonObject.put("Date", data.getDateServer());
            jsonObject.put("DocCategoryId", data.getDocCategoryId());
            jsonObject.put("Category", data.getCategory().getJSON());
            jsonObject.put("ProfileId", data.getProfileId());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String method = "POST";
        if (!data.getDocId().equals("")) {
            method = "PUT";
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonArray.toString(),0,true,method, userInfo.getAuthToken()));

        String result = "";

        //"Category":{"DocCategoryId":"d71cdd39-a1ce-ed2c-bcc4-2c9b22d77577","Name":"Payroll","Description":null}
        //[{"DocId":"98e8dd39-ebf3-00c4-f79b-0c9e1ab746ab","Name":"2","Date":"2017-03-16T10:12:44.200Z","DocCategoryId":"d71cdd39-a1ce-ed2c-bcc4-2c9b22d77577","Category":{"DocCategoryId":"d71cdd39-a1ce-ed2c-bcc4-2c9b22d77577","Name":"Payroll","Description":null},"ProfileId":"6509dd39-9115-3061-544e-6f4b7c26f932","FileId":"98e8dd39-ebf3-0311-0d3c-987a4e6b473c"}]

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
            ((RequestDocumentAdd) mContext).onRequestDocumentAdd(resultData);
        }
    }
}
