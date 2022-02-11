package com.facebook.ads.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.util.Log;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0522g.C0521a;
import com.facebook.ads.internal.util.C0536s;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/* renamed from: com.facebook.ads.internal.f */
public class C0473f {
    public static final String f1769a;
    private final String f1770b;
    private final boolean f1771c;

    /* renamed from: com.facebook.ads.internal.f.a */
    private static final class C0471a implements IInterface {
        private IBinder f1766a;

        C0471a(IBinder iBinder) {
            this.f1766a = iBinder;
        }

        public String m1387a() {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken(AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                this.f1766a.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                return readString;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public IBinder asBinder() {
            return this.f1766a;
        }

        public boolean m1388b() {
            boolean z = true;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken(AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                obtain.writeInt(1);
                this.f1766a.transact(2, obtain, obtain2, 0);
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
    }

    /* renamed from: com.facebook.ads.internal.f.b */
    private static final class C0472b implements ServiceConnection {
        private AtomicBoolean f1767a;
        private final BlockingQueue<IBinder> f1768b;

        private C0472b() {
            this.f1767a = new AtomicBoolean(false);
            this.f1768b = new LinkedBlockingDeque();
        }

        public IBinder m1389a() {
            if (!this.f1767a.compareAndSet(true, true)) {
                return (IBinder) this.f1768b.take();
            }
            throw new IllegalStateException("Binder already consumed");
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.f1768b.put(iBinder);
            } catch (InterruptedException e) {
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    static {
        f1769a = C0473f.class.getSimpleName();
    }

    private C0473f(String str, boolean z) {
        this.f1770b = str;
        this.f1771c = z;
    }

    private static C0473f m1390a(Context context) {
        Method a = C0522g.m1534a("com.google.android.gms.common.GooglePlayServicesUtil", "isGooglePlayServicesAvailable", Context.class);
        if (a == null) {
            return null;
        }
        Object a2 = C0522g.m1529a(null, a, context);
        if (a2 == null || ((Integer) a2).intValue() != 0) {
            return null;
        }
        a = C0522g.m1534a("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", Context.class);
        if (a == null) {
            return null;
        }
        Object a3 = C0522g.m1529a(null, a, context);
        if (a3 == null) {
            return null;
        }
        a = C0522g.m1533a(a3.getClass(), "getId", new Class[0]);
        Method a4 = C0522g.m1533a(a3.getClass(), "isLimitAdTrackingEnabled", new Class[0]);
        return (a == null || a4 == null) ? null : new C0473f((String) C0522g.m1529a(a3, a, new Object[0]), ((Boolean) C0522g.m1529a(a3, a4, new Object[0])).booleanValue());
    }

    public static C0473f m1391a(Context context, C0521a c0521a) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot get advertising info on main thread.");
        } else if (c0521a != null && !C0536s.m1572a(c0521a.f1911b)) {
            return new C0473f(c0521a.f1911b, c0521a.f1912c);
        } else {
            C0473f a = C0473f.m1390a(context);
            return a == null ? C0473f.m1392b(context) : a;
        }
    }

    private static C0473f m1392b(Context context) {
        ServiceConnection c0472b = new C0472b();
        Intent intent = new Intent(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT);
        intent.setPackage(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT_PACKAGE_NAME);
        if (context.bindService(intent, c0472b, 1)) {
            C0473f c0473f;
            try {
                C0471a c0471a = new C0471a(c0472b.m1389a());
                c0473f = new C0473f(c0471a.m1387a(), c0471a.m1388b());
                return c0473f;
            } catch (Exception e) {
                c0473f = e;
                Log.d(f1769a, "Unable to get advertising id from service", c0473f);
            } finally {
                context.unbindService(c0472b);
            }
        }
        return null;
    }

    public String m1393a() {
        return this.f1770b;
    }

    public boolean m1394b() {
        return this.f1771c;
    }
}
