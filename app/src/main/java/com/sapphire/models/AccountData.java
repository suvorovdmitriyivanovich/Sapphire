package com.sapphire.models;

import com.sapphire.utils.DateOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AccountData {
    private String accountId = "";
    private String accountName = "";
    private String authToken = "";
    private String languageCode = "";
    private OrganizationData currentOrganization = new OrganizationData();
    private ArrayList<OrganizationData> organizations = new ArrayList<OrganizationData>();
    private ArrayList<NavigationMenuData> navigationMenus = new ArrayList<NavigationMenuData>();
    private ProfileData currentProfile = new ProfileData();
    private boolean isPasswordRetrieval = false;
    private Long dateCreated = 0l;
    private Long dateUpdated = 0l;
    private AppRoleAppSecurityData globalAppRoleAppSecurities = new AppRoleAppSecurityData();

    public AccountData() {

    }

    public AccountData(JSONObject data) {
        try {
            if (!data.isNull("CurrentOrganization")) {
                setCurrentOrganization(data.getJSONObject("CurrentOrganization"));
            }
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
            if (!data.isNull("CurrentProfile")) {
                setCurrentProfile(data.getJSONObject("CurrentProfile"));
            }
            if (!data.isNull("GlobalAppRoleAppSecurities")) {
                setGlobalAppRoleAppSecurities(data.getJSONObject("GlobalAppRoleAppSecurities"));
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
                if (!navigationMenus.getJSONObject(y).isNull("IsAndroid") && navigationMenus.getJSONObject(y).getBoolean("IsAndroid")) {
                    navigationMenuDatas.add(new NavigationMenuData(navigationMenus.getJSONObject(y)));
                }
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
        this.dateCreated = DateOperations.getDate(dateCreated);
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = DateOperations.getDate(dateUpdated);
    }

    public ProfileData getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(JSONObject currentProfile) {
        this.currentProfile = new ProfileData(currentProfile);
    }

    public AppRoleAppSecurityData getGlobalAppRoleAppSecurities() {
        return globalAppRoleAppSecurities;
    }

    public void setGlobalAppRoleAppSecurities(JSONObject globalAppRoleAppSecurities) {
        this.globalAppRoleAppSecurities = new AppRoleAppSecurityData(globalAppRoleAppSecurities);
    }
}