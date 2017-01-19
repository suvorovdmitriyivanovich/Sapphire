package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageData {
    private int id = -1;
    private String message = "";
    private boolean isRead = false;
    private boolean upload = false;

    public MessageData() {

    }

    public MessageData(JSONObject jsonObject) {
        try {
            if (!jsonObject.isNull("message")) {
                setMessage(jsonObject.getString("message"));
            }
            if (!jsonObject.isNull("is_read")) {
                setIsRead(jsonObject.getInt("is_read"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        if (isRead == 1) {
            this.isRead = true;
        } else {
            this.isRead = false;
        }
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        if (upload == 1) {
            this.upload = true;
        } else {
            this.upload = false;
        }
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }
}