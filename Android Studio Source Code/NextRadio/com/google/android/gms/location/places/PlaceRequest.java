package com.google.android.gms.location.places;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.concurrent.TimeUnit;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;

public final class PlaceRequest implements SafeParcelable {
    public static final Creator<PlaceRequest> CREATOR;
    static final long zzaPJ;
    private final int mPriority;
    final int mVersionCode;
    private final long zzaND;
    private final long zzaNY;
    private final PlaceFilter zzaPK;

    static {
        CREATOR = new zzk();
        zzaPJ = TimeUnit.HOURS.toMillis(1);
    }

    public PlaceRequest(int versionCode, PlaceFilter filter, long interval, int priority, long expireAt) {
        this.mVersionCode = versionCode;
        this.zzaPK = filter;
        this.zzaNY = interval;
        this.mPriority = priority;
        this.zzaND = expireAt;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PlaceRequest)) {
            return false;
        }
        PlaceRequest placeRequest = (PlaceRequest) object;
        return zzw.equal(this.zzaPK, placeRequest.zzaPK) && this.zzaNY == placeRequest.zzaNY && this.mPriority == placeRequest.mPriority && this.zzaND == placeRequest.zzaND;
    }

    public long getExpirationTime() {
        return this.zzaND;
    }

    public long getInterval() {
        return this.zzaNY;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaPK, Long.valueOf(this.zzaNY), Integer.valueOf(this.mPriority), Long.valueOf(this.zzaND));
    }

    @SuppressLint({"DefaultLocale"})
    public String toString() {
        return zzw.zzy(this).zzg("filter", this.zzaPK).zzg("interval", Long.valueOf(this.zzaNY)).zzg(Send.PRIORITY, Integer.valueOf(this.mPriority)).zzg("expireAt", Long.valueOf(this.zzaND)).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzk.zza(this, parcel, flags);
    }

    public PlaceFilter zzyZ() {
        return this.zzaPK;
    }
}
