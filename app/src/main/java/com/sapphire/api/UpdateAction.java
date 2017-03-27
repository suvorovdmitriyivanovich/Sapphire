package com.sapphire.api;

import android.content.Context;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.db.DBHelper;
import com.sapphire.logic.Environment;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.FileData;
import java.util.ArrayList;

public class UpdateAction extends BaseActivity implements WorkplaceInspectionItemAddAction.RequestWorkplaceInspectionItemAdd,
                                                          UploadFileAction.RequestUploadFile,
                                                          FileAddAction.RequestFileAdd{

    public interface RequestUpdate {
        public void onRequestUpdate(String result);
    }

    private Context mContext;
    private String resultData;
    private ArrayList<FileData> datas;
    private int lastindex;
    private FileData data;

    public UpdateAction(Context context) {
        this.mContext = context;

        new WorkplaceInspectionItemAddAction(UpdateAction.this, null, true, 0, "").execute();
    }

    @Override
    public void onRequestWorkplaceInspectionItemAdd(String result, boolean neddclosepd, int ihms, String id) {
        resultData = result;
        if (!result.equals("OK")) {
            returnResult();
        } else {
            datas = DBHelper.getInstance(Sapphire.getInstance()).getWorkplaceInspectionItemFiles("");
            if (datas.size() == 0) {
                returnResult();
            } else {
                lastindex = 0;
                updateFile();
            }
        }
    }

    private void updateFile() {
        if (lastindex >= datas.size()) {
            returnResult();
            return;
        }
        data = datas.get(lastindex);
        lastindex = lastindex + 1;
        if (data.getFileId().equals("")) {
            new UploadFileAction(UpdateAction.this, data.getFile()).execute();
        } else {
            new FileAddAction(UpdateAction.this, data.getFileId(), data.getParentId(), Environment.WorkplaceInspectionsItemsFilesURL, "WorkplaceInspectionItemId").execute();
        }
    }

    @Override
    public void onRequestUploadFile(String result, FileData fileData) {
        resultData = result;
        if (!result.equals("OK")) {
            returnResult();
        } else {
            UserInfo.getUserInfo().getFileDatas().add(fileData);
            new FileAddAction(UpdateAction.this, fileData.getFileId(), data.getParentId(), Environment.WorkplaceInspectionsItemsFilesURL, "WorkplaceInspectionItemId").execute();
        }
    }

    @Override
    public void onRequestFileAdd(String result) {
        resultData = result;
        if (!result.equals("OK")) {
            returnResult();
        } else {
            updateFile();
        }
    }

    private void returnResult() {
        ((RequestUpdate) mContext).onRequestUpdate(resultData);
    }
}
