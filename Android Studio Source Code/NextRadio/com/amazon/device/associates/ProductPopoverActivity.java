package com.amazon.device.associates;

import android.content.Intent;

/* renamed from: com.amazon.device.associates.v */
class ProductPopoverActivity implements C0350a {
    final /* synthetic */ ProductPopoverActivity f1391a;

    ProductPopoverActivity(ProductPopoverActivity productPopoverActivity) {
        this.f1391a = productPopoverActivity;
    }

    public void m1027a(PopoverStatus popoverStatus) {
        if (!(popoverStatus == null || this.f1391a.f1104g)) {
            Intent intent = new Intent();
            intent.setAction("com.amazon.device.iap.physical.physical_purchase");
            intent.putExtra("Povover-Status", popoverStatus.f1387d);
            intent.putExtra("requestId", this.f1391a.f1100c);
            this.f1391a.sendBroadcast(intent);
            this.f1391a.f1104g = true;
        }
        this.f1391a.finish();
    }
}
