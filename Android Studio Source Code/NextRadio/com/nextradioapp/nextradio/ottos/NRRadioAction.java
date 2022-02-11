package com.nextradioapp.nextradio.ottos;

import org.apache.activemq.transport.stomp.Stomp.Headers;

public class NRRadioAction {
    public static final int ACTION_HEART_OFF = 6;
    public static final int ACTION_HEART_ON = 7;
    public static final int ACTION_SEEK = 4;
    public static final int ACTION_SET_FREQ = 5;
    public static final int ACTION_TOGGLE_OUTPUT_TO_SPEAKER = 8;
    public static final int ACTION_TUNE = 3;
    public static final int ACTION_TURNOFF = 2;
    public static final int ACTION_TURNON = 1;
    public int action;
    public int direction;
    public int frequencyHz;
    public boolean fromWidget;
    public boolean isQuitting;
    public boolean shouldResumeNowPlaying;
    public int subChannel;
    public boolean toggleSpeakerOutput;

    public NRRadioAction() {
        this.fromWidget = false;
    }

    public String toString() {
        return "NRRadioAction " + this.action + Headers.SEPERATOR + this.frequencyHz + Headers.SEPERATOR + this.subChannel + Headers.SEPERATOR + this.direction + Headers.SEPERATOR + this.shouldResumeNowPlaying;
    }
}
