package com.dealerpilothr.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dealerpilothr.Dealerpilothr;

public class NetworkStateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Dealerpilothr.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
    }
}
