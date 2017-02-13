package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.CoursesData;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.utils.Files;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
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
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        //String urlstring = Environment.SERVERFull + Environment.CourseFileGetURL + "?Id=" + courseId;
        //String urlstring = Environment.SERVER + "/v1/CoursesFiles" + "?$filter=CourseFileId%20eq%20guid'"+courseId+"'";
        String urlstring = Environment.SERVER + "/v1/DocumentManagement/Files/DownloadZip" + "?fileId="+courseId;

        SharedPreferences sPref = mContext.getSharedPreferences("GlobalPreferences", mContext.MODE_PRIVATE);
        String result = NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET",sPref.getString("AUTHTOKEN",""));

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

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            if (resultData.equals("OK")) {
                ((RequestCoursesData) mContext).onRequestCoursesData(coursesDatas);
            } else {
                ((RequestCourses) mContext).onRequestCourses(resultData);
            }
        }
    }
}
