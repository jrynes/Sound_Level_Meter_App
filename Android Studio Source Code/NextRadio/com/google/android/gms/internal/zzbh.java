package com.google.android.gms.internal;

import android.util.Base64OutputStream;
import com.google.android.gms.ads.internal.util.client.zzb;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.PriorityQueue;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Zone;

@zzhb
public class zzbh {
    private final int zztp;
    private final int zztq;
    private final int zztr;
    private final zzbg zzts;

    /* renamed from: com.google.android.gms.internal.zzbh.1 */
    class C07461 implements Comparator<String> {
        final /* synthetic */ zzbh zztt;

        C07461(zzbh com_google_android_gms_internal_zzbh) {
            this.zztt = com_google_android_gms_internal_zzbh;
        }

        public int compare(String s1, String s2) {
            return s2.length() - s1.length();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzbh.2 */
    class C07472 implements Comparator<com.google.android.gms.internal.zzbk.zza> {
        final /* synthetic */ zzbh zztt;

        C07472(zzbh com_google_android_gms_internal_zzbh) {
            this.zztt = com_google_android_gms_internal_zzbh;
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return zza((com.google.android.gms.internal.zzbk.zza) obj, (com.google.android.gms.internal.zzbk.zza) obj2);
        }

        public int zza(com.google.android.gms.internal.zzbk.zza com_google_android_gms_internal_zzbk_zza, com.google.android.gms.internal.zzbk.zza com_google_android_gms_internal_zzbk_zza2) {
            int i = com_google_android_gms_internal_zzbk_zza.zzty - com_google_android_gms_internal_zzbk_zza2.zzty;
            return i != 0 ? i : (int) (com_google_android_gms_internal_zzbk_zza.value - com_google_android_gms_internal_zzbk_zza2.value);
        }
    }

    static class zza {
        ByteArrayOutputStream zztu;
        Base64OutputStream zztv;

        public zza() {
            this.zztu = new ByteArrayOutputStream(Flags.EXTEND);
            this.zztv = new Base64OutputStream(this.zztu, 10);
        }

        public String toString() {
            String byteArrayOutputStream;
            try {
                this.zztv.close();
            } catch (Throwable e) {
                zzb.zzb("HashManager: Unable to convert to Base64.", e);
            }
            try {
                this.zztu.close();
                byteArrayOutputStream = this.zztu.toString();
            } catch (Throwable e2) {
                zzb.zzb("HashManager: Unable to convert to Base64.", e2);
                byteArrayOutputStream = Stomp.EMPTY;
            } finally {
                this.zztu = null;
                this.zztv = null;
            }
            return byteArrayOutputStream;
        }

        public void write(byte[] data) throws IOException {
            this.zztv.write(data);
        }
    }

    public zzbh(int i) {
        this.zzts = new zzbj();
        this.zztq = i;
        this.zztp = 6;
        this.zztr = 0;
    }

    private String zzv(String str) {
        String[] split = str.split(Stomp.NEWLINE);
        if (split.length == 0) {
            return Stomp.EMPTY;
        }
        zza zzcM = zzcM();
        Arrays.sort(split, new C07461(this));
        int i = 0;
        while (i < split.length && i < this.zztq) {
            if (split[i].trim().length() != 0) {
                try {
                    zzcM.write(this.zzts.zzu(split[i]));
                } catch (Throwable e) {
                    zzb.zzb("Error while writing hash to byteStream", e);
                }
            }
            i++;
        }
        return zzcM.toString();
    }

    public String zza(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            stringBuffer.append(((String) it.next()).toLowerCase(Locale.US));
            stringBuffer.append('\n');
        }
        switch (this.zztr) {
            case Tokenizer.EOF /*0*/:
                return zzw(stringBuffer.toString());
            case Zone.PRIMARY /*1*/:
                return zzv(stringBuffer.toString());
            default:
                return Stomp.EMPTY;
        }
    }

    zza zzcM() {
        return new zza();
    }

    String zzw(String str) {
        String[] split = str.split(Stomp.NEWLINE);
        if (split.length == 0) {
            return Stomp.EMPTY;
        }
        zza zzcM = zzcM();
        PriorityQueue priorityQueue = new PriorityQueue(this.zztq, new C07472(this));
        for (String zzy : split) {
            String[] zzy2 = zzbi.zzy(zzy);
            if (zzy2.length != 0) {
                zzbk.zza(zzy2, this.zztq, this.zztp, priorityQueue);
            }
        }
        Iterator it = priorityQueue.iterator();
        while (it.hasNext()) {
            try {
                zzcM.write(this.zzts.zzu(((com.google.android.gms.internal.zzbk.zza) it.next()).zztx));
            } catch (Throwable e) {
                zzb.zzb("Error while writing hash to byteStream", e);
            }
        }
        return zzcM.toString();
    }
}
