package com.sapphire.models;

import com.sapphire.utils.DateOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PolicyData {
    private String policyId = "";
    private String name = "";
    private boolean isAcknowledged = false;
    private String description = "";
    private String parentId = "";
    private SettingData settings = new SettingData();
    private boolean isDisabled = false;
    private boolean isActive = false;
    private Long dateModified = 0l;
    private DurationData duration = new DurationData();
    private Long dateCreated = 0l;
    private String fileId = "";
    private Long acknowledgedOn = 0l;
    private ArrayList<PolicyData> subPolicies = new ArrayList<PolicyData>();

    public PolicyData() {

    }

    public PolicyData(JSONObject data) {
        try {
            if (!data.isNull("PolicyId")) {
                setPolicyId(data.getString("PolicyId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("IsAcknowledged")) {
                setIsAcknowledged(data.getBoolean("IsAcknowledged"));
            }
            if (!data.isNull("AcknowledgedOn")) {
                setAcknowledgedOn(data.getString("AcknowledgedOn"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("ParentId")) {
                setParentId(data.getString("ParentId"));
            }
            if (!data.isNull("Settings")) {
                setSettings(data.getJSONObject("Settings"));
            }
            if (!data.isNull("IsDisabled")) {
                setIsDisabled(data.getBoolean("IsDisabled"));
            }
            if (!data.isNull("IsActive")) {
                setIsActive(data.getBoolean("IsActive"));
            }
            if (!data.isNull("DateModified")) {
                setDateModified(data.getString("DateModified"));
            }
            if (!data.isNull("Duration")) {
                setDuration(data.getJSONObject("Duration"));
            }
            if (!data.isNull("DateCreated")) {
                setDateCreated(data.getString("DateCreated"));
            }
            if (!data.isNull("FileId")) {
                setFileId(data.getString("FileId"));
            }
            if (!data.isNull("SubPolicies")) {
                setSubPolicies(data.getJSONArray("SubPolicies"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsAcknowledged() {
        return isAcknowledged;
    }

    public void setIsAcknowledged(boolean isAcknowledged) {
        this.isAcknowledged = isAcknowledged;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setSettings(JSONObject settings) {
        this.settings = new SettingData(settings);
    }

    public SettingData getSettings() {
        return settings;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = DateOperations.getDate(dateModified);
    }

    public ArrayList<PolicyData> getSubPolicies() {
        return subPolicies;
    }

    public void setSubPolicies(JSONArray subPolicies) {
        ArrayList<PolicyData> policiesDatas = new ArrayList<PolicyData>();
        for (int y=0; y < subPolicies.length(); y++) {
            try {
                policiesDatas.add(new PolicyData(subPolicies.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.subPolicies = policiesDatas;
    }

    public void setDuration(JSONObject duration) {
        this.duration = new DurationData(duration);
    }

    public DurationData getDuration() {
        return duration;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = DateOperations.getDate(dateCreated);
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Long getAcknowledgedOn() {
        return acknowledgedOn;
    }

    public void setAcknowledgedOn(String acknowledgedOn) {
        this.acknowledgedOn = DateOperations.getDate(acknowledgedOn);
    }
}