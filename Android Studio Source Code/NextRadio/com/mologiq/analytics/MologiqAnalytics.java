package com.mologiq.analytics;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings.Secure;
import com.mologiq.analytics.RegisterAdEventAsyncTask.RegisterAdEventAsyncTask;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;

public class MologiqAnalytics {
    private static MologiqAnalytics f2085b;
    private static final AtomicLong f2086c;
    private final WeakReference<Context> f2087a;

    static {
        f2086c = new AtomicLong(0);
    }

    private MologiqAnalytics(Context c) {
        this.f2087a = new WeakReference(c);
    }

    private static Map<String, Object> m1681a(List<AppInstallCampaign> list, List<Integer> list2) {
        Map<String, Object> map = null;
        for (AppInstallCampaign appInstallCampaign : list) {
            Object obj = 1;
            if (appInstallCampaign.f2124d.size() > 0) {
                Set hashSet;
                hashSet = new HashSet(list2);
                hashSet.retainAll(appInstallCampaign.f2124d);
                if (hashSet.isEmpty()) {
                    obj = null;
                }
            }
            if (appInstallCampaign.f2123c.size() > 0) {
                hashSet = new HashSet(list2);
                hashSet.retainAll(appInstallCampaign.f2123c);
                if (!hashSet.isEmpty()) {
                    obj = null;
                }
            }
            if (obj != null) {
                if (map == null) {
                    map = new HashMap();
                }
                for (TargetParam targetParam : appInstallCampaign.f2125e) {
                    map.put(targetParam.m1785a(), targetParam.m1787b());
                }
            }
        }
        return map;
    }

    public static MologiqAnalytics getInstance(Context c) {
        if (f2085b == null) {
            f2085b = new MologiqAnalytics(c);
        }
        return f2085b;
    }

    public void enableAppInstallCheck(Context context, boolean isappInstallCheckEnabled) {
        try {
            UserState b = UserState.m1852b();
            b.m1768a(context);
            b.m1876c(isappInstallCheckEnabled);
            b.m1770b(context);
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    public void enableLocationCheck(Context context, boolean islocationCheckEnabled) {
        try {
            UserState b = UserState.m1852b();
            b.m1768a(context);
            b.m1881d(islocationCheckEnabled);
            b.m1770b(context);
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    public void enableNetworkCheck(Context context, boolean isNetworkCheckEnable) {
        try {
            UserState b = UserState.m1852b();
            b.m1768a(context);
            b.m1871b(isNetworkCheckEnable);
            b.m1770b(context);
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    public Map<String, Object> getEnhancedTargetParams(String siteId, Map<String, Object> targetParams) {
        try {
            Context context = (Context) this.f2087a.get();
            if (context == null) {
                return null;
            }
            UserSettings d = UserSettings.m1806d(context);
            if (d.m1813b()) {
                return null;
            }
            Map a;
            if (targetParams == null) {
                targetParams = new HashMap();
            }
            UserState b = UserState.m1852b();
            b.m1768a(context);
            if (d.m1831s() != null && d.m1831s().size() > 0) {
                a = m1681a(d.m1831s(), b.m1915w());
                if (a != null) {
                    targetParams.putAll(a);
                }
            }
            ServerSideTargetParamsCache b2 = ServerSideTargetParamsCache.m1777b();
            b2.m1768a(context);
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - b2.m1784d() > d.m1824l()) {
                String a2;
                Utils utils = new Utils(context);
                TargetParamAndroidPostData targetParamAndroidPostData = new TargetParamAndroidPostData();
                AdvertisingId a3 = AdvertisingId.m1714a(context);
                String a4 = a3.m1716a();
                boolean b3 = a3.m1717b();
                if (a4 == null || a4.length() <= 0) {
                    targetParamAndroidPostData.m1804f(Secure.getString(context.getContentResolver(), "android_id"));
                } else {
                    targetParamAndroidPostData.m1803e(a4);
                    targetParamAndroidPostData.m1794a(b3);
                }
                targetParamAndroidPostData.m1796b(Version.VERSION);
                targetParamAndroidPostData.m1799c(Version.DATE);
                targetParamAndroidPostData.m1792a(context.getPackageName() == null ? Stomp.EMPTY : context.getPackageName());
                targetParamAndroidPostData.m1801d(siteId);
                if (d.m1819g()) {
                    targetParamAndroidPostData.m1797b(true);
                    LocationManager locationManager = (LocationManager) context.getSystemService("location");
                    Location lastKnownLocation = Utils.m1925a(context, "android.permission.ACCESS_FINE_LOCATION") ? locationManager.getLastKnownLocation("gps") : (Utils.m1925a(context, "android.permission.ACCESS_FINE_LOCATION") || Utils.m1925a(context, "android.permission.ACCESS_COARSE_LOCATION")) ? locationManager.getLastKnownLocation("network") : null;
                    if (lastKnownLocation != null) {
                        targetParamAndroidPostData.m1790a(lastKnownLocation.getLatitude());
                        targetParamAndroidPostData.m1795b(lastKnownLocation.getLongitude());
                        targetParamAndroidPostData.m1800d((double) lastKnownLocation.getAccuracy());
                        targetParamAndroidPostData.m1798c(lastKnownLocation.getAltitude());
                        targetParamAndroidPostData.m1802e((double) lastKnownLocation.getSpeed());
                        targetParamAndroidPostData.m1791a(lastKnownLocation.getTime());
                    }
                }
                if (targetParams != null && targetParams.size() > 0) {
                    List arrayList = new ArrayList();
                    for (String a22 : targetParams.keySet()) {
                        if (targetParams.get(a22) instanceof String) {
                            TargetParam targetParam = new TargetParam();
                            targetParam.m1786a(a22);
                            targetParam.m1788b((String) targetParams.get(a22));
                            arrayList.add(targetParam);
                        }
                    }
                    if (arrayList.size() > 0) {
                        targetParamAndroidPostData.m1793a(arrayList);
                    }
                }
                String a5 = targetParamAndroidPostData.m1789a(context);
                if (a5 != null && a5.length() > 0) {
                    a22 = Utils.m1921a(d.m1818f(), a5, context, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, true);
                    if (a22 != null && a22.length() > 0) {
                        b2.m1781b(a22);
                    }
                    b2.m1779a(currentTimeMillis);
                    b2.m1770b(context);
                }
            }
            a = b2.m1782c();
            if (!(targetParams == null || a == null)) {
                targetParams.putAll(a);
            }
            return targetParams;
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
            return null;
        }
    }

    public void onAdClick(String siteId, int bannerid, Map<String, Object> targetParams, String str, String ipAddress) {
        try {
            Context context = (Context) this.f2087a.get();
            if (context != null) {
                List arrayList = new ArrayList();
                arrayList.add(RegisterAdEventAsyncTask.CLICK);
                if (Version.m1682a() >= 11) {
                    new Handler(context.getMainLooper()).post(new RegisterAdEventAsyncTask(context, arrayList, targetParams, siteId, bannerid, ipAddress));
                } else {
                    new Handler(context.getMainLooper()).post(new RegisterAdEventAsyncTask(context, arrayList, targetParams, siteId, bannerid, ipAddress));
                }
            }
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    public void onFailedToReceiveAd(String siteId, int bannerid, Map<String, Object> targetParams, String ipAddress) {
        try {
            Context context = (Context) this.f2087a.get();
            if (context != null) {
                List arrayList = new ArrayList();
                arrayList.add(RegisterAdEventAsyncTask.REQUEST);
                if (Version.m1682a() >= 11) {
                    new Handler(context.getMainLooper()).post(new RegisterAdEventAsyncTask(context, arrayList, targetParams, siteId, bannerid, ipAddress));
                } else {
                    new Handler(context.getMainLooper()).post(new RegisterAdEventAsyncTask(context, arrayList, targetParams, siteId, bannerid, ipAddress));
                }
            }
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    public void onPause() {
        Utils.m1924a("========= ON Pause called");
        try {
            Context context = (Context) this.f2087a.get();
            AdvertisingId.f2115a = null;
            if (context != null) {
            }
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    public void onReceiveAd(String siteId, int bannerid, Map<String, Object> targetParams, String ipAddress) {
        try {
            Context context = (Context) this.f2087a.get();
            if (context != null) {
                List arrayList = new ArrayList();
                arrayList.add(RegisterAdEventAsyncTask.REQUEST);
                arrayList.add(RegisterAdEventAsyncTask.IMPRESSION);
                if (Version.m1682a() >= 11) {
                    new Handler(context.getMainLooper()).post(new RegisterAdEventAsyncTask(context, arrayList, targetParams, siteId, bannerid, ipAddress));
                } else {
                    new Handler(context.getMainLooper()).post(new RegisterAdEventAsyncTask(context, arrayList, targetParams, siteId, bannerid, ipAddress));
                }
            }
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    public void onResume() {
        Utils.m1924a("========= ON Resume called");
        try {
            if (System.currentTimeMillis() - f2086c.getAndSet(System.currentTimeMillis()) > 5000) {
                Context context = (Context) this.f2087a.get();
                if (context != null) {
                    if (Version.m1682a() >= 11) {
                        new Handler(context.getMainLooper()).post(new MologiqDeviceEventsAsyncTask(context));
                    } else {
                        new Handler(context.getMainLooper()).post(new MologiqDeviceEventsAsyncTask(context));
                    }
                }
            }
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    public void onStart() {
        Utils.m1924a("========= ON Start called");
    }

    public void onStop() {
        Utils.m1924a("========= ON Stop called");
    }
}
