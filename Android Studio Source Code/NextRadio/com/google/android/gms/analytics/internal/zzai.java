package com.google.android.gms.analytics.internal;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.zzx;
import java.util.UUID;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.stomp.Stomp;

public class zzai extends zzd {
    private SharedPreferences zzTh;
    private long zzTi;
    private long zzTj;
    private final zza zzTk;

    public final class zza {
        private final String mName;
        private final long zzTl;
        final /* synthetic */ zzai zzTm;

        private zza(zzai com_google_android_gms_analytics_internal_zzai, String str, long j) {
            this.zzTm = com_google_android_gms_analytics_internal_zzai;
            zzx.zzcM(str);
            zzx.zzac(j > 0);
            this.mName = str;
            this.zzTl = j;
        }

        private void zzlL() {
            long currentTimeMillis = this.zzTm.zzjl().currentTimeMillis();
            Editor edit = this.zzTm.zzTh.edit();
            edit.remove(zzlQ());
            edit.remove(zzlR());
            edit.putLong(zzlP(), currentTimeMillis);
            edit.commit();
        }

        private long zzlM() {
            long zzlO = zzlO();
            return zzlO == 0 ? 0 : Math.abs(zzlO - this.zzTm.zzjl().currentTimeMillis());
        }

        private long zzlO() {
            return this.zzTm.zzTh.getLong(zzlP(), 0);
        }

        private String zzlP() {
            return this.mName + ":start";
        }

        private String zzlQ() {
            return this.mName + ":count";
        }

        public void zzbq(String str) {
            if (zzlO() == 0) {
                zzlL();
            }
            if (str == null) {
                str = Stomp.EMPTY;
            }
            synchronized (this) {
                long j = this.zzTm.zzTh.getLong(zzlQ(), 0);
                if (j <= 0) {
                    Editor edit = this.zzTm.zzTh.edit();
                    edit.putString(zzlR(), str);
                    edit.putLong(zzlQ(), 1);
                    edit.apply();
                    return;
                }
                Object obj = (UUID.randomUUID().getLeastSignificantBits() & OpenWireFormat.DEFAULT_MAX_FRAME_SIZE) < OpenWireFormat.DEFAULT_MAX_FRAME_SIZE / (j + 1) ? 1 : null;
                Editor edit2 = this.zzTm.zzTh.edit();
                if (obj != null) {
                    edit2.putString(zzlR(), str);
                }
                edit2.putLong(zzlQ(), j + 1);
                edit2.apply();
            }
        }

        public Pair<String, Long> zzlN() {
            long zzlM = zzlM();
            if (zzlM < this.zzTl) {
                return null;
            }
            if (zzlM > this.zzTl * 2) {
                zzlL();
                return null;
            }
            String string = this.zzTm.zzTh.getString(zzlR(), null);
            zzlM = this.zzTm.zzTh.getLong(zzlQ(), 0);
            zzlL();
            return (string == null || zzlM <= 0) ? null : new Pair(string, Long.valueOf(zzlM));
        }

        protected String zzlR() {
            return this.mName + ":value";
        }
    }

    protected zzai(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
        this.zzTj = -1;
        this.zzTk = new zza("monitoring", zzjn().zzkX(), null);
    }

    public void zzbp(String str) {
        zzjk();
        zzjv();
        Editor edit = this.zzTh.edit();
        if (TextUtils.isEmpty(str)) {
            edit.remove("installation_campaign");
        } else {
            edit.putString("installation_campaign", str);
        }
        if (!edit.commit()) {
            zzbg("Failed to commit campaign data");
        }
    }

    protected void zziJ() {
        this.zzTh = getContext().getSharedPreferences("com.google.android.gms.analytics.prefs", 0);
    }

    public long zzlF() {
        zzjk();
        zzjv();
        if (this.zzTi == 0) {
            long j = this.zzTh.getLong("first_run", 0);
            if (j != 0) {
                this.zzTi = j;
            } else {
                j = zzjl().currentTimeMillis();
                Editor edit = this.zzTh.edit();
                edit.putLong("first_run", j);
                if (!edit.commit()) {
                    zzbg("Failed to commit first run time");
                }
                this.zzTi = j;
            }
        }
        return this.zzTi;
    }

    public zzaj zzlG() {
        return new zzaj(zzjl(), zzlF());
    }

    public long zzlH() {
        zzjk();
        zzjv();
        if (this.zzTj == -1) {
            this.zzTj = this.zzTh.getLong("last_dispatch", 0);
        }
        return this.zzTj;
    }

    public void zzlI() {
        zzjk();
        zzjv();
        long currentTimeMillis = zzjl().currentTimeMillis();
        Editor edit = this.zzTh.edit();
        edit.putLong("last_dispatch", currentTimeMillis);
        edit.apply();
        this.zzTj = currentTimeMillis;
    }

    public String zzlJ() {
        zzjk();
        zzjv();
        CharSequence string = this.zzTh.getString("installation_campaign", null);
        return TextUtils.isEmpty(string) ? null : string;
    }

    public zza zzlK() {
        return this.zzTk;
    }
}
