package com.nextradioapp.nextradio.widgetnotifications;

import android.annotation.TargetApi;
import android.app.Notification.Builder;
import android.app.Notification.MediaStyle;
import android.app.Service;
import android.graphics.Bitmap;
import android.util.Log;
import org.apache.activemq.transport.stomp.Stomp;

@TargetApi(21)
public class NotificationHelper21 extends NotificationHelperBase {
    public NotificationHelper21(Service uiService) {
        super(uiService);
    }

    public void buildNotification(String albumName, String artistName, String trackName, Bitmap albumArt, int radioMode) {
        int statusIconResource;
        boolean z;
        Log.d("NotificationHelper21", "radioMode:" + radioMode);
        if (radioMode == 1) {
            statusIconResource = 2130837549;
        } else if (radioMode == -1) {
            statusIconResource = 2130837546;
        } else {
            statusIconResource = 2130837550;
        }
        Builder contentIntent = new Builder(this.mService).setSmallIcon(2130837744).setContentTitle(trackName).setContentText(artistName).setContentIntent(getPendingIntent());
        if (radioMode != -1) {
            z = true;
        } else {
            z = false;
        }
        this.mNotification = contentIntent.setOngoing(z).setShowWhen(false).setVisibility(1).setLargeIcon(albumArt).setStyle(new MediaStyle().setShowActionsInCompactView(new int[]{0, 1, 2})).addAction(2130837548, Stomp.EMPTY, retreivePlaybackActions(3)).addAction(statusIconResource, Stomp.EMPTY, retreivePlaybackActions(1)).addAction(2130837544, Stomp.EMPTY, retreivePlaybackActions(2)).build();
        if (radioMode != -1) {
            this.mService.startForeground(1, this.mNotification);
        } else {
            this.mNotificationManager.notify(1, this.mNotification);
        }
    }
}
