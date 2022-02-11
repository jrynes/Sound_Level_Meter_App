package com.amazon.device.associates;

import android.os.RemoteException;
import android.util.Log;
import com.amazon.device.associates.SearchByIdResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;
import com.amazon.venezia.command.SuccessResult;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONException;

/* compiled from: KiwiSearchByIdCommandTask */
final class bi extends as {
    private static final String f1268b;

    static {
        f1268b = bi.class.getSimpleName();
    }

    bi(RequestId requestId, SearchByIdRequest searchByIdRequest, bb bbVar) {
        super("physical_searchById", Stomp.V1_0, requestId, bbVar);
        m713a("productIds", (Object) searchByIdRequest.getProductIds());
        m715a(false);
    }

    protected void m883a() {
        this.a.m866a(CommandDispatcher.SEARCH_BY_ID, new SearchByIdResponse(m717b(), Status.NOT_SUPPORTED));
    }

    protected void onSuccess(SuccessResult successResult) throws RemoteException {
        aa.m693b(f1268b, "onSuccess");
        ShoppingServiceResponse searchByIdResponse = new SearchByIdResponse(m717b(), Status.FAILED);
        ShoppingServiceResponse searchByIdResponse2;
        try {
            Map data = successResult.getData();
            aa.m693b(f1268b, "data: " + data);
            if (data.containsKey("errorMessage")) {
                Log.e(f1268b, (String) data.get("errorMessage"));
            }
            Status valueOf = Status.valueOf((String) data.get("requestStatus"));
            if (valueOf == Status.SUCCESSFUL) {
                String str = (String) data.get("userId");
                Object arrayList = new ArrayList();
                try {
                    ba.m862c(new JSONArray((String) data.get("products")), arrayList);
                } catch (JSONException e) {
                    aa.m691a(f1268b, "Error parsing JSON for products: " + e.getMessage());
                }
                searchByIdResponse2 = new SearchByIdResponse(m717b(), valueOf, arrayList, (Set) data.get("unavailableProductIds"));
                this.a.m866a(CommandDispatcher.SEARCH_BY_ID, searchByIdResponse2);
            }
            searchByIdResponse2 = new SearchByIdResponse(m717b(), valueOf);
            this.a.m866a(CommandDispatcher.SEARCH_BY_ID, searchByIdResponse2);
        } catch (Exception e2) {
            aa.m691a(f1268b, "error in onSuccess: " + e2.getMessage());
            searchByIdResponse2 = searchByIdResponse;
        }
    }
}
