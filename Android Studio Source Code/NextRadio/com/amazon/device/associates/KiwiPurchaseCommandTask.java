package com.amazon.device.associates;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.amazon.android.framework.context.ContextManager;
import com.amazon.android.framework.resource.Resource;
import com.amazon.android.framework.task.Task;
import com.amazon.android.framework.task.TaskManager;
import com.amazon.android.framework.task.pipeline.TaskPipelineId;
import com.amazon.device.associates.PurchaseResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;
import com.amazon.venezia.command.SuccessResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.amazon.device.associates.s */
final class KiwiPurchaseCommandTask extends as {
    private static final String f1380b;
    @Resource
    private ContextManager f1381c;
    @Resource
    private TaskManager f1382d;

    /* renamed from: com.amazon.device.associates.s.1 */
    class KiwiPurchaseCommandTask implements Task {
        final /* synthetic */ Intent f1378a;
        final /* synthetic */ KiwiPurchaseCommandTask f1379b;

        KiwiPurchaseCommandTask(KiwiPurchaseCommandTask kiwiPurchaseCommandTask, Intent intent) {
            this.f1379b = kiwiPurchaseCommandTask;
            this.f1378a = intent;
        }

        public void execute() {
            try {
                Activity visible = this.f1379b.f1381c.getVisible();
                if (visible == null) {
                    visible = this.f1379b.f1381c.getRoot();
                }
                aa.m693b(KiwiPurchaseCommandTask.f1380b, "About to fire intent with activity " + visible);
                visible.startActivity(this.f1378a);
            } catch (Exception e) {
                aa.m693b(KiwiPurchaseCommandTask.f1380b, "Exception when attempting to fire intent: " + e);
            }
        }
    }

    static {
        f1380b = KiwiPurchaseCommandTask.class.getSimpleName();
    }

    KiwiPurchaseCommandTask(RequestId requestId, PurchaseRequest purchaseRequest, bb bbVar) {
        super("physical_purchase", Stomp.V1_0, requestId, bbVar);
        List arrayList = new ArrayList();
        arrayList.add(purchaseRequest.getProductId());
        m713a("productIds", (Object) arrayList);
        m713a("receiveReceipt", (Object) Boolean.valueOf(purchaseRequest.getReceiveReceipt()));
        m715a(true);
    }

    protected void m1025a() {
        this.a.m866a(CommandDispatcher.PURCHASE, new PurchaseResponse(m717b(), Status.FAILED));
    }

    protected void onSuccess(SuccessResult successResult) {
        ShoppingServiceResponse shoppingServiceResponse;
        aa.m693b(f1380b, "onSuccess");
        try {
            Map data = successResult.getData();
            aa.m693b(f1380b, "data: " + data);
            if (data.containsKey("errorMessage")) {
                Log.e(f1380b, (String) data.get("errorMessage"));
            }
            Status valueOf = Status.valueOf((String) data.get("requestStatus"));
            if (valueOf == Status.SUCCESSFUL) {
                Intent intent = (Intent) data.get("purchaseIntent");
                if (intent == null) {
                    throw new IllegalArgumentException("purchaseIntent is null.");
                }
                this.f1382d.enqueueAtFront(TaskPipelineId.FOREGROUND, new KiwiPurchaseCommandTask(this, intent));
                shoppingServiceResponse = null;
            } else {
                shoppingServiceResponse = new PurchaseResponse(m717b(), valueOf);
            }
        } catch (Exception e) {
            aa.m691a(f1380b, "error in onSuccess: " + e.getMessage());
            shoppingServiceResponse = new PurchaseResponse(m717b(), Status.FAILED);
        }
        if (shoppingServiceResponse != null) {
            this.a.m866a(CommandDispatcher.PURCHASE, shoppingServiceResponse);
        }
    }
}
