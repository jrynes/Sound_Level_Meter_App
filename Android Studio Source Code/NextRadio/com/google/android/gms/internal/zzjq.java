package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.TransportMediator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.overlay.AdLauncherIntentInfoParcel;
import com.google.android.gms.ads.internal.overlay.AdOverlayInfoParcel;
import com.google.android.gms.ads.internal.overlay.zzg;
import com.google.android.gms.ads.internal.overlay.zzp;
import com.google.android.gms.ads.internal.zze;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.location.places.Place;
import com.rabbitmq.client.impl.AMQImpl.Confirm;
import com.rabbitmq.client.impl.AMQImpl.Tx;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Commands;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connect;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

@zzhb
public class zzjq extends WebViewClient {
    private static final String[] zzNy;
    private static final String[] zzNz;
    private zzft zzDk;
    private zza zzGm;
    private final HashMap<String, List<zzdf>> zzNA;
    private zzg zzNB;
    private zzb zzNC;
    private boolean zzND;
    private boolean zzNE;
    private zzp zzNF;
    private final zzfr zzNG;
    private boolean zzNH;
    private boolean zzNI;
    private boolean zzNJ;
    private int zzNK;
    protected zzjp zzpD;
    private final Object zzpV;
    private boolean zzsz;
    private com.google.android.gms.ads.internal.client.zza zztz;
    private zzdb zzyW;
    private zze zzzA;
    private zzfn zzzB;
    private zzdh zzzD;
    private zzdj zzzy;

    public interface zza {
        void zza(zzjp com_google_android_gms_internal_zzjp, boolean z);
    }

    public interface zzb {
        void zzbi();
    }

    /* renamed from: com.google.android.gms.internal.zzjq.1 */
    class C08991 implements Runnable {
        final /* synthetic */ zzjq zzNL;

        C08991(zzjq com_google_android_gms_internal_zzjq) {
            this.zzNL = com_google_android_gms_internal_zzjq;
        }

        public void run() {
            this.zzNL.zzpD.zzid();
            com.google.android.gms.ads.internal.overlay.zzd zzhS = this.zzNL.zzpD.zzhS();
            if (zzhS != null) {
                zzhS.zzfo();
            }
            if (this.zzNL.zzNC != null) {
                this.zzNL.zzNC.zzbi();
                this.zzNL.zzNC = null;
            }
        }
    }

    private static class zzc implements zzg {
        private zzg zzNB;
        private zzjp zzNM;

        public zzc(zzjp com_google_android_gms_internal_zzjp, zzg com_google_android_gms_ads_internal_overlay_zzg) {
            this.zzNM = com_google_android_gms_internal_zzjp;
            this.zzNB = com_google_android_gms_ads_internal_overlay_zzg;
        }

        public void onPause() {
        }

        public void onResume() {
        }

        public void zzaW() {
            this.zzNB.zzaW();
            this.zzNM.zzhN();
        }

        public void zzaX() {
            this.zzNB.zzaX();
            this.zzNM.zzfr();
        }
    }

    private class zzd implements zzdf {
        final /* synthetic */ zzjq zzNL;

        private zzd(zzjq com_google_android_gms_internal_zzjq) {
            this.zzNL = com_google_android_gms_internal_zzjq;
        }

        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            if (map.keySet().contains("start")) {
                this.zzNL.zzij();
            } else if (map.keySet().contains("stop")) {
                this.zzNL.zzik();
            } else if (map.keySet().contains("cancel")) {
                this.zzNL.zzil();
            }
        }
    }

    static {
        zzNy = new String[]{"UNKNOWN", "HOST_LOOKUP", "UNSUPPORTED_AUTH_SCHEME", "AUTHENTICATION", "PROXY_AUTHENTICATION", Commands.CONNECT, "IO", InstanceID.ERROR_TIMEOUT, "REDIRECT_LOOP", "UNSUPPORTED_SCHEME", "FAILED_SSL_HANDSHAKE", "BAD_URL", "FILE", "FILE_NOT_FOUND", "TOO_MANY_REQUESTS"};
        zzNz = new String[]{"NOT_YET_VALID", "EXPIRED", "ID_MISMATCH", "UNTRUSTED", "DATE_INVALID", "INVALID"};
    }

    public zzjq(zzjp com_google_android_gms_internal_zzjp, boolean z) {
        this(com_google_android_gms_internal_zzjp, z, new zzfr(com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzjp.zzhQ(), new zzbl(com_google_android_gms_internal_zzjp.getContext())), null);
    }

    zzjq(zzjp com_google_android_gms_internal_zzjp, boolean z, zzfr com_google_android_gms_internal_zzfr, zzfn com_google_android_gms_internal_zzfn) {
        this.zzNA = new HashMap();
        this.zzpV = new Object();
        this.zzND = false;
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzsz = z;
        this.zzNG = com_google_android_gms_internal_zzfr;
        this.zzzB = com_google_android_gms_internal_zzfn;
    }

    private void zza(Context context, String str, String str2, String str3) {
        if (((Boolean) zzbt.zzwO.get()).booleanValue()) {
            Bundle bundle = new Bundle();
            bundle.putString(NotificationCompat.CATEGORY_ERROR, str);
            bundle.putString("code", str2);
            bundle.putString(Connect.HOST, zzaN(str3));
            zzr.zzbC().zza(context, this.zzpD.zzhX().afmaVersion, "gmob-apps", bundle, true);
        }
    }

    private String zzaN(String str) {
        if (TextUtils.isEmpty(str)) {
            return Stomp.EMPTY;
        }
        Uri parse = Uri.parse(str);
        return parse.getHost() != null ? parse.getHost() : Stomp.EMPTY;
    }

    private static boolean zzg(Uri uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    private void zzij() {
        synchronized (this.zzpV) {
            this.zzNE = true;
        }
        this.zzNK++;
        zzim();
    }

    private void zzik() {
        this.zzNK--;
        zzim();
    }

    private void zzil() {
        this.zzNJ = true;
        zzim();
    }

    public final void onLoadResource(WebView webView, String url) {
        zzin.m1674v("Loading resource: " + url);
        Uri parse = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(parse.getScheme()) && "mobileads.google.com".equalsIgnoreCase(parse.getHost())) {
            zzh(parse);
        }
    }

    public final void onPageFinished(WebView webView, String url) {
        synchronized (this.zzpV) {
            if (this.zzNH) {
                zzin.m1674v("Blank page loaded, 1...");
                this.zzpD.zzhZ();
                return;
            }
            this.zzNI = true;
            zzim();
        }
    }

    public final void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        String valueOf = (errorCode >= 0 || (-errorCode) - 1 >= zzNy.length) ? String.valueOf(errorCode) : zzNy[(-errorCode) - 1];
        zza(this.zzpD.getContext(), "http_err", valueOf, failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    public final void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (error != null) {
            int primaryError = error.getPrimaryError();
            String valueOf = (primaryError < 0 || primaryError >= zzNz.length) ? String.valueOf(primaryError) : zzNz[primaryError];
            zza(this.zzpD.getContext(), "ssl_err", valueOf, zzr.zzbE().zza(error));
        }
        super.onReceivedSslError(view, handler, error);
    }

    public final void reset() {
        synchronized (this.zzpV) {
            this.zzNA.clear();
            this.zztz = null;
            this.zzNB = null;
            this.zzGm = null;
            this.zzyW = null;
            this.zzND = false;
            this.zzsz = false;
            this.zzNE = false;
            this.zzzD = null;
            this.zzNF = null;
            this.zzNC = null;
            if (this.zzzB != null) {
                this.zzzB.zzp(true);
                this.zzzB = null;
            }
        }
    }

    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        switch (event.getKeyCode()) {
            case Service.FINGER /*79*/:
            case Confirm.INDEX /*85*/:
            case Place.TYPE_STADIUM /*86*/:
            case Place.TYPE_STORAGE /*87*/:
            case Place.TYPE_STORE /*88*/:
            case Service.SU_MIT_TG /*89*/:
            case Tx.INDEX /*90*/:
            case Service.MIT_DOV /*91*/:
            case TransportMediator.KEYCODE_MEDIA_PLAY /*126*/:
            case Service.LOCUS_CON /*127*/:
            case Flags.FLAG8 /*128*/:
            case Service.PWDGEN /*129*/:
            case Service.CISCO_FNA /*130*/:
            case 222:
                return true;
            default:
                return false;
        }
    }

    public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
        zzin.m1674v("AdWebView shouldOverrideUrlLoading: " + url);
        Uri parse = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(parse.getScheme()) && "mobileads.google.com".equalsIgnoreCase(parse.getHost())) {
            zzh(parse);
        } else if (this.zzND && webView == this.zzpD.getWebView() && zzg(parse)) {
            if (this.zztz != null && ((Boolean) zzbt.zzww.get()).booleanValue()) {
                this.zztz.onAdClicked();
                this.zztz = null;
            }
            return super.shouldOverrideUrlLoading(webView, url);
        } else if (this.zzpD.getWebView().willNotDraw()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("AdWebView unable to handle URL: " + url);
        } else {
            Uri uri;
            try {
                zzan zzhW = this.zzpD.zzhW();
                if (zzhW != null && zzhW.zzb(parse)) {
                    parse = zzhW.zza(parse, this.zzpD.getContext());
                }
                uri = parse;
            } catch (zzao e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Unable to append parameter to URL: " + url);
                uri = parse;
            }
            if (this.zzzA == null || this.zzzA.zzbh()) {
                zza(new AdLauncherIntentInfoParcel("android.intent.action.VIEW", uri.toString(), null, null, null, null, null));
            } else {
                this.zzzA.zzq(url);
            }
        }
        return true;
    }

    public void zzG(boolean z) {
        this.zzND = z;
    }

    public void zza(int i, int i2, boolean z) {
        this.zzNG.zzf(i, i2);
        if (this.zzzB != null) {
            this.zzzB.zza(i, i2, z);
        }
    }

    public final void zza(AdLauncherIntentInfoParcel adLauncherIntentInfoParcel) {
        zzg com_google_android_gms_ads_internal_overlay_zzg = null;
        boolean zzhY = this.zzpD.zzhY();
        com.google.android.gms.ads.internal.client.zza com_google_android_gms_ads_internal_client_zza = (!zzhY || this.zzpD.zzaN().zzui) ? this.zztz : null;
        if (!zzhY) {
            com_google_android_gms_ads_internal_overlay_zzg = this.zzNB;
        }
        zza(new AdOverlayInfoParcel(adLauncherIntentInfoParcel, com_google_android_gms_ads_internal_client_zza, com_google_android_gms_ads_internal_overlay_zzg, this.zzNF, this.zzpD.zzhX()));
    }

    public void zza(AdOverlayInfoParcel adOverlayInfoParcel) {
        boolean z = false;
        boolean zzeN = this.zzzB != null ? this.zzzB.zzeN() : false;
        com.google.android.gms.ads.internal.overlay.zze zzbA = zzr.zzbA();
        Context context = this.zzpD.getContext();
        if (!zzeN) {
            z = true;
        }
        zzbA.zza(context, adOverlayInfoParcel, z);
    }

    public void zza(zza com_google_android_gms_internal_zzjq_zza) {
        this.zzGm = com_google_android_gms_internal_zzjq_zza;
    }

    public void zza(zzb com_google_android_gms_internal_zzjq_zzb) {
        this.zzNC = com_google_android_gms_internal_zzjq_zzb;
    }

    public void zza(String str, zzdf com_google_android_gms_internal_zzdf) {
        synchronized (this.zzpV) {
            List list = (List) this.zzNA.get(str);
            if (list == null) {
                list = new CopyOnWriteArrayList();
                this.zzNA.put(str, list);
            }
            list.add(com_google_android_gms_internal_zzdf);
        }
    }

    public final void zza(boolean z, int i) {
        com.google.android.gms.ads.internal.client.zza com_google_android_gms_ads_internal_client_zza = (!this.zzpD.zzhY() || this.zzpD.zzaN().zzui) ? this.zztz : null;
        zza(new AdOverlayInfoParcel(com_google_android_gms_ads_internal_client_zza, this.zzNB, this.zzNF, this.zzpD, z, i, this.zzpD.zzhX()));
    }

    public final void zza(boolean z, int i, String str) {
        zzg com_google_android_gms_ads_internal_overlay_zzg = null;
        boolean zzhY = this.zzpD.zzhY();
        com.google.android.gms.ads.internal.client.zza com_google_android_gms_ads_internal_client_zza = (!zzhY || this.zzpD.zzaN().zzui) ? this.zztz : null;
        if (!zzhY) {
            com_google_android_gms_ads_internal_overlay_zzg = new zzc(this.zzpD, this.zzNB);
        }
        zza(new AdOverlayInfoParcel(com_google_android_gms_ads_internal_client_zza, com_google_android_gms_ads_internal_overlay_zzg, this.zzyW, this.zzNF, this.zzpD, z, i, str, this.zzpD.zzhX(), this.zzzD));
    }

    public final void zza(boolean z, int i, String str, String str2) {
        boolean zzhY = this.zzpD.zzhY();
        com.google.android.gms.ads.internal.client.zza com_google_android_gms_ads_internal_client_zza = (!zzhY || this.zzpD.zzaN().zzui) ? this.zztz : null;
        zza(new AdOverlayInfoParcel(com_google_android_gms_ads_internal_client_zza, zzhY ? null : new zzc(this.zzpD, this.zzNB), this.zzyW, this.zzNF, this.zzpD, z, i, str, str2, this.zzpD.zzhX(), this.zzzD));
    }

    public void zzb(com.google.android.gms.ads.internal.client.zza com_google_android_gms_ads_internal_client_zza, zzg com_google_android_gms_ads_internal_overlay_zzg, zzdb com_google_android_gms_internal_zzdb, zzp com_google_android_gms_ads_internal_overlay_zzp, boolean z, zzdh com_google_android_gms_internal_zzdh, zzdj com_google_android_gms_internal_zzdj, zze com_google_android_gms_ads_internal_zze, zzft com_google_android_gms_internal_zzft) {
        if (com_google_android_gms_ads_internal_zze == null) {
            com_google_android_gms_ads_internal_zze = new zze(false);
        }
        this.zzzB = new zzfn(this.zzpD, com_google_android_gms_internal_zzft);
        zza("/appEvent", new zzda(com_google_android_gms_internal_zzdb));
        zza("/backButton", zzde.zzzh);
        zza("/canOpenURLs", zzde.zzyY);
        zza("/canOpenIntents", zzde.zzyZ);
        zza("/click", zzde.zzza);
        zza("/close", zzde.zzzb);
        zza("/customClose", zzde.zzzd);
        zza("/instrument", zzde.zzzk);
        zza("/delayPageLoaded", new zzd());
        zza("/httpTrack", zzde.zzze);
        zza("/log", zzde.zzzf);
        zza("/mraid", new zzdl(com_google_android_gms_ads_internal_zze, this.zzzB));
        zza("/mraidLoaded", this.zzNG);
        zza("/open", new zzdm(com_google_android_gms_internal_zzdh, com_google_android_gms_ads_internal_zze, this.zzzB));
        zza("/precache", zzde.zzzj);
        zza("/touch", zzde.zzzg);
        zza("/video", zzde.zzzi);
        zza("/appStreaming", zzde.zzzc);
        if (com_google_android_gms_internal_zzdj != null) {
            zza("/setInterstitialProperties", new zzdi(com_google_android_gms_internal_zzdj));
        }
        this.zztz = com_google_android_gms_ads_internal_client_zza;
        this.zzNB = com_google_android_gms_ads_internal_overlay_zzg;
        this.zzyW = com_google_android_gms_internal_zzdb;
        this.zzzD = com_google_android_gms_internal_zzdh;
        this.zzNF = com_google_android_gms_ads_internal_overlay_zzp;
        this.zzzA = com_google_android_gms_ads_internal_zze;
        this.zzDk = com_google_android_gms_internal_zzft;
        this.zzzy = com_google_android_gms_internal_zzdj;
        zzG(z);
    }

    public void zzb(String str, zzdf com_google_android_gms_internal_zzdf) {
        synchronized (this.zzpV) {
            List list = (List) this.zzNA.get(str);
            if (list == null) {
                return;
            }
            list.remove(com_google_android_gms_internal_zzdf);
        }
    }

    public boolean zzcv() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzsz;
        }
        return z;
    }

    public void zze(int i, int i2) {
        if (this.zzzB != null) {
            this.zzzB.zze(i, i2);
        }
    }

    public final void zzfo() {
        synchronized (this.zzpV) {
            this.zzND = false;
            this.zzsz = true;
            zzir.runOnUiThread(new C08991(this));
        }
    }

    public void zzh(Uri uri) {
        String path = uri.getPath();
        List<zzdf> list = (List) this.zzNA.get(path);
        if (list != null) {
            Map zze = zzr.zzbC().zze(uri);
            if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
                zzin.m1674v("Received GMSG: " + path);
                for (String path2 : zze.keySet()) {
                    zzin.m1674v("  " + path2 + ": " + ((String) zze.get(path2)));
                }
            }
            for (zzdf zza : list) {
                zza.zza(this.zzpD, zze);
            }
            return;
        }
        zzin.m1674v("No GMSG handler found for GMSG: " + uri);
    }

    public void zzh(zzjp com_google_android_gms_internal_zzjp) {
        this.zzpD = com_google_android_gms_internal_zzjp;
    }

    public zze zzig() {
        return this.zzzA;
    }

    public boolean zzih() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzNE;
        }
        return z;
    }

    public void zzii() {
        synchronized (this.zzpV) {
            zzin.m1674v("Loading blank page in WebView, 2...");
            this.zzNH = true;
            this.zzpD.zzaL("about:blank");
        }
    }

    public final void zzim() {
        if (this.zzGm != null && ((this.zzNI && this.zzNK <= 0) || this.zzNJ)) {
            this.zzGm.zza(this.zzpD, !this.zzNJ);
            this.zzGm = null;
        }
        this.zzpD.zzie();
    }
}
