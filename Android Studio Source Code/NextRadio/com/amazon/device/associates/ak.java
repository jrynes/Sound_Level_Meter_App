package com.amazon.device.associates;

import android.content.Context;
import com.amazon.android.Kiwi;
import com.amazon.device.associates.PurchaseResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;

/* compiled from: KiwiRequestHandler */
final class ak implements aw {
    private static final String f1183a;
    private static bb f1184b;

    static {
        f1183a = ak.class.getSimpleName();
    }

    ak() {
    }

    public void m749a(RequestId requestId, bb bbVar) {
        aa.m693b(f1183a, "sendGetUserDataRequest");
        f1184b = bbVar;
        Context b = bbVar.m867b();
        as kiwiUserDataCommandTask = new KiwiUserDataCommandTask(requestId, bbVar);
        aa.m693b(f1183a, "data: " + kiwiUserDataCommandTask.getCommandData());
        Kiwi.addCommandToCommandTaskPipeline(kiwiUserDataCommandTask, b);
    }

    public void m746a(RequestId requestId, ReceiptsRequest receiptsRequest, bb bbVar) {
        aa.m693b(f1183a, "sendGetReceiptsRequest");
        f1184b = bbVar;
        Context b = bbVar.m867b();
        as kiwiReceiptsCommandTask = new KiwiReceiptsCommandTask(requestId, receiptsRequest, bbVar);
        aa.m693b(f1183a, "data: " + kiwiReceiptsCommandTask.getCommandData());
        Kiwi.addCommandToCommandTaskPipeline(kiwiReceiptsCommandTask, b);
    }

    public void m748a(RequestId requestId, SearchRequest searchRequest, bb bbVar) {
        aa.m693b(f1183a, "sendSearchRequest");
        f1184b = bbVar;
        Context b = bbVar.m867b();
        as kiwiSearchCommandTask = new KiwiSearchCommandTask(requestId, searchRequest, bbVar);
        aa.m693b(f1183a, "data: " + kiwiSearchCommandTask.getCommandData());
        Kiwi.addCommandToCommandTaskPipeline(kiwiSearchCommandTask, b);
    }

    public void m747a(RequestId requestId, SearchByIdRequest searchByIdRequest, bb bbVar) {
        aa.m693b(f1183a, "sendSearchByIdRequest");
        f1184b = bbVar;
        Context b = bbVar.m867b();
        as biVar = new bi(requestId, searchByIdRequest, bbVar);
        aa.m693b(f1183a, "data: " + biVar.getCommandData());
        Kiwi.addCommandToCommandTaskPipeline(biVar, b);
    }

    public void m745a(RequestId requestId, PurchaseRequest purchaseRequest, bb bbVar) {
        aa.m693b(f1183a, "sendPurchaseRequest");
        f1184b = bbVar;
        if (purchaseRequest.getPurchaseExperience() == null || purchaseRequest.getPurchaseExperience() == PurchaseExperience.IN_APP) {
            Context b = bbVar.m867b();
            KiwiPurchaseCommandTask kiwiPurchaseCommandTask = new KiwiPurchaseCommandTask(requestId, purchaseRequest, bbVar);
            aa.m693b(f1183a, "data: " + kiwiPurchaseCommandTask.getCommandData());
            Kiwi.addCommandToCommandTaskPipeline(kiwiPurchaseCommandTask, b);
            return;
        }
        f1184b.m866a(CommandDispatcher.PURCHASE, new PurchaseResponse(requestId, Status.NOT_SUPPORTED));
    }

    public void m750b(RequestId requestId, bb bbVar) {
        aa.m693b(f1183a, "sendGetPurchaseResultRequest");
        f1184b = bbVar;
        Context b = bbVar.m867b();
        as btVar = new bt(requestId, bbVar);
        aa.m693b(f1183a, "data: " + btVar.getCommandData());
        Kiwi.addCommandToCommandTaskPipeline(btVar, b);
    }

    public void m751c(RequestId requestId, bb bbVar) {
        aa.m693b(f1183a, "sendNotifyReceiptReceivedRequest");
        f1184b = bbVar;
        Context b = bbVar.m867b();
        as aeVar = new ae(requestId, bbVar);
        aa.m693b(f1183a, "data: " + aeVar.getCommandData());
        Kiwi.addCommandToCommandTaskPipeline(aeVar, b);
    }
}
