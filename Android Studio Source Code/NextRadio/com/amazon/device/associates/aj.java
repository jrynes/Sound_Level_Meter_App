package com.amazon.device.associates;

import android.content.Intent;
import com.amazon.device.associates.PurchaseResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;

/* compiled from: DASRequestHandler */
final class aj implements aw {
    private static final String f1182a;

    aj() {
    }

    static {
        f1182a = aj.class.getSimpleName();
    }

    private void m737a(RequestId requestId, String str, bb bbVar, PopoverStatus popoverStatus) {
        Intent intent = new Intent(str);
        intent.putExtra("requestId", requestId.toString());
        if (popoverStatus != null) {
            intent.putExtra("Povover-Status", popoverStatus.f1387d);
        }
        DASResponseReceiver dASResponseReceiver = new DASResponseReceiver();
        Log.m1018c(f1182a, "Intent action sending for DAS response receiver:" + intent.getAction());
        dASResponseReceiver.m956a(intent, bbVar);
    }

    public void m742a(RequestId requestId, bb bbVar) {
        Log.m1018c(f1182a, "sendGetServiceStatusRequest for requestId:" + requestId);
        m737a(requestId, "com.amazon.device.iap.physical.get_serviceStatus", bbVar, null);
    }

    public void m738a(RequestId requestId, PurchaseRequest purchaseRequest, bb bbVar) {
        Log.m1018c(f1182a, "sendPurchaseRequest for request:" + purchaseRequest.toString());
        if ((purchaseRequest.getPurchaseExperience() == null || purchaseRequest.getPurchaseExperience() == PurchaseExperience.DIRECT_WITH_DETAIL || purchaseRequest.getPurchaseExperience() == PurchaseExperience.DIRECT_WITH_PREVIEW) && !purchaseRequest.getReceiveReceipt()) {
            PopoverStatus a;
            af a2 = af.m719a(bp.m899a(), bp.m901b(), null);
            OpenProductPageRequest openProductPageRequest = new OpenProductPageRequest(purchaseRequest.getProductId());
            if (PurchaseExperience.DIRECT_WITH_PREVIEW.equals(purchaseRequest.getPurchaseExperience())) {
                Log.m1018c(f1182a, "Calling showProductPreview for productID:" + openProductPageRequest.getProductId());
                a = a2.m722a(purchaseRequest.getOriginView(), openProductPageRequest, requestId);
            } else {
                Log.m1018c(f1182a, "Calling showProductDetail for productID:" + openProductPageRequest.getProductId());
                a = a2.m725b(purchaseRequest.getOriginView(), openProductPageRequest, requestId);
            }
            if (a != null) {
                m737a(requestId, "com.amazon.device.iap.physical.physical_purchase", bbVar, a);
            }
            DASBroadcastReceiver.m651a(bbVar);
            return;
        }
        bbVar.m866a(CommandDispatcher.PURCHASE, new PurchaseResponse(requestId, Status.NOT_SUPPORTED));
    }

    public void m739a(RequestId requestId, ReceiptsRequest receiptsRequest, bb bbVar) {
        bbVar.m866a(CommandDispatcher.GET_RECEIPTS, new ReceiptsResponse(requestId, ReceiptsResponse.Status.NOT_SUPPORTED));
    }

    public void m741a(RequestId requestId, SearchRequest searchRequest, bb bbVar) {
        bbVar.m866a(CommandDispatcher.SEARCH, new SearchResponse(requestId, SearchResponse.Status.NOT_SUPPORTED));
    }

    public void m740a(RequestId requestId, SearchByIdRequest searchByIdRequest, bb bbVar) {
        bbVar.m866a(CommandDispatcher.SEARCH_BY_ID, new SearchByIdResponse(requestId, SearchByIdResponse.Status.NOT_SUPPORTED));
    }

    public void m743b(RequestId requestId, bb bbVar) {
    }

    public void m744c(RequestId requestId, bb bbVar) {
    }
}
