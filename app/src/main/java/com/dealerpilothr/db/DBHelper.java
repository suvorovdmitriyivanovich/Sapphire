package com.dealerpilothr.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.dealerpilothr.models.FileData;
import com.dealerpilothr.models.ItemPriorityData;
import com.dealerpilothr.models.ItemStatusData;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.AppRoleAppSecurityData;
import com.dealerpilothr.models.MessageData;
import com.dealerpilothr.models.NavigationMenuData;
import com.dealerpilothr.models.WorkplaceInspectionItemData;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String ID = "id";
    private static final String EQUALS = " = ?";
    private static final String NOT = " != ?";
    private static final String DB_NAME = "sDB";
    private static final int DB_VERSION = 1;
    private Context mContext;
    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context){
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    private SQLiteDatabase getWriteDatabase(){
        synchronized (this) {
            return getWritableDatabase();
        }
    }

    private SQLiteDatabase getReadDatabase(){
        synchronized (this) {
            return getReadableDatabase();
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            db.enableWriteAheadLogging();
        }
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table messages ("
                + "id integer primary key autoincrement,"
                + "message text,"
                + "isread integer,"
                + "upload integer"
                + ");");

        db.execSQL("create table navigationmenus ("
                + "id integer primary key autoincrement,"
                + "menuid text,"
                + "name text,"
                + "ordernum text,"
                + "parentid text,"
                + "translationid text,"
                + "urlroute text,"
                + "cssclass text,"
                + "unicodeicon text"
                + ");");

        db.execSQL("create table workplaceinspectionitems ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "description text,"
                + "comments text,"
                + "recommendedactions text,"
                + "workplaceinspectionitemid text,"
                + "workplaceinspectionid text,"
                + "severity text,"
                + "statusid text,"
                + "priorityid text"
                + ");");

        db.execSQL("create table workplaceinspectionitemfiles ("
                + "id integer primary key autoincrement,"
                + "fileid text,"
                + "name text,"
                + "description text,"
                + "size integer,"
                + "parentid text,"
                + "isfolder integer,"
                + "file text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {

        }
    }

    public void deleteNavigationMenus() {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            db.delete("navigationmenus", null, null);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void addNavigationMenus(ArrayList<NavigationMenuData> navigationMenuDatas, AppRoleAppSecurityData appRoleAppSecurityData) {
        if (navigationMenuDatas.size() == 0) {
            return;
        }

        ContentValues cv;

        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            for (int y=0; y < navigationMenuDatas.size(); y++) {
                NavigationMenuData navigationMenuData = navigationMenuDatas.get(y);
                if (!navigationMenuData.getIsActive() || appRoleAppSecurityData.getSecurityMode(navigationMenuData.getUrlRoute(), navigationMenuData.getName()).equals("noAccess")) {
                    continue;
                }
                cv = new ContentValues();

                cv.put("menuid", navigationMenuData.getMenuId());
                cv.put("name", navigationMenuData.getName());
                cv.put("ordernum", navigationMenuData.getOrder());
                cv.put("parentid", navigationMenuData.getParentId());
                cv.put("translationid", navigationMenuData.getTranslationId());
                cv.put("urlroute", navigationMenuData.getUrlRoute());
                cv.put("cssclass", navigationMenuData.getCssClass());
                cv.put("unicodeicon", navigationMenuData.getUnicodeIcon());

                db.insert("navigationmenus", null, cv);

                ArrayList<NavigationMenuData> subMenus = navigationMenuData.getSubMenus();
                if (subMenus != null && subMenus.size() > 0) {
                    for (int s=0; s < subMenus.size(); s++) {
                        if (!subMenus.get(s).getIsActive() || appRoleAppSecurityData.getSecurityMode(subMenus.get(s).getUrlRoute(), "").equals("noAccess")) {
                            continue;
                        }
                        cv = new ContentValues();

                        cv.put("menuid", subMenus.get(s).getMenuId());
                        if (subMenus.get(s).getName().toLowerCase().equals(Dealerpilothr.getInstance().getResources().getString(R.string.text_my_hr_details).toLowerCase())) {
                            cv.put("name", Dealerpilothr.getInstance().getResources().getString(R.string.text_my_hr_details));
                        } else {
                            cv.put("name", subMenus.get(s).getName());
                        }
                        cv.put("ordernum", subMenus.get(s).getOrder());
                        cv.put("parentid", subMenus.get(s).getParentId());
                        cv.put("translationid", subMenus.get(s).getTranslationId());
                        cv.put("urlroute", subMenus.get(s).getUrlRoute());
                        cv.put("cssclass", subMenus.get(s).getCssClass());
                        cv.put("unicodeicon", subMenus.get(s).getUnicodeIcon());

                        db.insert("navigationmenus", null, cv);
                    }
                }
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        sendBrodcastLeftmenu();
    }

    public ArrayList<NavigationMenuData> getNavigationMenus() {
        ArrayList<NavigationMenuData> mNavigationMenuData = new ArrayList<NavigationMenuData>();

        String select = null;
        String[] selectarg = null;
        //select = "isread" + EQUALS + " and upload" + EQUALS;
        //selectarg = new String[]{String.valueOf(isread),String.valueOf(upload)};

        Cursor c = getReadDatabase().query("navigationmenus", null, select, selectarg, null, null, null);

        if (c.moveToFirst()) {
            int menuidColIndex = c.getColumnIndex("menuid");
            int nameColIndex = c.getColumnIndex("name");
            int ordernumColIndex = c.getColumnIndex("ordernum");
            int parentidColIndex = c.getColumnIndex("parentid");
            int translationidColIndex = c.getColumnIndex("translationid");
            int urlrouteColIndex = c.getColumnIndex("urlroute");
            int cssclassColIndex = c.getColumnIndex("cssclass");
            int unicodeiconColIndex = c.getColumnIndex("unicodeicon");

            do {
                NavigationMenuData navigationMenuData = new NavigationMenuData();
                if (menuidColIndex != -1) {
                    navigationMenuData.setMenuId(c.getString(menuidColIndex));
                }
                if (nameColIndex != -1) {
                    navigationMenuData.setName(c.getString(nameColIndex));
                }
                if (ordernumColIndex != -1) {
                    navigationMenuData.setOrder(c.getString(ordernumColIndex));
                }
                if (parentidColIndex != -1) {
                    navigationMenuData.setParentId(c.getString(parentidColIndex));
                }
                if (translationidColIndex != -1) {
                    navigationMenuData.setTranslationId(c.getString(translationidColIndex));
                }
                if (urlrouteColIndex != -1) {
                    navigationMenuData.setUrlRoute(c.getString(urlrouteColIndex));
                }
                if (cssclassColIndex != -1) {
                    navigationMenuData.setCssClass(c.getString(cssclassColIndex));
                }
                if (unicodeiconColIndex != -1) {
                    navigationMenuData.setUnicodeIcon(c.getString(unicodeiconColIndex));
                }
                //submenu
                if (!navigationMenuData.getParentId().equals("")) {
                    for (int y=0; y < mNavigationMenuData.size(); y++) {
                        if (mNavigationMenuData.get(y).getMenuId().equals(navigationMenuData.getParentId())) {
                            mNavigationMenuData.get(y).getSubMenus().add(navigationMenuData);
                            break;
                        }
                    }
                } else {
                    mNavigationMenuData.add(navigationMenuData);
                }
            } while (c.moveToNext());
        }
        c.close();

        return mNavigationMenuData;
    }

    public boolean needUpdate() {
        ArrayList<MessageData> mMessage = getMessages(-1,1);
        if (mMessage.size() > 0) {
            return true;
        }

        return false;
    }

    public ArrayList<MessageData> getMessages(int isread, int upload) {
        ArrayList<MessageData> mMessages = new ArrayList<MessageData>();

        String select = null;
        String[] selectarg = null;
        if (isread != -1 && upload != -1) {
            select = "isread" + EQUALS + " and upload" + EQUALS;
            selectarg = new String[]{String.valueOf(isread),String.valueOf(upload)};
        } else if (isread != -1 && upload == -1) {
            select = "isread" + EQUALS;
            selectarg = new String[]{String.valueOf(isread)};
        } else if (isread == -1 && upload != -1) {
            select = "upload" + EQUALS;
            selectarg = new String[]{String.valueOf(upload)};
        }


        Cursor c = getReadDatabase().query("messages", null, select, selectarg, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(ID);
            int messageColIndex = c.getColumnIndex("message");
            int isreadColIndex = c.getColumnIndex("isread");
            int uploadColIndex = c.getColumnIndex("upload");

            do {
                MessageData messageData = new MessageData();
                if (idColIndex != -1){
                    messageData.setId(c.getInt(idColIndex));
                }
                if (messageColIndex != -1) {
                    messageData.setMessage(c.getString(messageColIndex));
                }
                if (isreadColIndex != -1) {
                    messageData.setIsRead(c.getInt(isreadColIndex));
                }
                if (uploadColIndex != -1) {
                    messageData.setUpload(c.getInt(uploadColIndex));
                }
                mMessages.add(messageData);
            } while (c.moveToNext());
        }
        c.close();

        return mMessages;
    }

    public void updateMessageField(int id, String field, int valueInt, String valueString) {
        ContentValues cv = new ContentValues();

        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            if (valueInt != -1) {
                cv.put(field, valueInt);
            } else if (valueString != null) {
                cv.put(field, valueString);
            }

            db.update("messages", cv, "id" + EQUALS,
                    new String[]{String.valueOf(id)});

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        if (field.equals("isread")) {
            sendBrodcastLeftmenu();
        }
    }

    public void deleteMessages() {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            db.delete("messages", null, null);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public boolean addMessage(MessageData messageData) {
        ContentValues cv = new ContentValues();
        cv.put("message", messageData.getMessage());
        cv.put("isread", messageData.getIsRead());
        cv.put("upload", 0);

        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            db.insert("messages", null, cv);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        sendBrodcastLeftmenu();

        return true;
    }

    void sendBrodcastLeftmenu() {
        Intent intent = new Intent(Environment.BROADCAST_ACTION);
        try {
            intent.putExtra(Environment.PARAM_TASK, "updateleftmenu");
            Dealerpilothr.getInstance().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorkplaceInspectionItem(WorkplaceInspectionItemData data) {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();

            cv.put("name", data.getName());
            cv.put("description", data.getDescription());
            cv.put("comments", data.getComments());
            cv.put("recommendedactions", data.getRecommendedActions());
            cv.put("workplaceinspectionitemid", data.getWorkplaceInspectionItemId());
            cv.put("workplaceinspectionid", data.getWorkplaceInspectionId());
            cv.put("severity", data.getSeverity());
            cv.put("statusid", data.getStatus().getWorkplaceInspectionItemStatusId());
            cv.put("priorityid", data.getPriority().getWorkplaceInspectionItemPriorityId());

            db.insert("workplaceinspectionitems", null, cv);

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void changeWorkplaceInspectionItem(WorkplaceInspectionItemData data) {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();

            cv.put("name", data.getName());
            cv.put("description", data.getDescription());
            cv.put("comments", data.getComments());
            cv.put("recommendedactions", data.getRecommendedActions());
            cv.put("workplaceinspectionitemid", data.getWorkplaceInspectionItemId());
            cv.put("workplaceinspectionid", data.getWorkplaceInspectionId());
            cv.put("severity", data.getSeverity());
            cv.put("statusid", data.getStatus().getWorkplaceInspectionItemStatusId());
            cv.put("priorityid", data.getPriority().getWorkplaceInspectionItemPriorityId());

            db.update("workplaceinspectionitems", cv, "id" + EQUALS,
                    new String[]{String.valueOf(data.getId())});

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public ArrayList<WorkplaceInspectionItemData> getWorkplaceInspectionItems(String id) {
        ArrayList<WorkplaceInspectionItemData> mDatas = new ArrayList<WorkplaceInspectionItemData>();

        String select = null;
        String[] selectarg = null;
        if (!id.equals("")) {
            select = "workplaceinspectionid" + EQUALS;
            selectarg = new String[]{String.valueOf(id)};
        }

        Cursor c = getReadDatabase().query("workplaceinspectionitems", null, select, selectarg, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int descriptionColIndex = c.getColumnIndex("description");
            int commentsColIndex = c.getColumnIndex("comments");
            int recommendedactionsColIndex = c.getColumnIndex("recommendedactions");
            int workplaceinspectionitemidColIndex = c.getColumnIndex("workplaceinspectionitemid");
            int workplaceinspectionidColIndex = c.getColumnIndex("workplaceinspectionid");
            int severityColIndex = c.getColumnIndex("severity");
            int statusidColIndex = c.getColumnIndex("statusid");
            int priorityidColIndex = c.getColumnIndex("priorityid");

            do {
                WorkplaceInspectionItemData data = new WorkplaceInspectionItemData();
                if (idColIndex != -1) {
                    data.setId(c.getString(idColIndex));
                }
                if (nameColIndex != -1) {
                    data.setName(c.getString(nameColIndex));
                }
                if (descriptionColIndex != -1) {
                    data.setDescription(c.getString(descriptionColIndex));
                }
                if (commentsColIndex != -1) {
                    data.setComments(c.getString(commentsColIndex));
                }
                if (recommendedactionsColIndex != -1) {
                    data.setRecommendedActions(c.getString(recommendedactionsColIndex));
                }
                if (workplaceinspectionitemidColIndex != -1) {
                    data.setWorkplaceInspectionItemId(c.getString(workplaceinspectionitemidColIndex));
                }
                if (workplaceinspectionidColIndex != -1) {
                    data.setWorkplaceInspectionId(c.getString(workplaceinspectionidColIndex));
                }
                if (severityColIndex != -1) {
                    data.setSeverity(c.getString(severityColIndex));
                }
                if (statusidColIndex != -1) {
                    data.setStatus(new ItemStatusData(c.getString(statusidColIndex)));
                }
                if (priorityidColIndex != -1) {
                    data.setPriority(new ItemPriorityData(c.getString(priorityidColIndex)));
                }

                mDatas.add(data);
            } while (c.moveToNext());
        }
        c.close();

        return mDatas;
    }

    public void deleteWorkplaceInspectionItem(String id) {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            db.delete("workplaceinspectionitems", "id = " + id, null);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void addWorkplaceInspectionItemFile(FileData data) {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();

            cv.put("fileid", data.getFileId());
            cv.put("name", data.getName());
            cv.put("description", data.getDescription());
            cv.put("parentid", data.getParentId());
            cv.put("size", data.getSize());
            cv.put("isfolder", data.getIsFolder());
            cv.put("file", data.getFile());

            db.insert("workplaceinspectionitemfiles", null, cv);

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void changeWorkplaceInspectionItemFile(FileData data) {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();

            cv.put("fileid", data.getFileId());
            cv.put("name", data.getName());
            cv.put("description", data.getDescription());
            cv.put("parentid", data.getParentId());
            cv.put("size", data.getSize());
            cv.put("isfolder", data.getIsFolder());
            cv.put("file", data.getFile());

            db.update("workplaceinspectionitemfiles", cv, "id" + EQUALS,
                    new String[]{String.valueOf(data.getId())});

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public ArrayList<FileData> getWorkplaceInspectionItemFiles(String id) {
        ArrayList<FileData> mDatas = new ArrayList<FileData>();

        String select = null;
        String[] selectarg = null;
        if (!id.equals("")) {
            select = "parentid" + EQUALS;
            selectarg = new String[]{String.valueOf(id)};
        }

        Cursor c = getReadDatabase().query("workplaceinspectionitemfiles", null, select, selectarg, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int fileidColIndex = c.getColumnIndex("fileid");
            int nameColIndex = c.getColumnIndex("name");
            int descriptionColIndex = c.getColumnIndex("description");
            int parentidColIndex = c.getColumnIndex("parentid");
            int sizeColIndex = c.getColumnIndex("size");
            int isfolderColIndex = c.getColumnIndex("isfolder");
            int fileColIndex = c.getColumnIndex("file");

            do {
                FileData data = new FileData();
                if (idColIndex != -1) {
                    data.setId(c.getString(idColIndex));
                }
                if (fileidColIndex != -1) {
                    data.setFileId(c.getString(fileidColIndex));
                }
                if (nameColIndex != -1) {
                    data.setName(c.getString(nameColIndex));
                }
                if (descriptionColIndex != -1) {
                    data.setDescription(c.getString(descriptionColIndex));
                }
                if (parentidColIndex != -1) {
                    data.setParentId(c.getString(parentidColIndex));
                }
                if (sizeColIndex != -1) {
                    data.setSize(c.getInt(sizeColIndex));
                }
                if (isfolderColIndex != -1) {
                    data.setIsFolder(c.getInt(isfolderColIndex));
                }
                if (fileColIndex != -1) {
                    data.setFile(c.getString(fileColIndex));
                }

                mDatas.add(data);
            } while (c.moveToNext());
        }
        c.close();

        return mDatas;
    }

    public void deleteWorkplaceInspectionItemFile(String id) {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            db.delete("workplaceinspectionitemfiles", "id = " + id, null);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void deleteWorkplaceInspectionItemFileServerId(String fileid) {
        String id = "";

        SQLiteDatabase db = getWriteDatabase();

        Cursor c = db.query("workplaceinspectionitemfiles", null, "fileid = ?", new String[] {String.valueOf(fileid)}, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");

            do {
                id = c.getString(idColIndex);
                break;
            } while (c.moveToNext());
        }
        c.close();

        if (!id.equals("")) {
            db.beginTransaction();
            try {
                db.delete("workplaceinspectionitemfiles", "id = " + id, null);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    public boolean existUpdate() {
        boolean exist = false;

        try {
            Cursor c = getReadDatabase().query("workplaceinspectionitems", null, null, null, null, null, null);

            if (c.moveToFirst()) {
                exist = true;
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!exist) {
            try {
                Cursor c = getReadDatabase().query("workplaceinspectionitemfiles", null, null, null, null, null, null);

                if (c.moveToFirst()) {
                    exist = true;
                }
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return exist;
    }
}