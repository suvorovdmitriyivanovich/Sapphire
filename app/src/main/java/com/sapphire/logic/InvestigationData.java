package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvestigationData {
    private String investigationId = "";
    private String name = "";
    private String description = "";
    private Long date = 0l;
    private ArrayList<FileData> files = new ArrayList<FileData>();

    public InvestigationData() {

    }

    public InvestigationData(String name) {
        setName(name);
    }

    public InvestigationData(JSONObject data) {
        try {
            if (!data.isNull("InvestigationId")) {
                setInvestigationId(data.getString("InvestigationId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Date")) {
                setDate(data.getString("Date"));
            }
            if (!data.isNull("Files")) {
                setFiles(data.getJSONArray("Files"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(String investigationId) {
        this.investigationId = investigationId;
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

    public Long getDate() {
        return date;
    }

    public String getDateString() {
        String dateString = "";
        if (date != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date dateT = new Date();
            dateT.setTime(date);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setDate(String date) {
        Long dateLong = 0l;
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date newdate = format.parse(date);
            //dateLong = newdate.getTime();

            String datet = formatDate.format(newdate);
            newdate = formatDate.parse(datet);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.date = dateLong;
    }

    public ArrayList<FileData> getFiles() {
        return files;
    }

    public void setFiles(JSONArray files) {
        ArrayList<FileData> fileDatas = new ArrayList<FileData>();
        for (int y=0; y < files.length(); y++) {
            try {
                fileDatas.add(new FileData(files.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.files = fileDatas;
    }
}