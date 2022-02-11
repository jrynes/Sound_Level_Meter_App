package com.google.android.gms.location.places.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.zzf;
import com.google.android.gms.location.places.zzf.zzb;
import com.google.android.gms.location.places.zzl;
import com.google.android.gms.location.places.zzl.zza;
import com.google.android.gms.location.places.zzl.zzc;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.Arrays;

public class zzd implements GeoDataApi {

    /* renamed from: com.google.android.gms.location.places.internal.zzd.1 */
    class C09501 extends zzc<zze> {
        final /* synthetic */ AddPlaceRequest zzaQk;
        final /* synthetic */ zzd zzaQl;

        C09501(zzd com_google_android_gms_location_places_internal_zzd, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, AddPlaceRequest addPlaceRequest) {
            this.zzaQl = com_google_android_gms_location_places_internal_zzd;
            this.zzaQk = addPlaceRequest;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzl((zzc) this, com_google_android_gms_location_places_internal_zze.getContext()), this.zzaQk);
        }
    }

    /* renamed from: com.google.android.gms.location.places.internal.zzd.2 */
    class C09512 extends zzc<zze> {
        final /* synthetic */ zzd zzaQl;
        final /* synthetic */ String[] zzaQm;

        C09512(zzd com_google_android_gms_location_places_internal_zzd, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, String[] strArr) {
            this.zzaQl = com_google_android_gms_location_places_internal_zzd;
            this.zzaQm = strArr;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzl((zzc) this, com_google_android_gms_location_places_internal_zze.getContext()), Arrays.asList(this.zzaQm));
        }
    }

    /* renamed from: com.google.android.gms.location.places.internal.zzd.3 */
    class C09523 extends zza<zze> {
        final /* synthetic */ String zzaGh;
        final /* synthetic */ zzd zzaQl;
        final /* synthetic */ LatLngBounds zzaQn;
        final /* synthetic */ AutocompleteFilter zzaQo;

        C09523(zzd com_google_android_gms_location_places_internal_zzd, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, String str, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {
            this.zzaQl = com_google_android_gms_location_places_internal_zzd;
            this.zzaGh = str;
            this.zzaQn = latLngBounds;
            this.zzaQo = autocompleteFilter;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzl((zza) this), this.zzaGh, this.zzaQn, this.zzaQo);
        }
    }

    /* renamed from: com.google.android.gms.location.places.internal.zzd.4 */
    class C09534 extends zzb<zze> {
        final /* synthetic */ zzd zzaQl;
        final /* synthetic */ String zzaQp;

        C09534(zzd com_google_android_gms_location_places_internal_zzd, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, String str) {
            this.zzaQl = com_google_android_gms_location_places_internal_zzd;
            this.zzaQp = str;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzf((zzb) this), this.zzaQp);
        }
    }

    public PendingResult<PlaceBuffer> addPlace(GoogleApiClient client, AddPlaceRequest addPlaceRequest) {
        return client.zzb(new C09501(this, Places.zzaPN, client, addPlaceRequest));
    }

    public PendingResult<AutocompletePredictionBuffer> getAutocompletePredictions(GoogleApiClient client, String query, LatLngBounds bounds, AutocompleteFilter filter) {
        return client.zza(new C09523(this, Places.zzaPN, client, query, bounds, filter));
    }

    public PendingResult<PlaceBuffer> getPlaceById(GoogleApiClient client, String... placeIds) {
        boolean z = true;
        if (placeIds == null || placeIds.length < 1) {
            z = false;
        }
        zzx.zzac(z);
        return client.zza(new C09512(this, Places.zzaPN, client, placeIds));
    }

    public PendingResult<PlacePhotoMetadataResult> getPlacePhotos(GoogleApiClient client, String placeId) {
        return client.zza(new C09534(this, Places.zzaPN, client, placeId));
    }
}
