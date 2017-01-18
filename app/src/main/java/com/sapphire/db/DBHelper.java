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
import com.sapphire.logic.Message;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {

        }
    }

    public boolean needUpdate() {
        ArrayList<Message> mMessage = getMessages(-1,1);
        if (mMessage.size() > 0) {
            return true;
        }

        return false;
    }

    public ArrayList<Message> getMessages(int isread, int upload) {
        ArrayList<Message> mMessages = new ArrayList<Message>();

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
                Message message = new Message();
                if (idColIndex != -1){
                    message.setId(c.getInt(idColIndex));
                }
                if (messageColIndex != -1) {
                    message.setMessage(c.getString(messageColIndex));
                }
                if (isreadColIndex != -1) {
                    message.setIsRead(c.getInt(isreadColIndex));
                }
                if (uploadColIndex != -1) {
                    message.setUpload(c.getInt(uploadColIndex));
                }
                mMessages.add(message);
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

    public boolean addMessage(Message message) {
        ContentValues cv = new ContentValues();
        cv.put("message", message.getMessage());
        cv.put("isread", message.getIsRead());
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