package com.sapphire.logic;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileData {
    private String profileId = "";
    private String birthday = "";
    private String driverLicenseNumber = "";
    private String driverLicenseNumberExpire = "";
    private String techLicenseNumber = "";
    private String techLicenseNumberExpire = "";
    private String workPermitNumber = "";
    private String vSRNumber = "";
    private String vSRNumberExpire = "";
    private String sINNumber = "";
    private Long hireDate = 0l;
    private Long probationEndDate = 0l;
    private Long terminationDate = 0l;
    private String hireType = "";
    private String customField1 = "";
    private String customField2 = "";
    private String contact = "";
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
                setContact(data.getString("Contact"));
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
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumberExpire(String driverLicenseNumberExpire) {
        this.driverLicenseNumberExpire = driverLicenseNumberExpire;
    }

    public String getDriverLicenseNumberExpire() {
        return driverLicenseNumberExpire;
    }

    public void setTechLicenseNumber(String techLicenseNumber) {
        this.techLicenseNumber = techLicenseNumber;
    }

    public String getTechLicenseNumber() {
        return techLicenseNumber;
    }

    public void setTechLicenseNumberExpire(String techLicenseNumberExpire) {
        this.techLicenseNumberExpire = techLicenseNumberExpire;
    }

    public String getTechLicenseNumberExpire() {
        return techLicenseNumberExpire;
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
        this.vSRNumberExpire = vSRNumberExpire;
    }

    public String getVSRNumberExpire() {
        return vSRNumberExpire;
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

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
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