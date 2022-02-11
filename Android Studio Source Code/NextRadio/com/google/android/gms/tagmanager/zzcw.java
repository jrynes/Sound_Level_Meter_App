package com.google.android.gms.tagmanager;

import com.google.android.gms.tagmanager.zzm.zza;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class zzcw<K, V> implements zzl<K, V> {
    private final Map<K, V> zzbld;
    private final int zzble;
    private final zza<K, V> zzblf;
    private int zzblg;

    zzcw(int i, zza<K, V> com_google_android_gms_tagmanager_zzm_zza_K__V) {
        this.zzbld = new HashMap();
        this.zzble = i;
        this.zzblf = com_google_android_gms_tagmanager_zzm_zza_K__V;
    }

    public synchronized V get(K key) {
        return this.zzbld.get(key);
    }

    public synchronized void zzh(K k, V v) {
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        this.zzblg += this.zzblf.sizeOf(k, v);
        if (this.zzblg > this.zzble) {
            Iterator it = this.zzbld.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                this.zzblg -= this.zzblf.sizeOf(entry.getKey(), entry.getValue());
                it.remove();
                if (this.zzblg <= this.zzble) {
                    break;
                }
            }
        }
        this.zzbld.put(k, v);
    }
}
