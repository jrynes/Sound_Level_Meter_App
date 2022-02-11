package com.mologiq.analytics;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import com.mologiq.analytics.AdEventsAndroidPostData.AdEventsAndroidPostData;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.mologiq.analytics.o */
final class RegisterAdEventAsyncTask extends AsyncTask<Object, Object, Object> {
    private final List<RegisterAdEventAsyncTask> f2158a;
    private final Map<String, String> f2159b;
    private final String f2160c;
    private final long f2161d;
    private final String f2162e;
    private final WeakReference<Context> f2163f;

    /* renamed from: com.mologiq.analytics.o.a */
    enum RegisterAdEventAsyncTask {
        REQUEST,
        IMPRESSION,
        CLICK
    }

    RegisterAdEventAsyncTask(Context context, List<RegisterAdEventAsyncTask> list, Map<String, Object> map, String str, long j, String str2) {
        this.f2163f = new WeakReference(context);
        this.f2158a = list;
        this.f2159b = new HashMap();
        for (String str3 : map.keySet()) {
            if (map.get(str3) instanceof String) {
                this.f2159b.put(str3, (String) map.get(str3));
            }
        }
        this.f2160c = str;
        this.f2161d = j;
        this.f2162e = str2;
    }

    protected final Object doInBackground(Object... objArr) {
        try {
            Context context = (Context) this.f2163f.get();
            if (!(context == null || UserSettings.m1806d(context).m1813b())) {
                try {
                    Context context2 = (Context) this.f2163f.get();
                    if (context2 != null) {
                        int i = Utils.m1920a(context2).equals("wifi") ? 1 : 0;
                        AdEventsAndroidPostData adEventsAndroidPostData = new AdEventsAndroidPostData();
                        AdvertisingId a = AdvertisingId.m1714a(context2);
                        String a2 = a.m1716a();
                        boolean b = a.m1717b();
                        if (a2 == null || a2.length() <= 0) {
                            adEventsAndroidPostData.m1707f(Secure.getString(context2.getContentResolver(), "android_id"));
                        } else {
                            adEventsAndroidPostData.m1706e(a2);
                            adEventsAndroidPostData.m1700a(b);
                        }
                        adEventsAndroidPostData.m1699a(Version.VERSION);
                        adEventsAndroidPostData.m1702b(Version.DATE);
                        adEventsAndroidPostData.m1704c(context2.getPackageName() == null ? Stomp.EMPTY : context2.getPackageName());
                        adEventsAndroidPostData.getClass();
                        AdEventsAndroidPostData adEventsAndroidPostData2 = new AdEventsAndroidPostData(adEventsAndroidPostData);
                        adEventsAndroidPostData2.m1691a(Long.valueOf(System.currentTimeMillis()));
                        adEventsAndroidPostData2.m1692a(this.f2160c);
                        adEventsAndroidPostData2.m1694b(Long.valueOf(this.f2161d));
                        adEventsAndroidPostData2.m1695b(context2.getPackageName());
                        if (this.f2159b != null && this.f2159b.size() > 0) {
                            List arrayList = new ArrayList();
                            for (String str : this.f2159b.keySet()) {
                                a2 = (String) this.f2159b.get(str);
                                adEventsAndroidPostData2.getClass();
                                AdEventsAndroidPostData.AdEventsAndroidPostData adEventsAndroidPostData3 = new AdEventsAndroidPostData.AdEventsAndroidPostData(adEventsAndroidPostData2);
                                adEventsAndroidPostData3.m1687a(str);
                                adEventsAndroidPostData3.m1689b(a2);
                                arrayList.add(adEventsAndroidPostData3);
                            }
                            adEventsAndroidPostData2.m1693a(arrayList);
                        }
                        adEventsAndroidPostData.m1705d(this.f2162e);
                        adEventsAndroidPostData.m1697a(i);
                        for (RegisterAdEventAsyncTask registerAdEventAsyncTask : this.f2158a) {
                            if (registerAdEventAsyncTask.equals(RegisterAdEventAsyncTask.REQUEST)) {
                                adEventsAndroidPostData.m1701b(adEventsAndroidPostData2);
                            } else if (registerAdEventAsyncTask.equals(RegisterAdEventAsyncTask.IMPRESSION)) {
                                adEventsAndroidPostData.m1698a(adEventsAndroidPostData2);
                            } else if (registerAdEventAsyncTask.equals(RegisterAdEventAsyncTask.CLICK)) {
                                adEventsAndroidPostData.m1703c(adEventsAndroidPostData2);
                            }
                        }
                        a2 = adEventsAndroidPostData.m1696a(context2);
                        if (a2 != null && a2.length() > 0) {
                            Utils utils = new Utils(context2);
                            UserSettings d = UserSettings.m1806d(context2);
                            if (d.m1823k()) {
                                Utils.m1921a(d.m1816d(), a2, context2, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, true);
                            }
                        }
                    }
                } catch (Exception e) {
                    Utils.m1924a(e.getStackTrace().toString());
                }
            }
        } catch (Throwable e2) {
            Utils.m1924a(Utils.m1922a(e2));
        }
        return null;
    }
}
