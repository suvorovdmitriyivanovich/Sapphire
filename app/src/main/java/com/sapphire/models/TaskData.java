package com.sapphire.models;

import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskData {
    private String taskId = "";
    private String parentId = "";
    private String taskTypeId = "";
    private String taskCategoryId = "";
    private String creatorId = "";
    private ArrayList<ProfileData> assignedProfiles = new ArrayList<ProfileData>();
    private String name = "";
    private String description = "";
    private Long plannedStartDate = 0l;
    private Long plannedFinishDate = 0l;
    private Double percentComplete = 0d;
    private String priority = "";
    private SettingData properties = new SettingData();
    private SettingData editRight = new SettingData();

    public TaskData() {

    }

    public TaskData(String name) {
        setName(name);
    }

    public TaskData(JSONObject data) {
        try {
            if (!data.isNull("TaskId")) {
                setTaskId(data.getString("TaskId"));
            }
            if (!data.isNull("ParentId")) {
                setParentId(data.getString("ParentId"));
            }
            if (!data.isNull("TaskTypeId")) {
                setTaskTypeId(data.getString("TaskTypeId"));
            }
            if (!data.isNull("TaskCategoryId")) {
                setTaskCategoryId(data.getString("TaskCategoryId"));
            }
            if (!data.isNull("CreatorId")) {
                setCreatorId(data.getString("CreatorId"));
            }
            if (!data.isNull("AssignedProfiles")) {
                setAssignedProfiles(data.getJSONArray("AssignedProfiles"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("PlannedStartDate")) {
                setPlannedStartDate(data.getString("PlannedStartDate"));
            }
            if (!data.isNull("PlannedFinishDate")) {
                setPlannedFinishDate(data.getString("PlannedFinishDate"));
            }
            if (!data.isNull("PercentComplete")) {
                setPercentComplete(data.getDouble("PercentComplete"));
            }
            if (!data.isNull("Priority")) {
                setPriority(data.getString("Priority"));
            }
            if (!data.isNull("Properties")) {
                setProperties(data.getJSONObject("Properties"));
            }
            if (!data.isNull("EditRight")) {
                setEditRight(data.getJSONObject("EditRight"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskCategoryId() {
        return taskCategoryId;
    }

    public void setTaskCategoryId(String taskCategoryId) {
        this.taskCategoryId = taskCategoryId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public ArrayList<ProfileData> getAssignedProfiles() {
        return assignedProfiles;
    }

    public void setAssignedProfiles(JSONArray assignedProfiles) {
        ArrayList<ProfileData> datas = new ArrayList<ProfileData>();
        for (int y=0; y < assignedProfiles.length(); y++) {
            try {
                datas.add(new ProfileData(assignedProfiles.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.assignedProfiles = datas;
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

    public Long getPlannedStartDate() {
        return plannedStartDate;
    }

    public String getPlannedStartDateString() {
        String dateString = "";
        if (plannedStartDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date dateT = new Date();
            dateT.setTime(plannedStartDate);
            dateString = format.format(dateT);
        }
        return dateString;
    }

    public String getPlannedStartDateServer() {
        return DateOperations.getDateServer(plannedStartDate);
    }

    public void setPlannedStartDate(String date) {
        this.plannedStartDate = DateOperations.getDate(date);
    }

    public void setPlannedStartDate(Long date) {
        this.plannedStartDate = date;
    }

    public Long getPlannedFinishDate() {
        return plannedFinishDate;
    }

    public String getPlannedFinishDateString() {
        String dateString = "";
        if (plannedFinishDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date dateT = new Date();
            dateT.setTime(plannedFinishDate);
            dateString = format.format(dateT);
        }
        return dateString;
    }

    public String getPlannedFinishDateServer() {
        return DateOperations.getDateServer(plannedFinishDate);
    }

    public void setPlannedFinishDate(String date) {
        this.plannedFinishDate = DateOperations.getDate(date);
    }

    public void setPlannedFinishDate(Long date) {
        this.plannedFinishDate = date;
    }

    public Double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public SettingData getProperties() {
        return properties;
    }

    public void setProperties(JSONObject properties) {
        this.properties = new SettingData(properties);
    }

    public SettingData getEditRight() {
        return editRight;
    }

    public void setEditRight(JSONObject editRight) {
        this.editRight = new SettingData(editRight);
    }
}