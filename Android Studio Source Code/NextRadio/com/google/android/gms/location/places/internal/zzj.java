package com.google.android.gms.location.places.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.zzl;
import com.google.android.gms.location.places.zzl.zzd;
import com.google.android.gms.location.places.zzl.zzf;

public class zzj implements PlaceDetectionApi {

    /* renamed from: com.google.android.gms.location.places.internal.zzj.1 */
    class C09541 extends zzd<zzk> {
        final /* synthetic */ PlaceFilter zzaQt;
        final /* synthetic */ zzj zzaQu;

        C09541(zzj com_google_android_gms_location_places_internal_zzj, zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, PlaceFilter placeFilter) {
            this.zzaQu = com_google_android_gms_location_places_internal_zzj;
            this.zzaQt = placeFilter;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzk com_google_android_gms_location_places_internal_zzk) throws RemoteException {
            com_google_android_gms_location_places_internal_zzk.zza(new zzl((zzd) this, com_google_android_gms_location_places_internal_zzk.getContext()), this.zzaQt);
        }
    }

    /* renamed from: com.google.android.gms.location.places.internal.zzj.2 */
    class C09552 extends zzf<zzk> {
        final /* synthetic */ zzj zzaQu;
        final /* synthetic */ PlaceReport zzaQv;

        C09552(zzj com_google_android_gms_location_places_internal_zzj, zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, PlaceReport placeReport) {
            this.zzaQu = com_google_android_gms_location_places_internal_zzj;
            this.zzaQv = placeReport;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzk com_google_android_gms_location_places_internal_zzk) throws RemoteException {
            com_google_android_gms_location_places_internal_zzk.zza(new zzl((zzf) this), this.zzaQv);
        }
    }

    public PendingResult<PlaceLikelihoodBuffer> getCurrentPlace(GoogleApiClient client, PlaceFilter filter) {
        return client.zza(new C09541(this, Places.zzaPO, client, filter));
    }

    public PendingResult<Status> reportDeviceAtPlace(GoogleApiClient client, PlaceReport report) {
        return client.zzb(new C09552(this, Places.zzaPO, client, report));
    }
}
