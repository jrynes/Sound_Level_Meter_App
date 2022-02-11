package com.amazon.device.associates;

import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.Map;

/* compiled from: AndroidThreadHandlerManager */
class bf {
    private static volatile Map<String, Handler> f1258a;
    private static volatile Handler f1259b;

    bf() {
    }

    static {
        f1258a = new HashMap();
        f1259b = null;
    }

    static Handler m871a() {
        if (f1259b == null) {
            synchronized (bf.class) {
                if (f1259b == null) {
                    f1259b = new Handler(Looper.getMainLooper());
                }
            }
        }
        return f1259b;
    }
}
