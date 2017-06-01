package com.dealerpilothr.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ParameterData {
    private ArrayList<CategoryData> categories = new ArrayList<CategoryData>();
    private ArrayList<TypeData> types = new ArrayList<TypeData>();

    public ParameterData() {

    }

    public ParameterData(JSONObject data) {
        try {
            if (!data.isNull("Categories")) {
                setCategories(data.getJSONArray("Categories"));
            }
            if (!data.isNull("Types")) {
                setTypes(data.getJSONArray("Types"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CategoryData> getCategories() {
        return categories;
    }

    public void setCategories(JSONArray categories) {
        ArrayList<CategoryData> datas = new ArrayList<CategoryData>();
        for (int y=0; y < categories.length(); y++) {
            try {
                datas.add(new CategoryData(categories.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.categories = datas;
    }

    public ArrayList<TypeData> getTypes() {
        return types;
    }

    public void setTypes(JSONArray types) {
        ArrayList<TypeData> datas = new ArrayList<TypeData>();
        for (int y=0; y < types.length(); y++) {
            try {
                datas.add(new TypeData(types.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.types = datas;
    }
}