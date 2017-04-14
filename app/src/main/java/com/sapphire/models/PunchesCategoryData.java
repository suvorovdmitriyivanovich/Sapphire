package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class PunchesCategoryData {
    private String punchCategoryId = "";
    private String name = "";
    private String description = "";

    public PunchesCategoryData() {

    }

    public PunchesCategoryData(JSONObject data) {
        try {
            if (!data.isNull("PunchCategoryId")) {
                setPunchCategoryId(data.getString("PunchCategoryId"));
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

    public String getPunchCategoryId() {
        return punchCategoryId;
    }

    public void setPunchCategoryId(String punchCategoryId) {
        this.punchCategoryId = punchCategoryId;
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