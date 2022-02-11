package com.google.android.gms.location.places.internal;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.location.places.NearbyAlertRequest;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.PlaceRequest;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public interface zzf extends IInterface {

    public static abstract class zza extends Binder implements zzf {

        private static class zza implements zzf {
            private IBinder zzoz;

            zza(IBinder iBinder) {
                this.zzoz = iBinder;
            }

            public IBinder asBinder() {
                return this.zzoz;
            }

            public void zza(NearbyAlertRequest nearbyAlertRequest, PlacesParams placesParams, PendingIntent pendingIntent, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (nearbyAlertRequest != null) {
                        obtain.writeInt(1);
                        nearbyAlertRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (placesParams != null) {
                        obtain.writeInt(1);
                        placesParams.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_places_internal_zzi != null ? com_google_android_gms_location_places_internal_zzi.asBinder() : null);
                    this.zzoz.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PlaceFilter placeFilter, PlacesParams placesParams, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (placeFilter != null) {
                        obtain.writeInt(1);
                        placeFilter.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (placesParams != null) {
                        obtain.writeInt(1);
                        placesParams.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_places_internal_zzi != null ? com_google_android_gms_location_places_internal_zzi.asBinder() : null);
                    this.zzoz.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PlaceReport placeReport, PlacesParams placesParams, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (placeReport != null) {
                        obtain.writeInt(1);
                        placeReport.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (placesParams != null) {
                        obtain.writeInt(1);
                        placesParams.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_places_internal_zzi != null ? com_google_android_gms_location_places_internal_zzi.asBinder() : null);
                    this.zzoz.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PlaceRequest placeRequest, PlacesParams placesParams, PendingIntent pendingIntent, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (placeRequest != null) {
                        obtain.writeInt(1);
                        placeRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (placesParams != null) {
                        obtain.writeInt(1);
                        placesParams.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_places_internal_zzi != null ? com_google_android_gms_location_places_internal_zzi.asBinder() : null);
                    this.zzoz.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PlacesParams placesParams, PendingIntent pendingIntent, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (placesParams != null) {
                        obtain.writeInt(1);
                        placesParams.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_places_internal_zzi != null ? com_google_android_gms_location_places_internal_zzi.asBinder() : null);
                    this.zzoz.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(PlacesParams placesParams, PendingIntent pendingIntent, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (placesParams != null) {
                        obtain.writeInt(1);
                        placesParams.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_places_internal_zzi != null ? com_google_android_gms_location_places_internal_zzi.asBinder() : null);
                    this.zzoz.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzf zzcm(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzf)) ? new zza(iBinder) : (zzf) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PlacesParams placesParams = null;
            switch (code) {
                case Zone.SECONDARY /*2*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    zza(data.readInt() != 0 ? (PlaceRequest) PlaceRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? PlacesParams.CREATOR.zzfo(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.location.places.internal.zzi.zza.zzcp(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Protocol.GGP /*3*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    zza(data.readInt() != 0 ? PlacesParams.CREATOR.zzfo(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.location.places.internal.zzi.zza.zzcp(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Type.MF /*4*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    zza(data.readInt() != 0 ? NearbyAlertRequest.CREATOR.zzfd(data) : null, data.readInt() != 0 ? PlacesParams.CREATOR.zzfo(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.location.places.internal.zzi.zza.zzcp(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.RJE /*5*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    zzb(data.readInt() != 0 ? PlacesParams.CREATOR.zzfo(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.location.places.internal.zzi.zza.zzcp(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Protocol.TCP /*6*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    PlaceFilter zzfe = data.readInt() != 0 ? PlaceFilter.CREATOR.zzfe(data) : null;
                    if (data.readInt() != 0) {
                        placesParams = PlacesParams.CREATOR.zzfo(data);
                    }
                    zza(zzfe, placesParams, com.google.android.gms.location.places.internal.zzi.zza.zzcp(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.ECHO /*7*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    PlaceReport placeReport = data.readInt() != 0 ? (PlaceReport) PlaceReport.CREATOR.createFromParcel(data) : null;
                    if (data.readInt() != 0) {
                        placesParams = PlacesParams.CREATOR.zzfo(data);
                    }
                    zza(placeReport, placesParams, com.google.android.gms.location.places.internal.zzi.zza.zzcp(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zza(NearbyAlertRequest nearbyAlertRequest, PlacesParams placesParams, PendingIntent pendingIntent, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException;

    void zza(PlaceFilter placeFilter, PlacesParams placesParams, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException;

    void zza(PlaceReport placeReport, PlacesParams placesParams, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException;

    void zza(PlaceRequest placeRequest, PlacesParams placesParams, PendingIntent pendingIntent, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException;

    void zza(PlacesParams placesParams, PendingIntent pendingIntent, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException;

    void zzb(PlacesParams placesParams, PendingIntent pendingIntent, zzi com_google_android_gms_location_places_internal_zzi) throws RemoteException;
}
