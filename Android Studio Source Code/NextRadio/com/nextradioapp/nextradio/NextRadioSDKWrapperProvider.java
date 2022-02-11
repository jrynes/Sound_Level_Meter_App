package com.nextradioapp.nextradio;

import android.content.Context;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.core.objects.StationInfo;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NREventList;
import com.nextradioapp.nextradio.ottos.NRInitCompleted;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.nextradioapp.nextradio.ottos.NRRadioResult;
import com.nextradioapp.nextradio.ottos.NRRecentlyPlayed;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.nextradioapp.radioadapters.AdapterListing;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Map;

public class NextRadioSDKWrapperProvider {
    private static final NextRadioAndroid SDK;
    private static final String TAG = "NextRadioSDKWrapperProvider";
    private static Context mContext;
    private static ArrayList<NextRadioEventInfo> mCurrentSecondaryEvents;
    private static boolean mDataModeFreshRequest;
    private static NextRadioEventInfo mLastCurrentEvent;
    private static ArrayList<NextRadioEventInfo> mLastEventHistoryList;
    private static ArrayList<NextRadioEventInfo> mLastRecentlyPlayed;
    private static ArrayList<StationInfo> mLastStationList;
    private static boolean mRadioIsOn;
    private static int mRegisterStatus;
    private static int mStationUpdateErrorCode;
    private static boolean mStationUpdateForced;
    private static boolean mStationUpdatedFromNetwork;
    private boolean mHasInitialized;
    private boolean mIsRegisteredWithBus;

    public NextRadioSDKWrapperProvider() {
        this.mHasInitialized = false;
    }

    static {
        SDK = NextRadioAndroid.getInstance();
        mRadioIsOn = false;
        mStationUpdateErrorCode = 4;
        mLastRecentlyPlayed = new ArrayList();
        mDataModeFreshRequest = false;
        mStationUpdatedFromNetwork = false;
        mRegisterStatus = NRInitCompleted.STATUS_CODE_UNKNOWN;
    }

    public static NextRadioAndroid getInstance() {
        return SDK;
    }

    @Produce
    public static NRDataMode produceDataMode() {
        return new 1();
    }

    @Produce
    public static NRInitCompleted produceInitEvent() {
        return new 2();
    }

    @Produce
    public static NRStationList produceStationList() {
        return new 3();
    }

    @Produce
    public static NREventList produceHistoryEventList() {
        return new 4();
    }

    @Produce
    public static NRRecentlyPlayed produceRecentlyPlayed() {
        return new 5();
    }

    @Produce
    public static NRCurrentEvent produceCurrentEvent() {
        return new 6();
    }

    @Subscribe
    public void radioAction(NRRadioResult result) {
        if (result.action == 2 || result.action == 3) {
            mLastRecentlyPlayed = new ArrayList();
            if (result.action == 2) {
                mRadioIsOn = false;
                NextRadioAndroid.getInstance().stopListening();
            }
        }
        if (result.action == 1 || result.action == 3) {
            mRadioIsOn = true;
        }
    }

    public void init(Context context) {
        mContext = context;
        if (!this.mIsRegisteredWithBus) {
            this.mIsRegisteredWithBus = true;
            NextRadioApplication.registerWithBus(this);
            SDK.setErrorHandler(new 7(this));
            SDK.setEventListener(new 8(this));
        }
    }

    public static void register(NextRadioApplication context) {
        SDK.register(context, AdapterListing.getFMRadioImplementationString(mContext));
    }

    public static EventAction getEventActionFromMap(NextRadioEventInfo nInfo, Context context, String type, ActionPayload payload, Map<String, String> map) {
        if (type.toLowerCase().equals("internal_viewrecent")) {
            return new 9();
        }
        return type.toLowerCase().equals("internal_viewevent") ? new 10(map) : getInstance().getEventActionFromMap(nInfo, type, payload, map);
    }

    public static void setNoDataMode(boolean isNoDataMode, Context context) {
        NRDataMode nrDataMode = new NRDataMode();
        nrDataMode.mIsDataModeOff = isNoDataMode;
        mDataModeFreshRequest = true;
        NextRadioApplication.postToBus(context, nrDataMode);
        NRRadioAction nrRadioAction = new NRRadioAction();
        nrRadioAction.action = 2;
        NextRadioApplication.postToBus(context, nrRadioAction);
        getInstance().setNoDataMode(isNoDataMode);
    }

    public static void setDataModeReceived() {
        mDataModeFreshRequest = false;
    }
}
