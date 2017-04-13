package com.sapphire.models;

import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeOffRequestData {
    private String timeoffRequestId = "";
    private String organizationId = "";
    private String profileId = "";
    private String timeoffRequestStatusId = "";
    private String timeBankName = "";
    private String attendanceCodeId = "";
    private String employeeName = "";
    private String department = "";
    private Long requestDate = 0l;
    private ArrayList<DayData> days = new ArrayList<DayData>();

    public TimeOffRequestData() {

    }

    public TimeOffRequestData(JSONObject data) {
        try {
            if (!data.isNull("TimeoffRequestId")) {
                setTimeoffRequestId(data.getString("TimeoffRequestId"));
            }
            if (!data.isNull("OrganizationId")) {
                setOrganizationId(data.getString("OrganizationId"));
            }
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
            if (!data.isNull("TimeoffRequestStatusId")) {
                setTimeoffRequestStatusId(data.getString("TimeoffRequestStatusId"));
            }
            if (!data.isNull("TimeBankName")) {
                setTimeBankName(data.getString("TimeBankName"));
            }
            if (!data.isNull("AttendanceCodeId")) {
                setAttendanceCodeId(data.getString("AttendanceCodeId"));
            }
            if (!data.isNull("EmployeeName")) {
                setEmployeeName(data.getString("EmployeeName"));
            }
            if (!data.isNull("Department")) {
                setDepartment(data.getString("Department"));
            }
            if (!data.isNull("RequestDate")) {
                setRequestDate(data.getString("RequestDate"));
            }
            if (!data.isNull("Days")) {
                setDays(data.getJSONArray("Days"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTimeoffRequestId() {
        return timeoffRequestId;
    }

    public void setTimeoffRequestId(String timeoffRequestId) {
        this.timeoffRequestId = timeoffRequestId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getTimeoffRequestStatusId() {
        return timeoffRequestStatusId;
    }

    public void setTimeoffRequestStatusId(String timeoffRequestStatusId) {
        this.timeoffRequestStatusId = timeoffRequestStatusId;
    }

    public String getTimeBankName() {
        return timeBankName;
    }

    public void setTimeBankName(String timeBankName) {
        this.timeBankName = timeBankName;
    }

    public String getAttendanceCodeId() {
        return attendanceCodeId;
    }

    public void setAttendanceCodeId(String attendanceCodeId) {
        this.attendanceCodeId = attendanceCodeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList<DayData> getDays() {
        return days;
    }

    public void setDays(ArrayList<DayData> days) {
        this.days = days;
    }

    public void setDays(JSONArray days) {
        ArrayList<DayData> dayDatas = new ArrayList<DayData>();
        for (int y=0; y < days.length(); y++) {
            try {
                dayDatas.add(new DayData(days.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.days = dayDatas;
    }

    public Long getRequestDate() {
        return requestDate;
    }

    public String getRequestDateString() {
        String dateString = "";
        if (requestDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date dateT = new Date();
            dateT.setTime(requestDate);
            dateString = format.format(dateT);
        }
        return dateString;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = DateOperations.getDate(requestDate);
    }
}