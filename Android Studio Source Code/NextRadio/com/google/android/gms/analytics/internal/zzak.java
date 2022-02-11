package com.google.android.gms.analytics.internal;

public class zzak extends zzq<zzal> {

    private static class zza extends zzc implements com.google.android.gms.analytics.internal.zzq.zza<zzal> {
        private final zzal zzTn;

        public zza(zzf com_google_android_gms_analytics_internal_zzf) {
            super(com_google_android_gms_analytics_internal_zzf);
            this.zzTn = new zzal();
        }

        public void zzc(String str, int i) {
            if ("ga_sessionTimeout".equals(str)) {
                this.zzTn.zzTp = i;
            } else {
                zzd("int configuration name not recognized", str);
            }
        }

        public void zzf(String str, boolean z) {
            int i = 1;
            zzal com_google_android_gms_analytics_internal_zzal;
            if ("ga_autoActivityTracking".equals(str)) {
                com_google_android_gms_analytics_internal_zzal = this.zzTn;
                if (!z) {
                    i = 0;
                }
                com_google_android_gms_analytics_internal_zzal.zzTq = i;
            } else if ("ga_anonymizeIp".equals(str)) {
                com_google_android_gms_analytics_internal_zzal = this.zzTn;
                if (!z) {
                    i = 0;
                }
                com_google_android_gms_analytics_internal_zzal.zzTr = i;
            } else if ("ga_reportUncaughtExceptions".equals(str)) {
                com_google_android_gms_analytics_internal_zzal = this.zzTn;
                if (!z) {
                    i = 0;
                }
                com_google_android_gms_analytics_internal_zzal.zzTs = i;
            } else {
                zzd("bool configuration name not recognized", str);
            }
        }

        public void zzj(String str, String str2) {
            this.zzTn.zzTt.put(str, str2);
        }

        public void zzk(String str, String str2) {
            if ("ga_trackingId".equals(str)) {
                this.zzTn.zzOV = str2;
            } else if ("ga_sampleFrequency".equals(str)) {
                try {
                    this.zzTn.zzTo = Double.parseDouble(str2);
                } catch (NumberFormatException e) {
                    zzc("Error parsing ga_sampleFrequency value", str2, e);
                }
            } else {
                zzd("string configuration name not recognized", str);
            }
        }

        public /* synthetic */ zzp zzkq() {
            return zzlS();
        }

        public zzal zzlS() {
            return this.zzTn;
        }
    }

    public zzak(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf, new zza(com_google_android_gms_analytics_internal_zzf));
    }
}
