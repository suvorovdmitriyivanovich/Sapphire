package com.sapphire.models;

import com.sapphire.logic.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class OrganizationData {
    private String organizationId = "";
    private String systemFolderId = "";
    private String name = "";
    private String domain = "";
    private String logoId = "";
    private TimeZoneData timeZone = new TimeZoneData();
    private String accountOrganizationRoles = "";
    private ArrayList<OrganizationStructureData> accountCurrentOrganizationStructures = new ArrayList<OrganizationStructureData>();
    private ArrayList<AppSecurityData> accountOrganizationAppSecurities = new ArrayList<AppSecurityData>();
    private ArrayList<AppSecurityData> accountOrganizationGlobalAppSecurities = new ArrayList<AppSecurityData>();

    public OrganizationData() {

    }

    public OrganizationData(JSONObject data) {
        try {
            if (!data.isNull("TimeZone")) {
                setTimeZone(data.getJSONObject("TimeZone"));
            }
            if (!data.isNull("OrganizationId")) {
                setOrganizationId(data.getString("OrganizationId"));
            }
            if (!data.isNull("SystemFolderId")) {
                setSystemFolderId(data.getString("SystemFolderId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Domain")) {
                setDomain(data.getString("Domain"));
            }
            if (!data.isNull("LogoId")) {
                setLogoId(data.getString("LogoId"));
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

    public String getSystemFolderId() {
        return systemFolderId;
    }

    public void setSystemFolderId(String systemFolderId) {
        this.systemFolderId = systemFolderId;
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

    public String getLogoId() {
        return logoId;
    }

    public void setLogoId(String logoId) {
        this.logoId = logoId;
    }

    public TimeZoneData getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(JSONObject timeZone) {
        this.timeZone = new TimeZoneData(timeZone);
        UserInfo.getUserInfo().setTimeZone(this.timeZone);
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

    public ArrayList<AppSecurityData> getAccountOrganizationAppSecurities() {
        return accountOrganizationAppSecurities;
    }

    public void setAccountOrganizationAppSecurities(JSONArray accountOrganizationAppSecurities) {
        ArrayList<AppSecurityData> appSecuritiesDatas = new ArrayList<AppSecurityData>();
        for (int y=0; y < accountOrganizationAppSecurities.length(); y++) {
            try {
                appSecuritiesDatas.add(new AppSecurityData(accountOrganizationAppSecurities.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.accountOrganizationAppSecurities = appSecuritiesDatas;
    }

    public ArrayList<AppSecurityData> getAccountOrganizationGlobalAppSecurities() {
        return accountOrganizationGlobalAppSecurities;
    }

    public void setAccountOrganizationGlobalAppSecurities(JSONArray accountOrganizationGlobalAppSecurities) {
        ArrayList<AppSecurityData> appSecuritiesDatas = new ArrayList<AppSecurityData>();
        for (int y=0; y < accountOrganizationGlobalAppSecurities.length(); y++) {
            try {
                appSecuritiesDatas.add(new AppSecurityData(accountOrganizationGlobalAppSecurities.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.accountOrganizationGlobalAppSecurities = appSecuritiesDatas;
    }
}