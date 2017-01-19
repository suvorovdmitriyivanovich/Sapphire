package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.sapphire.Sapphire;
import com.sapphire.R;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        String urlstring = Environment.SERVER
                + "/v1/Security/Authentications";

        JSONObject json = new JSONObject();
        try {
            json.put("OrganizationName", organization);
            json.put("AccountName", login);
            json.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String result = NetRequests.getNetRequests().SendRequestCommon(urlstring,json.toString(),0,false);

        ResponseData responseData = new ResponseData(result);

        return result;
    }

    public void downloadFile(String fileURL, String saveDir)
            throws IOException {
        final int BUFFER_SIZE = 4096;

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {

            String fileName = "";
            int contentLength = httpConn.getContentLength();
            String disposition = httpConn.getHeaderField("Content-Disposition");
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
        }
        httpConn.disconnect();
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            ((RequestAuthentications) mContext).onRequestAuthentications(resultData);
        }
    }
}
