package com.google.android.gms.location.places.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.zzf;
import com.google.android.gms.location.places.zzf.zza;

public class zzp implements PlacePhotoMetadata {
    private int mIndex;
    private final int zzDF;
    private final int zzDG;
    private final String zzaQR;
    private final CharSequence zzaQS;

    /* renamed from: com.google.android.gms.location.places.internal.zzp.1 */
    class C09561 extends zza<zze> {
        final /* synthetic */ int zzaQT;
        final /* synthetic */ int zzaQU;
        final /* synthetic */ zzp zzaQV;

        C09561(zzp com_google_android_gms_location_places_internal_zzp, zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, int i, int i2) {
            this.zzaQV = com_google_android_gms_location_places_internal_zzp;
            this.zzaQT = i;
            this.zzaQU = i2;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzf((zza) this), this.zzaQV.zzaQR, this.zzaQT, this.zzaQU, this.zzaQV.mIndex);
        }
    }

    public zzp(String str, int i, int i2, CharSequence charSequence, int i3) {
        this.zzaQR = str;
        this.zzDF = i;
        this.zzDG = i2;
        this.zzaQS = charSequence;
        this.mIndex = i3;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof zzp)) {
            return false;
        }
        zzp com_google_android_gms_location_places_internal_zzp = (zzp) other;
        return com_google_android_gms_location_places_internal_zzp.zzDF == this.zzDF && com_google_android_gms_location_places_internal_zzp.zzDG == this.zzDG && zzw.equal(com_google_android_gms_location_places_internal_zzp.zzaQR, this.zzaQR) && zzw.equal(com_google_android_gms_location_places_internal_zzp.zzaQS, this.zzaQS);
    }

    public /* synthetic */ Object freeze() {
        return zzzz();
    }

    public CharSequence getAttributions() {
        return this.zzaQS;
    }

    public int getMaxHeight() {
        return this.zzDG;
    }

    public int getMaxWidth() {
        return this.zzDF;
    }

    public PendingResult<PlacePhotoResult> getPhoto(GoogleApiClient client) {
        return getScaledPhoto(client, getMaxWidth(), getMaxHeight());
    }

    public PendingResult<PlacePhotoResult> getScaledPhoto(GoogleApiClient client, int width, int height) {
        return client.zza(new C09561(this, Places.zzaPN, client, width, height));
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.zzDF), Integer.valueOf(this.zzDG), this.zzaQR, this.zzaQS);
    }

    public boolean isDataValid() {
        return true;
    }

    public PlacePhotoMetadata zzzz() {
        return this;
    }
}
