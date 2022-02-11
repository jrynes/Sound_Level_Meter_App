package com.google.android.gms.location.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zza.zzb;
import com.google.android.gms.location.LocationServices.zza;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsApi;

public class zzq implements SettingsApi {

    /* renamed from: com.google.android.gms.location.internal.zzq.1 */
    class C09471 extends zza<LocationSettingsResult> {
        final /* synthetic */ String zzaHU;
        final /* synthetic */ LocationSettingsRequest zzaPa;
        final /* synthetic */ zzq zzaPb;

        C09471(zzq com_google_android_gms_location_internal_zzq, GoogleApiClient googleApiClient, LocationSettingsRequest locationSettingsRequest, String str) {
            this.zzaPb = com_google_android_gms_location_internal_zzq;
            this.zzaPa = locationSettingsRequest;
            this.zzaHU = str;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaPa, (zzb) this, this.zzaHU);
        }

        public LocationSettingsResult zzaR(Status status) {
            return new LocationSettingsResult(status);
        }

        public /* synthetic */ Result zzc(Status status) {
            return zzaR(status);
        }
    }

    public PendingResult<LocationSettingsResult> checkLocationSettings(GoogleApiClient client, LocationSettingsRequest request) {
        return zza(client, request, null);
    }

    public PendingResult<LocationSettingsResult> zza(GoogleApiClient googleApiClient, LocationSettingsRequest locationSettingsRequest, String str) {
        return googleApiClient.zza(new C09471(this, googleApiClient, locationSettingsRequest, str));
    }
}
