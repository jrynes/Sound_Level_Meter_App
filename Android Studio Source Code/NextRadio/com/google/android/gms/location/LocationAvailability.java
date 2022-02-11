package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import org.apache.activemq.ActiveMQPrefetchPolicy;

public final class LocationAvailability implements SafeParcelable {
    public static final LocationAvailabilityCreator CREATOR;
    private final int mVersionCode;
    int zzaNU;
    int zzaNV;
    long zzaNW;
    int zzaNX;

    static {
        CREATOR = new LocationAvailabilityCreator();
    }

    LocationAvailability(int versionCode, int locationStatus, int cellStatus, int wifiStatus, long elapsedRealtimeNs) {
        this.mVersionCode = versionCode;
        this.zzaNX = locationStatus;
        this.zzaNU = cellStatus;
        this.zzaNV = wifiStatus;
        this.zzaNW = elapsedRealtimeNs;
    }

    public static LocationAvailability extractLocationAvailability(Intent intent) {
        return !hasLocationAvailability(intent) ? null : (LocationAvailability) intent.getExtras().getParcelable("com.google.android.gms.location.EXTRA_LOCATION_AVAILABILITY");
    }

    public static boolean hasLocationAvailability(Intent intent) {
        return intent == null ? false : intent.hasExtra("com.google.android.gms.location.EXTRA_LOCATION_AVAILABILITY");
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof LocationAvailability)) {
            return false;
        }
        LocationAvailability locationAvailability = (LocationAvailability) other;
        return this.zzaNX == locationAvailability.zzaNX && this.zzaNU == locationAvailability.zzaNU && this.zzaNV == locationAvailability.zzaNV && this.zzaNW == locationAvailability.zzaNW;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.zzaNX), Integer.valueOf(this.zzaNU), Integer.valueOf(this.zzaNV), Long.valueOf(this.zzaNW));
    }

    public boolean isLocationAvailable() {
        return this.zzaNX < ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
    }

    public String toString() {
        return "LocationAvailability[isLocationAvailable: " + isLocationAvailable() + "]";
    }

    public void writeToParcel(Parcel parcel, int flags) {
        LocationAvailabilityCreator.zza(this, parcel, flags);
    }
}
