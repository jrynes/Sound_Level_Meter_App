package com.admarvel.android.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import com.admarvel.android.ads.Version;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.LinkedBlockingQueue;

/* renamed from: com.admarvel.android.util.l */
public class GoogleAdvertisingIdClient {
    public static GoogleAdvertisingIdClient f1047a;
    private String f1048b;
    private int f1049c;
    private final WeakReference<Context> f1050d;

    /* renamed from: com.admarvel.android.util.l.a */
    private static final class GoogleAdvertisingIdClient {
        private final String f1038a;
        private final boolean f1039b;

        GoogleAdvertisingIdClient(String str, boolean z) {
            this.f1038a = str;
            this.f1039b = z;
        }

        public String m607a() {
            return this.f1038a;
        }
    }

    /* renamed from: com.admarvel.android.util.l.b */
    private static final class GoogleAdvertisingIdClient implements ServiceConnection {
        boolean f1040a;
        private final LinkedBlockingQueue<IBinder> f1041b;

        private GoogleAdvertisingIdClient() {
            this.f1040a = false;
            this.f1041b = new LinkedBlockingQueue(1);
        }

        public IBinder m608a() {
            if (this.f1040a) {
                throw new IllegalStateException();
            }
            this.f1040a = true;
            return (IBinder) this.f1041b.take();
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.f1041b.put(service);
            } catch (InterruptedException e) {
            }
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }

    /* renamed from: com.admarvel.android.util.l.c */
    private static final class GoogleAdvertisingIdClient implements IInterface {
        private IBinder f1042a;

        public GoogleAdvertisingIdClient(IBinder iBinder) {
            this.f1042a = iBinder;
        }

        public String m609a() {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken(AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                this.f1042a.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.readInt();
                return readString;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public boolean m610a(boolean z, Context context) {
            int i = 1;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken(AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                obtain.writeInt(z ? 1 : 0);
                this.f1042a.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                boolean z2 = obtain2.readInt() != 0;
                GoogleAdvertisingIdClient googleAdvertisingIdClient = GoogleAdvertisingIdClient.f1047a;
                if (!z2) {
                    i = 0;
                }
                googleAdvertisingIdClient.f1049c = i;
                return z2;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public IBinder asBinder() {
            return this.f1042a;
        }
    }

    /* renamed from: com.admarvel.android.util.l.d */
    public class GoogleAdvertisingIdClient implements Runnable {
        final /* synthetic */ GoogleAdvertisingIdClient f1043a;
        private final Context f1044b;

        public GoogleAdvertisingIdClient(GoogleAdvertisingIdClient googleAdvertisingIdClient, Context context) {
            this.f1043a = googleAdvertisingIdClient;
            this.f1044b = context;
        }

        public void run() {
            if (this.f1044b != null) {
                try {
                    GoogleAdvertisingIdClient.m618e(this.f1044b);
                } catch (Exception e) {
                    Logging.log("FetchAndroidAdvertisingIdUsingIntentRunnable error " + e.getMessage());
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.util.l.e */
    public class GoogleAdvertisingIdClient {
        final /* synthetic */ GoogleAdvertisingIdClient f1045a;
        private final Context f1046b;

        public GoogleAdvertisingIdClient(GoogleAdvertisingIdClient googleAdvertisingIdClient, Context context) {
            this.f1045a = googleAdvertisingIdClient;
            this.f1046b = context;
        }

        private String m612b() {
            String str = null;
            try {
                Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.f1046b.getApplicationContext());
                if (advertisingIdInfo != null) {
                    str = advertisingIdInfo.getId();
                }
            } catch (Exception e) {
                Logging.log("Issue in fetching ADvID from Google Play Services " + e.getMessage());
            }
            return str;
        }

        public boolean m613a() {
            boolean z = false;
            try {
                Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.f1046b.getApplicationContext());
                if (advertisingIdInfo != null) {
                    z = advertisingIdInfo.isLimitAdTrackingEnabled();
                }
            } catch (Exception e) {
                Logging.log("Issue in fetching OptOut from Google Play Services " + e.getMessage());
            }
            return z;
        }
    }

    private GoogleAdvertisingIdClient(Context context) {
        int i;
        int i2 = 1;
        int i3 = 0;
        this.f1048b = "VALUE_NOT_DEFINED";
        this.f1050d = new WeakReference(context);
        try {
            Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
            i = 1;
        } catch (ClassNotFoundException e) {
            i = 0;
        }
        if (i != 0) {
            GoogleAdvertisingIdClient googleAdvertisingIdClient = new GoogleAdvertisingIdClient(this, context);
            String a = googleAdvertisingIdClient.m612b();
            boolean a2 = googleAdvertisingIdClient.m613a();
            if (a == null || a.length() <= 0) {
                Logging.log("Not able to fetch GoogleAdv Id form google service library trying form gms IAdvertisingIdService" + this.f1048b);
                if (i3 == 0 && Version.getAndroidSDKVersion() > 13) {
                    Logging.log("Fetching from IAdvertisingIdService ");
                    m623b();
                    return;
                }
            }
            m621a(a);
            if (!a2) {
                i2 = 0;
            }
            m620a(i2);
            if (this.f1048b != null && this.f1048b.equals("VALUE_NOT_DEFINED")) {
                Logging.log("Not able to fetch GoogleAdv Id form google service library trying form gms IAdvertisingIdService" + this.f1048b);
                if (i3 == 0) {
                }
            }
        }
        i3 = i;
        if (i3 == 0) {
        }
    }

    public static void m615a() {
        f1047a = null;
    }

    public static GoogleAdvertisingIdClient m616c(Context context) {
        if (f1047a == null || (f1047a != null && f1047a.f1048b.equals("VALUE_NOT_DEFINED"))) {
            f1047a = null;
            f1047a = new GoogleAdvertisingIdClient(context);
        }
        return f1047a;
    }

    private static void m618e(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot be called from the main thread");
        }
        try {
            context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0);
            ServiceConnection googleAdvertisingIdClient = new GoogleAdvertisingIdClient();
            Intent intent = new Intent(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT);
            intent.setPackage(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT_PACKAGE_NAME);
            if (context.bindService(intent, googleAdvertisingIdClient, 1)) {
                try {
                    GoogleAdvertisingIdClient googleAdvertisingIdClient2 = new GoogleAdvertisingIdClient(googleAdvertisingIdClient.m608a());
                    GoogleAdvertisingIdClient googleAdvertisingIdClient3 = new GoogleAdvertisingIdClient(googleAdvertisingIdClient2.m609a(), googleAdvertisingIdClient2.m610a(true, context));
                    f1047a.f1048b = googleAdvertisingIdClient3.m607a();
                    context.unbindService(googleAdvertisingIdClient);
                } catch (Exception e) {
                    throw e;
                } catch (Throwable th) {
                    context.unbindService(googleAdvertisingIdClient);
                }
            } else {
                throw new IOException("Google Play connection failed");
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public int m619a(Context context) {
        return this.f1049c;
    }

    public void m620a(int i) {
        this.f1049c = i;
    }

    public void m621a(String str) {
        this.f1048b = str;
    }

    public String m622b(Context context) {
        return this.f1048b != null ? this.f1048b : "VALUE_NOT_DEFINED";
    }

    public void m623b() {
        Context context = this.f1050d != null ? (Context) this.f1050d.get() : null;
        if (context != null) {
            new Thread(new GoogleAdvertisingIdClient(this, context)).start();
        }
    }
}
