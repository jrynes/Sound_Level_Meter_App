package com.google.android.gms.tagmanager;

import android.content.Context;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzaf.zzi;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.internal.zzrs.zze;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

class zzcp {
    private static final zzbw<com.google.android.gms.internal.zzag.zza> zzbkq;
    private final DataLayer zzbhN;
    private volatile String zzbkA;
    private int zzbkB;
    private final com.google.android.gms.internal.zzrs.zzc zzbkr;
    private final zzah zzbks;
    private final Map<String, zzak> zzbkt;
    private final Map<String, zzak> zzbku;
    private final Map<String, zzak> zzbkv;
    private final zzl<com.google.android.gms.internal.zzrs.zza, zzbw<com.google.android.gms.internal.zzag.zza>> zzbkw;
    private final zzl<String, zzb> zzbkx;
    private final Set<zze> zzbky;
    private final Map<String, zzc> zzbkz;

    /* renamed from: com.google.android.gms.tagmanager.zzcp.1 */
    class C10421 implements com.google.android.gms.tagmanager.zzm.zza<com.google.android.gms.internal.zzrs.zza, zzbw<com.google.android.gms.internal.zzag.zza>> {
        final /* synthetic */ zzcp zzbkC;

        C10421(zzcp com_google_android_gms_tagmanager_zzcp) {
            this.zzbkC = com_google_android_gms_tagmanager_zzcp;
        }

        public /* synthetic */ int sizeOf(Object obj, Object obj2) {
            return zza((com.google.android.gms.internal.zzrs.zza) obj, (zzbw) obj2);
        }

        public int zza(com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza, zzbw<com.google.android.gms.internal.zzag.zza> com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza) {
            return ((com.google.android.gms.internal.zzag.zza) com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza.getObject()).getCachedSize();
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcp.2 */
    class C10432 implements com.google.android.gms.tagmanager.zzm.zza<String, zzb> {
        final /* synthetic */ zzcp zzbkC;

        C10432(zzcp com_google_android_gms_tagmanager_zzcp) {
            this.zzbkC = com_google_android_gms_tagmanager_zzcp;
        }

        public /* synthetic */ int sizeOf(Object obj, Object obj2) {
            return zza((String) obj, (zzb) obj2);
        }

        public int zza(String str, zzb com_google_android_gms_tagmanager_zzcp_zzb) {
            return str.length() + com_google_android_gms_tagmanager_zzcp_zzb.getSize();
        }
    }

    interface zza {
        void zza(zze com_google_android_gms_internal_zzrs_zze, Set<com.google.android.gms.internal.zzrs.zza> set, Set<com.google.android.gms.internal.zzrs.zza> set2, zzck com_google_android_gms_tagmanager_zzck);
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcp.3 */
    class C10443 implements zza {
        final /* synthetic */ zzcp zzbkC;
        final /* synthetic */ Map zzbkD;
        final /* synthetic */ Map zzbkE;
        final /* synthetic */ Map zzbkF;
        final /* synthetic */ Map zzbkG;

        C10443(zzcp com_google_android_gms_tagmanager_zzcp, Map map, Map map2, Map map3, Map map4) {
            this.zzbkC = com_google_android_gms_tagmanager_zzcp;
            this.zzbkD = map;
            this.zzbkE = map2;
            this.zzbkF = map3;
            this.zzbkG = map4;
        }

        public void zza(zze com_google_android_gms_internal_zzrs_zze, Set<com.google.android.gms.internal.zzrs.zza> set, Set<com.google.android.gms.internal.zzrs.zza> set2, zzck com_google_android_gms_tagmanager_zzck) {
            List list = (List) this.zzbkD.get(com_google_android_gms_internal_zzrs_zze);
            List list2 = (List) this.zzbkE.get(com_google_android_gms_internal_zzrs_zze);
            if (list != null) {
                set.addAll(list);
                com_google_android_gms_tagmanager_zzck.zzGJ().zzc(list, list2);
            }
            list = (List) this.zzbkF.get(com_google_android_gms_internal_zzrs_zze);
            list2 = (List) this.zzbkG.get(com_google_android_gms_internal_zzrs_zze);
            if (list != null) {
                set2.addAll(list);
                com_google_android_gms_tagmanager_zzck.zzGK().zzc(list, list2);
            }
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcp.4 */
    class C10454 implements zza {
        final /* synthetic */ zzcp zzbkC;

        C10454(zzcp com_google_android_gms_tagmanager_zzcp) {
            this.zzbkC = com_google_android_gms_tagmanager_zzcp;
        }

        public void zza(zze com_google_android_gms_internal_zzrs_zze, Set<com.google.android.gms.internal.zzrs.zza> set, Set<com.google.android.gms.internal.zzrs.zza> set2, zzck com_google_android_gms_tagmanager_zzck) {
            set.addAll(com_google_android_gms_internal_zzrs_zze.zzHR());
            set2.addAll(com_google_android_gms_internal_zzrs_zze.zzHS());
            com_google_android_gms_tagmanager_zzck.zzGL().zzc(com_google_android_gms_internal_zzrs_zze.zzHR(), com_google_android_gms_internal_zzrs_zze.zzHW());
            com_google_android_gms_tagmanager_zzck.zzGM().zzc(com_google_android_gms_internal_zzrs_zze.zzHS(), com_google_android_gms_internal_zzrs_zze.zzHX());
        }
    }

    private static class zzb {
        private zzbw<com.google.android.gms.internal.zzag.zza> zzbkH;
        private com.google.android.gms.internal.zzag.zza zzbkI;

        public zzb(zzbw<com.google.android.gms.internal.zzag.zza> com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza, com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza) {
            this.zzbkH = com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza;
            this.zzbkI = com_google_android_gms_internal_zzag_zza;
        }

        public int getSize() {
            return (this.zzbkI == null ? 0 : this.zzbkI.getCachedSize()) + ((com.google.android.gms.internal.zzag.zza) this.zzbkH.getObject()).getCachedSize();
        }

        public zzbw<com.google.android.gms.internal.zzag.zza> zzHg() {
            return this.zzbkH;
        }

        public com.google.android.gms.internal.zzag.zza zzHh() {
            return this.zzbkI;
        }
    }

    private static class zzc {
        private final Map<zze, List<com.google.android.gms.internal.zzrs.zza>> zzbkJ;
        private final Map<zze, List<com.google.android.gms.internal.zzrs.zza>> zzbkK;
        private final Map<zze, List<String>> zzbkL;
        private final Map<zze, List<String>> zzbkM;
        private com.google.android.gms.internal.zzrs.zza zzbkN;
        private final Set<zze> zzbky;

        public zzc() {
            this.zzbky = new HashSet();
            this.zzbkJ = new HashMap();
            this.zzbkL = new HashMap();
            this.zzbkK = new HashMap();
            this.zzbkM = new HashMap();
        }

        public Set<zze> zzHi() {
            return this.zzbky;
        }

        public Map<zze, List<com.google.android.gms.internal.zzrs.zza>> zzHj() {
            return this.zzbkJ;
        }

        public Map<zze, List<String>> zzHk() {
            return this.zzbkL;
        }

        public Map<zze, List<String>> zzHl() {
            return this.zzbkM;
        }

        public Map<zze, List<com.google.android.gms.internal.zzrs.zza>> zzHm() {
            return this.zzbkK;
        }

        public com.google.android.gms.internal.zzrs.zza zzHn() {
            return this.zzbkN;
        }

        public void zza(zze com_google_android_gms_internal_zzrs_zze) {
            this.zzbky.add(com_google_android_gms_internal_zzrs_zze);
        }

        public void zza(zze com_google_android_gms_internal_zzrs_zze, com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza) {
            List list = (List) this.zzbkJ.get(com_google_android_gms_internal_zzrs_zze);
            if (list == null) {
                list = new ArrayList();
                this.zzbkJ.put(com_google_android_gms_internal_zzrs_zze, list);
            }
            list.add(com_google_android_gms_internal_zzrs_zza);
        }

        public void zza(zze com_google_android_gms_internal_zzrs_zze, String str) {
            List list = (List) this.zzbkL.get(com_google_android_gms_internal_zzrs_zze);
            if (list == null) {
                list = new ArrayList();
                this.zzbkL.put(com_google_android_gms_internal_zzrs_zze, list);
            }
            list.add(str);
        }

        public void zzb(com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza) {
            this.zzbkN = com_google_android_gms_internal_zzrs_zza;
        }

        public void zzb(zze com_google_android_gms_internal_zzrs_zze, com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza) {
            List list = (List) this.zzbkK.get(com_google_android_gms_internal_zzrs_zze);
            if (list == null) {
                list = new ArrayList();
                this.zzbkK.put(com_google_android_gms_internal_zzrs_zze, list);
            }
            list.add(com_google_android_gms_internal_zzrs_zza);
        }

        public void zzb(zze com_google_android_gms_internal_zzrs_zze, String str) {
            List list = (List) this.zzbkM.get(com_google_android_gms_internal_zzrs_zze);
            if (list == null) {
                list = new ArrayList();
                this.zzbkM.put(com_google_android_gms_internal_zzrs_zze, list);
            }
            list.add(str);
        }
    }

    static {
        zzbkq = new zzbw(zzdf.zzHF(), true);
    }

    public zzcp(Context context, com.google.android.gms.internal.zzrs.zzc com_google_android_gms_internal_zzrs_zzc, DataLayer dataLayer, com.google.android.gms.tagmanager.zzt.zza com_google_android_gms_tagmanager_zzt_zza, com.google.android.gms.tagmanager.zzt.zza com_google_android_gms_tagmanager_zzt_zza2, zzah com_google_android_gms_tagmanager_zzah) {
        if (com_google_android_gms_internal_zzrs_zzc == null) {
            throw new NullPointerException("resource cannot be null");
        }
        this.zzbkr = com_google_android_gms_internal_zzrs_zzc;
        this.zzbky = new HashSet(com_google_android_gms_internal_zzrs_zzc.zzHL());
        this.zzbhN = dataLayer;
        this.zzbks = com_google_android_gms_tagmanager_zzah;
        this.zzbkw = new zzm().zza(AccessibilityNodeInfoCompat.ACTION_DISMISS, new C10421(this));
        this.zzbkx = new zzm().zza(AccessibilityNodeInfoCompat.ACTION_DISMISS, new C10432(this));
        this.zzbkt = new HashMap();
        zzb(new zzj(context));
        zzb(new zzt(com_google_android_gms_tagmanager_zzt_zza2));
        zzb(new zzx(dataLayer));
        zzb(new zzdg(context, dataLayer));
        zzb(new zzdb(context, dataLayer));
        this.zzbku = new HashMap();
        zzc(new zzr());
        zzc(new zzae());
        zzc(new zzaf());
        zzc(new zzam());
        zzc(new zzan());
        zzc(new zzbc());
        zzc(new zzbd());
        zzc(new zzcf());
        zzc(new zzcy());
        this.zzbkv = new HashMap();
        zza(new zzb(context));
        zza(new zzc(context));
        zza(new zze(context));
        zza(new zzf(context));
        zza(new zzg(context));
        zza(new zzh(context));
        zza(new zzi(context));
        zza(new zzn());
        zza(new zzq(this.zzbkr.getVersion()));
        zza(new zzt(com_google_android_gms_tagmanager_zzt_zza));
        zza(new zzv(dataLayer));
        zza(new zzaa(context));
        zza(new zzab());
        zza(new zzad());
        zza(new zzai(this));
        zza(new zzao());
        zza(new zzap());
        zza(new zzaw(context));
        zza(new zzay());
        zza(new zzbb());
        zza(new zzbi());
        zza(new zzbk(context));
        zza(new zzbx());
        zza(new zzbz());
        zza(new zzcc());
        zza(new zzce());
        zza(new zzcg(context));
        zza(new zzcq());
        zza(new zzcr());
        zza(new zzda());
        zza(new zzdh());
        this.zzbkz = new HashMap();
        for (zze com_google_android_gms_internal_zzrs_zze : this.zzbky) {
            if (com_google_android_gms_tagmanager_zzah.zzGA()) {
                zza(com_google_android_gms_internal_zzrs_zze.zzHT(), com_google_android_gms_internal_zzrs_zze.zzHU(), "add macro");
                zza(com_google_android_gms_internal_zzrs_zze.zzHY(), com_google_android_gms_internal_zzrs_zze.zzHV(), "remove macro");
                zza(com_google_android_gms_internal_zzrs_zze.zzHR(), com_google_android_gms_internal_zzrs_zze.zzHW(), "add tag");
                zza(com_google_android_gms_internal_zzrs_zze.zzHS(), com_google_android_gms_internal_zzrs_zze.zzHX(), "remove tag");
            }
            int i = 0;
            while (i < com_google_android_gms_internal_zzrs_zze.zzHT().size()) {
                com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza = (com.google.android.gms.internal.zzrs.zza) com_google_android_gms_internal_zzrs_zze.zzHT().get(i);
                String str = "Unknown";
                if (com_google_android_gms_tagmanager_zzah.zzGA() && i < com_google_android_gms_internal_zzrs_zze.zzHU().size()) {
                    str = (String) com_google_android_gms_internal_zzrs_zze.zzHU().get(i);
                }
                zzc zzi = zzi(this.zzbkz, zza(com_google_android_gms_internal_zzrs_zza));
                zzi.zza(com_google_android_gms_internal_zzrs_zze);
                zzi.zza(com_google_android_gms_internal_zzrs_zze, com_google_android_gms_internal_zzrs_zza);
                zzi.zza(com_google_android_gms_internal_zzrs_zze, str);
                i++;
            }
            i = 0;
            while (i < com_google_android_gms_internal_zzrs_zze.zzHY().size()) {
                com_google_android_gms_internal_zzrs_zza = (com.google.android.gms.internal.zzrs.zza) com_google_android_gms_internal_zzrs_zze.zzHY().get(i);
                str = "Unknown";
                if (com_google_android_gms_tagmanager_zzah.zzGA() && i < com_google_android_gms_internal_zzrs_zze.zzHV().size()) {
                    str = (String) com_google_android_gms_internal_zzrs_zze.zzHV().get(i);
                }
                zzi = zzi(this.zzbkz, zza(com_google_android_gms_internal_zzrs_zza));
                zzi.zza(com_google_android_gms_internal_zzrs_zze);
                zzi.zzb(com_google_android_gms_internal_zzrs_zze, com_google_android_gms_internal_zzrs_zza);
                zzi.zzb(com_google_android_gms_internal_zzrs_zze, str);
                i++;
            }
        }
        for (Entry entry : this.zzbkr.zzHM().entrySet()) {
            for (com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza2 : (List) entry.getValue()) {
                if (!zzdf.zzk((com.google.android.gms.internal.zzag.zza) com_google_android_gms_internal_zzrs_zza2.zzHI().get(zzae.NOT_DEFAULT_MACRO.toString())).booleanValue()) {
                    zzi(this.zzbkz, (String) entry.getKey()).zzb(com_google_android_gms_internal_zzrs_zza2);
                }
            }
        }
    }

    private String zzHf() {
        if (this.zzbkB <= 1) {
            return Stomp.EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Integer.toString(this.zzbkB));
        for (int i = 2; i < this.zzbkB; i++) {
            stringBuilder.append(' ');
        }
        stringBuilder.append(": ");
        return stringBuilder.toString();
    }

    private zzbw<com.google.android.gms.internal.zzag.zza> zza(com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza, Set<String> set, zzdi com_google_android_gms_tagmanager_zzdi) {
        if (!com_google_android_gms_internal_zzag_zza.zzjH) {
            return new zzbw(com_google_android_gms_internal_zzag_zza, true);
        }
        com.google.android.gms.internal.zzag.zza zzo;
        int i;
        zzbw zza;
        switch (com_google_android_gms_internal_zzag_zza.type) {
            case Zone.SECONDARY /*2*/:
                zzo = zzrs.zzo(com_google_android_gms_internal_zzag_zza);
                zzo.zzjy = new com.google.android.gms.internal.zzag.zza[com_google_android_gms_internal_zzag_zza.zzjy.length];
                for (i = 0; i < com_google_android_gms_internal_zzag_zza.zzjy.length; i++) {
                    zza = zza(com_google_android_gms_internal_zzag_zza.zzjy[i], (Set) set, com_google_android_gms_tagmanager_zzdi.zzkh(i));
                    if (zza == zzbkq) {
                        return zzbkq;
                    }
                    zzo.zzjy[i] = (com.google.android.gms.internal.zzag.zza) zza.getObject();
                }
                return new zzbw(zzo, false);
            case Protocol.GGP /*3*/:
                zzo = zzrs.zzo(com_google_android_gms_internal_zzag_zza);
                if (com_google_android_gms_internal_zzag_zza.zzjz.length != com_google_android_gms_internal_zzag_zza.zzjA.length) {
                    zzbg.m1675e("Invalid serving value: " + com_google_android_gms_internal_zzag_zza.toString());
                    return zzbkq;
                }
                zzo.zzjz = new com.google.android.gms.internal.zzag.zza[com_google_android_gms_internal_zzag_zza.zzjz.length];
                zzo.zzjA = new com.google.android.gms.internal.zzag.zza[com_google_android_gms_internal_zzag_zza.zzjz.length];
                for (i = 0; i < com_google_android_gms_internal_zzag_zza.zzjz.length; i++) {
                    zza = zza(com_google_android_gms_internal_zzag_zza.zzjz[i], (Set) set, com_google_android_gms_tagmanager_zzdi.zzki(i));
                    zzbw zza2 = zza(com_google_android_gms_internal_zzag_zza.zzjA[i], (Set) set, com_google_android_gms_tagmanager_zzdi.zzkj(i));
                    if (zza == zzbkq || zza2 == zzbkq) {
                        return zzbkq;
                    }
                    zzo.zzjz[i] = (com.google.android.gms.internal.zzag.zza) zza.getObject();
                    zzo.zzjA[i] = (com.google.android.gms.internal.zzag.zza) zza2.getObject();
                }
                return new zzbw(zzo, false);
            case Type.MF /*4*/:
                if (set.contains(com_google_android_gms_internal_zzag_zza.zzjB)) {
                    zzbg.m1675e("Macro cycle detected.  Current macro reference: " + com_google_android_gms_internal_zzag_zza.zzjB + ActiveMQDestination.PATH_SEPERATOR + "  Previous macro references: " + set.toString() + ActiveMQDestination.PATH_SEPERATOR);
                    return zzbkq;
                }
                set.add(com_google_android_gms_internal_zzag_zza.zzjB);
                zzbw<com.google.android.gms.internal.zzag.zza> zza3 = zzdj.zza(zza(com_google_android_gms_internal_zzag_zza.zzjB, (Set) set, com_google_android_gms_tagmanager_zzdi.zzGO()), com_google_android_gms_internal_zzag_zza.zzjG);
                set.remove(com_google_android_gms_internal_zzag_zza.zzjB);
                return zza3;
            case Service.ECHO /*7*/:
                zzo = zzrs.zzo(com_google_android_gms_internal_zzag_zza);
                zzo.zzjF = new com.google.android.gms.internal.zzag.zza[com_google_android_gms_internal_zzag_zza.zzjF.length];
                for (i = 0; i < com_google_android_gms_internal_zzag_zza.zzjF.length; i++) {
                    zza = zza(com_google_android_gms_internal_zzag_zza.zzjF[i], (Set) set, com_google_android_gms_tagmanager_zzdi.zzkk(i));
                    if (zza == zzbkq) {
                        return zzbkq;
                    }
                    zzo.zzjF[i] = (com.google.android.gms.internal.zzag.zza) zza.getObject();
                }
                return new zzbw(zzo, false);
            default:
                zzbg.m1675e("Unknown type: " + com_google_android_gms_internal_zzag_zza.type);
                return zzbkq;
        }
    }

    private zzbw<com.google.android.gms.internal.zzag.zza> zza(String str, Set<String> set, zzbj com_google_android_gms_tagmanager_zzbj) {
        this.zzbkB++;
        zzb com_google_android_gms_tagmanager_zzcp_zzb = (zzb) this.zzbkx.get(str);
        if (com_google_android_gms_tagmanager_zzcp_zzb == null || this.zzbks.zzGA()) {
            zzc com_google_android_gms_tagmanager_zzcp_zzc = (zzc) this.zzbkz.get(str);
            if (com_google_android_gms_tagmanager_zzcp_zzc == null) {
                zzbg.m1675e(zzHf() + "Invalid macro: " + str);
                this.zzbkB--;
                return zzbkq;
            }
            com.google.android.gms.internal.zzrs.zza zzHn;
            zzbw zza = zza(str, com_google_android_gms_tagmanager_zzcp_zzc.zzHi(), com_google_android_gms_tagmanager_zzcp_zzc.zzHj(), com_google_android_gms_tagmanager_zzcp_zzc.zzHk(), com_google_android_gms_tagmanager_zzcp_zzc.zzHm(), com_google_android_gms_tagmanager_zzcp_zzc.zzHl(), set, com_google_android_gms_tagmanager_zzbj.zzGq());
            if (((Set) zza.getObject()).isEmpty()) {
                zzHn = com_google_android_gms_tagmanager_zzcp_zzc.zzHn();
            } else {
                if (((Set) zza.getObject()).size() > 1) {
                    zzbg.zzaK(zzHf() + "Multiple macros active for macroName " + str);
                }
                zzHn = (com.google.android.gms.internal.zzrs.zza) ((Set) zza.getObject()).iterator().next();
            }
            if (zzHn == null) {
                this.zzbkB--;
                return zzbkq;
            }
            zzbw zza2 = zza(this.zzbkv, zzHn, (Set) set, com_google_android_gms_tagmanager_zzbj.zzGG());
            boolean z = zza.zzGP() && zza2.zzGP();
            zzbw<com.google.android.gms.internal.zzag.zza> com_google_android_gms_tagmanager_zzbw = zza2 == zzbkq ? zzbkq : new zzbw(zza2.getObject(), z);
            com.google.android.gms.internal.zzag.zza zzHh = zzHn.zzHh();
            if (com_google_android_gms_tagmanager_zzbw.zzGP()) {
                this.zzbkx.zzh(str, new zzb(com_google_android_gms_tagmanager_zzbw, zzHh));
            }
            zza(zzHh, (Set) set);
            this.zzbkB--;
            return com_google_android_gms_tagmanager_zzbw;
        }
        zza(com_google_android_gms_tagmanager_zzcp_zzb.zzHh(), (Set) set);
        this.zzbkB--;
        return com_google_android_gms_tagmanager_zzcp_zzb.zzHg();
    }

    private zzbw<com.google.android.gms.internal.zzag.zza> zza(Map<String, zzak> map, com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza, Set<String> set, zzch com_google_android_gms_tagmanager_zzch) {
        boolean z = true;
        com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza = (com.google.android.gms.internal.zzag.zza) com_google_android_gms_internal_zzrs_zza.zzHI().get(zzae.FUNCTION.toString());
        if (com_google_android_gms_internal_zzag_zza == null) {
            zzbg.m1675e("No function id in properties");
            return zzbkq;
        }
        String str = com_google_android_gms_internal_zzag_zza.zzjC;
        zzak com_google_android_gms_tagmanager_zzak = (zzak) map.get(str);
        if (com_google_android_gms_tagmanager_zzak == null) {
            zzbg.m1675e(str + " has no backing implementation.");
            return zzbkq;
        }
        zzbw<com.google.android.gms.internal.zzag.zza> com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza = (zzbw) this.zzbkw.get(com_google_android_gms_internal_zzrs_zza);
        if (com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza != null && !this.zzbks.zzGA()) {
            return com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza;
        }
        Map hashMap = new HashMap();
        boolean z2 = true;
        for (Entry entry : com_google_android_gms_internal_zzrs_zza.zzHI().entrySet()) {
            zzbw zza = zza((com.google.android.gms.internal.zzag.zza) entry.getValue(), (Set) set, com_google_android_gms_tagmanager_zzch.zzgj((String) entry.getKey()).zze((com.google.android.gms.internal.zzag.zza) entry.getValue()));
            if (zza == zzbkq) {
                return zzbkq;
            }
            boolean z3;
            if (zza.zzGP()) {
                com_google_android_gms_internal_zzrs_zza.zza((String) entry.getKey(), (com.google.android.gms.internal.zzag.zza) zza.getObject());
                z3 = z2;
            } else {
                z3 = false;
            }
            hashMap.put(entry.getKey(), zza.getObject());
            z2 = z3;
        }
        if (com_google_android_gms_tagmanager_zzak.zze(hashMap.keySet())) {
            if (!(z2 && com_google_android_gms_tagmanager_zzak.zzFW())) {
                z = false;
            }
            com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza = new zzbw(com_google_android_gms_tagmanager_zzak.zzP(hashMap), z);
            if (z) {
                this.zzbkw.zzh(com_google_android_gms_internal_zzrs_zza, com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza);
            }
            com_google_android_gms_tagmanager_zzch.zzd((com.google.android.gms.internal.zzag.zza) com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza.getObject());
            return com_google_android_gms_tagmanager_zzbw_com_google_android_gms_internal_zzag_zza;
        }
        zzbg.m1675e("Incorrect keys for function " + str + " required " + com_google_android_gms_tagmanager_zzak.zzGC() + " had " + hashMap.keySet());
        return zzbkq;
    }

    private zzbw<Set<com.google.android.gms.internal.zzrs.zza>> zza(Set<zze> set, Set<String> set2, zza com_google_android_gms_tagmanager_zzcp_zza, zzco com_google_android_gms_tagmanager_zzco) {
        Set hashSet = new HashSet();
        Collection hashSet2 = new HashSet();
        boolean z = true;
        for (zze com_google_android_gms_internal_zzrs_zze : set) {
            zzck zzGN = com_google_android_gms_tagmanager_zzco.zzGN();
            zzbw zza = zza(com_google_android_gms_internal_zzrs_zze, (Set) set2, zzGN);
            if (((Boolean) zza.getObject()).booleanValue()) {
                com_google_android_gms_tagmanager_zzcp_zza.zza(com_google_android_gms_internal_zzrs_zze, hashSet, hashSet2, zzGN);
            }
            boolean z2 = z && zza.zzGP();
            z = z2;
        }
        hashSet.removeAll(hashSet2);
        com_google_android_gms_tagmanager_zzco.zzf(hashSet);
        return new zzbw(hashSet, z);
    }

    private static String zza(com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza) {
        return zzdf.zzg((com.google.android.gms.internal.zzag.zza) com_google_android_gms_internal_zzrs_zza.zzHI().get(zzae.INSTANCE_NAME.toString()));
    }

    private void zza(com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza, Set<String> set) {
        if (com_google_android_gms_internal_zzag_zza != null) {
            zzbw zza = zza(com_google_android_gms_internal_zzag_zza, (Set) set, new zzbu());
            if (zza != zzbkq) {
                Object zzl = zzdf.zzl((com.google.android.gms.internal.zzag.zza) zza.getObject());
                if (zzl instanceof Map) {
                    this.zzbhN.push((Map) zzl);
                } else if (zzl instanceof List) {
                    for (Object zzl2 : (List) zzl2) {
                        if (zzl2 instanceof Map) {
                            this.zzbhN.push((Map) zzl2);
                        } else {
                            zzbg.zzaK("pushAfterEvaluate: value not a Map");
                        }
                    }
                } else {
                    zzbg.zzaK("pushAfterEvaluate: value not a Map or List");
                }
            }
        }
    }

    private static void zza(List<com.google.android.gms.internal.zzrs.zza> list, List<String> list2, String str) {
        if (list.size() != list2.size()) {
            zzbg.zzaJ("Invalid resource: imbalance of rule names of functions for " + str + " operation. Using default rule name instead");
        }
    }

    private static void zza(Map<String, zzak> map, zzak com_google_android_gms_tagmanager_zzak) {
        if (map.containsKey(com_google_android_gms_tagmanager_zzak.zzGB())) {
            throw new IllegalArgumentException("Duplicate function type name: " + com_google_android_gms_tagmanager_zzak.zzGB());
        }
        map.put(com_google_android_gms_tagmanager_zzak.zzGB(), com_google_android_gms_tagmanager_zzak);
    }

    private static zzc zzi(Map<String, zzc> map, String str) {
        zzc com_google_android_gms_tagmanager_zzcp_zzc = (zzc) map.get(str);
        if (com_google_android_gms_tagmanager_zzcp_zzc != null) {
            return com_google_android_gms_tagmanager_zzcp_zzc;
        }
        com_google_android_gms_tagmanager_zzcp_zzc = new zzc();
        map.put(str, com_google_android_gms_tagmanager_zzcp_zzc);
        return com_google_android_gms_tagmanager_zzcp_zzc;
    }

    public synchronized void zzF(List<zzi> list) {
        for (zzi com_google_android_gms_internal_zzaf_zzi : list) {
            if (com_google_android_gms_internal_zzaf_zzi.name == null || !com_google_android_gms_internal_zzaf_zzi.name.startsWith("gaExperiment:")) {
                zzbg.m1676v("Ignored supplemental: " + com_google_android_gms_internal_zzaf_zzi);
            } else {
                zzaj.zza(this.zzbhN, com_google_android_gms_internal_zzaf_zzi);
            }
        }
    }

    synchronized String zzHe() {
        return this.zzbkA;
    }

    zzbw<Boolean> zza(com.google.android.gms.internal.zzrs.zza com_google_android_gms_internal_zzrs_zza, Set<String> set, zzch com_google_android_gms_tagmanager_zzch) {
        zzbw zza = zza(this.zzbku, com_google_android_gms_internal_zzrs_zza, (Set) set, com_google_android_gms_tagmanager_zzch);
        Boolean zzk = zzdf.zzk((com.google.android.gms.internal.zzag.zza) zza.getObject());
        com_google_android_gms_tagmanager_zzch.zzd(zzdf.zzR(zzk));
        return new zzbw(zzk, zza.zzGP());
    }

    zzbw<Boolean> zza(zze com_google_android_gms_internal_zzrs_zze, Set<String> set, zzck com_google_android_gms_tagmanager_zzck) {
        boolean z = true;
        for (com.google.android.gms.internal.zzrs.zza zza : com_google_android_gms_internal_zzrs_zze.zzHQ()) {
            zzbw zza2 = zza(zza, (Set) set, com_google_android_gms_tagmanager_zzck.zzGH());
            if (((Boolean) zza2.getObject()).booleanValue()) {
                com_google_android_gms_tagmanager_zzck.zzf(zzdf.zzR(Boolean.valueOf(false)));
                return new zzbw(Boolean.valueOf(false), zza2.zzGP());
            }
            boolean z2 = z && zza2.zzGP();
            z = z2;
        }
        for (com.google.android.gms.internal.zzrs.zza zza3 : com_google_android_gms_internal_zzrs_zze.zzHP()) {
            zza2 = zza(zza3, (Set) set, com_google_android_gms_tagmanager_zzck.zzGI());
            if (((Boolean) zza2.getObject()).booleanValue()) {
                z = z && zza2.zzGP();
            } else {
                com_google_android_gms_tagmanager_zzck.zzf(zzdf.zzR(Boolean.valueOf(false)));
                return new zzbw(Boolean.valueOf(false), zza2.zzGP());
            }
        }
        com_google_android_gms_tagmanager_zzck.zzf(zzdf.zzR(Boolean.valueOf(true)));
        return new zzbw(Boolean.valueOf(true), z);
    }

    zzbw<Set<com.google.android.gms.internal.zzrs.zza>> zza(String str, Set<zze> set, Map<zze, List<com.google.android.gms.internal.zzrs.zza>> map, Map<zze, List<String>> map2, Map<zze, List<com.google.android.gms.internal.zzrs.zza>> map3, Map<zze, List<String>> map4, Set<String> set2, zzco com_google_android_gms_tagmanager_zzco) {
        return zza((Set) set, (Set) set2, new C10443(this, map, map2, map3, map4), com_google_android_gms_tagmanager_zzco);
    }

    zzbw<Set<com.google.android.gms.internal.zzrs.zza>> zza(Set<zze> set, zzco com_google_android_gms_tagmanager_zzco) {
        return zza((Set) set, new HashSet(), new C10454(this), com_google_android_gms_tagmanager_zzco);
    }

    void zza(zzak com_google_android_gms_tagmanager_zzak) {
        zza(this.zzbkv, com_google_android_gms_tagmanager_zzak);
    }

    void zzb(zzak com_google_android_gms_tagmanager_zzak) {
        zza(this.zzbkt, com_google_android_gms_tagmanager_zzak);
    }

    void zzc(zzak com_google_android_gms_tagmanager_zzak) {
        zza(this.zzbku, com_google_android_gms_tagmanager_zzak);
    }

    public synchronized void zzfR(String str) {
        zzgo(str);
        zzag zzge = this.zzbks.zzge(str);
        zzu zzGy = zzge.zzGy();
        for (com.google.android.gms.internal.zzrs.zza zza : (Set) zza(this.zzbky, zzGy.zzGq()).getObject()) {
            zza(this.zzbkt, zza, new HashSet(), zzGy.zzGp());
        }
        zzge.zzGz();
        zzgo(null);
    }

    public zzbw<com.google.android.gms.internal.zzag.zza> zzgn(String str) {
        this.zzbkB = 0;
        zzag zzgd = this.zzbks.zzgd(str);
        zzbw<com.google.android.gms.internal.zzag.zza> zza = zza(str, new HashSet(), zzgd.zzGx());
        zzgd.zzGz();
        return zza;
    }

    synchronized void zzgo(String str) {
        this.zzbkA = str;
    }
}
