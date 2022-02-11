package com.google.android.gms.analytics.internal;

public class zzz extends zzq<zzaa> {

    private static class zza implements com.google.android.gms.analytics.internal.zzq.zza<zzaa> {
        private final zzf zzQj;
        private final zzaa zzSD;

        public zza(zzf com_google_android_gms_analytics_internal_zzf) {
            this.zzQj = com_google_android_gms_analytics_internal_zzf;
            this.zzSD = new zzaa();
        }

        public void zzc(String str, int i) {
            if ("ga_dispatchPeriod".equals(str)) {
                this.zzSD.zzSH = i;
            } else {
                this.zzQj.zzjm().zzd("Int xml configuration name not recognized", str);
            }
        }

        public void zzf(String str, boolean z) {
            if ("ga_dryRun".equals(str)) {
                this.zzSD.zzSI = z ? 1 : 0;
                return;
            }
            this.zzQj.zzjm().zzd("Bool xml configuration name not recognized", str);
        }

        public void zzj(String str, String str2) {
        }

        public void zzk(String str, String str2) {
            if ("ga_appName".equals(str)) {
                this.zzSD.zzSE = str2;
            } else if ("ga_appVersion".equals(str)) {
                this.zzSD.zzSF = str2;
            } else if ("ga_logLevel".equals(str)) {
                this.zzSD.zzSG = str2;
            } else {
                this.zzQj.zzjm().zzd("String xml configuration name not recognized", str);
            }
        }

        public /* synthetic */ zzp zzkq() {
            return zzle();
        }

        public zzaa zzle() {
            return this.zzSD;
        }
    }

    public zzz(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf, new zza(com_google_android_gms_analytics_internal_zzf));
    }
}
