package com.nextradioapp.nextradio.widgetnotifications;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.nextradioapp.androidSDK.data.schema.Tables.impressionReporting;
import com.nextradioapp.core.Log;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.activities.SplashScreen;
import com.nextradioapp.nextradio.activities.StationsActivity_;
import com.nextradioapp.nextradio.activities.TabletFragContainerActivity_;
import com.nextradioapp.nextradio.services.RadioAdapterService;
import com.nextradioapp.nextradio.services.RadioAdapterService_;
import com.nextradioapp.nextradio.services.UIService;
import com.nextradioapp.nextradio.services.UIService_;
import org.apache.activemq.transport.stomp.Stomp;

public class Widget2Rows extends AppWidgetProvider {
    private static final String TAG = "Widget2Rows";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int updateAppWidget : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, updateAppWidget);
        }
        Intent intent = new Intent(context, UIService_.class);
        intent.setAction(UIService.ACTION_WIDGETUPDATE);
        context.startService(intent);
    }

    public void onEnabled(Context context) {
    }

    public void onDisabled(Context context) {
    }

    private boolean hasInstances(Context context) {
        return AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass())).length > 0;
    }

    public void notifyChange(Context context, NextRadioEventInfo event, Bitmap bitmap, int isPlaying) {
        Log.m1934d(TAG, "notifyChange-event==null:" + (event == null));
        if (hasInstances(context)) {
            RemoteViews views;
            int requestCode;
            if (isPlaying == -1) {
                if (event == null) {
                    views = new RemoteViews(context.getPackageName(), 2130903130);
                    requestCode = 2;
                } else {
                    views = new RemoteViews(context.getPackageName(), 2130903127);
                    requestCode = 3;
                }
            } else if (isPlaying == 1) {
                views = new RemoteViews(context.getPackageName(), 2130903127);
                requestCode = 4;
            } else {
                views = new RemoteViews(context.getPackageName(), 2130903127);
                requestCode = 5;
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (isPlaying == -1) {
                views.setViewVisibility(2131689829, 0);
                views.setViewVisibility(2131689827, 8);
                views.setViewVisibility(2131689828, 8);
            } else if (isPlaying == 1) {
                views.setViewVisibility(2131689829, 8);
                views.setViewVisibility(2131689827, 0);
                views.setViewVisibility(2131689828, 8);
            } else if (isPlaying == 0) {
                views.setViewVisibility(2131689829, 8);
                views.setViewVisibility(2131689827, 8);
                views.setViewVisibility(2131689828, 0);
            }
            views.setOnClickPendingIntent(2131689819, PendingIntent.getActivity(context, requestCode, Intent.makeMainActivity(new ComponentName(context, SplashScreen.class)), 0));
            if (event != null) {
                populateWidget(context, event, bitmap, views);
            }
            appWidgetManager.updateAppWidget(appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass())), views);
        }
    }

    private void populateWidget(Context context, NextRadioEventInfo event, Bitmap bitmap, RemoteViews views) {
        String dash = " - ";
        if ((event.getUILine1() != null && event.getUILine1().length() == 0) || (event.getUILine2() != null && event.getUILine2().length() == 0)) {
            dash = Stomp.EMPTY;
        }
        views.setTextViewText(2131689822, event.getUILine1() + dash + event.getUILine2());
        if (event.stationInfo.slogan == null || event.stationInfo.slogan.length() == 0) {
            views.setTextViewText(2131689821, Stomp.EMPTY);
        } else {
            views.setTextViewText(2131689821, " - " + event.stationInfo.slogan);
        }
        views.setTextViewText(2131689676, event.stationInfo.frequencyMHz());
        EventAction primaryAction = NextRadioSDKWrapperProvider.getInstance().getPrimaryEventActions(event);
        if (primaryAction == null) {
            views.setViewVisibility(2131689824, 4);
        } else if (primaryAction.getActionDescription() == null || primaryAction.getType().equals("share")) {
            views.setViewVisibility(2131689824, 4);
        } else {
            Intent launchWidgetAction;
            views.setTextViewText(2131689824, primaryAction.getActionDescription().toUpperCase());
            views.setViewVisibility(2131689824, 0);
            if (NextRadioApplication.isTablet) {
                launchWidgetAction = new Intent(context, TabletFragContainerActivity_.class);
            } else {
                launchWidgetAction = new Intent(context, StationsActivity_.class);
            }
            launchWidgetAction.setAction(primaryAction.getType() + event.UFIDIdentifier);
            launchWidgetAction.putExtra("what", primaryAction.getType());
            launchWidgetAction.putExtra(impressionReporting.ufid, event.UFIDIdentifier);
            launchWidgetAction.putExtra("isFrom", 1);
            launchWidgetAction.setFlags(536870912);
            views.setOnClickPendingIntent(2131689824, PendingIntent.getActivity(context, 0, launchWidgetAction, 134217728));
        }
        if (event.UFIDIdentifier != null) {
            Intent launchShareWidgetAction;
            if (NextRadioApplication.isTablet) {
                launchShareWidgetAction = new Intent(context, TabletFragContainerActivity_.class);
            } else {
                launchShareWidgetAction = new Intent(context, StationsActivity_.class);
            }
            launchShareWidgetAction.setAction("share");
            launchShareWidgetAction.putExtra("what", "share");
            launchShareWidgetAction.putExtra(impressionReporting.ufid, event.UFIDIdentifier);
            launchShareWidgetAction.putExtra("isFrom", 1);
            NextRadioApplication.getInstance().setEventInfo(event);
            launchShareWidgetAction.setFlags(536870912);
            PendingIntent pendingWidgetAction = PendingIntent.getActivity(context, 0, launchShareWidgetAction, 0);
            views.setViewVisibility(2131689817, 0);
            views.setOnClickPendingIntent(2131689817, pendingWidgetAction);
        } else {
            views.setViewVisibility(2131689817, 4);
        }
        if (bitmap != null) {
            views.setImageViewBitmap(2131689820, bitmap);
        }
        Intent actionSeekUp = new Intent(context, RadioAdapterService_.class);
        actionSeekUp.setAction(RadioAdapterService.ACTION_SEEKUP);
        actionSeekUp.putExtra("direction", 1);
        actionSeekUp.putExtra("widgetSeek", true);
        views.setOnClickPendingIntent(2131689695, PendingIntent.getService(context, 0, actionSeekUp, 0));
        Intent action = new Intent(context, RadioAdapterService_.class);
        action.setAction(RadioAdapterService.ACTION_SEEKDOWN);
        action.putExtra("direction", -1);
        action.putExtra("widgetSeek", true);
        views.setOnClickPendingIntent(2131689692, PendingIntent.getService(context, 1, action, 0));
        Intent actionTurnOn = new Intent(context, RadioAdapterService_.class);
        actionTurnOn.setAction(RadioAdapterService.ACTION_PLAY);
        views.setOnClickPendingIntent(2131689829, PendingIntent.getService(context, 2, actionTurnOn, 0));
        Intent actionTurnOff = new Intent(context, RadioAdapterService_.class);
        actionTurnOff.setAction(RadioAdapterService.ACTION_STOP);
        views.setOnClickPendingIntent(2131689827, PendingIntent.getService(context, 3, actionTurnOff, 0));
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        CharSequence widgetText = context.getString(2131165440);
        RemoteViews views = new RemoteViews(context.getPackageName(), 2130903130);
        views.setOnClickPendingIntent(2131689819, PendingIntent.getActivity(context, 0, Intent.makeMainActivity(new ComponentName(context, SplashScreen.class)), 0));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
