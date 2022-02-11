package com.amazon.device.associates;

import android.content.Context;
import android.webkit.WebView;
import com.admarvel.android.ads.Constants;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.Set;

/* compiled from: LinkServiceImpl */
public class bd implements LinkService {
    private af f1256a;

    protected bd(Context context, String str, Set<String> set) {
        this.f1256a = af.m719a(context, str, (Set) set);
    }

    public void openRetailPage(OpenRetailPageRequest openRetailPageRequest) {
        ar.m782a((Object) openRetailPageRequest, Constants.AD_REQUEST);
        this.f1256a.m723a(openRetailPageRequest);
    }

    public boolean overrideLinkInvocation(WebView webView, String str) {
        ar.m782a((Object) webView, "webView");
        ar.m782a((Object) str, SettingsJsonConstants.APP_URL_KEY);
        this.f1256a.m724a(webView, str);
        return true;
    }
}
