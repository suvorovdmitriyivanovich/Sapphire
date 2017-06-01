package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberData {
    private String meetingMemberId = "";
    private boolean presence = false;
    private ProfileData profile = new ProfileData();
    private String name = "";
    private String workplaceInspectionId = "";
    private String WorkplaceInspectionProfileId = "";
    private boolean isProfile = false;

    public MemberData() {

    }

    public MemberData(String meetingMemberId) {
        this.meetingMemberId = meetingMemberId;
    }

    public MemberData(MemberData memberData) {
        this.meetingMemberId = memberData.getMeetingMemberId();
        this.presence = memberData.getPresence();
        this.profile = memberData.getProfile();
        this.name = memberData.getName();
        this.workplaceInspectionId = memberData.getWorkplaceInspectionId();
        this.WorkplaceInspectionProfileId = memberData.getWorkplaceInspectionProfileId();
    }

    public MemberData(JSONObject data) {
        try {
            if (!data.isNull("MeetingMemberId")) {
                setMeetingMemberId(data.getString("MeetingMemberId"));
            } else if (!data.isNull("WorkplaceInspectionProfileId")) {
                setMeetingMemberId(data.getString("WorkplaceInspectionProfileId"));
            }
            if (!data.isNull("Presence")) {
                setPresence(data.getBoolean("Presence"));
            }
            if (!data.isNull("Profile")) {
                setProfile(data.getJSONObject("Profile"));
            } else if (!data.isNull("ProfileId")) {
                setProfile(data.getString("ProfileId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("WorkplaceInspectionId")) {
                setWorkplaceInspectionId(data.getString("WorkplaceInspectionId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMeetingMemberId() {
        return meetingMemberId;
    }

    public void setMeetingMemberId(String meetingMemberId) {
        this.meetingMemberId = meetingMemberId;
    }

    public boolean getPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    public ProfileData getProfile() {
        return profile;
    }

    public JSONObject getProfileJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfileId", profile.getProfileId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void setProfile(JSONObject profile) {
        this.profile = new ProfileData(profile);
    }

    public void setProfile(String profileId) {
        this.profile = new ProfileData(profileId);
    }

    public void setProfile(ProfileData profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkplaceInspectionId() {
        return workplaceInspectionId;
    }

    public void setWorkplaceInspectionId(String workplaceInspectionId) {
        this.workplaceInspectionId = workplaceInspectionId;
    }

    public String getWorkplaceInspectionProfileId() {
        return WorkplaceInspectionProfileId;
    }

    public void setWorkplaceInspectionProfileId(String workplaceInspectionProfileId) {
        WorkplaceInspectionProfileId = workplaceInspectionProfileId;
    }

    public boolean getIsProfile() {
        return isProfile;
    }

    public void setIsProfile(boolean isProfile) {
        this.isProfile = isProfile;
    }
}