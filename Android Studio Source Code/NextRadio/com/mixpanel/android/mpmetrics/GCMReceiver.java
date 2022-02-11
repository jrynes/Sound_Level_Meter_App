package com.mixpanel.android.mpmetrics;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.mixpanel.android.mpmetrics.ResourceReader.Drawables;

public class GCMReceiver extends BroadcastReceiver {
    private static final String LOGTAG = "MixpanelAPI.GCMReceiver";

    /* renamed from: com.mixpanel.android.mpmetrics.GCMReceiver.1 */
    class C10801 implements InstanceProcessor {
        final /* synthetic */ String val$registration;

        C10801(String str) {
            this.val$registration = str;
        }

        public void process(MixpanelAPI api) {
            api.getPeople().setPushRegistrationId(this.val$registration);
        }
    }

    /* renamed from: com.mixpanel.android.mpmetrics.GCMReceiver.2 */
    class C10812 implements InstanceProcessor {
        C10812() {
        }

        public void process(MixpanelAPI api) {
            api.getPeople().clearPushRegistrationId();
        }
    }

    static class NotificationData {
        public final int icon;
        public final Intent intent;
        public final String message;
        public final CharSequence title;

        private NotificationData(int anIcon, CharSequence aTitle, String aMessage, Intent anIntent) {
            this.icon = anIcon;
            this.title = aTitle;
            this.message = aMessage;
            this.intent = anIntent;
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
            handleRegistrationIntent(intent);
        } else if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
            handleNotificationIntent(context, intent);
        }
    }

    Intent getDefaultIntent(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    }

    NotificationData readInboundIntent(Context context, Intent inboundIntent, ResourceIds iconIds) {
        PackageManager manager = context.getPackageManager();
        String message = inboundIntent.getStringExtra("mp_message");
        String iconName = inboundIntent.getStringExtra("mp_icnm");
        String uriString = inboundIntent.getStringExtra("mp_cta");
        CharSequence notificationTitle = inboundIntent.getStringExtra("mp_title");
        if (message == null) {
            return null;
        }
        ApplicationInfo appInfo;
        int notificationIcon = -1;
        if (iconName != null && iconIds.knownIdName(iconName)) {
            notificationIcon = iconIds.idFromName(iconName);
        }
        try {
            appInfo = manager.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            appInfo = null;
        }
        if (notificationIcon == -1 && appInfo != null) {
            notificationIcon = appInfo.icon;
        }
        if (notificationIcon == -1) {
            notificationIcon = 17301651;
        }
        if (notificationTitle == null && appInfo != null) {
            notificationTitle = manager.getApplicationLabel(appInfo);
        }
        if (notificationTitle == null) {
            notificationTitle = "A message for you";
        }
        return new NotificationData(notificationTitle, message, buildNotificationIntent(context, uriString), null);
    }

    private Intent buildNotificationIntent(Context context, String uriString) {
        Uri uri = null;
        if (uriString != null) {
            uri = Uri.parse(uriString);
        }
        if (uri == null) {
            return getDefaultIntent(context);
        }
        return new Intent("android.intent.action.VIEW", uri);
    }

    private Notification buildNotification(Context context, Intent inboundIntent, ResourceIds iconIds) {
        NotificationData notificationData = readInboundIntent(context, inboundIntent, iconIds);
        if (notificationData == null) {
            return null;
        }
        if (MPConfig.DEBUG) {
            Log.d(LOGTAG, "MP GCM notification received: " + notificationData.message);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationData.intent, 134217728);
        if (VERSION.SDK_INT >= 16) {
            return makeNotificationSDK16OrHigher(context, contentIntent, notificationData);
        }
        if (VERSION.SDK_INT >= 11) {
            return makeNotificationSDK11OrHigher(context, contentIntent, notificationData);
        }
        return makeNotificationSDKLessThan11(context, contentIntent, notificationData);
    }

    private void handleRegistrationIntent(Intent intent) {
        String registration = intent.getStringExtra("registration_id");
        if (intent.getStringExtra(Diagnostics.error) != null) {
            Log.e(LOGTAG, "Error when registering for GCM: " + intent.getStringExtra(Diagnostics.error));
        } else if (registration != null) {
            if (MPConfig.DEBUG) {
                Log.d(LOGTAG, "Registering GCM ID: " + registration);
            }
            MixpanelAPI.allInstances(new C10801(registration));
        } else if (intent.getStringExtra("unregistered") != null) {
            if (MPConfig.DEBUG) {
                Log.d(LOGTAG, "Unregistering from GCM");
            }
            MixpanelAPI.allInstances(new C10812());
        }
    }

    private void handleNotificationIntent(Context context, Intent intent) {
        String resourcePackage = MPConfig.getInstance(context).getResourcePackageName();
        if (resourcePackage == null) {
            resourcePackage = context.getPackageName();
        }
        Notification notification = buildNotification(context.getApplicationContext(), intent, new Drawables(resourcePackage, context));
        if (notification != null) {
            ((NotificationManager) context.getSystemService("notification")).notify(0, notification);
        }
    }

    @TargetApi(9)
    private Notification makeNotificationSDKLessThan11(Context context, PendingIntent intent, NotificationData notificationData) {
        Notification n = new Builder(context).setSmallIcon(notificationData.icon).setTicker(notificationData.message).setWhen(System.currentTimeMillis()).setContentTitle(notificationData.title).setContentText(notificationData.message).setContentIntent(intent).getNotification();
        n.flags |= 16;
        return n;
    }

    @TargetApi(11)
    private Notification makeNotificationSDK11OrHigher(Context context, PendingIntent intent, NotificationData notificationData) {
        Notification n = new Notification.Builder(context).setSmallIcon(notificationData.icon).setTicker(notificationData.message).setWhen(System.currentTimeMillis()).setContentTitle(notificationData.title).setContentText(notificationData.message).setContentIntent(intent).getNotification();
        n.flags |= 16;
        return n;
    }

    @SuppressLint({"NewApi"})
    @TargetApi(16)
    private Notification makeNotificationSDK16OrHigher(Context context, PendingIntent intent, NotificationData notificationData) {
        Notification n = new Notification.Builder(context).setSmallIcon(notificationData.icon).setTicker(notificationData.message).setWhen(System.currentTimeMillis()).setContentTitle(notificationData.title).setContentText(notificationData.message).setContentIntent(intent).setStyle(new BigTextStyle().bigText(notificationData.message)).build();
        n.flags |= 16;
        return n;
    }
}
