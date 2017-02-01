package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrganizationStructureData {
    private String organizationStructureId = "";
    private String parentId = "";
    private String name = "";
    private boolean isPosition = false;
    private boolean isLinked = false;
    private ArrayList<AppSecuritiesData> accountOrganizationStructureAppSecurities = new ArrayList<AppSecuritiesData>();

    public OrganizationStructureData() {

    }

    public OrganizationStructureData(JSONObject data) {
        try {
            if (!data.isNull("OrganizationStructureId")) {
                setOrganizationStructureId(data.getString("OrganizationStructureId"));
            }
            if (!data.isNull("ParentId")) {
                setParentId(data.getString("ParentId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("IsPosition")) {
                setIsPosition(data.getBoolean("IsPosition"));
            }
            if (!data.isNull("IsLinked")) {
                setIsLinked(data.getBoolean("IsLinked"));
            }
            if (!data.isNull("AccountOrganizationStructureAppSecurities")) {
                setAccountOrganizationStructureAppSecurities(data.getJSONArray("AccountOrganizationStructureAppSecurities"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getOrganizationStructureId() {
        return organizationStructureId;
    }

    public void setOrganizationStructureId(String organizationStructureId) {
        this.organizationStructureId = organizationStructureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean getIsPosition() {
        return isPosition;
    }

    public void setIsPosition(boolean isPosition) {
        this.isPosition = isPosition;
    }

    public boolean getIsLinked() {
        return isLinked;
    }

    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    public ArrayList<AppSecuritiesData> getAccountOrganizationStructureAppSecurities() {
        return accountOrganizationStructureAppSecurities;
    }

    public void setAccountOrganizationStructureAppSecurities(JSONArray accountOrganizationStructureAppSecurities) {
        ArrayList<AppSecuritiesData> appSecuritiesDatas = new ArrayList<AppSecuritiesData>();
        for (int y=0; y < accountOrganizationStructureAppSecurities.length(); y++) {
            try {
                appSecuritiesDatas.add(new AppSecuritiesData(accountOrganizationStructureAppSecurities.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.accountOrganizationStructureAppSecurities = appSecuritiesDatas;
    }
}