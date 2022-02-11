package com.nextradioapp.androidSDK.ext;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.nextradioapp.core.Log;
import com.nextradioapp.core.dependencies.IPreferenceStorage;
import com.nextradioapp.core.objects.DeviceRegistrationInfo;
import com.nextradioapp.core.objects.Location;
import org.apache.activemq.transport.stomp.Stomp;

public class PreferenceStorage implements IPreferenceStorage {
    public static final String AD_GROUP = "adGroup";
    public static final String CACHING_ID = "cachingID";
    private static final String DEMO_STATIONS_ALLOWED = "DemoStationsAllowed";
    public static final String DEVICE_ID = "deviceID";
    private static final String DEVICE_STRING = "DeviceString";
    private static final String DefaultTAGURL = "http://api.tagstation.com/";
    public static final String EULA_AGREEMENT = "EULA";
    public static final String NOTIFICATION_PREFERENCE = "notifications";
    public static final String NO_DATA_MODE = "NoDataMode";
    public static final String OVERRIDE_URL = "apiurl";
    public static final String SELECTED_COUNTRY = "country";
    private static final String STATION_DOWNLOAD_LAT = "LOCATION_LATITUDE";
    private static final String STATION_DOWNLOAD_LONG = "LOCATION_LONGITUDE";
    private static final String STATION_DOWNLOAD_TIME_UTC_MILLIS = "lastStationRefresh";
    private static final String TAG = "PreferenceStorage";
    private Context mContext;

    public void setDeviceID(String deviceID) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString(DEVICE_ID, deviceID).commit();
    }

    public void setCachingID(String ID) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString(CACHING_ID, ID).commit();
    }

    public void setAdGroupID(String ID) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString(AD_GROUP, ID).commit();
    }

    public DeviceRegistrationInfo getDeviceRegistrationInfo() {
        SharedPreferences sharePrefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String tsd = sharePrefs.getString(DEVICE_ID, Stomp.EMPTY);
        String ad = sharePrefs.getString(AD_GROUP, Stomp.EMPTY);
        String cache = sharePrefs.getString(CACHING_ID, Stomp.EMPTY);
        DeviceRegistrationInfo di = new DeviceRegistrationInfo();
        di.adGroup = ad;
        di.cachingGroup = cache;
        di.TSD = tsd;
        return di;
    }

    public PreferenceStorage(Context context) {
        this.mContext = context;
    }

    public Location getStationDownloadLocation() {
        SharedPreferences sharePrefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        Location l = new Location((double) sharePrefs.getFloat(STATION_DOWNLOAD_LAT, 0.0f), (double) sharePrefs.getFloat(STATION_DOWNLOAD_LONG, 0.0f), 0);
        Log.m1934d(TAG, "getStationDownloadLocation " + l.getLatitude() + Stomp.COMMA + l.getLongitude());
        return l;
    }

    public long getStationDownloadTime() {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getLong(STATION_DOWNLOAD_TIME_UTC_MILLIS, 0);
    }

    public boolean getNoDataMode() {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean(NO_DATA_MODE, false);
    }

    public void setNoDataMode(boolean isNoDataMode) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putBoolean(NO_DATA_MODE, isNoDataMode).commit();
    }

    public void setStationDownloadLocation(Location location) {
        Log.m1934d(TAG, "setStationDownloadLocation " + location.getLatitude() + Stomp.COMMA + location.getLongitude());
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putFloat(STATION_DOWNLOAD_LAT, location.getLatitude()).putFloat(STATION_DOWNLOAD_LONG, location.getLongitude()).commit();
    }

    public void setStationDownloadTime(long downloadTimeUTCMillis) {
        Log.m1934d(TAG, "setStationDownloadTime " + downloadTimeUTCMillis);
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putLong(STATION_DOWNLOAD_TIME_UTC_MILLIS, downloadTimeUTCMillis).commit();
    }

    public void setDeviceRegistration(DeviceRegistrationInfo deviceInfo) {
        if (deviceInfo != null) {
            PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString(DEVICE_ID, deviceInfo.TSD).putString(CACHING_ID, deviceInfo.cachingGroup).putString(AD_GROUP, deviceInfo.adGroup).commit();
        }
    }

    public boolean getDemoStationsAllowed() {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean(DEMO_STATIONS_ALLOWED, false);
    }

    public void setDemoStationsAllowed(boolean isAllowed) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putBoolean(DEMO_STATIONS_ALLOWED, isAllowed).commit();
    }

    public String getTagURL() {
        SharedPreferences sharePrefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (!sharePrefs.getBoolean("enabledev", false)) {
            return DefaultTAGURL;
        }
        String value = sharePrefs.getString(OVERRIDE_URL, null);
        if (value != null) {
            return value;
        }
        return DefaultTAGURL;
    }

    public void setTagURL(String url) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString(OVERRIDE_URL, url).commit();
    }

    public String getDeviceString() {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString(DEVICE_STRING, null);
    }

    public void setDeviceString(String newDeviceRegistrationString) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString(DEVICE_STRING, newDeviceRegistrationString).commit();
    }

    public String getCountryString() {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString(SELECTED_COUNTRY, null);
    }

    public void setCountryString(String countryString) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString(SELECTED_COUNTRY, countryString).commit();
    }
}
