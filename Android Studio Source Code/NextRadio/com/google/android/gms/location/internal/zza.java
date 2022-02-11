package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognitionApi;

public class zza implements ActivityRecognitionApi {

    private static abstract class zza extends com.google.android.gms.location.ActivityRecognition.zza<Status> {
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

    /* renamed from: com.google.android.gms.location.internal.zza.1 */
    class C09311 extends zza {
        final /* synthetic */ long zzaOp;
        final /* synthetic */ PendingIntent zzaOq;
        final /* synthetic */ zza zzaOr;

        C09311(zza com_google_android_gms_location_internal_zza, GoogleApiClient googleApiClient, long j, PendingIntent pendingIntent) {
            this.zzaOr = com_google_android_gms_location_internal_zza;
            this.zzaOp = j;
            this.zzaOq = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaOp, this.zzaOq);
            zza(Status.zzagC);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zza.2 */
    class C09322 extends zza {
        final /* synthetic */ PendingIntent zzaOq;
        final /* synthetic */ zza zzaOr;

        C09322(zza com_google_android_gms_location_internal_zza, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzaOr = com_google_android_gms_location_internal_zza;
            this.zzaOq = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaOq);
            zza(Status.zzagC);
        }
    }

    public PendingResult<Status> removeActivityUpdates(GoogleApiClient client, PendingIntent callbackIntent) {
        return client.zzb(new C09322(this, client, callbackIntent));
    }

    public PendingResult<Status> requestActivityUpdates(GoogleApiClient client, long detectionIntervalMillis, PendingIntent callbackIntent) {
        return client.zzb(new C09311(this, client, detectionIntervalMillis, callbackIntent));
    }
}
