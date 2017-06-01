package com.dealerpilothr.models;

import com.dealerpilothr.utils.DateOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvestigationItemData {
    private String investigationItemId = "";
    private String investigationId = "";
    private String name = "";
    private String description = "";
    private Long date = 0l;
    private ArrayList<FileData> files = new ArrayList<FileData>();

    public InvestigationItemData() {

    }

    public InvestigationItemData(String name) {
        setName(name);
    }

    public InvestigationItemData(JSONObject data) {
        try {
            if (!data.isNull("InvestigationItemId")) {
                setInvestigationItemId(data.getString("InvestigationItemId"));
            }
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

    public String getInvestigationItemId() {
        return investigationItemId;
    }

    public void setInvestigationItemId(String investigationItemId) {
        this.investigationItemId = investigationItemId;
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