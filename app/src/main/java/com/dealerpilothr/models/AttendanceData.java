package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceData {
    private String attendanceId = "";
    private String timeBankAccountId = "";
    private String attendanceCodeId = "";
    private String profileId = "";
    private String accountName = "";
    private String attendanceCode = "";
    private Double value = 0d;

    public AttendanceData() {

    }

    public AttendanceData(JSONObject data) {
        try {
            if (!data.isNull("AttendanceId")) {
                setAttendanceId(data.getString("AttendanceId"));
            }
            if (!data.isNull("TimeBankAccountId")) {
                setTimeBankAccountId(data.getString("TimeBankAccountId"));
            }
            if (!data.isNull("AttendanceCodeId")) {
                setAttendanceCodeId(data.getString("AttendanceCodeId"));
            }
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
            if (!data.isNull("AccountName")) {
                setAccountName(data.getString("AccountName"));
            }
            if (!data.isNull("AttendanceCode")) {
                setAttendanceCode(data.getString("AttendanceCode"));
            }
            if (!data.isNull("Value")) {
                setValue(data.getDouble("Value"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getTimeBankAccountId() {
        return timeBankAccountId;
    }

    public void setTimeBankAccountId(String timeBankAccountId) {
        this.timeBankAccountId = timeBankAccountId;
    }

    public String getAttendanceCodeId() {
        return attendanceCodeId;
    }

    public void setAttendanceCodeId(String attendanceCodeId) {
        this.attendanceCodeId = attendanceCodeId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAttendanceCode() {
        return attendanceCode;
    }

    public void setAttendanceCode(String attendanceCode) {
        this.attendanceCode = attendanceCode;
    }

    public Double getValue() {
        return value;
    }

    public String getValueString() {
        return String.valueOf(value);
    }

    public void setValue(Double value) {
        this.value = value;
    }
}