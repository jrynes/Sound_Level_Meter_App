package com.google.android.gms.tagmanager;

import android.annotation.TargetApi;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RawRes;
import android.support.annotation.RequiresPermission;
import com.google.android.gms.common.api.PendingResult;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TagManager {
    private static TagManager zzblm;
    private final Context mContext;
    private final DataLayer zzbhN;
    private final zzs zzbkh;
    private final zza zzblj;
    private final zzct zzblk;
    private final ConcurrentMap<zzo, Boolean> zzbll;

    /* renamed from: com.google.android.gms.tagmanager.TagManager.1 */
    class C10291 implements zzb {
        final /* synthetic */ TagManager zzbln;

        C10291(TagManager tagManager) {
            this.zzbln = tagManager;
        }

        public void zzQ(Map<String, Object> map) {
            Object obj = map.get(DataLayer.EVENT_KEY);
            if (obj != null) {
                this.zzbln.zzgp(obj.toString());
            }
        }
    }

    public interface zza {
        zzp zza(Context context, TagManager tagManager, Looper looper, String str, int i, zzs com_google_android_gms_tagmanager_zzs);
    }

    /* renamed from: com.google.android.gms.tagmanager.TagManager.2 */
    static class C10302 implements zza {
        C10302() {
        }

        public zzp zza(Context context, TagManager tagManager, Looper looper, String str, int i, zzs com_google_android_gms_tagmanager_zzs) {
            return new zzp(context, tagManager, looper, str, i, com_google_android_gms_tagmanager_zzs);
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.TagManager.3 */
    class C10313 implements ComponentCallbacks2 {
        final /* synthetic */ TagManager zzbln;

        C10313(TagManager tagManager) {
            this.zzbln = tagManager;
        }

        public void onConfigurationChanged(Configuration configuration) {
        }

        public void onLowMemory() {
        }

        public void onTrimMemory(int i) {
            if (i == 20) {
                this.zzbln.dispatch();
            }
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.TagManager.4 */
    static /* synthetic */ class C10324 {
        static final /* synthetic */ int[] zzblo;

        static {
            zzblo = new int[zza.values().length];
            try {
                zzblo[zza.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                zzblo[zza.CONTAINER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                zzblo[zza.CONTAINER_DEBUG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    TagManager(Context context, zza containerHolderLoaderProvider, DataLayer dataLayer, zzct serviceManager) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }
        this.mContext = context.getApplicationContext();
        this.zzblk = serviceManager;
        this.zzblj = containerHolderLoaderProvider;
        this.zzbll = new ConcurrentHashMap();
        this.zzbhN = dataLayer;
        this.zzbhN.zza(new C10291(this));
        this.zzbhN.zza(new zzd(this.mContext));
        this.zzbkh = new zzs();
        zzHt();
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public static TagManager getInstance(Context context) {
        TagManager tagManager;
        synchronized (TagManager.class) {
            if (zzblm == null) {
                if (context == null) {
                    zzbg.m1675e("TagManager.getInstance requires non-null context.");
                    throw new NullPointerException();
                }
                zzblm = new TagManager(context, new C10302(), new DataLayer(new zzw(context)), zzcu.zzHo());
            }
            tagManager = zzblm;
        }
        return tagManager;
    }

    @TargetApi(14)
    private void zzHt() {
        if (VERSION.SDK_INT >= 14) {
            this.mContext.registerComponentCallbacks(new C10313(this));
        }
    }

    private void zzgp(String str) {
        for (zzo zzfR : this.zzbll.keySet()) {
            zzfR.zzfR(str);
        }
    }

    public void dispatch() {
        this.zzblk.dispatch();
    }

    public DataLayer getDataLayer() {
        return this.zzbhN;
    }

    public PendingResult<ContainerHolder> loadContainerDefaultOnly(String containerId, @RawRes int defaultContainerResourceId) {
        PendingResult zza = this.zzblj.zza(this.mContext, this, null, containerId, defaultContainerResourceId, this.zzbkh);
        zza.zzGg();
        return zza;
    }

    public PendingResult<ContainerHolder> loadContainerDefaultOnly(String containerId, @RawRes int defaultContainerResourceId, Handler handler) {
        PendingResult zza = this.zzblj.zza(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.zzbkh);
        zza.zzGg();
        return zza;
    }

    public PendingResult<ContainerHolder> loadContainerPreferFresh(String containerId, @RawRes int defaultContainerResourceId) {
        PendingResult zza = this.zzblj.zza(this.mContext, this, null, containerId, defaultContainerResourceId, this.zzbkh);
        zza.zzGi();
        return zza;
    }

    public PendingResult<ContainerHolder> loadContainerPreferFresh(String containerId, @RawRes int defaultContainerResourceId, Handler handler) {
        PendingResult zza = this.zzblj.zza(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.zzbkh);
        zza.zzGi();
        return zza;
    }

    public PendingResult<ContainerHolder> loadContainerPreferNonDefault(String containerId, @RawRes int defaultContainerResourceId) {
        PendingResult zza = this.zzblj.zza(this.mContext, this, null, containerId, defaultContainerResourceId, this.zzbkh);
        zza.zzGh();
        return zza;
    }

    public PendingResult<ContainerHolder> loadContainerPreferNonDefault(String containerId, @RawRes int defaultContainerResourceId, Handler handler) {
        PendingResult zza = this.zzblj.zza(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.zzbkh);
        zza.zzGh();
        return zza;
    }

    public void setVerboseLoggingEnabled(boolean enableVerboseLogging) {
        zzbg.setLogLevel(enableVerboseLogging ? 2 : 5);
    }

    public void zza(zzo com_google_android_gms_tagmanager_zzo) {
        this.zzbll.put(com_google_android_gms_tagmanager_zzo, Boolean.valueOf(true));
    }

    public boolean zzb(zzo com_google_android_gms_tagmanager_zzo) {
        return this.zzbll.remove(com_google_android_gms_tagmanager_zzo) != null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    synchronized boolean zzp(android.net.Uri r6) {
        /*
        r5 = this;
        monitor-enter(r5);
        r1 = com.google.android.gms.tagmanager.zzcb.zzGU();	 Catch:{ all -> 0x0049 }
        r0 = r1.zzp(r6);	 Catch:{ all -> 0x0049 }
        if (r0 == 0) goto L_0x0085;
    L_0x000b:
        r2 = r1.getContainerId();	 Catch:{ all -> 0x0049 }
        r0 = com.google.android.gms.tagmanager.TagManager.C10324.zzblo;	 Catch:{ all -> 0x0049 }
        r3 = r1.zzGV();	 Catch:{ all -> 0x0049 }
        r3 = r3.ordinal();	 Catch:{ all -> 0x0049 }
        r0 = r0[r3];	 Catch:{ all -> 0x0049 }
        switch(r0) {
            case 1: goto L_0x0021;
            case 2: goto L_0x004c;
            case 3: goto L_0x004c;
            default: goto L_0x001e;
        };
    L_0x001e:
        r0 = 1;
    L_0x001f:
        monitor-exit(r5);
        return r0;
    L_0x0021:
        r0 = r5.zzbll;	 Catch:{ all -> 0x0049 }
        r0 = r0.keySet();	 Catch:{ all -> 0x0049 }
        r1 = r0.iterator();	 Catch:{ all -> 0x0049 }
    L_0x002b:
        r0 = r1.hasNext();	 Catch:{ all -> 0x0049 }
        if (r0 == 0) goto L_0x001e;
    L_0x0031:
        r0 = r1.next();	 Catch:{ all -> 0x0049 }
        r0 = (com.google.android.gms.tagmanager.zzo) r0;	 Catch:{ all -> 0x0049 }
        r3 = r0.getContainerId();	 Catch:{ all -> 0x0049 }
        r3 = r3.equals(r2);	 Catch:{ all -> 0x0049 }
        if (r3 == 0) goto L_0x002b;
    L_0x0041:
        r3 = 0;
        r0.zzfT(r3);	 Catch:{ all -> 0x0049 }
        r0.refresh();	 Catch:{ all -> 0x0049 }
        goto L_0x002b;
    L_0x0049:
        r0 = move-exception;
        monitor-exit(r5);
        throw r0;
    L_0x004c:
        r0 = r5.zzbll;	 Catch:{ all -> 0x0049 }
        r0 = r0.keySet();	 Catch:{ all -> 0x0049 }
        r3 = r0.iterator();	 Catch:{ all -> 0x0049 }
    L_0x0056:
        r0 = r3.hasNext();	 Catch:{ all -> 0x0049 }
        if (r0 == 0) goto L_0x001e;
    L_0x005c:
        r0 = r3.next();	 Catch:{ all -> 0x0049 }
        r0 = (com.google.android.gms.tagmanager.zzo) r0;	 Catch:{ all -> 0x0049 }
        r4 = r0.getContainerId();	 Catch:{ all -> 0x0049 }
        r4 = r4.equals(r2);	 Catch:{ all -> 0x0049 }
        if (r4 == 0) goto L_0x0077;
    L_0x006c:
        r4 = r1.zzGW();	 Catch:{ all -> 0x0049 }
        r0.zzfT(r4);	 Catch:{ all -> 0x0049 }
        r0.refresh();	 Catch:{ all -> 0x0049 }
        goto L_0x0056;
    L_0x0077:
        r4 = r0.zzGd();	 Catch:{ all -> 0x0049 }
        if (r4 == 0) goto L_0x0056;
    L_0x007d:
        r4 = 0;
        r0.zzfT(r4);	 Catch:{ all -> 0x0049 }
        r0.refresh();	 Catch:{ all -> 0x0049 }
        goto L_0x0056;
    L_0x0085:
        r0 = 0;
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.TagManager.zzp(android.net.Uri):boolean");
    }
}
