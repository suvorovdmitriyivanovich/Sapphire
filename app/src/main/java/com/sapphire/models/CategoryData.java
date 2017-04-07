package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoryData {
    private String id = "";
    private String name = "";
    private String description = "";

    public CategoryData() {

    }

    public CategoryData(String id) {
        this.id = id;
    }

    public CategoryData(JSONObject data) {
        try {
            if (!data.isNull("QuestionCategoryId")) {
                setId(data.getString("QuestionCategoryId"));
            } else if (!data.isNull("DocCategoryId")) {
                setId(data.getString("DocCategoryId"));
            } else if (!data.isNull("TaskCategoryId")) {
                setId(data.getString("TaskCategoryId"));
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DocCategoryId", id);
            jsonObject.put("Name", name);
            jsonObject.put("Description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}