package com.amazon.device.associates;

/* compiled from: AbstractServiceCall */
abstract class ag {
    protected au f1158a;

    protected abstract void m702a();

    protected abstract void m703b() throws Exception;

    protected abstract String m704c();

    ag() {
        this.f1158a = new au();
    }

    public void m705e() {
        m702a();
        long nanoTime = System.nanoTime();
        Log.m1018c("AbstractServiceCall", m704c() + " Started");
        try {
            m703b();
        } catch (Exception e) {
            Log.m1020d("AbstractServiceCall", "Error making MobileAssociates service call.");
            Log.m1013a("AbstractServiceCall", "Service name: " + m704c() + " Ex=" + e);
        }
        Log.m1018c("AbstractServiceCall", m704c() + " Completed");
        Log.m1018c("AbstractServiceCall", m704c() + ".Latency=" + ((System.nanoTime() - nanoTime) / 1000000) + "ms");
    }
}
