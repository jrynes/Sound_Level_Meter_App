package com.admarvel.android.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.admarvel.android.ads.AdMarvelInternalWebView;
import com.admarvel.android.ads.Utils;
import java.lang.ref.WeakReference;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.admarvel.android.util.b */
public class AdMarvelConnectivityChangeReceiver extends BroadcastReceiver {
    private static String f975a;
    private static WeakReference<AdMarvelInternalWebView> f976b;
    private static AdMarvelConnectivityChangeReceiver f977c;

    static {
        f975a = null;
        f977c = null;
    }

    private AdMarvelConnectivityChangeReceiver() {
    }

    public static AdMarvelConnectivityChangeReceiver m559a() {
        if (f977c == null) {
            f977c = new AdMarvelConnectivityChangeReceiver();
        }
        return f977c;
    }

    public static void m560a(AdMarvelInternalWebView adMarvelInternalWebView, String str) {
        f976b = new WeakReference(adMarvelInternalWebView);
        if (str != null) {
            f975a = str;
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action != null && action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            try {
                Object valueOf;
                String a = Utils.m181a(context);
                Boolean valueOf2 = Boolean.valueOf(false);
                if (a.equals("mobile") || a.equals("wifi")) {
                    valueOf = Boolean.valueOf(true);
                } else {
                    Boolean bool = valueOf2;
                }
                if (f976b != null) {
                    AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) f976b.get();
                    if (adMarvelInternalWebView != null && f975a != null) {
                        adMarvelInternalWebView.m315e(f975a + "(" + "'" + valueOf + "'" + Stomp.COMMA + "'" + a + "'" + ")");
                    }
                }
            } catch (Exception e) {
                Logging.log(e.getMessage() + " Exception in AdMarvelConnectivityChangeReceiver ");
            }
        }
    }
}
