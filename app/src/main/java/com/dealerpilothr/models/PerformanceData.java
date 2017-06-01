package com.dealerpilothr.models;

import com.dealerpilothr.utils.DateOperations;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PerformanceData {
    private String performanceEvaluationId = "";
    private String name = "";
    private Long datePosted = 0l;
    private Long renewalDate = 0l;
    private String fileId = "";
    private String profileId = "";

    public PerformanceData() {

    }

    public PerformanceData(String name) {
        setName(name);
    }

    public PerformanceData(JSONObject data) {
        try {
            if (!data.isNull("PerformanceEvaluationId")) {
                setPerformanceEvaluationId(data.getString("PerformanceEvaluationId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("DatePosted")) {
                setDatePosted(data.getString("DatePosted"));
            }
            if (!data.isNull("RenewalDate")) {
                setRenewalDate(data.getString("RenewalDate"));
            }
            if (!data.isNull("FileId")) {
                setFileId(data.getString("FileId"));
            }
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPerformanceEvaluationId() {
        return performanceEvaluationId;
    }

    public void setPerformanceEvaluationId(String performanceEvaluationId) {
        this.performanceEvaluationId = performanceEvaluationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDatePosted() {
        return datePosted;
    }

    public String getDatePostedString() {
        String dateString = "";
        if (datePosted != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(datePosted);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public String getDatePostedServer() {
        return DateOperations.getDateServer(datePosted);
    }

    public void setDatePosted(String date) {
        this.datePosted = DateOperations.getDate(date);
    }

    public void setDatePosted(Long date) {
        this.datePosted = date;
    }

    public Long getRenewalDate() {
        return renewalDate;
    }

    public String getRenewalDateString() {
        String dateString = "";
        if (renewalDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(renewalDate);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public String getRenewalDateServer() {
        return DateOperations.getDateServer(renewalDate);
    }

    public void setRenewalDate(String date) {
        this.renewalDate = DateOperations.getDate(date);
    }

    public void setRenewalDate(Long date) {
        this.renewalDate = date;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}