package com.amazon.device.associates;

import android.os.RemoteException;
import android.util.Log;
import com.amazon.venezia.command.SuccessResult;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: KiwiReceiptReceivedCommandTask */
final class ae extends as {
    private static final String f1179b;

    static {
        f1179b = ae.class.getSimpleName();
    }

    ae(RequestId requestId, bb bbVar) {
        super("physical_notify_receiptReceived", Stomp.V1_0, requestId, bbVar);
        m715a(false);
    }

    protected void m718a() {
    }

    protected void onSuccess(SuccessResult successResult) throws RemoteException {
        aa.m693b(f1179b, "onSuccess");
        try {
            Map data = successResult.getData();
            aa.m693b(f1179b, "data: " + data);
            if (data.containsKey("errorMessage")) {
                Log.e(f1179b, (String) data.get("errorMessage"));
            }
        } catch (Exception e) {
            aa.m691a(f1179b, "error in onSuccess: " + e.getMessage());
        }
    }
}
