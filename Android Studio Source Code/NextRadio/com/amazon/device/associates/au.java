package com.amazon.device.associates;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xbill.DNS.Zone;

/* compiled from: RestClient */
class au {
    private ArrayList<NameValuePair> f1197a;
    private ArrayList<NameValuePair> f1198b;
    private String f1199c;
    private int f1200d;
    private String f1201e;
    private String f1202f;

    /* renamed from: com.amazon.device.associates.au.1 */
    static /* synthetic */ class RestClient {
        static final /* synthetic */ int[] f1196a;

        static {
            f1196a = new int[RequestMethod.values().length];
            try {
                f1196a[RequestMethod.GET.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1196a[RequestMethod.POST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public String m795a() {
        return this.f1202f;
    }

    public int m798b() {
        return this.f1200d;
    }

    public au() {
        this.f1202f = null;
    }

    public au(String str) {
        this.f1202f = null;
        this.f1199c = str;
        this.f1197a = new ArrayList();
        this.f1198b = new ArrayList();
    }

    public void m797a(String str, String str2) {
        this.f1197a.add(new BasicNameValuePair(str, str2));
    }

    public void m796a(RequestMethod requestMethod) throws UnsupportedEncodingException {
        Iterator it;
        NameValuePair nameValuePair;
        switch (RestClient.f1196a[requestMethod.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                String str = Stomp.EMPTY;
                if (!this.f1197a.isEmpty()) {
                    String str2 = str + "?";
                    it = this.f1197a.iterator();
                    str = str2;
                    while (it.hasNext()) {
                        nameValuePair = (NameValuePair) it.next();
                        str2 = nameValuePair.getName() + "=" + URLEncoder.encode(nameValuePair.getValue(), HttpRequest.CHARSET_UTF8);
                        if (str.length() > 1) {
                            str2 = str + "&" + str2;
                        } else {
                            str2 = str + str2;
                        }
                        str = str2;
                    }
                }
                HttpUriRequest httpGet = new HttpGet(this.f1199c + str);
                Iterator it2 = this.f1198b.iterator();
                while (it2.hasNext()) {
                    nameValuePair = (NameValuePair) it2.next();
                    httpGet.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                }
                m794a(httpGet, this.f1199c);
            case Zone.SECONDARY /*2*/:
                HttpUriRequest httpPost = new HttpPost(this.f1199c);
                it = this.f1198b.iterator();
                while (it.hasNext()) {
                    nameValuePair = (NameValuePair) it.next();
                    httpPost.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                }
                if (!this.f1197a.isEmpty()) {
                    httpPost.setEntity(new UrlEncodedFormEntity(this.f1197a, HttpRequest.CHARSET_UTF8));
                }
                m794a(httpPost, this.f1199c);
            default:
        }
    }

    private void m794a(HttpUriRequest httpUriRequest, String str) {
        HttpClient defaultHttpClient = new DefaultHttpClient();
        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
        defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(60000));
        try {
            HttpResponse execute = defaultHttpClient.execute(httpUriRequest);
            this.f1200d = execute.getStatusLine().getStatusCode();
            this.f1201e = execute.getStatusLine().getReasonPhrase();
            HttpEntity entity = execute.getEntity();
            if (entity != null) {
                InputStream content = entity.getContent();
                this.f1202f = m793a(content);
                content.close();
            }
            defaultHttpClient.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {
            defaultHttpClient.getConnectionManager().shutdown();
            this.f1202f = null;
        } catch (IOException e2) {
            defaultHttpClient.getConnectionManager().shutdown();
            this.f1202f = null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String m793a(java.io.InputStream r5) {
        /*
        r0 = new java.io.BufferedReader;
        r1 = new java.io.InputStreamReader;
        r1.<init>(r5);
        r0.<init>(r1);
        r1 = new java.lang.StringBuilder;
        r1.<init>();
    L_0x000f:
        r2 = r0.readLine();	 Catch:{ IOException -> 0x002c }
        if (r2 == 0) goto L_0x004d;
    L_0x0015:
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x002c }
        r3.<init>();	 Catch:{ IOException -> 0x002c }
        r2 = r3.append(r2);	 Catch:{ IOException -> 0x002c }
        r3 = "\n";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x002c }
        r2 = r2.toString();	 Catch:{ IOException -> 0x002c }
        r1.append(r2);	 Catch:{ IOException -> 0x002c }
        goto L_0x000f;
    L_0x002c:
        r0 = move-exception;
        r2 = "RestClient";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0053 }
        r3.<init>();	 Catch:{ all -> 0x0053 }
        r4 = "Error converting stream to string. Ex=";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0053 }
        r0 = r3.append(r0);	 Catch:{ all -> 0x0053 }
        r0 = r0.toString();	 Catch:{ all -> 0x0053 }
        com.amazon.device.associates.Log.m1020d(r2, r0);	 Catch:{ all -> 0x0053 }
        r5.close();	 Catch:{ IOException -> 0x0058 }
    L_0x0048:
        r0 = r1.toString();
        return r0;
    L_0x004d:
        r5.close();	 Catch:{ IOException -> 0x0051 }
        goto L_0x0048;
    L_0x0051:
        r0 = move-exception;
        goto L_0x0048;
    L_0x0053:
        r0 = move-exception;
        r5.close();	 Catch:{ IOException -> 0x005a }
    L_0x0057:
        throw r0;
    L_0x0058:
        r0 = move-exception;
        goto L_0x0048;
    L_0x005a:
        r1 = move-exception;
        goto L_0x0057;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amazon.device.associates.au.a(java.io.InputStream):java.lang.String");
    }
}
