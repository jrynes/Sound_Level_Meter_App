package com.google.android.gms.analytics;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.zzab;
import com.google.android.gms.analytics.internal.zzad;
import com.google.android.gms.analytics.internal.zzal;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.analytics.internal.zzd;
import com.google.android.gms.analytics.internal.zze;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.analytics.internal.zzh;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzpq;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class Tracker extends zzd {
    private boolean zzPs;
    private final Map<String, String> zzPt;
    private final zzad zzPu;
    private final zza zzPv;
    private ExceptionReporter zzPw;
    private zzal zzPx;
    private final Map<String, String> zzxA;

    /* renamed from: com.google.android.gms.analytics.Tracker.1 */
    class C06501 implements Runnable {
        final /* synthetic */ String zzPA;
        final /* synthetic */ long zzPB;
        final /* synthetic */ boolean zzPC;
        final /* synthetic */ boolean zzPD;
        final /* synthetic */ String zzPE;
        final /* synthetic */ Tracker zzPF;
        final /* synthetic */ Map zzPy;
        final /* synthetic */ boolean zzPz;

        C06501(Tracker tracker, Map map, boolean z, String str, long j, boolean z2, boolean z3, String str2) {
            this.zzPF = tracker;
            this.zzPy = map;
            this.zzPz = z;
            this.zzPA = str;
            this.zzPB = j;
            this.zzPC = z2;
            this.zzPD = z3;
            this.zzPE = str2;
        }

        public void run() {
            boolean z = true;
            if (this.zzPF.zzPv.zziM()) {
                this.zzPy.put("sc", "start");
            }
            zzam.zzd(this.zzPy, "cid", this.zzPF.zziC().getClientId());
            String str = (String) this.zzPy.get("sf");
            if (str != null) {
                double zza = zzam.zza(str, 100.0d);
                if (zzam.zza(zza, (String) this.zzPy.get("cid"))) {
                    this.zzPF.zzb("Sampling enabled. Hit sampled out. sample rate", Double.valueOf(zza));
                    return;
                }
            }
            com.google.android.gms.analytics.internal.zza zzb = this.zzPF.zzjs();
            if (this.zzPz) {
                zzam.zzb(this.zzPy, "ate", zzb.zziU());
                zzam.zzc(this.zzPy, "adid", zzb.zziY());
            } else {
                this.zzPy.remove("ate");
                this.zzPy.remove("adid");
            }
            zzpq zzjS = this.zzPF.zzjt().zzjS();
            zzam.zzc(this.zzPy, "an", zzjS.zzlg());
            zzam.zzc(this.zzPy, "av", zzjS.zzli());
            zzam.zzc(this.zzPy, "aid", zzjS.zzwK());
            zzam.zzc(this.zzPy, "aiid", zzjS.zzAJ());
            this.zzPy.put("v", "1");
            this.zzPy.put("_v", zze.zzQm);
            zzam.zzc(this.zzPy, "ul", this.zzPF.zzju().zzkZ().getLanguage());
            zzam.zzc(this.zzPy, "sr", this.zzPF.zzju().zzla());
            boolean z2 = this.zzPA.equals(Headers.TRANSACTION) || this.zzPA.equals("item");
            if (z2 || this.zzPF.zzPu.zzlw()) {
                long zzbt = zzam.zzbt((String) this.zzPy.get("ht"));
                if (zzbt == 0) {
                    zzbt = this.zzPB;
                }
                if (this.zzPC) {
                    this.zzPF.zzjm().zzc("Dry run enabled. Would have sent hit", new zzab(this.zzPF, this.zzPy, zzbt, this.zzPD));
                    return;
                }
                String str2 = (String) this.zzPy.get("cid");
                Map hashMap = new HashMap();
                zzam.zza(hashMap, "uid", this.zzPy);
                zzam.zza(hashMap, "an", this.zzPy);
                zzam.zza(hashMap, "aid", this.zzPy);
                zzam.zza(hashMap, "av", this.zzPy);
                zzam.zza(hashMap, "aiid", this.zzPy);
                String str3 = this.zzPE;
                if (TextUtils.isEmpty((CharSequence) this.zzPy.get("adid"))) {
                    z = false;
                }
                this.zzPy.put("_s", String.valueOf(this.zzPF.zziH().zza(new zzh(0, str2, str3, z, 0, hashMap))));
                this.zzPF.zziH().zza(new zzab(this.zzPF, this.zzPy, zzbt, this.zzPD));
                return;
            }
            this.zzPF.zzjm().zzh(this.zzPy, "Too many hits sent too quickly, rate limiting invoked");
        }
    }

    private class zza extends zzd implements zza {
        final /* synthetic */ Tracker zzPF;
        private boolean zzPG;
        private int zzPH;
        private long zzPI;
        private boolean zzPJ;
        private long zzPK;

        protected zza(Tracker tracker, zzf com_google_android_gms_analytics_internal_zzf) {
            this.zzPF = tracker;
            super(com_google_android_gms_analytics_internal_zzf);
            this.zzPI = -1;
        }

        private void zziN() {
            if (this.zzPI >= 0 || this.zzPG) {
                zziC().zza(this.zzPF.zzPv);
            } else {
                zziC().zzb(this.zzPF.zzPv);
            }
        }

        public void enableAutoActivityTracking(boolean enabled) {
            this.zzPG = enabled;
            zziN();
        }

        public void setSessionTimeout(long sessionTimeout) {
            this.zzPI = sessionTimeout;
            zziN();
        }

        protected void zziJ() {
        }

        public synchronized boolean zziM() {
            boolean z;
            z = this.zzPJ;
            this.zzPJ = false;
            return z;
        }

        boolean zziO() {
            return zzjl().elapsedRealtime() >= this.zzPK + Math.max(1000, this.zzPI);
        }

        public void zzl(Activity activity) {
            if (this.zzPH == 0 && zziO()) {
                this.zzPJ = true;
            }
            this.zzPH++;
            if (this.zzPG) {
                Intent intent = activity.getIntent();
                if (intent != null) {
                    this.zzPF.setCampaignParamsOnNextHit(intent.getData());
                }
                Map hashMap = new HashMap();
                hashMap.put("&t", "screenview");
                this.zzPF.set("&cd", this.zzPF.zzPx != null ? this.zzPF.zzPx.zzo(activity) : activity.getClass().getCanonicalName());
                if (TextUtils.isEmpty((CharSequence) hashMap.get("&dr"))) {
                    CharSequence zzn = Tracker.zzn(activity);
                    if (!TextUtils.isEmpty(zzn)) {
                        hashMap.put("&dr", zzn);
                    }
                }
                this.zzPF.send(hashMap);
            }
        }

        public void zzm(Activity activity) {
            this.zzPH--;
            this.zzPH = Math.max(0, this.zzPH);
            if (this.zzPH == 0) {
                this.zzPK = zzjl().elapsedRealtime();
            }
        }
    }

    Tracker(zzf analytics, String trackingId, zzad rateLimiter) {
        super(analytics);
        this.zzxA = new HashMap();
        this.zzPt = new HashMap();
        if (trackingId != null) {
            this.zzxA.put("&tid", trackingId);
        }
        this.zzxA.put("useSecure", "1");
        this.zzxA.put("&a", Integer.toString(new Random().nextInt(Integer.MAX_VALUE) + 1));
        if (rateLimiter == null) {
            this.zzPu = new zzad("tracking", zzjl());
        } else {
            this.zzPu = rateLimiter;
        }
        this.zzPv = new zza(this, analytics);
    }

    private static boolean zza(Entry<String, String> entry) {
        String str = (String) entry.getKey();
        String str2 = (String) entry.getValue();
        return str.startsWith("&") && str.length() >= 2;
    }

    private static String zzb(Entry<String, String> entry) {
        return !zza((Entry) entry) ? null : ((String) entry.getKey()).substring(1);
    }

    private static void zzb(Map<String, String> map, Map<String, String> map2) {
        zzx.zzz(map2);
        if (map != null) {
            for (Entry entry : map.entrySet()) {
                String zzb = zzb(entry);
                if (zzb != null) {
                    map2.put(zzb, entry.getValue());
                }
            }
        }
    }

    private static void zzc(Map<String, String> map, Map<String, String> map2) {
        zzx.zzz(map2);
        if (map != null) {
            for (Entry entry : map.entrySet()) {
                String zzb = zzb(entry);
                if (!(zzb == null || map2.containsKey(zzb))) {
                    map2.put(zzb, entry.getValue());
                }
            }
        }
    }

    private boolean zziK() {
        return this.zzPw != null;
    }

    static String zzn(Activity activity) {
        zzx.zzz(activity);
        Intent intent = activity.getIntent();
        if (intent == null) {
            return null;
        }
        CharSequence stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        return !TextUtils.isEmpty(stringExtra) ? stringExtra : null;
    }

    public void enableAdvertisingIdCollection(boolean enabled) {
        this.zzPs = enabled;
    }

    public void enableAutoActivityTracking(boolean enabled) {
        this.zzPv.enableAutoActivityTracking(enabled);
    }

    public void enableExceptionReporting(boolean enable) {
        synchronized (this) {
            if (zziK() == enable) {
                return;
            }
            if (enable) {
                this.zzPw = new ExceptionReporter(this, Thread.getDefaultUncaughtExceptionHandler(), getContext());
                Thread.setDefaultUncaughtExceptionHandler(this.zzPw);
                zzbd("Uncaught exceptions will be reported to Google Analytics");
            } else {
                Thread.setDefaultUncaughtExceptionHandler(this.zzPw.zziD());
                zzbd("Uncaught exceptions will not be reported to Google Analytics");
            }
        }
    }

    public String get(String key) {
        zzjv();
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        if (this.zzxA.containsKey(key)) {
            return (String) this.zzxA.get(key);
        }
        if (key.equals("&ul")) {
            return zzam.zza(Locale.getDefault());
        }
        if (key.equals("&cid")) {
            return zzjr().zzkk();
        }
        if (key.equals("&sr")) {
            return zzju().zzla();
        }
        if (key.equals("&aid")) {
            return zzjt().zzjS().zzwK();
        }
        if (key.equals("&an")) {
            return zzjt().zzjS().zzlg();
        }
        if (key.equals("&av")) {
            return zzjt().zzjS().zzli();
        }
        return key.equals("&aiid") ? zzjt().zzjS().zzAJ() : null;
    }

    public void send(Map<String, String> params) {
        long currentTimeMillis = zzjl().currentTimeMillis();
        if (zziC().getAppOptOut()) {
            zzbe("AppOptOut is set to true. Not sending Google Analytics hit");
            return;
        }
        boolean isDryRunEnabled = zziC().isDryRunEnabled();
        Map hashMap = new HashMap();
        zzb(this.zzxA, hashMap);
        zzb(params, hashMap);
        boolean zzh = zzam.zzh((String) this.zzxA.get("useSecure"), true);
        zzc(this.zzPt, hashMap);
        this.zzPt.clear();
        String str = (String) hashMap.get("t");
        if (TextUtils.isEmpty(str)) {
            zzjm().zzh(hashMap, "Missing hit type parameter");
            return;
        }
        String str2 = (String) hashMap.get("tid");
        if (TextUtils.isEmpty(str2)) {
            zzjm().zzh(hashMap, "Missing tracking id parameter");
            return;
        }
        boolean zziL = zziL();
        synchronized (this) {
            if ("screenview".equalsIgnoreCase(str) || "pageview".equalsIgnoreCase(str) || "appview".equalsIgnoreCase(str) || TextUtils.isEmpty(str)) {
                int parseInt = Integer.parseInt((String) this.zzxA.get("&a")) + 1;
                if (parseInt >= Integer.MAX_VALUE) {
                    parseInt = 1;
                }
                this.zzxA.put("&a", Integer.toString(parseInt));
            }
        }
        zzjo().zzf(new C06501(this, hashMap, zziL, str, currentTimeMillis, isDryRunEnabled, zzh, str2));
    }

    public void set(String key, String value) {
        zzx.zzb((Object) key, (Object) "Key should be non-null");
        if (!TextUtils.isEmpty(key)) {
            this.zzxA.put(key, value);
        }
    }

    public void setAnonymizeIp(boolean anonymize) {
        set("&aip", zzam.zzK(anonymize));
    }

    public void setAppId(String appId) {
        set("&aid", appId);
    }

    public void setAppInstallerId(String appInstallerId) {
        set("&aiid", appInstallerId);
    }

    public void setAppName(String appName) {
        set("&an", appName);
    }

    public void setAppVersion(String appVersion) {
        set("&av", appVersion);
    }

    public void setCampaignParamsOnNextHit(Uri uri) {
        if (uri != null && !uri.isOpaque()) {
            Object queryParameter = uri.getQueryParameter("referrer");
            if (!TextUtils.isEmpty(queryParameter)) {
                Uri parse = Uri.parse("http://hostname/?" + queryParameter);
                String queryParameter2 = parse.getQueryParameter("utm_id");
                if (queryParameter2 != null) {
                    this.zzPt.put("&ci", queryParameter2);
                }
                queryParameter2 = parse.getQueryParameter("anid");
                if (queryParameter2 != null) {
                    this.zzPt.put("&anid", queryParameter2);
                }
                queryParameter2 = parse.getQueryParameter("utm_campaign");
                if (queryParameter2 != null) {
                    this.zzPt.put("&cn", queryParameter2);
                }
                queryParameter2 = parse.getQueryParameter("utm_content");
                if (queryParameter2 != null) {
                    this.zzPt.put("&cc", queryParameter2);
                }
                queryParameter2 = parse.getQueryParameter("utm_medium");
                if (queryParameter2 != null) {
                    this.zzPt.put("&cm", queryParameter2);
                }
                queryParameter2 = parse.getQueryParameter("utm_source");
                if (queryParameter2 != null) {
                    this.zzPt.put("&cs", queryParameter2);
                }
                queryParameter2 = parse.getQueryParameter("utm_term");
                if (queryParameter2 != null) {
                    this.zzPt.put("&ck", queryParameter2);
                }
                queryParameter2 = parse.getQueryParameter("dclid");
                if (queryParameter2 != null) {
                    this.zzPt.put("&dclid", queryParameter2);
                }
                queryParameter2 = parse.getQueryParameter("gclid");
                if (queryParameter2 != null) {
                    this.zzPt.put("&gclid", queryParameter2);
                }
                String queryParameter3 = parse.getQueryParameter("aclid");
                if (queryParameter3 != null) {
                    this.zzPt.put("&aclid", queryParameter3);
                }
            }
        }
    }

    public void setClientId(String clientId) {
        set("&cid", clientId);
    }

    public void setEncoding(String encoding) {
        set("&de", encoding);
    }

    public void setHostname(String hostname) {
        set("&dh", hostname);
    }

    public void setLanguage(String language) {
        set("&ul", language);
    }

    public void setLocation(String location) {
        set("&dl", location);
    }

    public void setPage(String page) {
        set("&dp", page);
    }

    public void setReferrer(String referrer) {
        set("&dr", referrer);
    }

    public void setSampleRate(double sampleRate) {
        set("&sf", Double.toString(sampleRate));
    }

    public void setScreenColors(String screenColors) {
        set("&sd", screenColors);
    }

    public void setScreenName(String screenName) {
        set("&cd", screenName);
    }

    public void setScreenResolution(int width, int height) {
        if (width >= 0 || height >= 0) {
            set("&sr", width + "x" + height);
        } else {
            zzbg("Invalid width or height. The values should be non-negative.");
        }
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.zzPv.setSessionTimeout(1000 * sessionTimeout);
    }

    public void setTitle(String title) {
        set("&dt", title);
    }

    public void setUseSecure(boolean useSecure) {
        set("useSecure", zzam.zzK(useSecure));
    }

    public void setViewportSize(String viewportSize) {
        set("&vp", viewportSize);
    }

    void zza(zzal com_google_android_gms_analytics_internal_zzal) {
        zzbd("Loading Tracker config values");
        this.zzPx = com_google_android_gms_analytics_internal_zzal;
        if (this.zzPx.zzlT()) {
            String trackingId = this.zzPx.getTrackingId();
            set("&tid", trackingId);
            zza("trackingId loaded", trackingId);
        }
        if (this.zzPx.zzlU()) {
            trackingId = Double.toString(this.zzPx.zzlV());
            set("&sf", trackingId);
            zza("Sample frequency loaded", trackingId);
        }
        if (this.zzPx.zzlW()) {
            int sessionTimeout = this.zzPx.getSessionTimeout();
            setSessionTimeout((long) sessionTimeout);
            zza("Session timeout loaded", Integer.valueOf(sessionTimeout));
        }
        if (this.zzPx.zzlX()) {
            boolean zzlY = this.zzPx.zzlY();
            enableAutoActivityTracking(zzlY);
            zza("Auto activity tracking loaded", Boolean.valueOf(zzlY));
        }
        if (this.zzPx.zzlZ()) {
            zzlY = this.zzPx.zzma();
            if (zzlY) {
                set("&aip", "1");
            }
            zza("Anonymize ip loaded", Boolean.valueOf(zzlY));
        }
        enableExceptionReporting(this.zzPx.zzmb());
    }

    protected void zziJ() {
        this.zzPv.zza();
        String zzlg = zziI().zzlg();
        if (zzlg != null) {
            set("&an", zzlg);
        }
        zzlg = zziI().zzli();
        if (zzlg != null) {
            set("&av", zzlg);
        }
    }

    boolean zziL() {
        return this.zzPs;
    }
}
