package com.amazon.device.associates;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import com.amazon.device.associates.ReceiptsResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp.Headers.Error;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: SandboxRequestHandler */
final class at implements aw {
    private static final String f1191a;
    private bb f1192b;
    private final Set<PurchaseExperience> f1193c;
    private boolean f1194d;
    private boolean f1195e;

    static {
        f1191a = at.class.getSimpleName();
    }

    at() {
        this.f1193c = new HashSet();
        this.f1194d = false;
        this.f1195e = false;
        this.f1193c.add(PurchaseExperience.DIRECT_WITH_DETAIL);
        this.f1193c.add(PurchaseExperience.DIRECT_WITH_PREVIEW);
    }

    private void m784a(RequestId requestId, String str, JSONObject jSONObject) throws JSONException {
        Intent intent = new Intent(str);
        Context b = this.f1192b.m867b();
        Bundle bundle = new Bundle();
        jSONObject.put("sdkVersion", this.f1192b.m865a());
        jSONObject.put("packageName", b.getPackageName());
        jSONObject.put("requestId", requestId.toString());
        bundle.putString(Error.MESSAGE, jSONObject.toString());
        aa.m693b(f1191a, "action: " + str + ", message: " + jSONObject.toString(4));
        intent.addFlags(268435456);
        intent.putExtras(bundle);
        b.startService(intent);
    }

    private boolean m785a() {
        for (PackageInfo packageInfo : this.f1192b.m867b().getPackageManager().getInstalledPackages(0)) {
            if (packageInfo.packageName.equals("com.amazon.sdktestclient")) {
                return true;
            }
        }
        return false;
    }

    public void m790a(RequestId requestId, bb bbVar) {
        aa.m693b(f1191a, "sendGetServiceStatusRequest");
        this.f1192b = bbVar;
        if (m785a()) {
            try {
                m784a(requestId, "com.amazon.device.iap.physical.get_userData", new JSONObject());
                return;
            } catch (JSONException e) {
                aa.m691a(f1191a, "Error in sendGetServiceStatusRequest.");
                return;
            }
        }
        bbVar.m866a(CommandDispatcher.GET_SERVICE_STATUS, new ServiceStatusResponse(requestId, null, this.f1193c, this.f1194d, this.f1195e));
    }

    public void m787a(RequestId requestId, ReceiptsRequest receiptsRequest, bb bbVar) {
        aa.m693b(f1191a, "sendGetReceiptsRequest");
        this.f1192b = bbVar;
        if (m785a()) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("offset", receiptsRequest.getOffset());
                m784a(requestId, "com.amazon.device.iap.physical.physical_get_receipts", jSONObject);
                return;
            } catch (JSONException e) {
                aa.m691a(f1191a, "Error in sendGetReceiptsRequest.");
                return;
            }
        }
        bbVar.m866a(CommandDispatcher.GET_RECEIPTS, new ReceiptsResponse(requestId, Status.NOT_SUPPORTED));
    }

    public void m789a(RequestId requestId, SearchRequest searchRequest, bb bbVar) {
        aa.m693b(f1191a, "sendSearchRequest");
        this.f1192b = bbVar;
        if (m785a()) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("category", searchRequest.getCategory().toString());
                jSONObject.put("searchTerm", searchRequest.getSearchTerm());
                Map filters = searchRequest.getFilters();
                JSONObject jSONObject2 = new JSONObject();
                for (FilterType filterType : filters.keySet()) {
                    jSONObject2.put(filterType.toString(), filters.get(filterType));
                }
                jSONObject.put("filters", jSONObject2);
                jSONObject.put("sortType", searchRequest.getSortType().toString());
                jSONObject.put("page", searchRequest.getPage());
                m784a(requestId, "com.amazon.device.iap.physical.physical_search", jSONObject);
                return;
            } catch (JSONException e) {
                aa.m691a(f1191a, "Error in sendSearchRequest.");
                return;
            }
        }
        bbVar.m866a(CommandDispatcher.SEARCH, new SearchResponse(requestId, SearchResponse.Status.NOT_SUPPORTED));
    }

    public void m788a(RequestId requestId, SearchByIdRequest searchByIdRequest, bb bbVar) {
        aa.m693b(f1191a, "sendSearchByIdRequest");
        this.f1192b = bbVar;
        if (m785a()) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("productType", "PHYSICAL");
                jSONObject.put("productIds", new JSONArray(searchByIdRequest.getProductIds()));
                m784a(requestId, "com.amazon.device.iap.physical.physical_searchById", jSONObject);
                return;
            } catch (JSONException e) {
                aa.m691a(f1191a, "Error in sendSearchByIdRequest.");
                return;
            }
        }
        bbVar.m866a(CommandDispatcher.SEARCH_BY_ID, new SearchByIdResponse(requestId, SearchByIdResponse.Status.NOT_SUPPORTED));
    }

    public void m786a(RequestId requestId, PurchaseRequest purchaseRequest, bb bbVar) {
        aa.m693b(f1191a, "sendPurchaseRequest");
        this.f1192b = bbVar;
        if (m785a() && (purchaseRequest.getPurchaseExperience() == null || purchaseRequest.getPurchaseExperience() == PurchaseExperience.IN_APP)) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("productType", "PHYSICAL");
                jSONObject.put("productIds", new JSONArray("[\"" + purchaseRequest.getProductId() + "\"]"));
                jSONObject.put("receiveReceipt", purchaseRequest.getReceiveReceipt());
                m784a(requestId, "com.amazon.device.iap.physical.physical_purchase", jSONObject);
                return;
            } catch (JSONException e) {
                aa.m691a(f1191a, "Error in sendPurchaseRequest.");
                return;
            }
        }
        bbVar.m866a(CommandDispatcher.PURCHASE, new PurchaseResponse(requestId, PurchaseResponse.Status.NOT_SUPPORTED));
    }

    public void m791b(RequestId requestId, bb bbVar) {
        aa.m693b(f1191a, "sendPurchaseResponseRequest");
        this.f1192b = bbVar;
        try {
            m784a(requestId, "com.amazon.device.iap.physical.physical_get_purchaseResult", new JSONObject());
        } catch (JSONException e) {
            aa.m691a(f1191a, "Error in sendPurchaseResponseRequest.");
        }
    }

    public void m792c(RequestId requestId, bb bbVar) {
        aa.m693b(f1191a, "sendReceiptReceivedRequest");
        this.f1192b = bbVar;
        try {
            m784a(requestId, "com.amazon.device.iap.physical.physical_notify_receiptReceived", new JSONObject());
        } catch (JSONException e) {
            aa.m691a(f1191a, "Error in sendReceiptReceivedRequest.");
        }
    }
}
