package com.sapphire.models;

import com.sapphire.utils.DateOperations;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentData {
    private String docId = "";
    private String name = "";
    private Long date = 0l;
    private String docCategoryId = "";
    private CategoryData category = new CategoryData();
    private String fileId = "";
    private String profileId = "";

    public DocumentData() {

    }

    public DocumentData(String name) {
        setName(name);
    }

    public DocumentData(JSONObject data) {
        try {
            if (!data.isNull("DocId")) {
                setDocId(data.getString("DocId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Date")) {
                setDate(data.getString("Date"));
            }
            if (!data.isNull("DocCategoryId")) {
                setDocCategoryId(data.getString("DocCategoryId"));
            }
            if (!data.isNull("Category")) {
                setCategory(data.getJSONObject("Category"));
            }
            if (!data.isNull("FileId")) {
                setFileId(data.getString("FileId"));
            }
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDate() {
        return date;
    }

    public String getDateString() {
        String dateString = "";
        if (date != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(date);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public String getDateServer() {
        return DateOperations.getDateServer(date);
    }

    public void setDate(String date) {
        this.date = DateOperations.getDate(date);
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getDocCategoryId() {
        return docCategoryId;
    }

    public void setDocCategoryId(String docCategoryId) {
        this.docCategoryId = docCategoryId;
    }

    public CategoryData getCategory() {
        return category;
    }

    public void setCategory(JSONObject category) {
        this.category = new CategoryData(category);
    }

    public void setCategory(CategoryData category) {
        this.category = category;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}