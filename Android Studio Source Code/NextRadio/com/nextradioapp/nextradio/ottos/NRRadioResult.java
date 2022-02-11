package com.nextradioapp.nextradio.ottos;

import org.apache.activemq.transport.stomp.Stomp.Headers;

public class NRRadioResult {
    public static final int ACTION_NEWFREQ = 3;
    public static final int ACTION_TRANSITIONING = 4;
    public static final int ACTION_TURNEDOFF = 2;
    public static final int ACTION_TURNEDON = 1;
    public int action;
    public int frequencyHz;
    public boolean shouldResumeNowPlaying;
    public int subChannel;

    public String toString() {
        return "NRRadioResult " + this.action + Headers.SEPERATOR + this.frequencyHz + Headers.SEPERATOR + this.subChannel + Headers.SEPERATOR + this.shouldResumeNowPlaying;
    }
}
