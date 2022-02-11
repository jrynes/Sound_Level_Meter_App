package com.google.android.gms.location.places.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
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

            public void zzaU(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzac(DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (dataHolder != null) {
                        obtain.writeInt(1);
                        dataHolder.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzad(DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (dataHolder != null) {
                        obtain.writeInt(1);
                        dataHolder.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzae(DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (dataHolder != null) {
                        obtain.writeInt(1);
                        dataHolder.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzaf(DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (dataHolder != null) {
                        obtain.writeInt(1);
                        dataHolder.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.location.places.internal.IPlacesCallbacks");
        }

        public static zzi zzcp(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.places.internal.IPlacesCallbacks");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzi)) ? new zza(iBinder) : (zzi) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            DataHolder dataHolder = null;
            switch (code) {
                case Zone.PRIMARY /*1*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.zzak(data);
                    }
                    zzac(dataHolder);
                    return true;
                case Zone.SECONDARY /*2*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.zzak(data);
                    }
                    zzad(dataHolder);
                    return true;
                case Protocol.GGP /*3*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.zzak(data);
                    }
                    zzae(dataHolder);
                    return true;
                case Type.MF /*4*/:
                    Status status;
                    data.enforceInterface("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (data.readInt() != 0) {
                        status = (Status) Status.CREATOR.createFromParcel(data);
                    }
                    zzaU(status);
                    return true;
                case Service.RJE /*5*/:
                    data.enforceInterface("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.zzak(data);
                    }
                    zzaf(dataHolder);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.location.places.internal.IPlacesCallbacks");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zzaU(Status status) throws RemoteException;

    void zzac(DataHolder dataHolder) throws RemoteException;

    void zzad(DataHolder dataHolder) throws RemoteException;

    void zzae(DataHolder dataHolder) throws RemoteException;

    void zzaf(DataHolder dataHolder) throws RemoteException;
}
