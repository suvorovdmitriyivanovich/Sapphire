package com.dealerpilothr.models;

public class LanguageData {
    private String name = "";
    private boolean checked = false;

    public LanguageData(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}