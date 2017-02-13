package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class TemplateData {
    private String workplaceInspectionTemplateId = "";
    private String name = "";
    private String description = "";
    private ArrayList<TemplateData> subTemplates = new ArrayList<TemplateData>();

    public TemplateData() {

    }

    public TemplateData(String name) {
        setName(name);
    }

    public TemplateData(JSONObject data) {
        try {
            if (!data.isNull("WorkplaceInspectionTemplateId")) {
                setWorkplaceInspectionTemplateId(data.getString("WorkplaceInspectionTemplateId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("SubTemplates")) {
                setSubTemplates(data.getJSONArray("SubTemplates"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWorkplaceInspectionTemplateId() {
        return workplaceInspectionTemplateId;
    }

    public void setWorkplaceInspectionTemplateId(String workplaceInspectionTemplateId) {
        this.workplaceInspectionTemplateId = workplaceInspectionTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<TemplateData> getSubTemplates() {
        return subTemplates;
    }

    public void setSubTemplates(JSONArray subTemplates) {
        ArrayList<TemplateData> templatesDatas = new ArrayList<TemplateData>();
        for (int y=0; y < subTemplates.length(); y++) {
            try {
                templatesDatas.add(new TemplateData(subTemplates.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.subTemplates = templatesDatas;
    }
}