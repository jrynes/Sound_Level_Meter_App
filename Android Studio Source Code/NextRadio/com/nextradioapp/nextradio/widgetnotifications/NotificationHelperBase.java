package com.nextradioapp.nextradio.widgetnotifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import com.nextradioapp.core.Log;
import com.nextradioapp.nextradio.IntentBuilder;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.activities.StationsActivity_;
import com.nextradioapp.nextradio.activities.TabletFragContainerActivity_;
import com.nextradioapp.nextradio.services.RadioAdapterService_;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public abstract class NotificationHelperBase {
    protected static final int NOTIFICATION_NEXTRADIO = 1;
    static final String TAG = "NotificationHelperBase";
    protected Notification mNotification;
    protected NotificationManager mNotificationManager;
    protected Service mService;

    public abstract void buildNotification(String str, String str2, String str3, Bitmap bitmap, int i);

    public NotificationHelperBase() {
        this.mNotification = null;
    }

    public NotificationHelperBase(Service service) {
        this.mNotification = null;
        this.mService = service;
        this.mNotificationManager = (NotificationManager) service.getSystemService("notification");
    }

    public void killNotification() {
        Log.m1934d(TAG, "killNotification");
        this.mService.stopForeground(true);
        this.mNotificationManager.cancel(NOTIFICATION_NEXTRADIO);
        this.mNotification = null;
    }

    protected PendingIntent getPendingIntent() {
        Intent intent;
        if (NextRadioApplication.isTablet) {
            intent = new Intent(this.mService, TabletFragContainerActivity_.class);
        } else {
            intent = new Intent(this.mService, StationsActivity_.class);
        }
        return PendingIntent.getActivity(this.mService, 0, intent, 0);
    }

    protected final PendingIntent retreivePlaybackActions(int which) {
        ComponentName serviceName = new ComponentName(this.mService, RadioAdapterService_.class);
        Intent action;
        switch (which) {
            case NOTIFICATION_NEXTRADIO /*1*/:
            case WKSRecord.Service.USERS /*11*/:
                action = IntentBuilder.playPause(this.mService);
                action.setComponent(serviceName);
                return PendingIntent.getService(this.mService, which, action, 0);
            case Zone.SECONDARY /*2*/:
            case Protocol.PUP /*12*/:
                action = IntentBuilder.seek(this.mService, NOTIFICATION_NEXTRADIO, true);
                action.setComponent(serviceName);
                return PendingIntent.getService(this.mService, which, action, 0);
            case Protocol.GGP /*3*/:
            case WKSRecord.Service.DAYTIME /*13*/:
                action = IntentBuilder.seek(this.mService, -1, true);
                action.setComponent(serviceName);
                return PendingIntent.getService(this.mService, which, action, 0);
            case Type.MF /*4*/:
            case Protocol.EMCON /*14*/:
                action = IntentBuilder.turnOff(this.mService, false);
                action.setComponent(serviceName);
                return PendingIntent.getService(this.mService, which, action, 0);
            default:
                return null;
        }
    }
}
