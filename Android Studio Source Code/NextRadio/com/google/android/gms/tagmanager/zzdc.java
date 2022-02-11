package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzdc {
    private Context mContext;
    private Tracker zzPb;
    private GoogleAnalytics zzPd;

    static class zza implements Logger {
        zza() {
        }

        private static int zzkn(int i) {
            switch (i) {
                case Zone.SECONDARY /*2*/:
                    return 0;
                case Protocol.GGP /*3*/:
                case Type.MF /*4*/:
                    return 1;
                case Service.RJE /*5*/:
                    return 2;
                default:
                    return 3;
            }
        }

        public void error(Exception exception) {
            zzbg.zzb(Stomp.EMPTY, exception);
        }

        public void error(String message) {
            zzbg.m1675e(message);
        }

        public int getLogLevel() {
            return zzkn(zzbg.getLogLevel());
        }

        public void info(String message) {
            zzbg.zzaJ(message);
        }

        public void setLogLevel(int logLevel) {
            zzbg.zzaK("GA uses GTM logger. Please use TagManager.setLogLevel(int) instead.");
        }

        public void verbose(String message) {
            zzbg.m1676v(message);
        }

        public void warn(String message) {
            zzbg.zzaK(message);
        }
    }

    public zzdc(Context context) {
        this.mContext = context;
    }

    private synchronized void zzgr(String str) {
        if (this.zzPd == null) {
            this.zzPd = GoogleAnalytics.getInstance(this.mContext);
            this.zzPd.setLogger(new zza());
            this.zzPb = this.zzPd.newTracker(str);
        }
    }

    public Tracker zzgq(String str) {
        zzgr(str);
        return this.zzPb;
    }
}
