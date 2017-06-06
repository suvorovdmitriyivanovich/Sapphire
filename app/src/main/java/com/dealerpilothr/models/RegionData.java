package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class RegionData {
    private String regionId = "";
    private String code = "";
    private String name = "";
    private String nameAscii = "";
    private String countryId = "";

    public RegionData() {

    }

    public RegionData(JSONObject data) {
        try {
            if (!data.isNull("RegionId")) {
                setRegionId(data.getString("RegionId"));
            }
            if (!data.isNull("Code")) {
                setCode(data.getString("Code"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("NameAscii")) {
                setNameAscii(data.getString("NameAscii"));
            }
            if (!data.isNull("CountryId")) {
                setCountryId(data.getString("CountryId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAscii() {
        return nameAscii;
    }

    public void setNameAscii(String nameAscii) {
        this.nameAscii = nameAscii;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}