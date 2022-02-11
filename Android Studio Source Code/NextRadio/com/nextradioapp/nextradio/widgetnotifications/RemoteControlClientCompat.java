package com.nextradioapp.nextradio.widgetnotifications;

import android.app.PendingIntent;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Method;

public class RemoteControlClientCompat {
    private static final String TAG = "RemoteControlCompat";
    private static boolean sHasRemoteControlAPIs;
    private static Method sRCCEditMetadataMethod;
    private static Method sRCCSetPlayStateMethod;
    private static Method sRCCSetPlayStateMethodAPI18;
    private static Method sRCCSetTransportControlFlags;
    private static Class sRemoteControlClientClass;
    private Object mActualRemoteControlClient;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static {
        /*
        r8 = 0;
        sHasRemoteControlAPIs = r8;
        r8 = com.nextradioapp.nextradio.widgetnotifications.RemoteControlClientCompat.class;
        r1 = r8.getClassLoader();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = getActualRemoteControlClientClass(r1);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        sRemoteControlClientClass = r8;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = com.nextradioapp.nextradio.widgetnotifications.RemoteControlClientCompat.class;
        r0 = r8.getFields();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r5 = r0.length;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r4 = 0;
    L_0x0017:
        if (r4 >= r5) goto L_0x00ad;
    L_0x0019:
        r3 = r0[r4];	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = sRemoteControlClientClass;	 Catch:{ NoSuchFieldException -> 0x0031, IllegalArgumentException -> 0x0051, IllegalAccessException -> 0x007f, ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, SecurityException -> 0x0104 }
        r9 = r3.getName();	 Catch:{ NoSuchFieldException -> 0x0031, IllegalArgumentException -> 0x0051, IllegalAccessException -> 0x007f, ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, SecurityException -> 0x0104 }
        r6 = r8.getField(r9);	 Catch:{ NoSuchFieldException -> 0x0031, IllegalArgumentException -> 0x0051, IllegalAccessException -> 0x007f, ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, SecurityException -> 0x0104 }
        r8 = 0;
        r7 = r6.get(r8);	 Catch:{ NoSuchFieldException -> 0x0031, IllegalArgumentException -> 0x0051, IllegalAccessException -> 0x007f, ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, SecurityException -> 0x0104 }
        r8 = 0;
        r3.set(r8, r7);	 Catch:{ NoSuchFieldException -> 0x0031, IllegalArgumentException -> 0x0051, IllegalAccessException -> 0x007f, ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, SecurityException -> 0x0104 }
    L_0x002e:
        r4 = r4 + 1;
        goto L_0x0017;
    L_0x0031:
        r2 = move-exception;
        r8 = "RemoteControlCompat";
        r9 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9.<init>();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = "Could not get real field: ";
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = r3.getName();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = r9.toString();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        android.util.Log.w(r8, r9);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        goto L_0x002e;
    L_0x004f:
        r8 = move-exception;
    L_0x0050:
        return;
    L_0x0051:
        r2 = move-exception;
        r8 = "RemoteControlCompat";
        r9 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9.<init>();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = "Error trying to pull field value for: ";
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = r3.getName();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = " ";
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = r2.getMessage();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = r9.toString();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        android.util.Log.w(r8, r9);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        goto L_0x002e;
    L_0x007d:
        r8 = move-exception;
        goto L_0x0050;
    L_0x007f:
        r2 = move-exception;
        r8 = "RemoteControlCompat";
        r9 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9.<init>();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = "Error trying to pull field value for: ";
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = r3.getName();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = " ";
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10 = r2.getMessage();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = r9.toString();	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        android.util.Log.w(r8, r9);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        goto L_0x002e;
    L_0x00ab:
        r8 = move-exception;
        goto L_0x0050;
    L_0x00ad:
        r8 = sRemoteControlClientClass;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = "editMetadata";
        r10 = 1;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r11 = 0;
        r12 = java.lang.Boolean.TYPE;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10[r11] = r12;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = r8.getMethod(r9, r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        sRCCEditMetadataMethod = r8;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = sRemoteControlClientClass;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = "setPlaybackState";
        r10 = 1;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r11 = 0;
        r12 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10[r11] = r12;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = r8.getMethod(r9, r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        sRCCSetPlayStateMethod = r8;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = sRemoteControlClientClass;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = "setPlaybackState";
        r10 = 3;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r11 = 0;
        r12 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10[r11] = r12;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r11 = 1;
        r12 = java.lang.Long.TYPE;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10[r11] = r12;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r11 = 2;
        r12 = java.lang.Float.TYPE;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10[r11] = r12;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = r8.getMethod(r9, r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        sRCCSetPlayStateMethodAPI18 = r8;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = sRemoteControlClientClass;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r9 = "setTransportControlFlags";
        r10 = 1;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r11 = 0;
        r12 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r10[r11] = r12;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = r8.getMethod(r9, r10);	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        sRCCSetTransportControlFlags = r8;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        r8 = 1;
        sHasRemoteControlAPIs = r8;	 Catch:{ ClassNotFoundException -> 0x004f, NoSuchMethodException -> 0x007d, IllegalArgumentException -> 0x00ab, SecurityException -> 0x0104 }
        goto L_0x0050;
    L_0x0104:
        r8 = move-exception;
        goto L_0x0050;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nextradioapp.nextradio.widgetnotifications.RemoteControlClientCompat.<clinit>():void");
    }

    public static Class getActualRemoteControlClientClass(ClassLoader classLoader) throws ClassNotFoundException {
        return classLoader.loadClass("android.media.RemoteControlClient");
    }

    public RemoteControlClientCompat(PendingIntent pendingIntent) {
        if (sHasRemoteControlAPIs) {
            try {
                this.mActualRemoteControlClient = sRemoteControlClientClass.getConstructor(new Class[]{PendingIntent.class}).newInstance(new Object[]{pendingIntent});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public RemoteControlClientCompat(PendingIntent pendingIntent, Looper looper) {
        if (sHasRemoteControlAPIs) {
            try {
                this.mActualRemoteControlClient = sRemoteControlClientClass.getConstructor(new Class[]{PendingIntent.class, Looper.class}).newInstance(new Object[]{pendingIntent, looper});
            } catch (Exception e) {
                Log.e(TAG, "Error creating new instance of " + sRemoteControlClientClass.getName(), e);
            }
        }
    }

    public MetadataEditorCompat editMetadata(boolean startEmpty) {
        Object invoke;
        if (sHasRemoteControlAPIs) {
            try {
                invoke = sRCCEditMetadataMethod.invoke(this.mActualRemoteControlClient, new Object[]{Boolean.valueOf(startEmpty)});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        invoke = null;
        return new MetadataEditorCompat(this, invoke, null);
    }

    public void setPlaybackState(int state) {
        if (sHasRemoteControlAPIs) {
            try {
                if (sRCCSetPlayStateMethodAPI18 != null) {
                    sRCCSetPlayStateMethodAPI18.invoke(this.mActualRemoteControlClient, new Object[]{Integer.valueOf(state), Integer.valueOf(-1), Integer.valueOf(-1)});
                    return;
                }
                sRCCSetPlayStateMethod.invoke(this.mActualRemoteControlClient, new Object[]{Integer.valueOf(state)});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setTransportControlFlags(int transportControlFlags) {
        if (sHasRemoteControlAPIs) {
            try {
                sRCCSetTransportControlFlags.invoke(this.mActualRemoteControlClient, new Object[]{Integer.valueOf(transportControlFlags)});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final Object getActualRemoteControlClientObject() {
        return this.mActualRemoteControlClient;
    }
}
