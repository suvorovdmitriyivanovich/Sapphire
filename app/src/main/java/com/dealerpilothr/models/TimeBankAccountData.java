package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeBankAccountData {
    private String timeBankAccountId = "";
    private String name = "";
    private String description = "";
    private Double state = 0d;
    private String profileId = "";
    private String timeBankId = "";
    private TimeBankData timeBank = new TimeBankData();

    public TimeBankAccountData() {

    }

    public TimeBankAccountData(String timeBankAccountId) {
        this.timeBankAccountId = timeBankAccountId;
    }

    public TimeBankAccountData(JSONObject data) {
        try {
            if (!data.isNull("TimeBankAccountId")) {
                setTimeBankAccountId(data.getString("TimeBankAccountId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("State")) {
                setState(data.getDouble("State"));
            }
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
            if (!data.isNull("TimeBankId")) {
                setTimeBankId(data.getString("TimeBankId"));
            }
            if (!data.isNull("TimeBank")) {
                setTimeBank(data.getJSONObject("TimeBank"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTimeBankAccountId() {
        return timeBankAccountId;
    }

    public void setTimeBankAccountId(String timeBankAccountId) {
        this.timeBankAccountId = timeBankAccountId;
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

    public Double getState() {
        return state;
    }

    public void setState(Double state) {
        this.state = state;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getTimeBankId() {
        return timeBankId;
    }

    public void setTimeBankId(String timeBankId) {
        this.timeBankId = timeBankId;
    }

    public TimeBankData getTimeBank() {
        return timeBank;
    }

    public void setTimeBank(JSONObject timeBank) {
        this.timeBank = new TimeBankData(timeBank);
    }
}