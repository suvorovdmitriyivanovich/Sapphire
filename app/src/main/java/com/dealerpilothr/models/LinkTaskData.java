package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class LinkTaskData {
    private String linkId = "";
    private TaskData task = new TaskData();

    public LinkTaskData() {

    }

    public LinkTaskData(JSONObject data) {
        try {
            if (!data.isNull("LinkId")) {
                setLinkId(data.getString("LinkId"));
            }
            if (!data.isNull("Task")) {
                setTask(data.getJSONObject("Task"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public TaskData getTask() {
        return task;
    }

    public void setTask(JSONObject task) {
        this.task = new TaskData(task);
    }
}