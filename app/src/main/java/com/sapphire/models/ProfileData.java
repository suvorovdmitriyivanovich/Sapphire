package com.sapphire.models;

import com.sapphire.utils.DateOperations;

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
    private HireTypeData hireType = new HireTypeData();
    private String customField1 = "";
    private String customField2 = "";
    private ContactData contact = new ContactData();
    private PayrollInformationData payrollInformation = new PayrollInformationData();
    private String accountId = "";
    private String organizationId = "";
    private String firstName = "";
    private String lastName = "";
    private String avatarId = "";
    private String fullName = "";
    private String status = "";
    private String name = "";
    private String position = "";
    private String email = "";
    private boolean isCPRCertified = false;
    private boolean isFirstAidCertified = false;
    private boolean isSafetyCertified = false;
    private boolean presence = false;

    public ProfileData() {

    }

    public ProfileData(String profileId, String name, boolean presence) {
        this.profileId = profileId;
        this.name = name;
        this.presence = presence;
    }

    public ProfileData(JSONObject data) {
        try {
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
            if (!data.isNull("FullName")) {
                setFullName(data.getString("FullName"));
            }
            if (!data.isNull("Status")) {
                setStatus(data.getString("Status"));
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
                setHireType(data.getJSONObject("HireType"));
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
                setPayrollInformation(data.getJSONObject("PayrollInformation"));
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
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Position")) {
                setPosition(data.getString("Position"));
            }
            if (!data.isNull("Email")) {
                setEmail(data.getString("Email"));
            }
            if (!data.isNull("IsCPRCertified")) {
                setIsCPRCertified(data.getBoolean("IsCPRCertified"));
            }
            if (!data.isNull("IsFirstAidCertified")) {
                setIsFirstAidCertified(data.getBoolean("IsFirstAidCertified"));
            }
            if (!data.isNull("IsSafetyCertified")) {
                setIsSafetyCertified(data.getBoolean("IsSafetyCertified"));
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setBirthday(String birthday) {
        this.birthday = DateOperations.getDate(birthday);
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
        this.driverLicenseNumberExpire = DateOperations.getDate(driverLicenseNumberExpire);
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
        this.techLicenseNumberExpire = DateOperations.getDate(techLicenseNumberExpire);
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
        this.vSRNumberExpire = DateOperations.getDate(vSRNumberExpire);
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
        this.hireDate = DateOperations.getDate(hireDate);
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
        this.probationEndDate = DateOperations.getDate(probationEndDate);
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
        this.terminationDate = DateOperations.getDate(terminationDate);
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

    public void setHireType(JSONObject hireType) {
        this.hireType = new HireTypeData(hireType);
    }

    public HireTypeData getHireType() {
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

    public void setPayrollInformation(JSONObject payrollInformation) {
        this.payrollInformation = new PayrollInformationData(payrollInformation);
    }

    public PayrollInformationData getPayrollInformation() {
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

    public String getFullName() {
        String fullNameReturn = "";
        if (!fullName.equals("")) {
            fullNameReturn = fullName;
        } else if (!name.equals("")) {
            fullNameReturn = name;
        } else {
            if (!firstName.equals("")) {
                if (!fullNameReturn.equals("")) {
                    fullNameReturn = fullNameReturn + " ";
                }
                fullNameReturn = fullNameReturn + firstName;
            }
            if (!lastName.equals("")) {
                if (!fullNameReturn.equals("")) {
                    fullNameReturn = fullNameReturn + " ";
                }
                fullNameReturn = fullNameReturn + lastName;
            }
        }
        return fullNameReturn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setIsCPRCertified(boolean isCPRCertified) {
        this.isCPRCertified = isCPRCertified;
    }

    public boolean getIsCPRCertified() {
        return isCPRCertified;
    }

    public void setIsFirstAidCertified(boolean isFirstAidCertified) {
        this.isFirstAidCertified = isFirstAidCertified;
    }

    public boolean getIsFirstAidCertified() {
        return isFirstAidCertified;
    }

    public void setIsSafetyCertified(boolean isSafetyCertified) {
        this.isSafetyCertified = isSafetyCertified;
    }

    public boolean getIsSafetyCertified() {
        return isSafetyCertified;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    public boolean getPresence() {
        return presence;
    }
}