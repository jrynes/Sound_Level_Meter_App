package com.mologiq.analytics;

import android.content.Context;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.mologiq.analytics.MeanListData.MeanListData;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connected;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.mologiq.analytics.u */
final class UserSettings extends PersistentObject {
    private static final UserSettings f2199x;
    private static final AtomicBoolean f2200y;
    private long f2201a;
    private long f2202b;
    private String f2203c;
    private String f2204d;
    private String f2205e;
    private String f2206f;
    private int f2207g;
    private boolean f2208h;
    private boolean f2209i;
    private boolean f2210j;
    private boolean f2211k;
    private boolean f2212l;
    private boolean f2213m;
    private int f2214n;
    private int f2215o;
    private long f2216p;
    private long f2217q;
    private long f2218r;
    private long f2219s;
    private long f2220t;
    private MeanListData f2221u;
    private List<AppInstallCampaign> f2222v;
    private Map<Integer, AppUrlInfo> f2223w;

    static {
        f2199x = new UserSettings("deviceEventResponse");
        f2200y = new AtomicBoolean(false);
    }

    private UserSettings(String str) {
        super(str);
        this.f2201a = 0;
        this.f2202b = 0;
        this.f2203c = "http://a.mologiq.net/mologiq/aea";
        this.f2204d = "http://a.mologiq.net/mologiq/deai";
        this.f2205e = "http://a.mologiq.net/mologiq/dea";
        this.f2206f = "http://a.mologiq.net/mologiq/et";
        this.f2207g = 100;
        this.f2208h = true;
        this.f2209i = true;
        this.f2210j = false;
        this.f2211k = false;
        this.f2212l = false;
        this.f2213m = false;
        this.f2216p = 600000;
        this.f2217q = 600000;
        this.f2219s = 600000;
        this.f2218r = 0;
        this.f2220t = 0;
        this.f2215o = 0;
        this.f2221u = MeanListData.m1765a().m1766b();
    }

    private void m1805b(String str) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull("adEventsAndroidUrl")) {
                        this.f2203c = jSONObject.getString("adEventsAndroidUrl");
                    }
                    if (!jSONObject.isNull("deviceEventsInitAndroidUrl")) {
                        this.f2204d = jSONObject.getString("deviceEventsInitAndroidUrl");
                    }
                    if (!jSONObject.isNull("deviceEventsAndroidUrl")) {
                        this.f2205e = jSONObject.getString("deviceEventsAndroidUrl");
                    }
                    if (!jSONObject.isNull("targetParamsAndroidUrl")) {
                        this.f2206f = jSONObject.getString("targetParamsAndroidUrl");
                    }
                    if (!jSONObject.isNull("stopForPeriodInMs")) {
                        this.f2201a = jSONObject.getLong("stopForPeriodInMs");
                    }
                    if (!jSONObject.isNull("locationMask")) {
                        this.f2207g = jSONObject.getInt("locationMask");
                    }
                    if (!jSONObject.isNull("ttlEnhancedTargetParamsInMs")) {
                        this.f2216p = jSONObject.getLong("ttlEnhancedTargetParamsInMs");
                    }
                    if (!jSONObject.isNull("ttlDeviceStateInMs")) {
                        this.f2217q = jSONObject.getLong("ttlDeviceStateInMs");
                    }
                    if (!jSONObject.isNull("ttlAppListSyncInMs")) {
                        this.f2219s = jSONObject.getLong("ttlAppListSyncInMs");
                    }
                    if (!jSONObject.isNull("deviceStateTimestamp")) {
                        this.f2218r = jSONObject.getLong("deviceStateTimestamp");
                    }
                    if (!jSONObject.isNull("appListSyncTimestamp")) {
                        this.f2220t = jSONObject.getLong("appListSyncTimestamp");
                    }
                    if (jSONObject.isNull("enableLocation")) {
                        this.f2208h = false;
                    } else {
                        this.f2208h = jSONObject.getBoolean("enableLocation");
                    }
                    if (jSONObject.isNull("enableInstalledApps")) {
                        this.f2209i = false;
                    } else {
                        this.f2209i = jSONObject.getBoolean("enableInstalledApps");
                    }
                    if (jSONObject.isNull("enableNetworkInfo")) {
                        this.f2210j = false;
                    } else {
                        this.f2210j = jSONObject.getBoolean("enableNetworkInfo");
                    }
                    if (jSONObject.isNull("enableDeviceInfo")) {
                        this.f2211k = false;
                    } else {
                        this.f2211k = jSONObject.getBoolean("enableDeviceInfo");
                    }
                    if (jSONObject.isNull("enableAudience")) {
                        this.f2212l = false;
                    } else {
                        this.f2212l = jSONObject.getBoolean("enableAudience");
                    }
                    if (jSONObject.isNull("enableAdEvent")) {
                        this.f2213m = false;
                    } else {
                        this.f2213m = jSONObject.getBoolean("enableAdEvent");
                    }
                    if (jSONObject.isNull("totalNoOfCategory")) {
                        this.f2214n = 0;
                    } else {
                        this.f2214n = jSONObject.getInt("totalNoOfCategory");
                    }
                    if (jSONObject.isNull("policy")) {
                        this.f2215o = 1;
                    } else {
                        this.f2215o = jSONObject.getInt("policy");
                    }
                    if (!jSONObject.isNull("appfilters")) {
                        List arrayList;
                        int i;
                        int i2;
                        JSONObject jSONObject2 = jSONObject.getJSONObject("appfilters");
                        if (!jSONObject2.isNull("meandata")) {
                            jSONObject = jSONObject2.getJSONObject("meandata");
                            MeanListData a = MeanListData.m1765a();
                            a.getClass();
                            MeanListData meanListData = new MeanListData(a);
                            meanListData.m1761a(jSONObject.getString(Connected.VERSION));
                            meanListData.m1760a(jSONObject.getInt("appcount"));
                            if (!jSONObject.isNull("totalNoOfCategory")) {
                                this.f2214n = jSONObject.getInt("totalNoOfCategory");
                            }
                            JSONArray jSONArray = jSONObject.getJSONArray("classifications");
                            if (jSONArray != null && jSONArray.length() > 0) {
                                arrayList = new ArrayList();
                                for (i = 0; i < jSONArray.length(); i++) {
                                    a = MeanListData.m1765a();
                                    a.getClass();
                                    MeanListData meanListData2 = new MeanListData(a);
                                    JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                                    meanListData2.m1754a(jSONObject3.getInt(Name.MARK));
                                    meanListData2.m1757b(jSONObject3.getInt("mean"));
                                    JSONArray jSONArray2 = jSONObject3.getJSONArray("apps");
                                    List arrayList2 = new ArrayList();
                                    for (i2 = 0; i2 < jSONArray2.length(); i2++) {
                                        arrayList2.add(Integer.valueOf(jSONArray2.getInt(i2)));
                                    }
                                    meanListData2.m1755a(arrayList2);
                                    arrayList.add(meanListData2);
                                }
                                meanListData.m1762a(arrayList);
                            }
                            this.f2221u = meanListData;
                        }
                        if (!jSONObject2.isNull("appinstallcampaigns")) {
                            if (this.f2222v == null) {
                                this.f2222v = new ArrayList();
                            }
                            JSONArray jSONArray3 = jSONObject2.getJSONArray("appinstallcampaigns");
                            for (i2 = 0; i2 < jSONArray3.length(); i2++) {
                                JSONObject jSONObject4 = jSONArray3.getJSONObject(i2);
                                AppInstallCampaign appInstallCampaign = new AppInstallCampaign();
                                appInstallCampaign.f2121a = Integer.valueOf(jSONObject4.getInt(Name.MARK));
                                appInstallCampaign.f2122b = Integer.valueOf(jSONObject4.getInt("userid"));
                                if (!jSONObject4.isNull("isnotinstalled")) {
                                    JSONArray jSONArray4 = jSONObject4.getJSONArray("isnotinstalled");
                                    List arrayList3 = new ArrayList();
                                    for (i = 0; i < jSONArray4.length(); i++) {
                                        arrayList3.add(Integer.valueOf(jSONArray4.getInt(i)));
                                    }
                                    appInstallCampaign.f2123c = arrayList3;
                                }
                                if (!jSONObject4.isNull("isinstalled")) {
                                    arrayList = new ArrayList();
                                    JSONArray jSONArray5 = jSONObject4.getJSONArray("isinstalled");
                                    for (i = 0; i < jSONArray5.length(); i++) {
                                        arrayList.add(Integer.valueOf(jSONArray5.getInt(i)));
                                    }
                                    appInstallCampaign.f2124d = arrayList;
                                }
                                if (!jSONObject4.isNull("tp")) {
                                    arrayList = new ArrayList();
                                    JSONArray jSONArray6 = jSONObject4.getJSONArray("tp");
                                    for (i = 0; i < jSONArray6.length(); i++) {
                                        JSONObject jSONObject5 = jSONArray6.getJSONObject(i);
                                        TargetParam targetParam = new TargetParam();
                                        targetParam.m1786a(jSONObject5.getString("n"));
                                        targetParam.m1788b(jSONObject5.getString("v"));
                                        arrayList.add(targetParam);
                                    }
                                    appInstallCampaign.f2125e = arrayList;
                                }
                                this.f2222v.add(appInstallCampaign);
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                Utils.m1924a(Utils.m1922a(th));
            }
        }
    }

    static UserSettings m1806d(Context context) {
        if (f2200y.compareAndSet(false, true)) {
            f2199x.m1768a(context);
        }
        return f2199x;
    }

    protected final String m1807a() {
        JSONArray jSONArray;
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        if (this.f2203c != null) {
            jSONObject2.put("adEventsAndroidUrl", this.f2203c);
        }
        if (this.f2205e != null) {
            jSONObject2.put("deviceEventsAndroidUrl", this.f2205e);
        }
        if (this.f2204d != null) {
            jSONObject2.put("deviceEventsInitAndroidUrl", this.f2204d);
        }
        if (this.f2206f != null) {
            jSONObject2.put("targetParamsAndroidUrl", this.f2206f);
        }
        jSONObject2.put("stopForPeriodInMs", this.f2201a);
        jSONObject2.put("locationMask", this.f2207g);
        jSONObject2.put("ttlEnhancedTargetParamsInMs", this.f2216p);
        jSONObject2.put("ttlDeviceStateInMs", this.f2217q);
        jSONObject2.put("deviceStateTimestamp", this.f2218r);
        jSONObject2.put("ttlAppListSyncInMs", this.f2219s);
        jSONObject2.put("appListSyncTimestamp", this.f2220t);
        jSONObject2.put("enableLocation", this.f2208h);
        jSONObject2.put("enableInstalledApps", this.f2209i);
        jSONObject2.put("enableNetworkInfo", this.f2210j);
        jSONObject2.put("enableDeviceInfo", this.f2211k);
        jSONObject2.put("enableAudience", this.f2212l);
        jSONObject2.put("enableAdEvent", this.f2213m);
        jSONObject2.put("policy", this.f2215o);
        JSONObject jSONObject3 = new JSONObject();
        JSONObject jSONObject4 = new JSONObject();
        if (this.f2221u != null) {
            jSONObject4.put(Connected.VERSION, this.f2221u.m1759a());
            jSONObject4.put("appcount", this.f2221u.m1763b());
            jSONObject4.put("totalNoOfCategory", this.f2214n);
            JSONArray jSONArray2 = new JSONArray();
            if (this.f2221u.m1764c() != null && this.f2221u.m1764c().size() > 0) {
                for (MeanListData meanListData : this.f2221u.m1764c()) {
                    JSONObject jSONObject5 = new JSONObject();
                    jSONObject5.put(Name.MARK, meanListData.m1753a());
                    jSONObject5.put("mean", meanListData.m1756b());
                    jSONObject5.put("apps", new JSONArray(meanListData.m1758c()));
                    jSONArray2.put(jSONObject5);
                }
            }
            jSONObject4.put("classifications", jSONArray2);
            jSONObject3.put("meandata", jSONObject4);
        }
        if (this.f2222v != null) {
            jSONArray = new JSONArray();
            for (AppInstallCampaign appInstallCampaign : this.f2222v) {
                jSONObject = new JSONObject();
                jSONObject.put(Name.MARK, appInstallCampaign.f2121a);
                jSONObject.put("userid", appInstallCampaign.f2122b);
                jSONObject.put("isnotinstalled", new JSONArray(appInstallCampaign.f2123c));
                jSONObject.put("isinstalled", new JSONArray(appInstallCampaign.f2124d));
                if (appInstallCampaign.f2125e != null) {
                    JSONArray jSONArray3 = new JSONArray();
                    for (TargetParam targetParam : appInstallCampaign.f2125e) {
                        JSONObject jSONObject6 = new JSONObject();
                        jSONObject6.put("n", targetParam.m1785a());
                        jSONObject6.put("v", targetParam.m1787b());
                        jSONArray3.put(jSONObject6);
                    }
                    jSONObject.put("tp", jSONArray3);
                }
                jSONArray.put(jSONObject);
            }
            jSONObject3.put("appinstallcampaigns", jSONArray);
        }
        if (this.f2223w != null) {
            jSONArray = new JSONArray();
            for (AppUrlInfo appUrlInfo : this.f2223w.values()) {
                jSONObject = new JSONObject();
                jSONObject.put(Name.MARK, appUrlInfo.f2126a);
                jSONObject.put(SettingsJsonConstants.APP_URL_KEY, appUrlInfo.f2127b);
                jSONArray.put(jSONObject);
            }
            jSONObject2.put("appurls", jSONArray);
        }
        jSONObject2.put("appfilters", jSONObject3);
        return jSONObject2.toString();
    }

    final void m1808a(int i) {
        this.f2215o = i;
    }

    final void m1809a(long j) {
        this.f2218r = j;
    }

    protected final void m1810a(String str) {
        if (!m1813b()) {
            this.f2202b = System.currentTimeMillis();
        }
        if (str != null && str.length() > 0) {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.isNull("core")) {
                m1805b(jSONObject.getJSONObject("core").toString());
            }
        }
    }

    protected final void m1811a(String str, Context context) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    JSONObject jSONObject;
                    int i;
                    JSONObject jSONObject2;
                    JSONObject jSONObject3 = new JSONObject(str);
                    if (!jSONObject3.isNull("adEventsAndroidUrl")) {
                        this.f2203c = jSONObject3.getString("adEventsAndroidUrl");
                    }
                    if (!jSONObject3.isNull("deviceEventsInitAndroidUrl")) {
                        this.f2204d = jSONObject3.getString("deviceEventsInitAndroidUrl");
                    }
                    if (!jSONObject3.isNull("deviceEventsAndroidUrl")) {
                        this.f2205e = jSONObject3.getString("deviceEventsAndroidUrl");
                    }
                    if (!jSONObject3.isNull("targetParamsAndroidUrl")) {
                        this.f2206f = jSONObject3.getString("targetParamsAndroidUrl");
                    }
                    if (!jSONObject3.isNull("stopForPeriodInMs")) {
                        this.f2201a = jSONObject3.getLong("stopForPeriodInMs");
                    }
                    if (!jSONObject3.isNull("locationMask")) {
                        this.f2207g = jSONObject3.getInt("locationMask");
                    }
                    if (!jSONObject3.isNull("ttlEnhancedTargetParamsInMs")) {
                        this.f2216p = jSONObject3.getLong("ttlEnhancedTargetParamsInMs");
                    }
                    if (!jSONObject3.isNull("ttlDeviceStateInMs")) {
                        this.f2217q = jSONObject3.getLong("ttlDeviceStateInMs");
                    }
                    if (!jSONObject3.isNull("ttlAppListSyncInMs")) {
                        this.f2219s = jSONObject3.getLong("ttlAppListSyncInMs");
                    }
                    if (!jSONObject3.isNull("deviceStateTimestamp")) {
                        this.f2218r = jSONObject3.getLong("deviceStateTimestamp");
                    }
                    if (!jSONObject3.isNull("appListSyncTimestamp")) {
                        this.f2220t = jSONObject3.getLong("appListSyncTimestamp");
                    }
                    if (jSONObject3.isNull("enableLocation")) {
                        this.f2208h = false;
                    } else {
                        this.f2208h = jSONObject3.getBoolean("enableLocation");
                    }
                    if (jSONObject3.isNull("enableInstalledApps")) {
                        this.f2209i = false;
                    } else {
                        this.f2209i = jSONObject3.getBoolean("enableInstalledApps");
                    }
                    if (jSONObject3.isNull("enableNetworkInfo")) {
                        this.f2210j = false;
                    } else {
                        this.f2210j = jSONObject3.getBoolean("enableNetworkInfo");
                    }
                    if (jSONObject3.isNull("enableDeviceInfo")) {
                        this.f2211k = false;
                    } else {
                        this.f2211k = jSONObject3.getBoolean("enableDeviceInfo");
                    }
                    if (jSONObject3.isNull("enableAudience")) {
                        this.f2212l = false;
                    } else {
                        this.f2212l = jSONObject3.getBoolean("enableAudience");
                    }
                    if (jSONObject3.isNull("enableAdEvent")) {
                        this.f2213m = false;
                    } else {
                        this.f2213m = jSONObject3.getBoolean("enableAdEvent");
                    }
                    if (jSONObject3.isNull("totalNoOfCategory")) {
                        this.f2214n = 0;
                    } else {
                        this.f2214n = jSONObject3.getInt("totalNoOfCategory");
                    }
                    if (jSONObject3.isNull("policy")) {
                        this.f2215o = 0;
                    } else {
                        this.f2215o = jSONObject3.getInt("policy");
                    }
                    if (!jSONObject3.isNull("urlpackagesAndroid")) {
                        jSONObject = jSONObject3.getJSONObject("urlpackagesAndroid");
                        if (!jSONObject.isNull("appUrlPackageAndroid")) {
                            JSONArray jSONArray = jSONObject.getJSONArray("appUrlPackageAndroid");
                            if (jSONArray != null && jSONArray.length() > 0) {
                                for (i = 0; i < jSONArray.length(); i++) {
                                    if (this.f2223w == null) {
                                        this.f2223w = new HashMap();
                                    }
                                    jSONObject2 = jSONArray.getJSONObject(i);
                                    AppUrlInfo appUrlInfo = new AppUrlInfo();
                                    appUrlInfo.f2126a = jSONObject2.getInt(Name.MARK);
                                    appUrlInfo.f2127b = jSONObject2.getString(SettingsJsonConstants.APP_URL_KEY);
                                    this.f2223w.put(Integer.valueOf(appUrlInfo.f2126a), appUrlInfo);
                                }
                            }
                        }
                    }
                    if (!jSONObject3.isNull("appFilters")) {
                        JSONArray jSONArray2;
                        int i2;
                        JSONArray jSONArray3;
                        JSONArray jSONArray4;
                        DatabaseHandler a = DatabaseHandler.m1725a(context);
                        jSONObject2 = jSONObject3.getJSONObject("appFilters");
                        if (!jSONObject2.isNull("appList")) {
                            jSONArray2 = jSONObject2.getJSONArray("appList");
                            if (jSONArray2 != null && jSONArray2.length() > 0) {
                                for (i2 = 0; i2 < jSONArray2.length(); i2++) {
                                    jSONObject = jSONArray2.getJSONObject(i2);
                                    AppInfo appInfo = new AppInfo();
                                    if (!jSONObject.isNull("name")) {
                                        appInfo.m1720a(jSONObject.getString("name"));
                                    }
                                    if (!jSONObject.isNull("classificationid")) {
                                        appInfo.m1719a(jSONObject.getInt("classificationid"));
                                    }
                                    if (!jSONObject.isNull(Name.MARK)) {
                                        appInfo.m1722b(jSONObject.getInt(Name.MARK));
                                    }
                                    a.m1738a(appInfo);
                                }
                            }
                        }
                        if (!jSONObject2.isNull("mean")) {
                            jSONObject = jSONObject2.getJSONObject("mean");
                            MeanListData a2 = MeanListData.m1765a();
                            a2.getClass();
                            MeanListData meanListData = new MeanListData(a2);
                            meanListData.m1761a(jSONObject.getString(Connected.VERSION));
                            meanListData.m1760a(jSONObject.getInt("appcount"));
                            if (!jSONObject.isNull("totalNoOfCategory")) {
                                this.f2214n = jSONObject.getInt("totalNoOfCategory");
                            }
                            jSONArray3 = jSONObject.getJSONArray("classifications");
                            if (jSONArray3 != null && jSONArray3.length() > 0) {
                                List arrayList = new ArrayList();
                                for (i2 = 0; i2 < jSONArray3.length(); i2++) {
                                    MeanListData a3 = MeanListData.m1765a();
                                    a3.getClass();
                                    MeanListData meanListData2 = new MeanListData(a3);
                                    jSONObject = jSONArray3.getJSONObject(i2);
                                    meanListData2.m1754a(jSONObject.getInt(Name.MARK));
                                    meanListData2.m1757b(jSONObject.getInt("mean"));
                                    jSONArray4 = jSONObject.getJSONArray("apps");
                                    List arrayList2 = new ArrayList();
                                    for (i = 0; i < jSONArray4.length(); i++) {
                                        arrayList2.add(Integer.valueOf(jSONArray4.getInt(i)));
                                    }
                                    meanListData2.m1755a(arrayList2);
                                    arrayList.add(meanListData2);
                                    a.m1739a(meanListData2);
                                }
                                meanListData.m1762a(arrayList);
                            }
                            a.m1740a(meanListData);
                            this.f2221u = meanListData;
                        }
                        if (!jSONObject2.isNull("campaigns")) {
                            if (this.f2222v == null) {
                                this.f2222v = new ArrayList();
                            }
                            JSONArray jSONArray5 = jSONObject2.getJSONArray("campaigns");
                            for (i2 = 0; i2 < jSONArray5.length(); i2++) {
                                jSONObject = jSONArray5.getJSONObject(i2);
                                if (!jSONObject.isNull(locationTracking.action)) {
                                    String string = jSONObject.getString(locationTracking.action);
                                    if (string.equals(ProductAction.ACTION_ADD)) {
                                        if (!jSONObject.isNull(MPDbAdapter.KEY_DATA)) {
                                            jSONArray2 = jSONObject.getJSONArray(MPDbAdapter.KEY_DATA);
                                            for (int i3 = 0; i3 < jSONArray2.length(); i3++) {
                                                List arrayList3;
                                                JSONObject jSONObject4 = jSONArray2.getJSONObject(i3);
                                                AppInstallCampaign appInstallCampaign = new AppInstallCampaign();
                                                appInstallCampaign.f2121a = Integer.valueOf(jSONObject4.getInt(Name.MARK));
                                                appInstallCampaign.f2122b = Integer.valueOf(jSONObject4.getInt("userid"));
                                                if (!jSONObject4.isNull("notinstall")) {
                                                    JSONArray jSONArray6 = jSONObject4.getJSONArray("notinstall");
                                                    List arrayList4 = new ArrayList();
                                                    for (i = 0; i < jSONArray6.length(); i++) {
                                                        arrayList4.add(Integer.valueOf(jSONArray6.getInt(i)));
                                                    }
                                                    appInstallCampaign.f2123c = arrayList4;
                                                }
                                                if (!jSONObject4.isNull("install")) {
                                                    arrayList3 = new ArrayList();
                                                    jSONArray4 = jSONObject4.getJSONArray("install");
                                                    for (i = 0; i < jSONArray4.length(); i++) {
                                                        arrayList3.add(Integer.valueOf(jSONArray4.getInt(i)));
                                                    }
                                                    appInstallCampaign.f2124d = arrayList3;
                                                }
                                                if (!jSONObject4.isNull("tp")) {
                                                    arrayList3 = new ArrayList();
                                                    jSONArray3 = jSONObject4.getJSONArray("tp");
                                                    for (i = 0; i < jSONArray3.length(); i++) {
                                                        JSONObject jSONObject5 = jSONArray3.getJSONObject(i);
                                                        TargetParam targetParam = new TargetParam();
                                                        targetParam.m1786a(jSONObject5.getString("n"));
                                                        targetParam.m1788b(jSONObject5.getString("v"));
                                                        arrayList3.add(targetParam);
                                                    }
                                                    appInstallCampaign.f2125e = arrayList3;
                                                }
                                                this.f2222v.add(appInstallCampaign);
                                            }
                                        }
                                    } else if (string.equals("delete")) {
                                        Set hashSet = new HashSet();
                                        if (!jSONObject.isNull(MPDbAdapter.KEY_DATA)) {
                                            jSONArray2 = jSONObject.getJSONArray(MPDbAdapter.KEY_DATA);
                                            for (i = 0; i < jSONArray2.length(); i++) {
                                                hashSet.add(Integer.valueOf(jSONArray2.getJSONObject(i2).getInt(Name.MARK)));
                                            }
                                            List arrayList5 = new ArrayList();
                                            for (AppInstallCampaign appInstallCampaign2 : this.f2222v) {
                                                if (!hashSet.contains(appInstallCampaign2.f2121a)) {
                                                    arrayList5.add(appInstallCampaign2);
                                                }
                                            }
                                            this.f2222v = arrayList5;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                Utils.m1924a(Utils.m1922a(th));
            }
        }
    }

    public final void m1812b(long j) {
        this.f2220t = j;
    }

    final boolean m1813b() {
        return this.f2201a == -1 || System.currentTimeMillis() - this.f2202b < this.f2201a;
    }

    final String m1814c() {
        return this.f2204d;
    }

    final void m1815c(Context context) {
        JSONObject jSONObject = new JSONObject();
        String a = m1807a();
        if (a != null && a.length() > 0) {
            jSONObject.put("core", new JSONObject(a));
        }
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("stopForPeriodTimestamp", this.f2202b);
        a = jSONObject2.toString();
        if (a != null && a.length() > 0) {
            jSONObject.put("extra", new JSONObject(a));
        }
        try {
            DatabaseHandler.m1725a(context).m1741a("deviceEventResponse", jSONObject.toString());
        } catch (Throwable th) {
            Utils.m1924a(Utils.m1922a(th));
        }
    }

    final String m1816d() {
        return this.f2203c;
    }

    final String m1817e() {
        return this.f2205e;
    }

    final String m1818f() {
        return this.f2206f;
    }

    final boolean m1819g() {
        return this.f2208h;
    }

    final boolean m1820h() {
        return this.f2209i;
    }

    final boolean m1821i() {
        return this.f2210j;
    }

    final boolean m1822j() {
        return this.f2212l;
    }

    final boolean m1823k() {
        return this.f2213m;
    }

    final long m1824l() {
        return this.f2216p;
    }

    final long m1825m() {
        return this.f2217q;
    }

    final long m1826n() {
        return this.f2219s;
    }

    final long m1827o() {
        return this.f2218r;
    }

    public final long m1828p() {
        return this.f2220t;
    }

    final int m1829q() {
        return this.f2207g;
    }

    final Map<Integer, AppUrlInfo> m1830r() {
        return this.f2223w;
    }

    final List<AppInstallCampaign> m1831s() {
        return this.f2222v;
    }

    final int m1832t() {
        return this.f2215o;
    }

    public final int m1833u() {
        return this.f2214n;
    }
}
