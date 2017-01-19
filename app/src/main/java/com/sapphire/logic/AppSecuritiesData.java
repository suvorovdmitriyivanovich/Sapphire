package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class AppSecuritiesData {
    private String appSecurityId = "";
    private String name = "";
    private boolean isActive = false;
    private boolean isDisabled = false;
    private String urlRoute = "";

    public AppSecuritiesData() {

    }

    public AppSecuritiesData(JSONObject data) {
        try {
            if (!data.isNull("AppSecurityId")) {
                setAppSecurityId(data.getString("AppSecurityId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("IsActive")) {
                setIsActive(data.getBoolean("IsActive"));
            }
            if (!data.isNull("IsDisabled")) {
                setIsDisabled(data.getBoolean("IsDisabled"));
            }
            if (!data.isNull("UrlRoute")) {
                setUrlRoute(data.getString("UrlRoute"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAppSecurityId() {
        return appSecurityId;
    }

    public void setAppSecurityId(String appSecurityId) {
        this.appSecurityId = appSecurityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getUrlRoute() {
        return urlRoute;
    }

    public void setUrlRoute(String urlRoute) {
        this.urlRoute = urlRoute;
    }
}