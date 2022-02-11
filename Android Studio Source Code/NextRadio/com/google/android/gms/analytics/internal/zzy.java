package com.google.android.gms.analytics.internal;

import com.facebook.ads.AdError;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzlz;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.ThreadPoolUtils;
import org.xbill.DNS.KEYRecord.Flags;

public final class zzy {
    public static zza<Boolean> zzRJ;
    public static zza<Boolean> zzRK;
    public static zza<String> zzRL;
    public static zza<Long> zzRM;
    public static zza<Float> zzRN;
    public static zza<Integer> zzRO;
    public static zza<Integer> zzRP;
    public static zza<Integer> zzRQ;
    public static zza<Long> zzRR;
    public static zza<Long> zzRS;
    public static zza<Long> zzRT;
    public static zza<Long> zzRU;
    public static zza<Long> zzRV;
    public static zza<Long> zzRW;
    public static zza<Integer> zzRX;
    public static zza<Integer> zzRY;
    public static zza<String> zzRZ;
    public static zza<String> zzSa;
    public static zza<String> zzSb;
    public static zza<String> zzSc;
    public static zza<Integer> zzSd;
    public static zza<String> zzSe;
    public static zza<String> zzSf;
    public static zza<Integer> zzSg;
    public static zza<Integer> zzSh;
    public static zza<Integer> zzSi;
    public static zza<Integer> zzSj;
    public static zza<String> zzSk;
    public static zza<Integer> zzSl;
    public static zza<Long> zzSm;
    public static zza<Integer> zzSn;
    public static zza<Integer> zzSo;
    public static zza<Long> zzSp;
    public static zza<String> zzSq;
    public static zza<Integer> zzSr;
    public static zza<Boolean> zzSs;
    public static zza<Long> zzSt;
    public static zza<Long> zzSu;
    public static zza<Long> zzSv;
    public static zza<Long> zzSw;
    public static zza<Long> zzSx;
    public static zza<Long> zzSy;
    public static zza<Long> zzSz;

    public static final class zza<V> {
        private final V zzSA;
        private final zzlz<V> zzSB;
        private V zzSC;

        private zza(zzlz<V> com_google_android_gms_internal_zzlz_V, V v) {
            zzx.zzz(com_google_android_gms_internal_zzlz_V);
            this.zzSB = com_google_android_gms_internal_zzlz_V;
            this.zzSA = v;
        }

        static zza<Float> zza(String str, float f) {
            return zza(str, f, f);
        }

        static zza<Float> zza(String str, float f, float f2) {
            return new zza(zzlz.zza(str, Float.valueOf(f2)), Float.valueOf(f));
        }

        static zza<Integer> zza(String str, int i, int i2) {
            return new zza(zzlz.zza(str, Integer.valueOf(i2)), Integer.valueOf(i));
        }

        static zza<Long> zza(String str, long j, long j2) {
            return new zza(zzlz.zza(str, Long.valueOf(j2)), Long.valueOf(j));
        }

        static zza<Boolean> zza(String str, boolean z, boolean z2) {
            return new zza(zzlz.zzk(str, z2), Boolean.valueOf(z));
        }

        static zza<Long> zzb(String str, long j) {
            return zza(str, j, j);
        }

        static zza<Integer> zzd(String str, int i) {
            return zza(str, i, i);
        }

        static zza<String> zze(String str, String str2, String str3) {
            return new zza(zzlz.zzv(str, str3), str2);
        }

        static zza<Boolean> zzg(String str, boolean z) {
            return zza(str, z, z);
        }

        static zza<String> zzl(String str, String str2) {
            return zze(str, str2, str2);
        }

        public V get() {
            return this.zzSC != null ? this.zzSC : (zzd.zzakE && zzlz.isInitialized()) ? this.zzSB.zzpX() : this.zzSA;
        }
    }

    static {
        zzRJ = zza.zzg("analytics.service_enabled", false);
        zzRK = zza.zzg("analytics.service_client_enabled", true);
        zzRL = zza.zze("analytics.log_tag", "GAv4", "GAv4-SVC");
        zzRM = zza.zzb("analytics.max_tokens", 60);
        zzRN = zza.zza("analytics.tokens_per_sec", 0.5f);
        zzRO = zza.zza("analytics.max_stored_hits", (int) AdError.SERVER_ERROR_CODE, (int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        zzRP = zza.zzd("analytics.max_stored_hits_per_app", AdError.SERVER_ERROR_CODE);
        zzRQ = zza.zzd("analytics.max_stored_properties_per_app", 100);
        zzRR = zza.zza("analytics.local_dispatch_millis", 1800000, 120000);
        zzRS = zza.zza("analytics.initial_local_dispatch_millis", 5000, 5000);
        zzRT = zza.zzb("analytics.min_local_dispatch_millis", 120000);
        zzRU = zza.zzb("analytics.max_local_dispatch_millis", 7200000);
        zzRV = zza.zzb("analytics.dispatch_alarm_millis", 7200000);
        zzRW = zza.zzb("analytics.max_dispatch_alarm_millis", 32400000);
        zzRX = zza.zzd("analytics.max_hits_per_dispatch", 20);
        zzRY = zza.zzd("analytics.max_hits_per_batch", 20);
        zzRZ = zza.zzl("analytics.insecure_host", "http://www.google-analytics.com");
        zzSa = zza.zzl("analytics.secure_host", "https://ssl.google-analytics.com");
        zzSb = zza.zzl("analytics.simple_endpoint", "/collect");
        zzSc = zza.zzl("analytics.batching_endpoint", "/batch");
        zzSd = zza.zzd("analytics.max_get_length", 2036);
        zzSe = zza.zze("analytics.batching_strategy.k", zzm.BATCH_BY_COUNT.name(), zzm.BATCH_BY_COUNT.name());
        zzSf = zza.zzl("analytics.compression_strategy.k", zzo.GZIP.name());
        zzSg = zza.zzd("analytics.max_hits_per_request.k", 20);
        zzSh = zza.zzd("analytics.max_hit_length.k", Flags.FLAG2);
        zzSi = zza.zzd("analytics.max_post_length.k", Flags.FLAG2);
        zzSj = zza.zzd("analytics.max_batch_post_length", Flags.FLAG2);
        zzSk = zza.zzl("analytics.fallback_responses.k", "404,502");
        zzSl = zza.zzd("analytics.batch_retry_interval.seconds.k", SettingsJsonConstants.SETTINGS_CACHE_DURATION_DEFAULT);
        zzSm = zza.zzb("analytics.service_monitor_interval", 86400000);
        zzSn = zza.zzd("analytics.http_connection.connect_timeout_millis", 60000);
        zzSo = zza.zzd("analytics.http_connection.read_timeout_millis", 61000);
        zzSp = zza.zzb("analytics.campaigns.time_limit", 86400000);
        zzSq = zza.zzl("analytics.first_party_experiment_id", Stomp.EMPTY);
        zzSr = zza.zzd("analytics.first_party_experiment_variant", 0);
        zzSs = zza.zzg("analytics.test.disable_receiver", false);
        zzSt = zza.zza("analytics.service_client.idle_disconnect_millis", (long) ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION, (long) ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION);
        zzSu = zza.zzb("analytics.service_client.connect_timeout_millis", 5000);
        zzSv = zza.zzb("analytics.service_client.second_connect_delay_millis", 5000);
        zzSw = zza.zzb("analytics.service_client.unexpected_reconnect_millis", 60000);
        zzSx = zza.zzb("analytics.service_client.reconnect_throttle_millis", 1800000);
        zzSy = zza.zzb("analytics.monitoring.sample_period_millis", 86400000);
        zzSz = zza.zzb("analytics.initialization_warning_threshold", 5000);
    }
}
