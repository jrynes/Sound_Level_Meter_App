package com.amazon.device.associates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.amazon.device.associates.bb.CommandDispatcher;

/* renamed from: com.amazon.device.associates.w */
final class KiwiResponseHandler implements bj {
    private static final String f1392a;

    KiwiResponseHandler() {
    }

    static {
        f1392a = KiwiResponseHandler.class.getSimpleName();
    }

    public void m1028a(Context context, Intent intent, bb bbVar) {
        aa.m693b(f1392a, "handleResponse");
        try {
            Bundle extras = intent.getExtras();
            String string = extras.getString("notifyType");
            if (string.equalsIgnoreCase("physical_purchase_done")) {
                bbVar.m864a(CommandDispatcher.GET_PURCHASE_RESULT, RequestId.m671a(extras.getString("requestId")), null);
                return;
            }
            aa.m691a(f1392a, "unknown notifyType: " + string);
        } catch (Exception e) {
            aa.m691a(f1392a, "Error in handleResponse: " + e);
        }
    }
}
