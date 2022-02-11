package com.mologiq.analytics;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/* renamed from: com.mologiq.analytics.c */
final class AdvertisingId {
    static AdvertisingId f2115a;
    private final String f2116b;
    private final boolean f2117c;

    /* renamed from: com.mologiq.analytics.c.a */
    private static final class AdvertisingId implements ServiceConnection {
        boolean f2110a;
        private final LinkedBlockingQueue<IBinder> f2111b;

        private AdvertisingId() {
            this.f2110a = false;
            this.f2111b = new LinkedBlockingQueue(1);
        }

        final IBinder m1708a() {
            if (this.f2110a) {
                throw new IllegalStateException();
            }
            this.f2110a = true;
            return (IBinder) this.f2111b.take();
        }

        public final void onServiceConnected(ComponentName componentName, IBinder service) {
            try {
                this.f2111b.put(service);
            } catch (InterruptedException e) {
            }
        }

        public final void onServiceDisconnected(ComponentName componentName) {
        }
    }

    /* renamed from: com.mologiq.analytics.c.b */
    private static final class AdvertisingId implements IInterface {
        private IBinder f2112a;

        AdvertisingId(IBinder iBinder) {
            this.f2112a = iBinder;
        }

        final String m1709a() {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken(AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                this.f2112a.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.readInt();
                return readString;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        final boolean m1710a(boolean z) {
            boolean z2 = true;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken(AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                obtain.writeInt(1);
                this.f2112a.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() == 0) {
                    z2 = false;
                }
                obtain2.recycle();
                obtain.recycle();
                return z2;
            } catch (Throwable th) {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public final IBinder asBinder() {
            return this.f2112a;
        }
    }

    /* renamed from: com.mologiq.analytics.c.c */
    class AdvertisingId {
        final /* synthetic */ AdvertisingId f2113a;
        private final Context f2114b;

        AdvertisingId(AdvertisingId advertisingId, Context context) {
            this.f2113a = advertisingId;
            this.f2114b = context;
        }

        private String m1712b() {
            String str = null;
            try {
                Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.f2114b.getApplicationContext());
                if (advertisingIdInfo != null) {
                    str = advertisingIdInfo.getId();
                }
            } catch (Exception e) {
            }
            return str;
        }

        final boolean m1713a() {
            boolean z = false;
            try {
                Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.f2114b.getApplicationContext());
                if (advertisingIdInfo != null) {
                    z = advertisingIdInfo.isLimitAdTrackingEnabled();
                }
            } catch (Exception e) {
            }
            return z;
        }
    }

    private AdvertisingId(Context context) {
        boolean z = true;
        boolean z2 = false;
        try {
            Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
            Class.forName("com.google.android.gms.common.GooglePlayServicesNotAvailableException");
            Class.forName("com.google.android.gms.common.GooglePlayServicesRepairableException");
        } catch (ClassNotFoundException e) {
            z = z2;
        }
        if (z) {
            AdvertisingId advertisingId = new AdvertisingId(this, context);
            this.f2116b = advertisingId.m1712b();
            this.f2117c = advertisingId.m1713a();
            return;
        }
        String str = null;
        try {
            AdvertisingId b = AdvertisingId.m1715b(context);
            str = b.m1709a();
            z2 = b.m1710a(true);
        } catch (Exception e2) {
        }
        this.f2116b = str;
        this.f2117c = z2;
    }

    static AdvertisingId m1714a(Context context) {
        if (f2115a == null) {
            f2115a = new AdvertisingId(context);
        }
        return f2115a;
    }

    private static AdvertisingId m1715b(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot be called from the main thread");
        }
        try {
            context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0);
            ServiceConnection advertisingId = new AdvertisingId();
            Intent intent = new Intent(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT);
            intent.setPackage(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT_PACKAGE_NAME);
            if (context.bindService(intent, advertisingId, 1)) {
                try {
                    AdvertisingId advertisingId2 = new AdvertisingId(advertisingId.m1708a());
                    context.unbindService(advertisingId);
                    return advertisingId2;
                } catch (Exception e) {
                    throw e;
                } catch (Throwable th) {
                    context.unbindService(advertisingId);
                }
            } else {
                throw new IOException("Google Play connection failed");
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    final String m1716a() {
        return this.f2116b;
    }

    final boolean m1717b() {
        return this.f2117c;
    }
}
