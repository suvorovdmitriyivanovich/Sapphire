package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoryData {
    private String questionCategoryId = "";
    private String name = "";
    private String description = "";

    public CategoryData() {

    }

    public CategoryData(JSONObject data) {
        try {
            if (!data.isNull("QuestionCategoryId")) {
                setQuestionCategoryId(data.getString("QuestionCategoryId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setQuestionCategoryId(String questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public String getQuestionCategoryId() {
        return questionCategoryId;
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
}