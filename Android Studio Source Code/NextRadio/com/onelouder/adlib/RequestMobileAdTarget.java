package com.onelouder.adlib;

import android.content.Context;
import com.admarvel.android.ads.Constants;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.SAXParserFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

class RequestMobileAdTarget {
    private static final String ENDPOINT_DEV = "https://mss-dev.handmark.com/mss/adTarget?";
    private static final String ENDPOINT_PROD = "https://mss.handmark.com/mss/adTarget?";
    private static final String ENDPOINT_QA = "https://mss-qa.handmark.com/mss/adTarget?";
    private static final String ENDPOINT_STAGE = "https://mss-stage.handmark.com/mss/adTarget?";
    private static final String TAG = "RequestMobileAdTarget";
    private Context context;

    class AdTargetParser extends DefaultHandler {
        String curMediator;
        StringBuilder sbArguments;
        StringBuilder sbValue;
        int valueCount;

        AdTargetParser() {
            this.curMediator = null;
            this.sbArguments = null;
            this.sbValue = null;
            this.valueCount = 0;
        }

        public void startElement(String uri, String name, String qName, Attributes atts) {
            int interval;
            if (name.equals("adtarget")) {
                String update = atts.getValue("update");
                if (update != null) {
                    interval = Integer.parseInt(update) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
                    Preferences.setAdTargetingInterval(RequestMobileAdTarget.this.context, interval);
                    Preferences.setAdTargetingUpdate(RequestMobileAdTarget.this.context, System.currentTimeMillis() + ((long) interval));
                }
            } else if (name.equals("refreshing")) {
                String ready = atts.getValue("ready");
                if (ready != null) {
                    interval = Integer.parseInt(ready) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
                    Preferences.setAdTargetingInterval(RequestMobileAdTarget.this.context, interval);
                    Preferences.setAdTargetingUpdate(RequestMobileAdTarget.this.context, System.currentTimeMillis() + ((long) interval));
                }
            } else if (name.equals("mediator")) {
                this.curMediator = atts.getValue("name");
                this.sbArguments = new StringBuilder();
            } else if (name.equals("argument")) {
                String argument = atts.getValue("name");
                if (argument != null) {
                    if (this.sbArguments.length() > 0) {
                        this.sbArguments.append(Headers.SEPERATOR);
                    }
                    this.sbArguments.append(argument);
                    this.sbArguments.append("=");
                }
                this.valueCount = 0;
            } else if (name.equals(Constants.NATIVE_AD_VALUE_ELEMENT)) {
                this.sbValue = new StringBuilder();
            }
        }

        public void endElement(String uri, String name, String qName) throws SAXException {
            if (name.equals("mediator")) {
                if (!(this.curMediator == null || this.sbArguments == null || this.sbArguments.length() <= 0)) {
                    Preferences.setMediatorArguments(RequestMobileAdTarget.this.context, this.curMediator.toLowerCase(), this.sbArguments.toString());
                }
                this.sbArguments = null;
                this.curMediator = null;
            } else if (!(!name.equals(Constants.NATIVE_AD_VALUE_ELEMENT) || this.sbValue == null || this.sbArguments == null)) {
                if (this.valueCount > 0) {
                    this.sbArguments.append(Stomp.COMMA);
                }
                this.sbArguments.append(this.sbValue);
                this.valueCount++;
            }
            this.sbValue = null;
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            if (this.sbValue != null) {
                this.sbValue.append(ch, start, length);
            }
        }
    }

    class AdTargetRequest extends ServerBase {
        public AdTargetRequest(String url) {
            this.mUrl = url;
            this.mZeroBytesAllowed = false;
        }

        protected String TAG() {
            return "AdTargetRequest";
        }

        public void addCustomConnectionInfo(HttpURLConnection connection) {
            String etag = Preferences.getMobileConsumerEtag(RequestMobileAdTarget.this.context);
            if (etag.length() > 0) {
                connection.setRequestProperty(HttpRequest.HEADER_IF_NONE_MATCH, etag);
            }
        }

        public void processResponseHeaders(Map<String, List<String>> headers) {
            if (headers != null && headers.containsKey(HttpRequest.HEADER_ETAG)) {
                List<String> values = (List) headers.get(HttpRequest.HEADER_ETAG);
                if (values != null && values.size() > 0) {
                    Preferences.setMobileConsumerEtag(RequestMobileAdTarget.this.context, (String) values.get(0));
                }
            }
        }

        public boolean ProcessResponse() throws EOFException {
            try {
                InputStream stm = new ByteArrayInputStream(this.data);
                InputStream is = stm;
                if (this.mResponseGzipped) {
                    is = new GZIPInputStream(stm);
                }
                XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                xr.setContentHandler(new AdTargetParser());
                xr.parse(new InputSource(is));
                stm.close();
            } catch (Throwable e) {
                Diagnostics.m1953e(TAG(), e);
            }
            return true;
        }

        public void ProcessFailure() {
            Diagnostics.m1957w(RequestMobileAdTarget.TAG, "onError (" + this.mResponseCode + ") " + this.mResponse);
            if (this.mResponseCode == 410) {
                Preferences.setMobileConsumerId(RequestMobileAdTarget.this.context, Stomp.EMPTY);
                Preferences.setMobileConsumerEtag(RequestMobileAdTarget.this.context, Stomp.EMPTY);
            }
        }
    }

    public RequestMobileAdTarget(Context context) {
        this.context = context;
        String url = null;
        if (Preferences.isProdEnv(context)) {
            url = ENDPOINT_PROD;
        } else if (Preferences.isQaEnv(context)) {
            url = ENDPOINT_QA;
        } else if (Preferences.isDevEnv(context)) {
            url = ENDPOINT_DEV;
        } else if (Preferences.isStageEnv(context)) {
            url = ENDPOINT_STAGE;
        }
        if (url != null) {
            StringBuilder sbUrl = new StringBuilder(url);
            boolean bParamAdded = false;
            try {
                String tsn = Preferences.getSimplePref(context, "ads-tsn", null);
                if (tsn != null) {
                    String tsn2 = new String(Base64.decode(tsn.getBytes()));
                    sbUrl.append("twitterSN=");
                    sbUrl.append(URLEncoder.encode(tsn2, HttpRequest.CHARSET_UTF8));
                    bParamAdded = true;
                    tsn = tsn2;
                }
                String tfnds = Preferences.getSimplePref(context, "ads-tfnds", null);
                if (tfnds != null) {
                    String tfnds2 = new String(Base64.decode(tfnds.getBytes()));
                    if (bParamAdded) {
                        sbUrl.append("&");
                    }
                    sbUrl.append("twitterFriends=");
                    sbUrl.append(tfnds2);
                    bParamAdded = true;
                    tfnds = tfnds2;
                }
            } catch (Throwable e) {
                Diagnostics.m1953e(TAG, e);
            }
            if (bParamAdded) {
                sbUrl.append("&");
            }
            sbUrl.append("consumer=");
            sbUrl.append(Preferences.getMobileConsumerId(context));
            new Thread(new AdTargetRequest(sbUrl.toString())).start();
        }
    }
}
