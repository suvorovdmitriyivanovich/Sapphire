package com.sapphire.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AppRoleAppSecurityData {
    private ArrayList<AppSecurityData> appSecurityDatas = new ArrayList<AppSecurityData>();

    public AppRoleAppSecurityData() {

    }

    public AppRoleAppSecurityData(JSONObject data) {
        try {
            JSONArray jsonArray = data.names();
            for (int y=0; y < jsonArray.length(); y++) {
                try {
                    appSecurityDatas.add(new AppSecurityData(data.getJSONObject(jsonArray.get(y).toString())));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasGlobalAppRoleAppSecurities(String globalAppRoleAppSecurities) {
        for(AppSecurityData item: appSecurityDatas) {
            if (item.getAppSecurityId().equals(globalAppRoleAppSecurities)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActiveGlobalAppRoleAppSecurities(String globalAppRoleAppSecurities) {
        for(AppSecurityData item: appSecurityDatas) {
            if (item.getAppSecurityId().equals(globalAppRoleAppSecurities) && item.getIsActive()) {
                return true;
            }
        }
        return false;
    }
}