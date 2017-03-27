package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TopicData {
    private String meetingTopicId = "";
    private String name = "";
    private String description = "";
    private boolean completed = false;
    private boolean isTemplate = false;

    public TopicData() {

    }

    public TopicData(String name) {
        this.name = name;
    }

    public TopicData(JSONObject data) {
        try {
            if (!data.isNull("MeetingTopicId")) {
                setMeetingTopicId(data.getString("MeetingTopicId"));
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

    public String getMeetingTopicId() {
        return meetingTopicId;
    }

    public void setMeetingTopicId(String meetingTopicId) {
        this.meetingTopicId = meetingTopicId;
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

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(boolean isTemplate) {
        this.isTemplate = isTemplate;
    }
}