package com.sapphire.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class OrganizationStructureData {
    private boolean isOrganization = false;
    private String description = "";
    private String parentId = "";
    private String avatarId = "";
    private SettingData settings = new SettingData();
    private boolean isPosition = false;
    private boolean isEmployee = false;
    private boolean isOrganizationOrganizationStructure = false;
    private String organizationOrganizationStructureId = "";
    private String name = "";
    private boolean isRoot = false;
    private boolean isLinked = false;
    private String linked = "";
    private boolean isOrganizationStructure = false;
    private boolean isOrganizationCurrent = false;
    private String id = "";
    private boolean isCurrent = false;
    private ArrayList<OrganizationStructureData> subOrganizationStructures = new ArrayList<OrganizationStructureData>();

    public OrganizationStructureData() {

    }

    public OrganizationStructureData(JSONObject data) {
        try {
            if (!data.isNull("Id")) {
                setId(data.getString("Id"));
            }
            if (!data.isNull("ParentId")) {
                setParentId(data.getString("ParentId"));
            }
            if (!data.isNull("OrganizationOrganizationStructureId")) {
                setOrganizationOrganizationStructureId(data.getString("OrganizationOrganizationStructureId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Settings")) {
                setSettings(data.getJSONObject("Settings"));
            }
            if (!data.isNull("IsCurrent")) {
                setIsCurrent(data.getBoolean("IsCurrent"));
            }
            if (!data.isNull("IsOrganization")) {
                setIsOrganization(data.getBoolean("IsOrganization"));
            }
            if (!data.isNull("IsOrganizationCurrent")) {
                setIsOrganizationCurrent(data.getBoolean("IsOrganizationCurrent"));
            }
            if (!data.isNull("IsOrganizationStructure")) {
                setIsOrganizationStructure(data.getBoolean("IsOrganizationStructure"));
            }
            if (!data.isNull("IsPosition")) {
                setIsPosition(data.getBoolean("IsPosition"));
            }
            if (!data.isNull("IsEmployee")) {
                setIsEmployee(data.getBoolean("IsEmployee"));
            }
            if (!data.isNull("IsLinked")) {
                setIsLinked(data.getBoolean("IsLinked"));
            }
            if (!data.isNull("Linked")) {
                setLinked(data.getString("Linked"));
            }
            if (!data.isNull("IsRoot")) {
                setIsRoot(data.getBoolean("IsRoot"));
            }
            if (!data.isNull("AvatarId")) {
                setAvatarId(data.getString("AvatarId"));
            }
            if (!data.isNull("IsOrganizationOrganizationStructure")) {
                setIsOrganizationOrganizationStructure(data.getBoolean("IsOrganizationOrganizationStructure"));
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrganizationOrganizationStructureId() {
        return organizationOrganizationStructureId;
    }

    public void setOrganizationOrganizationStructureId(String organizationOrganizationStructureId) {
        this.organizationOrganizationStructureId = organizationOrganizationStructureId;
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

    public SettingData getSettings() {
        return settings;
    }

    public void setSettings(JSONObject settings) {
        this.settings = new SettingData(settings);
    }

    public boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public boolean getIsOrganization() {
        return isOrganization;
    }

    public void setIsOrganization(boolean isOrganization) {
        this.isOrganization = isOrganization;
    }

    public boolean getIsOrganizationCurrent() {
        return isOrganizationCurrent;
    }

    public void setIsOrganizationCurrent(boolean isOrganizationCurrent) {
        this.isOrganizationCurrent = isOrganizationCurrent;
    }

    public boolean getIsOrganizationStructure() {
        return isOrganizationStructure;
    }

    public void setIsOrganizationStructure(boolean isOrganizationStructure) {
        this.isOrganizationStructure = isOrganizationStructure;
    }

    public boolean getIsPosition() {
        return isPosition;
    }

    public void setIsPosition(boolean isPosition) {
        this.isPosition = isPosition;
    }

    public boolean getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    public boolean getIsLinked() {
        return isLinked;
    }

    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    public String getLinked() {
        return linked;
    }

    public void setLinked(String linked) {
        this.linked = linked;
    }

    public boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public ArrayList<OrganizationStructureData> getSubOrganizationStructures() {
        return subOrganizationStructures;
    }

    public boolean getIsOrganizationOrganizationStructure() {
        return isOrganizationOrganizationStructure;
    }

    public void setIsOrganizationOrganizationStructure(boolean isOrganizationOrganizationStructure) {
        this.isOrganizationOrganizationStructure = isOrganizationOrganizationStructure;
    }

    public void setSubOrganizationStructures(JSONArray subOrganizationStructures) {
        ArrayList<OrganizationStructureData> organizationStructureDatas = new ArrayList<OrganizationStructureData>();
        for (int y=0; y < subOrganizationStructures.length(); y++) {
            try {
                organizationStructureDatas.add(new OrganizationStructureData(subOrganizationStructures.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.subOrganizationStructures = organizationStructureDatas;
    }
}