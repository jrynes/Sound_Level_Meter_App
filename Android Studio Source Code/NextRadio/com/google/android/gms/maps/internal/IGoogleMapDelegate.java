package com.google.android.gms.maps.internal;

import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;
import com.google.android.gms.maps.model.internal.zzb;
import com.google.android.gms.maps.model.internal.zzc;
import com.google.android.gms.maps.model.internal.zzf;
import com.google.android.gms.maps.model.internal.zzg;
import com.google.android.gms.maps.model.internal.zzh;
import com.rabbitmq.client.impl.AMQImpl.Basic;
import com.rabbitmq.client.impl.AMQImpl.Basic.Ack;
import com.rabbitmq.client.impl.AMQImpl.Confirm;
import com.rabbitmq.client.impl.AMQImpl.Queue.Delete;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public interface IGoogleMapDelegate extends IInterface {

    public static abstract class zza extends Binder implements IGoogleMapDelegate {

        private static class zza implements IGoogleMapDelegate {
            private IBinder zzoz;

            zza(IBinder iBinder) {
                this.zzoz = iBinder;
            }

            public zzb addCircle(CircleOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    zzb zzde = com.google.android.gms.maps.model.internal.zzb.zza.zzde(obtain2.readStrongBinder());
                    return zzde;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzc addGroundOverlay(GroundOverlayOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    zzc zzdf = com.google.android.gms.maps.model.internal.zzc.zza.zzdf(obtain2.readStrongBinder());
                    return zzdf;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzf addMarker(MarkerOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    zzf zzdi = com.google.android.gms.maps.model.internal.zzf.zza.zzdi(obtain2.readStrongBinder());
                    return zzdi;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzg addPolygon(PolygonOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    zzg zzdj = com.google.android.gms.maps.model.internal.zzg.zza.zzdj(obtain2.readStrongBinder());
                    return zzdj;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IPolylineDelegate addPolyline(PolylineOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    IPolylineDelegate zzdk = com.google.android.gms.maps.model.internal.IPolylineDelegate.zza.zzdk(obtain2.readStrongBinder());
                    return zzdk;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzh addTileOverlay(TileOverlayOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    zzh zzdl = com.google.android.gms.maps.model.internal.zzh.zza.zzdl(obtain2.readStrongBinder());
                    return zzdl;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void animateCamera(zzd update) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                    this.zzoz.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void animateCameraWithCallback(zzd update, zzb callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzoz.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void animateCameraWithDurationAndCallback(zzd update, int durationMs, zzb callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                    obtain.writeInt(durationMs);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzoz.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.zzoz;
            }

            public void clear() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public CameraPosition getCameraPosition() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    CameraPosition zzfv = obtain2.readInt() != 0 ? CameraPosition.CREATOR.zzfv(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return zzfv;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public com.google.android.gms.maps.model.internal.zzd getFocusedBuilding() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                    com.google.android.gms.maps.model.internal.zzd zzdg = com.google.android.gms.maps.model.internal.zzd.zza.zzdg(obtain2.readStrongBinder());
                    return zzdg;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getMapAsync(zzo callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.zzoz.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getMapType() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getMaxZoomLevel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getMinZoomLevel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Location getMyLocation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(23, obtain, obtain2, 0);
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

            public IProjectionDelegate getProjection() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    IProjectionDelegate zzcX = com.google.android.gms.maps.internal.IProjectionDelegate.zza.zzcX(obtain2.readStrongBinder());
                    return zzcX;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IUiSettingsDelegate getUiSettings() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    IUiSettingsDelegate zzdc = com.google.android.gms.maps.internal.IUiSettingsDelegate.zza.zzdc(obtain2.readStrongBinder());
                    return zzdc;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isBuildingsEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isIndoorEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isMyLocationEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isTrafficEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void moveCamera(zzd update) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                    this.zzoz.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onCreate(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(54, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onEnterAmbient(Bundle ambientDetails) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (ambientDetails != null) {
                        obtain.writeInt(1);
                        ambientDetails.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(81, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onExitAmbient() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(82, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(58, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(56, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(55, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (outState != null) {
                        obtain.writeInt(1);
                        outState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(60, obtain, obtain2, 0);
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

            public void setBuildingsEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoz.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setContentDescription(String description) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeString(description);
                    this.zzoz.transact(61, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setIndoorEnabled(boolean enabled) throws RemoteException {
                boolean z = true;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zzoz.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setInfoWindowAdapter(zzd adapter) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(adapter != null ? adapter.asBinder() : null);
                    this.zzoz.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setLocationSource(ILocationSourceDelegate source) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(source != null ? source.asBinder() : null);
                    this.zzoz.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMapType(int type) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeInt(type);
                    this.zzoz.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMyLocationEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoz.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnCameraChangeListener(zze listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnGroundOverlayClickListener(zzf listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(83, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnIndoorStateChangeListener(zzg listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnInfoWindowClickListener(zzh listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnInfoWindowCloseListener(zzi listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(86, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnInfoWindowLongClickListener(zzj listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(84, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnMapClickListener(zzl listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnMapLoadedCallback(zzm callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.zzoz.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnMapLongClickListener(zzn listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnMarkerClickListener(zzp listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnMarkerDragListener(zzq listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnMyLocationButtonClickListener(zzr listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnMyLocationChangeListener(zzs listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnPoiClickListener(zzt listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(80, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnPolygonClickListener(zzu listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(85, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnPolylineClickListener(zzv listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoz.transact(87, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPadding(int left, int top, int right, int bottom) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeInt(left);
                    obtain.writeInt(top);
                    obtain.writeInt(right);
                    obtain.writeInt(bottom);
                    this.zzoz.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setTrafficEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoz.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void snapshot(zzab callback, zzd bitmap) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (bitmap != null) {
                        iBinder = bitmap.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzoz.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopAnimation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean useViewLifecycleWhenInFragment() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoz.transact(59, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IGoogleMapDelegate zzcv(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IGoogleMapDelegate)) ? new zza(iBinder) : (IGoogleMapDelegate) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            IBinder iBinder = null;
            float maxZoomLevel;
            boolean isTrafficEnabled;
            boolean z;
            switch (code) {
                case Zone.PRIMARY /*1*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    CameraPosition cameraPosition = getCameraPosition();
                    reply.writeNoException();
                    if (cameraPosition != null) {
                        reply.writeInt(1);
                        cameraPosition.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Zone.SECONDARY /*2*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    maxZoomLevel = getMaxZoomLevel();
                    reply.writeNoException();
                    reply.writeFloat(maxZoomLevel);
                    return true;
                case Protocol.GGP /*3*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    maxZoomLevel = getMinZoomLevel();
                    reply.writeNoException();
                    reply.writeFloat(maxZoomLevel);
                    return true;
                case Type.MF /*4*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    moveCamera(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.RJE /*5*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCamera(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Protocol.TCP /*6*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCameraWithCallback(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()), com.google.android.gms.maps.internal.zzb.zza.zzct(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.ECHO /*7*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCameraWithDurationAndCallback(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()), data.readInt(), com.google.android.gms.maps.internal.zzb.zza.zzct(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Protocol.EGP /*8*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    stopAnimation();
                    reply.writeNoException();
                    return true;
                case Service.DISCARD /*9*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    IPolylineDelegate addPolyline = addPolyline(data.readInt() != 0 ? PolylineOptions.CREATOR.zzfD(data) : null);
                    reply.writeNoException();
                    if (addPolyline != null) {
                        iBinder = addPolyline.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Protocol.BBN_RCC_MON /*10*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    zzg addPolygon = addPolygon(data.readInt() != 0 ? PolygonOptions.CREATOR.zzfC(data) : null);
                    reply.writeNoException();
                    if (addPolygon != null) {
                        iBinder = addPolygon.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Service.USERS /*11*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    zzf addMarker = addMarker(data.readInt() != 0 ? MarkerOptions.CREATOR.zzfA(data) : null);
                    reply.writeNoException();
                    if (addMarker != null) {
                        iBinder = addMarker.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Protocol.PUP /*12*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    zzc addGroundOverlay = addGroundOverlay(data.readInt() != 0 ? GroundOverlayOptions.CREATOR.zzfx(data) : null);
                    reply.writeNoException();
                    if (addGroundOverlay != null) {
                        iBinder = addGroundOverlay.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Service.DAYTIME /*13*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    zzh addTileOverlay = addTileOverlay(data.readInt() != 0 ? TileOverlayOptions.CREATOR.zzfJ(data) : null);
                    reply.writeNoException();
                    if (addTileOverlay != null) {
                        iBinder = addTileOverlay.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Protocol.EMCON /*14*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    clear();
                    reply.writeNoException();
                    return true;
                case Protocol.XNET /*15*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    int mapType = getMapType();
                    reply.writeNoException();
                    reply.writeInt(mapType);
                    return true;
                case Protocol.CHAOS /*16*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setMapType(data.readInt());
                    reply.writeNoException();
                    return true;
                case Service.QUOTE /*17*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isTrafficEnabled();
                    reply.writeNoException();
                    reply.writeInt(isTrafficEnabled ? 1 : 0);
                    return true;
                case Protocol.MUX /*18*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setTrafficEnabled(z);
                    reply.writeNoException();
                    return true;
                case Service.CHARGEN /*19*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isIndoorEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case Service.FTP_DATA /*20*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = setIndoorEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case Service.FTP /*21*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isMyLocationEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case Protocol.XNS_IDP /*22*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setMyLocationEnabled(z);
                    reply.writeNoException();
                    return true;
                case Service.TELNET /*23*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    Location myLocation = getMyLocation();
                    reply.writeNoException();
                    if (myLocation != null) {
                        reply.writeInt(1);
                        myLocation.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Protocol.TRUNK_2 /*24*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setLocationSource(com.google.android.gms.maps.internal.ILocationSourceDelegate.zza.zzcx(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.SMTP /*25*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    IUiSettingsDelegate uiSettings = getUiSettings();
                    reply.writeNoException();
                    if (uiSettings != null) {
                        iBinder = uiSettings.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Protocol.LEAF_2 /*26*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    IProjectionDelegate projection = getProjection();
                    reply.writeNoException();
                    if (projection != null) {
                        iBinder = projection.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Service.NSW_FE /*27*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnCameraChangeListener(com.google.android.gms.maps.internal.zze.zza.zzcA(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Protocol.IRTP /*28*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMapClickListener(com.google.android.gms.maps.internal.zzl.zza.zzcH(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.MSG_ICP /*29*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMapLongClickListener(com.google.android.gms.maps.internal.zzn.zza.zzcJ(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Protocol.NETBLT /*30*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMarkerClickListener(com.google.android.gms.maps.internal.zzp.zza.zzcL(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.MSG_AUTH /*31*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMarkerDragListener(com.google.android.gms.maps.internal.zzq.zza.zzcM(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Protocol.MERIT_INP /*32*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnInfoWindowClickListener(com.google.android.gms.maps.internal.zzh.zza.zzcD(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.DSP /*33*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setInfoWindowAdapter(com.google.android.gms.maps.internal.zzd.zza.zzcw(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Type.NAPTR /*35*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    zzb addCircle = addCircle(data.readInt() != 0 ? CircleOptions.CREATOR.zzfw(data) : null);
                    reply.writeNoException();
                    if (addCircle != null) {
                        iBinder = addCircle.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Type.KX /*36*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMyLocationChangeListener(com.google.android.gms.maps.internal.zzs.zza.zzcO(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.TIME /*37*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMyLocationButtonClickListener(com.google.android.gms.maps.internal.zzr.zza.zzcN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Type.A6 /*38*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    snapshot(com.google.android.gms.maps.internal.zzab.zza.zzcY(data.readStrongBinder()), com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.RLP /*39*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setPadding(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case Delete.INDEX /*40*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isBuildingsEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case Service.GRAPHICS /*41*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setBuildingsEnabled(z);
                    reply.writeNoException();
                    return true;
                case Service.NAMESERVER /*42*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMapLoadedCallback(com.google.android.gms.maps.internal.zzm.zza.zzcI(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.MPM_FLAGS /*44*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    com.google.android.gms.maps.model.internal.zzd focusedBuilding = getFocusedBuilding();
                    reply.writeNoException();
                    if (focusedBuilding != null) {
                        iBinder = focusedBuilding.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Service.MPM /*45*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnIndoorStateChangeListener(com.google.android.gms.maps.internal.zzg.zza.zzcC(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.DOMAIN /*53*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    getMapAsync(com.google.android.gms.maps.internal.zzo.zza.zzcK(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Place.TYPE_LAWYER /*54*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onCreate(data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Service.ISI_GL /*55*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onResume();
                    reply.writeNoException();
                    return true;
                case Place.TYPE_LIQUOR_STORE /*56*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onPause();
                    reply.writeNoException();
                    return true;
                case Place.TYPE_LOCAL_GOVERNMENT_OFFICE /*57*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onDestroy();
                    reply.writeNoException();
                    return true;
                case Place.TYPE_LOCKSMITH /*58*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onLowMemory();
                    reply.writeNoException();
                    return true;
                case Place.TYPE_LODGING /*59*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = useViewLifecycleWhenInFragment();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case Basic.INDEX /*60*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    Bundle bundle = data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null;
                    onSaveInstanceState(bundle);
                    reply.writeNoException();
                    if (bundle != null) {
                        reply.writeInt(1);
                        bundle.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Service.NI_MAIL /*61*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setContentDescription(data.readString());
                    reply.writeNoException();
                    return true;
                case Ack.INDEX /*80*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnPoiClickListener(com.google.android.gms.maps.internal.zzt.zza.zzcP(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Service.HOSTS2_NS /*81*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onEnterAmbient(data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_SCHOOL /*82*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onExitAmbient();
                    reply.writeNoException();
                    return true;
                case Place.TYPE_SHOE_STORE /*83*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnGroundOverlayClickListener(com.google.android.gms.maps.internal.zzf.zza.zzcB(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Place.TYPE_SHOPPING_MALL /*84*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnInfoWindowLongClickListener(com.google.android.gms.maps.internal.zzj.zza.zzcF(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Confirm.INDEX /*85*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnPolygonClickListener(com.google.android.gms.maps.internal.zzu.zza.zzcQ(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Place.TYPE_STADIUM /*86*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnInfoWindowCloseListener(com.google.android.gms.maps.internal.zzi.zza.zzcE(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Place.TYPE_STORAGE /*87*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnPolylineClickListener(com.google.android.gms.maps.internal.zzv.zza.zzcR(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    zzb addCircle(CircleOptions circleOptions) throws RemoteException;

    zzc addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException;

    zzf addMarker(MarkerOptions markerOptions) throws RemoteException;

    zzg addPolygon(PolygonOptions polygonOptions) throws RemoteException;

    IPolylineDelegate addPolyline(PolylineOptions polylineOptions) throws RemoteException;

    zzh addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException;

    void animateCamera(zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    void animateCameraWithCallback(zzd com_google_android_gms_dynamic_zzd, zzb com_google_android_gms_maps_internal_zzb) throws RemoteException;

    void animateCameraWithDurationAndCallback(zzd com_google_android_gms_dynamic_zzd, int i, zzb com_google_android_gms_maps_internal_zzb) throws RemoteException;

    void clear() throws RemoteException;

    CameraPosition getCameraPosition() throws RemoteException;

    com.google.android.gms.maps.model.internal.zzd getFocusedBuilding() throws RemoteException;

    void getMapAsync(zzo com_google_android_gms_maps_internal_zzo) throws RemoteException;

    int getMapType() throws RemoteException;

    float getMaxZoomLevel() throws RemoteException;

    float getMinZoomLevel() throws RemoteException;

    Location getMyLocation() throws RemoteException;

    IProjectionDelegate getProjection() throws RemoteException;

    IUiSettingsDelegate getUiSettings() throws RemoteException;

    boolean isBuildingsEnabled() throws RemoteException;

    boolean isIndoorEnabled() throws RemoteException;

    boolean isMyLocationEnabled() throws RemoteException;

    boolean isTrafficEnabled() throws RemoteException;

    void moveCamera(zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    void onCreate(Bundle bundle) throws RemoteException;

    void onDestroy() throws RemoteException;

    void onEnterAmbient(Bundle bundle) throws RemoteException;

    void onExitAmbient() throws RemoteException;

    void onLowMemory() throws RemoteException;

    void onPause() throws RemoteException;

    void onResume() throws RemoteException;

    void onSaveInstanceState(Bundle bundle) throws RemoteException;

    void setBuildingsEnabled(boolean z) throws RemoteException;

    void setContentDescription(String str) throws RemoteException;

    boolean setIndoorEnabled(boolean z) throws RemoteException;

    void setInfoWindowAdapter(zzd com_google_android_gms_maps_internal_zzd) throws RemoteException;

    void setLocationSource(ILocationSourceDelegate iLocationSourceDelegate) throws RemoteException;

    void setMapType(int i) throws RemoteException;

    void setMyLocationEnabled(boolean z) throws RemoteException;

    void setOnCameraChangeListener(zze com_google_android_gms_maps_internal_zze) throws RemoteException;

    void setOnGroundOverlayClickListener(zzf com_google_android_gms_maps_internal_zzf) throws RemoteException;

    void setOnIndoorStateChangeListener(zzg com_google_android_gms_maps_internal_zzg) throws RemoteException;

    void setOnInfoWindowClickListener(zzh com_google_android_gms_maps_internal_zzh) throws RemoteException;

    void setOnInfoWindowCloseListener(zzi com_google_android_gms_maps_internal_zzi) throws RemoteException;

    void setOnInfoWindowLongClickListener(zzj com_google_android_gms_maps_internal_zzj) throws RemoteException;

    void setOnMapClickListener(zzl com_google_android_gms_maps_internal_zzl) throws RemoteException;

    void setOnMapLoadedCallback(zzm com_google_android_gms_maps_internal_zzm) throws RemoteException;

    void setOnMapLongClickListener(zzn com_google_android_gms_maps_internal_zzn) throws RemoteException;

    void setOnMarkerClickListener(zzp com_google_android_gms_maps_internal_zzp) throws RemoteException;

    void setOnMarkerDragListener(zzq com_google_android_gms_maps_internal_zzq) throws RemoteException;

    void setOnMyLocationButtonClickListener(zzr com_google_android_gms_maps_internal_zzr) throws RemoteException;

    void setOnMyLocationChangeListener(zzs com_google_android_gms_maps_internal_zzs) throws RemoteException;

    void setOnPoiClickListener(zzt com_google_android_gms_maps_internal_zzt) throws RemoteException;

    void setOnPolygonClickListener(zzu com_google_android_gms_maps_internal_zzu) throws RemoteException;

    void setOnPolylineClickListener(zzv com_google_android_gms_maps_internal_zzv) throws RemoteException;

    void setPadding(int i, int i2, int i3, int i4) throws RemoteException;

    void setTrafficEnabled(boolean z) throws RemoteException;

    void snapshot(zzab com_google_android_gms_maps_internal_zzab, zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    void stopAnimation() throws RemoteException;

    boolean useViewLifecycleWhenInFragment() throws RemoteException;
}
