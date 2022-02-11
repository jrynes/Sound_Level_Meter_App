package com.google.android.gms.tagmanager;

import android.os.Build.VERSION;

class zzm<K, V> {
    final zza<K, V> zzbhK;

    public interface zza<K, V> {
        int sizeOf(K k, V v);
    }

    /* renamed from: com.google.android.gms.tagmanager.zzm.1 */
    class C10521 implements zza<K, V> {
        final /* synthetic */ zzm zzbhL;

        C10521(zzm com_google_android_gms_tagmanager_zzm) {
            this.zzbhL = com_google_android_gms_tagmanager_zzm;
        }

        public int sizeOf(K k, V v) {
            return 1;
        }
    }

    public zzm() {
        this.zzbhK = new C10521(this);
    }

    int zzFY() {
        return VERSION.SDK_INT;
    }

    public zzl<K, V> zza(int i, zza<K, V> com_google_android_gms_tagmanager_zzm_zza_K__V) {
        if (i > 0) {
            return zzFY() < 12 ? new zzcw(i, com_google_android_gms_tagmanager_zzm_zza_K__V) : new zzba(i, com_google_android_gms_tagmanager_zzm_zza_K__V);
        } else {
            throw new IllegalArgumentException("maxSize <= 0");
        }
    }
}
