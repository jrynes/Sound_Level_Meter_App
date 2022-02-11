package com.admarvel.android.ads;

import android.content.Context;
import android.util.Log;
import com.admarvel.android.ads.AdMarvelEvent.AdMarvelEvent;
import com.admarvel.android.util.AdMarvelObfuscator;
import com.admarvel.android.util.Logging;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQConnection;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.admarvel.android.ads.c */
public class AdMarvelEventHandler {

    /* renamed from: com.admarvel.android.ads.c.a */
    private static class AdMarvelEventHandler {
        public byte[] f509a;
        public int f510b;

        private AdMarvelEventHandler() {
            this.f509a = null;
            this.f510b = 0;
        }
    }

    public static String m267a(AdMarvelHttpPost adMarvelHttpPost) {
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
            httpURLConnection.setConnectTimeout(AMQConnection.HANDSHAKE_TIMEOUT);
            httpURLConnection.setReadTimeout(Constants.LOADING_INTERVAL);
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
                        AdMarvelEventHandler adMarvelEventHandler = new AdMarvelEventHandler();
                        adMarvelEventHandler.f509a = bArr;
                        adMarvelEventHandler.f510b = responseCode;
                        i2 += responseCode;
                        arrayList.add(adMarvelEventHandler);
                    }
                }
                inputStream.close();
                if (i2 > 0) {
                    Object obj = new byte[i2];
                    for (responseCode = 0; responseCode < arrayList.size(); responseCode++) {
                        AdMarvelEventHandler adMarvelEventHandler2 = (AdMarvelEventHandler) arrayList.get(responseCode);
                        System.arraycopy(adMarvelEventHandler2.f509a, 0, obj, i, adMarvelEventHandler2.f510b);
                        i += adMarvelEventHandler2.f510b;
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

    static void m268a(String str, AdMarvelInterstitialAds adMarvelInterstitialAds) {
        AdMarvelReward adMarvelReward;
        AdMarvelHttpPost adMarvelHttpPost = new AdMarvelHttpPost();
        adMarvelHttpPost.setEndpointUrl(str);
        Map hashMap = new HashMap();
        hashMap.put(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
        adMarvelHttpPost.setHttpHeaders(hashMap);
        String a = AdMarvelEventHandler.m267a(adMarvelHttpPost);
        if (a == null || a.length() <= 0) {
            Logging.log("Admarvel V4VC validation Response: Invalid response");
            adMarvelReward = new AdMarvelReward();
            adMarvelReward.setSuccess(false);
            if (AdMarvelInterstitialAds.getRewardListener() != null) {
                AdMarvelInterstitialAds.getRewardListener().onReward(adMarvelReward);
                return;
            }
            return;
        }
        try {
            a = new AdMarvelObfuscator().m577b(a);
            Logging.log("Admarvel V4VC validation Response parsed XML :" + new String(a));
            AdMarvelReward adMarvelReward2 = new AdMarvelReward();
            AdMarvelXMLReader adMarvelXMLReader = new AdMarvelXMLReader();
            adMarvelXMLReader.parseXMLString(a);
            AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
            if (parsedXMLData == null || !parsedXMLData.getName().equals("reward")) {
                adMarvelReward2.setSuccess(false);
            } else {
                a = (String) parsedXMLData.getAttributes().get("success");
                if (a == null || a.length() <= 0) {
                    adMarvelReward2.setSuccess(false);
                    if (AdMarvelInterstitialAds.getRewardListener() != null) {
                        AdMarvelInterstitialAds.getRewardListener().onReward(adMarvelReward2);
                        return;
                    }
                    return;
                }
                AdMarvelXMLElement adMarvelXMLElement;
                if (a.equalsIgnoreCase(Stomp.TRUE)) {
                    adMarvelReward2.setSuccess(true);
                } else {
                    adMarvelReward2.setSuccess(false);
                }
                if (parsedXMLData.getChildren().containsKey("rewardName")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get("rewardName")).get(0);
                    if (adMarvelXMLElement != null) {
                        adMarvelReward2.setRewardName(adMarvelXMLElement.getData());
                    }
                }
                if (parsedXMLData.getChildren().containsKey("rewardValue")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get("rewardValue")).get(0);
                    if (adMarvelXMLElement != null) {
                        adMarvelReward2.setRewardValue(adMarvelXMLElement.getData());
                    }
                }
                if (parsedXMLData.getChildren().containsKey("partnerId")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get("partnerId")).get(0);
                    if (adMarvelXMLElement != null) {
                        adMarvelReward2.setPartnerId(adMarvelXMLElement.getData());
                    }
                }
                if (parsedXMLData.getChildren().containsKey("siteId")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get("siteId")).get(0);
                    if (adMarvelXMLElement != null) {
                        adMarvelReward2.setSiteId(adMarvelXMLElement.getData());
                    }
                }
                if (parsedXMLData.getChildren().containsKey(Constants.NATIVE_AD_METADATAS_ELEMENT)) {
                    Map hashMap2 = new HashMap();
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get(Constants.NATIVE_AD_METADATAS_ELEMENT)).get(0);
                    if (adMarvelXMLElement.getChildren().containsKey(Constants.NATIVE_AD_METADATA_ELEMENT)) {
                        int size = ((ArrayList) adMarvelXMLElement.getChildren().get(Constants.NATIVE_AD_METADATA_ELEMENT)).size();
                        for (int i = 0; i < size; i++) {
                            parsedXMLData = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get(Constants.NATIVE_AD_METADATA_ELEMENT)).get(i);
                            if (parsedXMLData != null) {
                                String str2 = (String) parsedXMLData.getAttributes().get(Constants.NATIVE_AD_KEY_ELEMENT);
                                String data = parsedXMLData.getData();
                                if (!(str2 == null || data == null)) {
                                    hashMap2.put(str2, data);
                                }
                            }
                        }
                        if (hashMap2 != null && hashMap2.size() > 0) {
                            adMarvelReward2.setMetaDatas(hashMap2);
                        }
                    }
                }
            }
            if (AdMarvelInterstitialAds.getRewardListener() != null) {
                AdMarvelInterstitialAds.getRewardListener().onReward(adMarvelReward2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            adMarvelReward = new AdMarvelReward();
            adMarvelReward.setSuccess(false);
            if (AdMarvelInterstitialAds.getRewardListener() != null) {
                AdMarvelInterstitialAds.getRewardListener().onReward(adMarvelReward);
            }
        }
    }

    public static void m269a(String str, AdMarvelEvent adMarvelEvent, Context context, AdMarvelInterstitialAds adMarvelInterstitialAds) {
        if (adMarvelEvent != null && adMarvelEvent.m264a() != null) {
            Iterator it = adMarvelEvent.m264a().iterator();
            boolean z = false;
            while (it.hasNext()) {
                AdMarvelEvent adMarvelEvent2 = (AdMarvelEvent) it.next();
                if (!(adMarvelEvent2 == null || adMarvelEvent2.m258a() == null || !adMarvelEvent2.m258a().equalsIgnoreCase(str))) {
                    for (String str2 : adMarvelEvent2.m262b()) {
                        if (adMarvelEvent2.m263c()) {
                            if (adMarvelEvent.m266b() && !r1) {
                                adMarvelEvent.m265a(false);
                                if (adMarvelInterstitialAds != null) {
                                    adMarvelInterstitialAds.setRewardFired(true);
                                }
                                AdMarvelEventHandler.m268a(str2, adMarvelInterstitialAds);
                                z = true;
                            }
                        } else if (context != null) {
                            new Utils(context).m245a(str2);
                        }
                    }
                }
                z = z;
            }
        }
    }
}
