package com.onelouder.adlib;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

class LocationUtils {
    private static final String TAG = "LocationUtils";
    private static final Object csLock;
    private static LocationListener locationListener;
    private static WeakReference<Context> mContextRef;
    private static GeoLocation mLocation;

    /* renamed from: com.onelouder.adlib.LocationUtils.1 */
    static class C12981 implements LocationListener {
        Runnable geocoderRunnable;

        /* renamed from: com.onelouder.adlib.LocationUtils.1.1 */
        class C12971 implements Runnable {
            C12971() {
            }

            public void run() {
                try {
                    if (LocationUtils.mLocation != null) {
                        Geocoder myLocation = new Geocoder((Context) LocationUtils.mContextRef.get(), Locale.getDefault());
                        List<Address> addresslist = null;
                        try {
                            addresslist = myLocation.getFromLocation(Double.valueOf(Double.parseDouble(LocationUtils.mLocation.getLatitude())).doubleValue(), Double.valueOf(Double.parseDouble(LocationUtils.mLocation.getLongitude())).doubleValue(), 1);
                        } catch (Exception e) {
                        }
                        if (addresslist != null) {
                            for (Address addr : addresslist) {
                                if (!(addr == null || addr.getPostalCode() == null)) {
                                    Diagnostics.m1951d(LocationUtils.TAG, "postal code=" + addr.getPostalCode());
                                    if (addr.getPostalCode().length() > 0) {
                                        if (addr.getCountryCode().toLowerCase().equals("us")) {
                                            LocationUtils.mLocation.setPostalcode(addr.getPostalCode());
                                            return;
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                } catch (Throwable e2) {
                    Diagnostics.m1958w(LocationUtils.TAG, e2);
                }
            }
        }

        C12981() {
            this.geocoderRunnable = new C12971();
        }

        public void onLocationChanged(Location location) {
            if (LocationUtils.mContextRef != null && LocationUtils.mContextRef.get() != null) {
                Diagnostics.m1955i(LocationUtils.TAG, "onLocationChanged");
                if (location != null) {
                    Diagnostics.m1955i(LocationUtils.TAG, "location=" + location.getLatitude() + ", " + location.getLongitude());
                    ((LocationManager) ((Context) LocationUtils.mContextRef.get()).getSystemService("location")).removeUpdates(this);
                    LocationUtils.mLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
                    new Thread(this.geocoderRunnable).start();
                }
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    /* renamed from: com.onelouder.adlib.LocationUtils.2 */
    static class C12992 implements Runnable {
        final /* synthetic */ LocationManager val$lm;

        C12992(LocationManager locationManager) {
            this.val$lm = locationManager;
        }

        public void run() {
            synchronized (LocationUtils.locationListener) {
                Diagnostics.m1951d(LocationUtils.TAG, "requesting new Location");
                this.val$lm.requestLocationUpdates("network", 0, 0.0f, LocationUtils.locationListener, Looper.getMainLooper());
            }
        }
    }

    LocationUtils() {
    }

    static {
        csLock = new Object();
        locationListener = new C12981();
    }

    static void requestGeoLocation(Context context) {
        Diagnostics.m1955i(TAG, "requestGeoLocation");
        if (context != null) {
            mContextRef = new WeakReference(context.getApplicationContext());
        }
        try {
            LocationManager lm = (LocationManager) context.getSystemService("location");
            if (lm != null) {
                Location location;
                if (checkFineLocationPermission(context) && lm.isProviderEnabled("gps")) {
                    location = lm.getLastKnownLocation("gps");
                    if (location != null) {
                        Diagnostics.m1951d(TAG, "using last known GPS_PROVIDER Location");
                        locationListener.onLocationChanged(location);
                        return;
                    }
                }
                if (checkCourseLocationPermission(context) && lm.isProviderEnabled("network")) {
                    location = lm.getLastKnownLocation("network");
                    if (location != null) {
                        Diagnostics.m1951d(TAG, "using last known NETWORK_PROVIDER Location");
                        locationListener.onLocationChanged(location);
                        return;
                    }
                }
                if (checkCourseLocationPermission(context) && lm.isProviderEnabled("network")) {
                    new Thread(new C12992(lm)).start();
                }
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
    }

    public static GeoLocation getGeoLocation(Context context) {
        GeoLocation geoLocation;
        synchronized (csLock) {
            Diagnostics.m1955i(TAG, "getGeoLocation");
            if (mLocation == null || mLocation.isStale()) {
                requestGeoLocation(context);
            }
            geoLocation = mLocation;
        }
        return geoLocation;
    }

    public static GeoLocation getCachedGeoLocation(Context context) {
        GeoLocation geoLocation;
        synchronized (csLock) {
            Diagnostics.m1955i(TAG, "getCachedGeoLocation");
            geoLocation = mLocation;
        }
        return geoLocation;
    }

    static void setGeoLocation(GeoLocation location) {
        Diagnostics.m1955i(TAG, "setGeoLocation");
        mLocation = location;
    }

    private static boolean checkFineLocationPermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    private static boolean checkCourseLocationPermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0;
    }
}
