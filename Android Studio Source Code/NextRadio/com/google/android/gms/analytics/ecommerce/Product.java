package com.google.android.gms.analytics.ecommerce;

import com.google.android.gms.analytics.zzc;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Product {
    Map<String, String> zzPU;

    public Product() {
        this.zzPU = new HashMap();
    }

    void put(String name, String value) {
        zzx.zzb((Object) name, (Object) "Name should be non-null");
        this.zzPU.put(name, value);
    }

    public Product setBrand(String value) {
        put("br", value);
        return this;
    }

    public Product setCategory(String value) {
        put("ca", value);
        return this;
    }

    public Product setCouponCode(String value) {
        put("cc", value);
        return this;
    }

    public Product setCustomDimension(int index, String value) {
        put(zzc.zzae(index), value);
        return this;
    }

    public Product setCustomMetric(int index, int value) {
        put(zzc.zzaf(index), Integer.toString(value));
        return this;
    }

    public Product setId(String value) {
        put(Name.MARK, value);
        return this;
    }

    public Product setName(String value) {
        put("nm", value);
        return this;
    }

    public Product setPosition(int value) {
        put("ps", Integer.toString(value));
        return this;
    }

    public Product setPrice(double value) {
        put("pr", Double.toString(value));
        return this;
    }

    public Product setQuantity(int value) {
        put("qt", Integer.toString(value));
        return this;
    }

    public Product setVariant(String value) {
        put("va", value);
        return this;
    }

    public String toString() {
        return zze.zzO(this.zzPU);
    }

    public Map<String, String> zzba(String str) {
        Map<String, String> hashMap = new HashMap();
        for (Entry entry : this.zzPU.entrySet()) {
            hashMap.put(str + ((String) entry.getKey()), entry.getValue());
        }
        return hashMap;
    }
}
