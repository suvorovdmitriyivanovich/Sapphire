package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class FileData {
    private String id = "";
    private String name = "";
    private String description = "";
    private int size = 0;
    private String parentId = "";
    private FileTypeData fileType = new FileTypeData();

    public FileData() {

    }

    public FileData(String id) {
        this.id = id;
    }

    public FileData(JSONObject data) {
        try {
            if (!data.isNull("Id")) {
                setId(data.getString("Id"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Size")) {
                setSize(data.getInt("Size"));
            }
            if (!data.isNull("ParentId")) {
                setParentId(data.getString("ParentId"));
            }
            if (!data.isNull("FileType")) {
                setFileType(data.getJSONObject("FileType"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getSize() {
        return size;
    }

    public String getSizeString() {
        return String.valueOf(size);
    }

    public String getSizeKbString() {
        return String.valueOf(size/1024);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public FileTypeData getFileType() {
        return fileType;
    }

    public void setFileType(JSONObject fileType) {
        this.fileType = new FileTypeData(fileType);
    }
}