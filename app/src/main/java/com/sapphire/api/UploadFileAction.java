package com.sapphire.api;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.FileData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UploadFileAction extends AsyncTask{

    public interface RequestUploadFile {
        public void onRequestUploadFile(String result, FileData fileData);
    }

    private Context mContext;
    private String filename = "";
    private FileData fileData = new FileData();

    public UploadFileAction(Context context, String filename) {
        this.mContext = context;
        this.filename = filename;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        String urlstring = Environment.SERVER + Environment.DocumentManagementFilesUploadURL;

        File file = new File(filename);

        ResponseData responseData = new ResponseData();

        if (!file.exists()) {
            return Sapphire.getInstance().getResources().getString(R.string.text_no_file);
        } else {
            responseData = new ResponseData(UploadFile(urlstring, 120000, file,UserInfo.getUserInfo().getAuthToken()));
        }

        String result = "";

        if (responseData.getSuccess() && responseData.getDataCount() == 1) {
            JSONArray data = responseData.getData();
            try {
                fileData = new FileData(data.getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
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

    public String UploadFile(String request, int timout, File photo, String authToken) {
        String rez = "";

        if (timout == 0) {
            timout = 20000;
        }

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024*1024;

        String extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(photo.getAbsolutePath().toLowerCase()))));
        String fileMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        try{
            FileInputStream fileInputStream = new FileInputStream(photo);

            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            if (!authToken.equals("")) {
                conn.setRequestProperty("x-yauth", authToken);
            }
            conn.setConnectTimeout(timout);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"attachment\";filename=\"" + photo.getName() + "\"" + lineEnd); // uploaded_file_name is the Name of the File to be uploaded
            dos.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();
            dos.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "iso-8859-1"), 8);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                rez = response.toString();
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"), 8);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                rez = response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            rez = e.getMessage();
            if (rez == null) {
                rez = "";
            }
            if (rez.indexOf("after") != -1) {
                rez = Sapphire.getInstance().getResources().getString(R.string.text_timeaut);
            }
        }

        return rez;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            ((RequestUploadFile) mContext).onRequestUploadFile(resultData, fileData);
        }
    }
}
