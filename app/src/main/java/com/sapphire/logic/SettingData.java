package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingData {
    private boolean canAdd = false;
    private boolean canUpdate = false;
    private boolean canDelete = false;
    private boolean canRead = false;
    private boolean canDownload = false;

    public SettingData() {

    }

    public SettingData(JSONObject data) {
        try {
            if (!data.isNull("CanAdd")) {
                setCanAdd(data.getBoolean("CanAdd"));
            }
            if (!data.isNull("CanUpdate")) {
                setCanUpdate(data.getBoolean("CanUpdate"));
            }
            if (!data.isNull("CanDelete")) {
                setCanDelete(data.getBoolean("CanDelete"));
            }
            if (!data.isNull("CanRead")) {
                setCanRead(data.getBoolean("CanRead"));
            }
            if (!data.isNull("CanDownload")) {
                setCanDownload(data.getBoolean("CanDownload"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean getCanAdd() {
        return canAdd;
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean getCanDownload() {
        return canDownload;
    }

    public void setCanDownload(boolean canDownload) {
        this.canDownload = canDownload;
    }
}