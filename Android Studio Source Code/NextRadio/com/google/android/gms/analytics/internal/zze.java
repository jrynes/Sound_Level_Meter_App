package com.google.android.gms.analytics.internal;

import com.google.android.gms.common.zzc;
import org.apache.activemq.ActiveMQPrefetchPolicy;

public class zze {
    public static final String VERSION;
    public static final String zzQm;

    static {
        VERSION = String.valueOf(zzc.GOOGLE_PLAY_SERVICES_VERSION_CODE / ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH).replaceAll("(\\d+)(\\d)(\\d\\d)", "$1.$2.$3");
        zzQm = "ma" + VERSION;
    }
}
