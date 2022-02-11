package com.facebook.ads.internal.http;

import android.content.Context;
import com.facebook.ads.internal.C0469e;
import com.facebook.ads.internal.dto.C0468f;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0523h;
import com.facebook.ads.internal.util.C0532p;
import com.facebook.ads.internal.util.C0534q;
import com.mixpanel.android.java_websocket.WebSocket;
import com.rabbitmq.client.impl.AMQConnection;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.GZIPInputStream;
import org.apache.activemq.util.ThreadPoolUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.facebook.ads.internal.http.a */
public class C0490a {
    private static final C0532p f1814a;
    private static final ThreadPoolExecutor f1815b;
    private final DefaultHttpClient f1816c;
    private final Map<Context, List<WeakReference<Future<?>>>> f1817d;
    private final Map<String, String> f1818e;
    private Context f1819f;

    /* renamed from: com.facebook.ads.internal.http.a.1 */
    class C04871 implements HttpRequestInterceptor {
        final /* synthetic */ C0490a f1812a;

        C04871(C0490a c0490a) {
            this.f1812a = c0490a;
        }

        public void process(HttpRequest httpRequest, HttpContext httpContext) {
            if (!httpRequest.containsHeader(io.fabric.sdk.android.services.network.HttpRequest.HEADER_ACCEPT_ENCODING)) {
                httpRequest.addHeader(io.fabric.sdk.android.services.network.HttpRequest.HEADER_ACCEPT_ENCODING, io.fabric.sdk.android.services.network.HttpRequest.ENCODING_GZIP);
            }
            for (String str : this.f1812a.f1818e.keySet()) {
                httpRequest.addHeader(str, (String) this.f1812a.f1818e.get(str));
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.http.a.2 */
    class C04882 implements HttpResponseInterceptor {
        final /* synthetic */ C0490a f1813a;

        C04882(C0490a c0490a) {
            this.f1813a = c0490a;
        }

        public void process(HttpResponse httpResponse, HttpContext httpContext) {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                Header contentEncoding = entity.getContentEncoding();
                if (contentEncoding != null) {
                    for (HeaderElement name : contentEncoding.getElements()) {
                        if (name.getName().equalsIgnoreCase(io.fabric.sdk.android.services.network.HttpRequest.ENCODING_GZIP)) {
                            httpResponse.setEntity(new C0489a(httpResponse.getEntity()));
                            return;
                        }
                    }
                }
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.http.a.a */
    private static class C0489a extends HttpEntityWrapper {
        public C0489a(HttpEntity httpEntity) {
            super(httpEntity);
        }

        public InputStream getContent() {
            return new GZIPInputStream(this.wrappedEntity.getContent());
        }

        public long getContentLength() {
            return -1;
        }
    }

    static {
        f1814a = new C0532p();
        f1815b = (ThreadPoolExecutor) Executors.newCachedThreadPool(f1814a);
    }

    public C0490a(Context context, C0469e c0469e) {
        this.f1819f = context;
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        basicHttpParams.setParameter("http.protocol.cookie-policy", "compatibility");
        ConnManagerParams.setTimeout(basicHttpParams, ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(100));
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 100);
        HttpConnectionParams.setSoTimeout(basicHttpParams, AMQConnection.HANDSHAKE_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, AMQConnection.HANDSHAKE_TIMEOUT);
        HttpConnectionParams.setTcpNoDelay(basicHttpParams, true);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, Flags.FLAG2);
        HttpProtocolParams.setUserAgent(basicHttpParams, C0523h.m1545a(context, c0469e) + " [" + "FBAN/AudienceNetworkForAndroid;" + "FBSN/" + "Android" + ";" + "FBSV/" + C0468f.f1737a + ";" + "FBAB/" + C0468f.f1740d + ";" + "FBAV/" + C0468f.f1742f + ";" + "FBBV/" + C0468f.f1743g + ";" + "FBLC/" + Locale.getDefault().toString() + "]");
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        if (C0522g.m1538a()) {
            m1436a(basicHttpParams, schemeRegistry);
        } else {
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), WebSocket.DEFAULT_WSS_PORT));
        }
        this.f1816c = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
        this.f1816c.addRequestInterceptor(new C04871(this));
        this.f1816c.addResponseInterceptor(new C04882(this));
        this.f1817d = new WeakHashMap();
        this.f1818e = new HashMap();
    }

    private HttpEntity m1434a(C0495d c0495d) {
        return c0495d != null ? c0495d.m1461a() : null;
    }

    private HttpEntityEnclosingRequestBase m1435a(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, HttpEntity httpEntity) {
        if (httpEntity != null) {
            httpEntityEnclosingRequestBase.setEntity(httpEntity);
        }
        return httpEntityEnclosingRequestBase;
    }

    private void m1436a(BasicHttpParams basicHttpParams, SchemeRegistry schemeRegistry) {
        try {
            KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
            instance.load(null, null);
            SocketFactory c0534q = new C0534q(instance);
            c0534q.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpProtocolParams.setContentCharset(basicHttpParams, io.fabric.sdk.android.services.network.HttpRequest.CHARSET_UTF8);
            schemeRegistry.register(new Scheme("https", c0534q, WebSocket.DEFAULT_WSS_PORT));
        } catch (Exception e) {
        }
    }

    public void m1437a(String str, C0495d c0495d, C0493c c0493c) {
        m1438a(str, m1434a(c0495d), null, c0493c);
    }

    public void m1438a(String str, HttpEntity httpEntity, String str2, C0493c c0493c) {
        m1439a(this.f1816c, m1435a(new HttpPost(str), httpEntity), str2, c0493c);
    }

    protected void m1439a(DefaultHttpClient defaultHttpClient, HttpUriRequest httpUriRequest, String str, C0493c c0493c) {
        if (str != null) {
            httpUriRequest.addHeader(io.fabric.sdk.android.services.network.HttpRequest.HEADER_CONTENT_TYPE, str);
        }
        Future submit = f1815b.submit(new C0491b(defaultHttpClient, new SyncBasicHttpContext(new BasicHttpContext()), httpUriRequest, c0493c));
        List list = (List) this.f1817d.get(this.f1819f);
        if (list == null) {
            list = new LinkedList();
            this.f1817d.put(this.f1819f, list);
        }
        list.add(new WeakReference(submit));
    }

    public void m1440a(boolean z) {
        List<WeakReference> list = (List) this.f1817d.get(this.f1819f);
        if (list != null) {
            for (WeakReference weakReference : list) {
                Future future = (Future) weakReference.get();
                if (future != null) {
                    future.cancel(z);
                }
            }
        }
        this.f1817d.remove(this.f1819f);
    }
}
