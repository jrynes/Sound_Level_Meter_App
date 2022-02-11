package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zza.zzb;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.GeofencingRequest.Builder;
import java.util.List;

public class zzf implements GeofencingApi {

    private static abstract class zza extends com.google.android.gms.location.LocationServices.zza<Status> {
        public zza(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public Status zzb(Status status) {
            return status;
        }

        public /* synthetic */ Result zzc(Status status) {
            return zzb(status);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzf.1 */
    class C09431 extends zza {
        final /* synthetic */ PendingIntent zzaAp;
        final /* synthetic */ GeofencingRequest zzaOD;
        final /* synthetic */ zzf zzaOE;

        C09431(zzf com_google_android_gms_location_internal_zzf, GoogleApiClient googleApiClient, GeofencingRequest geofencingRequest, PendingIntent pendingIntent) {
            this.zzaOE = com_google_android_gms_location_internal_zzf;
            this.zzaOD = geofencingRequest;
            this.zzaAp = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaOD, this.zzaAp, (zzb) this);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzf.2 */
    class C09442 extends zza {
        final /* synthetic */ PendingIntent zzaAp;
        final /* synthetic */ zzf zzaOE;

        C09442(zzf com_google_android_gms_location_internal_zzf, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzaOE = com_google_android_gms_location_internal_zzf;
            this.zzaAp = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaAp, (zzb) this);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzf.3 */
    class C09453 extends zza {
        final /* synthetic */ zzf zzaOE;
        final /* synthetic */ List zzaOF;

        C09453(zzf com_google_android_gms_location_internal_zzf, GoogleApiClient googleApiClient, List list) {
            this.zzaOE = com_google_android_gms_location_internal_zzf;
            this.zzaOF = list;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaOF, (zzb) this);
        }
    }

    public PendingResult<Status> addGeofences(GoogleApiClient client, GeofencingRequest geofencingRequest, PendingIntent pendingIntent) {
        return client.zzb(new C09431(this, client, geofencingRequest, pendingIntent));
    }

    @Deprecated
    public PendingResult<Status> addGeofences(GoogleApiClient client, List<Geofence> geofences, PendingIntent pendingIntent) {
        Builder builder = new Builder();
        builder.addGeofences(geofences);
        builder.setInitialTrigger(5);
        return addGeofences(client, builder.build(), pendingIntent);
    }

    public PendingResult<Status> removeGeofences(GoogleApiClient client, PendingIntent pendingIntent) {
        return client.zzb(new C09442(this, client, pendingIntent));
    }

    public PendingResult<Status> removeGeofences(GoogleApiClient client, List<String> geofenceRequestIds) {
        return client.zzb(new C09453(this, client, geofenceRequestIds));
    }
}
