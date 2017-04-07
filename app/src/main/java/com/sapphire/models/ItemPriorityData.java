package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemPriorityData {
    private String workplaceInspectionItemPriorityId = "";
    private String name = "";
    private String description = "";

    public ItemPriorityData() {

    }

    public ItemPriorityData(String workplaceInspectionItemPriorityId) {
        this.workplaceInspectionItemPriorityId = workplaceInspectionItemPriorityId;
    }

    public ItemPriorityData(String workplaceInspectionItemPriorityId, String name) {
        this.workplaceInspectionItemPriorityId = workplaceInspectionItemPriorityId;
        this.name = name;
    }

    public ItemPriorityData(JSONObject data) {
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