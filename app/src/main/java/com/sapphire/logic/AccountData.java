package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AccountData {
    private String accountId = "";
    private String accountName = "";
    private String authToken = "";
    private String languageCode = "";
    private OrganizationData currentOrganization;
    private ArrayList<OrganizationData> organizations;
    private ArrayList<NavigationMenuData> navigationMenus;
    private boolean isPasswordRetrieval = false;
    private Long dateCreated = 0l;
    private Long dateUpdated = 0l;

    public AccountData() {

    }

    public AccountData(JSONObject data) {
        try {
            if (!data.isNull("AccountId")) {
                setAccountId(data.getString("AccountId"));
            }
            if (!data.isNull("AccountName")) {
                setAccountName(data.getString("AccountName"));
            }
            if (!data.isNull("AuthToken")) {
                setAuthToken(data.getString("AuthToken"));
            }
            if (!data.isNull("LanguageCode")) {
                setLanguageCode(data.getString("LanguageCode"));
            }
            if (!data.isNull("CurrentOrganization")) {
                setCurrentOrganization(data.getJSONObject("CurrentOrganization"));
            }
            if (!data.isNull("Organizations")) {
                setOrganizations(data.getJSONArray("Organizations"));
            }
            if (!data.isNull("NavigationMenus")) {
                setNavigationMenus(data.getJSONArray("NavigationMenus"));
            }
            if (!data.isNull("IsPasswordRetrieval")) {
                setIsPasswordRetrieval(data.getBoolean("IsPasswordRetrieval"));
            }
            if (!data.isNull("DateCreated")) {
                setDateCreated(data.getString("DateCreated"));
            }
            if (!data.isNull("DateUpdated")) {
                setDateUpdated(data.getString("DateUpdated"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public OrganizationData getCurrentOrganization() {
        return currentOrganization;
    }

    public void setCurrentOrganization(JSONObject currentOrganization) {
        this.currentOrganization = new OrganizationData(currentOrganization);
    }

    public ArrayList<OrganizationData> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(JSONArray organizations) {
        ArrayList<OrganizationData> organizationDatas = new ArrayList<OrganizationData>();
        for (int y=0; y < organizations.length(); y++) {
            try {
                organizationDatas.add(new OrganizationData(organizations.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.organizations = organizationDatas;
    }

    public ArrayList<NavigationMenuData> getNavigationMenus() {
        return navigationMenus;
    }

    public void setNavigationMenus(JSONArray navigationMenus) {
        ArrayList<NavigationMenuData> navigationMenuDatas = new ArrayList<NavigationMenuData>();
        for (int y=0; y < navigationMenus.length(); y++) {
            try {
                navigationMenuDatas.add(new NavigationMenuData(navigationMenus.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.navigationMenus = navigationMenuDatas;
    }

    public boolean getIsPasswordRetrieval() {
        return isPasswordRetrieval;
    }

    public void setIsPasswordRetrieval(boolean isPasswordRetrieval) {
        this.isPasswordRetrieval = isPasswordRetrieval;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        Long dateCreatedLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(dateCreated);
            dateCreatedLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dateCreated = dateCreatedLong;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        Long dateUpdatedLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(dateUpdated);
            dateUpdatedLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dateUpdated = dateUpdatedLong;
    }
}