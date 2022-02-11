package com.amazon.device.associates;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.activemq.jndi.ReadOnlyContext;

/* compiled from: SharedData */
class bp {
    public static final DateFormat f1275a;
    public static String f1276b;
    private static String f1277c;
    private static boolean f1278d;
    private static Context f1279e;

    bp() {
    }

    static {
        f1275a = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        f1277c = "AmazonMobileAssociates";
        f1278d = false;
    }

    public static void m900a(Context context, String str) {
        f1279e = context;
        f1276b = context.getFilesDir().getAbsolutePath() + ReadOnlyContext.SEPARATOR;
        if (!(m901b() == null || m901b().equals(str))) {
            ab.m699b(f1276b + "rut.db");
        }
        Editor edit = context.getSharedPreferences(f1277c, 0).edit();
        edit.putString("amzn-ad-app-id", str);
        edit.commit();
        f1278d = true;
    }

    protected static Context m899a() {
        return f1279e;
    }

    protected static String m901b() {
        return f1279e.getSharedPreferences(f1277c, 0).getString("amzn-ad-app-id", null);
    }

    protected static int m898a(String str, String str2) {
        return m899a().getResources().getIdentifier(str, str2, m899a().getPackageName());
    }
}
