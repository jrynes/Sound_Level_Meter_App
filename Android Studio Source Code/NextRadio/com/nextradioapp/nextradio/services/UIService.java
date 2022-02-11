package com.nextradioapp.nextradio.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.nextradioapp.nextradio.ottos.NRRadioResult;
import com.nextradioapp.nextradio.receivers.MusicIntentReceiver;
import com.nextradioapp.nextradio.widgetnotifications.MediaButtonHelper;
import com.nextradioapp.nextradio.widgetnotifications.NotificationHelper;
import com.nextradioapp.nextradio.widgetnotifications.NotificationHelper21;
import com.nextradioapp.nextradio.widgetnotifications.NotificationHelperBase;
import com.nextradioapp.nextradio.widgetnotifications.RemoteControlClientCompat;
import com.nextradioapp.nextradio.widgetnotifications.RemoteControlClientCompat.MetadataEditorCompat;
import com.nextradioapp.nextradio.widgetnotifications.RemoteControlHelper;
import com.nextradioapp.nextradio.widgetnotifications.Widget2Rows;
import com.nextradioapp.nextradio.widgetnotifications.Widget2RowsLarge;
import com.nextradioapp.nextradio.widgetnotifications.Widget2RowsSmall;
import com.nextradioapp.utils.ImageLoadingWrapper;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.UiThread;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.ThreadPoolUtils;

@EService
public class UIService extends Service {
    public static final String ACTION_NEWFREQ = "com.nextradioapp.nextradio.ACTION_NEWFREQ";
    public static final String ACTION_POWEREDOFF = "com.nextradioapp.nextradio.ACTION_POWEREDOFF";
    public static final String ACTION_TRANSITIONING = "com.nextradioapp.nextradio.ACTION_TRANSITIONING";
    public static final String ACTION_WIDGETUPDATE = "com.nextradioapp.nextradio.ACTION_WIDGETUPDATE";
    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_STOPPED = -1;
    public static final int STATUS_TRANSITION = 0;
    private static final String TAG = "UIService";
    private NextRadioEventInfo mCurrentEvent;
    private NRRadioResult mLastRadioResult;
    private String mLastTrackingID;
    private Thread mMediaControlsUpdateThread;
    private NotificationHelperBase mNotificationHelper;
    private ScreenReceiver mReceiver;
    private RemoteControlClientCompat mRemoteControlClientCompat;
    public boolean mScreenOn;
    private boolean mShouldClearNotificationOnPowerOff;
    private int mStatus;
    private Widget2Rows mWidget2Rows;
    private Widget2RowsSmall mWidget2RowsSmall;
    private Widget2RowsLarge mWidgetProvider2Rows;

    /* renamed from: com.nextradioapp.nextradio.services.UIService.1 */
    class C12301 extends NRRadioResult {
        C12301() {
            this.action = 2;
            this.frequencyHz = UIService.STATUS_TRANSITION;
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.UIService.2 */
    class C12312 extends NRRadioResult {
        final /* synthetic */ int val$currentFrequency;

        C12312(int i) {
            this.val$currentFrequency = i;
            this.action = 3;
            this.frequencyHz = this.val$currentFrequency;
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.UIService.3 */
    class C12323 extends NRRadioResult {
        C12323() {
            this.action = 4;
            this.frequencyHz = UIService.STATUS_STOPPED;
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.UIService.4 */
    class C12334 implements Runnable {
        final /* synthetic */ boolean val$justWidgets;
        final /* synthetic */ String val$line1;
        final /* synthetic */ String val$line2;

        C12334(boolean z, String str, String str2) {
            this.val$justWidgets = z;
            this.val$line2 = str;
            this.val$line1 = str2;
        }

        public void run() {
            Bitmap bitmap = null;
            if (UIService.this.mScreenOn) {
                Log.w(UIService.TAG, "updateNotificationAndWidgets - screen is on");
                try {
                    String str;
                    Thread.sleep(100);
                    ActionPayload payload = new ActionPayload(UIService.this.mCurrentEvent.trackingID, UIService.this.mCurrentEvent.teID, Stomp.EMPTY, UIService.this.mCurrentEvent.UFIDIdentifier, UIService.this.mCurrentEvent.stationInfo.publicStationID);
                    if (UIService.this.mCurrentEvent.trackingID == null || UIService.this.mCurrentEvent.trackingID.equals(UIService.this.mLastTrackingID)) {
                        str = null;
                    } else {
                        str = UIService.this.mCurrentEvent.trackingID;
                    }
                    bitmap = new ImageLoadingWrapper(null, str, UIService.this.mCurrentEvent.stationInfo.publicStationID, UIService.STATUS_PLAYING, 4, UIService.this.mCurrentEvent.title, payload, false).addImageURL(UIService.this.mCurrentEvent.imageURLHiRes).addImageURL(UIService.this.mCurrentEvent.imageURL).addImageURL(UIService.this.mCurrentEvent.stationInfo.imageURLHiRes).addImageURL(UIService.this.mCurrentEvent.stationInfo.imageURL).addImageURL("drawable://2130837652").loadImageSync();
                    UIService.this.mLastTrackingID = UIService.this.mCurrentEvent.trackingID;
                    if (UIService.this.mRemoteControlClientCompat != null) {
                        MetadataEditorCompat putString = UIService.this.mRemoteControlClientCompat.editMetadata(true).putString(7, UIService.this.mCurrentEvent.getMediaControlString());
                        String str2 = (UIService.this.mCurrentEvent.artist == null || UIService.this.mCurrentEvent.artist.length() == 0) ? " " : UIService.this.mCurrentEvent.artist;
                        putString = putString.putString(2, str2);
                        str2 = (UIService.this.mCurrentEvent.album == null || UIService.this.mCurrentEvent.album.length() == 0) ? " " : UIService.this.mCurrentEvent.album;
                        MetadataEditorCompat rcEditor = putString.putString(UIService.STATUS_PLAYING, str2).putLong(9, ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION);
                        if (bitmap != null) {
                            try {
                                if (bitmap.getConfig() != null) {
                                    rcEditor.putBitmap(100, bitmap.copy(bitmap.getConfig(), true));
                                }
                            } catch (Exception ex) {
                                Crashlytics.logException(ex);
                            }
                        }
                        if (!Thread.currentThread().isInterrupted()) {
                            rcEditor.apply();
                        } else {
                            return;
                        }
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            if (!this.val$justWidgets) {
                if (UIService.this.mRemoteControlClientCompat != null) {
                    if (UIService.this.mStatus == UIService.STATUS_STOPPED || UIService.this.mStatus == 0) {
                        UIService.this.mRemoteControlClientCompat.setPlaybackState(2);
                    } else {
                        UIService.this.mRemoteControlClientCompat.setPlaybackState(3);
                    }
                }
                if (UIService.this.mShouldClearNotificationOnPowerOff && UIService.this.mStatus == UIService.STATUS_STOPPED) {
                    Log.d(UIService.TAG, "mShouldClearNotificationOnPowerOff - true, kill notification");
                    UIService.this.mNotificationHelper.killNotification();
                    UIService.this.mShouldClearNotificationOnPowerOff = false;
                } else {
                    UIService.this.mNotificationHelper.buildNotification(Stomp.EMPTY, this.val$line2, this.val$line1, bitmap, UIService.this.mStatus);
                }
            }
            if (!Thread.currentThread().isInterrupted()) {
                UIService.this.mWidgetProvider2Rows.notifyChange(UIService.this, UIService.this.mCurrentEvent, bitmap, UIService.this.mStatus);
                UIService.this.mWidget2Rows.notifyChange(UIService.this, UIService.this.mCurrentEvent, bitmap, UIService.this.mStatus);
                UIService.this.mWidget2RowsSmall.notifyChange(UIService.this, UIService.this.mCurrentEvent, bitmap, UIService.this.mStatus);
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.UIService.5 */
    class C12345 implements Runnable {
        C12345() {
        }

        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int divByZero = UIService.STATUS_PLAYING / UIService.STATUS_TRANSITION;
        }
    }

    public class ScreenReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                Log.d(UIService.TAG, "screen turned OFF - disabling images");
                UIService.this.mScreenOn = false;
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                Log.d(UIService.TAG, "screen turned ON - enabling images");
                UIService.this.mScreenOn = true;
                if (UIService.this.mStatus != UIService.STATUS_STOPPED) {
                    UIService.this.updateNotificationAndWidgets();
                }
            }
        }
    }

    public UIService() {
        this.mStatus = STATUS_STOPPED;
        this.mScreenOn = true;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        if (VERSION.SDK_INT >= 21) {
            this.mNotificationHelper = new NotificationHelper21(this);
        } else {
            this.mNotificationHelper = new NotificationHelper(this);
        }
        NextRadioApplication.registerWithBus(this);
        initializeWidget();
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.mReceiver = new ScreenReceiver();
        registerReceiver(this.mReceiver, filter);
    }

    private void initializeWidget() {
        this.mWidgetProvider2Rows = new Widget2RowsLarge();
        this.mWidget2Rows = new Widget2Rows();
        this.mWidget2RowsSmall = new Widget2RowsSmall();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(this.mReceiver);
        NextRadioApplication.unregisterWithBus(this);
        stopSelf();
    }

    @Subscribe
    public void onRadioAction(NRRadioAction radioAction) {
        if (radioAction != null && radioAction.isQuitting) {
            Log.d(TAG, "setting flag: mShouldClearNotificationOnPowerOff");
            this.mShouldClearNotificationOnPowerOff = true;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return 2;
        }
        int currentFrequency = intent.getIntExtra("frequencyHz", STATUS_TRANSITION);
        boolean forceRefresh = intent.getBooleanExtra("forceRefresh", false);
        int reasonCode = intent.getIntExtra("reasonCode", STATUS_TRANSITION);
        this.mShouldClearNotificationOnPowerOff = intent.getBooleanExtra("isQuitting", false);
        String action = intent.getAction();
        if (action == null) {
            Log.d(TAG, "onStartCommand null action, do nothing");
            return 2;
        } else if (action.equals(ACTION_WIDGETUPDATE)) {
            try {
                updateNotificationAndWidgets(true);
                return 2;
            } catch (Exception e) {
                return 2;
            }
        } else if (action.equals(ACTION_POWEREDOFF)) {
            this.mStatus = STATUS_STOPPED;
            this.mLastRadioResult = new C12301();
            NextRadioApplication.postToBus(this, this.mLastRadioResult);
            if (reasonCode == STATUS_PLAYING) {
                updateNotificationAndWidgets();
                stopForeground(true);
                stopSelf();
            } else {
                stopForeground(false);
                updateNotificationAndWidgets();
            }
            this.mWidgetProvider2Rows.notifyChange(this, this.mCurrentEvent, null, STATUS_STOPPED);
            this.mWidget2Rows.notifyChange(this, this.mCurrentEvent, null, STATUS_STOPPED);
            this.mWidget2RowsSmall.notifyChange(this, this.mCurrentEvent, null, STATUS_STOPPED);
            return 2;
        } else if (action.equals(ACTION_NEWFREQ)) {
            this.mStatus = STATUS_PLAYING;
            if (!forceRefresh && this.mLastRadioResult != null && currentFrequency == this.mLastRadioResult.frequencyHz) {
                return STATUS_PLAYING;
            }
            this.mLastRadioResult = new C12312(currentFrequency);
            NextRadioApplication.postToBus(this, this.mLastRadioResult);
            if (NextRadioSDKWrapperProvider.getInstance().hasInitialized()) {
                NextRadioSDKWrapperProvider.getInstance().startListeningToStation(currentFrequency, STATUS_TRANSITION);
            } else {
                initSDKInBackground(currentFrequency);
            }
            registerRemoteControls();
            return 2;
        } else if (!action.equals(ACTION_TRANSITIONING)) {
            return 2;
        } else {
            this.mLastRadioResult = new C12323();
            this.mWidgetProvider2Rows.notifyChange(this, this.mCurrentEvent, null, STATUS_TRANSITION);
            this.mWidget2Rows.notifyChange(this, this.mCurrentEvent, null, STATUS_TRANSITION);
            this.mWidget2RowsSmall.notifyChange(this, this.mCurrentEvent, null, STATUS_TRANSITION);
            updateNotificationAndWidgets();
            NextRadioApplication.postToBus(this, this.mLastRadioResult);
            this.mStatus = STATUS_TRANSITION;
            return 2;
        }
    }

    @Background
    public void initSDKInBackground(int currentFrequency) {
        ((NextRadioApplication) getApplicationContext()).initSDK();
        NextRadioSDKWrapperProvider.getInstance().startListeningToStation(currentFrequency, STATUS_TRANSITION);
    }

    @Subscribe
    public void onNewCurrentEvent(NRCurrentEvent event) {
        if (this.mStatus > 0 && event != null && event.currentEvent != null) {
            this.mCurrentEvent = event.currentEvent;
            updateNotificationAndWidgets();
        }
    }

    public void updateNotificationAndWidgets() {
        try {
            updateNotificationAndWidgets(false);
        } catch (Exception e) {
        }
    }

    public void updateNotificationAndWidgets(boolean justWidgets) throws Exception {
        Log.d(TAG, "updateNotificationAndWidgets");
        if (this.mCurrentEvent == null) {
            if (this.mStatus == STATUS_STOPPED && this.mShouldClearNotificationOnPowerOff && this.mNotificationHelper != null) {
                this.mNotificationHelper.killNotification();
                this.mShouldClearNotificationOnPowerOff = false;
            }
            Log.w(TAG, "updateNotificationAndWidgets - mCurrentEventEmpty, doing nothing");
            return;
        }
        String line1 = this.mCurrentEvent.getUILine1() + Stomp.EMPTY;
        String line2 = this.mCurrentEvent.getUILine2() + Stomp.EMPTY;
        if (this.mMediaControlsUpdateThread != null) {
            this.mMediaControlsUpdateThread.interrupt();
        }
        this.mMediaControlsUpdateThread = new Thread(new C12334(justWidgets, line2, line1));
        this.mMediaControlsUpdateThread.start();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        int oldOrientation = getResources().getConfiguration().orientation;
        if (newConfig.orientation != oldOrientation) {
            Log.e(TAG, "newConfig.orientation:" + newConfig.orientation);
            Log.e(TAG, "oldOrientation:" + oldOrientation);
        }
    }

    private void registerRemoteControls() {
        AudioManager mAudioManager = (AudioManager) getSystemService("audio");
        ComponentName mediaButtonReceiverComponent = new ComponentName(this, MusicIntentReceiver.class);
        Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        MediaButtonHelper.registerMediaButtonEventReceiverCompat(mAudioManager, mediaButtonReceiverComponent);
        intent.setComponent(mediaButtonReceiverComponent);
        this.mRemoteControlClientCompat = new RemoteControlClientCompat(PendingIntent.getBroadcast(this, STATUS_TRANSITION, intent, STATUS_TRANSITION));
        RemoteControlHelper.registerRemoteControlClient(mAudioManager, this.mRemoteControlClientCompat);
        this.mRemoteControlClientCompat.setPlaybackState(3);
        this.mRemoteControlClientCompat.setTransportControlFlags(181);
    }

    @UiThread
    public void crashTest() {
        Thread crashThisMuthaThread = new Thread(new C12345());
        Toast.makeText(this, "CRASHING IN 5s", STATUS_TRANSITION).show();
        crashThisMuthaThread.start();
    }
}
