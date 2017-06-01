package com.dealerpilothr.api;

import android.content.Context;
import android.os.AsyncTask;

import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.models.CoursesData;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.UserInfo;
import java.util.ArrayList;

public class GetCourseFileAction extends AsyncTask{

    public interface RequestCourses {
        public void onRequestCourses(String result);
    }

    public interface RequestCoursesData {
        public void onRequestCoursesData(ArrayList<CoursesData> coursesDatas);
    }

    private Context mContext;
    private ArrayList<CoursesData> coursesDatas;
    private String courseId = "";

    public GetCourseFileAction(Context context, String courseId) {
        this.mContext = context;
        this.courseId = courseId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Dealerpilothr.getInstance().getResources().getString(R.string.text_need_internet);
        }

        String urlstring = Environment.SERVERFull + Environment.CourseFileURL + "?Id=" + courseId + "&AuthToken=" + UserInfo.getUserInfo().getAuthToken();
        //String urlstring = Environment.SERVER + "/v1/CoursesFiles" + "?$filter=CourseFileId%20eq%20guid'"+courseId+"'";
        //String urlstring = Environment.SERVER + "/v1/DocumentManagement/Files/DownloadZip" + "?fileId="+courseId;

        String result = NetRequests.getNetRequests().SendRequestCommon(urlstring,"",60000,true,"GET",UserInfo.getUserInfo().getAuthToken());

        /*
        String result = "";

        if (responseData.getSuccess()) {
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

        /*
        if (result.indexOf("<!DOCTYPE html><html><head>") == 0) {
            File sdPath = new File(Sapphire.getInstance().getFilesDir() + "/temp");
            if (!sdPath.exists()) {
                // создаем каталог
                sdPath.mkdirs();
            }
            sdPath = new File(Sapphire.getInstance().getFilesDir() + "/temp/temp.html");
            if (sdPath.exists()) {
                sdPath.delete();
            }

            //File f = new File(android.os.Environment.getExternalStorageDirectory() + "/Download/temp.html");

            Files.writeToFile(result, sdPath);
            //Files.writeToFile(result, f);

            result = "OK";
        }
        */

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            //if (resultData.equals("OK")) {
            //    ((RequestCoursesData) mContext).onRequestCoursesData(coursesDatas);
            //} else {
                ((RequestCourses) mContext).onRequestCourses(resultData);
            //}
        }
    }
}
