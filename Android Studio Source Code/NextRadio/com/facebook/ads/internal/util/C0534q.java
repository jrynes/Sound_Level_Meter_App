package com.facebook.ads.internal.util;

import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

/* renamed from: com.facebook.ads.internal.util.q */
public class C0534q extends SSLSocketFactory {
    SSLContext f1945a;

    /* renamed from: com.facebook.ads.internal.util.q.1 */
    class C05331 implements X509TrustManager {
        final /* synthetic */ C0534q f1944a;

        C05331(C0534q c0534q) {
            this.f1944a = c0534q;
        }

        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public C0534q(KeyStore keyStore) {
        super(keyStore);
        this.f1945a = SSLContext.getInstance("TLS");
        C05331 c05331 = new C05331(this);
        this.f1945a.init(null, new TrustManager[]{c05331}, null);
    }

    public Socket createSocket() {
        return this.f1945a.getSocketFactory().createSocket();
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) {
        return this.f1945a.getSocketFactory().createSocket(socket, str, i, z);
    }
}
