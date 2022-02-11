package com.facebook.ads.internal.http;

import java.io.IOException;
import java.net.ConnectException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

/* renamed from: com.facebook.ads.internal.http.b */
class C0491b implements Runnable {
    private final AbstractHttpClient f1820a;
    private final HttpContext f1821b;
    private final HttpUriRequest f1822c;
    private final C0493c f1823d;
    private int f1824e;

    public C0491b(AbstractHttpClient abstractHttpClient, HttpContext httpContext, HttpUriRequest httpUriRequest, C0493c c0493c) {
        this.f1820a = abstractHttpClient;
        this.f1821b = httpContext;
        this.f1822c = httpUriRequest;
        this.f1823d = c0493c;
    }

    private void m1441a() {
        if (!Thread.currentThread().isInterrupted()) {
            HttpResponse execute = this.f1820a.execute(this.f1822c, this.f1821b);
            if (!Thread.currentThread().isInterrupted() && this.f1823d != null) {
                this.f1823d.m1450a(execute);
            }
        }
    }

    private void m1442b() {
        int i;
        boolean z = true;
        Throwable th = null;
        HttpRequestRetryHandler httpRequestRetryHandler = this.f1820a.getHttpRequestRetryHandler();
        while (z) {
            try {
                m1441a();
                return;
            } catch (Throwable th2) {
                if (this.f1823d != null) {
                    this.f1823d.m1454b(th2, "can't resolve host");
                    return;
                }
                return;
            } catch (Throwable th22) {
                if (this.f1823d != null) {
                    this.f1823d.m1454b(th22, "can't resolve host");
                    return;
                }
                return;
            } catch (Throwable th222) {
                if (this.f1823d != null) {
                    this.f1823d.m1454b(th222, "socket time out");
                    return;
                }
                return;
            } catch (IOException e) {
                th222 = e;
                i = this.f1824e + 1;
                this.f1824e = i;
                z = httpRequestRetryHandler.retryRequest(th222, i, this.f1821b);
            } catch (NullPointerException e2) {
                th222 = new IOException("NPE in HttpClient" + e2.getMessage());
                i = this.f1824e + 1;
                this.f1824e = i;
                z = httpRequestRetryHandler.retryRequest(th222, i, this.f1821b);
            }
        }
        ConnectException connectException = new ConnectException();
        connectException.initCause(th222);
        throw connectException;
    }

    public void run() {
        try {
            if (this.f1823d != null) {
                this.f1823d.m1455c();
            }
            m1442b();
            if (this.f1823d != null) {
                this.f1823d.m1458d();
            }
        } catch (Throwable e) {
            Throwable th = e;
            if (this.f1823d != null) {
                this.f1823d.m1458d();
                this.f1823d.m1454b(th, (String) null);
            }
        }
    }
}
