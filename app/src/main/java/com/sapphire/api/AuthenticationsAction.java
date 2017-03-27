package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.sapphire.Sapphire;
import com.sapphire.R;
import com.sapphire.db.DBHelper;
import com.sapphire.models.AccountData;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.models.NavigationMenuData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AuthenticationsAction extends AsyncTask{

    public interface RequestAuthentications {
        public void onRequestAuthentications(String result);
    }

    private Context mContext;
    private String organization;
    private String login;
    private String password;

    public AuthenticationsAction(Context context, String organization, String login, String password) {
        this.mContext = context;
        this.organization = organization;
        this.login = login;
        this.password = password;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.SecurityAuthenticationsURL;

        JSONObject json = new JSONObject();
        try {
            json.put("OrganizationName", organization);
            json.put("AccountName", login);
            json.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,json.toString(),0,true,"POST",""));

        String result = "";

        SharedPreferences sPref = mContext.getSharedPreferences("GlobalPreferences", mContext.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        if (responseData.getSuccess()) {
            ed.putBoolean("INSUCCESS", true);
            ed.apply();

            JSONArray data = responseData.getData();
            ArrayList<AccountData> accountDatas = new ArrayList<AccountData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    accountDatas.add(new AccountData(data.getJSONObject(y)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            result = "OK";

            if (accountDatas.size() == 1) {
                ArrayList<NavigationMenuData> navigationMenuDatas = accountDatas.get(0).getNavigationMenus();
                if (navigationMenuDatas != null && navigationMenuDatas.size() > 0) {
                    DBHelper.getInstance(Sapphire.getInstance()).deleteNavigationMenus();
                    DBHelper.getInstance(Sapphire.getInstance()).addNavigationMenus(navigationMenuDatas);
                }
                UserInfo userInfo = UserInfo.getUserInfo();
                userInfo.setAuthToken(accountDatas.get(0).getAuthToken());
                userInfo.setAccountId(accountDatas.get(0).getAccountId());
                userInfo.setProfile(accountDatas.get(0).getCurrentProfile());
                userInfo.setOrganizations(accountDatas.get(0).getOrganizations());
                userInfo.setCurrentOrganization(accountDatas.get(0).getCurrentOrganization());
                //ed.putString("AUTHTOKEN", accountDatas.get(0).getAuthToken());
                //ed.putString("ACCOUNTID", accountDatas.get(0).getAccountId());
                //ed.apply();

                //userInfo.getProfile().setAvatarId("6993dd39-d2da-8230-6ad8-86f3839db097");

                String folder = Sapphire.getInstance().getFilesDir().getAbsolutePath();

                if (!userInfo.getProfile().getAvatarId().equals("")) {
                    urlstring = Environment.SERVER + Environment.DocumentManagementFilesDownloadURL + userInfo.getProfile().getAvatarId();

                    try {
                        result = downloadFile(urlstring, folder, userInfo.getAuthToken());
                    } catch (IOException e) {
                        e.printStackTrace();
                        result = e.getMessage();
                    }
                } else {
                    File sdPath = new File(folder + File.separator + "user.png");
                    if(sdPath.exists()){
                        sdPath.delete();
                    }
                }
            }
        } else {
            ed.putBoolean("INSUCCESS", false);
            ed.apply();

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

    public String downloadFile(String fileURL, String saveDir, String authToken)
            throws IOException {
        final int BUFFER_SIZE = 4096;

        String rezult = "OK";

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        if (!authToken.equals("")) {
            httpConn.setRequestProperty("x-yauth", authToken);
        }
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {

            String fileName = "";
            int contentLength = httpConn.getContentLength();
            //String disposition = httpConn.getHeaderField("Content-Disposition");
            //String contentType = httpConn.getContentType();

            /*
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
            */
            fileName = "user.png";

            String saveFilePath = saveDir + File.separator + fileName;

            if (new File(saveFilePath).length() != contentLength) {
                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
            }
        } else {
            rezult = httpConn.getResponseMessage();
        }
        httpConn.disconnect();

        return rezult;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            ((RequestAuthentications) mContext).onRequestAuthentications(resultData);
        }
    }
}
