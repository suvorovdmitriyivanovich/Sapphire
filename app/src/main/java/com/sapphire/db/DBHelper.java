package com.sapphire.db;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.sapphire.Sapphire;
import com.sapphire.activities.MainActivity;

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
        db.execSQL("create table libs ("
                + "id integer primary key autoincrement,"
                + "add_date integer,"
                + "last_chr_count integer,"
                + "type integer,"
                + "bookid integer,"
                + "upload integer"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {

        }
    }

    public void deleteLibs() {
        SQLiteDatabase db = getWriteDatabase();
        db.beginTransaction();
        try {
            db.delete("libs", null, null);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
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