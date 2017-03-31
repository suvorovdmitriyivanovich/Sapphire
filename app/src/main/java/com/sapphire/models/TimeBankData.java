package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeBankData {
    private String timeBankAccountId = "";
    private String name = "";
    private String description = "";

    public TimeBankData() {

    }

    public TimeBankData(String timeBankAccountId) {
        this.timeBankAccountId = timeBankAccountId;
    }

    public TimeBankData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionItemPriorityId")) {
                setTimeBankAccountId(data.getString("WorkplaceInspectionItemPriorityId"));
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

    public String getTimeBankAccountId() {
        return timeBankAccountId;
    }

    public void setTimeBankAccountId(String timeBankAccountId) {
        this.timeBankAccountId = timeBankAccountId;
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