package com.amazon.device.associates;

import com.amazon.device.associates.PurchaseResponse.Status;

/* renamed from: com.amazon.device.associates.t */
enum PopoverStatus {
    SUCCESS("success"),
    FAILED("failed"),
    INVALID_PRODUCT_ID("invalid_product_id");
    
    final String f1387d;

    private PopoverStatus(String str) {
        this.f1387d = str;
    }

    public static Status m1026a(String str) {
        if (str == null) {
            return null;
        }
        if (str.equals(SUCCESS.f1387d)) {
            return Status.SUCCESSFUL;
        }
        if (str.equals(FAILED.f1387d)) {
            return Status.FAILED;
        }
        if (str.equals(INVALID_PRODUCT_ID.f1387d)) {
            return Status.INVALID_ID;
        }
        return null;
    }
}
