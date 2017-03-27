package com.sapphire.models;

import com.sapphire.models.ProfileData;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberData {
    private String meetingMemberId = "";
    private boolean presence = false;
    private ProfileData profile = new ProfileData();

    public MemberData() {

    }

    public MemberData(String meetingMemberId) {
        this.meetingMemberId = meetingMemberId;
    }

    public MemberData(JSONObject data) {
        try {
            if (!data.isNull("MeetingMemberId")) {
                setMeetingMemberId(data.getString("MeetingMemberId"));
            }
            if (!data.isNull("Presence")) {
                setPresence(data.getBoolean("Presence"));
            }
            if (!data.isNull("Profile")) {
                setProfile(data.getJSONObject("Profile"));
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

    public void setProfile(ProfileData profile) {
        this.profile = profile;
    }
}