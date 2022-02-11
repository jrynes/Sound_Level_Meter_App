package com.amazon.device.associates;

import java.io.Serializable;

/* compiled from: AbstractTemplates */
abstract class bl implements Serializable {
    private String f1270a;
    protected long f1271j;
    protected long f1272k;
    protected long f1273l;

    bl() {
        this.f1271j = System.currentTimeMillis();
        this.f1272k = -1;
        this.f1273l = 0;
    }

    public void m888a(String str) {
        this.f1270a = str;
    }

    protected long m889b() {
        return this.f1273l;
    }

    protected void m887a(long j) {
        this.f1273l = j;
    }

    protected long m891c() {
        return this.f1271j;
    }

    protected void m890b(long j) {
        this.f1271j = j;
    }

    protected long m893d() {
        return this.f1272k;
    }

    protected void m892c(long j) {
        this.f1272k = j;
    }
}
