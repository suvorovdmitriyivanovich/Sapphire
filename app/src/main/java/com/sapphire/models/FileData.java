package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class FileData {
    private String id = "";
    private String fileId = "";
    private String name = "";
    private String description = "";
    private int size = 0;
    private String parentId = "";
    private FileTypeData fileType = new FileTypeData();
    private boolean isFolder = false;
    private SettingData settings = new SettingData();
    private String file = "";

    public FileData() {

    }

    public FileData(String fileId) {
        this.fileId = fileId;
    }

    public FileData(JSONObject data) {
        try {
            if (!data.isNull("FileId")) {
                setFileId(data.getString("FileId"));
            } else if (!data.isNull("Id")) {
                setFileId(data.getString("Id"));
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
            if (!data.isNull("IsFolder")) {
                setIsFolder(data.getBoolean("IsFolder"));
            }
            if (!data.isNull("Settings")) {
                setSettings(data.getJSONObject("Settings"));
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    public boolean getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    public void setIsFolder(int isFolder) {
        if (isFolder == 1) {
            this.isFolder = true;
        } else {
            this.isFolder = false;
        }
    }

    public SettingData getSettings() {
        return settings;
    }

    public void setSettings(JSONObject settings) {
        this.settings = new SettingData(settings);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}