package com.google.android.gms.analytics.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.measurement.zzg;
import org.apache.activemq.transport.stomp.Stomp;

public class zzc {
    private final zzf zzQj;

    protected zzc(zzf com_google_android_gms_analytics_internal_zzf) {
        zzx.zzz(com_google_android_gms_analytics_internal_zzf);
        this.zzQj = com_google_android_gms_analytics_internal_zzf;
    }

    private void zza(int i, String str, Object obj, Object obj2, Object obj3) {
        zzaf com_google_android_gms_analytics_internal_zzaf = null;
        if (this.zzQj != null) {
            com_google_android_gms_analytics_internal_zzaf = this.zzQj.zzjy();
        }
        if (com_google_android_gms_analytics_internal_zzaf != null) {
            com_google_android_gms_analytics_internal_zzaf.zza(i, str, obj, obj2, obj3);
            return;
        }
        String str2 = (String) zzy.zzRL.get();
        if (Log.isLoggable(str2, i)) {
            Log.println(i, str2, zzc(str, obj, obj2, obj3));
        }
    }

    protected static String zzc(String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            Object obj4 = Stomp.EMPTY;
        }
        Object zzj = zzj(obj);
        Object zzj2 = zzj(obj2);
        Object zzj3 = zzj(obj3);
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = Stomp.EMPTY;
        if (!TextUtils.isEmpty(obj4)) {
            stringBuilder.append(obj4);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(zzj)) {
            stringBuilder.append(str2);
            stringBuilder.append(zzj);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzj2)) {
            stringBuilder.append(str2);
            stringBuilder.append(zzj2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzj3)) {
            stringBuilder.append(str2);
            stringBuilder.append(zzj3);
            str2 = ", ";
        }
        return stringBuilder.toString();
    }

    private static String zzj(Object obj) {
        if (obj == null) {
            return Stomp.EMPTY;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (!(obj instanceof Boolean)) {
            return obj instanceof Throwable ? ((Throwable) obj).toString() : obj.toString();
        } else {
            return obj == Boolean.TRUE ? Stomp.TRUE : Stomp.FALSE;
        }
    }

    protected Context getContext() {
        return this.zzQj.getContext();
    }

    public void zza(String str, Object obj) {
        zza(2, str, obj, null, null);
    }

    public void zza(String str, Object obj, Object obj2) {
        zza(2, str, obj, obj2, null);
    }

    public void zza(String str, Object obj, Object obj2, Object obj3) {
        zza(3, str, obj, obj2, obj3);
    }

    public void zzb(String str, Object obj) {
        zza(3, str, obj, null, null);
    }

    public void zzb(String str, Object obj, Object obj2) {
        zza(3, str, obj, obj2, null);
    }

    public void zzb(String str, Object obj, Object obj2, Object obj3) {
        zza(5, str, obj, obj2, obj3);
    }

    public void zzbd(String str) {
        zza(2, str, null, null, null);
    }

    public void zzbe(String str) {
        zza(3, str, null, null, null);
    }

    public void zzbf(String str) {
        zza(4, str, null, null, null);
    }

    public void zzbg(String str) {
        zza(5, str, null, null, null);
    }

    public void zzbh(String str) {
        zza(6, str, null, null, null);
    }

    public void zzc(String str, Object obj) {
        zza(4, str, obj, null, null);
    }

    public void zzc(String str, Object obj, Object obj2) {
        zza(5, str, obj, obj2, null);
    }

    public void zzd(String str, Object obj) {
        zza(5, str, obj, null, null);
    }

    public void zzd(String str, Object obj, Object obj2) {
        zza(6, str, obj, obj2, null);
    }

    public void zze(String str, Object obj) {
        zza(6, str, obj, null, null);
    }

    public boolean zzhp() {
        return Log.isLoggable((String) zzy.zzRL.get(), 2);
    }

    public GoogleAnalytics zziC() {
        return this.zzQj.zzjz();
    }

    protected zzb zziH() {
        return this.zzQj.zziH();
    }

    protected zzan zziI() {
        return this.zzQj.zziI();
    }

    public zzf zzji() {
        return this.zzQj;
    }

    protected void zzjj() {
        if (zzjn().zzkr()) {
            throw new IllegalStateException("Call only supported on the client side");
        }
    }

    protected void zzjk() {
        this.zzQj.zzjk();
    }

    protected zzmq zzjl() {
        return this.zzQj.zzjl();
    }

    protected zzaf zzjm() {
        return this.zzQj.zzjm();
    }

    protected zzr zzjn() {
        return this.zzQj.zzjn();
    }

    protected zzg zzjo() {
        return this.zzQj.zzjo();
    }

    protected zzv zzjp() {
        return this.zzQj.zzjp();
    }

    protected zzai zzjq() {
        return this.zzQj.zzjq();
    }

    protected zzn zzjr() {
        return this.zzQj.zzjC();
    }

    protected zza zzjs() {
        return this.zzQj.zzjB();
    }

    protected zzk zzjt() {
        return this.zzQj.zzjt();
    }

    protected zzu zzju() {
        return this.zzQj.zzju();
    }
}
