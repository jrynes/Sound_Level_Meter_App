package com.google.android.gms.tagmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.WKSRecord.Service;

public class DataLayer {
    public static final String EVENT_KEY = "event";
    public static final Object OBJECT_NOT_PRESENT;
    static final String[] zzbir;
    private static final Pattern zzbis;
    private final ConcurrentHashMap<zzb, Integer> zzbit;
    private final Map<String, Object> zzbiu;
    private final ReentrantLock zzbiv;
    private final LinkedList<Map<String, Object>> zzbiw;
    private final zzc zzbix;
    private final CountDownLatch zzbiy;

    interface zzc {

        public interface zza {
            void zzB(List<zza> list);
        }

        void zza(zza com_google_android_gms_tagmanager_DataLayer_zzc_zza);

        void zza(List<zza> list, long j);

        void zzfZ(String str);
    }

    /* renamed from: com.google.android.gms.tagmanager.DataLayer.1 */
    class C10261 implements zzc {
        C10261() {
        }

        public void zza(zza com_google_android_gms_tagmanager_DataLayer_zzc_zza) {
            com_google_android_gms_tagmanager_DataLayer_zzc_zza.zzB(new ArrayList());
        }

        public void zza(List<zza> list, long j) {
        }

        public void zzfZ(String str) {
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.DataLayer.2 */
    class C10272 implements zza {
        final /* synthetic */ DataLayer zzbiz;

        C10272(DataLayer dataLayer) {
            this.zzbiz = dataLayer;
        }

        public void zzB(List<zza> list) {
            for (zza com_google_android_gms_tagmanager_DataLayer_zza : list) {
                this.zzbiz.zzS(this.zzbiz.zzn(com_google_android_gms_tagmanager_DataLayer_zza.zzvs, com_google_android_gms_tagmanager_DataLayer_zza.zzNc));
            }
            this.zzbiz.zzbiy.countDown();
        }
    }

    static final class zza {
        public final Object zzNc;
        public final String zzvs;

        zza(String str, Object obj) {
            this.zzvs = str;
            this.zzNc = obj;
        }

        public boolean equals(Object o) {
            if (!(o instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_tagmanager_DataLayer_zza = (zza) o;
            return this.zzvs.equals(com_google_android_gms_tagmanager_DataLayer_zza.zzvs) && this.zzNc.equals(com_google_android_gms_tagmanager_DataLayer_zza.zzNc);
        }

        public int hashCode() {
            return Arrays.hashCode(new Integer[]{Integer.valueOf(this.zzvs.hashCode()), Integer.valueOf(this.zzNc.hashCode())});
        }

        public String toString() {
            return "Key: " + this.zzvs + " value: " + this.zzNc.toString();
        }
    }

    interface zzb {
        void zzQ(Map<String, Object> map);
    }

    static {
        OBJECT_NOT_PRESENT = new Object();
        zzbir = "gtm.lifetime".toString().split("\\.");
        zzbis = Pattern.compile("(\\d+)\\s*([smhd]?)");
    }

    DataLayer() {
        this(new C10261());
    }

    DataLayer(zzc persistentStore) {
        this.zzbix = persistentStore;
        this.zzbit = new ConcurrentHashMap();
        this.zzbiu = new HashMap();
        this.zzbiv = new ReentrantLock();
        this.zzbiw = new LinkedList();
        this.zzbiy = new CountDownLatch(1);
        zzGn();
    }

    public static List<Object> listOf(Object... objects) {
        List<Object> arrayList = new ArrayList();
        for (Object add : objects) {
            arrayList.add(add);
        }
        return arrayList;
    }

    public static Map<String, Object> mapOf(Object... objects) {
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("expected even number of key-value pairs");
        }
        Map<String, Object> hashMap = new HashMap();
        int i = 0;
        while (i < objects.length) {
            if (objects[i] instanceof String) {
                hashMap.put((String) objects[i], objects[i + 1]);
                i += 2;
            } else {
                throw new IllegalArgumentException("key is not a string: " + objects[i]);
            }
        }
        return hashMap;
    }

    private void zzGn() {
        this.zzbix.zza(new C10272(this));
    }

    private void zzGo() {
        int i = 0;
        while (true) {
            Map map = (Map) this.zzbiw.poll();
            if (map != null) {
                zzX(map);
                int i2 = i + 1;
                if (i2 > ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH) {
                    break;
                }
                i = i2;
            } else {
                return;
            }
        }
        this.zzbiw.clear();
        throw new RuntimeException("Seems like an infinite loop of pushing to the data layer");
    }

    private void zzS(Map<String, Object> map) {
        this.zzbiv.lock();
        try {
            this.zzbiw.offer(map);
            if (this.zzbiv.getHoldCount() == 1) {
                zzGo();
            }
            zzT(map);
        } finally {
            this.zzbiv.unlock();
        }
    }

    private void zzT(Map<String, Object> map) {
        Long zzU = zzU(map);
        if (zzU != null) {
            List zzW = zzW(map);
            zzW.remove("gtm.lifetime");
            this.zzbix.zza(zzW, zzU.longValue());
        }
    }

    private Long zzU(Map<String, Object> map) {
        Object zzV = zzV(map);
        return zzV == null ? null : zzfY(zzV.toString());
    }

    private Object zzV(Map<String, Object> map) {
        String[] strArr = zzbir;
        int length = strArr.length;
        int i = 0;
        Object obj = map;
        while (i < length) {
            Object obj2 = strArr[i];
            if (!(obj instanceof Map)) {
                return null;
            }
            i++;
            obj = ((Map) obj).get(obj2);
        }
        return obj;
    }

    private List<zza> zzW(Map<String, Object> map) {
        Object arrayList = new ArrayList();
        zza(map, Stomp.EMPTY, arrayList);
        return arrayList;
    }

    private void zzX(Map<String, Object> map) {
        synchronized (this.zzbiu) {
            for (String str : map.keySet()) {
                zzd(zzn(str, map.get(str)), this.zzbiu);
            }
        }
        zzY(map);
    }

    private void zzY(Map<String, Object> map) {
        for (zzb zzQ : this.zzbit.keySet()) {
            zzQ.zzQ(map);
        }
    }

    private void zza(Map<String, Object> map, String str, Collection<zza> collection) {
        for (Entry entry : map.entrySet()) {
            String str2 = str + (str.length() == 0 ? Stomp.EMPTY : ActiveMQDestination.PATH_SEPERATOR) + ((String) entry.getKey());
            if (entry.getValue() instanceof Map) {
                zza((Map) entry.getValue(), str2, collection);
            } else if (!str2.equals("gtm.lifetime")) {
                collection.add(new zza(str2, entry.getValue()));
            }
        }
    }

    static Long zzfY(String str) {
        Matcher matcher = zzbis.matcher(str);
        if (matcher.matches()) {
            long parseLong;
            try {
                parseLong = Long.parseLong(matcher.group(1));
            } catch (NumberFormatException e) {
                zzbg.zzaK("illegal number in _lifetime value: " + str);
                parseLong = 0;
            }
            if (parseLong <= 0) {
                zzbg.zzaJ("non-positive _lifetime: " + str);
                return null;
            }
            String group = matcher.group(2);
            if (group.length() == 0) {
                return Long.valueOf(parseLong);
            }
            switch (group.charAt(0)) {
                case ActiveMQPrefetchPolicy.DEFAULT_INPUT_STREAM_PREFETCH /*100*/:
                    return Long.valueOf((((parseLong * 1000) * 60) * 60) * 24);
                case Service.X400_SND /*104*/:
                    return Long.valueOf(((parseLong * 1000) * 60) * 60);
                case Service.POP_2 /*109*/:
                    return Long.valueOf((parseLong * 1000) * 60);
                case Service.SFTP /*115*/:
                    return Long.valueOf(parseLong * 1000);
                default:
                    zzbg.zzaK("unknown units in _lifetime: " + str);
                    return null;
            }
        }
        zzbg.zzaJ("unknown _lifetime: " + str);
        return null;
    }

    public Object get(String key) {
        synchronized (this.zzbiu) {
            Map map = this.zzbiu;
            String[] split = key.split("\\.");
            int length = split.length;
            Object obj = map;
            int i = 0;
            while (i < length) {
                Object obj2 = split[i];
                if (obj instanceof Map) {
                    obj2 = ((Map) obj).get(obj2);
                    if (obj2 == null) {
                        return null;
                    }
                    i++;
                    obj = obj2;
                } else {
                    return null;
                }
            }
            return obj;
        }
    }

    public void push(String key, Object value) {
        push(zzn(key, value));
    }

    public void push(Map<String, Object> update) {
        try {
            this.zzbiy.await();
        } catch (InterruptedException e) {
            zzbg.zzaK("DataLayer.push: unexpected InterruptedException");
        }
        zzS(update);
    }

    public void pushEvent(String eventName, Map<String, Object> update) {
        Map hashMap = new HashMap(update);
        hashMap.put(EVENT_KEY, eventName);
        push(hashMap);
    }

    public String toString() {
        String stringBuilder;
        synchronized (this.zzbiu) {
            StringBuilder stringBuilder2 = new StringBuilder();
            for (Entry entry : this.zzbiu.entrySet()) {
                stringBuilder2.append(String.format("{\n\tKey: %s\n\tValue: %s\n}\n", new Object[]{entry.getKey(), entry.getValue()}));
            }
            stringBuilder = stringBuilder2.toString();
        }
        return stringBuilder;
    }

    void zza(zzb com_google_android_gms_tagmanager_DataLayer_zzb) {
        this.zzbit.put(com_google_android_gms_tagmanager_DataLayer_zzb, Integer.valueOf(0));
    }

    void zzb(List<Object> list, List<Object> list2) {
        while (list2.size() < list.size()) {
            list2.add(null);
        }
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (obj instanceof List) {
                if (!(list2.get(i) instanceof List)) {
                    list2.set(i, new ArrayList());
                }
                zzb((List) obj, (List) list2.get(i));
            } else if (obj instanceof Map) {
                if (!(list2.get(i) instanceof Map)) {
                    list2.set(i, new HashMap());
                }
                zzd((Map) obj, (Map) list2.get(i));
            } else if (obj != OBJECT_NOT_PRESENT) {
                list2.set(i, obj);
            }
        }
    }

    void zzd(Map<String, Object> map, Map<String, Object> map2) {
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            if (obj instanceof List) {
                if (!(map2.get(str) instanceof List)) {
                    map2.put(str, new ArrayList());
                }
                zzb((List) obj, (List) map2.get(str));
            } else if (obj instanceof Map) {
                if (!(map2.get(str) instanceof Map)) {
                    map2.put(str, new HashMap());
                }
                zzd((Map) obj, (Map) map2.get(str));
            } else {
                map2.put(str, obj);
            }
        }
    }

    void zzfX(String str) {
        push(str, null);
        this.zzbix.zzfZ(str);
    }

    Map<String, Object> zzn(String str, Object obj) {
        Map hashMap = new HashMap();
        String[] split = str.toString().split("\\.");
        int i = 0;
        Map map = hashMap;
        while (i < split.length - 1) {
            HashMap hashMap2 = new HashMap();
            map.put(split[i], hashMap2);
            i++;
            Object obj2 = hashMap2;
        }
        map.put(split[split.length - 1], obj);
        return hashMap;
    }
}
