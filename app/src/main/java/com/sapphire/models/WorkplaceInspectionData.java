package com.sapphire.models;

import com.sapphire.logic.UserInfo;
import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkplaceInspectionData {
    private String workplaceInspectionId = "";
    private String name = "";
    private String description = "";
    private Long date = 0l;
    private boolean completed = false;
    private boolean postedOnBoard = false;
    private ArrayList<FileData> files = new ArrayList<FileData>();
    private JSONArray filesJson = new JSONArray();
    private String organizationId = "";
    private JSONArray itemsJson = new JSONArray();
    private JSONObject taskJson = new JSONObject();
    private boolean inspected = false;
    private ArrayList<MemberData> profiles = new ArrayList<MemberData>();

    public WorkplaceInspectionData() {

    }

    public WorkplaceInspectionData(String name) {
        setName(name);
    }

    public WorkplaceInspectionData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionId")) {
                setWorkplaceInspectionId(data.getString("WorkplaceInspectionId"));
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
            if (!data.isNull("Completed")) {
                setCompleted(data.getBoolean("Completed"));
            }
            if (!data.isNull("PostedOnBoard")) {
                setPostedOnBoard(data.getBoolean("PostedOnBoard"));
            }
            if (!data.isNull("Files")) {
                setFiles(data.getJSONArray("Files"));
                setFilesJson(data.getJSONArray("Files"));
            }
            if (!data.isNull("OrganizationId")) {
                setOrganizationId(data.getString("OrganizationId"));
            }
            if (!data.isNull("Items")) {
                setItemsJson(data.getJSONArray("Items"));
            }
            if (!data.isNull("Task")) {
                setTaskJson(data.getJSONObject("Task"));
            }
            if (!data.isNull("Inspected")) {
                setInspected(data.getBoolean("Inspected"));
            }
            setProfiles(UserInfo.getUserInfo().getCurrentOrganizationStructures());
            if (!data.isNull("Profiles")) {
                setProfiles(data.getJSONArray("Profiles"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWorkplaceInspectionId() {
        return workplaceInspectionId;
    }

    public void setWorkplaceInspectionId(String workplaceInspectionId) {
        this.workplaceInspectionId = workplaceInspectionId;
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

    public void setDate(String date) {
        this.date = DateOperations.getDate(date);
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getPostedOnBoard() {
        return postedOnBoard;
    }

    public void setPostedOnBoard(boolean postedOnBoard) {
        this.postedOnBoard = postedOnBoard;
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

    public JSONArray getFilesJson() {
        return filesJson;
    }

    public void setFilesJson(JSONArray filesJson) {
        this.filesJson = filesJson;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public JSONArray getItemsJson() {
        return itemsJson;
    }

    public void setItemsJson(JSONArray itemsJson) {
        this.itemsJson = itemsJson;
    }

    public JSONObject getTaskJson() {
        return taskJson;
    }

    public void setTaskJson(JSONObject taskJson) {
        this.taskJson = taskJson;
    }

    public boolean getInspected() {
        return inspected;
    }

    public void setInspected(boolean inspected) {
        this.inspected = inspected;
    }

    public ArrayList<MemberData> getProfiles() {
        return profiles;
    }

    public void setProfiles(JSONArray profiles) {
        for (int y=0; y < profiles.length(); y++) {
            try {
                MemberData memberData = new MemberData(profiles.getJSONObject(y));
                for (MemberData item: this.profiles) {
                    if (item.getProfile().getProfileId().equals(memberData.getProfile().getProfileId())) {
                        item.setPresence(true);
                        /*
                        item.setWorkplaceInspectionProfileId(memberData.getWorkplaceInspectionProfileId());
                        item.setWorkplaceInspectionId(memberData.getWorkplaceInspectionId());
                        item.setName(memberData.getName());
                        */
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setProfiles(ArrayList<ProfileData> profiles) {
        this.profiles.clear();
        for (ProfileData item: profiles) {
            MemberData memberData = new MemberData();
            memberData.setProfile(item);

            this.profiles.add(memberData);
        }
    }

    /*
    public void setProfiles(ArrayList<MemberData> profiles) {
        this.profiles.clear();
        for (MemberData item: profiles) {
            this.profiles.add(item);
        }
    }
    */
}