package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import com.admarvel.android.util.AdMarvelThreadExecutorService;
import com.admarvel.android.util.GoogleAdvertisingIdClient;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint({"NewApi"})
/* renamed from: com.admarvel.android.ads.a */
class AdMarvelActivityLifecycleCallbacksListener implements ActivityLifecycleCallbacks {
    private static volatile AdMarvelActivityLifecycleCallbacksListener f500c;
    private Map<String, WeakReference<AdMarvelActivityLifecycleCallbacksListener>> f501a;
    private List<String> f502b;

    /* renamed from: com.admarvel.android.ads.a.a */
    interface AdMarvelActivityLifecycleCallbacksListener {
        void onInternalDestroy(Activity activity);

        void onInternalPause(Activity activity);

        void onInternalResume(Activity activity);
    }

    /* renamed from: com.admarvel.android.ads.a.1 */
    class AdMarvelActivityLifecycleCallbacksListener implements Runnable {
        final /* synthetic */ Activity f498a;
        final /* synthetic */ AdMarvelActivityLifecycleCallbacksListener f499b;

        AdMarvelActivityLifecycleCallbacksListener(AdMarvelActivityLifecycleCallbacksListener adMarvelActivityLifecycleCallbacksListener, Activity activity) {
            this.f499b = adMarvelActivityLifecycleCallbacksListener;
            this.f498a = activity;
        }

        public void run() {
            if (Version.getAndroidSDKVersion() > 8) {
                GoogleAdvertisingIdClient.m615a();
                GoogleAdvertisingIdClient.m616c(this.f498a);
            }
        }
    }

    static {
        f500c = null;
    }

    private AdMarvelActivityLifecycleCallbacksListener() {
        this.f501a = new HashMap();
        this.f502b = new ArrayList();
    }

    public static AdMarvelActivityLifecycleCallbacksListener m248a() {
        if (f500c == null) {
            synchronized (AdMarvelActivityLifecycleCallbacksListener.class) {
                if (f500c == null) {
                    f500c = new AdMarvelActivityLifecycleCallbacksListener();
                }
            }
        }
        return f500c;
    }

    public void m249a(String str) {
        if (str != null && str.length() > 0) {
            this.f502b.add(str);
        }
    }

    public void m250a(String str, AdMarvelActivityLifecycleCallbacksListener adMarvelActivityLifecycleCallbacksListener) {
        if (this.f501a != null && str != null && str.length() > 0) {
            this.f501a.put(str, new WeakReference(adMarvelActivityLifecycleCallbacksListener));
        }
    }

    public void m251b(String str) {
        if (str != null) {
            try {
                if (this.f501a != null && this.f501a.containsKey(str)) {
                    this.f501a.remove(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityDestroyed(Activity activity) {
        if (this.f501a != null && this.f501a.size() > 0) {
            for (WeakReference weakReference : this.f501a.values()) {
                if (!(weakReference == null || weakReference.get() == null)) {
                    ((AdMarvelActivityLifecycleCallbacksListener) weakReference.get()).onInternalDestroy(activity);
                }
            }
        }
        if (this.f502b.size() > 0) {
            for (String str : this.f502b) {
                if (!(str == null || this.f501a == null || !this.f501a.containsKey(str))) {
                    this.f501a.remove(str);
                }
            }
            this.f502b.clear();
        }
    }

    public void onActivityPaused(Activity activity) {
        AdMarvelUtils.pause(activity);
        if (this.f501a != null && this.f501a.size() > 0) {
            for (WeakReference weakReference : this.f501a.values()) {
                if (!(weakReference == null || weakReference.get() == null)) {
                    ((AdMarvelActivityLifecycleCallbacksListener) weakReference.get()).onInternalPause(activity);
                }
            }
        }
    }

    public void onActivityResumed(Activity activity) {
        AdMarvelUtils.resume(activity);
        if (this.f501a != null && this.f501a.size() > 0) {
            for (WeakReference weakReference : this.f501a.values()) {
                if (!(weakReference == null || weakReference.get() == null)) {
                    ((AdMarvelActivityLifecycleCallbacksListener) weakReference.get()).onInternalResume(activity);
                }
            }
        }
        AdMarvelThreadExecutorService.m597a().m598b().execute(new AdMarvelActivityLifecycleCallbacksListener(this, activity));
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }
}
