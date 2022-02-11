package com.amazon.device.ads;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestFactory;
import com.nextradioapp.androidSDK.data.schema.Tables.stations;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;
import org.apache.activemq.filter.DestinationFilter;
import org.apache.activemq.transport.stomp.Stomp;

class WebUtils {
    private static final String LOGTAG;
    private static final MobileAdsLogger logger;

    /* renamed from: com.amazon.device.ads.WebUtils.1 */
    static class C03471 implements Runnable {
        final /* synthetic */ boolean val$disconnectEnabled;
        final /* synthetic */ String val$url;

        C03471(String str, boolean z) {
            this.val$url = str;
            this.val$disconnectEnabled = z;
        }

        public void run() {
            WebRequest request = new WebRequestFactory().createWebRequest();
            request.enableLog(true);
            request.setUrlString(this.val$url);
            request.setDisconnectEnabled(this.val$disconnectEnabled);
            try {
                request.makeCall();
            } catch (WebRequestException e) {
            }
        }
    }

    WebUtils() {
    }

    static {
        LOGTAG = WebUtils.class.getSimpleName();
        logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
    }

    public static boolean launchActivityForIntentLink(String url, Context context) {
        if (url == null || url.equals(Stomp.EMPTY)) {
            url = "about:blank";
        }
        logger.m637d("Launch Intent: " + url);
        Intent actionIntent = new Intent();
        if (url.startsWith("intent:")) {
            try {
                actionIntent = Intent.parseUri(url, 1);
            } catch (URISyntaxException e) {
                return false;
            }
        }
        actionIntent.setData(Uri.parse(url));
        actionIntent.setAction("android.intent.action.VIEW");
        actionIntent.setFlags(268435456);
        try {
            context.startActivity(actionIntent);
            return true;
        } catch (ActivityNotFoundException e2) {
            String action = actionIntent.getAction();
            logger.m645w("Could not handle " + (action.startsWith("market://") ? stations.market : "intent") + " action: " + action);
            return false;
        }
    }

    public static final String getURLEncodedString(String s) {
        if (s == null) {
            return null;
        }
        try {
            return URLEncoder.encode(s, HttpRequest.CHARSET_UTF8).replace("+", "%20").replace(DestinationFilter.ANY_CHILD, "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            logger.m638d("getURLEncodedString threw: %s", e);
            return s;
        }
    }

    public static final String getURLDecodedString(String s) {
        if (s == null) {
            return null;
        }
        try {
            return URLDecoder.decode(s, HttpRequest.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            logger.m638d("getURLDecodedString threw: %s", e);
            return s;
        }
    }

    public static final String getScheme(String url) {
        String scheme = Uri.parse(url).getScheme();
        if (scheme != null) {
            return scheme.toLowerCase(Locale.US);
        }
        return scheme;
    }

    public static final String encloseHtml(String html, boolean isHTML5) {
        if (html == null) {
            return html;
        }
        if (html.indexOf("<html>") == -1) {
            html = "<html>" + html + "</html>";
        }
        if (isHTML5 && html.indexOf("<!DOCTYPE html>") == -1) {
            return "<!DOCTYPE html>" + html;
        }
        return html;
    }

    public static final void executeWebRequestInThread(String url, boolean disconnectEnabled) {
        ThreadUtils.scheduleRunnable(new C03471(url, disconnectEnabled));
    }
}
