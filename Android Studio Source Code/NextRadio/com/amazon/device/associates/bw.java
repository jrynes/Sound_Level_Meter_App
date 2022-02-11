package com.amazon.device.associates;

import android.util.Log;
import com.amazon.venezia.command.SuccessResult;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: KiwiSubmitMetricCommandTask */
class bw extends as {
    private static final String f1300b;

    static {
        f1300b = bw.class.getSimpleName();
    }

    bw(RequestId requestId, String str, String str2, bb bbVar) {
        super("submit_metric", Stomp.V1_0, requestId, bbVar);
        m713a("metricName", (Object) str);
        m713a("metricAttributes", (Object) str2);
        m715a(false);
    }

    protected void m931a() {
    }

    protected void onSuccess(SuccessResult successResult) {
        aa.m693b(f1300b, "onSuccess");
        try {
            Map data = successResult.getData();
            aa.m693b(f1300b, "data: " + data);
            if (data.containsKey("errorMessage")) {
                Log.e(f1300b, (String) data.get("errorMessage"));
            }
        } catch (Exception e) {
            aa.m691a(f1300b, "error in onSuccess: " + e.getMessage());
        }
    }
}
