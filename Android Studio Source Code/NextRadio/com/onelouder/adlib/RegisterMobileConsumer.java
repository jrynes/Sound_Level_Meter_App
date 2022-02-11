package com.onelouder.adlib;

import android.content.Context;
import android.os.Build;
import android.util.Xml;
import com.google.android.gms.common.Scopes;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.onelouder.adlib.crypto.DocumentEncryptor;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.settings.AppSettingsData;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.SAXParserFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connected;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

class RegisterMobileConsumer {
    private static final String ENDPOINT_DEV = "https://mss-dev.handmark.com/mss/rmc?crypt=";
    private static final String ENDPOINT_PROD = "https://mss.handmark.com/mss/rmc?crypt=";
    private static final String ENDPOINT_QA = "https://mss-qa.handmark.com/mss/rmc?crypt=";
    private static final String TAG = "RegisterMobileConsumer";
    private Context context;
    private String mConsumerData;

    /* renamed from: com.onelouder.adlib.RegisterMobileConsumer.1 */
    class C13091 implements Runnable {
        final /* synthetic */ Context val$context;

        C13091(Context context) {
            this.val$context = context;
        }

        public void run() {
            BigInteger mod;
            if (Preferences.isProdEnv(this.val$context)) {
                mod = new BigInteger("92b27fe1ade9cae71460fe941bb7f50845dbd0d1389d073571d7bdeb53d811ada9c1d975b02393badfc4ff84f23caa12aaa775159b8faf017f52fbb67765dbcfd3694995fe09b8a47f7f30eddeba87a1f710fdcf7168060eee3f6c4a2478b282cbdadee813cfc09c5df5ff39b19e5903bd44a3b1ec8b665b5b23235364b1dd99", 16);
            } else {
                mod = new BigInteger("8cc1fb717ba3ae5eff1dcda358621875b3148782a5fd101a53c4ab54c3376849ab6d1068bf43e96b0b4edb6b4eb69b27a26f1ad17fba2250243e4e33ac1da9ebc727ede586f343bd0273a230f26e3d31f3fa506d967171fc67d56eff372252ae6907cd70aca9c2a4b60ab73313fb9b03deba1e14d7b2ecfe780214837651e44f", 16);
            }
            BigInteger exp = new BigInteger("10001", 16);
            byte[] encrypted = null;
            try {
                encrypted = new DocumentEncryptor(mod, exp).encrypt("teststring".getBytes());
            } catch (Throwable e) {
                Diagnostics.m1953e(RegisterMobileConsumer.TAG, e);
            }
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            try {
                serializer.setOutput(writer);
                serializer.startDocument(HttpRequest.CHARSET_UTF8, Boolean.valueOf(true));
                serializer.startTag(Stomp.EMPTY, "register");
                String value = Utils.getPhoneNumber(this.val$context);
                if (value != null && value.length() > 0) {
                    serializer.startTag(Stomp.EMPTY, "phone");
                    serializer.text(value);
                    serializer.endTag(Stomp.EMPTY, "phone");
                }
                value = Utils.getDeviceId(this.val$context);
                if (value != null && value.length() > 0) {
                    int phoneType = Utils.getPhoneType(this.val$context);
                    if (phoneType == 1) {
                        serializer.startTag(Stomp.EMPTY, "imei");
                        serializer.text(value);
                        serializer.endTag(Stomp.EMPTY, "imei");
                    } else if (phoneType == 2) {
                        serializer.startTag(Stomp.EMPTY, "meid");
                        serializer.text(value);
                        serializer.endTag(Stomp.EMPTY, "meid");
                    }
                }
                value = Utils.getAdvertisingId(this.val$context);
                if (value != null && value.length() > 0) {
                    serializer.startTag(Stomp.EMPTY, "googleAdvertisingId");
                    serializer.attribute(Stomp.EMPTY, "limitAdTrackingEnabled", Utils.getLimitAdTrackingEnabled(this.val$context));
                    serializer.text(value);
                    serializer.endTag(Stomp.EMPTY, "googleAdvertisingId");
                }
                value = Utils.getWifiMacAddress(this.val$context);
                if (value != null && value.length() > 0) {
                    serializer.startTag(Stomp.EMPTY, "wifimac");
                    serializer.text(value);
                    serializer.endTag(Stomp.EMPTY, "wifimac");
                }
                value = Preferences.getSimplePref(this.val$context, "ads-twitterid", Stomp.EMPTY);
                if (value != null && value.length() > 0) {
                    serializer.startTag(Stomp.EMPTY, "twitter");
                    serializer.text(value);
                    serializer.endTag(Stomp.EMPTY, "twitter");
                }
                value = Preferences.getSimplePref(this.val$context, "ads-email", Stomp.EMPTY);
                if (value != null && value.length() > 0) {
                    serializer.startTag(Stomp.EMPTY, Scopes.EMAIL);
                    serializer.text(value);
                    serializer.endTag(Stomp.EMPTY, Scopes.EMAIL);
                }
                GeoLocation location = LocationUtils.getGeoLocation(this.val$context);
                if (location != null) {
                    value = location.getLatitude();
                    if (value != null && value.length() > 0) {
                        serializer.startTag(Stomp.EMPTY, locationTracking.latitude);
                        serializer.text(value);
                        serializer.endTag(Stomp.EMPTY, locationTracking.latitude);
                    }
                    value = location.getLongitude();
                    if (value != null && value.length() > 0) {
                        serializer.startTag(Stomp.EMPTY, locationTracking.longitude);
                        serializer.text(value);
                        serializer.endTag(Stomp.EMPTY, locationTracking.longitude);
                    }
                    value = location.getPostalcode();
                    if (value != null && value.length() > 0) {
                        serializer.startTag(Stomp.EMPTY, "zip");
                        serializer.text(value);
                        serializer.endTag(Stomp.EMPTY, "zip");
                    }
                }
                value = Preferences.getSimplePref(this.val$context, "ads-product-name", Stomp.EMPTY);
                if (value != null && value.length() > 0) {
                    serializer.startTag(Stomp.EMPTY, "product");
                    serializer.text(value);
                    serializer.endTag(Stomp.EMPTY, "product");
                }
                value = Utils.getVersionName(this.val$context);
                if (value != null && value.length() > 0) {
                    serializer.startTag(Stomp.EMPTY, Connected.VERSION);
                    serializer.text(value);
                    serializer.endTag(Stomp.EMPTY, Connected.VERSION);
                }
                value = Utils.getCarrier(this.val$context);
                if (value != null && value.length() > 0) {
                    serializer.startTag(Stomp.EMPTY, "carrier");
                    serializer.text(value);
                    serializer.endTag(Stomp.EMPTY, "carrier");
                }
                serializer.startTag(Stomp.EMPTY, "lang");
                serializer.text(Utils.getLanguage(this.val$context));
                serializer.endTag(Stomp.EMPTY, "lang");
                serializer.startTag(Stomp.EMPTY, PreferenceStorage.SELECTED_COUNTRY);
                serializer.text(Utils.getCountry(this.val$context));
                serializer.endTag(Stomp.EMPTY, PreferenceStorage.SELECTED_COUNTRY);
                serializer.startTag(Stomp.EMPTY, "devname");
                serializer.text(Build.MODEL);
                serializer.endTag(Stomp.EMPTY, "devname");
                serializer.startTag(Stomp.EMPTY, "manufacturer");
                serializer.text(Build.MANUFACTURER);
                serializer.endTag(Stomp.EMPTY, "manufacturer");
                serializer.startTag(Stomp.EMPTY, "platform");
                serializer.text("and");
                serializer.endTag(Stomp.EMPTY, "platform");
                serializer.endTag(Stomp.EMPTY, "register");
                serializer.endDocument();
            } catch (Throwable e2) {
                Diagnostics.m1953e(RegisterMobileConsumer.TAG, e2);
                writer = null;
            }
            if (writer != null) {
                Diagnostics.m1955i(RegisterMobileConsumer.TAG, writer.toString());
                String url = null;
                if (Preferences.isProdEnv(this.val$context)) {
                    url = RegisterMobileConsumer.ENDPOINT_PROD;
                } else if (Preferences.isQaEnv(this.val$context)) {
                    url = RegisterMobileConsumer.ENDPOINT_QA;
                } else if (Preferences.isDevEnv(this.val$context)) {
                    url = RegisterMobileConsumer.ENDPOINT_DEV;
                }
                if (url != null) {
                    try {
                        encrypted = new DocumentEncryptor(mod, exp).encrypt(writer.toString().getBytes());
                        url = url + "1";
                    } catch (Throwable e22) {
                        Diagnostics.m1958w(RegisterMobileConsumer.TAG, e22);
                        url = url + "0";
                    }
                    if (encrypted != null) {
                        RegisterMobileConsumer.this.mConsumerData = Base64.encode(encrypted);
                    } else {
                        RegisterMobileConsumer.this.mConsumerData = Base64.encode(Obfuscator.process(writer.toString().getBytes()));
                    }
                    new ConsumerRequest(url).run();
                }
            }
        }
    }

    class ConsumerParser extends DefaultHandler {
        ConsumerParser() {
        }

        public void startElement(String uri, String name, String qName, Attributes atts) {
            if (name.equals("registered")) {
                Preferences.setMobileConsumerId(RegisterMobileConsumer.this.context, atts.getValue(AppSettingsData.STATUS_NEW));
                String ready = atts.getValue("ready");
                if (ready != null) {
                    int interval = Integer.parseInt(ready) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
                    Preferences.setAdTargetingInterval(RegisterMobileConsumer.this.context, interval);
                    Preferences.setAdTargetingUpdate(RegisterMobileConsumer.this.context, System.currentTimeMillis() + ((long) interval));
                    return;
                }
                Preferences.setAdTargetingUpdate(RegisterMobileConsumer.this.context, System.currentTimeMillis() + 86400000);
            }
        }
    }

    class ConsumerRequest extends ServerBase {
        public ConsumerRequest(String url) {
            this.mUrl = url;
            this.mZeroBytesAllowed = false;
        }

        protected String TAG() {
            return "ConsumerRequest";
        }

        protected String ConstructPOST() {
            return RegisterMobileConsumer.this.mConsumerData;
        }

        public boolean ProcessResponse() throws EOFException {
            try {
                InputStream stm = new ByteArrayInputStream(this.data);
                InputStream is = stm;
                if (this.mResponseGzipped) {
                    is = new GZIPInputStream(stm);
                }
                XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                xr.setContentHandler(new ConsumerParser());
                xr.parse(new InputSource(is));
                stm.close();
            } catch (Throwable e) {
                Diagnostics.m1953e(TAG(), e);
            }
            return true;
        }

        public void ProcessFailure() {
            Diagnostics.m1957w(RegisterMobileConsumer.TAG, "onError (" + this.mResponseCode + ") " + this.mResponse);
        }
    }

    public RegisterMobileConsumer(Context context) {
        this.context = context;
        new Thread(new C13091(context)).start();
    }
}
