package com.dealerpilothr.models;

import com.dealerpilothr.utils.DateOperations;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccidentData {
    private String accidentId = "";
    private String name = "";
    private String description = "";
    private Long date = 0l;
    private String profileId = "";
    private String fileId = "";

    public AccidentData() {

    }

    public AccidentData(String accidentId) {
        this.accidentId = accidentId;
    }

    public AccidentData(JSONObject data) {
        try {
            if (!data.isNull("AccidentId")) {
                setAccidentId(data.getString("AccidentId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Date")) {
                setDate(data.getString("Date"));
            }
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
            if (!data.isNull("FileId")) {
                setFileId(data.getString("FileId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(String accidentId) {
        this.accidentId = accidentId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = DateOperations.getDate(date);
    }

    public String getDateString() {
        String dateString = "";
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date thisdaten = new Date();
            thisdaten.setTime(date);
            dateString = format.format(thisdaten);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}