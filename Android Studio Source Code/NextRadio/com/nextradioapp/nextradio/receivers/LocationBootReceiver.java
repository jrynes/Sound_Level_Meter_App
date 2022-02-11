package com.nextradioapp.nextradio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            LocationReceiver.getInstance().scheduleAlarms(context);
        }
    }
}
