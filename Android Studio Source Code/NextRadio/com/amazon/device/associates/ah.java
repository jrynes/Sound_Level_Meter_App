package com.amazon.device.associates;

import android.webkit.WebView;
import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: OverrideLink */
class ah {
    ah() {
    }

    private static boolean m727a(String str) {
        if (str == null || (!str.matches("^((http|https)://)(www[.])?(amazon[.])(com|ca|cn|de|es|fr|it|in|co[.]jp|co[.]uk)(/)?.*") && !str.matches("^((http|https)://)(www[.])?(myhabit|amazonwireless|amazonsupply)([.]com)(/)?.*"))) {
            return false;
        }
        return true;
    }

    public static boolean m726a(WebView webView, String str) {
        RetailURLTemplates retailURLTemplates = (RetailURLTemplates) ((be) al.m752a(be.class)).m778j();
        if (str == null || str.trim().contentEquals(Stomp.EMPTY)) {
            Log.m1018c("OverrideLink", "Invalid input parameter. Input url " + str);
            return false;
        }
        String trim = str.trim();
        if (m727a(trim)) {
            String str2;
            if (trim.contains("amazon.com") && retailURLTemplates != null) {
                Log.m1013a("OverrideLink", "Map received");
                if (retailURLTemplates.m974a().containsKey(RetailURLTemplates.f1344c)) {
                    str2 = (String) retailURLTemplates.m974a().get(RetailURLTemplates.f1344c);
                    Log.m1018c("OverrideLink", "Parameter template: " + str2);
                } else {
                    str2 = null;
                }
                if (str2 != null) {
                    trim = br.m913e(trim);
                    int indexOf = trim.indexOf("#");
                    int indexOf2 = trim.indexOf("?");
                    if (indexOf == -1 || indexOf2 == -1) {
                        if (indexOf2 != -1) {
                            str2 = trim.substring(0, indexOf2 + 1) + str2 + "&" + trim.substring(indexOf2 + 1);
                        } else if (indexOf != -1) {
                            str2 = trim.substring(0, indexOf) + "?" + str2 + trim.substring(indexOf);
                        } else {
                            str2 = trim + "?" + str2;
                        }
                    } else if (indexOf < indexOf2) {
                        str2 = trim.substring(0, indexOf) + "?" + str2 + trim.substring(indexOf);
                    } else {
                        str2 = trim.substring(0, indexOf2 + 1) + str2 + "&" + trim.substring(indexOf2 + 1);
                    }
                    str2 = str2.replace("$SUBTAG", bp.m901b());
                    Log.m1018c("OverrideLink", "Loading amazon url in browser with additional parameters: " + str2);
                    br.m912d(str2);
                    return true;
                }
            }
            str2 = trim;
            Log.m1018c("OverrideLink", "Loading amazon url in browser with additional parameters: " + str2);
            br.m912d(str2);
            return true;
        }
        Log.m1018c("OverrideLink", "Loading specified url in webview: " + trim);
        webView.loadUrl(trim);
        return false;
    }
}
