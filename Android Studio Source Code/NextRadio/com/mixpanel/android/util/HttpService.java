package com.mixpanel.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.mixpanel.android.mpmetrics.MPConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.xbill.DNS.KEYRecord.Flags;

public class HttpService implements RemoteService {
    private static final String LOGTAG = "MixpanelAPI.Message";
    private static boolean sIsMixpanelBlocked;

    /* renamed from: com.mixpanel.android.util.HttpService.1 */
    class C11271 implements Runnable {
        C11271() {
        }

        public void run() {
            try {
                InetAddress apiMixpanelInet = InetAddress.getByName("api.mixpanel.com");
                InetAddress decideMixpanelInet = InetAddress.getByName("decide.mixpanel.com");
                boolean z = apiMixpanelInet.isLoopbackAddress() || apiMixpanelInet.isAnyLocalAddress() || decideMixpanelInet.isLoopbackAddress() || decideMixpanelInet.isAnyLocalAddress();
                HttpService.sIsMixpanelBlocked = z;
                if (MPConfig.DEBUG && HttpService.sIsMixpanelBlocked) {
                    Log.v(HttpService.LOGTAG, "AdBlocker is enabled. Won't be able to use Mixpanel services.");
                }
            } catch (UnknownHostException e) {
            }
        }
    }

    public void checkIsMixpanelBlocked() {
        new Thread(new C11271()).start();
    }

    public boolean isOnline(Context context) {
        boolean isOnline = false;
        if (!sIsMixpanelBlocked) {
            try {
                NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    isOnline = true;
                }
                if (MPConfig.DEBUG) {
                    Log.v(LOGTAG, "ConnectivityManager says we " + (isOnline ? "are" : "are not") + " online");
                }
            } catch (SecurityException e) {
                isOnline = true;
                if (MPConfig.DEBUG) {
                    Log.v(LOGTAG, "Don't have permission to check connectivity, will assume we are online");
                }
            }
        }
        return isOnline;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] performRequest(java.lang.String r20, java.util.Map<java.lang.String, java.lang.Object> r21, javax.net.ssl.SSLSocketFactory r22) throws com.mixpanel.android.util.RemoteService.ServiceUnavailableException, java.io.IOException {
        /*
        r19 = this;
        r16 = com.mixpanel.android.mpmetrics.MPConfig.DEBUG;
        if (r16 == 0) goto L_0x0020;
    L_0x0004:
        r16 = "MixpanelAPI.Message";
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r18 = "Attempting request to ";
        r17 = r17.append(r18);
        r0 = r17;
        r1 = r20;
        r17 = r0.append(r1);
        r17 = r17.toString();
        android.util.Log.v(r16, r17);
    L_0x0020:
        r12 = 0;
        r13 = 0;
        r14 = 0;
    L_0x0023:
        r16 = 3;
        r0 = r16;
        if (r13 >= r0) goto L_0x015e;
    L_0x0029:
        if (r14 != 0) goto L_0x015e;
    L_0x002b:
        r8 = 0;
        r9 = 0;
        r2 = 0;
        r5 = 0;
        r15 = new java.net.URL;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r0 = r20;
        r15.<init>(r0);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = r15.openConnection();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r0 = r16;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r5 = r0;
        if (r22 == 0) goto L_0x0053;
    L_0x0041:
        r0 = r5 instanceof javax.net.ssl.HttpsURLConnection;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = r0;
        if (r16 == 0) goto L_0x0053;
    L_0x0047:
        r0 = r5;
        r0 = (javax.net.ssl.HttpsURLConnection) r0;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = r0;
        r0 = r16;
        r1 = r22;
        r0.setSSLSocketFactory(r1);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
    L_0x0053:
        r16 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r0 = r16;
        r5.setConnectTimeout(r0);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = r16;
        r5.setReadTimeout(r0);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        if (r21 == 0) goto L_0x00fb;
    L_0x0063:
        r4 = new android.net.Uri$Builder;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r4.<init>();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = r21.entrySet();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r7 = r16.iterator();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
    L_0x0070:
        r16 = r7.hasNext();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        if (r16 == 0) goto L_0x00b6;
    L_0x0076:
        r10 = r7.next();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r10 = (java.util.Map.Entry) r10;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = r10.getKey();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = (java.lang.String) r16;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r17 = r10.getValue();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r17 = r17.toString();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r0 = r16;
        r1 = r17;
        r4.appendQueryParameter(r0, r1);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        goto L_0x0070;
    L_0x0092:
        r6 = move-exception;
    L_0x0093:
        r16 = com.mixpanel.android.mpmetrics.MPConfig.DEBUG;	 Catch:{ all -> 0x0147 }
        if (r16 == 0) goto L_0x009e;
    L_0x0097:
        r16 = "MixpanelAPI.Message";
        r17 = "Failure to connect, likely caused by a known issue with Android lib. Retrying.";
        android.util.Log.d(r16, r17);	 Catch:{ all -> 0x0147 }
    L_0x009e:
        r13 = r13 + 1;
        if (r2 == 0) goto L_0x00a5;
    L_0x00a2:
        r2.close();	 Catch:{ IOException -> 0x0176 }
    L_0x00a5:
        if (r9 == 0) goto L_0x00aa;
    L_0x00a7:
        r9.close();	 Catch:{ IOException -> 0x0179 }
    L_0x00aa:
        if (r8 == 0) goto L_0x00af;
    L_0x00ac:
        r8.close();	 Catch:{ IOException -> 0x017c }
    L_0x00af:
        if (r5 == 0) goto L_0x0023;
    L_0x00b1:
        r5.disconnect();
        goto L_0x0023;
    L_0x00b6:
        r16 = r4.build();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r11 = r16.getEncodedQuery();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = r11.getBytes();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r0 = r16;
        r0 = r0.length;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = r0;
        r0 = r16;
        r5.setFixedLengthStreamingMode(r0);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = 1;
        r0 = r16;
        r5.setDoOutput(r0);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = "POST";
        r0 = r16;
        r5.setRequestMethod(r0);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r9 = r5.getOutputStream();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r3 = new java.io.BufferedOutputStream;	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r3.<init>(r9);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r16 = "UTF-8";
        r0 = r16;
        r16 = r11.getBytes(r0);	 Catch:{ EOFException -> 0x018b, IOException -> 0x0188, all -> 0x0185 }
        r0 = r16;
        r3.write(r0);	 Catch:{ EOFException -> 0x018b, IOException -> 0x0188, all -> 0x0185 }
        r3.flush();	 Catch:{ EOFException -> 0x018b, IOException -> 0x0188, all -> 0x0185 }
        r3.close();	 Catch:{ EOFException -> 0x018b, IOException -> 0x0188, all -> 0x0185 }
        r2 = 0;
        r9.close();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r9 = 0;
    L_0x00fb:
        r8 = r5.getInputStream();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r12 = slurp(r8);	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r8.close();	 Catch:{ EOFException -> 0x0092, IOException -> 0x011e }
        r8 = 0;
        r14 = 1;
        if (r2 == 0) goto L_0x010d;
    L_0x010a:
        r2.close();	 Catch:{ IOException -> 0x0170 }
    L_0x010d:
        if (r9 == 0) goto L_0x0112;
    L_0x010f:
        r9.close();	 Catch:{ IOException -> 0x0172 }
    L_0x0112:
        if (r8 == 0) goto L_0x0117;
    L_0x0114:
        r8.close();	 Catch:{ IOException -> 0x0174 }
    L_0x0117:
        if (r5 == 0) goto L_0x0023;
    L_0x0119:
        r5.disconnect();
        goto L_0x0023;
    L_0x011e:
        r6 = move-exception;
    L_0x011f:
        r16 = r5.getResponseCode();	 Catch:{ all -> 0x0147 }
        r17 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r0 = r16;
        r1 = r17;
        if (r0 < r1) goto L_0x015d;
    L_0x012b:
        r16 = r5.getResponseCode();	 Catch:{ all -> 0x0147 }
        r17 = 599; // 0x257 float:8.4E-43 double:2.96E-321;
        r0 = r16;
        r1 = r17;
        if (r0 > r1) goto L_0x015d;
    L_0x0137:
        r16 = new com.mixpanel.android.util.RemoteService$ServiceUnavailableException;	 Catch:{ all -> 0x0147 }
        r17 = "Service Unavailable";
        r18 = "Retry-After";
        r0 = r18;
        r18 = r5.getHeaderField(r0);	 Catch:{ all -> 0x0147 }
        r16.<init>(r17, r18);	 Catch:{ all -> 0x0147 }
        throw r16;	 Catch:{ all -> 0x0147 }
    L_0x0147:
        r16 = move-exception;
    L_0x0148:
        if (r2 == 0) goto L_0x014d;
    L_0x014a:
        r2.close();	 Catch:{ IOException -> 0x017f }
    L_0x014d:
        if (r9 == 0) goto L_0x0152;
    L_0x014f:
        r9.close();	 Catch:{ IOException -> 0x0181 }
    L_0x0152:
        if (r8 == 0) goto L_0x0157;
    L_0x0154:
        r8.close();	 Catch:{ IOException -> 0x0183 }
    L_0x0157:
        if (r5 == 0) goto L_0x015c;
    L_0x0159:
        r5.disconnect();
    L_0x015c:
        throw r16;
    L_0x015d:
        throw r6;	 Catch:{ all -> 0x0147 }
    L_0x015e:
        r16 = com.mixpanel.android.mpmetrics.MPConfig.DEBUG;
        if (r16 == 0) goto L_0x016f;
    L_0x0162:
        r16 = 3;
        r0 = r16;
        if (r13 < r0) goto L_0x016f;
    L_0x0168:
        r16 = "MixpanelAPI.Message";
        r17 = "Could not connect to Mixpanel service after three retries.";
        android.util.Log.v(r16, r17);
    L_0x016f:
        return r12;
    L_0x0170:
        r16 = move-exception;
        goto L_0x010d;
    L_0x0172:
        r16 = move-exception;
        goto L_0x0112;
    L_0x0174:
        r16 = move-exception;
        goto L_0x0117;
    L_0x0176:
        r16 = move-exception;
        goto L_0x00a5;
    L_0x0179:
        r16 = move-exception;
        goto L_0x00aa;
    L_0x017c:
        r16 = move-exception;
        goto L_0x00af;
    L_0x017f:
        r17 = move-exception;
        goto L_0x014d;
    L_0x0181:
        r17 = move-exception;
        goto L_0x0152;
    L_0x0183:
        r17 = move-exception;
        goto L_0x0157;
    L_0x0185:
        r16 = move-exception;
        r2 = r3;
        goto L_0x0148;
    L_0x0188:
        r6 = move-exception;
        r2 = r3;
        goto L_0x011f;
    L_0x018b:
        r6 = move-exception;
        r2 = r3;
        goto L_0x0093;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.util.HttpService.performRequest(java.lang.String, java.util.Map, javax.net.ssl.SSLSocketFactory):byte[]");
    }

    private static byte[] slurp(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[Flags.FLAG2];
        while (true) {
            int nRead = inputStream.read(data, 0, data.length);
            if (nRead != -1) {
                buffer.write(data, 0, nRead);
            } else {
                buffer.flush();
                return buffer.toByteArray();
            }
        }
    }
}
