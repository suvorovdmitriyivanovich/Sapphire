package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class QuizData {
    private String quizId = "";
    private String name = "";
    private String description = "";
    private String passingScore = "";
    private ArrayList<QuestionData> questions = new ArrayList<QuestionData>();

    public QuizData() {

    }

    public QuizData(JSONObject data) {
        try {
            if (!data.isNull("QuizId")) {
                setQuizId(data.getString("QuizId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("PassingScore")) {
                setPassingScore(data.getString("PassingScore"));
            }
            if (!data.isNull("Questions")) {
                setQuestions(data.getJSONArray("Questions"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(String passingScore) {
        this.passingScore = passingScore;
    }

    public ArrayList<QuestionData> getQuestions() {
        return questions;
    }

    public void setQuestions(JSONArray questions) {
        ArrayList<QuestionData> questionDatas = new ArrayList<QuestionData>();
        for (int y=0; y < questions.length(); y++) {
            try {
                questionDatas.add(new QuestionData(questions.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.questions = questionDatas;
    }
}