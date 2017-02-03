package com.sapphire.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.QuestionData;
import com.sapphire.logic.QuizData;
import com.sapphire.logic.QuizScoreData;
import com.sapphire.logic.ResponseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostQuizzesAction extends AsyncTask{

    public interface RequestPostQuizzes {
        public void onRequestPostQuizzes(String result);
    }

    public interface RequestPostQuizzesData {
        public void onRequestPostQuizzesData(QuizScoreData quizScoreData);
    }

    private Context mContext;
    private String quizeId;
    private QuizData quizData;
    private QuizScoreData quizScoreData;

    public PostQuizzesAction(Context context, String quizeId, QuizData quizData) {
        this.mContext = context;
        this.quizeId = quizeId;
        this.quizData = quizData;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }
        String urlstring = Environment.SERVER + Environment.QuizzesURL;

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < quizData.getQuestions().size(); i++) {
            QuestionData questionData = quizData.getQuestions().get(i);
            try {
                JSONObject jsonObjectQuestion = new JSONObject();

                for (int y = 0; y < questionData.getAnswers().size(); y++) {
                    if (questionData.getAnswers().get(y).getChecked()) {
                        jsonObjectQuestion.put("AnswerId", questionData.getAnswers().get(y).getAnswerId());
                        jsonObjectQuestion.put("QuestionId", questionData.getQuestionId());
                        jsonArray.put(jsonObjectQuestion);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("QuizId", quizeId);
            jsonObject.put("ChosenAnswers", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sPref = mContext.getSharedPreferences("GlobalPreferences", mContext.MODE_PRIVATE);

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,jsonObject.toString(),0,true,"POST",sPref.getString("AUTHTOKEN","")));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            try {
                quizScoreData = new QuizScoreData(data.getJSONObject(0));
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

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            if (resultData.equals("OK")) {
                ((RequestPostQuizzesData) mContext).onRequestPostQuizzesData(quizScoreData);
            } else {
                ((RequestPostQuizzes) mContext).onRequestPostQuizzes(resultData);
            }
        }
    }
}
