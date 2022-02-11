package com.amazon.device.associates;

import android.util.Log;

/* compiled from: HandlerFactory */
final class az {
    private static final String f1207a;
    private static boolean f1208b;
    private static volatile boolean f1209c;
    private static volatile boolean f1210d;
    private static volatile aw f1211e;
    private static volatile aw f1212f;
    private static volatile bj f1213g;
    private static volatile ay f1214h;
    private static volatile ay f1215i;

    az() {
    }

    static {
        f1207a = az.class.getSimpleName();
        f1208b = false;
        f1209c = false;
        f1210d = false;
        f1211e = null;
        f1212f = null;
        f1213g = null;
        f1214h = null;
        f1215i = null;
    }

    static void m820a(boolean z) {
        f1208b = z;
    }

    static aw m818a() {
        if (f1211e == null) {
            synchronized (az.class) {
                f1211e = (aw) m819a(aw.class, f1211e, false);
            }
        }
        return f1211e;
    }

    static aw m821b() {
        if (f1212f == null) {
            synchronized (az.class) {
                f1212f = (aw) m819a(aw.class, f1212f, true);
            }
        }
        return f1212f;
    }

    static bj m822c() {
        if (f1213g == null) {
            synchronized (az.class) {
                f1213g = (bj) m819a(bj.class, f1213g, false);
            }
        }
        return f1213g;
    }

    private static <T> T m819a(Class<T> cls, T t, boolean z) {
        if (t != null) {
            return t;
        }
        try {
            ay ayVar;
            m824e();
            if (z) {
                ayVar = f1215i;
            } else {
                ayVar = f1214h;
            }
            if (ayVar != null) {
                return ayVar.m817a(cls).newInstance();
            }
            return t;
        } catch (Throwable e) {
            Log.e(f1207a, "error getting instance for " + cls, e);
            return t;
        }
    }

    private static void m824e() {
        if (f1214h == null) {
            synchronized (az.class) {
                if (f1214h == null) {
                    if (m823d()) {
                        f1214h = new KiwiHandlerRegistry();
                        f1215i = new DASHandlerRegistry();
                        aa.m690a(new KiwiLogHandler());
                    } else if (f1208b) {
                        f1214h = new SandboxHandlerRegistry();
                        f1215i = new DASHandlerRegistry();
                        aa.m690a(new am());
                    } else {
                        f1214h = new DASHandlerRegistry();
                        f1215i = null;
                    }
                }
            }
        }
    }

    static boolean m823d() {
        if (f1210d) {
            return f1209c;
        }
        synchronized (az.class) {
            if (f1210d) {
                boolean z = f1209c;
                return z;
            }
            try {
                az.class.getClassLoader().loadClass("com.amazon.android.Kiwi");
                f1209c = true;
            } catch (Throwable th) {
                f1209c = false;
            }
            f1210d = true;
            return f1209c;
        }
    }
}
