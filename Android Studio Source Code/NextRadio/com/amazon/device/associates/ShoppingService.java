package com.amazon.device.associates;

public interface ShoppingService {
    RequestId getReceipts(ReceiptsRequest receiptsRequest) throws NoListenerException;

    RequestId getServiceStatus() throws NoListenerException;

    RequestId purchase(PurchaseRequest purchaseRequest) throws NoListenerException;

    RequestId search(SearchRequest searchRequest) throws NoListenerException;

    RequestId searchById(SearchByIdRequest searchByIdRequest) throws NoListenerException;

    void setListener(ShoppingListener shoppingListener);
}
