package com.amazon.device.associates;

import android.content.Context;
import android.content.Intent;
import com.amazon.device.associates.bb.CommandDispatcher;
import java.util.HashSet;
import java.util.Set;

/* compiled from: DASResponseHandler */
final class bk implements bj {
    private static final String f1269a;

    bk() {
    }

    static {
        f1269a = bk.class.getSimpleName();
    }

    public void m886a(Context context, Intent intent, bb bbVar) {
        Log.m1018c(f1269a, "handleResponse");
        try {
            Log.m1018c(f1269a, "Intent action received from Dummy Response Receiver:" + intent.getAction());
            String action = intent.getAction();
            RequestId a = RequestId.m671a(intent.getStringExtra("requestId"));
            if (action.equalsIgnoreCase("com.amazon.device.iap.physical.get_serviceStatus")) {
                m884a(bbVar, a);
            } else if (action.equalsIgnoreCase("com.amazon.device.iap.physical.physical_purchase")) {
                m885a(bbVar, a, intent);
            } else {
                Log.m1018c(f1269a, "response with unknown command: " + action);
            }
        } catch (Exception e) {
            Log.m1018c(f1269a, "error in handleResponse: " + e);
        }
    }

    private void m884a(bb bbVar, RequestId requestId) {
        Set hashSet = new HashSet();
        hashSet.add(PurchaseExperience.DIRECT_WITH_DETAIL);
        hashSet.add(PurchaseExperience.DIRECT_WITH_PREVIEW);
        bbVar.m866a(CommandDispatcher.GET_SERVICE_STATUS, new ServiceStatusResponse(requestId, null, hashSet, false, false));
    }

    private void m885a(bb bbVar, RequestId requestId, Intent intent) {
        bbVar.m866a(CommandDispatcher.PURCHASE, new PurchaseResponse(requestId, PopoverStatus.m1026a(intent.getExtras().getString("Povover-Status"))));
    }
}
