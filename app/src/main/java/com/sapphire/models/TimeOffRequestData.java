package com.sapphire.models;

import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeOffRequestData {
    private String timeOffRequestId = "";
    private TimeBankData timeBank = new TimeBankData();
    private AttendanceCodeData attendanceCode = new AttendanceCodeData();
    private ArrayList<DayData> days = new ArrayList<DayData>();
    private String name = "";
    private String description = "";
    private Long date = 0l;
    private boolean completed = false;
    private boolean postedOnBoard = false;
    private ArrayList<FileData> files = new ArrayList<FileData>();

    public TimeOffRequestData() {

    }

    public TimeOffRequestData(String name) {
        setName(name);
    }

    public TimeOffRequestData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionId")) {
                setTimeOffRequestId(data.getString("WorkplaceInspectionId"));
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

    public String getTimeOffRequestId() {
        return timeOffRequestId;
    }

    public void setTimeOffRequestId(String timeOffRequestId) {
        this.timeOffRequestId = timeOffRequestId;
    }

    public TimeBankData getTimeBank() {
        return timeBank;
    }

    public void setTimeBank(TimeBankData timeBank) {
        this.timeBank = timeBank;
    }

    public AttendanceCodeData getAttendanceCode() {
        return attendanceCode;
    }

    public void setAttendanceCode(AttendanceCodeData attendanceCode) {
        this.attendanceCode = attendanceCode;
    }

    public ArrayList<DayData> getDays() {
        return days;
    }

    public void setDays(ArrayList<DayData> days) {
        this.days = days;
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
        this.date = DateOperations.getDate(date);
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