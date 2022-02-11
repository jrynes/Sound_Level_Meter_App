package com.amazon.device.associates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class DASBroadcastReceiver extends BroadcastReceiver {
    private static final String f1073a;
    private static volatile boolean f1074c;
    private final bb f1075b;

    /* renamed from: com.amazon.device.associates.DASBroadcastReceiver.1 */
    static class C03481 extends Thread {
        final /* synthetic */ DASBroadcastReceiver f1072a;

        C03481(DASBroadcastReceiver dASBroadcastReceiver) {
            this.f1072a = dASBroadcastReceiver;
        }

        public void run() {
            bp.m899a().unregisterReceiver(this.f1072a);
        }
    }

    static {
        f1073a = DASBroadcastReceiver.class.getName();
        f1074c = false;
    }

    public DASBroadcastReceiver(bb bbVar) {
        this.f1075b = bbVar;
    }

    public void onReceive(Context context, Intent intent) {
        Log.m1013a(f1073a, "Received an Intent, forwarding to response handler.");
        new bk().m681a(context, intent, this.f1075b);
    }

    public static void m651a(bb bbVar) {
        if (!f1074c) {
            Log.m1013a(f1073a, "Registering the BroadcastRecevier for DAS.");
            synchronized (DASBroadcastReceiver.class) {
                if (!f1074c) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("com.amazon.device.iap.physical.physical_purchase");
                    BroadcastReceiver dASBroadcastReceiver = new DASBroadcastReceiver(bbVar);
                    bp.m899a().registerReceiver(dASBroadcastReceiver, intentFilter);
                    Runtime.getRuntime().addShutdownHook(new C03481(dASBroadcastReceiver));
                    f1074c = true;
                }
            }
        }
    }
}
