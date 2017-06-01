package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class AnswerData {
    private String answerId = "";
    private String name = "";
    private String description = "";
    private CategoryData category = new CategoryData();
    private boolean checked = false;

    public AnswerData() {

    }

    public AnswerData(JSONObject data, CategoryData category) {
        this.category = category;
        try {
            if (!data.isNull("AnswerId")) {
                setAnswerId(data.getString("AnswerId"));
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

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswerId() {
        return answerId;
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

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}