package com.nextradioapp.nextradio.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings.System;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.facebook.ads.AdError;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.activities.StationsActivity_;
import com.nextradioapp.nextradio.activities.TabletFragContainerActivity_;
import com.nextradioapp.radioadapters.AdapterListing;
import com.nextradioapp.radioadapters.IFmBind;
import com.nextradioapp.radioadapters.IFmLocalTune;
import com.nextradioapp.radioadapters.IFmRadio;
import com.nextradioapp.radioadapters.IRadioEventListener;
import com.nextradioapp.utils.ToasterStrudle;
import com.rabbitmq.client.impl.AMQConnection;
import io.fabric.sdk.android.Fabric;
import org.androidannotations.annotations.EService;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.WKSRecord;
import org.xbill.DNS.Zone;

@EService
public class RadioAdapterService extends Service implements IRadioEventListener, IFmBind {
    public static final String ACTION_PLAY = "com.nextradioapp.nextradio.action.PLAY";
    public static final String ACTION_PLAYPAUSE = "com.nextradioapp.nextradio.action.PLAYPAUSE";
    public static final String ACTION_SEEKDOWN = "com.nextradioapp.nextradio.action.SEEKDOWN";
    public static final String ACTION_SEEKUP = "com.nextradioapp.nextradio.action.SEEKUP";
    public static final String ACTION_SET_FREQ = "com.nextradioapp.nextradio.action.SET_FREQ";
    public static final String ACTION_STOP = "com.nextradioapp.nextradio.action.STOP";
    public static final String ACTION_TOGGLE_SPEAKER_OUTPUT = "com.nextradioapp.nextradio.action.TOGGLE_SPEAKER_OUTPUT";
    public static final String ACTION_TUNE = "com.nextradioapp.nextradio.action.TUNE";
    public static final String CMDNOTIF = "CMDNOTIF";
    public static final String PREFS = "remote_preferences";
    public static final String TAG = "RadioAdapterService";
    public static boolean isBuyThisSongSelected;
    public static boolean isPermissionRevoked;
    private static Handler mHandler;
    private static Thread mHeartbeatOutThread;
    public static IFmRadio mRadioAdapter;
    private static Thread sCommandThread;
    private int MODE_ASLEEP;
    private int MODE_AWAKE;
    private int MODE_TRANSITIONING;
    private int MODE_UNKNOWN;
    private Intent commandIntent;
    private HeadphoneAirplaneMonitor headphoneAirplaneMonitor;
    boolean isNotificationPreferenceOn;
    private int mExpectedFreq;
    private boolean mForceNewNotification;
    private boolean mIsPausing;
    private boolean mIsQuitting;
    private boolean mIsSeeking;
    private int mLastSeekDirection;
    private int mLastSentFreq;
    private int mMode;
    private NotificationManager mNotificationManager;
    private PowerManager mPowerManager;
    private int mPowerOffReasonCode;
    private long mShutdownTimeMillis;
    private int mStepValue;
    private boolean mWillUnmuteWhenTurnedOn;

    /* renamed from: com.nextradioapp.nextradio.services.RadioAdapterService.1 */
    class C12241 implements Runnable {
        C12241() {
        }

        public void run() {
            ToasterStrudle.displayToast(RadioAdapterService.this, 1, "There was an issue with the FM radio hardware. Please try again.");
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.RadioAdapterService.2 */
    class C12252 implements Runnable {
        C12252() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(2000);
                    if (!Thread.currentThread().isInterrupted() && RadioAdapterService.this.mMode != RadioAdapterService.this.MODE_ASLEEP) {
                        if (RadioAdapterService.this.mPowerManager != null && RadioAdapterService.this.mPowerManager.isScreenOn()) {
                            RadioAdapterService.this.notifyOfStatus();
                        }
                    } else {
                        return;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.RadioAdapterService.3 */
    class C12263 implements Runnable {
        final /* synthetic */ String val$action;
        final /* synthetic */ int val$arg1;
        final /* synthetic */ boolean val$throwErrorIfFailed;
        final /* synthetic */ int val$timeout;

        C12263(String str, int i, int i2, boolean z) {
            this.val$action = str;
            this.val$arg1 = i;
            this.val$timeout = i2;
            this.val$throwErrorIfFailed = z;
        }

        public void run() {
            try {
                RadioAdapterService.this.mIsSeeking = false;
                if (RadioAdapterService.mRadioAdapter != null) {
                    if (this.val$action.equals(RadioAdapterService.ACTION_STOP)) {
                        RadioAdapterService.this.stopHeartBeat();
                        RadioAdapterService.this.mWillUnmuteWhenTurnedOn = true;
                        RadioAdapterService.this.mMode = RadioAdapterService.this.MODE_TRANSITIONING;
                        RadioAdapterService.this.notifyOfStatus();
                        Crashlytics.log("powerOffAsync()");
                        RadioAdapterService.mRadioAdapter.powerOffAsync();
                    } else if (this.val$action.equals(RadioAdapterService.ACTION_PLAY)) {
                        RadioAdapterService.this.mWillUnmuteWhenTurnedOn = true;
                        RadioAdapterService.this.mMode = RadioAdapterService.this.MODE_TRANSITIONING;
                        Crashlytics.log("powerOnAsync(" + this.val$arg1 + ")");
                        RadioAdapterService.this.notifyOfStatus();
                        RadioAdapterService.this.mExpectedFreq = this.val$arg1;
                        RadioAdapterService.mRadioAdapter.powerOnAsync(this.val$arg1, 0);
                    } else if (this.val$action.equals(RadioAdapterService.ACTION_SEEKDOWN)) {
                        RadioAdapterService.this.mIsSeeking = true;
                        RadioAdapterService.this.internal_seek(-1);
                    } else if (this.val$action.equals(RadioAdapterService.ACTION_SEEKUP)) {
                        RadioAdapterService.this.mIsSeeking = true;
                        RadioAdapterService.this.internal_seek(1);
                    }
                    try {
                        Thread.sleep((long) this.val$timeout);
                        if (RadioAdapterService.this.mMode == RadioAdapterService.this.MODE_TRANSITIONING) {
                            Crashlytics.log(3, RadioAdapterService.TAG, "runCommandAsync aborted:" + this.val$action + Stomp.COMMA + this.val$arg1);
                            if (this.val$throwErrorIfFailed) {
                                try {
                                    throw new Exception("FM command aborted:" + Build.DEVICE);
                                } catch (Exception ex) {
                                    RadioAdapterService.this.handleRadioException(ex);
                                }
                            }
                            RadioAdapterService.this.shutDownCompletely();
                        }
                    } catch (InterruptedException e) {
                        Crashlytics.log(3, RadioAdapterService.TAG, "runCommandAsync:command finished or canceled");
                    }
                }
            } catch (Exception ex2) {
                RadioAdapterService.this.handleRadioException(ex2);
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.RadioAdapterService.4 */
    class C12274 implements Runnable {
        C12274() {
        }

        public void run() {
            RadioAdapterService.this.delayedUnmute(100);
            RadioAdapterService.this.delayedUnmute(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH);
            RadioAdapterService.this.delayedUnmute(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
            RadioAdapterService.this.delayedUnmute(AdError.SERVER_ERROR_CODE);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.RadioAdapterService.5 */
    class C12285 implements Runnable {
        C12285() {
        }

        public void run() {
            if (RadioAdapterService.this.isNotificationPreferenceOn) {
                ToasterStrudle.makeText(RadioAdapterService.this, RadioAdapterService.this.getResources().getString(2131165292), 0);
                RadioAdapterService.this.displayNotification(RadioAdapterService.this.getResources().getString(2131165250), RadioAdapterService.this.getResources().getString(2131165292));
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.RadioAdapterService.6 */
    class C12296 implements Runnable {
        C12296() {
        }

        public void run() {
            if (RadioAdapterService.this.isNotificationPreferenceOn) {
                ToasterStrudle.makeText(RadioAdapterService.this, RadioAdapterService.this.getResources().getString(2131165248), 0);
                RadioAdapterService.this.displayNotification(RadioAdapterService.this.getResources().getString(2131165250), RadioAdapterService.this.getResources().getString(2131165248));
            }
        }
    }

    private class HeadphoneAirplaneMonitor extends BroadcastReceiver {
        private String TAG;
        protected boolean mIsAirplaneMode;
        protected boolean mIsHeadsetIn;
        RadioAdapterService myParent;
        final /* synthetic */ RadioAdapterService this$0;

        public HeadphoneAirplaneMonitor(RadioAdapterService radioAdapterService, RadioAdapterService service) {
            boolean z = false;
            this.this$0 = radioAdapterService;
            this.TAG = "HeadphoneMonitor";
            this.myParent = service;
            this.mIsHeadsetIn = ((AudioManager) this.myParent.getSystemService("audio")).isWiredHeadsetOn();
            if (System.getInt(this.myParent.getContentResolver(), "airplane_mode_on", 0) != 0) {
                z = true;
            }
            this.mIsAirplaneMode = z;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                switch (intent.getIntExtra("state", -1)) {
                    case Tokenizer.EOF /*0*/:
                        this.mIsHeadsetIn = false;
                    case Zone.PRIMARY /*1*/:
                        this.this$0.removeNotification();
                        this.mIsHeadsetIn = true;
                    default:
                }
            } else if (intent.getAction().equals("android.intent.action.AIRPLANE_MODE")) {
                this.mIsAirplaneMode = intent.getBooleanExtra("state", false);
                if (this.mIsAirplaneMode) {
                    this.myParent.stopRadioIfRunning();
                }
            }
        }
    }

    static {
        isBuyThisSongSelected = false;
        isPermissionRevoked = true;
    }

    public void onCreate() {
        mHandler = new Handler();
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Crashlytics.log(3, TAG, "RadioAdapterService onCreate");
        Crashlytics.setString("Build.FINGERPRINT", Build.FINGERPRINT);
        this.headphoneAirplaneMonitor = new HeadphoneAirplaneMonitor(this, this);
        IntentFilter receiverFilter = new IntentFilter();
        receiverFilter.addAction("android.intent.action.HEADSET_PLUG");
        receiverFilter.addAction("android.intent.action.AIRPLANE_MODE");
        registerReceiver(this.headphoneAirplaneMonitor, receiverFilter);
        this.mStepValue = NextRadioSDKWrapperProvider.getInstance().getTuneStepValue();
    }

    public RadioAdapterService() {
        this.MODE_UNKNOWN = 0;
        this.MODE_ASLEEP = 1;
        this.MODE_AWAKE = 2;
        this.MODE_TRANSITIONING = 3;
        this.mMode = this.MODE_ASLEEP;
    }

    public void onDestroy() {
        super.onDestroy();
        Crashlytics.log(3, TAG, "onDestroy");
        unregisterReceiver(this.headphoneAirplaneMonitor);
        stopHeartBeat();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void internal_seek(int direction) {
        Crashlytics.log(3, TAG, "internal_seek(" + direction + ")");
        this.mLastSeekDirection = direction;
        try {
            mRadioAdapter.seekAsync(direction);
        } catch (Exception ex) {
            handleRadioException(ex);
        }
    }

    private void internal_tune(int direction) {
        Crashlytics.log(3, TAG, "internal_tune(" + direction + ")");
        this.mLastSeekDirection = direction;
        try {
            mRadioAdapter.tuneAsync(direction);
        } catch (Exception ex) {
            handleRadioException(ex);
        }
    }

    private void handleRadioException(Exception ex) {
        Crashlytics.logException(ex);
        try {
            mRadioAdapter.powerOffAsync();
        } catch (Exception e) {
        }
        Log.e(TAG, "RadioException");
        Log.e(TAG, ex.getLocalizedMessage());
        shutDownCompletely();
        mHandler.post(new C12241());
    }

    public void onBindComplete() {
        handleStartCommand(this.commandIntent, 0, 0);
    }

    private int handleStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        this.commandIntent = intent;
        this.mIsQuitting = false;
        if (this.mMode == this.MODE_TRANSITIONING) {
            Crashlytics.log(5, TAG, "skipping command:" + action + " mode:" + this.mMode);
            return 1;
        }
        if (mRadioAdapter == null && !(this.mMode == this.MODE_ASLEEP && action.equals(ACTION_STOP))) {
            mRadioAdapter = AdapterListing.getFMRadioImplementation(this);
            ((IFmLocalTune) mRadioAdapter).withTuneStep(NextRadioSDKWrapperProvider.getInstance().getTuneStepValue());
            Crashlytics.log(3, TAG, "getFMRadioImplementation:" + mRadioAdapter.getClass().toString());
            mRadioAdapter.setBinder(this);
            mRadioAdapter.init(this);
            mRadioAdapter.setRadioCallback(this);
        }
        if (action != null) {
            Crashlytics.log(3, TAG, "onStartCommand:" + action + " mode:" + this.mMode);
            if (action.equals(ACTION_PLAYPAUSE)) {
                if (mRadioAdapter.getIsPoweredOn()) {
                    if (intent.getBooleanExtra("isPause", false)) {
                        this.mIsPausing = true;
                    }
                    runCommandAsync(ACTION_STOP, 0, CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS, false);
                } else if (!headsetCheck()) {
                    return 2;
                } else {
                    runCommandAsync(ACTION_PLAY, getSharedPreferences(PREFS, 4).getInt("lastTunedFrequency", 87500000));
                }
                return 1;
            } else if (!action.equals(ACTION_PLAY)) {
                if (action.equals(ACTION_SEEKUP)) {
                    if (!headsetCheck() || seekTunePower() || widgetSeek(intent, 1)) {
                        return 1;
                    }
                    if (mRadioAdapter.getIsPoweredOn()) {
                        runCommandAsync(ACTION_SEEKUP, 0);
                    } else {
                        runCommandAsync(ACTION_PLAY, mRadioAdapter.getCurrentFrequency());
                    }
                }
                if (action.equals(ACTION_SEEKDOWN)) {
                    if (!headsetCheck() || seekTunePower() || widgetSeek(intent, -1)) {
                        return 1;
                    }
                    if (mRadioAdapter.getIsPoweredOn()) {
                        runCommandAsync(ACTION_SEEKDOWN, 0);
                    } else {
                        runCommandAsync(ACTION_PLAY, mRadioAdapter.getCurrentFrequency());
                    }
                }
                if (action.equals(ACTION_TUNE)) {
                    Log.d(TAG, "ACTION_TUNE freq ");
                    if (!headsetCheck() || seekTunePower()) {
                        return 1;
                    }
                    this.mIsSeeking = true;
                    internal_tune(intent.getIntExtra("direction", 1));
                }
                if (action.equals(ACTION_STOP)) {
                    this.mIsPausing = false;
                    this.mIsQuitting = intent.getBooleanExtra("isQuitting", false);
                    if (this.mMode == this.MODE_ASLEEP) {
                        if (this.mPowerOffReasonCode == 2) {
                            this.mIsPausing = false;
                            runCommandAsync(ACTION_STOP, 0, CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS, false);
                        } else {
                            shutDownCompletely();
                        }
                    } else if (this.mMode == this.MODE_AWAKE) {
                        runCommandAsync(ACTION_STOP, 0, CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS, false);
                    } else {
                        runCommandAsync(ACTION_STOP, 0, CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS, false);
                    }
                    return 2;
                }
                if (action.equals(ACTION_SET_FREQ)) {
                    int frequencyHz = intent.getIntExtra("frequencyHz", 87500000);
                    getSharedPreferences(PREFS, 4).edit().putInt("lastTunedFrequency", frequencyHz).commit();
                    Log.d(TAG, "getIsPoweredOn " + mRadioAdapter.getIsPoweredOn());
                    if (mRadioAdapter.getIsPoweredOn()) {
                        runCommandAsync(ACTION_SET_FREQ, frequencyHz);
                        this.mExpectedFreq = frequencyHz;
                        mRadioAdapter.setFrequencyAsync(frequencyHz);
                    } else {
                        runCommandAsync(ACTION_PLAY, frequencyHz);
                    }
                }
                if (action.equals(ACTION_TOGGLE_SPEAKER_OUTPUT)) {
                    mRadioAdapter.toggleSpeaker(intent.getBooleanExtra("speakerToggle", false));
                }
            } else if (!headsetCheck()) {
                return 2;
            } else {
                int freq = 87500000;
                try {
                    freq = getSharedPreferences(PREFS, 4).getInt("lastTunedFrequency", 87500000);
                } catch (Exception e) {
                }
                Log.d(TAG, "ACTION_PLAY freq " + freq);
                runCommandAsync(ACTION_PLAY, freq);
                return 1;
            }
        }
        return 1;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            if (mRadioAdapter == null && isPermissionRevoked) {
                isPermissionRevoked = false;
                reset();
            }
            Crashlytics.log(6, TAG, "onStartCommand intent == null");
            shutDownCompletely();
            return 2;
        }
        try {
            return handleStartCommand(intent, flags, startId);
        } catch (Exception ex) {
            handleRadioException(ex);
            return 1;
        }
    }

    private void reset() {
        mRadioAdapter = AdapterListing.getFMRadioImplementation(this);
        mRadioAdapter.init(this);
        mRadioAdapter.setRadioCallback(this);
        if (mRadioAdapter != null && mRadioAdapter.getIsPoweredOn()) {
            mRadioAdapter.powerOffAsync();
            mRadioAdapter = null;
        }
    }

    private boolean widgetSeek(Intent intent, int direction) {
        if (intent == null || !intent.getBooleanExtra("widgetSeek", false) || !getSharedPreferences(PREFS, 4).getBoolean("useFavoritesForSeek", false)) {
            return false;
        }
        String csvFavoriteStations = getSharedPreferences(PREFS, 4).getString("csvFavoriteStations", null);
        if (csvFavoriteStations == null || csvFavoriteStations.length() == 0) {
            return false;
        }
        String[] splitString = csvFavoriteStations.split(Stomp.COMMA);
        if (splitString.length == 0) {
            return false;
        }
        int i;
        int freq;
        if (direction > 0) {
            for (String parseInt : splitString) {
                freq = Integer.parseInt(parseInt);
                if (freq > this.mLastSentFreq) {
                    tuneTo(freq, 0, true);
                    return true;
                }
            }
            tuneTo(Integer.parseInt(splitString[0]), 0, true);
            return true;
        } else if (direction >= 0) {
            return false;
        } else {
            for (i = splitString.length - 1; i >= 0; i--) {
                freq = Integer.parseInt(splitString[i]);
                if (freq < this.mLastSentFreq) {
                    tuneTo(freq, 0, true);
                    return true;
                }
            }
            tuneTo(Integer.parseInt(splitString[splitString.length - 1]), 0, true);
            return true;
        }
    }

    boolean seekTunePower() {
        if (this.mMode != this.MODE_ASLEEP) {
            return false;
        }
        runCommandAsync(ACTION_PLAY, getSharedPreferences(PREFS, 4).getInt("lastTunedFrequency", 87500000));
        return true;
    }

    void initializeStartup() {
        Intent intent;
        this.mPowerManager = (PowerManager) getSystemService("power");
        this.mShutdownTimeMillis = System.currentTimeMillis() + 3000;
        if (NextRadioApplication.isTablet) {
            intent = new Intent(this, TabletFragContainerActivity_.class);
        } else {
            intent = new Intent(this, StationsActivity_.class);
        }
        Notification notification = new Builder(this).setSmallIcon(2130837744).setContentTitle("NextRadio started...").setContentIntent(PendingIntent.getActivity(this, 0, intent, 0)).setPriority(2).build();
        this.mForceNewNotification = true;
        startForeground(1, notification);
        stopHeartBeat();
        mHeartbeatOutThread = new Thread(new C12252());
        mHeartbeatOutThread.setName("RAHeartbeat");
        mHeartbeatOutThread.start();
    }

    private void stopHeartBeat() {
        if (mHeartbeatOutThread != null) {
            mHeartbeatOutThread.interrupt();
        }
    }

    protected void stopRadioIfRunning() {
        if (this.mMode == this.MODE_AWAKE || this.mMode == this.MODE_TRANSITIONING) {
            this.mIsPausing = false;
            runCommandAsync(ACTION_STOP, 0);
        }
    }

    private void runCommandAsync(String action, int arg1) {
        runCommandAsync(action, arg1, AMQConnection.HANDSHAKE_TIMEOUT, true);
    }

    private void runCommandAsync(String action, int arg1, int timeout, boolean throwErrorIfFailed) {
        if (mRadioAdapter != null && this.mMode != this.MODE_TRANSITIONING) {
            synchronized (mRadioAdapter) {
                Crashlytics.log(3, TAG, "runCommandAsync:" + action + Stomp.COMMA + arg1);
                if (sCommandThread != null) {
                    sCommandThread.interrupt();
                }
                sCommandThread = new Thread(new C12263(action, arg1, timeout, throwErrorIfFailed));
                sCommandThread.setName("NextRadio-RadioCommand");
                sCommandThread.start();
            }
        }
    }

    private void notifyOfStatus() {
        String action;
        Intent i = new Intent(this, UIService_.class);
        if (this.mMode == this.MODE_ASLEEP) {
            action = UIService.ACTION_POWEREDOFF;
        } else if (this.mMode == this.MODE_TRANSITIONING) {
            action = UIService.ACTION_TRANSITIONING;
        } else {
            action = UIService.ACTION_NEWFREQ;
        }
        if (this.mIsPausing) {
            i.putExtra("reasonCode", 2);
        } else {
            i.putExtra("reasonCode", this.mPowerOffReasonCode);
        }
        if (mRadioAdapter != null && isBuyThisSongSelected) {
            if (mRadioAdapter.getIsPoweredOn()) {
                this.mIsQuitting = false;
            } else {
                isBuyThisSongSelected = false;
                this.mIsQuitting = true;
                action = UIService.ACTION_POWEREDOFF;
            }
        }
        i.setAction(action);
        i.putExtra("frequencyHz", this.mLastSentFreq);
        i.putExtra("forceRefresh", this.mForceNewNotification);
        i.putExtra("isQuitting", this.mIsQuitting);
        startService(i);
        this.mForceNewNotification = false;
        if ((this.mMode == this.MODE_ASLEEP || action == UIService.ACTION_POWEREDOFF) && mHeartbeatOutThread != null) {
            mHeartbeatOutThread.interrupt();
        }
    }

    public void onRadioPoweredOff(int arg0) {
        Crashlytics.log(3, TAG, "onRadioPoweredOff:" + arg0);
        if (mRadioAdapter == null) {
            Crashlytics.log(6, TAG, "onRadioPoweredOff called will null mRadioAdapter");
            return;
        }
        if (sCommandThread != null) {
            sCommandThread.interrupt();
        }
        this.mPowerOffReasonCode = arg0;
        this.mLastSentFreq = 0;
        this.mMode = this.MODE_ASLEEP;
        if (this.mIsPausing || arg0 != 1) {
            stopHeartBeat();
            notifyOfStatus();
            Log.e(TAG, "mHeartbeatOutThread.interrupt()");
            return;
        }
        shutDownCompletely();
    }

    private void shutDownCompletely() {
        try {
            stopForeground(true);
            Crashlytics.log(3, TAG, "shutDownCompletely");
            this.mPowerOffReasonCode = 1;
            if (mRadioAdapter != null) {
                mRadioAdapter.serviceShuttingDown();
            }
            mRadioAdapter = null;
            this.mMode = this.MODE_ASLEEP;
        } catch (Exception e) {
        }
        notifyOfStatus();
        stopHeartBeat();
        stopSelf();
    }

    public void onRadioPoweredOn() {
        if (mRadioAdapter == null) {
            Crashlytics.log(6, TAG, "onRadioPoweredOn: called will null mRadioAdapter");
            return;
        }
        if (sCommandThread != null) {
            sCommandThread.interrupt();
        }
        try {
            initializeStartup();
            SharedPreferences sp = getSharedPreferences(PREFS, 4);
            this.mMode = this.MODE_AWAKE;
            boolean routeToSpeaker = sp.getBoolean("outputToSpeaker", false);
            Log.d(TAG, "onRadioPoweredOn");
            if (this.mWillUnmuteWhenTurnedOn) {
                this.mWillUnmuteWhenTurnedOn = false;
                Crashlytics.log(3, TAG, "unmute");
                mRadioAdapter.unmute();
                new Thread(new C12274()).start();
            } else {
                Crashlytics.log(3, TAG, "SKIPPING unmute");
            }
            int freq = sp.getInt("lastTunedFrequency", 87500000);
            int freqCheck = freq / 100000;
            if (this.mStepValue == 2 && freqCheck % 2 == 0) {
                freq = (freq < 87500000 || freq >= 107900000) ? freq - 100000 : freq + 100000;
            }
            tuneTo(freq, 0, true);
            this.mLastSentFreq = freq;
            notifyOfStatus();
            Crashlytics.log(3, TAG, "toggleSpeaker(" + routeToSpeaker + ")");
            mRadioAdapter.toggleSpeaker(routeToSpeaker);
        } catch (Exception e) {
        }
    }

    private void delayedUnmute(int delay) {
        if (this.mMode == this.MODE_AWAKE) {
            try {
                Thread.sleep((long) delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.mMode == this.MODE_AWAKE) {
                Crashlytics.log(3, TAG, "delayed unmute:" + delay + "ms unmuting");
                if (mRadioAdapter != null) {
                    mRadioAdapter.unmute();
                }
            }
        }
    }

    private void tuneTo(int frequency, int program, boolean direct) {
        Crashlytics.log(3, TAG, "setFrequencyAsync(" + frequency + ")");
        getSharedPreferences(PREFS, 4).edit().putInt("lastTunedFrequency", frequency).commit();
        if (mRadioAdapter.getCurrentFrequency() == frequency || this.mLastSentFreq == frequency) {
            Log.w(TAG, "frequency did not change:" + frequency);
            return;
        }
        this.mIsSeeking = false;
        this.mExpectedFreq = frequency;
        mRadioAdapter.setFrequencyAsync(frequency);
    }

    public void onRadioFrequencyChanged(int freq, int subchannel) {
        if (mRadioAdapter == null) {
            Crashlytics.log(6, TAG, "onRadioFrequencyChanged called will null mRadioAdapter");
            return;
        }
        int freqCheck = freq / 100000;
        if (this.mStepValue == 2 && freqCheck % 2 == 0) {
            if (freq < 87500000 || freq >= 107900000) {
                freq -= 100000;
            } else {
                freq += 100000;
            }
            Log.w(TAG, "changed onRadioFrequencyChanged(" + freq + Stomp.COMMA + subchannel + ")");
        }
        if (sCommandThread != null) {
            sCommandThread.interrupt();
        }
        if (this.mIsSeeking || freq == this.mExpectedFreq) {
            this.mMode = this.MODE_AWAKE;
            getSharedPreferences(PREFS, 4).edit().putInt("lastTunedFrequency", freq).commit();
            if (this.mLastSentFreq == freq) {
                Log.w(TAG, "onRadioFrequencyChanged - frequency did not change:" + freq);
                return;
            }
            this.mLastSentFreq = freq;
            notifyOfStatus();
            return;
        }
        Crashlytics.log(5, TAG, "wrong freq:setFrequencyAsync(" + freq + Stomp.COMMA + subchannel + ")");
        mRadioAdapter.setFrequencyAsync(this.mExpectedFreq);
    }

    private boolean headsetCheck() {
        this.isNotificationPreferenceOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PreferenceStorage.NOTIFICATION_PREFERENCE, true);
        if (!this.headphoneAirplaneMonitor.mIsHeadsetIn) {
            mHandler = new Handler();
            mHandler.post(new C12285());
            return false;
        } else if (!this.headphoneAirplaneMonitor.mIsAirplaneMode) {
            return true;
        } else {
            Log.w(TAG, "Airplane mode on");
            mHandler = new Handler();
            mHandler.post(new C12296());
            return false;
        }
    }

    public void onSignalStrengthChanged(int signalStrength) {
    }

    public void onRadioRDSAlternativeFrequenciesChanged(int[] alternativeFrequencies) {
    }

    public void onRadioRDSProgramServiceChanged(String newProgramService) {
    }

    public void onRadioRDSProgramIdentificationChanged(String newProgramIdentification) {
    }

    public void onRadioRDSRadioTextChanged(String newRadioText) {
    }

    public void onBroadcastDisabled() {
    }

    public void onBroadcastEnabled() {
    }

    private void displayNotification(String title, String msg) {
        removeNotification();
        Builder mBuilder = new Builder(this).setSmallIcon(2130837744).setContentTitle(title).setAutoCancel(true).setContentText(msg);
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        this.mNotificationManager.notify(WKSRecord.Service.HOSTNAME, mBuilder.build());
    }

    private void removeNotification() {
        if (this.mNotificationManager != null) {
            this.mNotificationManager.cancel(WKSRecord.Service.HOSTNAME);
        }
    }
}
