package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public interface IStreetViewPanoramaViewDelegate extends IInterface {

    public static abstract class zza extends Binder implements IStreetViewPanoramaViewDelegate {

        private static class zza implements IStreetViewPanoramaViewDelegate {
            private IBinder zzoz;

            zza(IBinder iBinder) {
                this.zzoz = iBinder;
            }

            public IBinder asBinder() {
                return this.zzoz;
            }

            public IStreetViewPanoramaDelegate getStreetViewPanorama() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    this.zzoz.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    IStreetViewPanoramaDelegate zzcZ = com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate.zza.zzcZ(obtain2.readStrongBinder());
                    return zzcZ;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getStreetViewPanoramaAsync(zzaa callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.zzoz.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd getView() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    this.zzoz.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzbs = com.google.android.gms.dynamic.zzd.zza.zzbs(obtain2.readStrongBinder());
                    return zzbs;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onCreate(Bundle savedInstanceState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    if (savedInstanceState != null) {
                        obtain.writeInt(1);
                        savedInstanceState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onDestroy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    this.zzoz.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onLowMemory() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    this.zzoz.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onPause() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    this.zzoz.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onResume() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    this.zzoz.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onSaveInstanceState(Bundle outState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    if (outState != null) {
                        obtain.writeInt(1);
                        outState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        outState.readFromParcel(obtain2);
                    }
                    obtain2.recycle();
                    obtain.recycle();
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IStreetViewPanoramaViewDelegate zzdb(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IStreetViewPanoramaViewDelegate)) ? new zza(iBinder) : (IStreetViewPanoramaViewDelegate) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            Bundle bundle;
            switch (code) {
                case Zone.PRIMARY /*1*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    IStreetViewPanoramaDelegate streetViewPanorama = getStreetViewPanorama();
                    reply.writeNoException();
                    if (streetViewPanorama != null) {
                        iBinder = streetViewPanorama.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Zone.SECONDARY /*2*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    onCreate(bundle);
                    reply.writeNoException();
                    return true;
                case Protocol.GGP /*3*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    onResume();
                    reply.writeNoException();
                    return true;
                case Type.MF /*4*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    onPause();
                    reply.writeNoException();
                    return true;
                case Service.RJE /*5*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    onDestroy();
                    reply.writeNoException();
                    return true;
                case Protocol.TCP /*6*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    onLowMemory();
                    reply.writeNoException();
                    return true;
                case Service.ECHO /*7*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    onSaveInstanceState(bundle);
                    reply.writeNoException();
                    if (bundle != null) {
                        reply.writeInt(1);
                        bundle.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case Protocol.EGP /*8*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    zzd view = getView();
                    reply.writeNoException();
                    if (view != null) {
                        iBinder = view.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Service.DISCARD /*9*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    getStreetViewPanoramaAsync(com.google.android.gms.maps.internal.zzaa.zza.zzcW(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IStreetViewPanoramaDelegate getStreetViewPanorama() throws RemoteException;

    void getStreetViewPanoramaAsync(zzaa com_google_android_gms_maps_internal_zzaa) throws RemoteException;

    zzd getView() throws RemoteException;

    void onCreate(Bundle bundle) throws RemoteException;

    void onDestroy() throws RemoteException;

    void onLowMemory() throws RemoteException;

    void onPause() throws RemoteException;

    void onResume() throws RemoteException;

    void onSaveInstanceState(Bundle bundle) throws RemoteException;
}
