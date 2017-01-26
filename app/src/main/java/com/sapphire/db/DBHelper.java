package com.sapphire.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.sapphire.Sapphire;
import com.sapphire.activities.MainActivity;
import com.sapphire.logic.MessageData;
import com.sapphire.logic.NavigationMenuData;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String ID = "id";
    private static final String EQUALS = " = ?";
    private static final String NOT = " != ?";
    private static final String DB_NAME = "leDB";
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
                + "cssclass text"
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

    public void addNavigationMenus(ArrayList<NavigationMenuData> navigationMenuDatas) {
        if (navigationMenuDatas.size() == 0) {
            return;
        }

        ContentValues cv;

        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            for (int y=0; y < navigationMenuDatas.size(); y++) {
                NavigationMenuData navigationMenuData = navigationMenuDatas.get(y);
                cv = new ContentValues();

                cv.put("menuid", navigationMenuData.getMenuId());
                cv.put("name", navigationMenuData.getName());
                cv.put("ordernum", navigationMenuData.getOrder());
                cv.put("parentid", navigationMenuData.getParentId());
                cv.put("translationid", navigationMenuData.getTranslationId());
                cv.put("urlroute", navigationMenuData.getUrlRoute());
                cv.put("cssclass", navigationMenuData.getCssClass());

                db.insert("navigationmenus", null, cv);

                ArrayList<NavigationMenuData> subMenus = navigationMenuData.getSubMenus();
                if (subMenus != null && subMenus.size() > 0) {
                    for (int s=0; s < subMenus.size(); s++) {
                        cv = new ContentValues();

                        cv.put("menuid", subMenus.get(s).getMenuId());
                        cv.put("name", subMenus.get(s).getName());
                        cv.put("ordernum", subMenus.get(s).getOrder());
                        cv.put("parentid", subMenus.get(s).getParentId());
                        cv.put("translationid", subMenus.get(s).getTranslationId());
                        cv.put("urlroute", subMenus.get(s).getUrlRoute());
                        cv.put("cssclass", subMenus.get(s).getCssClass());

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
        Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
        try {
            intent.putExtra(MainActivity.PARAM_TASK, "updateleftmenu");
            Sapphire.getInstance().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}