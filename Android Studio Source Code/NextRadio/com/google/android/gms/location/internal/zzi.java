package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.internal.zzo;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.GestureRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.zzd;
import com.rabbitmq.client.impl.AMQImpl.Basic;
import java.util.List;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public interface zzi extends IInterface {

    public static abstract class zza extends Binder implements zzi {

        private static class zza implements zzi {
            private IBinder zzoz;

            zza(IBinder iBinder) {
                this.zzoz = iBinder;
            }

            public IBinder asBinder() {
                return this.zzoz;
            }

            public void zza(long j, boolean z, PendingIntent pendingIntent) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeLong(j);
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_common_api_internal_zzo != null ? com_google_android_gms_common_api_internal_zzo.asBinder() : null);
                    this.zzoz.transact(65, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PendingIntent pendingIntent, zzh com_google_android_gms_location_internal_zzh, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_internal_zzh != null ? com_google_android_gms_location_internal_zzh.asBinder() : null);
                    obtain.writeString(str);
                    this.zzoz.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(Location location, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (location != null) {
                        obtain.writeInt(1);
                        location.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.zzoz.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(GeofencingRequest geofencingRequest, PendingIntent pendingIntent, zzh com_google_android_gms_location_internal_zzh) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (geofencingRequest != null) {
                        obtain.writeInt(1);
                        geofencingRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_internal_zzh != null ? com_google_android_gms_location_internal_zzh.asBinder() : null);
                    this.zzoz.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(GestureRequest gestureRequest, PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (gestureRequest != null) {
                        obtain.writeInt(1);
                        gestureRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_common_api_internal_zzo != null ? com_google_android_gms_common_api_internal_zzo.asBinder() : null);
                    this.zzoz.transact(60, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(LocationRequest locationRequest, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (locationRequest != null) {
                        obtain.writeInt(1);
                        locationRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(LocationRequest locationRequest, zzd com_google_android_gms_location_zzd) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (locationRequest != null) {
                        obtain.writeInt(1);
                        locationRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_zzd != null ? com_google_android_gms_location_zzd.asBinder() : null);
                    this.zzoz.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(LocationRequest locationRequest, zzd com_google_android_gms_location_zzd, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (locationRequest != null) {
                        obtain.writeInt(1);
                        locationRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_zzd != null ? com_google_android_gms_location_zzd.asBinder() : null);
                    obtain.writeString(str);
                    this.zzoz.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(LocationSettingsRequest locationSettingsRequest, zzj com_google_android_gms_location_internal_zzj, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (locationSettingsRequest != null) {
                        obtain.writeInt(1);
                        locationSettingsRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_internal_zzj != null ? com_google_android_gms_location_internal_zzj.asBinder() : null);
                    obtain.writeString(str);
                    this.zzoz.transact(63, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(LocationRequestInternal locationRequestInternal, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (locationRequestInternal != null) {
                        obtain.writeInt(1);
                        locationRequestInternal.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(LocationRequestInternal locationRequestInternal, zzd com_google_android_gms_location_zzd) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (locationRequestInternal != null) {
                        obtain.writeInt(1);
                        locationRequestInternal.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_zzd != null ? com_google_android_gms_location_zzd.asBinder() : null);
                    this.zzoz.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(LocationRequestUpdateData locationRequestUpdateData) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (locationRequestUpdateData != null) {
                        obtain.writeInt(1);
                        locationRequestUpdateData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(59, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzg com_google_android_gms_location_internal_zzg) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeStrongBinder(com_google_android_gms_location_internal_zzg != null ? com_google_android_gms_location_internal_zzg.asBinder() : null);
                    this.zzoz.transact(67, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzh com_google_android_gms_location_internal_zzh, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeStrongBinder(com_google_android_gms_location_internal_zzh != null ? com_google_android_gms_location_internal_zzh.asBinder() : null);
                    obtain.writeString(str);
                    this.zzoz.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzd com_google_android_gms_location_zzd) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeStrongBinder(com_google_android_gms_location_zzd != null ? com_google_android_gms_location_zzd.asBinder() : null);
                    this.zzoz.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(List<ParcelableGeofence> list, PendingIntent pendingIntent, zzh com_google_android_gms_location_internal_zzh, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeTypedList(list);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_location_internal_zzh != null ? com_google_android_gms_location_internal_zzh.asBinder() : null);
                    obtain.writeString(str);
                    this.zzoz.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(String[] strArr, zzh com_google_android_gms_location_internal_zzh, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeStringArray(strArr);
                    obtain.writeStrongBinder(com_google_android_gms_location_internal_zzh != null ? com_google_android_gms_location_internal_zzh.asBinder() : null);
                    obtain.writeString(str);
                    this.zzoz.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzam(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoz.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_common_api_internal_zzo != null ? com_google_android_gms_common_api_internal_zzo.asBinder() : null);
                    this.zzoz.transact(66, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzc(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_common_api_internal_zzo != null ? com_google_android_gms_common_api_internal_zzo.asBinder() : null);
                    this.zzoz.transact(61, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzc(Location location) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (location != null) {
                        obtain.writeInt(1);
                        location.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzd(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_common_api_internal_zzo != null ? com_google_android_gms_common_api_internal_zzo.asBinder() : null);
                    this.zzoz.transact(68, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zze(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_common_api_internal_zzo != null ? com_google_android_gms_common_api_internal_zzo.asBinder() : null);
                    this.zzoz.transact(69, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ActivityRecognitionResult zzeh(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeString(str);
                    this.zzoz.transact(64, obtain, obtain2, 0);
                    obtain2.readException();
                    ActivityRecognitionResult createFromParcel = obtain2.readInt() != 0 ? ActivityRecognitionResult.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return createFromParcel;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Location zzei(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeString(str);
                    this.zzoz.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    Location location = obtain2.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return location;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public LocationAvailability zzej(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    obtain.writeString(str);
                    this.zzoz.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                    LocationAvailability createFromParcel = obtain2.readInt() != 0 ? LocationAvailability.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return createFromParcel;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Location zzyN() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    this.zzoz.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    Location location = obtain2.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return location;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzi zzcj(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzi)) ? new zza(iBinder) : (zzi) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean z = false;
            LocationRequestUpdateData locationRequestUpdateData = null;
            Location zzyN;
            LocationRequest createFromParcel;
            switch (code) {
                case Zone.PRIMARY /*1*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.createTypedArrayList(ParcelableGeofence.CREATOR), data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.location.internal.zzh.zza.zzci(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case Zone.SECONDARY /*2*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.location.internal.zzh.zza.zzci(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case Protocol.GGP /*3*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.createStringArray(), com.google.android.gms.location.internal.zzh.zza.zzci(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case Type.MF /*4*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(com.google.android.gms.location.internal.zzh.zza.zzci(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case Service.RJE /*5*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readLong(), data.readInt() != 0, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Protocol.TCP /*6*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Service.ECHO /*7*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zzyN = zzyN();
                    reply.writeNoException();
                    if (zzyN != null) {
                        reply.writeInt(1);
                        zzyN.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Protocol.EGP /*8*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (data.readInt() != 0) {
                        createFromParcel = LocationRequest.CREATOR.createFromParcel(data);
                    }
                    zza(createFromParcel, com.google.android.gms.location.zzd.zza.zzcf(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.DISCARD /*9*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? LocationRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Protocol.BBN_RCC_MON /*10*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(com.google.android.gms.location.zzd.zza.zzcf(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.USERS /*11*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zzb(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Protocol.PUP /*12*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    zzam(z);
                    reply.writeNoException();
                    return true;
                case Service.DAYTIME /*13*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zzc(data.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Service.FTP_DATA /*20*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (data.readInt() != 0) {
                        createFromParcel = LocationRequest.CREATOR.createFromParcel(data);
                    }
                    zza(createFromParcel, com.google.android.gms.location.zzd.zza.zzcf(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case Service.FTP /*21*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zzyN = zzei(data.readString());
                    reply.writeNoException();
                    if (zzyN != null) {
                        reply.writeInt(1);
                        zzyN.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Protocol.LEAF_2 /*26*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(data) : null, data.readInt());
                    reply.writeNoException();
                    return true;
                case Type.ATMA /*34*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    LocationAvailability zzej = zzej(data.readString());
                    reply.writeNoException();
                    if (zzej != null) {
                        reply.writeInt(1);
                        zzej.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Type.TLSA /*52*/:
                    LocationRequestInternal zzeX;
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (data.readInt() != 0) {
                        zzeX = LocationRequestInternal.CREATOR.zzeX(data);
                    }
                    zza(zzeX, com.google.android.gms.location.zzd.zza.zzcf(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.DOMAIN /*53*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? LocationRequestInternal.CREATOR.zzeX(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_LOCAL_GOVERNMENT_OFFICE /*57*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? (GeofencingRequest) GeofencingRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.location.internal.zzh.zza.zzci(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Place.TYPE_LODGING /*59*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (data.readInt() != 0) {
                        locationRequestUpdateData = LocationRequestUpdateData.CREATOR.zzeY(data);
                    }
                    zza(locationRequestUpdateData);
                    reply.writeNoException();
                    return true;
                case Basic.INDEX /*60*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? GestureRequest.CREATOR.zzeQ(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.common.api.internal.zzo.zza.zzaN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.NI_MAIL /*61*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zzc(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.common.api.internal.zzo.zza.zzaN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.VIA_FTP /*63*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? (LocationSettingsRequest) LocationSettingsRequest.CREATOR.createFromParcel(data) : null, com.google.android.gms.location.internal.zzj.zza.zzck(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case Protocol.SAT_EXPAK /*64*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    ActivityRecognitionResult zzeh = zzeh(data.readString());
                    reply.writeNoException();
                    if (zzeh != null) {
                        reply.writeInt(1);
                        zzeh.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Service.TACACS_DS /*65*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.common.api.internal.zzo.zza.zzaN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Protocol.RVD /*66*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zzb(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.common.api.internal.zzo.zza.zzaN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.BOOTPS /*67*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zza(com.google.android.gms.location.internal.zzg.zza.zzch(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.BOOTPC /*68*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zzd(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.common.api.internal.zzo.zza.zzaN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.TFTP /*69*/:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    zze(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.common.api.internal.zzo.zza.zzaN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zza(long j, boolean z, PendingIntent pendingIntent) throws RemoteException;

    void zza(PendingIntent pendingIntent) throws RemoteException;

    void zza(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException;

    void zza(PendingIntent pendingIntent, zzh com_google_android_gms_location_internal_zzh, String str) throws RemoteException;

    void zza(Location location, int i) throws RemoteException;

    void zza(GeofencingRequest geofencingRequest, PendingIntent pendingIntent, zzh com_google_android_gms_location_internal_zzh) throws RemoteException;

    void zza(GestureRequest gestureRequest, PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException;

    void zza(LocationRequest locationRequest, PendingIntent pendingIntent) throws RemoteException;

    void zza(LocationRequest locationRequest, zzd com_google_android_gms_location_zzd) throws RemoteException;

    void zza(LocationRequest locationRequest, zzd com_google_android_gms_location_zzd, String str) throws RemoteException;

    void zza(LocationSettingsRequest locationSettingsRequest, zzj com_google_android_gms_location_internal_zzj, String str) throws RemoteException;

    void zza(LocationRequestInternal locationRequestInternal, PendingIntent pendingIntent) throws RemoteException;

    void zza(LocationRequestInternal locationRequestInternal, zzd com_google_android_gms_location_zzd) throws RemoteException;

    void zza(LocationRequestUpdateData locationRequestUpdateData) throws RemoteException;

    void zza(zzg com_google_android_gms_location_internal_zzg) throws RemoteException;

    void zza(zzh com_google_android_gms_location_internal_zzh, String str) throws RemoteException;

    void zza(zzd com_google_android_gms_location_zzd) throws RemoteException;

    void zza(List<ParcelableGeofence> list, PendingIntent pendingIntent, zzh com_google_android_gms_location_internal_zzh, String str) throws RemoteException;

    void zza(String[] strArr, zzh com_google_android_gms_location_internal_zzh, String str) throws RemoteException;

    void zzam(boolean z) throws RemoteException;

    void zzb(PendingIntent pendingIntent) throws RemoteException;

    void zzb(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException;

    void zzc(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException;

    void zzc(Location location) throws RemoteException;

    void zzd(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException;

    void zze(PendingIntent pendingIntent, zzo com_google_android_gms_common_api_internal_zzo) throws RemoteException;

    ActivityRecognitionResult zzeh(String str) throws RemoteException;

    Location zzei(String str) throws RemoteException;

    LocationAvailability zzej(String str) throws RemoteException;

    Location zzyN() throws RemoteException;
}
