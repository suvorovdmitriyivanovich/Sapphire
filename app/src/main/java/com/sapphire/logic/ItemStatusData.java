package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemStatusData {
    private String workplaceInspectionItemStatusId = "";
    private String name = "";
    private String description = "";

    public ItemStatusData() {

    }

    public ItemStatusData(String workplaceInspectionItemStatusId) {
        this.workplaceInspectionItemStatusId = workplaceInspectionItemStatusId;
    }

    public ItemStatusData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionItemStatusId")) {
                setWorkplaceInspectionItemStatusId(data.getString("WorkplaceInspectionItemStatusId"));
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

    public String getWorkplaceInspectionItemStatusId() {
        return workplaceInspectionItemStatusId;
    }

    public void setWorkplaceInspectionItemStatusId(String workplaceInspectionItemStatusId) {
        this.workplaceInspectionItemStatusId = workplaceInspectionItemStatusId;
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