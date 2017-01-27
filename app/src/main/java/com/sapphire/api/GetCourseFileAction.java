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

import org.json.JSONArray;
import org.json.JSONException;

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
        String urlstring = Environment.SERVERFull + Environment.CourseFileGetURL + "/" + courseId;

        SharedPreferences sPref = mContext.getSharedPreferences("GlobalPreferences", mContext.MODE_PRIVATE);

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET",sPref.getString("AUTHTOKEN","")));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            coursesDatas = new ArrayList<CoursesData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    if (!data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    coursesDatas.add(new CoursesData(data.getJSONObject(y)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int y=0; y < data.length(); y++) {
                try {
                    if (data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    CoursesData coursesData = new CoursesData(data.getJSONObject(y));
                    for (int z=0; z < coursesDatas.size(); z++) {
                        if (coursesDatas.get(z).getCourseFileId().equals(coursesData.getParentId())) {
                            coursesDatas.get(z).getSubCourses().add(coursesData);
                            break;
                        }
                    }
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
            if (resultData.equals("OK")) {
                ((RequestCoursesData) mContext).onRequestCoursesData(coursesDatas);
            } else {
                ((RequestCourses) mContext).onRequestCourses(resultData);
            }
        }
    }
}
