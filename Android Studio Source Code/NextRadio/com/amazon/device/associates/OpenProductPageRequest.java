package com.amazon.device.associates;

public class OpenProductPageRequest extends OpenRetailPageRequest {
    private String f1081a;

    public OpenProductPageRequest(String str) {
        ar.m782a((Object) str, "productId");
        this.f1081a = str;
    }

    public String getProductId() {
        return this.f1081a;
    }
}
