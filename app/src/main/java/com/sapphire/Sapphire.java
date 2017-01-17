package com.sapphire;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

public class Sapphire extends Application {
    private static Sapphire mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Sapphire getInstance() {
        return mInstance;
    }

    public static void exit(Activity sender) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sender.startActivity(intent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}