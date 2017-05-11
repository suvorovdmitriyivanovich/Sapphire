package com.sapphire.logic;

import com.sapphire.models.AppRoleAppSecurityData;
import com.sapphire.models.AttendanceCodeData;
import com.sapphire.models.CategoryData;
import com.sapphire.models.DayData;
import com.sapphire.models.FileData;
import com.sapphire.models.ItemPriorityData;
import com.sapphire.models.ItemStatusData;
import com.sapphire.models.MemberData;
import com.sapphire.models.OrganizationData;
import com.sapphire.models.OrganizationStructureData;
import com.sapphire.models.ParameterData;
import com.sapphire.models.ProfileData;
import com.sapphire.models.PunchesCategoryData;
import com.sapphire.models.TemplateData;
import com.sapphire.models.TimeBankAccountData;
import com.sapphire.models.TimeZoneData;
import com.sapphire.models.TopicData;
import com.sapphire.models.WorkplaceInspectionData;

import org.json.JSONObject;

import java.util.ArrayList;

public class UserInfo {
    private String authToken = "";
    private String authTokenFirst = "";
    private String accountId = "";
    private ProfileData profile = new ProfileData();
    private ArrayList<OrganizationData> organizations = new ArrayList<OrganizationData>();
    private ArrayList<OrganizationStructureData> organizationStructureDatas = new ArrayList<OrganizationStructureData>();
    private ArrayList<TemplateData> templateDatas = new ArrayList<TemplateData>();
    private ArrayList<TemplateData> templateMeetingDatas = new ArrayList<TemplateData>();
    private ArrayList<ItemPriorityData> itemPriorityDatas = new ArrayList<ItemPriorityData>();
    private ArrayList<ItemStatusData> itemStatusDatas = new ArrayList<ItemStatusData>();
    private ArrayList<FileData> fileDatas = new ArrayList<FileData>();
    private OrganizationData currentOrganization = new OrganizationData();
    private OrganizationData masterOrganization = new OrganizationData();
    private ArrayList<MemberData> members = new ArrayList<MemberData>();
    private ArrayList<TopicData> topics = new ArrayList<TopicData>();
    private ArrayList<MemberData> allmembers = new ArrayList<MemberData>();
    private TopicData topic = null;
    private int position = -1;
    private ArrayList<CategoryData> docCategoryDatas = new ArrayList<CategoryData>();
    private TimeZoneData timeZone = new TimeZoneData();
    private ArrayList<TimeBankAccountData> timeBankDatas = new ArrayList<TimeBankAccountData>();
    private ArrayList<AttendanceCodeData> attendanceCodeDatas = new ArrayList<AttendanceCodeData>();
    private ArrayList<DayData> days = new ArrayList<DayData>();
    private ArrayList<DayData> allDays = new ArrayList<DayData>();
    private ArrayList<ProfileData> assignedProfiles = new ArrayList<ProfileData>();
    private ArrayList<ProfileData> allAssignedProfiles = new ArrayList<ProfileData>();
    private ArrayList<ParameterData> parameterDatas = new ArrayList<ParameterData>();
    private String accountSession = "";
    private ArrayList<PunchesCategoryData> punchesCategories = new ArrayList<PunchesCategoryData>();
    private WorkplaceInspectionData workplaceInspection = new WorkplaceInspectionData();
    private AppRoleAppSecurityData globalAppRoleAppSecurities = new AppRoleAppSecurityData();

    //---------------------Singleton---------------------------
    private static UserInfo userInfo;
    private UserInfo() {}
    public static UserInfo getUserInfo() {
        if(userInfo == null)
            userInfo = new UserInfo();
        return userInfo;
    }
    //---------------------------------------------------------

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthTokenFirst(String authTokenFirst) {
        this.authTokenFirst = authTokenFirst;
    }

    public String getAuthTokenFirst() {
        return authTokenFirst;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setProfile(ProfileData profile) {
        this.profile = profile;
    }

    public ProfileData getProfile() {
        return profile;
    }

    public void setOrganizations(ArrayList<OrganizationData> organizations) {
        this.organizations = organizations;
    }

    public ArrayList<OrganizationData> getOrganizations() {
        return organizations;
    }

    public void setCurrentOrganization(OrganizationData currentOrganization) {
        this.currentOrganization = currentOrganization;
    }

    public OrganizationData getCurrentOrganization() {
        return currentOrganization;
    }

    public void setMasterOrganization(OrganizationData masterOrganization) {
        this.masterOrganization = masterOrganization;
    }

    public OrganizationData getMasterOrganization() {
        return masterOrganization;
    }

    public ArrayList<OrganizationStructureData> getOrganizationStructureDatas() {
        return organizationStructureDatas;
    }

    public OrganizationStructureData getOrganizationStructureDataById(String id) {
        for(OrganizationStructureData item: this.organizationStructureDatas) {
            if (item.getId().equals(id)) {
                return item;
            }
            item = findObjectOrganizationStructureData(item, id);
            if(item != null) {
                return item;
            }
        }
        return null;
    }

    private OrganizationStructureData findObjectOrganizationStructureData(OrganizationStructureData data, String id) {
        for (OrganizationStructureData item: data.getSubOrganizationStructures()) {
            if (item.getId().equals(id)) {
                return item;
            }
            item = findObjectOrganizationStructureData(item, id);
            if(item != null) {
                return item;
            }
        }
        return null;
    }

    public void setOrganizationStructureDatas(ArrayList<OrganizationStructureData> organizationStructureDatas) {
        this.organizationStructureDatas.clear();
        this.organizationStructureDatas.addAll(organizationStructureDatas);
    }

    public ArrayList<TemplateData> getTemplateDatas() {
        return templateDatas;
    }

    public void setTemplateDatas(ArrayList<TemplateData> templateDatas) {
        this.templateDatas.clear();
        this.templateDatas.add(new TemplateData());
        this.templateDatas.addAll(templateDatas);
    }

    public ArrayList<TemplateData> getTemplateMeetingDatas() {
        return templateMeetingDatas;
    }

    public void setTemplateMeetingDatas(ArrayList<TemplateData> templateMeetingDatas) {
        this.templateMeetingDatas.clear();
        this.templateMeetingDatas.add(new TemplateData());
        this.templateMeetingDatas.addAll(templateMeetingDatas);
    }

    public ArrayList<ItemPriorityData> getItemPriorityDatas() {
        return itemPriorityDatas;
    }

    public void setItemPriorityDatas(ArrayList<ItemPriorityData> itemPriorityDatas) {
        this.itemPriorityDatas.clear();
        this.itemPriorityDatas.add(new ItemPriorityData());
        this.itemPriorityDatas.addAll(itemPriorityDatas);
    }

    public ArrayList<ItemStatusData> getItemStatusDatas() {
        return itemStatusDatas;
    }

    public void setItemStatusDatas(ArrayList<ItemStatusData> itemStatusDatas) {
        this.itemStatusDatas.clear();
        this.itemStatusDatas.add(new ItemStatusData());
        this.itemStatusDatas.addAll(itemStatusDatas);
    }

    public ArrayList<FileData> getFileDatas() {
        return fileDatas;
    }

    public void setFileDatas(ArrayList<FileData> fileDatas) {
        this.fileDatas.clear();
        this.fileDatas.addAll(fileDatas);
    }

    public ArrayList<MemberData> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<MemberData> members) {
        this.members.clear();
        this.members.addAll(members);
    }

    public ArrayList<TopicData> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<TopicData> topics) {
        this.topics.clear();
        this.topics.addAll(topics);
    }

    public ArrayList<MemberData> getAllMembers() {
        ArrayList<MemberData> arrayList = new ArrayList<>();
        for (MemberData item: allmembers) {
            arrayList.add(new MemberData(item));
        }

        return arrayList;
    }

    public void setAllMembers(ArrayList<MemberData> allmembers) {
        this.allmembers.clear();
        this.allmembers.addAll(allmembers);
    }

    public void setTopic(TopicData topic) {
        this.topic = topic;
    }

    public TopicData getTopic() {
        return topic;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<CategoryData> getDocCategoryDatas() {
        return docCategoryDatas;
    }

    public void setDocCategoryDatas(ArrayList<CategoryData> datas) {
        this.docCategoryDatas.clear();
        this.docCategoryDatas.add(new CategoryData());
        this.docCategoryDatas.addAll(datas);
    }

    public void setTimeZone(TimeZoneData timeZone) {
        this.timeZone = timeZone;
    }

    public TimeZoneData getTimeZone() {
        return timeZone;
    }

    public void setTimeBankDatas(ArrayList<TimeBankAccountData> timeBankDatas) {
        this.timeBankDatas.clear();
        this.timeBankDatas.add(new TimeBankAccountData());
        this.timeBankDatas.addAll(timeBankDatas);
    }

    public ArrayList<TimeBankAccountData> getTimeBankDatas() {
        return timeBankDatas;
    }

    public void setAttendanceCodeDatas(ArrayList<AttendanceCodeData> attendanceCodeDatas) {
        this.attendanceCodeDatas.clear();
        this.attendanceCodeDatas.add(new AttendanceCodeData());
        this.attendanceCodeDatas.addAll(attendanceCodeDatas);
    }

    public ArrayList<AttendanceCodeData> getAttendanceCodeDatas() {
        return attendanceCodeDatas;
    }

    public String getAttendanceCodeName(String id) {
        String attendanceCodeName = "";
        for (AttendanceCodeData item: attendanceCodeDatas) {
            if (item.getAttendanceCodeId().equals(id)) {
                attendanceCodeName = item.getName();
                break;
            }
        }
        return attendanceCodeName;
    }

    public void setDays(ArrayList<DayData> days) {
        this.days = days;
    }

    public ArrayList<DayData> getDays() {
        return days;
    }

    public void setAllDays(ArrayList<DayData> allDays) {
        this.allDays = allDays;
    }

    public ArrayList<DayData> getAllDays() {
        return allDays;
    }

    public void setAssignedProfiles(ArrayList<ProfileData> assignedProfiles) {
        this.assignedProfiles = assignedProfiles;
    }

    public ArrayList<ProfileData> getAssignedProfiles() {
        return assignedProfiles;
    }

    public void setParameterDatas(ArrayList<ParameterData> parameterDatas) {
        this.parameterDatas = parameterDatas;
    }

    public ArrayList<ParameterData> geParameterDatas() {
        return parameterDatas;
    }

    public ArrayList<CategoryData> geParameterCategoryDatas() {
        if (parameterDatas.size() == 0) {
            return new ArrayList<CategoryData>();
        } else {
            return parameterDatas.get(0).getCategories();
        }
    }

    public ArrayList<ProfileData> getAllAssignedProfiles() {
        return allAssignedProfiles;
    }

    public void setAllAssignedProfiles(ArrayList<ProfileData> allAssignedProfiles) {
        this.allAssignedProfiles = allAssignedProfiles;
    }

    public String getAccountSession() {
        return accountSession;
    }

    public void setAccountSession(String accountSession) {
        this.accountSession = accountSession;
    }

    public void setPunchesCategories(ArrayList<PunchesCategoryData> punchesCategories) {
        this.punchesCategories = punchesCategories;
    }

    public ArrayList<PunchesCategoryData> gePunchesCategories() {
        return punchesCategories;
    }

    public String getPunchesCategoryId(String name) {
        String punchesCategoryId = "";
        for (PunchesCategoryData item: punchesCategories) {
            if (item.getName().equals(name)) {
                punchesCategoryId = item.getPunchCategoryId();
                break;
            }
        }
        return punchesCategoryId;
    }

    public WorkplaceInspectionData getWorkplaceInspection() {
        return workplaceInspection;
    }

    public void setWorkplaceInspection(WorkplaceInspectionData workplaceInspection) {
        this.workplaceInspection = workplaceInspection;
    }

    public AppRoleAppSecurityData getGlobalAppRoleAppSecurities() {
        return globalAppRoleAppSecurities;
    }

    public void setGlobalAppRoleAppSecurities(AppRoleAppSecurityData globalAppRoleAppSecurities) {
        this.globalAppRoleAppSecurities = globalAppRoleAppSecurities;
    }
}