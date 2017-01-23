package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class NavigationMenuData {
    private String menuId = "";
    private String name = "";
    private String parentId = "";
    private boolean isActive = false;
    private boolean isDisabled = false;
    private String translationId = "";
    private String order = "";
    private String urlRoute = "";
    private String cssClass = "";
    private ArrayList<NavigationMenuData> subMenus = new ArrayList<NavigationMenuData>();

    public NavigationMenuData() {

    }

    public NavigationMenuData(JSONObject data) {
        try {
            if (!data.isNull("MenuId")) {
                setMenuId(data.getString("MenuId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("ParentId")) {
                setParentId(data.getString("ParentId"));
            }
            if (!data.isNull("IsActive")) {
                setIsActive(data.getBoolean("IsActive"));
            }
            if (!data.isNull("IsDisabled")) {
                setIsDisabled(data.getBoolean("IsDisabled"));
            }
            if (!data.isNull("TranslationId")) {
                setTranslationId(data.getString("TranslationId"));
            }
            if (!data.isNull("Order")) {
                setOrder(data.getString("Order"));
            }
            if (!data.isNull("UrlRoute")) {
                setUrlRoute(data.getString("UrlRoute"));
            }
            if (!data.isNull("CssClass")) {
                setCssClass(data.getString("CssClass"));
            }
            if (!data.isNull("SubMenus")) {
                setSubMenus(data.getJSONArray("SubMenus"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
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

    public String getTranslationId() {
        return translationId;
    }

    public void setTranslationId(String translationId) {
        this.translationId = translationId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUrlRoute() {
        return urlRoute;
    }

    public void setUrlRoute(String urlRoute) {
        this.urlRoute = urlRoute;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public ArrayList<NavigationMenuData> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(JSONArray subMenus) {
        ArrayList<NavigationMenuData> navigationMenuDatas = new ArrayList<NavigationMenuData>();
        for (int y=0; y < subMenus.length(); y++) {
            try {
                navigationMenuDatas.add(new NavigationMenuData(subMenus.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.subMenus = navigationMenuDatas;
    }
}