package com.dealerpilothr.models;

import com.dealerpilothr.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MeetingData {
    private String meetingId = "";
    private String name = "";
    private String description = "";
    private String location = "";
    private Long meetingDate = 0l;
    private Long endTime = 0l;
    private boolean posted = false;
    private boolean completed = false;
    private String organizationId = "";
    private boolean published = false;
    private ArrayList<MemberData> members = new ArrayList<MemberData>();
    private ArrayList<TopicData> topics = new ArrayList<TopicData>();
    private ArrayList<FileData> files = new ArrayList<FileData>();
    private String customReportId = "";
    private ArrayList<MemberData> profiles = new ArrayList<MemberData>();

    public MeetingData() {

    }

    public MeetingData(String name) {
        setName(name);
    }

    public MeetingData(JSONObject data) {
        try {
            if (!data.isNull("MeetingId")) {
                setMeetingId(data.getString("MeetingId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Location")) {
                setLocation(data.getString("Location"));
            }
            if (!data.isNull("MeetingDate")) {
                setMeetingDate(data.getString("MeetingDate"));
            }
            if (!data.isNull("EndTime")) {
                setEndTime(data.getString("EndTime"));
            }
            if (!data.isNull("Posted")) {
                setPosted(data.getBoolean("Posted"));
            }
            if (!data.isNull("Published")) {
                setPublished(data.getBoolean("Published"));
            }
            if (!data.isNull("Completed")) {
                setCompleted(data.getBoolean("Completed"));
            }
            if (!data.isNull("OrganizationId")) {
                setOrganizationId(data.getString("OrganizationId"));
            }
            if (!data.isNull("Members")) {
                setMembers(data.getJSONArray("Members"));
            }
            if (!data.isNull("Topics")) {
                setTopics(data.getJSONArray("Topics"));
            }
            if (!data.isNull("Files")) {
                setFiles(data.getJSONArray("Files"));
            }
            if (!data.isNull("CustomReportId")) {
                setCustomReportId(data.getString("CustomReportId"));
            }
            if (!data.isNull("Profiles")) {
                setProfiles(data.getJSONArray("Profiles"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getMeetingDate() {
        return meetingDate;
    }

    public String getMeetingDateString() {
        String dateString = "";
        if (meetingDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(meetingDate);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public String getMeetingDateStartEndString() {
        String dateString = "";
        if (meetingDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");
            Date thisdaten = new Date();
            thisdaten.setTime(meetingDate);
            dateString = format.format(thisdaten);
        }
        if (endTime != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
            Date thisdaten = new Date();
            thisdaten.setTime(endTime);
            dateString = dateString + " - " + format.format(thisdaten);
        }
        return dateString;
    }

    public String getMeetingDateServer() {
        return DateOperations.getDateServer(meetingDate);
    }

    public void setMeetingDate(String date) {
        this.meetingDate = DateOperations.getDate(date);
    }

    public void setMeetingDate(Long date) {
        this.meetingDate = date;
    }

    public Long getEndTime() {
        return endTime;
    }

    public String getEndTimeString() {
        String dateString = "";
        if (endTime != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(endTime);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public String getEndTimeServer() {
        return DateOperations.getDateServer(endTime);
    }

    public void setEndTime(String date) {
        this.endTime = DateOperations.getDate(date);
    }

    public void setEndTime(Long date) {
        this.endTime = date;
    }

    public boolean getPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public ArrayList<MemberData> getMembers() {
        return members;
    }

    public void setMembers(JSONArray members) {
        ArrayList<MemberData> datas = new ArrayList<MemberData>();
        for (int y=0; y < members.length(); y++) {
            try {
                datas.add(new MemberData(members.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.members = datas;
    }

    public void setMembers(ArrayList<MemberData> members) {
        this.members = members;
    }

    public ArrayList<TopicData> getTopics() {
        return topics;
    }

    public void setTopics(JSONArray topics) {
        ArrayList<TopicData> datas = new ArrayList<TopicData>();
        for (int y=0; y < topics.length(); y++) {
            try {
                datas.add(new TopicData(topics.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.topics = datas;
    }

    public void setTopics(ArrayList<TopicData> topics) {
        this.topics = topics;
    }

    public boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
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

    public String getCustomReportId() {
        return customReportId;
    }

    public void setCustomReportId(String customReportId) {
        this.customReportId = customReportId;
    }

    public ArrayList<MemberData> getProfiles() {
        return profiles;
    }

    public void setProfiles(JSONArray profiles) {
        this.profiles.clear();
        for (int y=0; y < profiles.length(); y++) {
            try {
                this.profiles.add(new MemberData(profiles.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}