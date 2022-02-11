package com.amazon.device.associates;

import android.content.Context;

/* compiled from: CommandDispatcher */
interface bb {

    /* renamed from: com.amazon.device.associates.bb.a */
    public enum CommandDispatcher {
        GET_SERVICE_STATUS,
        GET_RECEIPTS,
        SEARCH,
        SEARCH_BY_ID,
        PURCHASE,
        GET_PURCHASE_RESULT,
        NOTIFY_RECEIPT_RECEIVED
    }

    RequestId m864a(CommandDispatcher commandDispatcher, RequestId requestId, ShoppingServiceRequest shoppingServiceRequest);

    String m865a();

    void m866a(CommandDispatcher commandDispatcher, ShoppingServiceResponse shoppingServiceResponse);

    Context m867b();
}
