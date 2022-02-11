package com.admarvel.android.util;

import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import org.apache.activemq.transport.stomp.Stomp;

public class OptionalUtils {
    public static String getId(Context context) {
        Class cls;
        Class cls2;
        IllegalAccessException illegalAccessException;
        Class cls3 = null;
        int i = 0;
        Class[] clsArr = new Class[]{Context.class};
        String str = Stomp.EMPTY;
        try {
            cls = Class.forName("com.admarvel.androidid.FetchAndroidId");
            try {
                Object newInstance = cls.newInstance();
                i = 1;
            } catch (ClassNotFoundException e) {
            } catch (InstantiationException e2) {
            } catch (IllegalAccessException e3) {
                IllegalAccessException illegalAccessException2 = e3;
                cls2 = cls;
                illegalAccessException = illegalAccessException2;
                illegalAccessException.printStackTrace();
                cls = cls2;
                i = 1;
            }
        } catch (ClassNotFoundException e4) {
            cls = cls3;
        } catch (InstantiationException e5) {
            cls = cls3;
        } catch (IllegalAccessException e6) {
            illegalAccessException = e6;
            cls2 = cls3;
            illegalAccessException.printStackTrace();
            cls = cls2;
            i = 1;
        }
        if (i != 0) {
            try {
                return (String) cls.getDeclaredMethod("getId", clsArr).invoke(newInstance, new Object[]{context});
            } catch (NoSuchMethodException e7) {
                e7.printStackTrace();
                return str;
            } catch (IllegalArgumentException e8) {
                e8.printStackTrace();
                return str;
            } catch (IllegalAccessException illegalAccessException3) {
                illegalAccessException3.printStackTrace();
                return str;
            } catch (InvocationTargetException e9) {
                e9.printStackTrace();
            }
        }
        return str;
    }
}
