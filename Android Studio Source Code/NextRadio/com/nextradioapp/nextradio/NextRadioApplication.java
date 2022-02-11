package com.nextradioapp.nextradio;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.AppViewBuilder;
import com.google.android.gms.analytics.Tracker;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.core.objects.StationInfo;
import com.nextradioapp.nextradio.ottos.NRImpression;
import com.nextradioapp.nextradio.ottos.NRInitCompleted;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.nextradioapp.nextradio.ottos.NRRadioAvailabilityEvent;
import com.nextradioapp.nextradio.ottos.NRRadioResult;
import com.nextradioapp.utils.HeadsetHelper;
import com.nextradioapp.utils.PermissionUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.C1271L;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import io.fabric.sdk.android.Fabric;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class NextRadioApplication extends MultiDexApplication implements ActivityLifecycleCallbacks, IActivityManager {
    public static final String TAG = "NextRadioApplication";
    public static DisplayImageOptions imageOptions;
    public static boolean isTablet;
    public static Activity mCurrentActivity;
    private static NextRadioApplication nextRadioApplication;
    private NextRadioEventInfo eventInfo;
    private Tracker mGATracker;
    private NRRadioResult mLastRadioResult;
    NextRadioSDKWrapperProvider mNextRadioSDKWrapperProvider;
    private StationInfo stationInfo;

    /* renamed from: com.nextradioapp.nextradio.NextRadioApplication.1 */
    class C11561 extends NRRadioResult {
        C11561() {
            this.action = 2;
        }
    }

    static {
        isTablet = false;
    }

    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Fabric.with(this, new Crashlytics());
        Crashlytics.log("NextRadioApplication onCreate");
        Crashlytics.setString("Build.FINGERPRINT", Build.FINGERPRINT);
        nextRadioApplication = this;
        registerActivityLifecycleCallbacks(this);
        PermissionUtil.saveUpgradingState(this, true);
        isTablet = NextRadioSDKWrapperProvider.getInstance().isTablet(this);
        if (VERSION.SDK_INT >= 23) {
            changeImageCache(false);
        } else {
            changeImageCache(true);
        }
    }

    public static NextRadioApplication getInstance() {
        return nextRadioApplication;
    }

    public void applyImageLoaderConfig() {
        ImageLoaderConfiguration config = new Builder(getApplicationContext()).defaultDisplayImageOptions(imageOptions).build();
        C1271L.disableLogging();
        ImageLoader.getInstance().init(config);
    }

    public void changeImageCache(boolean isCacheAllowed) {
        imageOptions = new DisplayImageOptions.Builder().showImageOnLoading(2130837651).cacheInMemory(true).cacheOnDisc(isCacheAllowed).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Config.RGB_565).build();
        applyImageLoaderConfig();
    }

    public static void registerWithBus(Object obj) {
        BusProvider.getInstance1().register(obj);
    }

    public static void unregisterWithBus(Object obj) {
        BusProvider.getInstance1().unregister(obj);
    }

    public static void postToBus(Object sourceObj, Object obj) {
        BusProvider.getInstance1().post(obj);
    }

    public synchronized void initSDK() {
        Log.d(TAG, "initSDK()");
        if (this.mNextRadioSDKWrapperProvider == null) {
            registerWithBus(this);
            this.mNextRadioSDKWrapperProvider = new NextRadioSDKWrapperProvider();
            this.mNextRadioSDKWrapperProvider.init(this);
            NextRadioSDKWrapperProvider nextRadioSDKWrapperProvider = this.mNextRadioSDKWrapperProvider;
            NextRadioSDKWrapperProvider.register(this);
        }
    }

    @Produce
    public NRRadioResult produceRadioResult() {
        if (this.mLastRadioResult == null) {
            this.mLastRadioResult = new C11561();
        }
        return this.mLastRadioResult;
    }

    @Subscribe
    public void onRadioResult(NRRadioResult result) {
        this.mLastRadioResult = result;
    }

    @Subscribe
    public void onInitCompleted(NRInitCompleted action) {
        if (action.statusCode == NRInitCompleted.STATUS_CODE_SUCCESS) {
            NextRadioSDKWrapperProvider.getInstance().requestStations(false);
        }
    }

    @Subscribe
    public void impressionVisual(NRImpression impression) {
        NextRadioSDKWrapperProvider.getInstance().recordVisualImpression(impression.trackingID, impression.trackingContext, impression.stationID, impression.cardTrackingID, impression.teID);
    }

    @Subscribe
    public void radioAction(NRRadioAction action) {
        switch (action.action) {
            case Zone.PRIMARY /*1*/:
                if (isRadioAvailable(action)) {
                    startService(IntentBuilder.turnOn(this));
                }
            case Zone.SECONDARY /*2*/:
                startService(IntentBuilder.turnOff(this, action.isQuitting));
            case Protocol.GGP /*3*/:
                if (isRadioAvailable(action)) {
                    startService(IntentBuilder.tune(this, action.direction));
                }
            case Type.MF /*4*/:
                if (isRadioAvailable(action)) {
                    startService(IntentBuilder.seek(this, action.direction, action.fromWidget));
                }
            case Service.RJE /*5*/:
                if (isRadioAvailable(action)) {
                    startService(IntentBuilder.setFreq(this, action.frequencyHz));
                }
            case Protocol.EGP /*8*/:
                if (PermissionUtil.getRadioState(this) != 2) {
                    startService(IntentBuilder.toggleSpeakerOutput(this, action.toggleSpeakerOutput));
                }
            default:
        }
    }

    private boolean isRadioAvailable(NRRadioAction action) {
        NRRadioAvailabilityEvent nrRadioAvailabilityEvent;
        if (!HeadsetHelper.isHeadphonesPluggedIn(this)) {
            nrRadioAvailabilityEvent = new NRRadioAvailabilityEvent();
            nrRadioAvailabilityEvent.status = 0;
            BusProvider.getInstance1().post(nrRadioAvailabilityEvent);
            action.shouldResumeNowPlaying = false;
            return false;
        } else if (!HeadsetHelper.isAirplaneModeOn(this)) {
            return true;
        } else {
            nrRadioAvailabilityEvent = new NRRadioAvailabilityEvent();
            nrRadioAvailabilityEvent.status = 3;
            BusProvider.getInstance1().post(nrRadioAvailabilityEvent);
            action.shouldResumeNowPlaying = false;
            return false;
        }
    }

    public void trackScreen(String screenName) {
        if (this.mGATracker == null) {
            this.mGATracker = GoogleAnalytics.getInstance(this).newTracker(2131034112);
            this.mGATracker.enableAdvertisingIdCollection(true);
        }
        this.mGATracker.setScreenName(screenName);
        this.mGATracker.send(new AppViewBuilder().build());
        GoogleAnalytics.getInstance(this).getLogger().setLogLevel(0);
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }

    public Application getCurrentApplication() {
        return this;
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mCurrentActivity = activity;
    }

    public void onActivityStarted(Activity activity) {
        mCurrentActivity = activity;
    }

    public void onActivityResumed(Activity activity) {
        mCurrentActivity = activity;
    }

    public void onActivityPaused(Activity activity) {
        if (activity == mCurrentActivity) {
            mCurrentActivity = null;
        }
    }

    public void onActivityStopped(Activity activity) {
        if (activity == mCurrentActivity) {
            mCurrentActivity = null;
        }
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
        if (activity == mCurrentActivity) {
            mCurrentActivity = null;
        }
    }

    public static boolean isCurrentOSAndroidM() {
        return VERSION.SDK_INT >= 23;
    }

    public NextRadioEventInfo getEventInfo() {
        return this.eventInfo;
    }

    public void setEventInfo(NextRadioEventInfo eventInfo) {
        this.eventInfo = eventInfo;
    }

    public StationInfo getStationInfo() {
        return this.stationInfo;
    }

    public void setStationInfo(StationInfo stationInfo1) {
        this.stationInfo = stationInfo1;
    }
}
