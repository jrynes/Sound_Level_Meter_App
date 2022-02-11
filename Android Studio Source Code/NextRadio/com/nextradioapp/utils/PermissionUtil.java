package com.nextradioapp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.nextradioapp.nextradio.activities.StationsActivity;
import org.apache.activemq.transport.stomp.Stomp;

public abstract class PermissionUtil {
    public static String[] PERMISSIONS_LOCATION = null;
    public static String[] PERMISSIONS_STORAGE = null;
    public static final int REQUEST_LOCATION = 1;
    public static final int REQUEST_STORAGE = 2;

    static {
        String[] strArr = new String[REQUEST_LOCATION];
        strArr[0] = "android.permission.ACCESS_FINE_LOCATION";
        PERMISSIONS_LOCATION = strArr;
        strArr = new String[REQUEST_LOCATION];
        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
        PERMISSIONS_STORAGE = strArr;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < REQUEST_LOCATION) {
            return false;
        }
        int[] arr$ = grantResults;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$ += REQUEST_LOCATION) {
            int result = arr$[i$];
            Log.e(StationsActivity.TAG, "result:" + result);
            if (result != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean showLocation(Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            return true;
        }
        requestLocationPermission(mActivity);
        return false;
    }

    private static void requestLocationPermission(Activity mActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_FINE_LOCATION")) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS_LOCATION, REQUEST_LOCATION);
        } else {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS_LOCATION, REQUEST_LOCATION);
        }
    }

    public static void showStoragePermission(Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mActivity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestStoragePermission(mActivity);
        }
    }

    private static void requestStoragePermission(Activity mActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS_STORAGE, REQUEST_STORAGE);
        } else {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS_STORAGE, REQUEST_STORAGE);
        }
    }

    public static void saveLocationPermissionDeniedDate(Context mContext, String date) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("location_permission_date", date).apply();
    }

    public static String getLocationPermissionDeniedDate(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString("location_permission_date", Stomp.EMPTY);
    }

    public static void saveStoragePermissionDeniedDate(Context mContext, String date) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("storage_permission_date", date).apply();
    }

    public static String getStoragePermissionStateDeniedDate(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString("storage_permission_date", Stomp.EMPTY);
    }

    public static void saveLocationPermissionState(Context mContext, boolean status) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean("location_permission_status", status).apply();
    }

    public static boolean isLocationPermissionAccepted(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("location_permission_status", false);
    }

    public static void saveStoragePermissionState(Context mContext, boolean status) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean("storage_permission_status", status).apply();
    }

    public static boolean isStoragePermissionAccepted(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("storage_permission_status", false);
    }

    public static void saveStationType(Context mContext, int status) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt("display_station_type", status).apply();
    }

    public static int getStationType(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("display_station_type", 3);
    }

    public static void saveUpgradingState(Context mContext, boolean status) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean("upgrade_application", status).apply();
    }

    public static boolean getUpgradingState(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("upgrade_application", true);
    }

    public static void saveRadioState(Context mContext, int val) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt("radio_state", val).apply();
    }

    public static int getRadioState(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt("radio_state", REQUEST_STORAGE);
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        State network = info.getState();
        if (network == State.CONNECTED || network == State.CONNECTING) {
            return true;
        }
        return false;
    }
}
