package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TypeData {
    private String id = "";
    private String name = "";
    private boolean canHaveChildren = false;

    public TypeData() {

    }

    public TypeData(String id) {
        this.id = id;
    }

    public TypeData(JSONObject data) {
        try {
            if (!data.isNull("TaskTypeId")) {
                setId(data.getString("TaskTypeId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("CanHaveChildren")) {
                setCanHaveChildren(data.getBoolean("CanHaveChildren"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getCanHaveChildren() {
        return canHaveChildren;
    }

    public void setCanHaveChildren(boolean canHaveChildren) {
        this.canHaveChildren = canHaveChildren;
    }
}