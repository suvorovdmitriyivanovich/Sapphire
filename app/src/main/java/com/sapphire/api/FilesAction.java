package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.FileData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class FilesAction extends AsyncTask{

    public interface RequestFiles {
        public void onRequestFiles(String result, ArrayList<FileData> fileDatas);
    }

    private Context mContext;
    private ArrayList<FileData> fileDatas;

    public FilesAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        String filter = "";
        ArrayList<FileData> filesDatas = UserInfo.getUserInfo().getFileDatas();
        for (FileData item: filesDatas) {
            if (filter.equals("")) {
                filter = "?$filter=";
            } else {
                filter = filter + "%20or%20";
            }
            filter = filter + "FileId%20eq%20guid'"+item.getFileId()+"'";
        }

        fileDatas = new ArrayList<FileData>();

        if (filter.equals("")) {
            return "OK";
        }

        String urlstring = Environment.SERVER + Environment.DocumentManagementFilesURL + filter;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            for (int y=0; y < data.length(); y++) {
                try {
                    fileDatas.add(new FileData(data.getJSONObject(y)));
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
            ((RequestFiles) mContext).onRequestFiles(resultData, fileDatas);
        }
    }
}
