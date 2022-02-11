package com.onelouder.adlib;

import android.location.Location;
import org.apache.activemq.transport.stomp.Stomp;

class GeoLocation {
    private static final long MAX_AGE = 1800000;
    private double mLatitude;
    private double mLongitude;
    private String mPostalcode;
    private long mTimeCreated;

    public GeoLocation() {
        this.mPostalcode = Stomp.EMPTY;
        this.mLatitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mTimeCreated = System.currentTimeMillis();
    }

    public GeoLocation(double lat, double lon) {
        this.mPostalcode = Stomp.EMPTY;
        this.mLatitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mTimeCreated = System.currentTimeMillis();
        this.mLatitude = lat;
        this.mLongitude = lon;
    }

    public Location getLocation() {
        Location location = new Location(Stomp.EMPTY);
        location.setLatitude(this.mLatitude);
        location.setLongitude(this.mLongitude);
        return location;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mLatitude);
        sb.append(Stomp.COMMA);
        sb.append(this.mLongitude);
        return sb.toString();
    }

    public void setPostalcode(String postalcode) {
        this.mPostalcode = postalcode;
    }

    public String getPostalcode() {
        return this.mPostalcode;
    }

    public void setLatLong(double lat, double lon) {
        this.mLatitude = lat;
        this.mLongitude = lon;
    }

    public String getLatitude() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mLatitude);
        return sb.toString();
    }

    public String getLongitude() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mLongitude);
        return sb.toString();
    }

    public boolean isStale() {
        long remaining = MAX_AGE - (System.currentTimeMillis() - this.mTimeCreated);
        if (remaining < 0) {
            remaining = 0;
        }
        if (Diagnostics.getInstance().isEnabled(3)) {
            Diagnostics.m1955i("GeoLocation", "staleness check -- remaining=" + remaining);
        }
        return remaining == 0;
    }
}
