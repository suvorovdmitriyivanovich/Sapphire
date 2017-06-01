package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeZoneData {
    private String timeZoneId = "";
    private String name = "";
    private String gMTOffset = "";

    public TimeZoneData() {

    }

    public TimeZoneData(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public TimeZoneData(JSONObject data) {
        try {
            if (!data.isNull("TimeZoneId")) {
                setTimeZoneId(data.getString("TimeZoneId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("GMTOffset")) {
                setGMTOffset(data.getString("GMTOffset"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGMTOffset() {
        return gMTOffset;
    }

    public void setGMTOffset(String gMTOffset) {
        this.gMTOffset = gMTOffset;
    }
}