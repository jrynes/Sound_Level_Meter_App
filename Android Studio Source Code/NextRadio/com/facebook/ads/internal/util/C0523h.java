package com.facebook.ads.internal.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.internal.C0469e;
import java.lang.reflect.Constructor;

/* renamed from: com.facebook.ads.internal.util.h */
public class C0523h {
    private static String f1915a;

    static {
        f1915a = null;
    }

    public static String m1543a() {
        if (C0536s.m1572a(AdSettings.getUrlPrefix())) {
            return "https://www.facebook.com/";
        }
        return String.format("https://www.%s.facebook.com", new Object[]{AdSettings.getUrlPrefix()});
    }

    @TargetApi(17)
    private static String m1544a(Context context) {
        return WebSettings.getDefaultUserAgent(context);
    }

    public static String m1545a(Context context, C0469e c0469e) {
        if (c0469e == C0469e.NATIVE_250 || c0469e == C0469e.NATIVE_UNKNOWN || c0469e == null) {
            return System.getProperty("http.agent");
        }
        if (f1915a == null) {
            if (VERSION.SDK_INT >= 17) {
                try {
                    f1915a = C0523h.m1544a(context);
                    return f1915a;
                } catch (Exception e) {
                }
            }
            try {
                f1915a = C0523h.m1546a(context, "android.webkit.WebSettings", "android.webkit.WebView");
            } catch (Exception e2) {
                try {
                    f1915a = C0523h.m1546a(context, "android.webkit.WebSettingsClassic", "android.webkit.WebViewClassic");
                } catch (Exception e3) {
                    WebView webView = new WebView(context.getApplicationContext());
                    f1915a = webView.getSettings().getUserAgentString();
                    webView.destroy();
                }
            }
        }
        return f1915a;
    }

    private static String m1546a(Context context, String str, String str2) {
        Class cls = Class.forName(str);
        Constructor declaredConstructor = cls.getDeclaredConstructor(new Class[]{Context.class, Class.forName(str2)});
        declaredConstructor.setAccessible(true);
        try {
            String str3 = (String) cls.getMethod("getUserAgentString", new Class[0]).invoke(declaredConstructor.newInstance(new Object[]{context, null}), new Object[0]);
            return str3;
        } finally {
            declaredConstructor.setAccessible(false);
        }
    }

    public static void m1547a(WebView webView) {
        webView.loadUrl("about:blank");
        webView.clearCache(true);
        if (VERSION.SDK_INT > 11) {
            webView.onPause();
            return;
        }
        try {
            WebView.class.getMethod("onPause", new Class[0]).invoke(webView, new Object[0]);
        } catch (Exception e) {
        }
    }

    @TargetApi(21)
    public static void m1548b(WebView webView) {
        WebSettings settings = webView.getSettings();
        if (VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(0);
            return;
        }
        try {
            WebSettings.class.getMethod("setMixedContentMode", new Class[0]).invoke(settings, new Object[]{Integer.valueOf(0)});
        } catch (Exception e) {
        }
    }
}
