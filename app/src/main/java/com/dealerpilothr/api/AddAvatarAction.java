package com.dealerpilothr.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.ErrorMessageData;
import com.dealerpilothr.models.ResponseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddAvatarAction extends AsyncTask{

    public interface RequestAddAvatar {
        public void onRequestAddAvatar(String result);
    }

    private Context mContext;
    private Bitmap bitmap;

    public AddAvatarAction(Context context, Bitmap bitmap) {
        this.mContext = context;
        this.bitmap = bitmap;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER;

        String body = "";
        String method = "DELETE";
        if (bitmap == null) {
            urlstring = urlstring + Environment.DeleteAvatarURL + "?profilesIds[]=" + UserInfo.getUserInfo().getProfile().getProfileId();
        } else {
            urlstring = urlstring + Environment.AddAvatarURL;

            method = "POST";

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            try {
                //jsonObject.put("ImageData", "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT));
                jsonObject.put("ImageData", Base64.encodeToString(byteArray, Base64.DEFAULT));
                jsonObject.put("ImageFileType", "image/png");
                jsonObject.put("ImageFullName", "user.png");
                jsonObject.put("ProfileId", UserInfo.getUserInfo().getProfile().getProfileId());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            body = jsonArray.toString();
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring, body, 0, true, method, UserInfo.getUserInfo().getAuthTokenFirst()));

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
            ((RequestAddAvatar) mContext).onRequestAddAvatar(resultData);
        }
    }
}