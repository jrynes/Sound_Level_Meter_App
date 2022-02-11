package com.admarvel.android.ads;

import java.util.Map;

public class AdMarvelHttpPost {
    private String f168a;
    private String f169b;
    private Map<String, String> f170c;

    public String getEndpointUrl() {
        return this.f168a;
    }

    public Map<String, String> getHttpHeaders() {
        return this.f170c;
    }

    public String getPostString() {
        return this.f169b;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.f168a = endpointUrl;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.f170c = httpHeaders;
    }

    public void setPostString(String postString) {
        this.f169b = postString;
    }
}
