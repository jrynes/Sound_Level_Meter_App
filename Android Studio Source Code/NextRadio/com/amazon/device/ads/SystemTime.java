package com.amazon.device.ads;

class SystemTime {
    SystemTime() {
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public long nanoTime() {
        return System.nanoTime();
    }
}
