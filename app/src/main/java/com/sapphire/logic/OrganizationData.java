package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractList;
import java.util.ArrayList;

public class OrganizationData {
    private String organizationId = "";
    private String name = "";
    private String domain = "";
    private String accountOrganizationRoles = "";
    private ArrayList<OrganizationStructureData> accountCurrentOrganizationStructures;
    private ArrayList<AppSecuritiesData> accountOrganizationAppSecurities;
    private ArrayList<AppSecuritiesData> accountOrganizationGlobalAppSecurities;

    public OrganizationData() {

    }

    public OrganizationData(JSONObject data) {
        try {
            if (!data.isNull("OrganizationId")) {
                setOrganizationId(data.getString("OrganizationId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Domain")) {
                setDomain(data.getString("Domain"));
            }
            if (!data.isNull("AccountOrganizationRoles")) {
                setAccountOrganizationRoles(data.getString("AccountOrganizationRoles"));
            }
            if (!data.isNull("AccountCurrentOrganizationStructures")) {
                setAccountCurrentOrganizationStructures(data.getJSONArray("AccountCurrentOrganizationStructures"));
            }
            if (!data.isNull("AccountOrganizationAppSecurities")) {
                setAccountOrganizationAppSecurities(data.getJSONArray("AccountOrganizationAppSecurities"));
            }
            if (!data.isNull("AccountOrganizationGlobalAppSecurities")) {
                setAccountOrganizationGlobalAppSecurities(data.getJSONArray("AccountOrganizationGlobalAppSecurities"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAccountOrganizationRoles() {
        return accountOrganizationRoles;
    }

    public void setAccountOrganizationRoles(String accountOrganizationRoles) {
        this.accountOrganizationRoles = accountOrganizationRoles;
    }

    public ArrayList<OrganizationStructureData> getAccountCurrentOrganizationStructures() {
        return accountCurrentOrganizationStructures;
    }

    public void setAccountCurrentOrganizationStructures(JSONArray accountCurrentOrganizationStructures) {
        ArrayList<OrganizationStructureData> organizationStructureDatas = new ArrayList<OrganizationStructureData>();
        for (int y=0; y < accountCurrentOrganizationStructures.length(); y++) {
            try {
                organizationStructureDatas.add(new OrganizationStructureData(accountCurrentOrganizationStructures.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.accountCurrentOrganizationStructures = organizationStructureDatas;
    }

    public ArrayList<AppSecuritiesData> getAccountOrganizationAppSecurities() {
        return accountOrganizationAppSecurities;
    }

    public void setAccountOrganizationAppSecurities(JSONArray accountOrganizationAppSecurities) {
        ArrayList<AppSecuritiesData> appSecuritiesDatas = new ArrayList<AppSecuritiesData>();
        for (int y=0; y < accountOrganizationAppSecurities.length(); y++) {
            try {
                appSecuritiesDatas.add(new AppSecuritiesData(accountOrganizationAppSecurities.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.accountOrganizationAppSecurities = appSecuritiesDatas;
    }

    public ArrayList<AppSecuritiesData> getAccountOrganizationGlobalAppSecurities() {
        return accountOrganizationGlobalAppSecurities;
    }

    public void setAccountOrganizationGlobalAppSecurities(JSONArray accountOrganizationGlobalAppSecurities) {
        ArrayList<AppSecuritiesData> appSecuritiesDatas = new ArrayList<AppSecuritiesData>();
        for (int y=0; y < accountOrganizationGlobalAppSecurities.length(); y++) {
            try {
                appSecuritiesDatas.add(new AppSecuritiesData(accountOrganizationGlobalAppSecurities.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.accountOrganizationGlobalAppSecurities = appSecuritiesDatas;
    }
}