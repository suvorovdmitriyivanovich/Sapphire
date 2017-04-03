package com.sapphire.models;

import com.sapphire.utils.DateOperations;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayData {
    private Long date = 0l;
    private Double ammount = 0.0;

    public DayData() {

    }

    public DayData(Long date) {
        setDate(date);
    }

    public DayData(JSONObject data) {
        try {
            if (!data.isNull("Date")) {
                setDate(data.getLong("Date"));
            }
            if (!data.isNull("Ammount")) {
                setAmmount(data.getDouble("Ammount"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDate(Long date) {
        this.date = date;
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