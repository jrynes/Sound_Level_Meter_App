package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.zzc;
import com.google.android.gms.location.zzd;
import com.google.android.gms.location.zzd.zza;

public class LocationRequestUpdateData implements SafeParcelable {
    public static final zzn CREATOR;
    PendingIntent mPendingIntent;
    private final int mVersionCode;
    int zzaOU;
    LocationRequestInternal zzaOV;
    zzd zzaOW;
    zzc zzaOX;
    zzg zzaOY;

    static {
        CREATOR = new zzn();
    }

    LocationRequestUpdateData(int versionCode, int operation, LocationRequestInternal locationRequest, IBinder locationListenerBinder, PendingIntent pendingIntent, IBinder locationCallbackBinder, IBinder fusedLocationProviderCallbackBinder) {
        zzg com_google_android_gms_location_internal_zzg = null;
        this.mVersionCode = versionCode;
        this.zzaOU = operation;
        this.zzaOV = locationRequest;
        this.zzaOW = locationListenerBinder == null ? null : zza.zzcf(locationListenerBinder);
        this.mPendingIntent = pendingIntent;
        this.zzaOX = locationCallbackBinder == null ? null : zzc.zza.zzce(locationCallbackBinder);
        if (fusedLocationProviderCallbackBinder != null) {
            com_google_android_gms_location_internal_zzg = zzg.zza.zzch(fusedLocationProviderCallbackBinder);
        }
        this.zzaOY = com_google_android_gms_location_internal_zzg;
    }

    public static LocationRequestUpdateData zza(LocationRequestInternal locationRequestInternal, PendingIntent pendingIntent, @Nullable zzg com_google_android_gms_location_internal_zzg) {
        return new LocationRequestUpdateData(1, 1, locationRequestInternal, null, pendingIntent, null, com_google_android_gms_location_internal_zzg != null ? com_google_android_gms_location_internal_zzg.asBinder() : null);
    }

    public static LocationRequestUpdateData zza(LocationRequestInternal locationRequestInternal, zzc com_google_android_gms_location_zzc, @Nullable zzg com_google_android_gms_location_internal_zzg) {
        return new LocationRequestUpdateData(1, 1, locationRequestInternal, null, null, com_google_android_gms_location_zzc.asBinder(), com_google_android_gms_location_internal_zzg != null ? com_google_android_gms_location_internal_zzg.asBinder() : null);
    }

    public static LocationRequestUpdateData zza(LocationRequestInternal locationRequestInternal, zzd com_google_android_gms_location_zzd, @Nullable zzg com_google_android_gms_location_internal_zzg) {
        return new LocationRequestUpdateData(1, 1, locationRequestInternal, com_google_android_gms_location_zzd.asBinder(), null, null, com_google_android_gms_location_internal_zzg != null ? com_google_android_gms_location_internal_zzg.asBinder() : null);
    }

    public static LocationRequestUpdateData zza(zzc com_google_android_gms_location_zzc, @Nullable zzg com_google_android_gms_location_internal_zzg) {
        return new LocationRequestUpdateData(1, 2, null, null, null, com_google_android_gms_location_zzc.asBinder(), com_google_android_gms_location_internal_zzg != null ? com_google_android_gms_location_internal_zzg.asBinder() : null);
    }

    public static LocationRequestUpdateData zza(zzd com_google_android_gms_location_zzd, @Nullable zzg com_google_android_gms_location_internal_zzg) {
        return new LocationRequestUpdateData(1, 2, null, com_google_android_gms_location_zzd.asBinder(), null, null, com_google_android_gms_location_internal_zzg != null ? com_google_android_gms_location_internal_zzg.asBinder() : null);
    }

    public static LocationRequestUpdateData zzb(PendingIntent pendingIntent, @Nullable zzg com_google_android_gms_location_internal_zzg) {
        return new LocationRequestUpdateData(1, 2, null, null, pendingIntent, null, com_google_android_gms_location_internal_zzg != null ? com_google_android_gms_location_internal_zzg.asBinder() : null);
    }

    public int describeContents() {
        return 0;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzn.zza(this, parcel, flags);
    }

    IBinder zzyQ() {
        return this.zzaOW == null ? null : this.zzaOW.asBinder();
    }

    IBinder zzyR() {
        return this.zzaOX == null ? null : this.zzaOX.asBinder();
    }

    IBinder zzyS() {
        return this.zzaOY == null ? null : this.zzaOY.asBinder();
    }
}
