package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class PunchesCategoryData {
    private String fileTypeId = "";
    private String name = "";
    private String description = "";
    private String contentType = "";
    private String extension = "";

    public PunchesCategoryData() {

    }

    public PunchesCategoryData(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public PunchesCategoryData(JSONObject data) {
        try {
            if (!data.isNull("FileTypeId")) {
                setFileTypeId(data.getString("FileTypeId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("ContentType")) {
                setContentType(data.getString("ContentType"));
            }
            if (!data.isNull("Extension")) {
                setExtension(data.getString("Extension"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}