package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TemplateItemData {
    private String templateItemId = "";
    private String templateId = "";
    private String name = "";
    private String description = "";
    private ItemStatusData status = new ItemStatusData();
    private ItemPriorityData priority = new ItemPriorityData();
    private int severity = 0;

    public TemplateItemData() {

    }

    public TemplateItemData(String name) {
        setName(name);
    }

    public TemplateItemData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionTemplateItemId")) {
                setTemplateItemId(data.getString("WorkplaceInspectionTemplateItemId"));
            }
            if (!data.isNull("MeetingTopicTemplateItemId")) {
                setTemplateItemId(data.getString("MeetingTopicTemplateItemId"));
            }
            if (!data.isNull("WorkplaceInspectionTemplateId")) {
                setTemplateId(data.getString("WorkplaceInspectionTemplateId"));
            }
            if (!data.isNull("MeetingTopicTemplateId")) {
                setTemplateId(data.getString("MeetingTopicTemplateId"));
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

    public String getTemplateItemId() {
        return templateItemId;
    }

    public void setTemplateItemId(String templateItemId) {
        this.templateItemId = templateItemId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public ItemPriorityData getPriority() {
        return priority;
    }

    public void setPriority(JSONObject priority) {
        this.priority = new ItemPriorityData(priority);
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }
}