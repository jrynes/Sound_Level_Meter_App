package com.nextradioapp.nextradio;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;
import com.google.android.gms.gcm.GcmListenerService;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.nextradioapp.core.Log;
import com.nextradioapp.nextradio.activities.StationsActivity_;
import com.nextradioapp.nextradio.activities.TabletFragContainerActivity_;

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    public void onMessageReceived(String from, Bundle data) {
        Log.m1934d(TAG, "data:" + data);
        String message = data.getString("mp_message");
        String campaignId = data.getString("mp_campaign_id");
        CharSequence notificationTitle = data.getString("mp_title");
        if (notificationTitle == null || notificationTitle.length() == 0) {
            notificationTitle = "NextRadio";
        }
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(PreferenceStorage.NOTIFICATION_PREFERENCE, true)) {
            sendNotification(message, notificationTitle, campaignId);
        }
    }

    private void sendNotification(String message, CharSequence notificationTitle, String campaignId) {
        Intent intent;
        if (NextRadioApplication.isTablet) {
            intent = new Intent(this, TabletFragContainerActivity_.class);
        } else {
            intent = new Intent(this, StationsActivity_.class);
        }
        intent.putExtra("campaign_id", campaignId);
        intent.setFlags(603979776);
        ((NotificationManager) getSystemService("notification")).notify(0, new Builder(this).setSmallIcon(2130837705).setContentTitle(notificationTitle).setContentText(message).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).setContentIntent(PendingIntent.getActivity(this, 0, intent, 1073741824)).build());
    }
}
