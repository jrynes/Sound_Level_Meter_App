package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;

class zzj extends zzdd {
    private static final String ID;
    private static final String URL;
    private static final String zzbhF;
    private static final String zzbhG;
    static final String zzbhH;
    private static final Set<String> zzbhI;
    private final Context mContext;
    private final zza zzbhJ;

    public interface zza {
        zzar zzFX();
    }

    /* renamed from: com.google.android.gms.tagmanager.zzj.1 */
    class C10511 implements zza {
        final /* synthetic */ Context zzxh;

        C10511(Context context) {
            this.zzxh = context;
        }

        public zzar zzFX() {
            return zzz.zzaX(this.zzxh);
        }
    }

    static {
        ID = zzad.ARBITRARY_PIXEL.toString();
        URL = zzae.URL.toString();
        zzbhF = zzae.ADDITIONAL_PARAMS.toString();
        zzbhG = zzae.UNREPEATABLE.toString();
        zzbhH = "gtm_" + ID + "_unrepeatable";
        zzbhI = new HashSet();
    }

    public zzj(Context context) {
        this(context, new C10511(context));
    }

    zzj(Context context, zza com_google_android_gms_tagmanager_zzj_zza) {
        super(ID, URL);
        this.zzbhJ = com_google_android_gms_tagmanager_zzj_zza;
        this.mContext = context;
    }

    private synchronized boolean zzfL(String str) {
        boolean z = true;
        synchronized (this) {
            if (!zzfN(str)) {
                if (zzfM(str)) {
                    zzbhI.add(str);
                } else {
                    z = false;
                }
            }
        }
        return z;
    }

    public void zzR(Map<String, com.google.android.gms.internal.zzag.zza> map) {
        String zzg = map.get(zzbhG) != null ? zzdf.zzg((com.google.android.gms.internal.zzag.zza) map.get(zzbhG)) : null;
        if (zzg == null || !zzfL(zzg)) {
            Builder buildUpon = Uri.parse(zzdf.zzg((com.google.android.gms.internal.zzag.zza) map.get(URL))).buildUpon();
            com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza = (com.google.android.gms.internal.zzag.zza) map.get(zzbhF);
            if (com_google_android_gms_internal_zzag_zza != null) {
                Object zzl = zzdf.zzl(com_google_android_gms_internal_zzag_zza);
                if (zzl instanceof List) {
                    for (Object zzl2 : (List) zzl2) {
                        if (zzl2 instanceof Map) {
                            for (Entry entry : ((Map) zzl2).entrySet()) {
                                buildUpon.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                            }
                        } else {
                            zzbg.m1675e("ArbitraryPixel: additional params contains non-map: not sending partial hit: " + buildUpon.build().toString());
                            return;
                        }
                    }
                }
                zzbg.m1675e("ArbitraryPixel: additional params not a list: not sending partial hit: " + buildUpon.build().toString());
                return;
            }
            String uri = buildUpon.build().toString();
            this.zzbhJ.zzFX().zzgc(uri);
            zzbg.m1676v("ArbitraryPixel: url = " + uri);
            if (zzg != null) {
                synchronized (zzj.class) {
                    zzbhI.add(zzg);
                    zzcv.zzb(this.mContext, zzbhH, zzg, Stomp.TRUE);
                }
            }
        }
    }

    boolean zzfM(String str) {
        return this.mContext.getSharedPreferences(zzbhH, 0).contains(str);
    }

    boolean zzfN(String str) {
        return zzbhI.contains(str);
    }
}
