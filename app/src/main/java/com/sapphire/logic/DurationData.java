package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DurationData {
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;

    public DurationData() {

    }

    public DurationData(JSONObject data) {
        try {
            if (!data.isNull("Hours")) {
                setHours(data.getInt("Hours"));
            }
            if (!data.isNull("Minutes")) {
                setMinutes(data.getInt("Minutes"));
            }
            if (!data.isNull("Seconds")) {
                setSeconds(data.getInt("Seconds"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}