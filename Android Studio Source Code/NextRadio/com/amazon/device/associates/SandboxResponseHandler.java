package com.amazon.device.associates;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.amazon.device.associates.ReceiptsResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp.Headers.Error;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.amazon.device.associates.a */
final class SandboxResponseHandler implements bj {
    private static final String f1155a;
    private bb f1156b;

    /* renamed from: com.amazon.device.associates.a.1 */
    class SandboxResponseHandler implements Runnable {
        final /* synthetic */ Intent f1153a;
        final /* synthetic */ SandboxResponseHandler f1154b;

        SandboxResponseHandler(SandboxResponseHandler sandboxResponseHandler, Intent intent) {
            this.f1154b = sandboxResponseHandler;
            this.f1153a = intent;
        }

        public void run() {
            this.f1154b.f1156b.m867b().startActivity(this.f1153a);
        }
    }

    SandboxResponseHandler() {
    }

    static {
        f1155a = SandboxResponseHandler.class.getSimpleName();
    }

    public void m689a(Context context, Intent intent, bb bbVar) {
        aa.m693b(f1155a, "handleResponse");
        this.f1156b = bbVar;
        try {
            JSONObject jSONObject = new JSONObject(intent.getExtras().getString(Error.MESSAGE));
            aa.m693b(f1155a, "message: " + jSONObject.toString(4));
            if (jSONObject.has("errorMessage")) {
                Log.e(f1155a, jSONObject.optString("errorMessage"));
            }
            String string = jSONObject.getString("command");
            if (string.equalsIgnoreCase("com.amazon.device.iap.physical.get_userData")) {
                m683a(intent, jSONObject);
            } else if (string.equalsIgnoreCase("com.amazon.device.iap.physical.physical_get_receipts")) {
                m684b(intent, jSONObject);
            } else if (string.equalsIgnoreCase("com.amazon.device.iap.physical.physical_search")) {
                m685c(intent, jSONObject);
            } else if (string.equalsIgnoreCase("com.amazon.device.iap.physical.physical_searchById")) {
                m686d(intent, jSONObject);
            } else if (string.equalsIgnoreCase("com.amazon.device.iap.physical.physical_purchase")) {
                m687e(intent, jSONObject);
            } else if (string.equalsIgnoreCase("com.amazon.device.iap.physical.physical_get_purchaseResult")) {
                m688f(intent, jSONObject);
            } else if (string.equalsIgnoreCase("com.amazon.device.iap.physical.physical_purchase_done")) {
                bbVar.m864a(CommandDispatcher.GET_PURCHASE_RESULT, RequestId.m671a(jSONObject.getString("requestId")), null);
            } else if (!string.equalsIgnoreCase("com.amazon.device.iap.physical.physical_notify_receiptReceived")) {
                aa.m691a(f1155a, "response with unknown command: " + string);
            }
        } catch (Exception e) {
            aa.m691a(f1155a, "error in handleResponse: " + e);
        }
    }

    private void m683a(Intent intent, JSONObject jSONObject) throws JSONException {
        ShoppingServiceResponse serviceStatusResponse;
        boolean z = true;
        boolean z2 = false;
        Set hashSet = new HashSet();
        hashSet.add(PurchaseExperience.DIRECT_WITH_DETAIL);
        hashSet.add(PurchaseExperience.DIRECT_WITH_PREVIEW);
        RequestId a = RequestId.m671a(jSONObject.getString("requestId"));
        if (jSONObject.getString("requestStatus").equals("SUCCESSFUL")) {
            UserData a2 = ba.m857a(jSONObject);
            if (((Set) ba.m858a(jSONObject.getJSONArray("availableProductTypes"), new HashSet())).contains("PHYSICAL")) {
                hashSet.add(PurchaseExperience.IN_APP);
                z2 = true;
            } else {
                z = false;
            }
            serviceStatusResponse = new ServiceStatusResponse(a, a2, hashSet, z2, z);
        } else {
            serviceStatusResponse = new ServiceStatusResponse(a, null, hashSet, false, false);
        }
        this.f1156b.m866a(CommandDispatcher.GET_SERVICE_STATUS, serviceStatusResponse);
    }

    private void m684b(Intent intent, JSONObject jSONObject) throws JSONException {
        ShoppingServiceResponse receiptsResponse;
        RequestId a = RequestId.m671a(jSONObject.getString("requestId"));
        Status valueOf = Status.valueOf(jSONObject.getString("requestStatus"));
        if (valueOf == Status.SUCCESSFUL) {
            receiptsResponse = new ReceiptsResponse(a, valueOf, (List) ba.m860b(jSONObject.getJSONArray("receipts"), new ArrayList()), Offset.fromString(jSONObject.getString("offset")), jSONObject.optBoolean("hasMore"));
        } else {
            receiptsResponse = new ReceiptsResponse(a, valueOf);
        }
        this.f1156b.m866a(CommandDispatcher.GET_RECEIPTS, receiptsResponse);
    }

    private void m685c(Intent intent, JSONObject jSONObject) throws JSONException {
        ShoppingServiceResponse searchResponse;
        RequestId a = RequestId.m671a(jSONObject.getString("requestId"));
        SearchResponse.Status valueOf = SearchResponse.Status.valueOf(jSONObject.getString("requestStatus"));
        if (valueOf == SearchResponse.Status.SUCCESSFUL) {
            searchResponse = new SearchResponse(a, valueOf, (List) ba.m862c(jSONObject.getJSONArray("products"), new ArrayList()), jSONObject.getInt("page"), jSONObject.getInt("totalPages"));
        } else {
            searchResponse = new SearchResponse(a, valueOf);
        }
        this.f1156b.m866a(CommandDispatcher.SEARCH, searchResponse);
    }

    private void m686d(Intent intent, JSONObject jSONObject) throws JSONException {
        ShoppingServiceResponse searchByIdResponse;
        RequestId a = RequestId.m671a(jSONObject.getString("requestId"));
        SearchByIdResponse.Status valueOf = SearchByIdResponse.Status.valueOf(jSONObject.getString("requestStatus"));
        if (valueOf == SearchByIdResponse.Status.SUCCESSFUL) {
            searchByIdResponse = new SearchByIdResponse(a, valueOf, (List) ba.m862c(jSONObject.getJSONArray("products"), new ArrayList()), (Set) ba.m858a(jSONObject.getJSONArray("unavailableProductIds"), new HashSet()));
        } else {
            searchByIdResponse = new SearchByIdResponse(a, valueOf);
        }
        this.f1156b.m866a(CommandDispatcher.SEARCH_BY_ID, searchByIdResponse);
    }

    private void m687e(Intent intent, JSONObject jSONObject) throws JSONException {
        RequestId a = RequestId.m671a(jSONObject.getString("requestId"));
        PurchaseResponse.Status valueOf = PurchaseResponse.Status.valueOf(jSONObject.getString("requestStatus"));
        if (valueOf == PurchaseResponse.Status.SUCCESSFUL) {
            bf.m871a().post(new SandboxResponseHandler(this, (Intent) intent.getExtras().get("intent")));
            return;
        }
        this.f1156b.m866a(CommandDispatcher.PURCHASE, new PurchaseResponse(a, valueOf));
    }

    private void m688f(Intent intent, JSONObject jSONObject) throws JSONException, ParseException {
        ShoppingServiceResponse purchaseResponse;
        RequestId a = RequestId.m671a(jSONObject.getString("requestId"));
        PurchaseResponse.Status valueOf = PurchaseResponse.Status.valueOf(jSONObject.getString("requestStatus"));
        if (valueOf == PurchaseResponse.Status.SUCCESSFUL) {
            Object arrayList = new ArrayList();
            if (jSONObject.has("receipts") && !jSONObject.isNull("receipts")) {
                ba.m860b(jSONObject.getJSONArray("receipts"), arrayList);
            }
            purchaseResponse = new PurchaseResponse(a, valueOf, arrayList);
        } else {
            purchaseResponse = new PurchaseResponse(a, valueOf);
        }
        this.f1156b.m866a(CommandDispatcher.PURCHASE, purchaseResponse);
    }
}
