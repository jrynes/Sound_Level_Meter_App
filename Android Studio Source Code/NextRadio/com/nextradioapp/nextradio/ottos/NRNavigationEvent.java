package com.nextradioapp.nextradio.ottos;

public class NRNavigationEvent {
    public static final int BOOKMARKS = 7;
    public static final int FREQUENCY_LIST = 3;
    public static final int GENRE_LIST = 2;
    public static final int HISTORY = 8;
    public static final int MANUAL_TUNER = 4;
    public static final int MY_FAVORITES = 0;
    public static final int NOW_PLAYING = 12;
    public static final int ON_AIR_NOW = 1;
    public static final int OUTPUT_TO_SPEAKER = 10;
    public static final int QUIT = 11;
    public static final int RECENTLY_PLAYED = 9;
    public static final int SETTINGS = 5;
    public static final int UPDATE_STATIONS = 6;
    public int chosenView;

    public String toString() {
        return "NRNavigationEvent chosenView:" + this.chosenView;
    }
}
