package com.sapphire.logic;

import java.util.ArrayList;

public class UserInfo {
    private String authToken = "";
    private String accountId = "";
    private ProfileData profile = new ProfileData();
    private ArrayList<OrganizationData> organizations = new ArrayList<OrganizationData>();
    private ArrayList<OrganizationStructureData> organizationStructureDatas = new ArrayList<OrganizationStructureData>();
    private ArrayList<TemplateData> templateDatas = new ArrayList<TemplateData>();
    private ArrayList<ItemPriorityData> itemPriorityDatas = new ArrayList<ItemPriorityData>();
    private ArrayList<ItemStatusData> itemStatusDatas = new ArrayList<ItemStatusData>();
    private ArrayList<FileData> fileDatas = new ArrayList<FileData>();
    private OrganizationData currentOrganization = new OrganizationData();

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
}