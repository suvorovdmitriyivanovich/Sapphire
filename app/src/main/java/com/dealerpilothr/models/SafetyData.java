package com.dealerpilothr.models;

import com.dealerpilothr.utils.DateOperations;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SafetyData {
    private String safetyDataSheetId = "";
    private String name = "";
    private String supplier = "";
    private String notes = "";
    private Long uploadDate = 0l;
    private Long renewalDate = 0l;
    private String fileId = "";
    private String organizationId = "";

    public SafetyData() {

    }

    public SafetyData(String name) {
        setName(name);
    }

    public SafetyData(JSONObject data) {
        try {
            if (!data.isNull("SafetyDataSheetId")) {
                setSafetyDataSheetId(data.getString("SafetyDataSheetId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Supplier")) {
                setSupplier(data.getString("Supplier"));
            }
            if (!data.isNull("Notes")) {
                setNotes(data.getString("Notes"));
            }
            if (!data.isNull("UploadDate")) {
                setUploadDate(data.getString("UploadDate"));
            }
            if (!data.isNull("RenewalDate")) {
                setRenewalDate(data.getString("RenewalDate"));
            }
            if (!data.isNull("FileId")) {
                setFileId(data.getString("FileId"));
            }
            if (!data.isNull("OrganizationId")) {
                setOrganizationId(data.getString("OrganizationId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSafetyDataSheetId() {
        return safetyDataSheetId;
    }

    public void setSafetyDataSheetId(String safetyDataSheetId) {
        this.safetyDataSheetId = safetyDataSheetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getUploadDate() {
        return uploadDate;
    }

    public String getUploadDateString() {
        String dateString = "";
        if (uploadDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(uploadDate);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = DateOperations.getDate(uploadDate);
    }

    public Long getRenewalDate() {
        return renewalDate;
    }

    public String getRenewalDateString() {
        String dateString = "";
        if (renewalDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(renewalDate);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public void setRenewalDate(String renewalDate) {
        this.renewalDate = DateOperations.getDate(renewalDate);
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}