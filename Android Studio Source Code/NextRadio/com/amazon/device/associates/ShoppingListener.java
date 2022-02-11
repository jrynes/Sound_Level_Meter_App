package com.amazon.device.associates;

public interface ShoppingListener {
    void onPurchaseResponse(PurchaseResponse purchaseResponse);

    void onReceiptsResponse(ReceiptsResponse receiptsResponse);

    void onSearchByIdResponse(SearchByIdResponse searchByIdResponse);

    void onSearchResponse(SearchResponse searchResponse);

    void onServiceStatusResponse(ServiceStatusResponse serviceStatusResponse);
}
