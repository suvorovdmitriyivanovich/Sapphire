package com.dealerpilothr.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class TimeBankData {
    private String timeBankId = "";
    private String name = "";
    private String description = "";
    private String frequencyId = "";
    private int resetDay = 0;
    private int resetMonth = 0;
    private boolean isVisible = false;
    private boolean allowNegativeEntry = false;
    private boolean allowRequestToNegative = false;
    private boolean isPaid = false;
    private SettingData settings = new SettingData();
    private ArrayList<AttendanceCodeData> attendanceCodes = new ArrayList<AttendanceCodeData>();

    public TimeBankData() {

    }

    public TimeBankData(String timeBankId) {
        this.timeBankId = timeBankId;
    }

    public TimeBankData(JSONObject data) {
        try {
            if (!data.isNull("TimeBankId")) {
                setTimeBankId(data.getString("TimeBankId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("FrequencyId")) {
                setFrequencyId(data.getString("FrequencyId"));
            }
            if (!data.isNull("ResetDay")) {
                setResetDay(data.getInt("ResetDay"));
            }
            if (!data.isNull("ResetMonth")) {
                setResetMonth(data.getInt("ResetMonth"));
            }
            if (!data.isNull("IsVisible")) {
                setIsVisible(data.getBoolean("IsVisible"));
            }
            if (!data.isNull("AllowNegativeEntry")) {
                setAllowNegativeEntry(data.getBoolean("AllowNegativeEntry"));
            }
            if (!data.isNull("AllowRequestToNegative")) {
                setAllowRequestToNegative(data.getBoolean("AllowRequestToNegative"));
            }
            if (!data.isNull("IsPaid")) {
                setIsPaid(data.getBoolean("IsPaid"));
            }
            if (!data.isNull("Settings")) {
                setSettings(data.getJSONObject("Settings"));
            }
            if (!data.isNull("AttendanceCodes")) {
                setAttendanceCodes(data.getJSONArray("AttendanceCodes"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTimeBankId() {
        return timeBankId;
    }

    public void setTimeBankId(String timeBankId) {
        this.timeBankId = timeBankId;
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

    public String getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(String frequencyId) {
        this.frequencyId = frequencyId;
    }

    public int getResetDay() {
        return resetDay;
    }

    public void setResetDay(int resetDay) {
        this.resetDay = resetDay;
    }

    public int getResetMonth() {
        return resetMonth;
    }

    public void setResetMonth(int resetMonth) {
        this.resetMonth = resetMonth;
    }

    public boolean getVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean getAllowNegativeEntry() {
        return allowNegativeEntry;
    }

    public void setAllowNegativeEntry(boolean allowNegativeEntry) {
        this.allowNegativeEntry = allowNegativeEntry;
    }

    public boolean getAllowRequestToNegative() {
        return allowRequestToNegative;
    }

    public void setAllowRequestToNegative(boolean allowRequestToNegative) {
        this.allowRequestToNegative = allowRequestToNegative;
    }

    public boolean getPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }

    public SettingData getSettings() {
        return settings;
    }

    public void setSettings(JSONObject settings) {
        this.settings = new SettingData(settings);
    }

    public ArrayList<AttendanceCodeData> getAttendanceCodes() {
        return attendanceCodes;
    }

    public void setAttendanceCodes(JSONArray attendanceCodes) {
        ArrayList<AttendanceCodeData> attendanceCodeDatas = new ArrayList<AttendanceCodeData>();
        for (int y=0; y < attendanceCodes.length(); y++) {
            try {
                attendanceCodeDatas.add(new AttendanceCodeData(attendanceCodes.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.attendanceCodes = attendanceCodeDatas;
    }
}