package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkplaceInspectionItemData {
    private String workplaceInspectionItemId = "";
    private String workplaceInspectionId = "";
    private String name = "";
    private String description = "";
    private ItemStatusData status = new ItemStatusData();
    private ItemPriorityData priority = new ItemPriorityData();
    private int severity = 0;

    public WorkplaceInspectionItemData() {

    }

    public WorkplaceInspectionItemData(String name) {
        setName(name);
    }

    public WorkplaceInspectionItemData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionItemId")) {
                setWorkplaceInspectionItemId(data.getString("WorkplaceInspectionItemId"));
            }
            if (!data.isNull("WorkplaceInspectionId")) {
                setWorkplaceInspectionId(data.getString("WorkplaceInspectionId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Severity")) {
                setSeverity(data.getInt("Severity"));
            }
            if (!data.isNull("Status")) {
                setStatus(data.getJSONObject("Status"));
            }
            if (!data.isNull("Priority")) {
                setPriority(data.getJSONObject("Priority"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWorkplaceInspectionItemId() {
        return workplaceInspectionItemId;
    }

    public void setWorkplaceInspectionItemId(String workplaceInspectionItemId) {
        this.workplaceInspectionItemId = workplaceInspectionItemId;
    }

    public String getWorkplaceInspectionId() {
        return workplaceInspectionId;
    }

    public void setWorkplaceInspectionId(String workplaceInspectionId) {
        this.workplaceInspectionId = workplaceInspectionId;
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

    public ItemStatusData getStatus() {
        return status;
    }

    public void setStatus(JSONObject status) {
        this.status = new ItemStatusData(status);
    }

    public void setStatus(ItemStatusData status) {
        this.status = status;
    }

    public ItemPriorityData getPriority() {
        return priority;
    }

    public void setPriority(JSONObject priority) {
        this.priority = new ItemPriorityData(priority);
    }

    public void setPriority(ItemPriorityData priority) {
        this.priority = priority;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public void setSeverity(String severity) {
        this.severity = 0;
        if (severity != null && !severity.equals("")) {
            this.severity = Integer.parseInt(severity);
        }
    }
}