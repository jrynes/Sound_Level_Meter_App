package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import org.apache.activemq.transport.stomp.Stomp;

public final class LatLng implements SafeParcelable {
    public static final zze CREATOR;
    public final double latitude;
    public final double longitude;
    private final int mVersionCode;

    static {
        CREATOR = new zze();
    }

    public LatLng(double latitude, double longitude) {
        this(1, latitude, longitude);
    }

    LatLng(int versionCode, double latitude, double longitude) {
        this.mVersionCode = versionCode;
        if (-180.0d > longitude || longitude >= 180.0d) {
            this.longitude = ((((longitude - 180.0d) % 360.0d) + 360.0d) % 360.0d) - 180.0d;
        } else {
            this.longitude = longitude;
        }
        this.latitude = Math.max(-90.0d, Math.min(90.0d, latitude));
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LatLng)) {
            return false;
        }
        LatLng latLng = (LatLng) o;
        return Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(latLng.latitude) && Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(latLng.longitude);
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.latitude);
        int i = ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31;
        long doubleToLongBits2 = Double.doubleToLongBits(this.longitude);
        return (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
    }

    public String toString() {
        return "lat/lng: (" + this.latitude + Stomp.COMMA + this.longitude + ")";
    }

    public void writeToParcel(Parcel out, int flags) {
        zze.zza(this, out, flags);
    }
}
