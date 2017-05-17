package com.sapphire.models;

import com.sapphire.utils.DateOperations;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayData {

    private String timeoffRequestDayId = "";
    private String timeoffRequestId = "";
    private Long date = 0l;
    private Double ammount = 0.0;

    public DayData() {

    }

    public DayData(Long date) {
        setDate(date);
    }

    public DayData(JSONObject data) {
        try {
            if (!data.isNull("TimeoffRequestDayId")) {
                setTimeoffRequestDayId(data.getString("TimeoffRequestDayId"));
            }
            if (!data.isNull("TimeoffRequestId")) {
                setTimeoffRequestId(data.getString("TimeoffRequestId"));
            }
            if (!data.isNull("Date")) {
                setDate(data.getString("Date"));
            }
            if (!data.isNull("Ammount")) {
                setAmmount(data.getDouble("Ammount"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTimeoffRequestDayId() {
        return timeoffRequestDayId;
    }

    public void setTimeoffRequestDayId(String timeoffRequestDayId) {
        this.timeoffRequestDayId = timeoffRequestDayId;
    }

    public String getTimeoffRequestId() {
        return timeoffRequestId;
    }

    public void setTimeoffRequestId(String timeoffRequestId) {
        this.timeoffRequestId = timeoffRequestId;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date = DateOperations.getDate(date);
    }

    public Long getDate() {
        return date;
    }

    public String getDateString() {
        String dateString = "";
        if (date != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(date);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public String getDateServer() {
        return DateOperations.getDateServer(date);
    }

    public Double getAmmount() {
        return ammount;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

    public void setAmmount(String ammount) {
        try {
            this.ammount = Double.parseDouble(ammount); // Make use of autoboxing.  It's also easier to read.
        } catch (NumberFormatException e) {
            this.ammount = 0d;
        }
    }
}