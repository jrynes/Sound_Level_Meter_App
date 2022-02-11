package com.amazon.device.associates;

import android.content.Context;
import android.os.AsyncTask;
import com.rabbitmq.client.AMQP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.amazon.device.associates.p */
class PopoverCacheManager {
    private static volatile PopoverCacheManager f1373a;
    private static final StringBuilder f1374b;

    /* renamed from: com.amazon.device.associates.p.1 */
    static class PopoverCacheManager extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ String[] f1367a;

        PopoverCacheManager(String[] strArr) {
            this.f1367a = strArr;
        }

        protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return m996a((Void[]) objArr);
        }

        protected Void m996a(Void... voidArr) {
            PopoverCacheManager.m1011b(bp.m899a(), this.f1367a);
            return null;
        }
    }

    /* renamed from: com.amazon.device.associates.p.2 */
    static class PopoverCacheManager extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ String f1368a;
        final /* synthetic */ boolean[] f1369b;

        PopoverCacheManager(String str, boolean[] zArr) {
            this.f1368a = str;
            this.f1369b = zArr;
        }

        protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return m997a((Void[]) objArr);
        }

        protected Void m997a(Void... voidArr) {
            try {
                if (!(this.f1368a == null || this.f1368a.trim().equals(Stomp.EMPTY))) {
                    PopoverCacheManager.m1012b(PopoverCacheManager.f1373a, new String[]{this.f1368a}, false);
                }
            } catch (Exception e) {
                Log.m1017b("PopoverCacheManager", "Exception in making async service call", e);
                this.f1369b[0] = true;
            }
            return null;
        }
    }

    /* renamed from: com.amazon.device.associates.p.a */
    static final class PopoverCacheManager implements Serializable {
        private final Map<String, bs> f1370a;
        private Date f1371b;
        private long f1372c;

        private PopoverCacheManager() {
            this.f1370a = new HashMap();
            this.f1371b = new Date();
            this.f1372c = 86400000;
        }

        public synchronized bs m999a(String str) {
            return (bs) this.f1370a.get(str.toUpperCase(Locale.ENGLISH));
        }

        public synchronized void m1000a(String str, bs bsVar) {
            this.f1370a.put(str.toUpperCase(Locale.ENGLISH), bsVar);
        }

        public boolean m1001a() {
            if (new Date().getTime() - this.f1371b.getTime() < this.f1372c) {
                return false;
            }
            this.f1370a.clear();
            Log.m1018c("PopoverCacheManager", "Expiring popover data.");
            return true;
        }
    }

    PopoverCacheManager() {
    }

    static {
        f1373a = null;
        f1374b = new StringBuilder();
    }

    public static void m1008a(String[] strArr) {
        new PopoverCacheManager(strArr).execute(new Void[0]);
    }

    private static void m1011b(Context context, String[] strArr) {
        if (strArr != null && strArr.length > 20) {
            Log.m1020d("PopoverCacheManager", "The asin list for prefetching has exceeded limit. " + strArr.length + ", trimming it down to first 20. ");
            Object obj = new String[20];
            System.arraycopy(strArr, 0, obj, 0, 20);
            strArr = obj;
        }
        String str = (context.getCacheDir() != null ? context.getCacheDir().getAbsolutePath() + ReadOnlyContext.SEPARATOR : ReadOnlyContext.SEPARATOR) + "POD.db";
        try {
            int i;
            PopoverCacheManager b = PopoverCacheManager.m1010b(str);
            boolean a = b.m1001a();
            if (PopoverCacheManager.m1012b(b, strArr, true) || a) {
                i = 1;
            } else {
                i = 0;
            }
            if (i != 0) {
                PopoverCacheManager.m1007a(str, b);
            }
            f1373a = b;
            if (f1373a == null) {
                Log.m1013a("PopoverCacheManager", "Failed to initialize cache, so constructing it using default constructor");
                f1373a = new PopoverCacheManager();
            }
        } catch (Exception e) {
            Log.m1014a("PopoverCacheManager", "Error initializing cache manager", e);
            Log.m1018c("PopoverCacheManager", "As cache file must have been corrupted, deleting it.");
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            if (f1373a == null) {
                Log.m1013a("PopoverCacheManager", "Failed to initialize cache, so constructing it using default constructor");
                f1373a = new PopoverCacheManager();
            }
        } catch (Throwable th) {
            if (f1373a == null) {
                Log.m1013a("PopoverCacheManager", "Failed to initialize cache, so constructing it using default constructor");
                f1373a = new PopoverCacheManager();
            }
        }
    }

    private static boolean m1012b(PopoverCacheManager popoverCacheManager, String[] strArr, boolean z) throws Exception {
        int i;
        List arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            if (!(str == null || str.trim().equals(Stomp.EMPTY) || popoverCacheManager.m999a(str) != null)) {
                arrayList.add(str);
            }
        }
        if (arrayList.size() <= 0) {
            return false;
        }
        Log.m1018c("PopoverCacheManager", "Quering service to get popover data for: " + arrayList);
        i = 0;
        boolean z2 = false;
        while (i < arrayList.size()) {
            List subList = arrayList.subList(i, i + 5 < arrayList.size() ? i + 5 : arrayList.size());
            au auVar = new au("http://ws-na.amazon-adsystem.com/widgets/q");
            auVar.m797a("TemplateId", "MobileAssociates");
            auVar.m797a("ServiceVersion", "20070822");
            auVar.m797a("MarketPlace", "US");
            auVar.m797a("Operation", "GetDetails");
            auVar.m797a("InstanceId", "1367560449185");
            auVar.m797a("ItemId", PopoverCacheManager.m1004a(subList));
            try {
                auVar.m796a(RequestMethod.GET);
                if (auVar.m798b() == AMQP.REPLY_SUCCESS) {
                    PopoverCacheManager.m1006a(popoverCacheManager, auVar.m795a());
                    z2 = true;
                } else {
                    Log.m1013a("PopoverCacheManager", "Widget Server didn't return data, error code: " + auVar.m798b());
                    new MetricsRecorderCall("widgetServerCallFailed").m973d();
                }
            } catch (Exception e) {
                Log.m1019c("PopoverCacheManager", "Error loading Popover Preview data for " + subList, e);
                if (!z) {
                    throw e;
                }
            }
            i += 5;
        }
        popoverCacheManager.f1371b = new Date();
        return z2;
    }

    private static void m1006a(PopoverCacheManager popoverCacheManager, String str) throws JSONException {
        if (str.length() >= "asinDetails".length() + 1) {
            JSONArray jSONArray = new JSONObject(str.substring("asinDetails".length() + 1)).getJSONArray("results");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                popoverCacheManager.m1000a(jSONObject.getString("ASIN"), new bs(jSONObject.getString("ASIN"), jSONObject.getString("LargeImageUrl"), jSONObject.getString("Title"), jSONObject.getString("Price"), jSONObject.getString("Rating"), jSONObject.getString("TotalReviews"), jSONObject.getString("DetailPageURL"), jSONObject.getString("category")));
            }
        }
    }

    private static String m1004a(List<String> list) {
        f1374b.setLength(0);
        for (String toUpperCase : list) {
            f1374b.append(toUpperCase.toUpperCase(Locale.ENGLISH)).append(Stomp.COMMA);
        }
        return f1374b.length() > 1 ? f1374b.substring(0, f1374b.length() - 1) : Stomp.EMPTY;
    }

    private static void m1007a(String str, PopoverCacheManager popoverCacheManager) throws FileNotFoundException, IOException {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        try {
            objectOutputStream.writeObject(popoverCacheManager);
        } finally {
            objectOutputStream.close();
        }
    }

    private static PopoverCacheManager m1010b(String str) throws StreamCorruptedException, IOException, ClassNotFoundException {
        File file = new File(str);
        if (file.exists()) {
            Log.m1018c("PopoverCacheManager", "Reading cache file....");
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            try {
                PopoverCacheManager popoverCacheManager = (PopoverCacheManager) objectInputStream.readObject();
                if (popoverCacheManager != null) {
                    return popoverCacheManager;
                }
                return new PopoverCacheManager();
            } finally {
                objectInputStream.close();
            }
        } else {
            Log.m1018c("PopoverCacheManager", "Cache file not found, creating object manually.");
            return new PopoverCacheManager();
        }
    }

    public static bs m1002a(String str) throws Exception {
        boolean[] zArr = new boolean[]{false};
        if (f1373a == null || str == null) {
            return null;
        }
        if (f1373a.m999a(str) == null) {
            try {
                AsyncTask popoverCacheManager = new PopoverCacheManager(str, zArr);
                popoverCacheManager.execute(new Void[0]);
                popoverCacheManager.get();
            } catch (Throwable e) {
                Log.m1014a("PopoverCacheManager", "Fetching Product info from Service for " + str + " failed", e);
                throw new Exception("Failed to load product info from service call.", e);
            }
        }
        if (!zArr[0]) {
            return f1373a.m999a(str);
        }
        Log.m1013a("PopoverCacheManager", "Service call to widget server has failed.");
        throw new Exception("Service call to widget server failed.");
    }
}
