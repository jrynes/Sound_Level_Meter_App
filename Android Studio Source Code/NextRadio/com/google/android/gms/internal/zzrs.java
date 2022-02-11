package com.google.android.gms.internal;

import com.google.android.gms.internal.zzaf.zzh;
import com.google.android.gms.tagmanager.zzbg;
import com.google.android.gms.tagmanager.zzdf;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzrs {

    public static class zza {
        private final com.google.android.gms.internal.zzag.zza zzbkI;
        private final Map<String, com.google.android.gms.internal.zzag.zza> zzbmi;

        private zza(Map<String, com.google.android.gms.internal.zzag.zza> map, com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza) {
            this.zzbmi = map;
            this.zzbkI = com_google_android_gms_internal_zzag_zza;
        }

        public static zzb zzHH() {
            return new zzb();
        }

        public String toString() {
            return "Properties: " + zzHI() + " pushAfterEvaluate: " + this.zzbkI;
        }

        public Map<String, com.google.android.gms.internal.zzag.zza> zzHI() {
            return Collections.unmodifiableMap(this.zzbmi);
        }

        public com.google.android.gms.internal.zzag.zza zzHh() {
            return this.zzbkI;
        }

        public void zza(String str, com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza) {
            this.zzbmi.put(str, com_google_android_gms_internal_zzag_zza);
        }
    }

    public static class zzb {
        private com.google.android.gms.internal.zzag.zza zzbkI;
        private final Map<String, com.google.android.gms.internal.zzag.zza> zzbmi;

        private zzb() {
            this.zzbmi = new HashMap();
        }

        public zza zzHJ() {
            return new zza(this.zzbkI, null);
        }

        public zzb zzb(String str, com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza) {
            this.zzbmi.put(str, com_google_android_gms_internal_zzag_zza);
            return this;
        }

        public zzb zzq(com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza) {
            this.zzbkI = com_google_android_gms_internal_zzag_zza;
            return this;
        }
    }

    public static class zzc {
        private final String zzadc;
        private final List<zze> zzbmj;
        private final Map<String, List<zza>> zzbmk;
        private final int zzbml;

        private zzc(List<zze> list, Map<String, List<zza>> map, String str, int i) {
            this.zzbmj = Collections.unmodifiableList(list);
            this.zzbmk = Collections.unmodifiableMap(map);
            this.zzadc = str;
            this.zzbml = i;
        }

        public static zzd zzHK() {
            return new zzd();
        }

        public String getVersion() {
            return this.zzadc;
        }

        public String toString() {
            return "Rules: " + zzHL() + "  Macros: " + this.zzbmk;
        }

        public List<zze> zzHL() {
            return this.zzbmj;
        }

        public Map<String, List<zza>> zzHM() {
            return this.zzbmk;
        }
    }

    public static class zzd {
        private String zzadc;
        private final List<zze> zzbmj;
        private final Map<String, List<zza>> zzbmk;
        private int zzbml;

        private zzd() {
            this.zzbmj = new ArrayList();
            this.zzbmk = new HashMap();
            this.zzadc = Stomp.EMPTY;
            this.zzbml = 0;
        }

        public zzc zzHN() {
            return new zzc(this.zzbmk, this.zzadc, this.zzbml, null);
        }

        public zzd zzb(zze com_google_android_gms_internal_zzrs_zze) {
            this.zzbmj.add(com_google_android_gms_internal_zzrs_zze);
            return this;
        }

        public zzd zzc(zza com_google_android_gms_internal_zzrs_zza) {
            String zzg = zzdf.zzg((com.google.android.gms.internal.zzag.zza) com_google_android_gms_internal_zzrs_zza.zzHI().get(zzae.INSTANCE_NAME.toString()));
            List list = (List) this.zzbmk.get(zzg);
            if (list == null) {
                list = new ArrayList();
                this.zzbmk.put(zzg, list);
            }
            list.add(com_google_android_gms_internal_zzrs_zza);
            return this;
        }

        public zzd zzgD(String str) {
            this.zzadc = str;
            return this;
        }

        public zzd zzko(int i) {
            this.zzbml = i;
            return this;
        }
    }

    public static class zze {
        private final List<zza> zzbmm;
        private final List<zza> zzbmn;
        private final List<zza> zzbmo;
        private final List<zza> zzbmp;
        private final List<zza> zzbmq;
        private final List<zza> zzbmr;
        private final List<String> zzbms;
        private final List<String> zzbmt;
        private final List<String> zzbmu;
        private final List<String> zzbmv;

        private zze(List<zza> list, List<zza> list2, List<zza> list3, List<zza> list4, List<zza> list5, List<zza> list6, List<String> list7, List<String> list8, List<String> list9, List<String> list10) {
            this.zzbmm = Collections.unmodifiableList(list);
            this.zzbmn = Collections.unmodifiableList(list2);
            this.zzbmo = Collections.unmodifiableList(list3);
            this.zzbmp = Collections.unmodifiableList(list4);
            this.zzbmq = Collections.unmodifiableList(list5);
            this.zzbmr = Collections.unmodifiableList(list6);
            this.zzbms = Collections.unmodifiableList(list7);
            this.zzbmt = Collections.unmodifiableList(list8);
            this.zzbmu = Collections.unmodifiableList(list9);
            this.zzbmv = Collections.unmodifiableList(list10);
        }

        public static zzf zzHO() {
            return new zzf();
        }

        public String toString() {
            return "Positive predicates: " + zzHP() + "  Negative predicates: " + zzHQ() + "  Add tags: " + zzHR() + "  Remove tags: " + zzHS() + "  Add macros: " + zzHT() + "  Remove macros: " + zzHY();
        }

        public List<zza> zzHP() {
            return this.zzbmm;
        }

        public List<zza> zzHQ() {
            return this.zzbmn;
        }

        public List<zza> zzHR() {
            return this.zzbmo;
        }

        public List<zza> zzHS() {
            return this.zzbmp;
        }

        public List<zza> zzHT() {
            return this.zzbmq;
        }

        public List<String> zzHU() {
            return this.zzbms;
        }

        public List<String> zzHV() {
            return this.zzbmt;
        }

        public List<String> zzHW() {
            return this.zzbmu;
        }

        public List<String> zzHX() {
            return this.zzbmv;
        }

        public List<zza> zzHY() {
            return this.zzbmr;
        }
    }

    public static class zzf {
        private final List<zza> zzbmm;
        private final List<zza> zzbmn;
        private final List<zza> zzbmo;
        private final List<zza> zzbmp;
        private final List<zza> zzbmq;
        private final List<zza> zzbmr;
        private final List<String> zzbms;
        private final List<String> zzbmt;
        private final List<String> zzbmu;
        private final List<String> zzbmv;

        private zzf() {
            this.zzbmm = new ArrayList();
            this.zzbmn = new ArrayList();
            this.zzbmo = new ArrayList();
            this.zzbmp = new ArrayList();
            this.zzbmq = new ArrayList();
            this.zzbmr = new ArrayList();
            this.zzbms = new ArrayList();
            this.zzbmt = new ArrayList();
            this.zzbmu = new ArrayList();
            this.zzbmv = new ArrayList();
        }

        public zze zzHZ() {
            return new zze(this.zzbmn, this.zzbmo, this.zzbmp, this.zzbmq, this.zzbmr, this.zzbms, this.zzbmt, this.zzbmu, this.zzbmv, null);
        }

        public zzf zzd(zza com_google_android_gms_internal_zzrs_zza) {
            this.zzbmm.add(com_google_android_gms_internal_zzrs_zza);
            return this;
        }

        public zzf zze(zza com_google_android_gms_internal_zzrs_zza) {
            this.zzbmn.add(com_google_android_gms_internal_zzrs_zza);
            return this;
        }

        public zzf zzf(zza com_google_android_gms_internal_zzrs_zza) {
            this.zzbmo.add(com_google_android_gms_internal_zzrs_zza);
            return this;
        }

        public zzf zzg(zza com_google_android_gms_internal_zzrs_zza) {
            this.zzbmp.add(com_google_android_gms_internal_zzrs_zza);
            return this;
        }

        public zzf zzgE(String str) {
            this.zzbmu.add(str);
            return this;
        }

        public zzf zzgF(String str) {
            this.zzbmv.add(str);
            return this;
        }

        public zzf zzgG(String str) {
            this.zzbms.add(str);
            return this;
        }

        public zzf zzgH(String str) {
            this.zzbmt.add(str);
            return this;
        }

        public zzf zzh(zza com_google_android_gms_internal_zzrs_zza) {
            this.zzbmq.add(com_google_android_gms_internal_zzrs_zza);
            return this;
        }

        public zzf zzi(zza com_google_android_gms_internal_zzrs_zza) {
            this.zzbmr.add(com_google_android_gms_internal_zzrs_zza);
            return this;
        }
    }

    public static class zzg extends Exception {
        public zzg(String str) {
            super(str);
        }
    }

    private static com.google.android.gms.internal.zzag.zza zza(int i, com.google.android.gms.internal.zzaf.zzf com_google_android_gms_internal_zzaf_zzf, com.google.android.gms.internal.zzag.zza[] com_google_android_gms_internal_zzag_zzaArr, Set<Integer> set) throws zzg {
        int i2 = 0;
        if (set.contains(Integer.valueOf(i))) {
            zzgC("Value cycle detected.  Current value reference: " + i + ActiveMQDestination.PATH_SEPERATOR + "  Previous value references: " + set + ActiveMQDestination.PATH_SEPERATOR);
        }
        com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza = (com.google.android.gms.internal.zzag.zza) zza(com_google_android_gms_internal_zzaf_zzf.zziI, i, "values");
        if (com_google_android_gms_internal_zzag_zzaArr[i] != null) {
            return com_google_android_gms_internal_zzag_zzaArr[i];
        }
        com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza2 = null;
        set.add(Integer.valueOf(i));
        zzh zzp;
        int[] iArr;
        int length;
        int i3;
        int i4;
        switch (com_google_android_gms_internal_zzag_zza.type) {
            case Zone.PRIMARY /*1*/:
            case Service.RJE /*5*/:
            case Protocol.TCP /*6*/:
            case Protocol.EGP /*8*/:
                com_google_android_gms_internal_zzag_zza2 = com_google_android_gms_internal_zzag_zza;
                break;
            case Zone.SECONDARY /*2*/:
                zzp = zzp(com_google_android_gms_internal_zzag_zza);
                com_google_android_gms_internal_zzag_zza2 = zzo(com_google_android_gms_internal_zzag_zza);
                com_google_android_gms_internal_zzag_zza2.zzjy = new com.google.android.gms.internal.zzag.zza[zzp.zzjj.length];
                iArr = zzp.zzjj;
                length = iArr.length;
                i3 = 0;
                while (i2 < length) {
                    i4 = i3 + 1;
                    com_google_android_gms_internal_zzag_zza2.zzjy[i3] = zza(iArr[i2], com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, (Set) set);
                    i2++;
                    i3 = i4;
                }
                break;
            case Protocol.GGP /*3*/:
                com_google_android_gms_internal_zzag_zza2 = zzo(com_google_android_gms_internal_zzag_zza);
                zzh zzp2 = zzp(com_google_android_gms_internal_zzag_zza);
                if (zzp2.zzjk.length != zzp2.zzjl.length) {
                    zzgC("Uneven map keys (" + zzp2.zzjk.length + ") and map values (" + zzp2.zzjl.length + ")");
                }
                com_google_android_gms_internal_zzag_zza2.zzjz = new com.google.android.gms.internal.zzag.zza[zzp2.zzjk.length];
                com_google_android_gms_internal_zzag_zza2.zzjA = new com.google.android.gms.internal.zzag.zza[zzp2.zzjk.length];
                int[] iArr2 = zzp2.zzjk;
                int length2 = iArr2.length;
                i3 = 0;
                i4 = 0;
                while (i3 < length2) {
                    int i5 = i4 + 1;
                    com_google_android_gms_internal_zzag_zza2.zzjz[i4] = zza(iArr2[i3], com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, (Set) set);
                    i3++;
                    i4 = i5;
                }
                iArr = zzp2.zzjl;
                length = iArr.length;
                i3 = 0;
                while (i2 < length) {
                    i4 = i3 + 1;
                    com_google_android_gms_internal_zzag_zza2.zzjA[i3] = zza(iArr[i2], com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, (Set) set);
                    i2++;
                    i3 = i4;
                }
                break;
            case Type.MF /*4*/:
                com_google_android_gms_internal_zzag_zza2 = zzo(com_google_android_gms_internal_zzag_zza);
                com_google_android_gms_internal_zzag_zza2.zzjB = zzdf.zzg(zza(zzp(com_google_android_gms_internal_zzag_zza).zzjo, com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, (Set) set));
                break;
            case Service.ECHO /*7*/:
                com_google_android_gms_internal_zzag_zza2 = zzo(com_google_android_gms_internal_zzag_zza);
                zzp = zzp(com_google_android_gms_internal_zzag_zza);
                com_google_android_gms_internal_zzag_zza2.zzjF = new com.google.android.gms.internal.zzag.zza[zzp.zzjn.length];
                iArr = zzp.zzjn;
                length = iArr.length;
                i3 = 0;
                while (i2 < length) {
                    i4 = i3 + 1;
                    com_google_android_gms_internal_zzag_zza2.zzjF[i3] = zza(iArr[i2], com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, (Set) set);
                    i2++;
                    i3 = i4;
                }
                break;
        }
        if (com_google_android_gms_internal_zzag_zza2 == null) {
            zzgC("Invalid value: " + com_google_android_gms_internal_zzag_zza);
        }
        com_google_android_gms_internal_zzag_zzaArr[i] = com_google_android_gms_internal_zzag_zza2;
        set.remove(Integer.valueOf(i));
        return com_google_android_gms_internal_zzag_zza2;
    }

    private static zza zza(com.google.android.gms.internal.zzaf.zzb com_google_android_gms_internal_zzaf_zzb, com.google.android.gms.internal.zzaf.zzf com_google_android_gms_internal_zzaf_zzf, com.google.android.gms.internal.zzag.zza[] com_google_android_gms_internal_zzag_zzaArr, int i) throws zzg {
        zzb zzHH = zza.zzHH();
        for (int valueOf : com_google_android_gms_internal_zzaf_zzb.zzit) {
            com.google.android.gms.internal.zzaf.zze com_google_android_gms_internal_zzaf_zze = (com.google.android.gms.internal.zzaf.zze) zza(com_google_android_gms_internal_zzaf_zzf.zziJ, Integer.valueOf(valueOf).intValue(), "properties");
            String str = (String) zza(com_google_android_gms_internal_zzaf_zzf.zziH, com_google_android_gms_internal_zzaf_zze.key, "keys");
            com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza = (com.google.android.gms.internal.zzag.zza) zza(com_google_android_gms_internal_zzag_zzaArr, com_google_android_gms_internal_zzaf_zze.value, "values");
            if (zzae.PUSH_AFTER_EVALUATE.toString().equals(str)) {
                zzHH.zzq(com_google_android_gms_internal_zzag_zza);
            } else {
                zzHH.zzb(str, com_google_android_gms_internal_zzag_zza);
            }
        }
        return zzHH.zzHJ();
    }

    private static zze zza(com.google.android.gms.internal.zzaf.zzg com_google_android_gms_internal_zzaf_zzg, List<zza> list, List<zza> list2, List<zza> list3, com.google.android.gms.internal.zzaf.zzf com_google_android_gms_internal_zzaf_zzf) {
        zzf zzHO = zze.zzHO();
        for (int valueOf : com_google_android_gms_internal_zzaf_zzg.zziX) {
            zzHO.zzd((zza) list3.get(Integer.valueOf(valueOf).intValue()));
        }
        for (int valueOf2 : com_google_android_gms_internal_zzaf_zzg.zziY) {
            zzHO.zze((zza) list3.get(Integer.valueOf(valueOf2).intValue()));
        }
        for (int valueOf22 : com_google_android_gms_internal_zzaf_zzg.zziZ) {
            zzHO.zzf((zza) list.get(Integer.valueOf(valueOf22).intValue()));
        }
        for (int valueOf3 : com_google_android_gms_internal_zzaf_zzg.zzjb) {
            zzHO.zzgE(com_google_android_gms_internal_zzaf_zzf.zziI[Integer.valueOf(valueOf3).intValue()].zzjx);
        }
        for (int valueOf222 : com_google_android_gms_internal_zzaf_zzg.zzja) {
            zzHO.zzg((zza) list.get(Integer.valueOf(valueOf222).intValue()));
        }
        for (int valueOf32 : com_google_android_gms_internal_zzaf_zzg.zzjc) {
            zzHO.zzgF(com_google_android_gms_internal_zzaf_zzf.zziI[Integer.valueOf(valueOf32).intValue()].zzjx);
        }
        for (int valueOf2222 : com_google_android_gms_internal_zzaf_zzg.zzjd) {
            zzHO.zzh((zza) list2.get(Integer.valueOf(valueOf2222).intValue()));
        }
        for (int valueOf322 : com_google_android_gms_internal_zzaf_zzg.zzjf) {
            zzHO.zzgG(com_google_android_gms_internal_zzaf_zzf.zziI[Integer.valueOf(valueOf322).intValue()].zzjx);
        }
        for (int valueOf22222 : com_google_android_gms_internal_zzaf_zzg.zzje) {
            zzHO.zzi((zza) list2.get(Integer.valueOf(valueOf22222).intValue()));
        }
        for (int valueOf4 : com_google_android_gms_internal_zzaf_zzg.zzjg) {
            zzHO.zzgH(com_google_android_gms_internal_zzaf_zzf.zziI[Integer.valueOf(valueOf4).intValue()].zzjx);
        }
        return zzHO.zzHZ();
    }

    private static <T> T zza(T[] tArr, int i, String str) throws zzg {
        if (i < 0 || i >= tArr.length) {
            zzgC("Index out of bounds detected: " + i + " in " + str);
        }
        return tArr[i];
    }

    public static zzc zzb(com.google.android.gms.internal.zzaf.zzf com_google_android_gms_internal_zzaf_zzf) throws zzg {
        int i;
        int i2 = 0;
        com.google.android.gms.internal.zzag.zza[] com_google_android_gms_internal_zzag_zzaArr = new com.google.android.gms.internal.zzag.zza[com_google_android_gms_internal_zzaf_zzf.zziI.length];
        for (i = 0; i < com_google_android_gms_internal_zzaf_zzf.zziI.length; i++) {
            zza(i, com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, new HashSet(0));
        }
        zzd zzHK = zzc.zzHK();
        List arrayList = new ArrayList();
        for (i = 0; i < com_google_android_gms_internal_zzaf_zzf.zziL.length; i++) {
            arrayList.add(zza(com_google_android_gms_internal_zzaf_zzf.zziL[i], com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, i));
        }
        List arrayList2 = new ArrayList();
        for (i = 0; i < com_google_android_gms_internal_zzaf_zzf.zziM.length; i++) {
            arrayList2.add(zza(com_google_android_gms_internal_zzaf_zzf.zziM[i], com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, i));
        }
        List arrayList3 = new ArrayList();
        for (i = 0; i < com_google_android_gms_internal_zzaf_zzf.zziK.length; i++) {
            zza zza = zza(com_google_android_gms_internal_zzaf_zzf.zziK[i], com_google_android_gms_internal_zzaf_zzf, com_google_android_gms_internal_zzag_zzaArr, i);
            zzHK.zzc(zza);
            arrayList3.add(zza);
        }
        com.google.android.gms.internal.zzaf.zzg[] com_google_android_gms_internal_zzaf_zzgArr = com_google_android_gms_internal_zzaf_zzf.zziN;
        int length = com_google_android_gms_internal_zzaf_zzgArr.length;
        while (i2 < length) {
            zzHK.zzb(zza(com_google_android_gms_internal_zzaf_zzgArr[i2], arrayList, arrayList3, arrayList2, com_google_android_gms_internal_zzaf_zzf));
            i2++;
        }
        zzHK.zzgD(com_google_android_gms_internal_zzaf_zzf.version);
        zzHK.zzko(com_google_android_gms_internal_zzaf_zzf.zziV);
        return zzHK.zzHN();
    }

    public static void zzb(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[Flags.FLAG5];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    private static void zzgC(String str) throws zzg {
        zzbg.m1675e(str);
        throw new zzg(str);
    }

    public static com.google.android.gms.internal.zzag.zza zzo(com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza) {
        com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza2 = new com.google.android.gms.internal.zzag.zza();
        com_google_android_gms_internal_zzag_zza2.type = com_google_android_gms_internal_zzag_zza.type;
        com_google_android_gms_internal_zzag_zza2.zzjG = (int[]) com_google_android_gms_internal_zzag_zza.zzjG.clone();
        if (com_google_android_gms_internal_zzag_zza.zzjH) {
            com_google_android_gms_internal_zzag_zza2.zzjH = com_google_android_gms_internal_zzag_zza.zzjH;
        }
        return com_google_android_gms_internal_zzag_zza2;
    }

    private static zzh zzp(com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza) throws zzg {
        if (((zzh) com_google_android_gms_internal_zzag_zza.zza(zzh.zzjh)) == null) {
            zzgC("Expected a ServingValue and didn't get one. Value is: " + com_google_android_gms_internal_zzag_zza);
        }
        return (zzh) com_google_android_gms_internal_zzag_zza.zza(zzh.zzjh);
    }
}
