package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class AdressData {
    private String addressId = "";
    private String city = "";
    private String regionId = "";
    private String region = "";
    private String countryId = "";
    private String country = "";
    private String postalCode = "";
    private String addressLine1 = "";
    private String addressLine2 = "";
    private boolean isPrimary = false;

    public AdressData() {

    }

    public AdressData(JSONObject data) {
        try {
            if (!data.isNull("AddressId")) {
                setAddressId(data.getString("AddressId"));
            }
            if (!data.isNull("City")) {
                setCity(data.getString("City"));
            }
            if (!data.isNull("RegionId")) {
                setRegionId(data.getString("RegionId"));
            }
            if (!data.isNull("Region")) {
                setRegion(data.getString("Region"));
            }
            if (!data.isNull("CountryId")) {
                setCountryId(data.getString("CountryId"));
            }
            if (!data.isNull("Country")) {
                setCountry(data.getString("Country"));
            }
            if (!data.isNull("PostalCode")) {
                setPostalCode(data.getString("PostalCode"));
            }
            if (!data.isNull("AddressLine1")) {
                setAddressLine1(data.getString("AddressLine1"));
            }
            if (!data.isNull("AddressLine2")) {
                setAddressLine2(data.getString("AddressLine2"));
            }
            if (!data.isNull("IsPrimary")) {
                setIsPrimary(data.getBoolean("IsPrimary"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getAddress() {
        String adress = "";
        if (!postalCode.equals("")) {
            if (!adress.equals("")) {
                adress = adress + ", ";
            }
            adress = adress + postalCode;
        }
        if (!country.equals("")) {
            if (!adress.equals("")) {
                adress = adress + ", ";
            }
            adress = adress + country;
        }
        if (!region.equals("")) {
            if (!adress.equals("")) {
                adress = adress + ", ";
            }
            adress = adress + region;
        }
        if (!city.equals("")) {
            if (!adress.equals("")) {
                adress = adress + ", ";
            }
            adress = adress + city;
        }
        if (!addressLine1.equals("")) {
            if (!adress.equals("")) {
                adress = adress + ", ";
            }
            adress = adress + addressLine1;
        }
        if (!addressLine2.equals("")) {
            if (!adress.equals("")) {
                adress = adress + ", ";
            }
            adress = adress + addressLine2;
        }
        return adress;
    }
}