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
import com.sapphire.logic.PoliciesData;
import com.sapphire.logic.QuizData;
import com.sapphire.logic.ResponseData;
import com.sapphire.utils.Files;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

public class GetQuizAction extends AsyncTask{

    public interface RequestQuiz {
        public void onRequestQuiz(String result);
    }

    public interface RequestQuizData {
        public void onRequestQuizData(ArrayList<QuizData> quizDatas);
    }

    private Context mContext;
    private ArrayList<QuizData> quizesDatas;
    private String quizeId = "";

    public GetQuizAction(Context context, String quizeId) {
        this.mContext = context;
        this.quizeId = quizeId;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.QuizzesURL + "?$filter=QuizId%20eq%20guid'"+quizeId+"'";

        SharedPreferences sPref = mContext.getSharedPreferences("GlobalPreferences", mContext.MODE_PRIVATE);

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET",sPref.getString("AUTHTOKEN","")));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            quizesDatas = new ArrayList<QuizData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    if (!data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    quizesDatas.add(new QuizData(data.getJSONObject(y)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int y=0; y < data.length(); y++) {
                try {
                    if (data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    QuizData quizDatas = new QuizData(data.getJSONObject(y));
                    /*
                    for (int z=0; z < policiesDatas.size(); z++) {
                        if (quizesDatas.get(z).getId().equals(policiesData.getParentId())) {
                            quizesDatas.get(z).getSubPolicies().add(policiesData);
                            break;
                        }
                    }
                    */
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
                ((RequestQuizData) mContext).onRequestQuizData(quizesDatas);
            } else {
                ((RequestQuiz) mContext).onRequestQuiz(resultData);
            }
        }
    }
}
