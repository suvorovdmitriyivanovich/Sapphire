package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingData {
    private boolean canAdd = false;
    private boolean canUpdate = false;
    private boolean canDelete = false;
    private boolean canRead = false;
    private boolean canDownload = false;
    private boolean canHaveChildren = false;
    private boolean constraintChildren = false;
    private boolean canEdit = false;
    private boolean canSubmitProgress = false;
    private boolean canAssign = false;
    private boolean canAddChild = false;

    public SettingData() {

    }

    public SettingData(JSONObject data) {
        try {
            if (!data.isNull("CanAdd")) {
                setCanAdd(data.getBoolean("CanAdd"));
            }
            if (!data.isNull("CanUpdate")) {
                setCanUpdate(data.getBoolean("CanUpdate"));
            }
            if (!data.isNull("CanDelete")) {
                setCanDelete(data.getBoolean("CanDelete"));
            }
            if (!data.isNull("CanRead")) {
                setCanRead(data.getBoolean("CanRead"));
            }
            if (!data.isNull("CanDownload")) {
                setCanDownload(data.getBoolean("CanDownload"));
            }
            if (!data.isNull("CanHaveChildren")) {
                setCanHaveChildren(data.getBoolean("CanHaveChildren"));
            }
            if (!data.isNull("ConstraintChildren")) {
                setConstraintChildren(data.getBoolean("ConstraintChildren"));
            }
            if (!data.isNull("CanEdit")) {
                setCanEdit(data.getBoolean("CanEdit"));
            }
            if (!data.isNull("CanSubmitProgress")) {
                setCanSubmitProgress(data.getBoolean("CanSubmitProgress"));
            }
            if (!data.isNull("CanAssign")) {
                setCanAssign(data.getBoolean("CanAssign"));
            }
            if (!data.isNull("CanAddChild")) {
                setCanAddChild(data.getBoolean("CanAddChild"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean getCanAdd() {
        return canAdd;
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean getCanDownload() {
        return canDownload;
    }

    public void setCanDownload(boolean canDownload) {
        this.canDownload = canDownload;
    }

    public boolean getCanHaveChildren() {
        return canHaveChildren;
    }

    public void setCanHaveChildren(boolean canHaveChildren) {
        this.canHaveChildren = canHaveChildren;
    }

    public boolean getConstraintChildren() {
        return constraintChildren;
    }

    public void setConstraintChildren(boolean constraintChildren) {
        this.constraintChildren = constraintChildren;
    }

    public boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean getCanSubmitProgress() {
        return canSubmitProgress;
    }

    public void setCanSubmitProgress(boolean canSubmitProgress) {
        this.canSubmitProgress = canSubmitProgress;
    }

    public boolean getCanAssign() {
        return canAssign;
    }

    public void setCanAssign(boolean canAssign) {
        this.canAssign = canAssign;
    }

    public boolean getCanAddChild() {
        return canAddChild;
    }

    public void setCanAddChild(boolean canAddChild) {
        this.canAddChild = canAddChild;
    }
}