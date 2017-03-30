package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeBankData {
    private String workplaceInspectionItemPriorityId = "";
    private String name = "";
    private String description = "";

    public TimeBankData() {

    }

    public TimeBankData(String workplaceInspectionItemPriorityId) {
        this.workplaceInspectionItemPriorityId = workplaceInspectionItemPriorityId;
    }

    public TimeBankData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionItemPriorityId")) {
                setWorkplaceInspectionItemPriorityId(data.getString("WorkplaceInspectionItemPriorityId"));
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

    public String getWorkplaceInspectionItemPriorityId() {
        return workplaceInspectionItemPriorityId;
    }

    public void setWorkplaceInspectionItemPriorityId(String workplaceInspectionItemPriorityId) {
        this.workplaceInspectionItemPriorityId = workplaceInspectionItemPriorityId;
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