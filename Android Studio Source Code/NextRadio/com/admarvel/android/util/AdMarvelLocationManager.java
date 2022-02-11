package com.admarvel.android.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.admarvel.android.ads.AdMarvelInternalWebView;
import com.admarvel.android.ads.Utils;
import java.lang.ref.WeakReference;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.admarvel.android.util.d */
public class AdMarvelLocationManager {
    private static AdMarvelLocationManager f982a;
    private static AdMarvelLocationManager f983b;
    private static AdMarvelLocationManager f984c;
    private String f985d;
    private String f986e;
    private WeakReference<AdMarvelInternalWebView> f987f;

    /* renamed from: com.admarvel.android.util.d.a */
    class AdMarvelLocationManager implements LocationListener {
        LocationManager f979a;
        final /* synthetic */ AdMarvelLocationManager f980b;
        private boolean f981c;

        public AdMarvelLocationManager(AdMarvelLocationManager adMarvelLocationManager, String str, LocationManager locationManager) {
            this.f980b = adMarvelLocationManager;
            this.f981c = false;
            this.f979a = locationManager;
            adMarvelLocationManager.f985d = str;
        }

        public void m565a() {
            if (!this.f981c) {
                this.f979a.requestLocationUpdates(this.f980b.f985d, 0, 0.0f, this);
                this.f981c = true;
            }
        }

        public void onLocationChanged(Location location) {
            AdMarvelInternalWebView adMarvelInternalWebView = this.f980b.f987f != null ? (AdMarvelInternalWebView) this.f980b.f987f.get() : null;
            if (adMarvelInternalWebView != null && adMarvelInternalWebView.getHandler() != null && this.f980b.f986e != null && location != null && this.f980b.f987f != null) {
                adMarvelInternalWebView.m315e(this.f980b.f986e + "(" + location.getLatitude() + Stomp.COMMA + location.getLongitude() + Stomp.COMMA + location.getAccuracy() + ")");
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    static {
        f982a = null;
        f983b = null;
        f984c = null;
    }

    private AdMarvelLocationManager() {
    }

    public static AdMarvelLocationManager m566a() {
        if (f982a == null) {
            f982a = new AdMarvelLocationManager();
        }
        return f982a;
    }

    private boolean m569a(Context context, String str) {
        try {
            List allProviders = ((LocationManager) context.getSystemService("location")).getAllProviders();
            for (int i = 0; i < allProviders.size(); i++) {
                if (str.equals((String) allProviders.get(i))) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public Location m572a(AdMarvelInternalWebView adMarvelInternalWebView) {
        Location location = null;
        if (!Utils.m202c(adMarvelInternalWebView.getContext(), "android.permission.ACCESS_COARSE_LOCATION") && !Utils.m202c(adMarvelInternalWebView.getContext(), "android.permission.ACCESS_FINE_LOCATION")) {
            return null;
        }
        LocationManager locationManager = (LocationManager) adMarvelInternalWebView.getContext().getSystemService("location");
        List providers = locationManager.getProviders(true);
        for (int size = providers.size() - 1; size >= 0; size--) {
            location = locationManager.getLastKnownLocation((String) providers.get(size));
            if (location != null) {
                return location;
            }
        }
        return location;
    }

    public void m573a(Context context) {
        if (context != null) {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            if (f984c != null) {
                locationManager.removeUpdates(f984c);
            }
            if (f983b != null) {
                locationManager.removeUpdates(f983b);
            }
            f984c = null;
            f983b = null;
        }
    }

    public void m574a(AdMarvelInternalWebView adMarvelInternalWebView, String str) {
        if (Utils.m202c(adMarvelInternalWebView.getContext(), "android.permission.ACCESS_COARSE_LOCATION") || Utils.m202c(adMarvelInternalWebView.getContext(), "android.permission.ACCESS_FINE_LOCATION")) {
            LocationManager locationManager = (LocationManager) adMarvelInternalWebView.getContext().getSystemService("location");
            this.f987f = new WeakReference(adMarvelInternalWebView);
            this.f986e = str;
            try {
                if (m569a(adMarvelInternalWebView.getContext(), "gps") && locationManager.getProvider("gps") != null) {
                    f983b = new AdMarvelLocationManager(this, "gps", locationManager);
                }
            } catch (Exception e) {
            }
            try {
                if (m569a(adMarvelInternalWebView.getContext(), "network") && locationManager.getProvider("network") != null) {
                    f984c = new AdMarvelLocationManager(this, "network", locationManager);
                }
            } catch (Exception e2) {
            }
            if (f984c != null) {
                f984c.m565a();
            }
            if (f983b != null) {
                f983b.m565a();
            }
        }
    }
}
