package com.nextradioapp.nextradio.widgetnotifications;

import android.annotation.SuppressLint;
import android.app.Service;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;

@SuppressLint({"NewApi"})
public class NotificationHelper extends NotificationHelperBase {
    private RemoteViews mExpandedView;
    private RemoteViews mNotificationTemplate;

    public NotificationHelper(Service uiService) {
        super(uiService);
    }

    public void buildNotification(String albumName, String artistName, String trackName, Bitmap albumArt, int radioMode) {
        this.mNotificationTemplate = new RemoteViews(this.mService.getPackageName(), 2130903101);
        initCollapsedLayout(trackName, artistName, albumArt);
        this.mNotification = new Builder(this.mService).setSmallIcon(2130837744).setContentIntent(getPendingIntent()).setLargeIcon(albumArt).setPriority(2).setVisibility(1).setContent(this.mNotificationTemplate).setContentIntent(getPendingIntent()).build();
        initPlaybackActions(radioMode);
        this.mExpandedView = new RemoteViews(this.mService.getPackageName(), 2130903100);
        this.mNotification.bigContentView = this.mExpandedView;
        initExpandedPlaybackActions(radioMode);
        initExpandedLayout(trackName, albumName, artistName, albumArt);
        this.mService.startForeground(1, this.mNotification);
    }

    private void initExpandedPlaybackActions(int radioMode) {
        this.mExpandedView.setOnClickPendingIntent(2131689743, retreivePlaybackActions(1));
        this.mExpandedView.setOnClickPendingIntent(2131689744, retreivePlaybackActions(2));
        this.mExpandedView.setOnClickPendingIntent(2131689742, retreivePlaybackActions(3));
        this.mExpandedView.setOnClickPendingIntent(2131689745, retreivePlaybackActions(4));
        if (radioMode == -1) {
            this.mExpandedView.setImageViewResource(2131689743, 2130837546);
        } else if (radioMode == 1) {
            this.mExpandedView.setImageViewResource(2131689743, 2130837549);
        } else {
            this.mExpandedView.setImageViewResource(2131689743, 2130837550);
        }
    }

    private void initPlaybackActions(int radioMode) {
        this.mNotificationTemplate.setOnClickPendingIntent(2131689754, retreivePlaybackActions(11));
        this.mNotificationTemplate.setOnClickPendingIntent(2131689755, retreivePlaybackActions(12));
        this.mNotificationTemplate.setOnClickPendingIntent(2131689753, retreivePlaybackActions(13));
        this.mNotificationTemplate.setOnClickPendingIntent(2131689756, retreivePlaybackActions(14));
        if (radioMode == -1) {
            this.mNotificationTemplate.setImageViewResource(2131689754, 2130837546);
        } else if (radioMode == 1) {
            this.mNotificationTemplate.setImageViewResource(2131689754, 2130837549);
        } else {
            this.mNotificationTemplate.setImageViewResource(2131689754, 2130837550);
        }
    }

    private void initCollapsedLayout(String trackName, String artistName, Bitmap albumArt) {
        this.mNotificationTemplate.setTextViewText(2131689751, trackName);
        this.mNotificationTemplate.setTextViewText(2131689752, artistName);
        this.mNotificationTemplate.setImageViewBitmap(2131689750, albumArt);
    }

    private void initExpandedLayout(String trackName, String artistName, String albumName, Bitmap albumArt) {
        this.mExpandedView.setTextViewText(2131689746, trackName);
        this.mExpandedView.setTextViewText(2131689747, albumName);
        this.mExpandedView.setTextViewText(2131689748, artistName);
        if (albumArt == null) {
            this.mExpandedView.setImageViewResource(2131689740, 2130837651);
        } else {
            this.mExpandedView.setImageViewBitmap(2131689740, albumArt);
        }
    }
}
