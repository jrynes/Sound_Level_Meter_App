package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzaf.zzf;
import com.google.android.gms.internal.zzaf.zzi;
import com.google.android.gms.internal.zzaf.zzj;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.internal.zzrs.zzc;
import com.google.android.gms.internal.zzrs.zzg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

public class Container {
    private final Context mContext;
    private final String zzbhM;
    private final DataLayer zzbhN;
    private zzcp zzbhO;
    private Map<String, FunctionCallMacroCallback> zzbhP;
    private Map<String, FunctionCallTagCallback> zzbhQ;
    private volatile long zzbhR;
    private volatile String zzbhS;

    public interface FunctionCallMacroCallback {
        Object getValue(String str, Map<String, Object> map);
    }

    public interface FunctionCallTagCallback {
        void execute(String str, Map<String, Object> map);
    }

    private class zza implements com.google.android.gms.tagmanager.zzt.zza {
        final /* synthetic */ Container zzbhT;

        private zza(Container container) {
            this.zzbhT = container;
        }

        public Object zzc(String str, Map<String, Object> map) {
            FunctionCallMacroCallback zzfP = this.zzbhT.zzfP(str);
            return zzfP == null ? null : zzfP.getValue(str, map);
        }
    }

    private class zzb implements com.google.android.gms.tagmanager.zzt.zza {
        final /* synthetic */ Container zzbhT;

        private zzb(Container container) {
            this.zzbhT = container;
        }

        public Object zzc(String str, Map<String, Object> map) {
            FunctionCallTagCallback zzfQ = this.zzbhT.zzfQ(str);
            if (zzfQ != null) {
                zzfQ.execute(str, map);
            }
            return zzdf.zzHE();
        }
    }

    Container(Context context, DataLayer dataLayer, String containerId, long lastRefreshTime, zzj resource) {
        this.zzbhP = new HashMap();
        this.zzbhQ = new HashMap();
        this.zzbhS = Stomp.EMPTY;
        this.mContext = context;
        this.zzbhN = dataLayer;
        this.zzbhM = containerId;
        this.zzbhR = lastRefreshTime;
        zza(resource.zzju);
        if (resource.zzjt != null) {
            zza(resource.zzjt);
        }
    }

    Container(Context context, DataLayer dataLayer, String containerId, long lastRefreshTime, zzc resource) {
        this.zzbhP = new HashMap();
        this.zzbhQ = new HashMap();
        this.zzbhS = Stomp.EMPTY;
        this.mContext = context;
        this.zzbhN = dataLayer;
        this.zzbhM = containerId;
        this.zzbhR = lastRefreshTime;
        zza(resource);
    }

    private synchronized zzcp zzGc() {
        return this.zzbhO;
    }

    private void zza(zzf com_google_android_gms_internal_zzaf_zzf) {
        if (com_google_android_gms_internal_zzaf_zzf == null) {
            throw new NullPointerException();
        }
        try {
            zza(zzrs.zzb(com_google_android_gms_internal_zzaf_zzf));
        } catch (zzg e) {
            zzbg.m1675e("Not loading resource: " + com_google_android_gms_internal_zzaf_zzf + " because it is invalid: " + e.toString());
        }
    }

    private void zza(zzc com_google_android_gms_internal_zzrs_zzc) {
        this.zzbhS = com_google_android_gms_internal_zzrs_zzc.getVersion();
        zzc com_google_android_gms_internal_zzrs_zzc2 = com_google_android_gms_internal_zzrs_zzc;
        zza(new zzcp(this.mContext, com_google_android_gms_internal_zzrs_zzc2, this.zzbhN, new zza(), new zzb(), zzfS(this.zzbhS)));
        if (getBoolean("_gtm.loadEventEnabled")) {
            this.zzbhN.pushEvent("gtm.load", DataLayer.mapOf("gtm.id", this.zzbhM));
        }
    }

    private synchronized void zza(zzcp com_google_android_gms_tagmanager_zzcp) {
        this.zzbhO = com_google_android_gms_tagmanager_zzcp;
    }

    private void zza(zzi[] com_google_android_gms_internal_zzaf_zziArr) {
        List arrayList = new ArrayList();
        for (Object add : com_google_android_gms_internal_zzaf_zziArr) {
            arrayList.add(add);
        }
        zzGc().zzF(arrayList);
    }

    public boolean getBoolean(String key) {
        zzcp zzGc = zzGc();
        if (zzGc == null) {
            zzbg.m1675e("getBoolean called for closed container.");
            return zzdf.zzHC().booleanValue();
        }
        try {
            return zzdf.zzk((com.google.android.gms.internal.zzag.zza) zzGc.zzgn(key).getObject()).booleanValue();
        } catch (Exception e) {
            zzbg.m1675e("Calling getBoolean() threw an exception: " + e.getMessage() + " Returning default value.");
            return zzdf.zzHC().booleanValue();
        }
    }

    public String getContainerId() {
        return this.zzbhM;
    }

    public double getDouble(String key) {
        zzcp zzGc = zzGc();
        if (zzGc == null) {
            zzbg.m1675e("getDouble called for closed container.");
            return zzdf.zzHB().doubleValue();
        }
        try {
            return zzdf.zzj((com.google.android.gms.internal.zzag.zza) zzGc.zzgn(key).getObject()).doubleValue();
        } catch (Exception e) {
            zzbg.m1675e("Calling getDouble() threw an exception: " + e.getMessage() + " Returning default value.");
            return zzdf.zzHB().doubleValue();
        }
    }

    public long getLastRefreshTime() {
        return this.zzbhR;
    }

    public long getLong(String key) {
        zzcp zzGc = zzGc();
        if (zzGc == null) {
            zzbg.m1675e("getLong called for closed container.");
            return zzdf.zzHA().longValue();
        }
        try {
            return zzdf.zzi((com.google.android.gms.internal.zzag.zza) zzGc.zzgn(key).getObject()).longValue();
        } catch (Exception e) {
            zzbg.m1675e("Calling getLong() threw an exception: " + e.getMessage() + " Returning default value.");
            return zzdf.zzHA().longValue();
        }
    }

    public String getString(String key) {
        zzcp zzGc = zzGc();
        if (zzGc == null) {
            zzbg.m1675e("getString called for closed container.");
            return zzdf.zzHE();
        }
        try {
            return zzdf.zzg((com.google.android.gms.internal.zzag.zza) zzGc.zzgn(key).getObject());
        } catch (Exception e) {
            zzbg.m1675e("Calling getString() threw an exception: " + e.getMessage() + " Returning default value.");
            return zzdf.zzHE();
        }
    }

    public boolean isDefault() {
        return getLastRefreshTime() == 0;
    }

    public void registerFunctionCallMacroCallback(String customMacroName, FunctionCallMacroCallback customMacroCallback) {
        if (customMacroCallback == null) {
            throw new NullPointerException("Macro handler must be non-null");
        }
        synchronized (this.zzbhP) {
            this.zzbhP.put(customMacroName, customMacroCallback);
        }
    }

    public void registerFunctionCallTagCallback(String customTagName, FunctionCallTagCallback customTagCallback) {
        if (customTagCallback == null) {
            throw new NullPointerException("Tag callback must be non-null");
        }
        synchronized (this.zzbhQ) {
            this.zzbhQ.put(customTagName, customTagCallback);
        }
    }

    void release() {
        this.zzbhO = null;
    }

    public void unregisterFunctionCallMacroCallback(String customMacroName) {
        synchronized (this.zzbhP) {
            this.zzbhP.remove(customMacroName);
        }
    }

    public void unregisterFunctionCallTagCallback(String customTagName) {
        synchronized (this.zzbhQ) {
            this.zzbhQ.remove(customTagName);
        }
    }

    public String zzGb() {
        return this.zzbhS;
    }

    FunctionCallMacroCallback zzfP(String str) {
        FunctionCallMacroCallback functionCallMacroCallback;
        synchronized (this.zzbhP) {
            functionCallMacroCallback = (FunctionCallMacroCallback) this.zzbhP.get(str);
        }
        return functionCallMacroCallback;
    }

    public FunctionCallTagCallback zzfQ(String str) {
        FunctionCallTagCallback functionCallTagCallback;
        synchronized (this.zzbhQ) {
            functionCallTagCallback = (FunctionCallTagCallback) this.zzbhQ.get(str);
        }
        return functionCallTagCallback;
    }

    public void zzfR(String str) {
        zzGc().zzfR(str);
    }

    zzah zzfS(String str) {
        if (zzcb.zzGU().zzGV().equals(zza.CONTAINER_DEBUG)) {
        }
        return new zzbo();
    }
}
