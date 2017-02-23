package com.sapphire.logic;

import java.util.ArrayList;

public class UserInfo {
    private String authToken = "";
    private String accountId = "";
    private String profileId = "";
    private ArrayList<OrganizationData> organizations = new ArrayList<OrganizationData>();
    private ArrayList<OrganizationStructureData> organizationStructureDatas = new ArrayList<OrganizationStructureData>();
    private ArrayList<TemplateData> templateDatas = new ArrayList<TemplateData>();
    private ArrayList<ItemPriorityData> itemPriorityDatas = new ArrayList<ItemPriorityData>();
    private ArrayList<ItemStatusData> itemStatusDatas = new ArrayList<ItemStatusData>();

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

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setOrganizations(ArrayList<OrganizationData> organizations) {
        this.organizations = organizations;
    }

    public ArrayList<OrganizationData> getOrganizations() {
        return organizations;
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
}