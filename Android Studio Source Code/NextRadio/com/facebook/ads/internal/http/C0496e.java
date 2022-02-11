package com.facebook.ads.internal.http;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

/* renamed from: com.facebook.ads.internal.http.e */
class C0496e implements HttpEntity {
    private static final char[] f1834d;
    ByteArrayOutputStream f1835a;
    boolean f1836b;
    boolean f1837c;
    private String f1838e;

    static {
        f1834d = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    public C0496e() {
        int i = 0;
        this.f1838e = null;
        this.f1835a = new ByteArrayOutputStream();
        this.f1836b = false;
        this.f1837c = false;
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        while (i < 30) {
            stringBuffer.append(f1834d[random.nextInt(f1834d.length)]);
            i++;
        }
        this.f1838e = stringBuffer.toString();
    }

    public void m1464a() {
        if (!this.f1837c) {
            try {
                this.f1835a.write(("--" + this.f1838e + "\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.f1837c = true;
    }

    public void m1465a(String str, String str2) {
        m1464a();
        try {
            this.f1835a.write(("Content-Disposition: form-data; name=\"" + str + "\"\r\n\r\n").getBytes());
            this.f1835a.write(str2.getBytes());
            this.f1835a.write(("\r\n--" + this.f1838e + "\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void m1466a(java.lang.String r5, java.lang.String r6, java.io.InputStream r7, java.lang.String r8, boolean r9) {
        /*
        r4 = this;
        r4.m1464a();
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006e }
        r0.<init>();	 Catch:{ IOException -> 0x006e }
        r1 = "Content-Type: ";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x006e }
        r0 = r0.append(r8);	 Catch:{ IOException -> 0x006e }
        r1 = "\r\n";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x006e }
        r0 = r0.toString();	 Catch:{ IOException -> 0x006e }
        r1 = r4.f1835a;	 Catch:{ IOException -> 0x006e }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006e }
        r2.<init>();	 Catch:{ IOException -> 0x006e }
        r3 = "Content-Disposition: form-data; name=\"";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x006e }
        r2 = r2.append(r5);	 Catch:{ IOException -> 0x006e }
        r3 = "\"; filename=\"";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x006e }
        r2 = r2.append(r6);	 Catch:{ IOException -> 0x006e }
        r3 = "\"\r\n";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x006e }
        r2 = r2.toString();	 Catch:{ IOException -> 0x006e }
        r2 = r2.getBytes();	 Catch:{ IOException -> 0x006e }
        r1.write(r2);	 Catch:{ IOException -> 0x006e }
        r1 = r4.f1835a;	 Catch:{ IOException -> 0x006e }
        r0 = r0.getBytes();	 Catch:{ IOException -> 0x006e }
        r1.write(r0);	 Catch:{ IOException -> 0x006e }
        r0 = r4.f1835a;	 Catch:{ IOException -> 0x006e }
        r1 = "Content-Transfer-Encoding: binary\r\n\r\n";
        r1 = r1.getBytes();	 Catch:{ IOException -> 0x006e }
        r0.write(r1);	 Catch:{ IOException -> 0x006e }
        r0 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = new byte[r0];	 Catch:{ IOException -> 0x006e }
    L_0x0060:
        r1 = r7.read(r0);	 Catch:{ IOException -> 0x006e }
        r2 = -1;
        if (r1 == r2) goto L_0x0076;
    L_0x0067:
        r2 = r4.f1835a;	 Catch:{ IOException -> 0x006e }
        r3 = 0;
        r2.write(r0, r3, r1);	 Catch:{ IOException -> 0x006e }
        goto L_0x0060;
    L_0x006e:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x00af }
        r7.close();	 Catch:{ IOException -> 0x00aa }
    L_0x0075:
        return;
    L_0x0076:
        if (r9 != 0) goto L_0x009c;
    L_0x0078:
        r0 = r4.f1835a;	 Catch:{ IOException -> 0x006e }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006e }
        r1.<init>();	 Catch:{ IOException -> 0x006e }
        r2 = "\r\n--";
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x006e }
        r2 = r4.f1838e;	 Catch:{ IOException -> 0x006e }
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x006e }
        r2 = "\r\n";
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x006e }
        r1 = r1.toString();	 Catch:{ IOException -> 0x006e }
        r1 = r1.getBytes();	 Catch:{ IOException -> 0x006e }
        r0.write(r1);	 Catch:{ IOException -> 0x006e }
    L_0x009c:
        r0 = r4.f1835a;	 Catch:{ IOException -> 0x006e }
        r0.flush();	 Catch:{ IOException -> 0x006e }
        r7.close();	 Catch:{ IOException -> 0x00a5 }
        goto L_0x0075;
    L_0x00a5:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0075;
    L_0x00aa:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0075;
    L_0x00af:
        r0 = move-exception;
        r7.close();	 Catch:{ IOException -> 0x00b4 }
    L_0x00b3:
        throw r0;
    L_0x00b4:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00b3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.ads.internal.http.e.a(java.lang.String, java.lang.String, java.io.InputStream, java.lang.String, boolean):void");
    }

    public void m1467a(String str, String str2, InputStream inputStream, boolean z) {
        m1466a(str, str2, inputStream, ActiveMQBlobMessage.BINARY_MIME_TYPE, z);
    }

    public void m1468b() {
        if (!this.f1836b) {
            try {
                this.f1835a.write(("\r\n--" + this.f1838e + "--\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.f1836b = true;
        }
    }

    public void consumeContent() {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    public InputStream getContent() {
        return new ByteArrayInputStream(this.f1835a.toByteArray());
    }

    public Header getContentEncoding() {
        return null;
    }

    public long getContentLength() {
        m1468b();
        return (long) this.f1835a.toByteArray().length;
    }

    public Header getContentType() {
        return new BasicHeader(HttpRequest.HEADER_CONTENT_TYPE, "multipart/form-data; boundary=" + this.f1838e);
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return false;
    }

    public void writeTo(OutputStream outputStream) {
        outputStream.write(this.f1835a.toByteArray());
    }
}
