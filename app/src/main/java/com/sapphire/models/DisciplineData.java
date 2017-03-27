package com.sapphire.models;

import com.sapphire.utils.DateOperations;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisciplineData {
    private String disciplineId = "";
    private String name = "";
    private String description = "";
    private String notes = "";
    private Long datePosted = 0l;
    private String fileId = "";
    private String profileId = "";

    public DisciplineData() {

    }

    public DisciplineData(String name) {
        setName(name);
    }

    public DisciplineData(JSONObject data) {
        try {
            if (!data.isNull("DisciplineId")) {
                setDisciplineId(data.getString("DisciplineId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Notes")) {
                setNotes(data.getString("Notes"));
            }
            if (!data.isNull("DatePosted")) {
                setDatePosted(data.getString("DatePosted"));
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

    public String getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getDatePosted() {
        return datePosted;
    }

    public String getDatePostedString() {
        String dateString = "";
        if (datePosted != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date dateT = new Date();
            dateT.setTime(datePosted);
            dateString = format.format(datePosted);
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