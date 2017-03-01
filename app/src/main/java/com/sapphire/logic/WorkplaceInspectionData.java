package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkplaceInspectionData {
    private String workplaceInspectionId = "";
    private String name = "";
    private String description = "";
    private Long date = 0l;
    private boolean completed = false;
    private boolean postedOnBoard = false;
    private ArrayList<FileData> files = new ArrayList<FileData>();

    public WorkplaceInspectionData() {

    }

    public WorkplaceInspectionData(String name) {
        setName(name);
    }

    public WorkplaceInspectionData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionId")) {
                setWorkplaceInspectionId(data.getString("WorkplaceInspectionId"));
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
            if (!data.isNull("Completed")) {
                setCompleted(data.getBoolean("Completed"));
            }
            if (!data.isNull("PostedOnBoard")) {
                setPostedOnBoard(data.getBoolean("PostedOnBoard"));
            }
            if (!data.isNull("Files")) {
                setFiles(data.getJSONArray("Files"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public Long getDate() {
        return date;
    }

    public String getDateString() {
        String dateString = "";
        if (date != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date dateT = new Date();
            dateT.setTime(date);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setDate(String date) {
        Long dateLong = 0l;
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date newdate = format.parse(date);
            //dateLong = newdate.getTime();

            String datet = formatDate.format(newdate);
            newdate = formatDate.parse(datet);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.date = dateLong;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getPostedOnBoard() {
        return postedOnBoard;
    }

    public void setPostedOnBoard(boolean postedOnBoard) {
        this.postedOnBoard = postedOnBoard;
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
}