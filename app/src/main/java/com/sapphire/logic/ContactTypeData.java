package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactTypeData {
    private String contactTypeId = "";
    private String name = "";
    private String description = "";

    public ContactTypeData() {

    }

    public ContactTypeData(String data) {
        setContactTypeId(data);
    }

    public ContactTypeData(JSONObject data) {
        try {
            if (!data.isNull("ContactTypeId")) {
                setContactTypeId(data.getString("ContactTypeId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setContactTypeId(String contactTypeId) {
        this.contactTypeId = contactTypeId;
    }

    public String getContactTypeId() {
        return contactTypeId;
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
}