package com.nextradioapp.nextradio.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;

public class LocationReceiver extends WakefulBroadcastReceiver {
    private static LocationReceiver instance;
    private String TAG;
    private AlarmManager alarmMgr;
    private Handler handler;
    private boolean isLocationRequested;
    private Context mContext;
    private PendingIntent pendingIntent;
    private Thread thread;

    class Task implements Runnable {
        Task() {
        }

        public void run() {
            try {
                NextRadioSDKWrapperProvider.getInstance().resumeLocationUpdates();
                LocationReceiver.this.isLocationRequested = true;
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LocationReceiver.this.handler.post(new 1(this));
        }
    }

    public LocationReceiver() {
        this.TAG = "location-settings";
    }

    public static synchronized LocationReceiver getInstance() {
        LocationReceiver locationReceiver;
        synchronized (LocationReceiver.class) {
            if (instance == null) {
                instance = new LocationReceiver();
            }
            locationReceiver = instance;
        }
        return locationReceiver;
    }

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        this.handler = new Handler();
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            recordLocation(context);
        }
    }

    private void recordLocation(Context context) {
        if (!isLocationServicesAvailable(context) || NextRadioSDKWrapperProvider.getInstance().getCurrentLocation() == null) {
            Log.d(this.TAG, "Location not saved, it will check again after 1 min:");
            if (isLocationEnabled(context) && !this.isLocationRequested) {
                removeCallBacks();
                this.thread = new Thread(new Task());
                this.thread.start();
            }
        } else if (NextRadioSDKWrapperProvider.getInstance().getCurrentLocation().isValid()) {
            NextRadioSDKWrapperProvider.getInstance().putLocationData(4, 0, String.valueOf(NextRadioSDKWrapperProvider.getInstance().getCurrentLocation().getLatitude()), String.valueOf(NextRadioSDKWrapperProvider.getInstance().getCurrentLocation().getLongitude()));
            Log.d(this.TAG, "Location saved:");
        } else if (isLocationEnabled(context) && !this.isLocationRequested) {
            removeCallBacks();
            this.thread = new Thread(new Task());
            this.thread.start();
        }
    }

    private void removeCallBacks() {
        try {
            if (this.thread != null) {
                this.thread.interrupt();
                this.handler.removeCallbacks(this.thread);
                this.thread = null;
            }
        } catch (Exception e) {
        }
    }

    public boolean isLocationServicesAvailable(Context context) {
        boolean foundNonPassive = false;
        try {
            LocationManager mLocationManager = (LocationManager) context.getSystemService("location");
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.mContext) == 0) {
                for (String provider : mLocationManager.getProviders(true)) {
                    if (!provider.equals("passive")) {
                        foundNonPassive = true;
                        break;
                    }
                }
            }
            foundNonPassive = false;
            Log.d(this.TAG, "location connection:" + foundNonPassive);
        } catch (Exception e) {
        }
        return foundNonPassive;
    }

    public static void setFlagIfLocationReceiverStart(Context mContext, boolean status) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean("alarmService", status).apply();
    }

    public static boolean isLocationServiceStarted(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("alarmService", false);
    }

    public void scheduleAlarms(Context context) {
        Log.d(this.TAG, "scheduleAlarms");
        setFlagIfLocationReceiverStart(context, true);
        this.alarmMgr = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        this.pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, LocationReceiver.class), 0);
        this.alarmMgr.setInexactRepeating(2, 900000, 900000, this.pendingIntent);
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, LocationReceiver.class), 1, 1);
    }

    private boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        if (locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network")) {
            return true;
        }
        return false;
    }

    public void cancelAlarm(Context context) {
        try {
            setFlagIfLocationReceiverStart(context, false);
            if (this.alarmMgr != null) {
                this.alarmMgr.cancel(this.pendingIntent);
            }
            context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, LocationReceiver.class), 2, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
