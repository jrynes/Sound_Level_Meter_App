package com.amazon.device.ads;

import com.google.android.gms.location.places.Place;
import com.mixpanel.android.java_websocket.framing.CloseFrame;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

enum AAXCreative {
    HTML(CloseFrame.NO_UTF8),
    MRAID1(Place.TYPE_POSTAL_CODE_PREFIX),
    MRAID2(Place.TYPE_POSTAL_TOWN),
    INTERSTITIAL(CloseFrame.POLICY_VALIDATION),
    CAN_PLAY_AUDIO1(CloseFrame.GOING_AWAY),
    CAN_PLAY_AUDIO2(CloseFrame.PROTOCOL_ERROR),
    CAN_EXPAND1(CloseFrame.REFUSE),
    CAN_EXPAND2(Place.TYPE_COLLOQUIAL_AREA),
    CAN_PLAY_VIDEO(Place.TYPE_POST_BOX),
    VIDEO_INTERSTITIAL(Place.TYPE_TRANSIT_STATION),
    REQUIRES_TRANSPARENCY(AdProperties.REQUIRES_TRANSPARENCY);
    
    private static final HashSet<AAXCreative> primaryCreativeTypes;
    private final int id;

    static {
        primaryCreativeTypes = new HashSet();
        primaryCreativeTypes.add(HTML);
        primaryCreativeTypes.add(MRAID1);
        primaryCreativeTypes.add(MRAID2);
        primaryCreativeTypes.add(INTERSTITIAL);
        primaryCreativeTypes.add(VIDEO_INTERSTITIAL);
    }

    private AAXCreative(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static boolean containsPrimaryCreativeType(Set<AAXCreative> creativeTypes) {
        Iterator i$ = primaryCreativeTypes.iterator();
        while (i$.hasNext()) {
            if (creativeTypes.contains((AAXCreative) i$.next())) {
                return true;
            }
        }
        return false;
    }

    public static AAXCreative getCreativeType(int id) {
        switch (id) {
            case CloseFrame.GOING_AWAY /*1001*/:
                return CAN_PLAY_AUDIO1;
            case CloseFrame.PROTOCOL_ERROR /*1002*/:
                return CAN_PLAY_AUDIO2;
            case CloseFrame.REFUSE /*1003*/:
                return CAN_EXPAND1;
            case Place.TYPE_COLLOQUIAL_AREA /*1004*/:
                return CAN_EXPAND2;
            case CloseFrame.NO_UTF8 /*1007*/:
                return HTML;
            case CloseFrame.POLICY_VALIDATION /*1008*/:
                return INTERSTITIAL;
            case Place.TYPE_POST_BOX /*1014*/:
                return CAN_PLAY_VIDEO;
            case Place.TYPE_POSTAL_CODE_PREFIX /*1016*/:
                return MRAID1;
            case Place.TYPE_POSTAL_TOWN /*1017*/:
                return MRAID2;
            case Place.TYPE_TRANSIT_STATION /*1030*/:
                return VIDEO_INTERSTITIAL;
            case AdProperties.REQUIRES_TRANSPARENCY /*1031*/:
                return REQUIRES_TRANSPARENCY;
            default:
                return null;
        }
    }

    static AAXCreative getTopCreative(Set<AAXCreative> creatives) {
        if (creatives.contains(MRAID2)) {
            return MRAID2;
        }
        if (creatives.contains(MRAID1)) {
            return MRAID1;
        }
        if (creatives.contains(HTML)) {
            return HTML;
        }
        return null;
    }
}
