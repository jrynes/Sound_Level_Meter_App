package com.amazon.device.associates;

import android.os.RemoteException;
import android.util.Log;
import com.amazon.device.associates.PurchaseResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;
import com.amazon.venezia.command.SuccessResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: KiwiPurchaseResultCommandTask */
final class bt extends as {
    private static final String f1295b;

    static {
        f1295b = bt.class.getSimpleName();
    }

    bt(RequestId requestId, bb bbVar) {
        super("physical_get_purchaseResult", Stomp.V1_0, requestId, bbVar);
        m715a(false);
    }

    protected void m923a() {
        this.a.m866a(CommandDispatcher.PURCHASE, new PurchaseResponse(m717b(), Status.NOT_SUPPORTED));
    }

    protected void onSuccess(SuccessResult successResult) throws RemoteException {
        ShoppingServiceResponse shoppingServiceResponse;
        aa.m693b(f1295b, "onSuccess");
        ShoppingServiceResponse purchaseResponse = new PurchaseResponse(m717b(), Status.FAILED);
        Map data = successResult.getData();
        aa.m693b(f1295b, "data: " + data);
        if (data.containsKey("errorMessage")) {
            Log.e(f1295b, (String) data.get("errorMessage"));
        }
        Status valueOf = Status.valueOf((String) data.get("requestStatus"));
        if (valueOf == Status.SUCCESSFUL) {
            String str = (String) data.get("userId");
            String str2 = (String) data.get("deviceId");
            List arrayList = new ArrayList();
            if (data.containsKey("receipts") && data.get("receipts") != null) {
                JSONArray jSONArray = new JSONArray((String) data.get("receipts"));
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    try {
                        if (m716a(str, str2, jSONObject)) {
                            arrayList.add(ba.m859b(jSONObject));
                        } else {
                            aa.m691a(f1295b, "receipt verification failed: " + jSONObject);
                        }
                    } catch (Exception e) {
                        try {
                            aa.m691a(f1295b, "error in parsing a receipt: " + jSONObject);
                        } catch (Exception e2) {
                            aa.m691a(f1295b, "error in onSuccess: " + e2.getMessage());
                            shoppingServiceResponse = purchaseResponse;
                        }
                    }
                }
            }
            shoppingServiceResponse = new PurchaseResponse(m717b(), valueOf, arrayList);
        } else {
            shoppingServiceResponse = new PurchaseResponse(m717b(), valueOf);
        }
        this.a.m866a(CommandDispatcher.PURCHASE, shoppingServiceResponse);
    }
}
