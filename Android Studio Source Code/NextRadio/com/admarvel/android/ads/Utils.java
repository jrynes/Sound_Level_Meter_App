package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.util.AdMarvelLocationManager;
import com.admarvel.android.util.AdMarvelSensorManager;
import com.admarvel.android.util.AdMarvelThreadExecutorService;
import com.admarvel.android.util.Base64;
import com.admarvel.android.util.Logging;
import com.facebook.ads.AdError;
import com.nextradioapp.androidSDK.data.schema.Tables.activityEvents;
import com.nextradioapp.androidSDK.data.schema.Tables.stations;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQConnection;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class Utils {
    public static String f493a;
    static boolean f494b;
    public static boolean f495c;
    private WeakReference<Context> f496d;
    private String f497e;

    /* renamed from: com.admarvel.android.ads.Utils.1 */
    class C02271 extends WebViewClient {
        final /* synthetic */ Utils f433a;

        C02271(Utils utils) {
            this.f433a = utils;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logging.log("Pixel successfully fired.");
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.2 */
    class C02292 implements Runnable {
        final /* synthetic */ Utils f435a;

        /* renamed from: com.admarvel.android.ads.Utils.2.1 */
        class C02281 extends WebViewClient {
            final /* synthetic */ C02292 f434a;

            C02281(C02292 c02292) {
                this.f434a = c02292;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logging.log("Pixel successfully fired.");
            }
        }

        C02292(Utils utils) {
            this.f435a = utils;
        }

        public void run() {
            if (this.f435a.f496d != null) {
                Context context = (Context) this.f435a.f496d.get();
                if (context != null) {
                    WebView webView = new WebView(context);
                    webView.setWebViewClient(new C02281(this));
                    webView.loadDataWithBaseURL(null, this.f435a.f497e, WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.3 */
    static class C02303 implements Runnable {
        final /* synthetic */ File f436a;

        C02303(File file) {
            this.f436a = file;
        }

        public void run() {
            try {
                for (File delete : this.f436a.listFiles()) {
                    delete.delete();
                }
                this.f436a.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.4 */
    static /* synthetic */ class C02314 {
        static final /* synthetic */ int[] f437a;

        static {
            f437a = new int[ErrorReason.values().length];
            try {
                f437a[ErrorReason.SITE_ID_OR_PARTNER_ID_NOT_PRESENT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f437a[ErrorReason.SITE_ID_AND_PARTNER_ID_DO_NOT_MATCH.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f437a[ErrorReason.BOT_USER_AGENT_FOUND.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f437a[ErrorReason.NO_BANNER_FOUND.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f437a[ErrorReason.NO_AD_FOUND.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f437a[ErrorReason.NO_USER_AGENT_FOUND.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f437a[ErrorReason.SITE_ID_NOT_PRESENT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f437a[ErrorReason.PARTNER_ID_NOT_PRESENT.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f437a[ErrorReason.NO_NETWORK_CONNECTIVITY.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f437a[ErrorReason.NETWORK_CONNECTIVITY_DISRUPTED.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                f437a[ErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                f437a[ErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                f437a[ErrorReason.AD_UNIT_NOT_ABLE_TO_RENDER.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                f437a[ErrorReason.AD_REQUEST_MISSING_XML_ELEMENTS.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                f437a[ErrorReason.AD_REQUEST_SDK_TYPE_UNSUPPORTED.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                f437a[ErrorReason.AD_UNIT_NOT_ABLE_TO_LOAD.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                f437a[ErrorReason.AD_UNIT_IN_DISPLAY_STATE.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.a */
    private static class C0232a implements Runnable {
        private C0232a() {
        }

        public void run() {
            new AdMarvelInstallTrackerCleanupAsyncTask().execute(new Object[]{null});
        }
    }

    @SuppressLint({"NewApi"})
    /* renamed from: com.admarvel.android.ads.Utils.b */
    private static class C0233b implements Runnable {
        private C0233b() {
        }

        public void run() {
            new AdMarvelInstallTrackerCleanupAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[]{null});
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.c */
    static class C0234c implements Runnable {
        private final WeakReference<AdMarvelInternalWebView> f438a;
        private final String f439b;

        public C0234c(AdMarvelInternalWebView adMarvelInternalWebView, String str) {
            this.f438a = new WeakReference(adMarvelInternalWebView);
            this.f439b = str;
        }

        public void run() {
            try {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f438a.get();
                if (adMarvelInternalWebView != null) {
                    if (Utils.m220l(adMarvelInternalWebView.getContext())) {
                        adMarvelInternalWebView.m315e(this.f439b + "(\"YES\")");
                    } else {
                        adMarvelInternalWebView.m315e(this.f439b + "(\"NO\")");
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.d */
    static class C0235d implements Runnable {
        SimpleDateFormat f440a;
        Long f441b;
        Long f442c;
        String f443d;
        String f444e;
        String f445f;
        String f446g;
        String f447h;
        String f448i;
        int f449j;
        private final WeakReference<Context> f450k;
        private final WeakReference<AdMarvelInternalWebView> f451l;

        public C0235d(AdMarvelInternalWebView adMarvelInternalWebView, Context context, String str, String str2, String str3) {
            this.f440a = new SimpleDateFormat("yyyyMMddhhmm");
            this.f441b = null;
            this.f442c = null;
            this.f449j = 1;
            this.f450k = new WeakReference(context);
            this.f451l = new WeakReference(adMarvelInternalWebView);
            if (str != null) {
                try {
                    this.f441b = Long.valueOf(this.f440a.parse(str).getTime());
                    this.f442c = Long.valueOf(this.f440a.parse(str).getTime() + 3600000);
                } catch (Throwable e) {
                    Logging.log(Log.getStackTraceString(e));
                    return;
                }
            }
            this.f443d = str2;
            this.f444e = str3;
        }

        public C0235d(AdMarvelInternalWebView adMarvelInternalWebView, Context context, String str, String str2, String str3, String str4, String str5, String str6, int i) {
            this.f440a = new SimpleDateFormat("yyyyMMddhhmm");
            this.f441b = null;
            this.f442c = null;
            this.f449j = 1;
            this.f450k = new WeakReference(context);
            this.f451l = new WeakReference(adMarvelInternalWebView);
            if (str != null) {
                try {
                    this.f441b = Long.valueOf(this.f440a.parse(str).getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (str6 != null) {
                this.f442c = Long.valueOf(this.f440a.parse(str6).getTime());
            } else {
                this.f442c = Long.valueOf(this.f440a.parse(str).getTime() + 3600000);
            }
            this.f443d = str2;
            this.f444e = str3;
            this.f445f = str4;
            this.f446g = str5;
            if (i <= 0) {
                this.f449j = i / 60;
            }
        }

        public C0235d(AdMarvelInternalWebView adMarvelInternalWebView, Context context, String str, String str2, String str3, String str4, String str5, String str6, int i, String str7, String str8, String str9, String str10, int i2, int i3, String str11) {
            this.f440a = new SimpleDateFormat("yyyyMMddhhmm");
            this.f441b = null;
            this.f442c = null;
            this.f449j = 1;
            this.f450k = new WeakReference(context);
            this.f451l = new WeakReference(adMarvelInternalWebView);
            if (str != null) {
                try {
                    this.f441b = Long.valueOf(this.f440a.parse(str).getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (str6 != null) {
                this.f442c = Long.valueOf(this.f440a.parse(str6).getTime());
            } else {
                this.f442c = Long.valueOf(this.f440a.parse(str).getTime() + 3600000);
            }
            this.f443d = str2;
            this.f444e = str3;
            this.f445f = str4;
            this.f446g = str5;
            if (i <= 0) {
                this.f449j = i / 60;
            }
            if (str7 != null && str7.length() > 0) {
                this.f447h = str7;
            }
            this.f448i = str11;
        }

        String m161a() {
            return Version.getAndroidSDKVersion() >= 8 ? "content://com.android.calendar/" : "content://calendar/";
        }

        public void run() {
            Cursor query;
            Exception e;
            Throwable th;
            Context context = (Context) this.f450k.get();
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f451l.get();
            if (context != null && adMarvelInternalWebView != null) {
                ContentResolver contentResolver = context.getContentResolver();
                try {
                    query = contentResolver.query(Uri.parse(m161a() + "calendars"), new String[]{stations._id, "displayname"}, null, null, null);
                    if (query != null) {
                        try {
                            if (query.moveToFirst()) {
                                String[] strArr = new String[query.getCount()];
                                int[] iArr = new int[query.getCount()];
                                for (int i = 0; i < strArr.length; i++) {
                                    iArr[i] = query.getInt(0);
                                    strArr[i] = query.getString(1);
                                    query.moveToNext();
                                }
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("calendar_id", Integer.valueOf(iArr[0]));
                                if (this.f443d != null) {
                                    contentValues.put(SettingsJsonConstants.PROMPT_TITLE_KEY, this.f443d);
                                }
                                if (this.f444e != null) {
                                    contentValues.put(activityEvents.description, this.f444e);
                                }
                                if (this.f445f != null) {
                                    contentValues.put("eventLocation", this.f445f);
                                }
                                if (this.f441b != null) {
                                    contentValues.put("dtstart", this.f441b);
                                }
                                if (this.f442c != null) {
                                    contentValues.put("dtend", this.f442c);
                                }
                                if (this.f446g != null && (this.f446g.toLowerCase().equals(Stomp.TRUE) || this.f446g.toLowerCase().equals("yes"))) {
                                    contentValues.put("allDay", Integer.valueOf(1));
                                }
                                if (this.f449j <= 0) {
                                    contentValues.put("hasAlarm", Integer.valueOf(1));
                                }
                                if (this.f447h != null && this.f447h.length() > 0) {
                                    contentValues.put("eventTimezone", this.f447h);
                                }
                                Uri parse = Uri.parse(m161a() + "events");
                                Uri parse2 = Uri.parse(m161a() + "reminders");
                                Uri insert = contentResolver.insert(parse, contentValues);
                                if (insert != null) {
                                    if (this.f449j <= 0) {
                                        ContentValues contentValues2 = new ContentValues();
                                        contentValues2.put("event_id", Long.valueOf(Long.parseLong(insert.getLastPathSegment())));
                                        contentValues2.put("method", Integer.valueOf(1));
                                        contentValues2.put("minutes", Integer.valueOf(Math.abs(this.f449j)));
                                        contentResolver.insert(parse2, contentValues2);
                                    }
                                    if (this.f448i != null && this.f448i.length() > 0) {
                                        adMarvelInternalWebView.m315e(this.f448i + "(\"YES\")");
                                    }
                                } else if (this.f448i != null && this.f448i.length() > 0) {
                                    adMarvelInternalWebView.m315e(this.f448i + "(\"NO\")");
                                }
                            }
                        } catch (Exception e2) {
                            e = e2;
                            try {
                                adMarvelInternalWebView.m315e(this.f448i + "(\"NO\")");
                                e.printStackTrace();
                                if (query != null) {
                                    query.close();
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (query != null) {
                                    query.close();
                                }
                                throw th;
                            }
                        }
                    }
                    if (query != null) {
                        query.close();
                    }
                } catch (Exception e3) {
                    e = e3;
                    query = null;
                    if (this.f448i != null && this.f448i.length() > 0) {
                        adMarvelInternalWebView.m315e(this.f448i + "(\"NO\")");
                    }
                    e.printStackTrace();
                    if (query != null) {
                        query.close();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    query = null;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.e */
    static class C0236e implements Runnable {
        SimpleDateFormat f452a;
        Long f453b;
        Long f454c;
        String f455d;
        String f456e;
        String f457f;
        String f458g;
        String f459h;
        String f460i;
        String f461j;
        String f462k;
        int f463l;
        int f464m;
        int f465n;
        private final WeakReference<Context> f466o;
        private final WeakReference<AdMarvelInternalWebView> f467p;

        public C0236e(AdMarvelInternalWebView adMarvelInternalWebView, Context context, String str, String str2, String str3) {
            this.f452a = new SimpleDateFormat("yyyyMMddhhmm");
            this.f453b = null;
            this.f454c = null;
            this.f463l = 1;
            this.f464m = 0;
            this.f465n = 0;
            this.f466o = new WeakReference(context);
            this.f467p = new WeakReference(adMarvelInternalWebView);
            if (str != null) {
                try {
                    this.f453b = Long.valueOf(this.f452a.parse(str).getTime());
                    this.f454c = Long.valueOf(this.f452a.parse(str).getTime() + 3600000);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            this.f455d = str2;
            this.f456e = str3;
        }

        public C0236e(AdMarvelInternalWebView adMarvelInternalWebView, Context context, String str, String str2, String str3, String str4, String str5, String str6, int i) {
            this.f452a = new SimpleDateFormat("yyyyMMddhhmm");
            this.f453b = null;
            this.f454c = null;
            this.f463l = 1;
            this.f464m = 0;
            this.f465n = 0;
            this.f466o = new WeakReference(context);
            this.f467p = new WeakReference(adMarvelInternalWebView);
            if (str != null) {
                try {
                    this.f453b = Long.valueOf(this.f452a.parse(str).getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (str6 != null) {
                this.f454c = Long.valueOf(this.f452a.parse(str6).getTime());
            } else {
                this.f454c = Long.valueOf(this.f452a.parse(str).getTime() + 3600000);
            }
            this.f455d = str2;
            this.f456e = str3;
            this.f457f = str4;
            this.f458g = str5;
            if (i <= 0) {
                this.f463l = i / 60;
            }
        }

        public C0236e(AdMarvelInternalWebView adMarvelInternalWebView, Context context, String str, String str2, String str3, String str4, String str5, String str6, int i, String str7, String str8, String str9, String str10, int i2, int i3, String str11) {
            this.f452a = new SimpleDateFormat("yyyyMMddhhmm");
            this.f453b = null;
            this.f454c = null;
            this.f463l = 1;
            this.f464m = 0;
            this.f465n = 0;
            this.f466o = new WeakReference(context);
            this.f467p = new WeakReference(adMarvelInternalWebView);
            if (str != null) {
                try {
                    this.f453b = Long.valueOf(this.f452a.parse(str).getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (str6 != null) {
                this.f454c = Long.valueOf(this.f452a.parse(str6).getTime());
            } else {
                this.f454c = Long.valueOf(this.f452a.parse(str).getTime() + 3600000);
            }
            this.f455d = str2;
            this.f456e = str3;
            this.f457f = str4;
            this.f458g = str5;
            if (i <= 0) {
                this.f463l = i / 60;
            }
            if (str7 != null && str7.length() > 0) {
                int i4 = 0;
                try {
                    String[] split = str7.split(Headers.SEPERATOR);
                    if (split.length == 2) {
                        if (split[0].startsWith("+")) {
                            i4 = (Integer.parseInt(split[0].substring(1)) * 60) + Integer.parseInt(split[1]);
                        } else if (split[0].startsWith("-")) {
                            i4 = ((Integer.parseInt(split[0].substring(1)) * 60) + Integer.parseInt(split[1])) * -1;
                        }
                    } else if (split.length == 1) {
                        if (split[0].startsWith("+")) {
                            i4 = Integer.parseInt(split[0].substring(1)) * 60;
                        } else if (split[0].startsWith("-")) {
                            i4 = (Integer.parseInt(split[0].substring(1)) * 60) * -1;
                        }
                    }
                    String[] availableIDs = TimeZone.getAvailableIDs((i4 * 60) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
                    if (availableIDs != null && availableIDs.length > 0) {
                        this.f459h = availableIDs[0];
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            this.f460i = str9;
            this.f461j = str10;
            this.f462k = str11;
            this.f464m = i2;
            this.f465n = i3;
        }

        @TargetApi(14)
        public void run() {
            Exception e;
            Throwable th;
            Context context = (Context) this.f466o.get();
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f467p.get();
            if (context != null && adMarvelInternalWebView != null) {
                ContentResolver contentResolver = context.getContentResolver();
                Cursor query;
                try {
                    query = contentResolver.query(Calendars.CONTENT_URI, new String[]{stations._id, "calendar_displayName"}, null, null, null);
                    if (query != null) {
                        try {
                            if (query.moveToFirst()) {
                                String[] strArr = new String[query.getCount()];
                                int[] iArr = new int[query.getCount()];
                                for (int i = 0; i < strArr.length; i++) {
                                    iArr[i] = query.getInt(0);
                                    strArr[i] = query.getString(1);
                                    query.moveToNext();
                                }
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("calendar_id", Integer.valueOf(iArr[0]));
                                if (this.f455d != null) {
                                    contentValues.put(SettingsJsonConstants.PROMPT_TITLE_KEY, this.f455d);
                                }
                                if (this.f456e != null) {
                                    contentValues.put(activityEvents.description, this.f456e);
                                }
                                if (this.f457f != null) {
                                    contentValues.put("eventLocation", this.f457f);
                                }
                                if (this.f453b != null) {
                                    contentValues.put("dtstart", this.f453b);
                                }
                                if (this.f454c != null) {
                                    contentValues.put("dtend", this.f454c);
                                }
                                if (this.f458g != null && (this.f458g.toLowerCase().equals(Stomp.TRUE) || this.f458g.toLowerCase().equals("yes"))) {
                                    contentValues.put("allDay", Integer.valueOf(1));
                                }
                                if (this.f463l <= 0) {
                                    contentValues.put("hasAlarm", Integer.valueOf(1));
                                }
                                if (this.f459h == null || this.f459h.length() <= 0) {
                                    contentValues.put("eventTimezone", TimeZone.getDefault().getID());
                                } else {
                                    contentValues.put("eventTimezone", this.f459h);
                                }
                                if (this.f460i != null && this.f460i.length() > 0) {
                                    contentValues.put("rrule", this.f460i);
                                }
                                if (this.f461j != null && this.f461j.length() > 0) {
                                    contentValues.put("exdate", this.f461j);
                                }
                                contentValues.put("eventStatus", Integer.valueOf(this.f464m));
                                contentValues.put("availability", Integer.valueOf(this.f465n));
                                Uri insert = contentResolver.insert(Events.CONTENT_URI, contentValues);
                                if (insert != null) {
                                    if (this.f463l <= 0) {
                                        ContentValues contentValues2 = new ContentValues();
                                        contentValues2.put("event_id", Long.valueOf(Long.parseLong(insert.getLastPathSegment())));
                                        contentValues2.put("method", Integer.valueOf(1));
                                        contentValues2.put("minutes", Integer.valueOf(Math.abs(this.f463l)));
                                        contentResolver.insert(Reminders.CONTENT_URI, contentValues2);
                                    }
                                    if (this.f462k != null && this.f462k.length() > 0) {
                                        adMarvelInternalWebView.m315e(this.f462k + "(\"YES\")");
                                    }
                                } else if (this.f462k != null && this.f462k.length() > 0) {
                                    adMarvelInternalWebView.m315e(this.f462k + "(\"NO\")");
                                }
                                if (query != null) {
                                    query.close();
                                }
                            }
                        } catch (Exception e2) {
                            e = e2;
                            try {
                                e.printStackTrace();
                                adMarvelInternalWebView.m315e(this.f462k + "(\"NO\")");
                                if (query != null) {
                                    query.close();
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (query != null) {
                                    query.close();
                                }
                                throw th;
                            }
                        }
                    }
                    if (this.f462k != null && this.f462k.length() > 0) {
                        adMarvelInternalWebView.m315e(this.f462k + "(\"NO\")");
                    }
                    if (query != null) {
                        query.close();
                    }
                } catch (Exception e3) {
                    e = e3;
                    query = null;
                    e.printStackTrace();
                    if (this.f462k != null && this.f462k.length() > 0) {
                        adMarvelInternalWebView.m315e(this.f462k + "(\"NO\")");
                    }
                    if (query != null) {
                        query.close();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    query = null;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.f */
    static class C0237f {
        static String m162a(TelephonyManager telephonyManager) {
            String str = Stomp.EMPTY;
            switch (telephonyManager.getNetworkType()) {
                case Zone.PRIMARY /*1*/:
                    return "GPRS";
                case Zone.SECONDARY /*2*/:
                    return "EDGE";
                case Protocol.GGP /*3*/:
                    return "UMTS";
                case Type.MF /*4*/:
                    return "CDMA";
                case Service.RJE /*5*/:
                    return "EVDO_0";
                case Protocol.TCP /*6*/:
                    return "EVDO_A";
                case Service.ECHO /*7*/:
                    return "1xRTT";
                case Protocol.EGP /*8*/:
                    return "HSDPA";
                case Service.DISCARD /*9*/:
                    return "HSUPA";
                case Protocol.BBN_RCC_MON /*10*/:
                    return "HSPA";
                case Service.USERS /*11*/:
                    return "IDEN";
                case Protocol.PUP /*12*/:
                    return "EVDO_B";
                case Service.DAYTIME /*13*/:
                    return "lte";
                case Protocol.EMCON /*14*/:
                    return "EHRPD";
                default:
                    return str;
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.g */
    static class C0238g {
        static String m163a(TelephonyManager telephonyManager) {
            String str = Stomp.EMPTY;
            switch (telephonyManager.getNetworkType()) {
                case Zone.PRIMARY /*1*/:
                    return "GPRS";
                case Zone.SECONDARY /*2*/:
                    return "EDGE";
                case Protocol.GGP /*3*/:
                    return "UMTS";
                case Type.MF /*4*/:
                    return "CDMA";
                case Service.RJE /*5*/:
                    return "EVDO_0";
                case Protocol.TCP /*6*/:
                    return "EVDO_A";
                case Service.ECHO /*7*/:
                    return "1xRTT";
                case Protocol.EGP /*8*/:
                    return "HSDPA";
                case Service.DISCARD /*9*/:
                    return "HSUPA";
                case Protocol.BBN_RCC_MON /*10*/:
                    return "HSPA";
                case Service.USERS /*11*/:
                    return "IDEN";
                case Protocol.PUP /*12*/:
                    return "EVDO_B";
                case Service.DAYTIME /*13*/:
                    return "lte";
                case Protocol.EMCON /*14*/:
                    return "EHRPD";
                case Protocol.XNET /*15*/:
                    return "HSPAP";
                default:
                    return str;
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.h */
    static class C0239h {
        static String m164a(TelephonyManager telephonyManager) {
            String str = Stomp.EMPTY;
            switch (telephonyManager.getNetworkType()) {
                case Zone.PRIMARY /*1*/:
                    return "GPRS";
                case Zone.SECONDARY /*2*/:
                    return "EDGE";
                case Protocol.GGP /*3*/:
                    return "UMTS";
                case Type.MF /*4*/:
                    return "CDMA";
                case Service.RJE /*5*/:
                    return "EVDO_0";
                case Protocol.TCP /*6*/:
                    return "EVDO_A";
                case Service.ECHO /*7*/:
                    return "1xRTT";
                default:
                    return str;
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.i */
    static class C0240i {
        static String m165a(TelephonyManager telephonyManager) {
            String str = Stomp.EMPTY;
            switch (telephonyManager.getNetworkType()) {
                case Zone.PRIMARY /*1*/:
                    return "GPRS";
                case Zone.SECONDARY /*2*/:
                    return "EDGE";
                case Protocol.GGP /*3*/:
                    return "UMTS";
                case Type.MF /*4*/:
                    return "CDMA";
                case Service.RJE /*5*/:
                    return "EVDO_0";
                case Protocol.TCP /*6*/:
                    return "EVDO_A";
                case Service.ECHO /*7*/:
                    return "1xRTT";
                case Protocol.EGP /*8*/:
                    return "HSDPA";
                case Service.DISCARD /*9*/:
                    return "HSUPA";
                case Protocol.BBN_RCC_MON /*10*/:
                    return "HSPA";
                default:
                    return str;
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.j */
    static class C0241j {
        static String m166a(TelephonyManager telephonyManager) {
            String str = Stomp.EMPTY;
            switch (telephonyManager.getNetworkType()) {
                case Zone.PRIMARY /*1*/:
                    return "GPRS";
                case Zone.SECONDARY /*2*/:
                    return "EDGE";
                case Protocol.GGP /*3*/:
                    return "UMTS";
                case Type.MF /*4*/:
                    return "CDMA";
                case Service.RJE /*5*/:
                    return "EVDO_0";
                case Protocol.TCP /*6*/:
                    return "EVDO_A";
                case Service.ECHO /*7*/:
                    return "1xRTT";
                case Protocol.EGP /*8*/:
                    return "HSDPA";
                case Service.DISCARD /*9*/:
                    return "HSUPA";
                case Protocol.BBN_RCC_MON /*10*/:
                    return "HSPA";
                default:
                    return str;
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.k */
    static class C0242k {
        static String m167a(TelephonyManager telephonyManager) {
            String str = Stomp.EMPTY;
            switch (telephonyManager.getNetworkType()) {
                case Zone.PRIMARY /*1*/:
                    return "GPRS";
                case Zone.SECONDARY /*2*/:
                    return "EDGE";
                case Protocol.GGP /*3*/:
                    return "UMTS";
                case Type.MF /*4*/:
                    return "CDMA";
                case Service.RJE /*5*/:
                    return "EVDO_0";
                case Protocol.TCP /*6*/:
                    return "EVDO_A";
                case Service.ECHO /*7*/:
                    return "1xRTT";
                case Protocol.EGP /*8*/:
                    return "HSDPA";
                case Service.DISCARD /*9*/:
                    return "HSUPA";
                case Protocol.BBN_RCC_MON /*10*/:
                    return "HSPA";
                case Service.USERS /*11*/:
                    return "IDEN";
                default:
                    return str;
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.l */
    static class C0243l {
        static String m168a(TelephonyManager telephonyManager) {
            String str = Stomp.EMPTY;
            switch (telephonyManager.getNetworkType()) {
                case Zone.PRIMARY /*1*/:
                    return "GPRS";
                case Zone.SECONDARY /*2*/:
                    return "EDGE";
                case Protocol.GGP /*3*/:
                    return "UMTS";
                case Type.MF /*4*/:
                    return "CDMA";
                case Service.RJE /*5*/:
                    return "EVDO_0";
                case Protocol.TCP /*6*/:
                    return "EVDO_A";
                case Service.ECHO /*7*/:
                    return "1xRTT";
                case Protocol.EGP /*8*/:
                    return "HSDPA";
                case Service.DISCARD /*9*/:
                    return "HSUPA";
                case Protocol.BBN_RCC_MON /*10*/:
                    return "HSPA";
                case Service.USERS /*11*/:
                    return "IDEN";
                case Protocol.PUP /*12*/:
                    return "EVDO_B";
                default:
                    return str;
            }
        }
    }

    @SuppressLint({"NewApi"})
    /* renamed from: com.admarvel.android.ads.Utils.m */
    private static class C0244m {
        public static boolean m169a(Context context, String str) {
            PackageManager packageManager = context.getPackageManager();
            if (str.equals("camera")) {
                return packageManager.hasSystemFeature("android.hardware.camera");
            }
            if (Version.getAndroidSDKVersion() >= 8) {
                if (str.equals("location")) {
                    return packageManager.hasSystemFeature("android.hardware.location");
                }
                if (str.equals("accelerometer")) {
                    return packageManager.hasSystemFeature("android.hardware.sensor.accelerometer");
                }
                if (str.equals("compass")) {
                    return packageManager.hasSystemFeature("android.hardware.sensor.compass");
                }
            }
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    /* renamed from: com.admarvel.android.ads.Utils.n */
    private class C0245n extends AsyncTask<Void, Void, Boolean> {
        final /* synthetic */ Utils f468a;
        private String f469b;

        public C0245n(Utils utils, String str) {
            this.f468a = utils;
            this.f469b = str;
        }

        protected Boolean m170a(Void... voidArr) {
            try {
                Context context = (Context) this.f468a.f496d.get();
                if (context == null) {
                    return Boolean.valueOf(false);
                }
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                if (!(connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null || !connectivityManager.getActiveNetworkInfo().isConnected())) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.f469b).openConnection();
                    httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
                    httpURLConnection.setRequestProperty("Accept-Language", Utils.m203d());
                    httpURLConnection.setRequestProperty(HttpRequest.HEADER_ACCEPT_ENCODING, "gzip,deflate");
                    httpURLConnection.setRequestProperty(HttpRequest.HEADER_USER_AGENT, Utils.m241w(context));
                    httpURLConnection.setReadTimeout(25000);
                    httpURLConnection.setConnectTimeout(AMQConnection.HANDSHAKE_TIMEOUT);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() != AMQP.REPLY_SUCCESS) {
                        return Boolean.valueOf(false);
                    }
                    Logging.log("FirePixelTask : pixelUrl :" + this.f469b);
                    return Boolean.valueOf(true);
                }
                return Boolean.valueOf(false);
            } catch (IOException e) {
                e.printStackTrace();
                this.f468a.m246b(this.f469b);
            } catch (Exception e2) {
                e2.printStackTrace();
                this.f468a.m246b(this.f469b);
            }
        }

        protected /* synthetic */ Object doInBackground(Object[] x0) {
            return m170a((Void[]) x0);
        }
    }

    @SuppressLint({"NewApi"})
    /* renamed from: com.admarvel.android.ads.Utils.o */
    private class C0246o implements Runnable {
        final /* synthetic */ Utils f470a;
        private String f471b;

        public C0246o(Utils utils, String str) {
            this.f470a = utils;
            this.f471b = str;
        }

        public void run() {
            new C0245n(this.f470a, this.f471b).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.p */
    private static class C0247p {
        @SuppressLint({"NewApi"})
        public static int m171a(WindowManager windowManager) {
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            return point.y;
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.q */
    private static class C0248q {
        @SuppressLint({"NewApi"})
        public static int m172a(WindowManager windowManager) {
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            return point.x;
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.r */
    static class C0249r implements Runnable {
        private final WeakReference<AdMarvelInternalWebView> f472a;
        private final String f473b;
        private String f474c;

        public C0249r(AdMarvelInternalWebView adMarvelInternalWebView, String str) {
            this.f474c = "null";
            this.f472a = new WeakReference(adMarvelInternalWebView);
            this.f473b = str;
        }

        public void run() {
            try {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f472a.get();
                if (adMarvelInternalWebView != null) {
                    if (!Utils.m202c(adMarvelInternalWebView.getContext(), "android.permission.ACCESS_COARSE_LOCATION") && !Utils.m202c(adMarvelInternalWebView.getContext(), "android.permission.ACCESS_FINE_LOCATION")) {
                        adMarvelInternalWebView.m315e(this.f473b + "(" + this.f474c + ")");
                    } else if (Utils.m199b(adMarvelInternalWebView.getContext(), "location")) {
                        Location a = AdMarvelLocationManager.m566a().m572a(adMarvelInternalWebView);
                        if (a != null) {
                            this.f474c = a.getLatitude() + Stomp.COMMA + a.getLongitude() + Stomp.COMMA + a.getAccuracy();
                        }
                        adMarvelInternalWebView.m315e(this.f473b + "(" + this.f474c + ")");
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.s */
    public enum C0250s {
        WITH_SLASH,
        WITHOUT_SLASH,
        NONE
    }

    /* renamed from: com.admarvel.android.ads.Utils.t */
    static class C0253t implements Runnable {
        String f481a;
        String f482b;
        InputStream f483c;
        Bitmap f484d;
        private final WeakReference<AdMarvelInternalWebView> f485e;

        /* renamed from: com.admarvel.android.ads.Utils.t.1 */
        class C02511 implements Runnable {
            final /* synthetic */ C0253t f479a;

            C02511(C0253t c0253t) {
                this.f479a = c0253t;
            }

            public void run() {
                try {
                    this.f479a.f483c = new URL(this.f479a.f481a).openStream();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }

        /* renamed from: com.admarvel.android.ads.Utils.t.2 */
        class C02522 implements Runnable {
            final /* synthetic */ C0253t f480a;

            C02522(C0253t c0253t) {
                this.f480a = c0253t;
            }

            public void run() {
                this.f480a.f484d = BitmapFactory.decodeStream(this.f480a.f483c);
            }
        }

        public C0253t(AdMarvelInternalWebView adMarvelInternalWebView, String str, String str2) {
            this.f483c = null;
            this.f484d = null;
            this.f485e = new WeakReference(adMarvelInternalWebView);
            this.f481a = str;
            this.f482b = str2;
        }

        @TargetApi(14)
        public void run() {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f485e.get();
            if (adMarvelInternalWebView != null) {
                String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + ReadOnlyContext.SEPARATOR;
                try {
                    Thread thread;
                    if (this.f481a != null) {
                        if (this.f481a.startsWith("/mnt/sdcard")) {
                            if (new File(this.f481a).exists()) {
                                this.f484d = BitmapFactory.decodeFile(this.f481a);
                            } else {
                                adMarvelInternalWebView.m315e(this.f482b + "(\"NO\")");
                            }
                        } else if (this.f481a.startsWith("http:") || this.f481a.startsWith("https:")) {
                            this.f481a = Utils.m183a(this.f481a, adMarvelInternalWebView.getContext());
                            thread = new Thread(new C02511(this));
                            thread.start();
                            thread.join();
                        } else if (this.f481a.startsWith("file:///android_asset/")) {
                            this.f483c = adMarvelInternalWebView.getContext().getAssets().open(this.f481a.substring("file:///android_asset/".length()));
                        }
                    }
                    if (this.f484d == null && this.f483c != null) {
                        thread = new Thread(new C02522(this));
                        thread.start();
                        thread.join();
                    }
                    if (this.f484d == null) {
                        adMarvelInternalWebView.m315e(this.f482b + "(\"NO\")");
                        if (this.f484d != null) {
                            this.f484d.recycle();
                            this.f484d = null;
                            return;
                        }
                        return;
                    }
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    str = str + System.currentTimeMillis() + ".jpg";
                    OutputStream fileOutputStream = new FileOutputStream(str);
                    if (!(fileOutputStream == null || this.f484d == null)) {
                        this.f484d.compress(CompressFormat.JPEG, 90, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(str)));
                    adMarvelInternalWebView.getContext().sendBroadcast(intent);
                    adMarvelInternalWebView.m315e(this.f482b + "(\"YES\")");
                    if (this.f484d != null) {
                        this.f484d.recycle();
                        this.f484d = null;
                    }
                } catch (Throwable e) {
                    adMarvelInternalWebView.m315e(this.f482b + "(\"NO\")");
                    Logging.log(Log.getStackTraceString(e));
                    if (this.f484d != null) {
                        this.f484d.recycle();
                        this.f484d = null;
                    }
                } catch (Throwable th) {
                    if (this.f484d != null) {
                        this.f484d.recycle();
                        this.f484d = null;
                    }
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.u */
    static class C0256u implements Runnable {
        String f488a;
        String f489b;
        InputStream f490c;
        Bitmap f491d;
        private final WeakReference<AdMarvelInternalWebView> f492e;

        /* renamed from: com.admarvel.android.ads.Utils.u.1 */
        class C02541 implements Runnable {
            final /* synthetic */ C0256u f486a;

            C02541(C0256u c0256u) {
                this.f486a = c0256u;
            }

            public void run() {
                try {
                    this.f486a.f490c = new URL(this.f486a.f488a).openStream();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }

        /* renamed from: com.admarvel.android.ads.Utils.u.2 */
        class C02552 implements Runnable {
            final /* synthetic */ C0256u f487a;

            C02552(C0256u c0256u) {
                this.f487a = c0256u;
            }

            public void run() {
                this.f487a.f491d = BitmapFactory.decodeStream(this.f487a.f490c);
            }
        }

        public C0256u(AdMarvelInternalWebView adMarvelInternalWebView, String str, String str2) {
            this.f490c = null;
            this.f491d = null;
            this.f492e = new WeakReference(adMarvelInternalWebView);
            this.f488a = str;
            this.f489b = str2;
        }

        @TargetApi(14)
        public void run() {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f492e.get();
            if (adMarvelInternalWebView != null) {
                String str = Environment.getExternalStorageDirectory().toString() + "/Pictures/";
                try {
                    Thread thread;
                    if (this.f488a != null) {
                        if (this.f488a.startsWith("/mnt/sdcard")) {
                            if (new File(this.f488a).exists()) {
                                this.f491d = BitmapFactory.decodeFile(this.f488a);
                            } else {
                                adMarvelInternalWebView.m315e(this.f489b + "(\"NO\")");
                            }
                        } else if (this.f488a.startsWith("http:") || this.f488a.startsWith("https:")) {
                            this.f488a = Utils.m183a(this.f488a, adMarvelInternalWebView.getContext());
                            thread = new Thread(new C02541(this));
                            thread.start();
                            thread.join();
                        } else if (this.f488a.startsWith("file:///android_asset/")) {
                            this.f490c = adMarvelInternalWebView.getContext().getAssets().open(this.f488a.substring("file:///android_asset/".length()));
                        }
                    }
                    if (this.f491d == null && this.f490c != null) {
                        thread = new Thread(new C02552(this));
                        thread.start();
                        thread.join();
                    }
                    if (this.f491d == null) {
                        adMarvelInternalWebView.m315e(this.f489b + "(\"NO\")");
                        if (this.f491d != null) {
                            this.f491d.recycle();
                            this.f491d = null;
                            return;
                        }
                        return;
                    }
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    str = str + System.currentTimeMillis() + ".jpg";
                    OutputStream fileOutputStream = new FileOutputStream(str);
                    if (!(fileOutputStream == null || this.f491d == null)) {
                        this.f491d.compress(CompressFormat.JPEG, 90, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(str)));
                    adMarvelInternalWebView.getContext().sendBroadcast(intent);
                    adMarvelInternalWebView.m315e(this.f489b + "(\"YES\")");
                    if (this.f491d != null) {
                        this.f491d.recycle();
                        this.f491d = null;
                    }
                } catch (Throwable e) {
                    adMarvelInternalWebView.m315e(this.f489b + "(\"NO\")");
                    Logging.log(Log.getStackTraceString(e));
                    if (this.f491d != null) {
                        this.f491d.recycle();
                        this.f491d = null;
                    }
                } catch (Throwable th) {
                    if (this.f491d != null) {
                        this.f491d.recycle();
                        this.f491d = null;
                    }
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.Utils.v */
    static class C0257v {
        @SuppressLint({"NewApi"})
        static String m173a(Context context) {
            try {
                return WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                return new WebView(context).getSettings().getUserAgentString();
            }
        }

        static String m174b(Context context) {
            Constructor declaredConstructor;
            try {
                Class cls = Class.forName("android.webkit.WebSettingsClassic");
                Class cls2 = Class.forName("android.webkit.WebViewClassic");
                declaredConstructor = cls.getDeclaredConstructor(new Class[]{Context.class, cls2});
                declaredConstructor.setAccessible(true);
                String userAgentString = ((WebSettings) declaredConstructor.newInstance(new Object[]{context, null})).getUserAgentString();
                declaredConstructor.setAccessible(false);
                return userAgentString;
            } catch (Exception e) {
                return new WebView(context).getSettings().getUserAgentString();
            } catch (Throwable th) {
                declaredConstructor.setAccessible(false);
            }
        }
    }

    static {
        f493a = Stomp.EMPTY;
        f494b = false;
    }

    public Utils(Context context) {
        this.f496d = new WeakReference(context);
    }

    public static float m175a(Context context, int i, int i2) {
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.density, ((float) i) / ((float) i2));
    }

    public static int m176a(float f, Context context) {
        return (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static int m177a(ErrorReason errorReason) {
        switch (C02314.f437a[errorReason.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                return 201;
            case Zone.SECONDARY /*2*/:
                return 202;
            case Protocol.GGP /*3*/:
                return 203;
            case Type.MF /*4*/:
                return 204;
            case Service.RJE /*5*/:
                return 205;
            case Protocol.TCP /*6*/:
                return AMQP.FRAME_END;
            case Service.ECHO /*7*/:
                return 207;
            case Protocol.EGP /*8*/:
                return 208;
            case Service.DISCARD /*9*/:
                return 301;
            case Protocol.BBN_RCC_MON /*10*/:
                return 302;
            case Service.USERS /*11*/:
                return 303;
            case Protocol.PUP /*12*/:
                return 304;
            case Service.DAYTIME /*13*/:
                return 305;
            case Protocol.EMCON /*14*/:
                return 306;
            case Protocol.XNET /*15*/:
                return 307;
            case Protocol.CHAOS /*16*/:
                return 308;
            case Service.QUOTE /*17*/:
                return 309;
            default:
                return -1;
        }
    }

    public static ErrorReason m178a(int i) {
        switch (i) {
            case 201:
                return ErrorReason.SITE_ID_OR_PARTNER_ID_NOT_PRESENT;
            case 202:
                return ErrorReason.SITE_ID_AND_PARTNER_ID_DO_NOT_MATCH;
            case 203:
                return ErrorReason.BOT_USER_AGENT_FOUND;
            case 204:
                return ErrorReason.NO_BANNER_FOUND;
            case 205:
                return ErrorReason.NO_AD_FOUND;
            case AMQP.FRAME_END /*206*/:
                return ErrorReason.NO_USER_AGENT_FOUND;
            case 207:
                return ErrorReason.SITE_ID_NOT_PRESENT;
            case 208:
                return ErrorReason.PARTNER_ID_NOT_PRESENT;
            case 301:
                return ErrorReason.NO_NETWORK_CONNECTIVITY;
            case 302:
                return ErrorReason.NETWORK_CONNECTIVITY_DISRUPTED;
            case 303:
                return ErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION;
            case 304:
                return ErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION;
            case 305:
                return ErrorReason.AD_UNIT_NOT_ABLE_TO_RENDER;
            case 306:
                return ErrorReason.AD_REQUEST_MISSING_XML_ELEMENTS;
            case 307:
                return ErrorReason.AD_REQUEST_SDK_TYPE_UNSUPPORTED;
            case 308:
                return ErrorReason.AD_UNIT_NOT_ABLE_TO_LOAD;
            case 309:
                return ErrorReason.AD_UNIT_IN_DISPLAY_STATE;
            default:
                return null;
        }
    }

    public static C0250s m179a(String str, String str2) {
        if (!(str == null || str2 == null)) {
            try {
                if (str.startsWith(str2 + "://")) {
                    return C0250s.WITH_SLASH;
                }
                if (str.startsWith(str2)) {
                    return C0250s.WITHOUT_SLASH;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return C0250s.NONE;
    }

    public static String m180a(Activity activity) {
        int requestedOrientation = activity.getRequestedOrientation();
        String str = Stomp.EMPTY;
        if (Version.getAndroidSDKVersion() < 9) {
            switch (requestedOrientation) {
                case RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES /*-1*/:
                    return "0,90";
                case Tokenizer.EOF /*0*/:
                    return "90";
                case Zone.PRIMARY /*1*/:
                    return "0";
                case Zone.SECONDARY /*2*/:
                    return "0,90";
                case Protocol.GGP /*3*/:
                    return "0,90";
                case Type.MF /*4*/:
                    return "0,90";
                case Service.RJE /*5*/:
                    return "0";
                default:
                    return "0";
            }
        }
        switch (requestedOrientation) {
            case RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES /*-1*/:
                return "0,-90,90";
            case Tokenizer.EOF /*0*/:
                return "90";
            case Zone.PRIMARY /*1*/:
                return "0";
            case Zone.SECONDARY /*2*/:
                return "0,90,-90";
            case Protocol.GGP /*3*/:
                return "0,90,-90";
            case Type.MF /*4*/:
                return "0,-90,90";
            case Service.RJE /*5*/:
                return "0";
            case Protocol.TCP /*6*/:
                return "90,-90";
            case Service.ECHO /*7*/:
                return "180,-180";
            case Protocol.EGP /*8*/:
                return "-90";
            case Service.DISCARD /*9*/:
                return "180";
            case Protocol.BBN_RCC_MON /*10*/:
                return "0,-90,90,180";
            default:
                return str;
        }
    }

    public static String m181a(Context context) {
        if (context == null) {
            throw new NullPointerException();
        } else if (!m202c(context, "android.permission.ACCESS_NETWORK_STATE")) {
            return null;
        } else {
            int x = m242x(context);
            return x == 1 ? "wifi" : x == 0 ? "mobile" : "none";
        }
    }

    public static String m182a(FileInputStream fileInputStream, String str) {
        File file = new File(Environment.getExternalStorageDirectory() + ReadOnlyContext.SEPARATOR + ".admfiles");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file.getAbsolutePath() + ReadOnlyContext.SEPARATOR + str);
        if (!file2.exists()) {
            byte[] bArr = new byte[KEYRecord.FLAG_NOCONF];
            try {
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return file2.getAbsolutePath();
    }

    static String m183a(String str, Context context) {
        if (str == null || str.length() <= 0) {
            return str;
        }
        String str2 = str;
        while (true) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_GET);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty(HttpRequest.HEADER_USER_AGENT, m241w(context));
                httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
                httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, "0");
                httpURLConnection.setConnectTimeout(4000);
                httpURLConnection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
                httpURLConnection.setInstanceFollowRedirects(false);
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 301 && responseCode != 302) {
                    break;
                }
                str2 = httpURLConnection.getHeaderField(HttpRequest.HEADER_LOCATION);
            } catch (Exception e) {
            }
        }
        return (str2 == null || str2.length() <= 0) ? str : str2;
    }

    public static String m184a(String str, String str2, String str3, C0250s c0250s, Context context) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    str = str.replace("content://" + context.getPackageName() + ".AdMarvelLocalFileContentProvider", Stomp.EMPTY);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return c0250s == C0250s.WITH_SLASH ? str.replaceFirst(str2 + "://", str3) : c0250s == C0250s.WITHOUT_SLASH ? str.replaceFirst(str2, str3) : null;
    }

    public static String m185a(Map<String, Object> map, String str) {
        if (map == null) {
            return null;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str2 : map.keySet()) {
                String str3 = map.get(str2) instanceof String ? (String) map.get(str2) : null;
                if (str3 != null && str3.length() > 0) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(str);
                    }
                    stringBuilder.append(URLEncoder.encode(str2, HttpRequest.CHARSET_UTF8)).append("=>").append(URLEncoder.encode(str3, HttpRequest.CHARSET_UTF8));
                }
            }
            return stringBuilder.toString();
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return null;
        }
    }

    public static void m187a(SDKAdNetwork sDKAdNetwork, Context context, int i) {
        String str;
        if (sDKAdNetwork == SDKAdNetwork.ADCOLONY) {
            str = "adc_adrequest_timestamp";
        } else if (sDKAdNetwork == SDKAdNetwork.UNITYADS) {
            str = "unity_adrequest_timestamp";
        } else if (sDKAdNetwork == SDKAdNetwork.VUNGLE) {
            str = "vungle_adrequest_timestamp";
        } else if (sDKAdNetwork == SDKAdNetwork.YUME) {
            str = "yume_adrequest_timestamp";
        } else if (sDKAdNetwork == SDKAdNetwork.CHARTBOOST) {
            str = "chartboost_adrequest_timestamp";
        } else {
            return;
        }
        if (context == null) {
            return;
        }
        Editor edit;
        if (i == 0) {
            edit = m243y(context).edit();
            edit.putLong(str, System.currentTimeMillis());
            edit.commit();
        } else if (i == 1) {
            SharedPreferences y = m243y(context);
            if (Long.valueOf(y.getLong(str, 0)).longValue() == 0) {
                Long valueOf = Long.valueOf(System.currentTimeMillis());
                edit = y.edit();
                edit.putLong(str, valueOf.longValue());
                edit.commit();
            }
        }
    }

    public static void m188a(StringBuilder stringBuilder, String str, String str2) {
        if (stringBuilder != null && str2 != null && str != null) {
            try {
                stringBuilder.append("&").append(str).append("=").append(URLEncoder.encode(str2, HttpRequest.CHARSET_UTF8));
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean m189a() {
        return Version.getAndroidSDKVersion() >= 4 && "Amazon".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean m190a(Context context, int i) {
        if (context != null) {
            try {
                if (m202c(context, "android.permission.VIBRATE")) {
                    ((Vibrator) context.getSystemService("vibrator")).vibrate((long) i);
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static boolean m191a(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, AccessibilityNodeInfoCompat.ACTION_CUT).size() > 0;
    }

    static boolean m192a(Context context, String str) {
        try {
            context.getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean m193a(Context context, String str, boolean z) {
        try {
            Uri parse;
            String str2;
            String[] split;
            Intent intent;
            Intent intent2;
            if ((AdMarvelView.enableOfflineSDK || AdMarvelInterstitialAds.enableOfflineSDK) && str != null && str.contains(ReadOnlyContext.SEPARATOR) && !URLUtil.isNetworkUrl(str)) {
                String substring = str.substring(str.lastIndexOf(ReadOnlyContext.SEPARATOR) + 1);
                if (m221l(substring)) {
                    parse = Uri.parse(substring);
                    Intent intent3;
                    if (Version.getAndroidSDKVersion() > 18) {
                        str2 = null;
                        if (str.contains("?body=")) {
                            str2 = URLDecoder.decode(str.split("\\?body=")[1]);
                        }
                        intent3 = new Intent("android.intent.action.SENDTO", Uri.parse(str));
                        if (str2 != null && str2.trim().length() > 0) {
                            intent3.putExtra("sms_body", str2);
                        }
                        if (m191a(context, intent3)) {
                            context.startActivity(intent3);
                            return true;
                        }
                    }
                    intent3 = new Intent("android.intent.action.VIEW");
                    if (str.contains("?body=")) {
                        split = str.split("\\?body=");
                        intent3.putExtra("address", URLDecoder.decode(split[0]).substring(4));
                        intent3.putExtra("sms_body", URLDecoder.decode(split[1]));
                    } else {
                        intent3.putExtra("address", parse.toString().substring(4));
                    }
                    intent3.setType("vnd.android-dir/mms-sms");
                    intent3.addFlags(268435456);
                    if (z) {
                        intent3.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                    }
                    if (m191a(context, intent3)) {
                        context.startActivity(intent3);
                        return true;
                    }
                } else if (m219k(substring)) {
                    intent = new Intent("android.intent.action.DIAL", Uri.parse(substring));
                    intent.addFlags(268435456);
                    if (z) {
                        intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                    }
                    if (m191a(context, intent)) {
                        context.startActivity(intent);
                        return true;
                    }
                }
                try {
                    FileInputStream e = m206e(str);
                    if (e == null || substring == null) {
                        if (m233r(str) || m237t(str) || m235s(str)) {
                            Logging.log(" ");
                            return true;
                        }
                    } else if (m233r(str)) {
                        str2 = m182a(e, substring);
                        if (str2 != null) {
                            intent2 = new Intent("android.intent.action.VIEW");
                            intent2.addFlags(268435456);
                            if (z) {
                                intent2.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                            }
                            intent2.setDataAndType(Uri.fromFile(new File(str2)), "video/*");
                            if (!m191a(context, intent2)) {
                                return true;
                            }
                            context.startActivity(intent2);
                            return true;
                        }
                    } else if (m235s(str)) {
                        str2 = m182a(e, substring);
                        if (str2 != null) {
                            intent2 = new Intent("android.intent.action.VIEW");
                            intent2.addFlags(268435456);
                            if (z) {
                                intent2.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                            }
                            intent2.setDataAndType(Uri.fromFile(new File(str2)), "audio/*");
                            if (!m191a(context, intent2)) {
                                return true;
                            }
                            context.startActivity(intent2);
                            return true;
                        }
                    } else if (m237t(str)) {
                        str2 = m182a(e, substring);
                        if (str2 == null) {
                            return true;
                        }
                        intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str2));
                        intent2.setDataAndType(Uri.fromFile(new File(str2)), "application/pdf");
                        intent2.setFlags(268435456);
                        if (z) {
                            intent2.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                        }
                        if (m191a(context, intent2)) {
                            context.startActivity(intent2);
                            return true;
                        }
                        intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                        intent.addFlags(268435456);
                        if (z) {
                            intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                        }
                        if (!m191a(context, intent)) {
                            return true;
                        }
                        context.startActivity(intent);
                        return true;
                    }
                } catch (Exception e2) {
                    if (m233r(str) || m237t(str) || m235s(str)) {
                        Logging.log(" ");
                        return true;
                    }
                }
            }
            if (str != null) {
                if (str.length() > 0) {
                    str = str.replace("content://" + context.getPackageName() + ".AdMarvelLocalFileContentProvider", Stomp.EMPTY);
                }
            }
            try {
                if (m220l(context) && m213h(str)) {
                    intent = new Intent("android.intent.action.VIEW");
                    intent.addFlags(268435456);
                    if (z) {
                        intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                    }
                    intent.setDataAndType(Uri.parse(str), "video/*");
                    if (m191a(context, intent)) {
                        context.startActivity(intent);
                        return true;
                    }
                    return false;
                } else if (m220l(context) && m215i(str)) {
                    intent = new Intent("android.intent.action.VIEW");
                    intent.addFlags(268435456);
                    if (z) {
                        intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                    }
                    intent.setDataAndType(Uri.parse(str), "audio/*");
                    if (m191a(context, intent)) {
                        context.startActivity(intent);
                        return true;
                    }
                    return false;
                } else if (m220l(context) && m227o(str)) {
                    intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                    intent.setDataAndType(Uri.parse(str), "application/pdf");
                    intent.setFlags(268435456);
                    if (z) {
                        intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                    }
                    if (m191a(context, intent)) {
                        context.startActivity(intent);
                        return true;
                    }
                    intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                    intent.addFlags(268435456);
                    if (!m191a(context, intent)) {
                        return true;
                    }
                    context.startActivity(intent);
                    return true;
                } else {
                    if (m221l(str)) {
                        if (Version.getAndroidSDKVersion() >= 19) {
                            str2 = null;
                            if (str.contains("?body=")) {
                                str2 = URLDecoder.decode(str.split("\\?body=")[1]);
                            }
                            intent2 = new Intent("android.intent.action.SENDTO", Uri.parse(str));
                            if (str2 != null && str2.trim().length() > 0) {
                                intent2.putExtra("sms_body", str2);
                            }
                            if (m191a(context, intent2)) {
                                context.startActivity(intent2);
                                return true;
                            }
                        }
                        parse = Uri.parse(str);
                        intent2 = new Intent("android.intent.action.VIEW");
                        if (str.contains("?body=")) {
                            split = str.split("\\?body=");
                            intent2.putExtra("address", URLDecoder.decode(split[0]).substring(4));
                            intent2.putExtra("sms_body", URLDecoder.decode(split[1]));
                        } else {
                            intent2.putExtra("address", parse.toString().substring(4));
                        }
                        intent2.setType("vnd.android-dir/mms-sms");
                        intent2.addFlags(268435456);
                        if (z) {
                            intent2.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                        }
                        if (m191a(context, intent2)) {
                            context.startActivity(intent2);
                            return true;
                        }
                    } else if (m219k(str)) {
                        intent = new Intent("android.intent.action.DIAL", Uri.parse(str));
                        intent.addFlags(268435456);
                        if (z) {
                            intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                        }
                        if (m191a(context, intent)) {
                            context.startActivity(intent);
                            return true;
                        }
                    } else if (m225n(str) || m217j(str) || m228p(str) || m231q(str) || m223m(str)) {
                        intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                        intent.addFlags(268435456);
                        if (z) {
                            intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                        }
                        if (m191a(context, intent)) {
                            context.startActivity(intent);
                            return true;
                        }
                    }
                    return false;
                }
            } catch (ActivityNotFoundException e3) {
                Logging.log(" ActivityNotFoundException");
                f495c = m211g(str);
                if ((AdMarvelView.enableOfflineSDK || AdMarvelInterstitialAds.enableOfflineSDK) && !f495c) {
                    return true;
                }
            }
        } catch (Exception e4) {
            Logging.log(" Exception in handling special intents");
            return false;
        }
    }

    public static boolean m194a(SDKAdNetwork sDKAdNetwork, Context context) {
        String str;
        String str2;
        Long valueOf = Long.valueOf(14400000);
        if (sDKAdNetwork == SDKAdNetwork.ADCOLONY) {
            str = "adc_adrequest_timestamp";
            str2 = "adc_ttl_value";
        } else if (sDKAdNetwork == SDKAdNetwork.UNITYADS) {
            str = "unity_adrequest_timestamp";
            str2 = "unity_ttl_value";
        } else if (sDKAdNetwork == SDKAdNetwork.VUNGLE) {
            str = "vungle_adrequest_timestamp";
            str2 = "vungle_ttl_value";
        } else if (sDKAdNetwork == SDKAdNetwork.YUME) {
            str = "yume_adrequest_timestamp";
            str2 = "yume_ttl_value";
        } else if (sDKAdNetwork != SDKAdNetwork.CHARTBOOST) {
            return false;
        } else {
            str = "chartboost_adrequest_timestamp";
            str2 = "chartboost_ttl_value";
        }
        if (context != null) {
            SharedPreferences y = m243y(context);
            Long valueOf2 = Long.valueOf(y.getLong(str, 0));
            if (valueOf2.longValue() == 0) {
                valueOf = Long.valueOf(System.currentTimeMillis());
                Editor edit = y.edit();
                edit.putLong(str, valueOf.longValue());
                edit.commit();
                return true;
            }
            Long valueOf3 = Long.valueOf(System.currentTimeMillis());
            Long valueOf4 = Long.valueOf(y.getLong(str2, 0));
            if (valueOf4.longValue() == 0) {
                Editor edit2 = y.edit();
                edit2.putLong(str2, valueOf.longValue());
                edit2.commit();
            } else {
                valueOf = valueOf4;
            }
            if (valueOf3.longValue() - valueOf2.longValue() > valueOf.longValue()) {
                return true;
            }
        }
        return false;
    }

    static String m195b(Context context) {
        String str = Stomp.EMPTY;
        if (context == null) {
            return str;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (activeNetworkInfo == null) {
            return str;
        }
        String a;
        switch (activeNetworkInfo.getType()) {
            case Tokenizer.EOF /*0*/:
                if (Version.getAndroidSDKVersion() < 13) {
                    if (Version.getAndroidSDKVersion() < 11 || Version.getAndroidSDKVersion() > 12) {
                        if (Version.getAndroidSDKVersion() < 9 || Version.getAndroidSDKVersion() > 10) {
                            if (Version.getAndroidSDKVersion() != 8) {
                                if (Version.getAndroidSDKVersion() != 7) {
                                    if (Version.getAndroidSDKVersion() >= 5 && Version.getAndroidSDKVersion() <= 6) {
                                        a = C0240i.m165a(telephonyManager);
                                        break;
                                    }
                                    a = C0239h.m164a(telephonyManager);
                                    break;
                                }
                                a = C0241j.m166a(telephonyManager);
                                break;
                            }
                            a = C0242k.m167a(telephonyManager);
                            break;
                        }
                        a = C0243l.m168a(telephonyManager);
                        break;
                    }
                    a = C0237f.m162a(telephonyManager);
                    break;
                }
                a = C0238g.m163a(telephonyManager);
                break;
                break;
            case Zone.PRIMARY /*1*/:
                a = "wifi";
                break;
            default:
                a = str;
                break;
        }
        return a;
    }

    public static String m197b(Map<String, String> map, String str) {
        if (map == null) {
            return null;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str2 : map.keySet()) {
                String str3 = map.get(str2) instanceof String ? (String) map.get(str2) : null;
                if (str3 != null && str3.length() > 0) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(str);
                    }
                    stringBuilder.append(URLEncoder.encode(str2, HttpRequest.CHARSET_UTF8)).append("=>").append(URLEncoder.encode(str3, HttpRequest.CHARSET_UTF8));
                }
            }
            return stringBuilder.toString();
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return null;
        }
    }

    public static boolean m198b() {
        return Version.getAndroidSDKVersion() == 16;
    }

    public static boolean m199b(Context context, String str) {
        Boolean valueOf = Boolean.valueOf(false);
        if (Version.getAndroidSDKVersion() >= 7 && str.equals("camera")) {
            return Boolean.valueOf(C0244m.m169a(context, str)).booleanValue();
        }
        if (Version.getAndroidSDKVersion() >= 8) {
            return Boolean.valueOf(C0244m.m169a(context, str)).booleanValue();
        }
        if (str.equals("camera")) {
            Camera camera = null;
            try {
                camera = Camera.open();
            } catch (RuntimeException e) {
            }
            valueOf = camera != null ? Boolean.valueOf(true) : valueOf;
        } else if (str.equals("location")) {
            if (((LocationManager) context.getSystemService("location")) != null) {
                valueOf = Boolean.valueOf(true);
            }
        } else if (str.equals("accelerometer")) {
            if (AdMarvelSensorManager.m579a().m591a(context)) {
                valueOf = Boolean.valueOf(true);
            }
        } else if (str.equals("compass") && AdMarvelSensorManager.m579a().m594b(context)) {
            valueOf = Boolean.valueOf(true);
        }
        return valueOf.booleanValue();
    }

    static String m200c(Context context) {
        try {
            String str = Stomp.EMPTY;
            if (context == null) {
                return "unknown-network";
            }
            str = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
            if (str.length() > 0) {
                return str;
            }
            return "unknown-network";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean m201c() {
        return m239u("su");
    }

    public static boolean m202c(Context context, String str) {
        return context != null && context.checkCallingOrSelfPermission(str) == 0;
    }

    public static String m203d() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Locale locale = Locale.getDefault();
            String language = locale.getLanguage();
            if (language != null) {
                stringBuilder.append(language.toLowerCase());
                String country = locale.getCountry();
                if (country != null) {
                    stringBuilder.append("-");
                    stringBuilder.append(country.toLowerCase());
                }
            } else {
                stringBuilder.append("en");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "en";
        }
    }

    static String m204d(Context context) {
        try {
            String str = Stomp.EMPTY;
            if (context == null) {
                return Stomp.EMPTY;
            }
            str = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
            if (str.length() > 0) {
                return str;
            }
            return Stomp.EMPTY;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String m205d(String str) {
        try {
            return URLEncoder.encode(Base64.m600a(str.getBytes(), 8), HttpRequest.CHARSET_UTF8);
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return Stomp.EMPTY;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.FileInputStream m206e(java.lang.String r3) {
        /*
        r0 = 0;
        r1 = "/";
        r1 = r3.lastIndexOf(r1);
        r3.substring(r0, r1);
        r0 = "/";
        r0 = r3.lastIndexOf(r0);
        r0 = r0 + 1;
        r3.substring(r0);
        r1 = new java.io.File;
        r1.<init>(r3);
        r0 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0020, NullPointerException -> 0x003d, Exception -> 0x0059 }
        r0.<init>(r1);	 Catch:{ FileNotFoundException -> 0x0020, NullPointerException -> 0x003d, Exception -> 0x0059 }
    L_0x001f:
        return r0;
    L_0x0020:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "read file";
        r1 = r1.append(r2);
        r0 = r0.getMessage();
        r0 = r1.append(r0);
        r0 = r0.toString();
        com.admarvel.android.util.Logging.log(r0);
    L_0x003b:
        r0 = 0;
        goto L_0x001f;
    L_0x003d:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "read file";
        r1 = r1.append(r2);
        r0 = r0.getMessage();
        r0 = r1.append(r0);
        r0 = r0.toString();
        com.admarvel.android.util.Logging.log(r0);
        goto L_0x003b;
    L_0x0059:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "read file";
        r1 = r1.append(r2);
        r0 = r0.getMessage();
        r0 = r1.append(r0);
        r0 = r0.toString();
        com.admarvel.android.util.Logging.log(r0);
        goto L_0x003b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.admarvel.android.ads.Utils.e(java.lang.String):java.io.FileInputStream");
    }

    static String m207e(Context context) {
        try {
            String str = Stomp.EMPTY;
            if (context == null) {
                return "unknown-network";
            }
            str = ((TelephonyManager) context.getSystemService("phone")).getSimOperatorName();
            if (str.length() > 0) {
                return str;
            }
            return "unknown-network";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String m208f(Context context) {
        try {
            String str = Stomp.EMPTY;
            if (context == null) {
                return Stomp.EMPTY;
            }
            str = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
            if (str.length() > 0) {
                return str;
            }
            return Stomp.EMPTY;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean m209f(String str) {
        return str != null && str.length() > 0 && (str.equals("admarvelsdk://noclick") || str.equals("admarvelsdk://nothing"));
    }

    static String m210g(Context context) {
        if (context == null) {
            try {
                return "0";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (((TelephonyManager) context.getSystemService("phone")).isNetworkRoaming()) {
                return "1";
            }
            return "0";
        }
    }

    public static boolean m211g(String str) {
        return (m179a(str, "admarvelsdk") == C0250s.NONE && m179a(str, "admarvelinternal") == C0250s.NONE && m179a(str, "admarvelvideo") == C0250s.NONE && m179a(str, "admarvelexternal") == C0250s.NONE && m179a(str, "admarvelcustomvideo") == C0250s.NONE) ? false : true;
    }

    static String m212h(Context context) {
        try {
            String str = Stomp.EMPTY;
            if (context == null) {
                return Stomp.EMPTY;
            }
            str = Stomp.EMPTY + ((TelephonyManager) context.getSystemService("phone")).getSimCountryIso();
            if (str.length() > 0) {
                return str;
            }
            return Stomp.EMPTY;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean m213h(String str) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        if (!str.toLowerCase().endsWith(".mp4") && !str.toLowerCase().endsWith(".3gp") && !str.toLowerCase().endsWith(".webm") && !str.toLowerCase().endsWith(".mkv")) {
            return false;
        }
        try {
            String path = new URL(str).getPath();
            return (path == null || path.length() <= 0) ? false : path.toLowerCase().endsWith(".mp4") || path.toLowerCase().endsWith(".3gp") || path.toLowerCase().endsWith(".webm") || path.toLowerCase().endsWith(".mkv");
        } catch (MalformedURLException e) {
            return false;
        }
    }

    static String m214i(Context context) {
        try {
            String str = Stomp.EMPTY;
            if (context == null) {
                return Stomp.EMPTY;
            }
            str = ((TelephonyManager) context.getSystemService("phone")).getNetworkCountryIso();
            if (str.length() > 0) {
                return str;
            }
            return Stomp.EMPTY;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean m215i(String str) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        if (!str.toLowerCase().endsWith(".flac") && !str.toLowerCase().endsWith(".mp3") && !str.toLowerCase().endsWith(".mid") && !str.toLowerCase().endsWith(".xmf") && !str.toLowerCase().endsWith(".mxmf") && !str.toLowerCase().endsWith(".rtttl") && !str.toLowerCase().endsWith(".rtx") && !str.toLowerCase().endsWith(".ota") && !str.toLowerCase().endsWith(".imy")) {
            return false;
        }
        try {
            String path = new URL(str).getPath();
            return (path == null || path.length() <= 0) ? false : path.toLowerCase().endsWith(".flac") || path.toLowerCase().endsWith(".mp3") || path.toLowerCase().endsWith(".mid") || path.toLowerCase().endsWith(".xmf") || path.toLowerCase().endsWith(".mxmf") || path.toLowerCase().endsWith(".rtttl") || path.toLowerCase().endsWith(".rtx") || path.toLowerCase().endsWith(".ota") || path.toLowerCase().endsWith(".imy");
        } catch (MalformedURLException e) {
            return false;
        }
    }

    static int m216j(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        return defaultDisplay.getWidth() > defaultDisplay.getHeight() ? 2 : 1;
    }

    private static boolean m217j(String str) {
        return str != null && str.length() > 0 && str.toLowerCase().startsWith("google.streetview:");
    }

    public static Integer m218k(Context context) {
        return context != null ? m216j(context) == 1 ? Integer.valueOf(1) : Integer.valueOf(0) : null;
    }

    private static boolean m219k(String str) {
        return str != null && str.length() > 0 && (str.toLowerCase().startsWith("tel:") || str.toLowerCase().startsWith("voicemail:"));
    }

    public static final boolean m220l(Context context) {
        if (m202c(context, "android.permission.ACCESS_NETWORK_STATE")) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected()) {
                return true;
            }
        }
        return false;
    }

    private static boolean m221l(String str) {
        return str != null && str.length() > 0 && str.toLowerCase().startsWith("sms:");
    }

    @SuppressLint({"NewApi"})
    public static int m222m(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
            return Version.getAndroidSDKVersion() >= 13 ? C0248q.m172a(windowManager) : windowManager.getDefaultDisplay().getWidth();
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return 0;
        }
    }

    private static boolean m223m(String str) {
        return str != null && str.length() > 0 && str.toLowerCase().startsWith("mailto:");
    }

    @SuppressLint({"NewApi"})
    public static int m224n(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
            return Version.getAndroidSDKVersion() >= 13 ? C0247p.m171a(windowManager) : windowManager.getDefaultDisplay().getHeight();
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return 0;
        }
    }

    private static boolean m225n(String str) {
        return str != null && str.length() > 0 && (str.toLowerCase().startsWith("geo:") || str.toLowerCase().startsWith("http://maps.google") || str.toLowerCase().startsWith("https://maps.google"));
    }

    public static float m226o(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.density;
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return 0.0f;
        }
    }

    private static boolean m227o(String str) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        if (!str.toLowerCase().endsWith(".pdf") && !str.toLowerCase().endsWith(".pdf")) {
            return false;
        }
        try {
            String path = new URL(str).getPath();
            return (path == null || path.length() <= 0) ? false : path.toLowerCase().endsWith(".pdf") || path.toLowerCase().endsWith(".pdf");
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private static boolean m228p(String str) {
        return str != null && str.length() > 0 && (str.toLowerCase().startsWith("market://") || str.toLowerCase().startsWith("http://play.google.com") || str.toLowerCase().startsWith("https://play.google.com"));
    }

    static int[] m229p(Context context) {
        if (context == null) {
            return null;
        }
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        int intExtra = registerReceiver.getIntExtra("level", -1);
        int intExtra2 = registerReceiver.getIntExtra(SettingsJsonConstants.APP_STATUS_KEY, -1);
        intExtra2 = (intExtra2 == 2 || intExtra2 == 5) ? 1 : 0;
        intExtra2 = intExtra2 != 0 ? 1 : 0;
        return new int[]{intExtra, intExtra2};
    }

    public static boolean m230q(Context context) {
        try {
            if (((TelephonyManager) context.getSystemService("phone")).getPhoneType() == 0) {
                return false;
            }
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
        }
        return true;
    }

    private static boolean m231q(String str) {
        return str != null && str.length() > 0 && str.toLowerCase().startsWith("vzw://");
    }

    public static boolean m232r(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private static boolean m233r(String str) {
        return str != null && str.length() > 0 && (str.toLowerCase().endsWith(".mp4") || str.toLowerCase().endsWith(".3gp"));
    }

    public static void m234s(Context context) {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        long preferenceValueLong = AdMarvelUtils.getPreferenceValueLong(context, "adm_viewport", "adm_viewport_timestamp");
        if (preferenceValueLong == -2147483648L) {
            preferenceValueLong = 0;
        }
        if (timeInMillis - preferenceValueLong >= 86400000) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                InputStream inputStream = new URL(Constants.DYNAMIC_VIEWPORT_JS_URL).openConnection().getInputStream();
                String str = Stomp.EMPTY;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuilder.append(readLine);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            AdMarvelUtils.setPreferenceValueString(context, "adm_viewport", "adm_viewport_data", stringBuilder.toString());
            AdMarvelUtils.setPreferenceValueLong(context, "adm_viewport", "adm_viewport_timestamp", Calendar.getInstance().getTimeInMillis());
        }
        f493a = AdMarvelUtils.getPreferenceValueString(context, "adm_viewport", "adm_viewport_data");
        if (f493a != null && f493a.equals("VALUE_NOT_DEFINED")) {
            Logging.log("Viewport data fetch failed - Setting blank string");
            f493a = Stomp.EMPTY;
        }
    }

    private static boolean m235s(String str) {
        return str != null && str.length() > 0 && (str.toLowerCase().endsWith(".flac") || str.toLowerCase().endsWith(".mp3") || str.toLowerCase().endsWith(".mid") || str.toLowerCase().endsWith(".xmf") || str.toLowerCase().endsWith(".mxmf") || str.toLowerCase().endsWith(".rtttl") || str.toLowerCase().endsWith(".rtx") || str.toLowerCase().endsWith(".ota") || str.toLowerCase().endsWith(".imy"));
    }

    public static void m236t(Context context) {
        try {
            File file = new File(context.getDir("adm_assets", 0), "admarvel_adHistory");
            if (file != null && file.isDirectory()) {
                new Thread(new C02303(file)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean m237t(String str) {
        return str != null && str.length() > 0 && (str.toLowerCase().endsWith(".pdf") || str.toLowerCase().endsWith(".pdf"));
    }

    public static void m238u(Context context) {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        long preferenceValueLong = AdMarvelUtils.getPreferenceValueLong(context, "adm_viewport", "adm_mraid_download_timestamp");
        if (preferenceValueLong == -2147483648L) {
            preferenceValueLong = 0;
        }
        if (timeInMillis - preferenceValueLong >= 86400000) {
            try {
                String str = context.getDir("adm_assets", 0).getAbsolutePath() + "/mraid.js";
                File file = new File(str + ".PROCESSING");
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constants.MRAID_JS_URL).openConnection();
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_GET);
                httpURLConnection.setDoOutput(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
                httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, "0");
                httpURLConnection.setConnectTimeout(AdError.SERVER_ERROR_CODE);
                httpURLConnection.setReadTimeout(AMQConnection.HANDSHAKE_TIMEOUT);
                int responseCode = httpURLConnection.getResponseCode();
                int contentLength = httpURLConnection.getContentLength();
                Logging.log("Mraid Connection Status Code: " + responseCode);
                Logging.log("Mraid Content Length: " + contentLength);
                if (responseCode == AMQP.REPLY_SUCCESS) {
                    InputStream inputStream = (InputStream) httpURLConnection.getContent();
                    if (HttpRequest.ENCODING_GZIP.equals(httpURLConnection.getContentEncoding()) && Version.getAndroidSDKVersion() < 9) {
                        inputStream = new GZIPInputStream(inputStream);
                    }
                    if (inputStream != null) {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bArr = new byte[Flags.EXTEND];
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        File file2 = new File(str);
                        if (file2.exists()) {
                            file2.delete();
                        }
                        file.renameTo(file2);
                        if (file.exists()) {
                            file.delete();
                        }
                        inputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        Logging.log("Mraid File updated ");
                        AdMarvelUtils.setPreferenceValueLong(context, "adm_viewport", "adm_mraid_download_timestamp", Calendar.getInstance().getTimeInMillis());
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    private static boolean m239u(String str) {
        for (String str2 : new String[]{"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"}) {
            if (new File(str2 + str).exists()) {
                return true;
            }
        }
        return false;
    }

    public static void updateTTlValueFromAdapter(SDKAdNetwork adNetwork, Context context, String adnetworkServerTTLValue) {
        String str;
        if (adNetwork == SDKAdNetwork.ADCOLONY) {
            str = "adc_ttl_value";
        } else if (adNetwork == SDKAdNetwork.UNITYADS) {
            str = "unity_ttl_value";
        } else if (adNetwork == SDKAdNetwork.VUNGLE) {
            str = "vungle_ttl_value";
        } else if (adNetwork == SDKAdNetwork.YUME) {
            str = "yume_ttl_value";
        } else if (adNetwork == SDKAdNetwork.CHARTBOOST) {
            str = "chartboost_ttl_value";
        } else {
            return;
        }
        if (context != null && adnetworkServerTTLValue != null && adnetworkServerTTLValue.length() > 0) {
            try {
                Long valueOf = Long.valueOf(Long.parseLong(adnetworkServerTTLValue));
                Editor edit = m243y(context).edit();
                edit.putLong(str, valueOf.longValue());
                edit.commit();
            } catch (Exception e) {
            }
        }
    }

    static void m240v(Context context) {
        if (!f494b) {
            Logging.log("Handling WebView Bad State issue for Android OS 4.4");
            View webView = new WebView(context.getApplicationContext());
            webView.setBackgroundColor(0);
            webView.loadDataWithBaseURL(null, Stomp.EMPTY, WebRequest.CONTENT_TYPE_HTML, HttpRequest.CHARSET_UTF8, null);
            LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = 1;
            layoutParams.height = 1;
            layoutParams.type = 2005;
            layoutParams.flags = 16777240;
            layoutParams.format = -2;
            layoutParams.gravity = 8388659;
            ((WindowManager) context.getSystemService("window")).addView(webView, layoutParams);
            f494b = true;
        }
    }

    public static String m241w(Context context) {
        if (VERSION.SDK_INT >= 17) {
            return C0257v.m173a(context);
        }
        if (VERSION.SDK_INT >= 16) {
            return C0257v.m174b(context);
        }
        Constructor declaredConstructor;
        try {
            declaredConstructor = WebSettings.class.getDeclaredConstructor(new Class[]{Context.class, WebView.class});
            declaredConstructor.setAccessible(true);
            String userAgentString = ((WebSettings) declaredConstructor.newInstance(new Object[]{context, null})).getUserAgentString();
            declaredConstructor.setAccessible(false);
            return userAgentString;
        } catch (Exception e) {
            return new WebView(context).getSettings().getUserAgentString();
        } catch (Throwable th) {
            declaredConstructor.setAccessible(false);
        }
    }

    private static int m242x(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == 1) {
                return 1;
            }
            if (activeNetworkInfo.getType() == 0) {
                return 0;
            }
        }
        return -1;
    }

    private static SharedPreferences m243y(Context context) {
        return context != null ? context.getSharedPreferences("admarvel_preferences", 0) : null;
    }

    public void m244a(AdMarvelAd adMarvelAd) {
        if (adMarvelAd != null && adMarvelAd.getPixels() != null) {
            for (String a : adMarvelAd.getPixels()) {
                m245a(a);
            }
        }
    }

    public void m245a(String str) {
        try {
            if (Version.getAndroidSDKVersion() >= 11) {
                new Thread(new C0246o(this, str)).start();
            } else {
                new C0245n(this, str).execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void m246b(String str) {
        if (str != null && str.length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            Logging.log("firePixel : firing pixelUrl :" + str);
            stringBuilder.append("<html><head></head><body><img src=\"").append(str).append("\" /></body></html>");
            if (stringBuilder != null && stringBuilder.length() > 0) {
                if (!Thread.currentThread().getName().equalsIgnoreCase("main")) {
                    this.f497e = stringBuilder.toString();
                    new Handler(Looper.getMainLooper()).post(new C02292(this));
                } else if (this.f496d != null) {
                    Context context = (Context) this.f496d.get();
                    if (context != null) {
                        WebView webView = new WebView(context);
                        webView.setWebViewClient(new C02271(this));
                        webView.loadDataWithBaseURL(null, stringBuilder.toString(), WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
                    }
                }
            }
        }
    }

    void m247c(String str) {
        Throwable e;
        if (str != null) {
            String str2;
            ArrayList arrayList;
            try {
                if (Version.getAndroidSDKVersion() >= 11) {
                    AdMarvelThreadExecutorService.m597a().m598b().execute(new C0233b());
                } else {
                    new Handler(Looper.getMainLooper()).post(new C0232a());
                }
                AdMarvelXMLReader adMarvelXMLReader = new AdMarvelXMLReader();
                adMarvelXMLReader.parseXMLString(str);
                AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
                if (parsedXMLData.getChildren().containsKey(Constants.NATIVE_AD_TRACKERS_ELEMENT)) {
                    parsedXMLData = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get(Constants.NATIVE_AD_TRACKERS_ELEMENT)).get(0);
                    str2 = (String) parsedXMLData.getAttributes().get(Name.MARK);
                    try {
                        if (parsedXMLData.getChildren().containsKey(Constants.NATIVE_AD_TRACKER_ELEMENT)) {
                            int size = ((ArrayList) parsedXMLData.getChildren().get(Constants.NATIVE_AD_TRACKER_ELEMENT)).size();
                            int i = 0;
                            arrayList = null;
                            while (i < size) {
                                try {
                                    ArrayList arrayList2;
                                    AdMarvelXMLElement adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get(Constants.NATIVE_AD_TRACKER_ELEMENT)).get(i);
                                    if (adMarvelXMLElement != null) {
                                        String data = adMarvelXMLElement.getData();
                                        arrayList2 = arrayList == null ? new ArrayList() : arrayList;
                                        try {
                                            arrayList2.add(data);
                                        } catch (Exception e2) {
                                            e = e2;
                                            arrayList = arrayList2;
                                        }
                                    } else {
                                        arrayList2 = arrayList;
                                    }
                                    i++;
                                    arrayList = arrayList2;
                                } catch (Exception e3) {
                                    e = e3;
                                }
                            }
                        } else {
                            arrayList = null;
                        }
                    } catch (Exception e4) {
                        e = e4;
                        arrayList = null;
                        Logging.log(Log.getStackTraceString(e));
                        if (str2 != null) {
                        }
                        return;
                    }
                }
                arrayList = null;
                str2 = null;
            } catch (Exception e5) {
                e = e5;
                arrayList = null;
                str2 = null;
                Logging.log(Log.getStackTraceString(e));
                if (str2 != null) {
                    return;
                }
            }
            if (str2 != null && str2.length() > 0) {
                Context context = this.f496d != null ? (Context) this.f496d.get() : null;
                if (context != null && !m192a(context, str2) && arrayList != null) {
                    try {
                        if (!arrayList.isEmpty()) {
                            if ("mounted".equals(Environment.getExternalStorageState())) {
                                File file = new File(Environment.getExternalStorageDirectory(), ReadOnlyContext.SEPARATOR + m205d("adm_tracker_dir") + ReadOnlyContext.SEPARATOR + m205d(str2));
                                file.getParentFile().mkdirs();
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                                objectOutputStream.writeObject(arrayList);
                                objectOutputStream.close();
                            }
                        }
                    } catch (Throwable e6) {
                        Logging.log(Log.getStackTraceString(e6));
                    }
                }
            }
        }
    }
}
