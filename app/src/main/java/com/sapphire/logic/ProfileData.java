package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileData {
    private String profileId = "";
    private Long birthday = 0l;
    private String driverLicenseNumber = "";
    private Long driverLicenseNumberExpire = 0l;
    private String techLicenseNumber = "";
    private Long techLicenseNumberExpire = 0l;
    private String workPermitNumber = "";
    private String vSRNumber = "";
    private Long vSRNumberExpire = 0l;
    private String sINNumber = "";
    private Long hireDate = 0l;
    private Long probationEndDate = 0l;
    private Long terminationDate = 0l;
    private String hireType = "";
    private String customField1 = "";
    private String customField2 = "";
    private ContactData contact = new ContactData();
    private String payrollInformation = "";
    private String accountId = "";
    private String organizationId = "";
    private String firstName = "";
    private String lastName = "";
    private String avatarId = "";

    public ProfileData() {

    }

    public ProfileData(JSONObject data) {
        try {
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
            if (!data.isNull("Birthday")) {
                setBirthday(data.getString("Birthday"));
            }
            if (!data.isNull("DriverLicenseNumber")) {
                setDriverLicenseNumber(data.getString("DriverLicenseNumber"));
            }
            if (!data.isNull("DriverLicenseNumberExpire")) {
                setDriverLicenseNumberExpire(data.getString("DriverLicenseNumberExpire"));
            }
            if (!data.isNull("TechLicenseNumber")) {
                setTechLicenseNumber(data.getString("TechLicenseNumber"));
            }
            if (!data.isNull("TechLicenseNumberExpire")) {
                setTechLicenseNumberExpire(data.getString("TechLicenseNumberExpire"));
            }
            if (!data.isNull("WorkPermitNumber")) {
                setWorkPermitNumber(data.getString("WorkPermitNumber"));
            }
            if (!data.isNull("VSRNumber")) {
                setVSRNumber(data.getString("VSRNumber"));
            }
            if (!data.isNull("VSRNumberExpire")) {
                setVSRNumberExpire(data.getString("VSRNumberExpire"));
            }
            if (!data.isNull("SINNumber")) {
                setSINNumber(data.getString("SINNumber"));
            }
            if (!data.isNull("HireDate")) {
                setHireDate(data.getString("HireDate"));
            }
            if (!data.isNull("ProbationEndDate")) {
                setProbationEndDate(data.getString("ProbationEndDate"));
            }
            if (!data.isNull("TerminationDate")) {
                setTerminationDate(data.getString("TerminationDate"));
            }
            if (!data.isNull("HireType")) {
                setHireType(data.getString("HireType"));
            }
            if (!data.isNull("CustomField1")) {
                setCustomField1(data.getString("CustomField1"));
            }
            if (!data.isNull("CustomField2")) {
                setCustomField2(data.getString("CustomField2"));
            }
            if (!data.isNull("Contact")) {
                setContact(data.getJSONObject("Contact"));
            }
            if (!data.isNull("PayrollInformation")) {
                setPayrollInformation(data.getString("PayrollInformation"));
            }
            if (!data.isNull("AccountId")) {
                setAccountId(data.getString("AccountId"));
            }
            if (!data.isNull("OrganizationId")) {
                setOrganizationId(data.getString("OrganizationId"));
            }
            if (!data.isNull("FirstName")) {
                setFirstName(data.getString("FirstName"));
            }
            if (!data.isNull("LastName")) {
                setLastName(data.getString("LastName"));
            }
            if (!data.isNull("AvatarId")) {
                setAvatarId(data.getString("AvatarId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setBirthday(String birthday) {
        Long dateLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(birthday);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.birthday = dateLong;
    }

    public Long getBirthday() {
        return birthday;
    }

    public String getBirthdayString() {
        String dateString = "";
        if (birthday != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            date.setTime(birthday);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumberExpire(String driverLicenseNumberExpire) {
        Long dateLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(driverLicenseNumberExpire);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.driverLicenseNumberExpire = dateLong;
    }

    public Long getDriverLicenseNumberExpire() {
        return driverLicenseNumberExpire;
    }

    public String getDriverLicenseNumberExpireString() {
        String dateString = "";
        if (driverLicenseNumberExpire != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            date.setTime(driverLicenseNumberExpire);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setTechLicenseNumber(String techLicenseNumber) {
        this.techLicenseNumber = techLicenseNumber;
    }

    public String getTechLicenseNumber() {
        return techLicenseNumber;
    }

    public void setTechLicenseNumberExpire(String techLicenseNumberExpire) {
        Long dateLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(techLicenseNumberExpire);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.techLicenseNumberExpire = dateLong;
    }

    public Long getTechLicenseNumberExpire() {
        return techLicenseNumberExpire;
    }

    public String getTechLicenseNumberExpireString() {
        String dateString = "";
        if (techLicenseNumberExpire != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            date.setTime(techLicenseNumberExpire);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setWorkPermitNumber(String workPermitNumber) {
        this.workPermitNumber = workPermitNumber;
    }

    public String getWorkPermitNumber() {
        return workPermitNumber;
    }

    public void setVSRNumber(String vSRNumber) {
        this.vSRNumber = vSRNumber;
    }

    public String getVSRNumber() {
        return vSRNumber;
    }

    public void setVSRNumberExpire(String vSRNumberExpire) {
        Long dateLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(vSRNumberExpire);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.vSRNumberExpire = dateLong;
    }

    public Long getVSRNumberExpire() {
        return vSRNumberExpire;
    }

    public String getVSRNumberExpireString() {
        String dateString = "";
        if (vSRNumberExpire != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            date.setTime(vSRNumberExpire);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setSINNumber(String sINNumber) {
        this.sINNumber = sINNumber;
    }

    public String getSINNumber() {
        return sINNumber;
    }

    public void setHireDate(String hireDate) {
        Long dateLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(hireDate);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.hireDate = dateLong;
    }

    public Long getHireDate() {
        return hireDate;
    }

    public String getHireDateString() {
        String dateString = "";
        if (hireDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            date.setTime(hireDate);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setProbationEndDate(String probationEndDate) {
        Long dateLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(probationEndDate);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.probationEndDate = dateLong;
    }

    public Long getProbationEndDate() {
        return probationEndDate;
    }

    public String getProbationEndDateString() {
        String dateString = "";
        if (probationEndDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            date.setTime(probationEndDate);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setTerminationDate(String terminationDate) {
        Long dateLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(terminationDate);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.terminationDate = dateLong;
    }

    public Long getTerminationDate() {
        return terminationDate;
    }

    public String getTerminationDateString() {
        String dateString = "";
        if (terminationDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            date.setTime(terminationDate);
            dateString = format.format(date);
        }
        return dateString;
    }

    public void setHireType(String hireType) {
        this.hireType = hireType;
    }

    public String getHireType() {
        return hireType;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField2(String customField2) {
        this.customField2 = customField2;
    }

    public String getCustomField2() {
        return customField2;
    }

    public void setContact(JSONObject contact) {
        this.contact = new ContactData(contact);
    }

    public ContactData getContact() {
        return contact;
    }

    public void setPayrollInformation(String payrollInformation) {
        this.payrollInformation = payrollInformation;
    }

    public String getPayrollInformation() {
        return payrollInformation;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatarId() {
        return avatarId;
    }
}