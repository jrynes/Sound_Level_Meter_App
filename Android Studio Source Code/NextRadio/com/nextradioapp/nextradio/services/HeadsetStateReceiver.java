package com.nextradioapp.nextradio.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.nextradioapp.nextradio.BusProvider;
import com.nextradioapp.nextradio.ottos.NRRadioAvailabilityEvent;

public class HeadsetStateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
            int state = intent.getIntExtra("state", -1);
            NRRadioAvailabilityEvent nrRadioAvailabilityEvent = new NRRadioAvailabilityEvent();
            nrRadioAvailabilityEvent.status = state + 1;
            BusProvider.getInstance1().post(nrRadioAvailabilityEvent);
        }
    }
}
