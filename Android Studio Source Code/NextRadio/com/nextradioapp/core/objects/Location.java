package com.nextradioapp.core.objects;

public class Location {
    private double mLatitude;
    private double mLongitude;
    public String tag;
    private long timeCreated;

    public Location(String tag) {
        this.timeCreated = System.currentTimeMillis();
        this.tag = tag;
    }

    public Location(double lat, double longi, long time) {
        this.mLatitude = lat;
        this.mLongitude = longi;
        if (time == 0) {
            this.timeCreated = System.currentTimeMillis();
        } else {
            this.timeCreated = time;
        }
    }

    public boolean isValid() {
        if (this.mLatitude == 0.0d && this.mLongitude == 0.0d) {
            return false;
        }
        return true;
    }

    public void setLatitude(float latitude) {
        this.mLatitude = (double) latitude;
    }

    public void setLongitude(float longitude) {
        this.mLongitude = (double) longitude;
    }

    public float getLatitude() {
        return (float) this.mLatitude;
    }

    public float getLongitude() {
        return (float) this.mLongitude;
    }

    public long getAge() {
        return System.currentTimeMillis() - this.timeCreated;
    }

    public double distTo(Location compareLoc) {
        double lat1 = this.mLatitude;
        double lng1 = this.mLongitude;
        double lat2 = (double) compareLoc.getLatitude();
        double lng2 = (double) compareLoc.getLongitude();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.pow(Math.sin(dLat / 2.0d), 2.0d) + ((Math.pow(Math.sin(dLng / 2.0d), 2.0d) * Math.cos(Math.toRadians(lat1))) * Math.cos(Math.toRadians(lat2)));
        return 6367500.0d * (2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a)));
    }
}
