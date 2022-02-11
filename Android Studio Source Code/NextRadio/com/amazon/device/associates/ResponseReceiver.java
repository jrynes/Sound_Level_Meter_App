package com.amazon.device.associates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class ResponseReceiver extends BroadcastReceiver {
    private static final String f1130a;

    static {
        f1130a = ResponseReceiver.class.getName();
    }

    public void onReceive(Context context, Intent intent) {
        try {
            ShoppingServiceImpl shoppingServiceImpl = (ShoppingServiceImpl) AssociatesAPI.getShoppingService();
            if (shoppingServiceImpl != null) {
                shoppingServiceImpl.m993a(context, intent);
            }
        } catch (Exception e) {
            aa.m691a(f1130a, "Error in handling broadcast: " + e);
        }
    }
}
