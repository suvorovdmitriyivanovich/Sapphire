package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class AppSecurityData {
    private String appSecurityId = "";
    private String name = "";
    private boolean isActive = false;
    private boolean isDisabled = false;
    private String urlRoute = "";
    private String description = "";
    private String parentId = "";
    private String translationId = "";
    private int order = 0;
    private boolean isMenu = false;
    private boolean isAndroid = false;
    private boolean isIos = false;
    private String securityMode = "";
    private String settingMode = "";

    public AppSecurityData() {

    }

    public AppSecurityData(JSONObject data) {
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
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("ParentId")) {
                setParentId(data.getString("ParentId"));
            }
            if (!data.isNull("TranslationId")) {
                setTranslationId(data.getString("TranslationId"));
            }
            if (!data.isNull("Order")) {
                setOrder(data.getInt("Order"));
            }
            if (!data.isNull("IsMenu")) {
                setIsMenu(data.getBoolean("IsMenu"));
            }
            if (!data.isNull("IsAndroid")) {
                setIsAndroid(data.getBoolean("IsAndroid"));
            }
            if (!data.isNull("IsIos")) {
                setIsIos(data.getBoolean("IsIos"));
            }
            if (!data.isNull("SecurityMode")) {
                setSecurityMode(data.getString("SecurityMode"));
            }
            if (!data.isNull("SettingMode")) {
                setSettingMode(data.getString("SettingMode"));
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

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean getDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
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

    public String getTranslationId() {
        return translationId;
    }

    public void setTranslationId(String translationId) {
        this.translationId = translationId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }

    public boolean getIsAndroid() {
        return isAndroid;
    }

    public void setIsAndroid(boolean IsAndroid) {
        this.isAndroid = isAndroid;
    }

    public boolean getIsIos() {
        return isIos;
    }

    public void setIsIos(boolean isIos) {
        this.isIos = isIos;
    }

    public String getSecurityMode() {
        return securityMode;
    }

    public void setSecurityMode(String securityMode) {
        this.securityMode = securityMode;
    }

    public String getSettingMode() {
        return settingMode;
    }

    public void setSettingMode(String settingMode) {
        this.settingMode = settingMode;
    }
}