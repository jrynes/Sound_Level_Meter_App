package com.amazon.device.associates;

public abstract class ShoppingServiceResponse {
    private final RequestId f1113a;

    protected abstract boolean m664a();

    ShoppingServiceResponse(RequestId requestId) {
        ar.m782a((Object) requestId, "requestId");
        this.f1113a = requestId;
    }

    public RequestId getRequestId() {
        return this.f1113a;
    }
}
