package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CourseData {
    private String courseId = "";
    private String name = "";
    private String description = "";

    public CourseData() {

    }

    public CourseData(JSONObject data) {
        try {
            if (!data.isNull("CourseId")) {
                setCourseId(data.getString("CourseId"));
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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