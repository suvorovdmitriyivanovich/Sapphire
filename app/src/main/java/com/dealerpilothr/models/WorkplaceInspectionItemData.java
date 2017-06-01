package com.dealerpilothr.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class WorkplaceInspectionItemData {
    private String id = "";
    private String workplaceInspectionItemId = "";
    private String workplaceInspectionId = "";
    private String name = "";
    private String description = "";
    private ItemStatusData status = new ItemStatusData();
    private ItemPriorityData priority = new ItemPriorityData();
    private int severity = 0;
    private String recommendedActions = "";
    private String comments = "";
    private ArrayList<FileData> files = new ArrayList<FileData>();
    private TaskData task = new TaskData();

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
            if (!data.isNull("RecommendedActions")) {
                setRecommendedActions(data.getString("RecommendedActions"));
            }
            if (!data.isNull("Comments")) {
                setComments(data.getString("Comments"));
            }
            if (!data.isNull("Status")) {
                setStatus(data.getJSONObject("Status"));
            }
            if (!data.isNull("Priority")) {
                setPriority(data.getJSONObject("Priority"));
            }
            if (!data.isNull("Files")) {
                setFiles(data.getJSONArray("Files"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRecommendedActions() {
        return recommendedActions;
    }

    public void setRecommendedActions(String recommendedActions) {
        this.recommendedActions = recommendedActions;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<FileData> getFiles() {
        return files;
    }

    public void setFiles(JSONArray files) {
        ArrayList<FileData> fileDatas = new ArrayList<FileData>();
        for (int y=0; y < files.length(); y++) {
            try {
                fileDatas.add(new FileData(files.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.files = fileDatas;
    }

    public TaskData getTask() {
        return task;
    }

    public void setTask(TaskData task) {
        this.task = task;
    }
}