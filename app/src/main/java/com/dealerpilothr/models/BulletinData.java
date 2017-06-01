package com.dealerpilothr.models;

import com.dealerpilothr.utils.DateOperations;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BulletinData {
    private String newsId = "";
    private String name = "";
    private String body = "";
    private String author = "";
    private Long datePublished = 0l;
    private String fileId = "";

    public BulletinData() {

    }

    public BulletinData(JSONObject data) {
        try {
            if (!data.isNull("NewsId")) {
                setNewsId(data.getString("NewsId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Body")) {
                setBody(data.getString("Body"));
            }
            if (!data.isNull("Author")) {
                setAuthor(data.getString("Author"));
            }
            if (!data.isNull("DatePublished")) {
                setDatePublished(data.getString("DatePublished"));
            }
            if (!data.isNull("FileId")) {
                setFileId(data.getString("FileId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getDatePublished() {
        return datePublished;
    }

    public String getDatePublishedString() {
        String date = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");
        try {
            Date thisdaten = new Date();
            thisdaten.setTime(datePublished);
            date = format.format(thisdaten);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = DateOperations.getDate(datePublished);
    }

    public void setDatePublished(Long datePublished) {
        this.datePublished = datePublished;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}