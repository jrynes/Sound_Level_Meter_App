package com.nextradioapp.nextradio.ottos;

import android.content.Intent;

public class NRRadioAvailabilityEvent {
    public static final int AIRPLANE_MODE_ON = 3;
    public static final int HEADPHONES_CONNECTED = 2;
    public static final int HEADPHONES_DISCONNECTED = 1;
    public static final int HEADPHONES_WARNING = 0;
    public Intent data;
    public int status;
}
