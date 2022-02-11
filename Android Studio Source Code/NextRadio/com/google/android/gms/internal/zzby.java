package com.google.android.gms.internal;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Map;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;

@zzhb
public abstract class zzby {
    @zzhb
    public static final zzby zzxs;
    @zzhb
    public static final zzby zzxt;
    @zzhb
    public static final zzby zzxu;

    /* renamed from: com.google.android.gms.internal.zzby.1 */
    static class C07551 extends zzby {
        C07551() {
        }

        public String zzb(@Nullable String str, String str2) {
            return str2;
        }
    }

    /* renamed from: com.google.android.gms.internal.zzby.2 */
    static class C07562 extends zzby {
        C07562() {
        }

        public String zzb(@Nullable String str, String str2) {
            return str != null ? str : str2;
        }
    }

    /* renamed from: com.google.android.gms.internal.zzby.3 */
    static class C07573 extends zzby {
        C07573() {
        }

        @Nullable
        private String zzM(@Nullable String str) {
            if (TextUtils.isEmpty(str)) {
                return str;
            }
            int i = 0;
            int length = str.length();
            while (i < str.length() && str.charAt(i) == ActiveMQDestination.COMPOSITE_SEPERATOR) {
                i++;
            }
            while (length > 0 && str.charAt(length - 1) == ActiveMQDestination.COMPOSITE_SEPERATOR) {
                length--;
            }
            return (i == 0 && length == str.length()) ? str : str.substring(i, length);
        }

        public String zzb(@Nullable String str, String str2) {
            String zzM = zzM(str);
            String zzM2 = zzM(str2);
            return TextUtils.isEmpty(zzM) ? zzM2 : TextUtils.isEmpty(zzM2) ? zzM : zzM + Stomp.COMMA + zzM2;
        }
    }

    static {
        zzxs = new C07551();
        zzxt = new C07562();
        zzxu = new C07573();
    }

    public final void zza(Map<String, String> map, String str, String str2) {
        map.put(str, zzb((String) map.get(str), str2));
    }

    public abstract String zzb(@Nullable String str, String str2);
}
