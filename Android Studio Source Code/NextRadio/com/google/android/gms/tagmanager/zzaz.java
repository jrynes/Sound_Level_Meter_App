package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag.zza;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.internal.zzrs.zzc;
import com.google.android.gms.internal.zzrs.zzd;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class zzaz {
    private static zza zzK(Object obj) throws JSONException {
        return zzdf.zzR(zzL(obj));
    }

    static Object zzL(Object obj) throws JSONException {
        if (obj instanceof JSONArray) {
            throw new RuntimeException("JSONArrays are not supported");
        } else if (JSONObject.NULL.equals(obj)) {
            throw new RuntimeException("JSON nulls are not supported");
        } else if (!(obj instanceof JSONObject)) {
            return obj;
        } else {
            JSONObject jSONObject = (JSONObject) obj;
            Map hashMap = new HashMap();
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                hashMap.put(str, zzL(jSONObject.get(str)));
            }
            return hashMap;
        }
    }

    public static zzc zzgi(String str) throws JSONException {
        zza zzK = zzK(new JSONObject(str));
        zzd zzHK = zzc.zzHK();
        for (int i = 0; i < zzK.zzjz.length; i++) {
            zzHK.zzc(zzrs.zza.zzHH().zzb(zzae.INSTANCE_NAME.toString(), zzK.zzjz[i]).zzb(zzae.FUNCTION.toString(), zzdf.zzgt(zzn.zzFZ())).zzb(zzn.zzGa(), zzK.zzjA[i]).zzHJ());
        }
        return zzHK.zzHN();
    }
}
