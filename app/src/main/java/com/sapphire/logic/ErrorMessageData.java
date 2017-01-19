package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorMessageData {
    private String id = "";
    private String name = "";

    public ErrorMessageData() {

    }

    public ErrorMessageData(JSONObject data) {
        try {
            if (!data.isNull("Id")) {
                setId(data.getString("Id"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}