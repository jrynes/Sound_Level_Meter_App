package com.amazon.device.associates;

import android.webkit.WebView;

public interface LinkService {
    void openRetailPage(OpenRetailPageRequest openRetailPageRequest);

    boolean overrideLinkInvocation(WebView webView, String str);
}
