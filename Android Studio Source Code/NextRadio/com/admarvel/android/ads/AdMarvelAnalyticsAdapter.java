package com.admarvel.android.ads;

import android.content.Context;
import com.admarvel.android.util.Logging;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class AdMarvelAnalyticsAdapter {
    protected AdMarvelAnalyticsAdapter(Context context) {
    }

    protected static AdMarvelAnalyticsAdapter createInstance(String className, Context context) {
        return (AdMarvelAnalyticsAdapter) createObject(className, context);
    }

    private static Object createObject(String className, Context context) {
        Object obj = null;
        try {
            Constructor declaredConstructor = Class.forName(className).getDeclaredConstructor(new Class[]{Context.class});
            declaredConstructor.setAccessible(true);
            obj = declaredConstructor.newInstance(new Object[]{context});
        } catch (InstantiationException e) {
            Logging.log(e.getMessage());
        } catch (IllegalAccessException e2) {
            Logging.log(e2.getMessage());
        } catch (ClassNotFoundException e3) {
            Logging.log(e3.getMessage());
        } catch (NoSuchMethodException e4) {
            Logging.log(e4.getMessage());
        } catch (IllegalArgumentException e5) {
            Logging.log(e5.getMessage());
        } catch (InvocationTargetException e6) {
            Logging.log(e6.getMessage());
        }
        return obj;
    }

    public abstract void enableAppInstallCheck(boolean z);

    public abstract String getAdapterAnalyticsVersion();

    public abstract String getAdapterAnalyticsVersionNumber();

    public abstract Map<String, Object> getEnhancedTargetParams(String str, Map<String, Object> map);

    public abstract void onAdClick(String str, int i, Map<String, Object> map, String str2, String str3);

    public abstract void onFailedToReceiveAd(String str, int i, Map<String, Object> map, String str2);

    public abstract void onReceiveAd(String str, int i, Map<String, Object> map, String str2);

    public abstract void pause();

    public abstract void resume();

    public abstract void start();

    public abstract void stop();
}
