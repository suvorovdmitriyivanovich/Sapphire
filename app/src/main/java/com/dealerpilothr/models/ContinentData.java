package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ContinentData {
    private String continentId = "";
    private String name = "";
    private String code = "";

    public ContinentData() {

    }

    public ContinentData(JSONObject data) {
        try {
            if (!data.isNull("ContinentId")) {
                setContinentId(data.getString("ContinentId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Code")) {
                setCode(data.getString("Code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getContinentId() {
        return continentId;
    }

    public void setContinentId(String continentId) {
        this.continentId = continentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}