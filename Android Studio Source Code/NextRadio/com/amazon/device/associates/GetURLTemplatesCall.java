package com.amazon.device.associates;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/* renamed from: com.amazon.device.associates.l */
class GetURLTemplatesCall extends ag {
    private static String f1352b;
    private String f1353c;
    private String f1354d;

    GetURLTemplatesCall() {
    }

    static {
        f1352b = "getURLTemplates";
    }

    public void m977a(String str) {
        this.f1353c = str;
        this.f1354d = "ATVPDKIKX0DER";
    }

    protected void m976a() {
        this.a = new au(new StringBuilder("http://assoc-msdk-us.amazon-adsystem.com/" + f1352b).toString());
        this.a.m797a("AppID", this.f1353c);
        this.a.m797a("MarketplaceID", this.f1354d);
    }

    protected synchronized void m978b() {
        try {
            this.a.m796a(RequestMethod.GET);
        } catch (UnsupportedEncodingException e) {
            Log.m1019c("GetAmazonURLCall", "Call to get ad URL failed. Ex=", e.getMessage());
            new MetricsRecorderCall(m979c() + ".failed", e.getClass().getSimpleName()).m973d();
        } catch (Exception e2) {
            Log.m1019c("GetAmazonURLCall", "Call to get ad URL failed. Ex=", e2.getMessage());
            new MetricsRecorderCall(m979c() + ".failed", e2.getClass().getSimpleName()).m973d();
        }
    }

    protected String m979c() {
        return "GetAmazonURLService";
    }

    public RetailURLTemplates m980d() {
        RetailURLTemplates retailURLTemplates = null;
        String a = this.a.m795a();
        if (a == null) {
            Log.m1013a("GetAmazonURLCall", "AmazonPurchasingAPI Response:Could not complete service call");
        } else {
            bg bgVar = new bg();
            int c = bgVar.m879c(a);
            if (c == -1) {
                Log.m1013a("GetAmazonURLCall", "AmazonPurchasingAPI Response:Could not complete service call");
            } else {
                Log.m1013a("GetAmazonURLCall", "AmazonPurchasingAPI Response:" + bgVar.m878b(a));
                try {
                    retailURLTemplates = bgVar.m876a(new ByteArrayInputStream(a.getBytes(HttpRequest.CHARSET_UTF8)));
                    retailURLTemplates.m890b(System.currentTimeMillis());
                } catch (Exception e) {
                    Log.m1016b("GetAmazonURLCall", "Parsing reponse failed. Ex=" + e);
                }
                retailURLTemplates.m892c(bgVar.m875a(a));
                retailURLTemplates.m888a(this.f1353c);
                retailURLTemplates.m887a((long) c);
            }
        }
        return retailURLTemplates;
    }
}
