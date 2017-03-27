package com.sapphire.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sapphire.Sapphire;

public class NetworkStateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Sapphire.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
    }
}
