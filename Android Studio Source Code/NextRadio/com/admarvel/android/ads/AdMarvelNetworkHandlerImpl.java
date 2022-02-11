package com.admarvel.android.ads;

import android.util.Log;
import com.admarvel.android.util.Logging;
import com.facebook.ads.AdError;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQConnection;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.admarvel.android.ads.i */
public class AdMarvelNetworkHandlerImpl implements AdMarvelNetworkHandler {

    /* renamed from: com.admarvel.android.ads.i.a */
    private static class AdMarvelNetworkHandlerImpl {
        public byte[] f622a;
        public int f623b;

        private AdMarvelNetworkHandlerImpl() {
            this.f622a = null;
            this.f623b = 0;
        }
    }

    public String executeNetworkCall(AdMarvelHttpPost adMarvelHttpPost) {
        int i = 0;
        String str = Stomp.EMPTY;
        try {
            String str2;
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(adMarvelHttpPost.getEndpointUrl()).openConnection();
            httpURLConnection.setRequestMethod(HttpRequest.METHOD_POST);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            for (String str3 : adMarvelHttpPost.getHttpHeaders().keySet()) {
                httpURLConnection.setRequestProperty(str3, (String) adMarvelHttpPost.getHttpHeaders().get(str3));
            }
            httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, Integer.toString(adMarvelHttpPost.getPostString().length()));
            httpURLConnection.setConnectTimeout(AdError.SERVER_ERROR_CODE);
            httpURLConnection.setReadTimeout(AMQConnection.HANDSHAKE_TIMEOUT);
            httpURLConnection.getOutputStream().write(adMarvelHttpPost.getPostString().getBytes());
            int responseCode = httpURLConnection.getResponseCode();
            int contentLength = httpURLConnection.getContentLength();
            Logging.log("Connection Status Code: " + responseCode);
            Logging.log("Content Length: " + contentLength);
            if (responseCode == AMQP.REPLY_SUCCESS) {
                InputStream inputStream = (InputStream) httpURLConnection.getContent();
                List arrayList = new ArrayList();
                responseCode = Flags.FLAG2;
                int i2 = 0;
                while (responseCode != -1) {
                    byte[] bArr = new byte[Flags.FLAG2];
                    responseCode = inputStream.read(bArr, 0, Flags.FLAG2);
                    if (responseCode > 0) {
                        AdMarvelNetworkHandlerImpl adMarvelNetworkHandlerImpl = new AdMarvelNetworkHandlerImpl();
                        adMarvelNetworkHandlerImpl.f622a = bArr;
                        adMarvelNetworkHandlerImpl.f623b = responseCode;
                        i2 += responseCode;
                        arrayList.add(adMarvelNetworkHandlerImpl);
                    }
                }
                inputStream.close();
                if (i2 > 0) {
                    Object obj = new byte[i2];
                    for (responseCode = 0; responseCode < arrayList.size(); responseCode++) {
                        AdMarvelNetworkHandlerImpl adMarvelNetworkHandlerImpl2 = (AdMarvelNetworkHandlerImpl) arrayList.get(responseCode);
                        System.arraycopy(adMarvelNetworkHandlerImpl2.f622a, 0, obj, i, adMarvelNetworkHandlerImpl2.f623b);
                        i += adMarvelNetworkHandlerImpl2.f623b;
                    }
                    str2 = new String(obj);
                    return str2.toString();
                }
            }
            str2 = str;
            return str2.toString();
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return null;
        }
    }
}
