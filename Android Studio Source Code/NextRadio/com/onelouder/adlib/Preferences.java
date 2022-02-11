package com.onelouder.adlib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.util.HashMap;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;

class Preferences {
    private static final String TAG = "Preferences";
    private static SharedPreferences mSharedPrefs;

    Preferences() {
    }

    static {
        mSharedPrefs = null;
    }

    static SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPrefs == null) {
            mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mSharedPrefs;
    }

    public static void setMediatorArguments(Context context, String mediator, String arguments) {
        if (Diagnostics.getInstance().isEnabled(4)) {
            Diagnostics.m1952e(TAG, "mediator=" + mediator);
            Diagnostics.m1952e(TAG, "arguments=" + arguments);
        }
        if (context != null) {
            Editor editor = getSharedPreferences(context).edit();
            editor.putString("adtarget-mediator-" + mediator, arguments);
            SharedPreferencesCompat.apply(editor);
        }
    }

    public static HashMap<String, String> getMediatorArguments(Context context, String mediator) {
        HashMap<String, String> arguments = new HashMap();
        if (context != null) {
            String args = getSharedPreferences(context).getString("adtarget-mediator-" + mediator, Stomp.EMPTY);
            if (args.length() > 0) {
                for (String a : args.split(Headers.SEPERATOR)) {
                    int idx = a.indexOf(61);
                    if (idx != -1) {
                        arguments.put(a.substring(0, idx), a.substring(idx + 1));
                    }
                }
            }
        }
        return arguments;
    }

    public static String getEnvPref(Context context) {
        if (context != null) {
            return getSimplePref(context, "1l-ad-env", "prod");
        }
        return "prod";
    }

    public static boolean isStageEnv(Context context) {
        return getEnvPref(context).equals("stage");
    }

    public static void setEnvPrefStage(Context context) {
        setSimplePref(context, "1l-ad-env", "stage");
    }

    public static boolean isDevEnv(Context context) {
        return getEnvPref(context).equals("dev");
    }

    public static void setEnvPrefDev(Context context) {
        setSimplePref(context, "1l-ad-env", "dev");
    }

    public static boolean isQaEnv(Context context) {
        return getEnvPref(context).equals("qa");
    }

    public static void setEnvPrefQa(Context context) {
        setSimplePref(context, "1l-ad-env", "qa");
    }

    public static boolean isProdEnv(Context context) {
        return getEnvPref(context).equals("prod");
    }

    public static void setEnvPrefProd(Context context) {
        setSimplePref(context, "1l-ad-env", "prod");
    }

    public static void setAdTargetingInterval(Context context, int interval) {
        if (Diagnostics.getInstance().isEnabled(4)) {
            Diagnostics.m1952e(TAG, "interval=" + interval);
        }
        if (context != null) {
            Editor editor = getSharedPreferences(context).edit();
            editor.putInt("adtarget-interval", interval);
            SharedPreferencesCompat.apply(editor);
        }
    }

    public static void setAdTargetingUpdate(Context context, long dt) {
        if (Diagnostics.getInstance().isEnabled(4)) {
            Diagnostics.m1952e(TAG, "update=" + dt);
        }
        if (context != null) {
            if (dt == 0) {
                int interval = getSharedPreferences(context).getInt("adtarget-interval", 0);
                if (interval != 0) {
                    dt = System.currentTimeMillis() + ((long) interval);
                } else {
                    return;
                }
            }
            Editor editor = getSharedPreferences(context).edit();
            editor.putLong("adtarget-update", dt);
            SharedPreferencesCompat.apply(editor);
        }
    }

    public static long getAdTargetingUpdate(Context context) {
        if (context != null) {
            return getSharedPreferences(context).getLong("adtarget-update", 0);
        }
        return 0;
    }

    public static void setMobileConsumerId(Context context, String id) {
        if (Diagnostics.getInstance().isEnabled(4)) {
            Diagnostics.m1952e(TAG, "mcid=" + id);
        }
        if (context != null) {
            Editor editor = getSharedPreferences(context).edit();
            if (!id.equals(getMobileConsumerId(context))) {
                editor.putString("etag", Stomp.EMPTY);
            }
            editor.putString("mcid", id);
            SharedPreferencesCompat.apply(editor);
        }
    }

    public static String getMobileConsumerId(Context context) {
        if (context != null) {
            return getSharedPreferences(context).getString("mcid", Stomp.EMPTY);
        }
        return Stomp.EMPTY;
    }

    public static void setMobileConsumerEtag(Context context, String tag) {
        if (Diagnostics.getInstance().isEnabled(4)) {
            Diagnostics.m1952e(TAG, "etag=" + tag);
        }
        if (context != null) {
            Editor editor = getSharedPreferences(context).edit();
            editor.putString("etag", tag);
            SharedPreferencesCompat.apply(editor);
        }
    }

    public static String getMobileConsumerEtag(Context context) {
        if (context != null) {
            return getSharedPreferences(context).getString("etag", Stomp.EMPTY);
        }
        return Stomp.EMPTY;
    }

    public static void setSimplePref(Context context, String pref_name, long value) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putLong(pref_name, value);
        SharedPreferencesCompat.apply(editor);
    }

    public static void setSimplePref(Context context, String pref_name, int value) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putInt(pref_name, value);
        SharedPreferencesCompat.apply(editor);
    }

    public static void setSimplePref(Context context, String pref_name, String value) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(pref_name, value);
        SharedPreferencesCompat.apply(editor);
    }

    public static void setSimplePref(Context context, String pref_name, boolean value) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(pref_name, value);
        SharedPreferencesCompat.apply(editor);
    }

    public static String getSimplePref(Context context, String pref_name, String defaultVal) {
        return getSharedPreferences(context).getString(pref_name, defaultVal);
    }

    public static boolean getSimplePref(Context context, String pref_name, boolean defaultVal) {
        return getSharedPreferences(context).getBoolean(pref_name, defaultVal);
    }

    public static long getSimplePref(Context context, String pref_name, long defaultVal) {
        long value = defaultVal;
        try {
            value = getSharedPreferences(context).getLong(pref_name, defaultVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int getSimplePref(Context context, String pref_name, int defaultVal) {
        int value = defaultVal;
        try {
            value = getSharedPreferences(context).getInt(pref_name, defaultVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
