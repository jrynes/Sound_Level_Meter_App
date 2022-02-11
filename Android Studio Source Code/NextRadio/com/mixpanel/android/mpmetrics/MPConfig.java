package com.mixpanel.android.mpmetrics;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import java.security.GeneralSecurityException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;

public class MPConfig {
    public static boolean DEBUG = false;
    private static final String LOGTAG = "MixpanelAPI.Conf";
    static final int MAX_NOTIFICATION_CACHE_COUNT = 2;
    static final String REFERRER_PREFS_NAME = "com.mixpanel.android.mpmetrics.ReferralInfo";
    public static final int UI_FEATURES_MIN_API = 16;
    public static final String VERSION = "4.8.0";
    private static MPConfig sInstance;
    private static final Object sInstanceLock;
    private final boolean mAutoShowMixpanelUpdates;
    private final int mBulkUploadLimit;
    private final int mDataExpiration;
    private final String mDecideEndpoint;
    private final String mDecideFallbackEndpoint;
    private final boolean mDisableAppOpenEvent;
    private final boolean mDisableDecideChecker;
    private final boolean mDisableEmulatorBindingUI;
    private final boolean mDisableFallback;
    private final boolean mDisableGestureBindingUI;
    private final boolean mDisableViewCrawler;
    private final String mEditorUrl;
    private final String mEventsEndpoint;
    private final String mEventsFallbackEndpoint;
    private final int mFlushInterval;
    private final int mMinimumDatabaseLimit;
    private final String mPeopleEndpoint;
    private final String mPeopleFallbackEndpoint;
    private final String mResourcePackageName;
    private SSLSocketFactory mSSLSocketFactory;
    private final boolean mTestMode;

    static {
        DEBUG = false;
        sInstanceLock = new Object();
    }

    public static MPConfig getInstance(Context context) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = readConfig(context.getApplicationContext());
            }
        }
        return sInstance;
    }

    public synchronized void setSSLSocketFactory(SSLSocketFactory factory) {
        this.mSSLSocketFactory = factory;
    }

    MPConfig(Bundle metaData, Context context) {
        SSLSocketFactory foundSSLFactory;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            foundSSLFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            Log.i(LOGTAG, "System has no SSL support. Built-in events editor will not be available", e);
            foundSSLFactory = null;
        }
        this.mSSLSocketFactory = foundSSLFactory;
        DEBUG = metaData.getBoolean("com.mixpanel.android.MPConfig.EnableDebugLogging", false);
        if (metaData.containsKey("com.mixpanel.android.MPConfig.AutoCheckForSurveys")) {
            Log.w(LOGTAG, "com.mixpanel.android.MPConfig.AutoCheckForSurveys has been deprecated in favor of com.mixpanel.android.MPConfig.AutoShowMixpanelUpdates. Please update this key as soon as possible.");
        }
        if (metaData.containsKey("com.mixpanel.android.MPConfig.DebugFlushInterval")) {
            Log.w(LOGTAG, "We do not support com.mixpanel.android.MPConfig.DebugFlushInterval anymore. There will only be one flush interval. Please, update your AndroidManifest.xml.");
        }
        this.mBulkUploadLimit = metaData.getInt("com.mixpanel.android.MPConfig.BulkUploadLimit", 40);
        this.mFlushInterval = metaData.getInt("com.mixpanel.android.MPConfig.FlushInterval", 60000);
        this.mDataExpiration = metaData.getInt("com.mixpanel.android.MPConfig.DataExpiration", 432000000);
        this.mMinimumDatabaseLimit = metaData.getInt("com.mixpanel.android.MPConfig.MinimumDatabaseLimit", 20971520);
        this.mDisableFallback = metaData.getBoolean("com.mixpanel.android.MPConfig.DisableFallback", true);
        this.mResourcePackageName = metaData.getString("com.mixpanel.android.MPConfig.ResourcePackageName");
        this.mDisableGestureBindingUI = metaData.getBoolean("com.mixpanel.android.MPConfig.DisableGestureBindingUI", false);
        this.mDisableEmulatorBindingUI = metaData.getBoolean("com.mixpanel.android.MPConfig.DisableEmulatorBindingUI", false);
        this.mDisableAppOpenEvent = metaData.getBoolean("com.mixpanel.android.MPConfig.DisableAppOpenEvent", true);
        this.mDisableViewCrawler = metaData.getBoolean("com.mixpanel.android.MPConfig.DisableViewCrawler", false);
        this.mDisableDecideChecker = metaData.getBoolean("com.mixpanel.android.MPConfig.DisableDecideChecker", false);
        boolean z = metaData.getBoolean("com.mixpanel.android.MPConfig.AutoCheckForSurveys", true) && metaData.getBoolean("com.mixpanel.android.MPConfig.AutoShowMixpanelUpdates", true);
        this.mAutoShowMixpanelUpdates = z;
        this.mTestMode = metaData.getBoolean("com.mixpanel.android.MPConfig.TestMode", false);
        String eventsEndpoint = metaData.getString("com.mixpanel.android.MPConfig.EventsEndpoint");
        if (eventsEndpoint == null) {
            eventsEndpoint = "https://api.mixpanel.com/track?ip=1";
        }
        this.mEventsEndpoint = eventsEndpoint;
        String eventsFallbackEndpoint = metaData.getString("com.mixpanel.android.MPConfig.EventsFallbackEndpoint");
        if (eventsFallbackEndpoint == null) {
            eventsFallbackEndpoint = "http://api.mixpanel.com/track?ip=1";
        }
        this.mEventsFallbackEndpoint = eventsFallbackEndpoint;
        String peopleEndpoint = metaData.getString("com.mixpanel.android.MPConfig.PeopleEndpoint");
        if (peopleEndpoint == null) {
            peopleEndpoint = "https://api.mixpanel.com/engage";
        }
        this.mPeopleEndpoint = peopleEndpoint;
        String peopleFallbackEndpoint = metaData.getString("com.mixpanel.android.MPConfig.PeopleFallbackEndpoint");
        if (peopleFallbackEndpoint == null) {
            peopleFallbackEndpoint = "http://api.mixpanel.com/engage";
        }
        this.mPeopleFallbackEndpoint = peopleFallbackEndpoint;
        String decideEndpoint = metaData.getString("com.mixpanel.android.MPConfig.DecideEndpoint");
        if (decideEndpoint == null) {
            decideEndpoint = "https://decide.mixpanel.com/decide";
        }
        this.mDecideEndpoint = decideEndpoint;
        String decideFallbackEndpoint = metaData.getString("com.mixpanel.android.MPConfig.DecideFallbackEndpoint");
        if (decideFallbackEndpoint == null) {
            decideFallbackEndpoint = "http://decide.mixpanel.com/decide";
        }
        this.mDecideFallbackEndpoint = decideFallbackEndpoint;
        String editorUrl = metaData.getString("com.mixpanel.android.MPConfig.EditorUrl");
        if (editorUrl == null) {
            editorUrl = "wss://switchboard.mixpanel.com/connect/";
        }
        this.mEditorUrl = editorUrl;
        if (DEBUG) {
            Log.v(LOGTAG, "Mixpanel configured with:\n    AutoShowMixpanelUpdates " + getAutoShowMixpanelUpdates() + Stomp.NEWLINE + "    BulkUploadLimit " + getBulkUploadLimit() + Stomp.NEWLINE + "    FlushInterval " + getFlushInterval() + Stomp.NEWLINE + "    DataExpiration " + getDataExpiration() + Stomp.NEWLINE + "    MinimumDatabaseLimit " + getMinimumDatabaseLimit() + Stomp.NEWLINE + "    DisableFallback " + getDisableFallback() + Stomp.NEWLINE + "    DisableAppOpenEvent " + getDisableAppOpenEvent() + Stomp.NEWLINE + "    DisableViewCrawler " + getDisableViewCrawler() + Stomp.NEWLINE + "    DisableDeviceUIBinding " + getDisableGestureBindingUI() + Stomp.NEWLINE + "    DisableEmulatorUIBinding " + getDisableEmulatorBindingUI() + Stomp.NEWLINE + "    EnableDebugLogging " + DEBUG + Stomp.NEWLINE + "    TestMode " + getTestMode() + Stomp.NEWLINE + "    EventsEndpoint " + getEventsEndpoint() + Stomp.NEWLINE + "    PeopleEndpoint " + getPeopleEndpoint() + Stomp.NEWLINE + "    DecideEndpoint " + getDecideEndpoint() + Stomp.NEWLINE + "    EventsFallbackEndpoint " + getEventsFallbackEndpoint() + Stomp.NEWLINE + "    PeopleFallbackEndpoint " + getPeopleFallbackEndpoint() + Stomp.NEWLINE + "    DecideFallbackEndpoint " + getDecideFallbackEndpoint() + Stomp.NEWLINE + "    EditorUrl " + getEditorUrl() + Stomp.NEWLINE + "    DisableDecideChecker " + getDisableDecideChecker() + Stomp.NEWLINE);
        }
    }

    public int getBulkUploadLimit() {
        return this.mBulkUploadLimit;
    }

    public int getFlushInterval() {
        return this.mFlushInterval;
    }

    public int getDataExpiration() {
        return this.mDataExpiration;
    }

    public int getMinimumDatabaseLimit() {
        return this.mMinimumDatabaseLimit;
    }

    public boolean getDisableFallback() {
        return this.mDisableFallback;
    }

    public boolean getDisableGestureBindingUI() {
        return this.mDisableGestureBindingUI;
    }

    public boolean getDisableEmulatorBindingUI() {
        return this.mDisableEmulatorBindingUI;
    }

    public boolean getDisableAppOpenEvent() {
        return this.mDisableAppOpenEvent;
    }

    public boolean getDisableViewCrawler() {
        return this.mDisableViewCrawler;
    }

    public boolean getTestMode() {
        return this.mTestMode;
    }

    public String getEventsEndpoint() {
        return this.mEventsEndpoint;
    }

    public String getPeopleEndpoint() {
        return this.mPeopleEndpoint;
    }

    public String getDecideEndpoint() {
        return this.mDecideEndpoint;
    }

    public String getEventsFallbackEndpoint() {
        return this.mEventsFallbackEndpoint;
    }

    public String getPeopleFallbackEndpoint() {
        return this.mPeopleFallbackEndpoint;
    }

    public String getDecideFallbackEndpoint() {
        return this.mDecideFallbackEndpoint;
    }

    public boolean getAutoShowMixpanelUpdates() {
        return this.mAutoShowMixpanelUpdates;
    }

    public String getEditorUrl() {
        return this.mEditorUrl;
    }

    public boolean getDisableDecideChecker() {
        return this.mDisableDecideChecker;
    }

    public String getResourcePackageName() {
        return this.mResourcePackageName;
    }

    public synchronized SSLSocketFactory getSSLSocketFactory() {
        return this.mSSLSocketFactory;
    }

    static MPConfig readConfig(Context appContext) {
        String packageName = appContext.getPackageName();
        try {
            Bundle configBundle = appContext.getPackageManager().getApplicationInfo(packageName, Flags.FLAG8).metaData;
            if (configBundle == null) {
                configBundle = new Bundle();
            }
            return new MPConfig(configBundle, appContext);
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Can't configure Mixpanel with package name " + packageName, e);
        }
    }
}
