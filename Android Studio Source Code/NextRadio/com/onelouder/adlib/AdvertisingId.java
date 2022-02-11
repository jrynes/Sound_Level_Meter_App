package com.onelouder.adlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingQueue;

public class AdvertisingId {
    private static final String TAG = "AdvertisingId";
    private static AdvertisingIdListener mListener;

    /* renamed from: com.onelouder.adlib.AdvertisingId.1 */
    static class C12931 implements Runnable {
        final /* synthetic */ Context val$context;

        C12931(Context context) {
            this.val$context = context;
        }

        public void run() {
            boolean DEBUG;
            try {
                DEBUG = (this.val$context.getApplicationInfo().flags & 2) != 0;
                Method getAdvertisingIdInfoMethod = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getDeclaredMethod("getAdvertisingIdInfo", new Class[]{Context.class});
                Object[] objArr = new Object[1];
                objArr[0] = this.val$context;
                Object adIdInfo = getAdvertisingIdInfoMethod.invoke(null, objArr);
                Class<?> adInfoClass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
                Method getIdMethod = adInfoClass.getDeclaredMethod("getId", new Class[0]);
                Method limitTrackingMethod = adInfoClass.getDeclaredMethod("isLimitAdTrackingEnabled", new Class[0]);
                Object adIdValue = getIdMethod.invoke(adIdInfo, (Object[]) null);
                Object adTrackingValue = limitTrackingMethod.invoke(adIdInfo, (Object[]) null);
                if ((adIdValue instanceof String) && (adTrackingValue instanceof Boolean)) {
                    if (DEBUG) {
                        Log.w(AdvertisingId.TAG, "Id=" + adIdValue + ", limitTracking=" + ((Boolean) adTrackingValue));
                    }
                    AdvertisingId.mListener.onAdIdReady((String) adIdValue, ((Boolean) adTrackingValue).booleanValue());
                }
            } catch (Exception e) {
                Log.w(AdvertisingId.TAG, "Google Play Services missing? Pull Ad Id as a service");
                try {
                    this.val$context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0);
                } catch (Exception e2) {
                    AdvertisingId.mListener.onAdIdError("Unable to resolve Google Play Service package");
                }
                AdvertisingConnection connection = new AdvertisingConnection();
                Intent intent = new Intent(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT);
                intent.setPackage(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT_PACKAGE_NAME);
                if (this.val$context.bindService(intent, connection, 1)) {
                    try {
                        AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());
                        String adIdValue2 = adInterface.getId();
                        boolean adTrackingValue2 = adInterface.isLimitAdTrackingEnabled(true);
                        if (DEBUG) {
                            Log.w(AdvertisingId.TAG, "Id=" + adIdValue2 + ", limitTracking=" + adTrackingValue2);
                        }
                        AdvertisingId.mListener.onAdIdReady(adIdValue2, adTrackingValue2);
                    } catch (Exception exception) {
                        String message = exception.getMessage();
                        String str = AdvertisingId.TAG;
                        if (message == null) {
                            message = "null message";
                        }
                        Log.w(str, message);
                        exception.printStackTrace();
                        AdvertisingId.mListener.onAdIdError(exception.getMessage());
                    } finally {
                        this.val$context.unbindService(connection);
                    }
                } else {
                    AdvertisingId.mListener.onAdIdError("Unable to bind to Google Play Service");
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        private final LinkedBlockingQueue<IBinder> queue;
        boolean retrieved;

        private AdvertisingConnection() {
            this.retrieved = false;
            this.queue = new LinkedBlockingQueue(1);
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.queue.put(service);
            } catch (InterruptedException e) {
            }
        }

        public void onServiceDisconnected(ComponentName name) {
        }

        public IBinder getBinder() throws InterruptedException {
            if (this.retrieved) {
                throw new IllegalStateException();
            }
            this.retrieved = true;
            return (IBinder) this.queue.take();
        }
    }

    public interface AdvertisingIdListener {
        void onAdIdError(String str);

        void onAdIdReady(String str, boolean z);
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;

        public AdvertisingInterface(IBinder pBinder) {
            this.binder = pBinder;
        }

        public IBinder asBinder() {
            return this.binder;
        }

        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                this.binder.transact(1, data, reply, 0);
                reply.readException();
                String id = reply.readString();
                return id;
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        public boolean isLimitAdTrackingEnabled(boolean paramBoolean) throws RemoteException {
            boolean limitAdTracking = true;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                int i;
                data.writeInterfaceToken(AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                if (paramBoolean) {
                    i = 1;
                } else {
                    i = 0;
                }
                data.writeInt(i);
                this.binder.transact(2, data, reply, 0);
                reply.readException();
                if (reply.readInt() == 0) {
                    limitAdTracking = false;
                }
                reply.recycle();
                data.recycle();
                return limitAdTracking;
            } catch (Throwable th) {
                reply.recycle();
                data.recycle();
            }
        }
    }

    public static void getAdvertisingId(Context context, AdvertisingIdListener listener) {
        if (listener != null) {
            mListener = listener;
            new Thread(new C12931(context)).start();
        }
    }
}
