package com.sapphire.models;

import com.sapphire.R;
import com.sapphire.Sapphire;
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
    private boolean isHumanAndSafetyMember = false;
    private boolean isCPRCertified = false;
    private boolean isFirstAidCertified = false;
    private boolean isSafetyCertified = false;
    private boolean presence = false;
    private String manager = "";
    private String managerId = "";
    private String secondaryManager = "";
    private String secondaryManagerId = "";
    private String homePhoneNumber = "";
    private String cellPhoneNumber = "";
    private String secondaryEmail = "";
    private Long workPermitNumberExpire = 0l;
    private String uniformDescription = "";
    private boolean uniformAllowance = false;
    private Long uniformRenewalDate = 0l;
    private Double uniformAllowanceAmount = 0d;
    private boolean isEmployee = false;
    private boolean primaryEmailAllowNotification = false;
    private boolean secondaryEmailAllowNotification = false;

    public ProfileData() {

    }

    public ProfileData(String profileId, String name, boolean presence) {
        this.profileId = profileId;
        this.name = name;
        this.presence = presence;
    }

    public ProfileData(String profileId) {
        this.profileId = profileId;
    }

    public ProfileData(JSONObject data) {
        try {
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            } else if (!data.isNull("Id")) {
                setProfileId(data.getString("Id"));
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
            } else if (!data.isNull("HireTypes")) {
                setHireType(data.getString("HireTypes"));
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
            } else if (!data.isNull("PrimaryEmail")) {
                setEmail(data.getString("PrimaryEmail"));
            }
            if (!data.isNull("SecondaryEmail")) {
                setSecondaryEmail(data.getString("SecondaryEmail"));
            }
            if (!data.isNull("HomePhoneNumber")) {
                setHomePhoneNumber(data.getString("HomePhoneNumber"));
            }
            if (!data.isNull("CellPhoneNumber")) {
                setCellPhoneNumber(data.getString("CellPhoneNumber"));
            }
            if (!data.isNull("IsHumanAndSafetyMember")) {
                setIsHumanAndSafetyMember(data.getBoolean("IsHumanAndSafetyMember"));
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
            if (!data.isNull("Manager")) {
                setManager(data.getString("Manager"));
            }
            if (!data.isNull("ManagerId")) {
                setManagerId(data.getString("ManagerId"));
            }
            if (!data.isNull("SecondaryManager")) {
                setSecondaryManager(data.getString("SecondaryManager"));
            }
            if (!data.isNull("SecondaryManagerId")) {
                setSecondaryManagerId(data.getString("SecondaryManagerId"));
            }
            if (!data.isNull("WorkPermitNumberExpire")) {
                setWorkPermitNumberExpire(data.getString("WorkPermitNumberExpire"));
            }
            if (!data.isNull("UniformDescription")) {
                setUniformDescription(data.getString("UniformDescription"));
            }
            if (!data.isNull("UniformAllowance")) {
                setUniformAllowance(data.getBoolean("UniformAllowance"));
            }
            if (!data.isNull("UniformRenewalDate")) {
                setUniformRenewalDate(data.getString("UniformRenewalDate"));
            }
            if (!data.isNull("UniformAllowanceAmount")) {
                setUniformAllowanceAmount(data.getDouble("UniformAllowanceAmount"));
            }
            if (!data.isNull("IsEmployee")) {
                setIsEmployee(data.getBoolean("IsEmployee"));
            }
            if (!data.isNull("PrimaryEmailAllowNotification")) {
                setPrimaryEmailAllowNotification(data.getBoolean("PrimaryEmailAllowNotification"));
            }
            if (!data.isNull("SecondaryEmailAllowNotification")) {
                setSecondaryEmailAllowNotification(data.getBoolean("SecondaryEmailAllowNotification"));
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

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Long getBirthday() {
        return birthday;
    }

    public String getBirthdayServer() {
        return DateOperations.getDateServer(birthday);
    }

    public String getBirthdayString() {
        String dateString = "";
        if (birthday != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(birthday);
            dateString = format.format(thisdaten);
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

    public void setDriverLicenseNumberExpire(Long driverLicenseNumberExpire) {
        this.driverLicenseNumberExpire = driverLicenseNumberExpire;
    }

    public Long getDriverLicenseNumberExpire() {
        return driverLicenseNumberExpire;
    }

    public String getDriverLicenseNumberExpireServer() {
        return DateOperations.getDateServer(driverLicenseNumberExpire);
    }

    public String getDriverLicenseNumberExpireString() {
        String dateString = "";
        if (driverLicenseNumberExpire != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(driverLicenseNumberExpire);
            dateString = format.format(thisdaten);
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
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(techLicenseNumberExpire);
            dateString = format.format(thisdaten);
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
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(vSRNumberExpire);
            dateString = format.format(thisdaten);
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
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(hireDate);
            dateString = format.format(thisdaten);
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
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(probationEndDate);
            dateString = format.format(thisdaten);
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
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(terminationDate);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public void setHireType(JSONObject hireType) {
        this.hireType = new HireTypeData(hireType);
    }

    public void setHireType(String hireType) {
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

    public void setIsHumanAndSafetyMember(boolean isHumanAndSafetyMember) {
        this.isHumanAndSafetyMember = isHumanAndSafetyMember;
    }

    public boolean getIsHumanAndSafetyMember() {
        return isHumanAndSafetyMember;
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

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getSecondaryManager() {
        return secondaryManager;
    }

    public void setSecondaryManager(String secondaryManager) {
        this.secondaryManager = secondaryManager;
    }

    public String getSecondaryManagerId() {
        return secondaryManagerId;
    }

    public void setSecondaryManagerId(String secondaryManagerId) {
        this.secondaryManagerId = secondaryManagerId;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public void setWorkPermitNumberExpire(String workPermitNumberExpire) {
        this.workPermitNumberExpire = DateOperations.getDate(workPermitNumberExpire);
    }

    public Long getWorkPermitNumberExpire() {
        return workPermitNumberExpire;
    }

    public String getWorkPermitNumberExpireString() {
        String dateString = "";
        if (workPermitNumberExpire != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(workPermitNumberExpire);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public String getUniformDescription() {
        return uniformDescription;
    }

    public void setUniformDescription(String uniformDescription) {
        this.uniformDescription = uniformDescription;
    }

    public boolean getUniformAllowance() {
        return uniformAllowance;
    }

    public String getUniformAllowanceString() {
        if (uniformAllowance) {
            return Sapphire.getInstance().getResources().getString(R.string.yes);
        } else {
            return Sapphire.getInstance().getResources().getString(R.string.no);
        }
    }

    public void setUniformAllowance(boolean uniformAllowance) {
        this.uniformAllowance = uniformAllowance;
    }

    public void setUniformRenewalDate(String uniformRenewalDate) {
        this.uniformRenewalDate = DateOperations.getDate(uniformRenewalDate);
    }

    public Long getUniformRenewalDate() {
        return uniformRenewalDate;
    }

    public String getUniformRenewalDateString() {
        String dateString = "";
        if (uniformRenewalDate != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date thisdaten = new Date();
            thisdaten.setTime(uniformRenewalDate);
            dateString = format.format(thisdaten);
        }
        return dateString;
    }

    public Double getUniformAllowanceAmount() {
        return uniformAllowanceAmount;
    }

    public void setUniformAllowanceAmount(Double uniformAllowanceAmount) {
        this.uniformAllowanceAmount = uniformAllowanceAmount;
    }

    public boolean getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    public boolean getPrimaryEmailAllowNotification() {
        return primaryEmailAllowNotification;
    }

    public void setPrimaryEmailAllowNotification(boolean primaryEmailAllowNotification) {
        this.primaryEmailAllowNotification = primaryEmailAllowNotification;
    }

    public boolean getSecondaryEmailAllowNotification() {
        return secondaryEmailAllowNotification;
    }

    public void setSecondaryEmailAllowNotification(boolean secondaryEmailAllowNotification) {
        this.secondaryEmailAllowNotification = secondaryEmailAllowNotification;
    }
}