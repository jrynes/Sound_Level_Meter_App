package com.google.android.gms.location;

import android.content.Intent;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class LocationResult implements SafeParcelable {
    public static final Creator<LocationResult> CREATOR;
    static final List<Location> zzaOd;
    private final int mVersionCode;
    private final List<Location> zzaOe;

    static {
        zzaOd = Collections.emptyList();
        CREATOR = new zze();
    }

    LocationResult(int versionCode, List<Location> locations) {
        this.mVersionCode = versionCode;
        this.zzaOe = locations;
    }

    public static LocationResult create(List<Location> locations) {
        if (locations == null) {
            locations = zzaOd;
        }
        return new LocationResult(2, locations);
    }

    public static LocationResult extractResult(Intent intent) {
        return !hasResult(intent) ? null : (LocationResult) intent.getExtras().getParcelable("com.google.android.gms.location.EXTRA_LOCATION_RESULT");
    }

    public static boolean hasResult(Intent intent) {
        return intent == null ? false : intent.hasExtra("com.google.android.gms.location.EXTRA_LOCATION_RESULT");
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof LocationResult)) {
            return false;
        }
        LocationResult locationResult = (LocationResult) other;
        if (locationResult.zzaOe.size() != this.zzaOe.size()) {
            return false;
        }
        Iterator it = this.zzaOe.iterator();
        for (Location time : locationResult.zzaOe) {
            if (((Location) it.next()).getTime() != time.getTime()) {
                return false;
            }
        }
        return true;
    }

    @NonNull
    public Location getLastLocation() {
        int size = this.zzaOe.size();
        return size == 0 ? null : (Location) this.zzaOe.get(size - 1);
    }

    @NonNull
    public List<Location> getLocations() {
        return this.zzaOe;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        int i = 17;
        for (Location time : this.zzaOe) {
            long time2 = time.getTime();
            i = ((int) (time2 ^ (time2 >>> 32))) + (i * 31);
        }
        return i;
    }

    public String toString() {
        return "LocationResult[locations: " + this.zzaOe + "]";
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zze.zza(this, parcel, flags);
    }
}
