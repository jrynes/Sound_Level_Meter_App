package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.zza;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.tagmanager.DataLayer;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp.Headers.Error;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

@zzhb
public abstract class zzdr implements Releasable {
    protected Context mContext;
    protected String zzzN;
    protected WeakReference<zzjp> zzzO;

    /* renamed from: com.google.android.gms.internal.zzdr.1 */
    class C07711 implements Runnable {
        final /* synthetic */ String zzzP;
        final /* synthetic */ String zzzQ;
        final /* synthetic */ int zzzR;
        final /* synthetic */ int zzzS;
        final /* synthetic */ boolean zzzT;
        final /* synthetic */ zzdr zzzU;

        C07711(zzdr com_google_android_gms_internal_zzdr, String str, String str2, int i, int i2, boolean z) {
            this.zzzU = com_google_android_gms_internal_zzdr;
            this.zzzP = str;
            this.zzzQ = str2;
            this.zzzR = i;
            this.zzzS = i2;
            this.zzzT = z;
        }

        public void run() {
            Map hashMap = new HashMap();
            hashMap.put(DataLayer.EVENT_KEY, "precacheProgress");
            hashMap.put("src", this.zzzP);
            hashMap.put("cachedSrc", this.zzzQ);
            hashMap.put("bytesLoaded", Integer.toString(this.zzzR));
            hashMap.put("totalBytes", Integer.toString(this.zzzS));
            hashMap.put("cacheReady", this.zzzT ? "1" : "0");
            this.zzzU.zza("onPrecacheEvent", hashMap);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdr.2 */
    class C07722 implements Runnable {
        final /* synthetic */ String zzzP;
        final /* synthetic */ String zzzQ;
        final /* synthetic */ int zzzS;
        final /* synthetic */ zzdr zzzU;

        C07722(zzdr com_google_android_gms_internal_zzdr, String str, String str2, int i) {
            this.zzzU = com_google_android_gms_internal_zzdr;
            this.zzzP = str;
            this.zzzQ = str2;
            this.zzzS = i;
        }

        public void run() {
            Map hashMap = new HashMap();
            hashMap.put(DataLayer.EVENT_KEY, "precacheComplete");
            hashMap.put("src", this.zzzP);
            hashMap.put("cachedSrc", this.zzzQ);
            hashMap.put("totalBytes", Integer.toString(this.zzzS));
            this.zzzU.zza("onPrecacheEvent", hashMap);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdr.3 */
    class C07733 implements Runnable {
        final /* synthetic */ String zzzP;
        final /* synthetic */ String zzzQ;
        final /* synthetic */ zzdr zzzU;
        final /* synthetic */ String zzzV;
        final /* synthetic */ String zzzW;

        C07733(zzdr com_google_android_gms_internal_zzdr, String str, String str2, String str3, String str4) {
            this.zzzU = com_google_android_gms_internal_zzdr;
            this.zzzP = str;
            this.zzzQ = str2;
            this.zzzV = str3;
            this.zzzW = str4;
        }

        public void run() {
            Map hashMap = new HashMap();
            hashMap.put(DataLayer.EVENT_KEY, "precacheCanceled");
            hashMap.put("src", this.zzzP);
            if (!TextUtils.isEmpty(this.zzzQ)) {
                hashMap.put("cachedSrc", this.zzzQ);
            }
            hashMap.put(Send.TYPE, this.zzzU.zzW(this.zzzV));
            hashMap.put("reason", this.zzzV);
            if (!TextUtils.isEmpty(this.zzzW)) {
                hashMap.put(Error.MESSAGE, this.zzzW);
            }
            this.zzzU.zza("onPrecacheEvent", hashMap);
        }
    }

    public zzdr(zzjp com_google_android_gms_internal_zzjp) {
        this.mContext = com_google_android_gms_internal_zzjp.getContext();
        this.zzzN = zzr.zzbC().zze(this.mContext, com_google_android_gms_internal_zzjp.zzhX().afmaVersion);
        this.zzzO = new WeakReference(com_google_android_gms_internal_zzjp);
    }

    private String zzW(String str) {
        String str2 = "internal";
        Object obj = -1;
        switch (str.hashCode()) {
            case -1396664534:
                if (str.equals("badUrl")) {
                    obj = 6;
                    break;
                }
                break;
            case -1347010958:
                if (str.equals("inProgress")) {
                    obj = 2;
                    break;
                }
                break;
            case -918817863:
                if (str.equals("downloadTimeout")) {
                    obj = 7;
                    break;
                }
                break;
            case -659376217:
                if (str.equals("contentLengthMissing")) {
                    obj = 3;
                    break;
                }
                break;
            case -642208130:
                if (str.equals("playerFailed")) {
                    obj = 1;
                    break;
                }
                break;
            case -354048396:
                if (str.equals("sizeExceeded")) {
                    obj = 8;
                    break;
                }
                break;
            case -32082395:
                if (str.equals("externalAbort")) {
                    obj = 9;
                    break;
                }
                break;
            case 96784904:
                if (str.equals(Diagnostics.error)) {
                    obj = null;
                    break;
                }
                break;
            case 580119100:
                if (str.equals("expireFailed")) {
                    obj = 5;
                    break;
                }
                break;
            case 725497484:
                if (str.equals("noCacheDir")) {
                    obj = 4;
                    break;
                }
                break;
        }
        switch (obj) {
            case Tokenizer.EOF /*0*/:
            case Zone.PRIMARY /*1*/:
            case Zone.SECONDARY /*2*/:
            case Protocol.GGP /*3*/:
                return "internal";
            case Type.MF /*4*/:
            case Service.RJE /*5*/:
                return "io";
            case Protocol.TCP /*6*/:
            case Service.ECHO /*7*/:
                return "network";
            case Protocol.EGP /*8*/:
            case Service.DISCARD /*9*/:
                return "policy";
            default:
                return str2;
        }
    }

    private void zza(String str, Map<String, String> map) {
        zzjp com_google_android_gms_internal_zzjp = (zzjp) this.zzzO.get();
        if (com_google_android_gms_internal_zzjp != null) {
            com_google_android_gms_internal_zzjp.zza(str, (Map) map);
        }
    }

    public abstract void abort();

    public void release() {
    }

    public abstract boolean zzU(String str);

    protected String zzV(String str) {
        return zzn.zzcS().zzaH(str);
    }

    protected void zza(String str, String str2, int i) {
        zza.zzMS.post(new C07722(this, str, str2, i));
    }

    protected void zza(String str, String str2, int i, int i2, boolean z) {
        zza.zzMS.post(new C07711(this, str, str2, i, i2, z));
    }

    protected void zza(String str, String str2, String str3, String str4) {
        zza.zzMS.post(new C07733(this, str, str2, str3, str4));
    }
}
