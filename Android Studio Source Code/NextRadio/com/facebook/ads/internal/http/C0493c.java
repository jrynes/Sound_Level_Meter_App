package com.facebook.ads.internal.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import io.fabric.sdk.android.services.network.HttpRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.http.c */
public class C0493c {
    private Handler f1826a;

    /* renamed from: com.facebook.ads.internal.http.c.1 */
    class C04921 extends Handler {
        final /* synthetic */ C0493c f1825a;

        C04921(C0493c c0493c) {
            this.f1825a = c0493c;
        }

        public void handleMessage(Message message) {
            this.f1825a.m1446a(message);
        }
    }

    public C0493c() {
        if (Looper.myLooper() != null) {
            this.f1826a = new C04921(this);
        }
    }

    protected Message m1443a(int i, Object obj) {
        if (this.f1826a != null) {
            return this.f1826a.obtainMessage(i, obj);
        }
        Message obtain = Message.obtain();
        obtain.what = i;
        obtain.obj = obj;
        return obtain;
    }

    public void m1444a() {
    }

    public void m1445a(int i, String str) {
        m1447a(str);
    }

    protected void m1446a(Message message) {
        Object[] objArr;
        switch (message.what) {
            case Tokenizer.EOF /*0*/:
                objArr = (Object[]) message.obj;
                m1456c(((Integer) objArr[0]).intValue(), (String) objArr[1]);
            case Zone.PRIMARY /*1*/:
                objArr = (Object[]) message.obj;
                m1457c((Throwable) objArr[0], (String) objArr[1]);
            case Zone.SECONDARY /*2*/:
                m1444a();
            case Protocol.GGP /*3*/:
                m1451b();
            default:
        }
    }

    public void m1447a(String str) {
    }

    public void m1448a(Throwable th) {
    }

    public void m1449a(Throwable th, String str) {
        m1448a(th);
    }

    void m1450a(HttpResponse httpResponse) {
        String str = null;
        StatusLine statusLine = httpResponse.getStatusLine();
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                str = EntityUtils.toString(new BufferedHttpEntity(entity), HttpRequest.CHARSET_UTF8);
            }
        } catch (Throwable e) {
            m1454b(e, null);
        }
        if (statusLine.getStatusCode() >= 300) {
            m1454b(new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase()), str);
        } else {
            m1452b(statusLine.getStatusCode(), str);
        }
    }

    public void m1451b() {
    }

    protected void m1452b(int i, String str) {
        m1453b(m1443a(0, new Object[]{Integer.valueOf(i), str}));
    }

    protected void m1453b(Message message) {
        if (this.f1826a != null) {
            this.f1826a.sendMessage(message);
        } else {
            m1446a(message);
        }
    }

    protected void m1454b(Throwable th, String str) {
        m1453b(m1443a(1, new Object[]{th, str}));
    }

    protected void m1455c() {
        m1453b(m1443a(2, null));
    }

    protected void m1456c(int i, String str) {
        m1445a(i, str);
    }

    protected void m1457c(Throwable th, String str) {
        m1449a(th, str);
    }

    protected void m1458d() {
        m1453b(m1443a(3, null));
    }
}
