package com.amazon.device.associates;

import android.os.RemoteException;
import android.util.Log;
import com.amazon.device.associates.ReceiptsResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;
import com.amazon.venezia.command.SuccessResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.amazon.device.associates.x */
final class KiwiReceiptsCommandTask extends as {
    private static final String f1393b;

    static {
        f1393b = KiwiReceiptsCommandTask.class.getSimpleName();
    }

    KiwiReceiptsCommandTask(RequestId requestId, ReceiptsRequest receiptsRequest, bb bbVar) {
        super("physical_get_receipts", Stomp.V1_0, requestId, bbVar);
        m713a("offset", (Object) receiptsRequest.getOffset().toString());
        m715a(false);
    }

    protected void m1029a() {
        this.a.m866a(CommandDispatcher.GET_RECEIPTS, new ReceiptsResponse(m717b(), Status.NOT_SUPPORTED));
    }

    protected void onSuccess(SuccessResult successResult) throws RemoteException {
        ShoppingServiceResponse receiptsResponse;
        aa.m693b(f1393b, "onSuccess");
        ShoppingServiceResponse receiptsResponse2 = new ReceiptsResponse(m717b(), Status.FAILED);
        try {
            Map data = successResult.getData();
            aa.m693b(f1393b, "data: " + data);
            if (data.containsKey("errorMessage")) {
                Log.e(f1393b, (String) data.get("errorMessage"));
            }
            Status valueOf = Status.valueOf((String) data.get("requestStatus"));
            if (valueOf == Status.SUCCESSFUL) {
                String str = (String) data.get("userId");
                String str2 = (String) data.get("deviceId");
                List arrayList = new ArrayList();
                JSONArray jSONArray = new JSONArray((String) data.get("receipts"));
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    try {
                        if (m716a(str, str2, jSONObject)) {
                            arrayList.add(ba.m859b(jSONObject));
                        } else {
                            aa.m691a(f1393b, "receipt verification failed: " + jSONObject);
                        }
                    } catch (Exception e) {
                        aa.m691a(f1393b, "error in parsing a receipt: " + jSONObject);
                    }
                }
                String str3 = (String) data.get("offset");
                receiptsResponse = new ReceiptsResponse(m717b(), valueOf, arrayList, Offset.fromString(str3), Boolean.parseBoolean((String) data.get("hasMore")));
            } else {
                receiptsResponse = new ReceiptsResponse(m717b(), valueOf);
            }
        } catch (Exception e2) {
            aa.m691a(f1393b, "error in onSuccess: " + e2);
            receiptsResponse = receiptsResponse2;
        }
        this.a.m866a(CommandDispatcher.GET_RECEIPTS, receiptsResponse);
    }
}
