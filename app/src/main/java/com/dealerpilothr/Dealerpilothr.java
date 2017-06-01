package com.dealerpilothr;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import com.dealerpilothr.db.DBHelper;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.logic.Environment;

public class Dealerpilothr extends Application {
    private static Dealerpilothr mInstance;
    private boolean needUpdate = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
    }

    public static synchronized Dealerpilothr getInstance() {
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

    public void setNeedUpdate(boolean needUpdate) {
        if (needUpdate) {
            needUpdate = DBHelper.getInstance(mInstance).existUpdate();
        }
        this.needUpdate = needUpdate;

        Intent intentBr = new Intent(Environment.BROADCAST_ACTION);
        try {
            intentBr.putExtra(Environment.PARAM_TASK, "updatebottom");
            mInstance.sendBroadcast(intentBr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    if (activity != null) {
                        intentBr = new Intent(activity.getLocalClassName());
                        try {
                            intentBr.putExtra(Environment.PARAM_TASK, "updatebottom");
                            mInstance.sendBroadcast(intentBr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    }

    public boolean getNeedUpdate() {
        return needUpdate;
    }
}