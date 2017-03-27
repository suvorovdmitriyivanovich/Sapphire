package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.CoursesData;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class CoursesAction extends AsyncTask{

    public interface RequestCourses {
        public void onRequestCourses(String result);
    }

    public interface RequestCoursesData {
        public void onRequestCoursesData(ArrayList<CoursesData> coursesDatas);
    }

    private Context mContext;
    private ArrayList<CoursesData> coursesDatas;
    private boolean onlyOutstanding;

    public CoursesAction(Context context, boolean onlyOutstanding) {
        this.mContext = context;
        this.onlyOutstanding = onlyOutstanding;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.CoursesCurrentURL;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET",UserInfo.getUserInfo().getAuthToken()));

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
                    if (onlyOutstanding && coursesData.getQuizPassed()) {
                        continue;
                    }
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
            if (onlyOutstanding) {
                ArrayList<CoursesData> coursesDatasRemove = new ArrayList<CoursesData>();
                for (CoursesData item: coursesDatas) {
                    if (item.getSubCourses().size() == 0) {
                        coursesDatasRemove.add(item);
                    }
                }
                for (CoursesData item: coursesDatasRemove) {
                    coursesDatas.remove(item);
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
