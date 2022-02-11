package com.nextradioapp.androidSDK.ext;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nextradioapp.core.Log;
import com.nextradioapp.core.dependencies.ILocationAdapter;
import com.nextradioapp.core.interfaces.ILocationListener;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.WKSRecord.Service;

public class LocationAdapter implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, ILocationAdapter {
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
    protected static final int REQUEST_CHECK_SETTINGS = 1;
    protected static final String TAG = "location-settings";
    public static long UPDATE_INTERVAL_IN_MILLISECONDS;
    private final Context mContext;
    protected Location mCurrentLocation;
    private double mDistance;
    protected GoogleApiClient mGoogleApiClient;
    private com.nextradioapp.core.objects.Location mLastLocation;
    private ArrayList<ILocationListener> mListeners;
    private final LocationManager mLocationManager;
    protected LocationRequest mLocationRequest;
    private boolean mProviderIsEnabled;

    static {
        UPDATE_INTERVAL_IN_MILLISECONDS = 60000;
        FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    }

    public LocationAdapter(Context context, int i) {
        this.mListeners = new ArrayList();
        this.mDistance = 0.0d;
        this.mContext = context;
        this.mLocationManager = (LocationManager) this.mContext.getSystemService("location");
        buildGoogleApiClient();
        onStart();
    }

    public void setUpdateLocationInterval(boolean isAppRunning) {
        if (isAppRunning) {
            UPDATE_INTERVAL_IN_MILLISECONDS = 60000;
        } else {
            UPDATE_INTERVAL_IN_MILLISECONDS = 1200000;
        }
    }

    public com.nextradioapp.core.objects.Location getLastLocation(long maxAgeMillis, long timeoutMillis) throws InterruptedException {
        Log.m1934d(TAG, "getLastLocation - " + maxAgeMillis);
        if (this.mLastLocation == null || !this.mLastLocation.isValid()) {
            com.nextradioapp.core.objects.Location networkLocation = convert(this.mCurrentLocation);
            if (networkLocation == null || !networkLocation.isValid() || networkLocation.getAge() >= maxAgeMillis) {
                com.nextradioapp.core.objects.Location gpsLocation = convert(this.mCurrentLocation);
                if (gpsLocation == null || !gpsLocation.isValid() || gpsLocation.getAge() >= maxAgeMillis) {
                    if (this.mCurrentLocation == null) {
                        connectLocation();
                    } else {
                        resumeLocationUpdates();
                    }
                    long timeOut = System.currentTimeMillis() + timeoutMillis;
                    while (System.currentTimeMillis() < timeOut) {
                        if (this.mLastLocation != null && this.mLastLocation.isValid()) {
                            return this.mLastLocation;
                        }
                        if (this.mLastLocation == null) {
                            this.mLastLocation = convert(this.mCurrentLocation);
                        }
                        Log.m1934d(TAG, "Waiting for location..." + this.mLastLocation);
                        Log.m1934d(TAG, "Waiting for Current location..." + this.mCurrentLocation);
                        Thread.sleep(1000);
                    }
                    Log.m1934d(TAG, "Location Not Found");
                    return null;
                }
                this.mLastLocation = gpsLocation;
                Log.m1934d(TAG, "GPS location OK, age: " + (gpsLocation.getAge() / 1000) + " sec, sending:" + this.mLastLocation.getLatitude() + Stomp.COMMA + this.mLastLocation.getLongitude());
                return gpsLocation;
            }
            this.mLastLocation = networkLocation;
            Log.m1934d(TAG, "network location OK, age: " + (networkLocation.getAge() / 1000) + " sec, sending:" + this.mLastLocation.getLatitude() + Stomp.COMMA + this.mLastLocation.getLongitude());
            return networkLocation;
        }
        Log.m1934d(TAG, "last location OK, sending:" + this.mLastLocation.getLatitude() + Stomp.COMMA + this.mLastLocation.getLongitude());
        return this.mLastLocation;
    }

    public void subscribeLocationListener(ILocationListener l, com.nextradioapp.core.objects.Location currentLocation, double distance) {
        Log.m1934d(TAG, "subscribeLocationListener()");
        this.mDistance = distance;
        this.mListeners.add(l);
    }

    public void unsubscribeLocationListner(ILocationListener l) {
        stopLocationUpdates();
        this.mListeners.remove(l);
    }

    public boolean locationServicesAvailable() {
        try {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.mContext) != 0) {
                return false;
            }
            for (String provider : this.mLocationManager.getProviders(true)) {
                if (!provider.equals("passive")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void shutdownLocationServices() {
        stopLocationUpdates();
    }

    public void testLocationChange(com.nextradioapp.core.objects.Location l) {
        this.mLastLocation = l;
        Iterator i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            ((ILocationListener) i$.next()).onLocationChanged(this.mLastLocation);
        }
    }

    public com.nextradioapp.core.objects.Location getCurrentLocation() {
        if (this.mLastLocation == null) {
            this.mLastLocation = convert(this.mCurrentLocation);
        }
        return this.mLastLocation;
    }

    public void simulateLocationChangeFromGPS(com.nextradioapp.core.objects.Location l) {
        Location location = new Location("Simulated");
        location.setLatitude((double) l.getLatitude());
        location.setLongitude((double) l.getLongitude());
        onLocationChanged(location);
    }

    public void startLocationTracking(com.nextradioapp.core.objects.Location lastLocation) {
        Log.m1935e(TAG, "startLocationTracking() " + (lastLocation == null ? "null" : lastLocation.getLatitude() + Stomp.COMMA + lastLocation.getLongitude()));
        this.mLastLocation = lastLocation;
        if (locationServicesAvailable()) {
            startLocationUpdates();
        }
    }

    public void invalidateLocation() {
        this.mLastLocation = null;
    }

    public void onConnected(Bundle bundle) {
        connectLocation();
    }

    private void connectLocation() {
        if (this.mCurrentLocation == null && this.mGoogleApiClient != null) {
            this.mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);
        }
        startLocationUpdates();
    }

    public void onLocationChanged(Location location) {
        this.mCurrentLocation = location;
        Log.m1934d(TAG, "last location, :" + this.mCurrentLocation.getLatitude() + Stomp.COMMA + this.mCurrentLocation.getLongitude() + " , accuracy :" + this.mCurrentLocation.getAccuracy());
        this.mLastLocation = convert(this.mCurrentLocation);
        Iterator i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            ((ILocationListener) i$.next()).onLocationChanged(this.mLastLocation);
        }
    }

    private com.nextradioapp.core.objects.Location convert(Location l) {
        if (l != null) {
            return new com.nextradioapp.core.objects.Location(l.getLatitude(), l.getLongitude(), l.getTime());
        }
        return null;
    }

    public void onConnectionSuspended(int cause) {
        Log.m1934d(TAG, "Connection suspended");
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }
    }

    public void onConnectionFailed(ConnectionResult result) {
        Log.m1934d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public synchronized void buildGoogleApiClient() {
        Log.m1935e(TAG, "Building GoogleApiClient");
        this.mGoogleApiClient = new Builder(this.mContext).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        createLocationRequest();
    }

    public void createLocationRequest() {
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        this.mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        this.mLocationRequest.setPriority(Service.ISO_TSAP);
    }

    public void startLocationUpdates() {
        try {
            if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
                if (VERSION.SDK_INT < 23) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
                } else if (ContextCompat.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
                }
            }
        } catch (Exception e) {
        }
    }

    public void stopLocationUpdates() {
        if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, (LocationListener) this);
        }
    }

    public void onStart() {
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }
    }

    public void resumeLocationUpdates() {
        if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    public void onPause() {
        if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    public void disConnectLocationClient() {
        if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
            this.mGoogleApiClient.disconnect();
        }
    }
}
