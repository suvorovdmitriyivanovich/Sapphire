package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetFileAction extends AsyncTask{

    public interface RequestFile {
        public void onRequestFile(String result, String file);
    }

    private Context mContext;
    private String id = "";
    private String filename = "";
    private String folder = "";
    private String file = "";

    public GetFileAction(Context context, String id, String filename, String folder) {
        this.mContext = context;
        this.id = id;
        this.filename = filename;
        this.folder = folder;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        String urlstring = Environment.SERVER + Environment.DocumentManagementFilesDownloadURL + id;

        String result = "";

        try {
            result = downloadFile(urlstring,folder,UserInfo.getUserInfo().getAuthToken());
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        /*
        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",60000,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            for (int y=0; y < data.length(); y++) {
                try {
                    adressDatas.add(new ContactData(data.getJSONObject(y)));
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
        */

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

            String saveFilePath = saveDir + File.separator + filename;

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

            file = saveFilePath;
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
            ((RequestFile) mContext).onRequestFile(resultData, file);
        }
    }
}
