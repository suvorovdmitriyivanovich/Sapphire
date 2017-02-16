package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactData {
    private String contactId = "";
    private String name = "";
    private String relationship = "";
    private String phone1 = "";
    private String phone2 = "";
    private String email1 = "";
    private String email2 = "";
    private boolean isPrimary = false;
    private String address = "";
    private ContactTypeData contactType = new ContactTypeData();
    private String note = "";

    public ContactData() {

    }

    public ContactData(JSONObject data) {
        try {
            if (!data.isNull("ContactId")) {
                setContactId(data.getString("ContactId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Relationship")) {
                setRelationship(data.getString("Relationship"));
            }
            if (!data.isNull("Phone1")) {
                setPhone1(data.getString("Phone1"));
            }
            if (!data.isNull("Phone2")) {
                setPhone2(data.getString("Phone2"));
            }
            if (!data.isNull("Email1")) {
                setEmail1(data.getString("Email1"));
            }
            if (!data.isNull("Email2")) {
                setEmail2(data.getString("Email2"));
            }
            if (!data.isNull("IsPrimary")) {
                setIsPrimary(data.getBoolean("IsPrimary"));
            }
            if (!data.isNull("Address")) {
                setAddress(data.getString("Address"));
            }
            if (!data.isNull("ContactType")) {
                setContactType(data.getJSONObject("ContactType"));
            }
            if (!data.isNull("Note")) {
                setNote(data.getString("Note"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail2() {
        return email2;
    }

    public boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setContactType(JSONObject contactType) {
        this.contactType = new ContactTypeData(contactType);
    }

    public ContactTypeData getContactType() {
        return contactType;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }
}