package com.nextradioapp.nextradio.mixpanel;

public class MipProperties {
    public static String ARTIST_NAME;
    public static String CAMPAIGN_ID;
    public static String LISTENING;
    public static String STATION_NAME;
    public static String SUPPORTED_DEVICE;
    public static String TAP;
    public static String VIEW;

    static {
        TAP = "Tap";
        VIEW = "View";
        SUPPORTED_DEVICE = "Supported Device";
        CAMPAIGN_ID = "campaign_id";
        LISTENING = "Listening";
        STATION_NAME = "station_name";
        ARTIST_NAME = "artist_name";
    }
}
