package com.mologiq.analytics;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.GZIPOutputStream;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.mologiq.analytics.w */
final class Utils {
    static boolean f2272a;
    static boolean f2273b;
    private static ReadWriteLock f2274d;
    private final WeakReference<Context> f2275c;

    /* renamed from: com.mologiq.analytics.w.1 */
    class Utils implements Comparator<Entry<Integer, Integer>> {
        private final /* synthetic */ boolean f2269a;

        Utils(boolean z) {
            this.f2269a = z;
        }

        public final /* synthetic */ int compare(Object obj, Object obj2) {
            Entry entry = (Entry) obj;
            Entry entry2 = (Entry) obj2;
            return this.f2269a ? ((Integer) entry.getValue()).compareTo((Integer) entry2.getValue()) : ((Integer) entry2.getValue()).compareTo((Integer) entry.getValue());
        }
    }

    /* renamed from: com.mologiq.analytics.w.a */
    static class Utils {
        byte[] f2270a;
        int f2271b;

        Utils() {
            this.f2270a = null;
            this.f2271b = 0;
        }
    }

    static {
        f2274d = new ReentrantReadWriteLock();
        f2272a = true;
        f2273b = false;
    }

    Utils(Context context) {
        this.f2275c = new WeakReference(context);
    }

    private String m1919a() {
        Constructor declaredConstructor;
        Context context = (Context) this.f2275c.get();
        if (context == null) {
            return Utils.m1926b();
        }
        try {
            declaredConstructor = WebSettings.class.getDeclaredConstructor(new Class[]{Context.class, WebView.class});
            declaredConstructor.setAccessible(true);
            String userAgentString = ((WebSettings) declaredConstructor.newInstance(new Object[]{context.getApplicationContext(), null})).getUserAgentString();
            declaredConstructor.setAccessible(false);
            return userAgentString;
        } catch (Exception e) {
            return Thread.currentThread().getName().equalsIgnoreCase("main") ? new WebView(context).getSettings().getUserAgentString() : Utils.m1926b();
        } catch (Throwable th) {
            declaredConstructor.setAccessible(false);
        }
    }

    static String m1920a(Context context) {
        if (!Utils.m1925a(context, "android.permission.ACCESS_NETWORK_STATE")) {
            return DeviceInfo.ORIENTATION_UNKNOWN;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return connectivityManager.getNetworkInfo(1).isAvailable() ? "wifi" : connectivityManager.getNetworkInfo(0).isAvailable() ? "mobile" : "none";
    }

    static String m1921a(String str, String str2, Context context, int i, int i2, boolean z) {
        try {
            AESEncryption aESEncryption;
            byte[] toByteArray;
            String stringBuilder = new StringBuilder(String.valueOf(str)).append("?v=1.4.4").append("&p=").append(context.getPackageName() == null ? Stomp.EMPTY : context.getPackageName()).toString();
            Utils.m1924a(stringBuilder);
            URL url = new URL(stringBuilder);
            Utils.m1924a(str2);
            if (z) {
                RSAEncryption rSAEncryption = new RSAEncryption();
                AESEncryption aESEncryption2 = new AESEncryption();
                str2 = rSAEncryption.m1772a(aESEncryption2.m1684a()) + aESEncryption2.m1685a(str2);
                aESEncryption = aESEncryption2;
            } else {
                aESEncryption = null;
            }
            long currentTimeMillis = System.currentTimeMillis();
            Utils utils = new Utils(context);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(HttpRequest.METHOD_POST);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty(HttpRequest.HEADER_USER_AGENT, utils.m1919a());
            httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
            httpURLConnection.setConnectTimeout(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH);
            httpURLConnection.setReadTimeout(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
            if (str2 == null || str2.length() <= 0) {
                httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, Integer.toString(0));
            } else {
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream(str2.length());
                GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                gZIPOutputStream.write(str2.getBytes());
                gZIPOutputStream.close();
                toByteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, Integer.toString(toByteArray.length));
                httpURLConnection.getOutputStream().write(toByteArray);
            }
            httpURLConnection.getOutputStream().flush();
            int responseCode = httpURLConnection.getResponseCode();
            int contentLength = httpURLConnection.getContentLength();
            Utils.m1924a("Error Code: " + responseCode);
            Utils.m1924a("Content Length: " + contentLength);
            Utils.m1924a("Time Taken: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
            if (responseCode == AMQP.REPLY_SUCCESS) {
                InputStream inputStream = (InputStream) httpURLConnection.getContent();
                if (inputStream != null) {
                    List arrayList = new ArrayList();
                    responseCode = Flags.FLAG2;
                    int i3 = 0;
                    while (responseCode != -1) {
                        toByteArray = new byte[Flags.FLAG2];
                        responseCode = inputStream.read(toByteArray, 0, Flags.FLAG2);
                        if (responseCode > 0) {
                            Utils utils2 = new Utils();
                            utils2.f2270a = toByteArray;
                            utils2.f2271b = responseCode;
                            contentLength = i3 + responseCode;
                            arrayList.add(utils2);
                            i3 = contentLength;
                        }
                    }
                    inputStream.close();
                    if (i3 > 0) {
                        byte[] bArr = new byte[i3];
                        responseCode = 0;
                        for (contentLength = 0; contentLength < arrayList.size(); contentLength++) {
                            Utils utils3 = (Utils) arrayList.get(contentLength);
                            int i4 = 0;
                            while (i4 < utils3.f2271b && responseCode < i3) {
                                int i5 = responseCode + 1;
                                bArr[responseCode] = utils3.f2270a[i4];
                                i4++;
                                responseCode = i5;
                            }
                        }
                        String str3 = new String(bArr);
                        if (str3.length() > 0) {
                            if (!z || aESEncryption == null) {
                                Utils.m1924a("Return Value: " + new String(str3));
                                return str3;
                            }
                            str3 = AESEncryption.m1683a(aESEncryption.m1684a(), str3);
                            Utils.m1924a("Return Value: " + new String(str3));
                            return str3;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Stomp.EMPTY;
    }

    static String m1922a(Throwable th) {
        Writer stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    static Map<Integer, Integer> m1923a(Map<Integer, Integer> map, boolean z) {
        List<Entry> linkedList = new LinkedList(map.entrySet());
        Collections.sort(linkedList, new Utils(z));
        Map<Integer, Integer> linkedHashMap = new LinkedHashMap();
        for (Entry entry : linkedList) {
            linkedHashMap.put((Integer) entry.getKey(), (Integer) entry.getValue());
        }
        return linkedHashMap;
    }

    static void m1924a(String str) {
        if (Log.isLoggable("mqaabb", 2)) {
            Log.d("mqaabb", str);
        }
    }

    static boolean m1925a(Context context, String str) {
        return context.checkCallingOrSelfPermission(str) == 0;
    }

    private static String m1926b() {
        Object stringBuilder;
        String str = Stomp.EMPTY;
        String str2 = VERSION.RELEASE;
        str = new StringBuilder(String.valueOf(str2.length() > 0 ? new StringBuilder(String.valueOf(str)).append(str2).toString() : new StringBuilder(String.valueOf(str)).append(Stomp.V1_0).toString())).append("; ").toString();
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        if (language != null) {
            stringBuilder = new StringBuilder(String.valueOf(str)).append(language.toLowerCase()).toString();
            str2 = locale.getCountry();
            if (str2 != null) {
                stringBuilder = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(stringBuilder)).append("-").toString())).append(str2.toLowerCase()).toString();
            }
        } else {
            stringBuilder = new StringBuilder(String.valueOf(str)).append("en").toString();
        }
        str2 = Build.MODEL;
        if (str2.length() > 0) {
            stringBuilder = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(stringBuilder)).append("; ").toString())).append(str2).toString();
        }
        str2 = Build.ID;
        if (str2.length() > 0) {
            stringBuilder = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(stringBuilder)).append(" Build/").toString())).append(str2).toString();
        }
        return String.format("Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2", new Object[]{stringBuilder});
    }

    static boolean m1927b(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        return context.getPackageManager().queryIntentActivities(intent, AccessibilityNodeInfoCompat.ACTION_CUT).size() > 0;
    }
}
