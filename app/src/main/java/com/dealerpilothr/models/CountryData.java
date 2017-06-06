package com.dealerpilothr.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountryData {
    private String countryId = "";
    private String code = "";
    private String iSOAlpha3 = "";
    private String iSONumeric = "";
    private String fipsCode = "";
    private String name = "";
    private Double areaInSqkm = 0d;
    private int population = 0;
    private String tLD = "";
    private String continentId = "";
    private ContinentData continent = new ContinentData();
    private ArrayList<RegionData> regions = new ArrayList<RegionData>();

    public CountryData() {

    }

    public CountryData(JSONObject data) {
        try {
            if (!data.isNull("CountryId")) {
                setCountryId(data.getString("CountryId"));
            }
            if (!data.isNull("Code")) {
                setCode(data.getString("Code"));
            }
            if (!data.isNull("ISOAlpha3")) {
                setISOAlpha3(data.getString("ISOAlpha3"));
            }
            if (!data.isNull("ISONumeric")) {
                setISONumeric(data.getString("ISONumeric"));
            }
            if (!data.isNull("FipsCode")) {
                setFipsCode(data.getString("FipsCode"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("AreaInSqkm")) {
                setAreaInSqkm(data.getDouble("AreaInSqkm"));
            }
            if (!data.isNull("Population")) {
                setPopulation(data.getInt("Population"));
            }
            if (!data.isNull("TLD")) {
                setTLD(data.getString("TLD"));
            }
            if (!data.isNull("СontinentId")) {
                setContinentId(data.getString("СontinentId"));
            }
            if (!data.isNull("Continent")) {
                setContinent(data.getJSONObject("Continent"));
            }
            if (!data.isNull("Regions")) {
                setRegions(data.getJSONArray("Regions"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getISOAlpha3() {
        return iSOAlpha3;
    }

    public void setISOAlpha3(String uSOAlpha3) {
        this.iSOAlpha3 = iSOAlpha3;
    }

    public String getISONumeric() {
        return iSONumeric;
    }

    public void setISONumeric(String iSONumeric) {
        this.iSONumeric = iSONumeric;
    }

    public String getFipsCode() {
        return fipsCode;
    }

    public void setFipsCode(String fipsCode) {
        this.fipsCode = fipsCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAreaInSqkm() {
        return areaInSqkm;
    }

    public void setAreaInSqkm(Double areaInSqkm) {
        this.areaInSqkm = areaInSqkm;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getTLD() {
        return tLD;
    }

    public void setTLD(String tLD) {
        this.tLD = tLD;
    }

    public String getContinentId() {
        return continentId;
    }

    public void setContinentId(String continentId) {
        this.continentId = continentId;
    }

    public ContinentData getContinent() {
        return continent;
    }

    public void setContinent(JSONObject continent) {
        this.continent = new ContinentData(continent);
    }

    public ArrayList<RegionData> getRegions() {
        return regions;
    }

    public void setRegions(JSONArray regions) {
        ArrayList<RegionData> regionDatas = new ArrayList<RegionData>();
        for (int y=0; y < regions.length(); y++) {
            try {
                regionDatas.add(new RegionData(regions.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.regions = regionDatas;
    }
}