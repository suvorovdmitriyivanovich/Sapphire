package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class HireTypeData {
    private String hireTypeId = "";
    private String name = "";
    private String description = "";

    public HireTypeData() {

    }

    public HireTypeData(String name) {
        this.name = name;
    }

    public HireTypeData(JSONObject data) {
        try {
            if (!data.isNull("HireTypeId")) {
                setHireTypeId(data.getString("HireTypeId"));
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

    public void setHireTypeId(String hireTypeId) {
        this.hireTypeId = hireTypeId;
    }

    public String getHireTypeId() {
        return hireTypeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}