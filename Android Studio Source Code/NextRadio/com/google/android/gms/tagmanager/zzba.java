package com.google.android.gms.tagmanager;

import android.annotation.TargetApi;
import android.util.LruCache;
import com.google.android.gms.tagmanager.zzm.zza;

@TargetApi(12)
class zzba<K, V> implements zzl<K, V> {
    private LruCache<K, V> zzbjq;

    /* renamed from: com.google.android.gms.tagmanager.zzba.1 */
    class C10371 extends LruCache<K, V> {
        final /* synthetic */ zza zzbjr;
        final /* synthetic */ zzba zzbjs;

        C10371(zzba com_google_android_gms_tagmanager_zzba, int i, zza com_google_android_gms_tagmanager_zzm_zza) {
            this.zzbjs = com_google_android_gms_tagmanager_zzba;
            this.zzbjr = com_google_android_gms_tagmanager_zzm_zza;
            super(i);
        }

        protected int sizeOf(K key, V value) {
            return this.zzbjr.sizeOf(key, value);
        }
    }

    zzba(int i, zza<K, V> com_google_android_gms_tagmanager_zzm_zza_K__V) {
        this.zzbjq = new C10371(this, i, com_google_android_gms_tagmanager_zzm_zza_K__V);
    }

    public V get(K key) {
        return this.zzbjq.get(key);
    }

    public void zzh(K k, V v) {
        this.zzbjq.put(k, v);
    }
}
