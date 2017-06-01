package com.dealerpilothr.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AttendanceCodeData {
    private String attendanceCodeId = "";
    private String name = "";
    private String description = "";
    private boolean isPaid = false;
    private Double defaultHours = 0.0;
    private boolean isRequestAvailable = false;
    private boolean replaceShift = false;
    private boolean promptToReplace = false;
    private boolean replaceHours = false;
    private boolean isVisible = false;
    private String attendanceCodeTypeId = "";
    private String canBeAttachedToBank = "";
    private SettingData settings = new SettingData();
    private String attendanceCodeType = "";
    private ArrayList<TimeBankAccountData> timeBanks = new ArrayList<TimeBankAccountData>();

    public AttendanceCodeData() {

    }

    public AttendanceCodeData(String attendanceCodeId) {
        this.attendanceCodeId = attendanceCodeId;
    }

    public AttendanceCodeData(JSONObject data) {
        try {
            if (!data.isNull("AttendanceCodeId")) {
                setAttendanceCodeId(data.getString("AttendanceCodeId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("IsPaid")) {
                setIsPaid(data.getBoolean("IsPaid"));
            }
            if (!data.isNull("DefaultHours")) {
                setDefaultHours(data.getDouble("DefaultHours"));
            }
            if (!data.isNull("IsRequestAvailable")) {
                setIsRequestAvailable(data.getBoolean("IsRequestAvailable"));
            }
            if (!data.isNull("ReplaceShift")) {
                setReplaceShift(data.getBoolean("ReplaceShift"));
            }
            if (!data.isNull("PromptToReplace")) {
                setPromptToReplace(data.getBoolean("PromptToReplace"));
            }
            if (!data.isNull("ReplaceHours")) {
                setReplaceHours(data.getBoolean("ReplaceHours"));
            }
            if (!data.isNull("IsVisible")) {
                setIsVisible(data.getBoolean("IsVisible"));
            }
            if (!data.isNull("AttendanceCodeTypeId")) {
                setAttendanceCodeTypeId(data.getString("AttendanceCodeTypeId"));
            }
            if (!data.isNull("CanBeAttachedToBank")) {
                setCanBeAttachedToBank(data.getString("CanBeAttachedToBank"));
            }
            if (!data.isNull("Settings")) {
                setSettings(data.getJSONObject("Settings"));
            }
            if (!data.isNull("AttendanceCodeType")) {
                setAttendanceCodeType(data.getString("AttendanceCodeType"));
            }
            if (!data.isNull("TimeBanks")) {
                setTimeBanks(data.getJSONArray("TimeBanks"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAttendanceCodeId() {
        return attendanceCodeId;
    }

    public void setAttendanceCodeId(String attendanceCodeId) {
        this.attendanceCodeId = attendanceCodeId;
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

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Double getDefaultHours() {
        return defaultHours;
    }

    public void setDefaultHours(Double defaultHours) {
        this.defaultHours = defaultHours;
    }

    public boolean getIsRequestAvailable() {
        return isRequestAvailable;
    }

    public void setIsRequestAvailable(boolean isRequestAvailable) {
        this.isRequestAvailable = isRequestAvailable;
    }

    public boolean getReplaceShift() {
        return replaceShift;
    }

    public void setReplaceShift(boolean replaceShift) {
        this.replaceShift = replaceShift;
    }

    public boolean getPromptToReplace() {
        return promptToReplace;
    }

    public void setPromptToReplace(boolean promptToReplace) {
        this.promptToReplace = promptToReplace;
    }

    public boolean getReplaceHours() {
        return replaceHours;
    }

    public void setReplaceHours(boolean replaceHours) {
        this.replaceHours = replaceHours;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getAttendanceCodeTypeId() {
        return attendanceCodeTypeId;
    }

    public void setAttendanceCodeTypeId(String attendanceCodeTypeId) {
        this.attendanceCodeTypeId = attendanceCodeTypeId;
    }

    public String getCanBeAttachedToBank() {
        return canBeAttachedToBank;
    }

    public void setCanBeAttachedToBank(String canBeAttachedToBank) {
        this.canBeAttachedToBank = canBeAttachedToBank;
    }

    public SettingData getSettings() {
        return settings;
    }

    public void setSettings(JSONObject settings) {
        this.settings = new SettingData(settings);
    }

    public String getAttendanceCodeType() {
        return attendanceCodeType;
    }

    public void setAttendanceCodeType(String attendanceCodeType) {
        this.attendanceCodeType = attendanceCodeType;
    }

    public ArrayList<TimeBankAccountData> getTimeBanks() {
        return timeBanks;
    }

    public void setTimeBanks(JSONArray timeBanks) {
        ArrayList<TimeBankAccountData> timeBankDatas = new ArrayList<TimeBankAccountData>();
        for (int y=0; y < timeBanks.length(); y++) {
            try {
                timeBankDatas.add(new TimeBankAccountData(timeBanks.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.timeBanks = timeBankDatas;
    }
}