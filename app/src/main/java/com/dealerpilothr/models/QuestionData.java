package com.dealerpilothr.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class QuestionData {
    private String questionId = "";
    private String name = "";
    private String description = "";
    private CategoryData category = new CategoryData();
    private int chanceToGet = 0;
    private ArrayList<AnswerData> answers = new ArrayList<AnswerData>();
    private int cost = 0;

    public QuestionData() {

    }

    public QuestionData(JSONObject data) {
        try {
            if (!data.isNull("QuestionId")) {
                setQuestionId(data.getString("QuestionId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Category")) {
                setCategory(data.getJSONObject("Category"));
            }
            if (!data.isNull("ChanceToGet")) {
                setChanceToGet(data.getInt("ChanceToGet"));
            }
            if (!data.isNull("Answers")) {
                setAnswers(data.getJSONArray("Answers"));
            }
            if (!data.isNull("Cost")) {
                setCost(data.getInt("Cost"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
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

    public CategoryData getCategory() {
        return category;
    }

    public void setCategory(JSONObject category) {
        this.category = new CategoryData(category);
    }

    public int getChanceToGet() {
        return chanceToGet;
    }

    public void setChanceToGet(int chanceToGet) {
        this.chanceToGet = chanceToGet;
    }

    public ArrayList<AnswerData> getAnswers() {
        return answers;
    }

    public void setAnswers(JSONArray answers) {
        ArrayList<AnswerData> answerDatas = new ArrayList<AnswerData>();
        for (int y=0; y < answers.length(); y++) {
            try {
                answerDatas.add(new AnswerData(answers.getJSONObject(y), category));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.answers = answerDatas;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}