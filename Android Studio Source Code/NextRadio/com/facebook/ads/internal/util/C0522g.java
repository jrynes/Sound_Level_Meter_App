package com.facebook.ads.internal.util;

import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import com.admarvel.android.ads.Constants;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.C0469e;
import com.mixpanel.android.java_websocket.WebSocket;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.util.g */
public class C0522g {
    private static final Uri f1913a;
    private static final String f1914b;

    /* renamed from: com.facebook.ads.internal.util.g.1 */
    static /* synthetic */ class C05201 {
        static final /* synthetic */ int[] f1909a;

        static {
            f1909a = new int[AdSize.values().length];
            try {
                f1909a[AdSize.INTERSTITIAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1909a[AdSize.RECTANGLE_HEIGHT_250.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1909a[AdSize.BANNER_HEIGHT_90.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1909a[AdSize.BANNER_HEIGHT_50.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.util.g.a */
    public static class C0521a {
        public String f1910a;
        public String f1911b;
        public boolean f1912c;

        public C0521a(String str, String str2, boolean z) {
            this.f1910a = str;
            this.f1911b = str2;
            this.f1912c = z;
        }
    }

    static {
        f1913a = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
        f1914b = C0522g.class.getSimpleName();
    }

    public static C0469e m1527a(AdSize adSize) {
        switch (C05201.f1909a[adSize.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                return C0469e.WEBVIEW_INTERSTITIAL_UNKNOWN;
            case Zone.SECONDARY /*2*/:
                return C0469e.WEBVIEW_BANNER_250;
            case Protocol.GGP /*3*/:
                return C0469e.WEBVIEW_BANNER_90;
            case Type.MF /*4*/:
                return C0469e.WEBVIEW_BANNER_50;
            default:
                return C0469e.WEBVIEW_BANNER_LEGACY;
        }
    }

    public static C0521a m1528a(ContentResolver contentResolver) {
        Cursor query;
        C0521a c0521a;
        Throwable th;
        try {
            ContentResolver contentResolver2 = contentResolver;
            query = contentResolver2.query(f1913a, new String[]{"aid", "androidid", "limit_tracking"}, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        c0521a = new C0521a(query.getString(query.getColumnIndex("aid")), query.getString(query.getColumnIndex("androidid")), Boolean.valueOf(query.getString(query.getColumnIndex("limit_tracking"))).booleanValue());
                        if (query != null) {
                            query.close();
                        }
                        return c0521a;
                    }
                } catch (Exception e) {
                    try {
                        c0521a = new C0521a(null, null, false);
                        if (query != null) {
                            query.close();
                        }
                        return c0521a;
                    } catch (Throwable th2) {
                        th = th2;
                        if (query != null) {
                            query.close();
                        }
                        throw th;
                    }
                }
            }
            c0521a = new C0521a(null, null, false);
            if (query != null) {
                query.close();
            }
        } catch (Exception e2) {
            query = null;
            c0521a = new C0521a(null, null, false);
            if (query != null) {
                query.close();
            }
            return c0521a;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return c0521a;
    }

    public static Object m1529a(Object obj, Method method, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (Exception e) {
            return null;
        }
    }

    public static String m1530a(InputStream inputStream) {
        StringWriter stringWriter = new StringWriter();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        char[] cArr = new char[Flags.EXTEND];
        while (true) {
            int read = inputStreamReader.read(cArr);
            if (read != -1) {
                stringWriter.write(cArr, 0, read);
            } else {
                String stringWriter2 = stringWriter.toString();
                stringWriter.close();
                inputStreamReader.close();
                return stringWriter2;
            }
        }
    }

    public static String m1531a(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        for (Entry entry : map.entrySet()) {
            try {
                jSONObject.put((String) entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject.toString();
    }

    public static String m1532a(byte[] bArr) {
        try {
            InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            InputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            String a = C0522g.m1530a(gZIPInputStream);
            gZIPInputStream.close();
            byteArrayInputStream.close();
            return a;
        } catch (Throwable e) {
            C0515c.m1515a(C0514b.m1512a(e, "Error decompressing data"));
            e.printStackTrace();
            return Stomp.EMPTY;
        }
    }

    public static Method m1533a(Class<?> cls, String str, Class<?>... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method m1534a(String str, String str2, Class<?>... clsArr) {
        try {
            return C0522g.m1533a(Class.forName(str), str2, (Class[]) clsArr);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static void m1535a(Context context, String str) {
        if (AdSettings.isTestMode(context)) {
            Log.d("FBAudienceNetworkLog", str + " (displayed for test ads only)");
        }
    }

    public static void m1536a(DisplayMetrics displayMetrics, View view, AdSize adSize) {
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(((int) (((float) displayMetrics.widthPixels) / displayMetrics.density)) >= adSize.getWidth() ? displayMetrics.widthPixels : (int) Math.ceil((double) (((float) adSize.getWidth()) * displayMetrics.density)), (int) Math.ceil((double) (((float) adSize.getHeight()) * displayMetrics.density)));
        layoutParams.addRule(14, -1);
        view.setLayoutParams(layoutParams);
    }

    public static void m1537a(View view, boolean z, String str) {
    }

    public static boolean m1538a() {
        String urlPrefix = AdSettings.getUrlPrefix();
        return !C0536s.m1572a(urlPrefix) && urlPrefix.endsWith(".sb");
    }

    public static boolean m1539a(Context context) {
        try {
            return !((PowerManager) context.getSystemService("power")).isScreenOn() ? false : !((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean m1540a(Context context, View view, int i) {
        if (view == null) {
            C0522g.m1537a(view, false, "adView is null.");
            return false;
        } else if (view.getParent() == null) {
            C0522g.m1537a(view, false, "adView has no parent.");
            return false;
        } else if (view.getVisibility() != 0) {
            C0522g.m1537a(view, false, "adView is not set to VISIBLE.");
            return false;
        } else if (VERSION.SDK_INT < 11 || view.getAlpha() >= 0.9f) {
            int width = view.getWidth();
            int height = view.getHeight();
            int[] iArr = new int[2];
            try {
                view.getLocationOnScreen(iArr);
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                if (iArr[0] < 0 || displayMetrics.widthPixels - iArr[0] < width) {
                    C0522g.m1537a(view, false, "adView is not fully on screen horizontally.");
                    return false;
                }
                width = (int) ((((double) height) * (100.0d - ((double) i))) / 100.0d);
                if (iArr[1] < 0 && Math.abs(iArr[1]) > width) {
                    C0522g.m1537a(view, false, "adView is not visible from the top.");
                    return false;
                } else if ((height + iArr[1]) - displayMetrics.heightPixels > width) {
                    C0522g.m1537a(view, false, "adView is not visible from the bottom.");
                    return false;
                } else {
                    C0522g.m1537a(view, true, "adView is visible.");
                    return C0522g.m1539a(context);
                }
            } catch (NullPointerException e) {
                C0522g.m1537a(view, false, "Cannot get location on screen.");
                return false;
            }
        } else {
            C0522g.m1537a(view, false, "adView is too transparent.");
            return false;
        }
    }

    public static byte[] m1541a(String str) {
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream(str.length());
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes());
            gZIPOutputStream.close();
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return toByteArray;
        } catch (Throwable e) {
            C0515c.m1515a(C0514b.m1512a(e, "Error compressing data"));
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static HttpClient m1542b() {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpParams params = defaultHttpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, Constants.LOADING_INTERVAL);
        HttpConnectionParams.setSoTimeout(params, Constants.LOADING_INTERVAL);
        if (C0522g.m1538a()) {
            try {
                KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
                instance.load(null, null);
                SocketFactory c0534q = new C0534q(instance);
                c0534q.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HttpRequest.CHARSET_UTF8);
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                schemeRegistry.register(new Scheme("https", c0534q, WebSocket.DEFAULT_WSS_PORT));
                return new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
            } catch (Exception e) {
            }
        }
        return defaultHttpClient;
    }
}
