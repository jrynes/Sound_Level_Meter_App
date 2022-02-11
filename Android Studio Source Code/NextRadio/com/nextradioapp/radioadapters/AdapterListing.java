package com.nextradioapp.radioadapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.nextradioapp.nextradio.services.RadioAdapterService;
import java.lang.reflect.InvocationTargetException;
import org.nablabs.libFmRadioImpl.FmRadio;

public class AdapterListing {
    private static final String TAG = "AdapterListing";
    private static IFmRadio instance;

    public static IFmRadio getFMRadioImplementation(Context context) {
        String build_device = Build.DEVICE.toLowerCase();
        Class c = (Class) getFMRadioImplementationClass(context);
        if (instance != null && (c == FmRadioHTC.class || c == FmRadioMotorola.class || build_device.equals("stormer"))) {
            return instance;
        }
        try {
            instance = (IFmRadio) c.getConstructor(new Class[0]).newInstance(new Object[0]);
            return instance;
        } catch (NoSuchMethodException e1) {
            Crashlytics.logException(e1);
            return null;
        } catch (IllegalArgumentException e) {
            Crashlytics.logException(e);
            return null;
        } catch (InstantiationException e2) {
            Crashlytics.logException(e2);
            return null;
        } catch (IllegalAccessException e3) {
            Crashlytics.logException(e3);
            return null;
        } catch (InvocationTargetException e4) {
            Crashlytics.logException(e4);
            return null;
        }
    }

    public static IFmRadio getEmulatedFMRadioImplementation(Context context) {
        try {
            instance = (IFmRadio) FmRadioEmulated.class.getConstructor(new Class[0]).newInstance(new Object[0]);
            return instance;
        } catch (Exception e) {
            Log.d(TAG, "Exception while building Emulated radio");
            return null;
        }
    }

    public static Object getFMRadioImplementationClass(Context context) {
        String build_device = Build.DEVICE.toLowerCase();
        Log.i(TAG, "Device: " + build_device);
        String deviceMan = Build.MANUFACTURER.toLowerCase();
        try {
            Class.forName("org.nablabs.libFmRadioImpl.FmRadio");
            return FmRadioFramework.class;
        } catch (ClassNotFoundException e) {
            for (String device : context.getResources().getStringArray(2131230720)) {
                if (build_device.matches(device)) {
                    Log.i(RadioAdapterService.TAG, "HTC FmRadio implementation found for device:" + build_device);
                    return FmRadioHTC.class;
                }
            }
            if (FmRadioMotorola.checkForLibrary(context)) {
                Log.i(RadioAdapterService.TAG, "Motorola FmRadio implementation found for device:" + build_device);
                return FmRadioMotorola.class;
            } else if (FMRadioBlu.checkForLibrary(context)) {
                Log.i(RadioAdapterService.TAG, "Blu FmRadio implementation found for device:" + build_device);
                return FMRadioBlu.class;
            } else {
                Log.e(RadioAdapterService.TAG, "FmRadio implementation not found for device:" + build_device);
                return FmRadioEmulated.class;
            }
        }
    }

    public static Object getFMRadioImplementationClass(Context context, boolean isFmradio) {
        String build_device = Build.DEVICE.toLowerCase();
        Log.i(TAG, "BRAND: " + Build.BRAND);
        try {
            Class.forName("org.nablabs.libFmRadioImpl.FmRadio");
            if (Build.BRAND.equalsIgnoreCase("samsung")) {
                try {
                    new FmRadio().init(context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "First FmRadio:" + e.getMessage());
                    return FmRadioEmulated.class;
                }
            }
            return FmRadioFramework.class;
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            for (String device : context.getResources().getStringArray(2131230720)) {
                if (build_device.matches(device)) {
                    Log.i(TAG, "HTC FmRadio implementation found for device:" + build_device);
                    return FmRadioHTC.class;
                }
            }
            if (FmRadioMotorola.checkForLibrary(context)) {
                Log.i(TAG, "Motorola FmRadio implementation found for device:" + build_device);
                return FmRadioMotorola.class;
            } else if (FMRadioBlu.checkForLibrary(context)) {
                Log.i(TAG, "Blu FmRadio implementation found for device:" + build_device);
                return FMRadioBlu.class;
            } else {
                Log.e(TAG, "FmRadio implementation not found for device:" + build_device);
                return FmRadioEmulated.class;
            }
        }
    }

    public static String getFMRadioImplementationString(Context context) {
        String FmAPI = "none";
        Class FMAPIClass = getFMRadioImplementationClass(context);
        if (FMAPIClass == FmRadioFramework.class) {
            return "nab_framework_1.0";
        }
        if (FMAPIClass == FmRadioHTC.class) {
            return "htc_1.0";
        }
        if (FMAPIClass == FmRadioMotorola.class) {
            return "motorola_1.0";
        }
        if (FMAPIClass == FMRadioBlu.class) {
            return "blu_1.0";
        }
        return FmAPI;
    }
}
