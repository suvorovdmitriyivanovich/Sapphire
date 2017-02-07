package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class TemplateItemData {
    private String workplaceInspectionTemplateItemId = "";
    private String workplaceInspectionTemplateId = "";
    private String name = "";
    private String description = "";
    private TemplateItemStatusData status = new TemplateItemStatusData();
    private TemplateItemPriorityData priority = new TemplateItemPriorityData();
    private int severity = 0;

    public TemplateItemData() {

    }

    public TemplateItemData(String name) {
        setName(name);
    }

    public TemplateItemData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionTemplateItemId")) {
                setWorkplaceInspectionTemplateItemId(data.getString("WorkplaceInspectionTemplateItemId"));
            }
            if (!data.isNull("WorkplaceInspectionTemplateId")) {
                setWorkplaceInspectionTemplateId(data.getString("WorkplaceInspectionTemplateId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Status")) {
                setStatus(data.getJSONObject("Status"));
            }
            if (!data.isNull("Priority")) {
                setPriority(data.getJSONObject("Priority"));
            }
            if (!data.isNull("Severity")) {
                setSeverity(data.getInt("Severity"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWorkplaceInspectionTemplateItemId() {
        return workplaceInspectionTemplateItemId;
    }

    public void setWorkplaceInspectionTemplateItemId(String workplaceInspectionTemplateItemId) {
        this.workplaceInspectionTemplateItemId = workplaceInspectionTemplateItemId;
    }

    public String getWorkplaceInspectionTemplateId() {
        return workplaceInspectionTemplateId;
    }

    public void setWorkplaceInspectionTemplateId(String workplaceInspectionTemplateId) {
        this.workplaceInspectionTemplateId = workplaceInspectionTemplateId;
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

    public TemplateItemStatusData getStatus() {
        return status;
    }

    public void setStatus(JSONObject status) {
        this.status = new TemplateItemStatusData(status);
    }

    public TemplateItemPriorityData getPriority() {
        return priority;
    }

    public void setPriority(JSONObject priority) {
        this.priority = new TemplateItemPriorityData(priority);
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }
}