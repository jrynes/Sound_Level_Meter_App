package com.amazon.device.associates;

import com.rabbitmq.client.AMQP;
import java.io.UnsupportedEncodingException;

/* compiled from: GetPopoverTemplatesCall */
class ac extends ag {
    private static final String f1159b;
    private static String f1160c;
    private final String f1161d;
    private final String f1162e;
    private final String f1163f;

    static {
        f1159b = ac.class.getName();
        f1160c = "getPopoverTemplates";
    }

    public ac(String str, String str2, String str3) {
        this.f1161d = str;
        this.f1163f = str2;
        this.f1162e = str3;
    }

    protected void m706a() {
        this.a = new au(new StringBuilder("http://assoc-msdk-us.amazon-adsystem.com/" + f1160c).toString());
        this.a.m797a("MarketplaceID", this.f1161d);
        this.a.m797a("templateName", this.f1163f);
        this.a.m797a("sdkVersion", this.f1162e);
    }

    protected synchronized void m707b() {
        try {
            this.a.m796a(RequestMethod.GET);
        } catch (UnsupportedEncodingException e) {
            Log.m1019c(f1159b, "Call to get ad URL failed. Ex=", e.getMessage());
            new MetricsRecorderCall(m708c() + ".failed").m973d();
        } catch (Exception e2) {
            Log.m1019c(f1159b, "Call to get ad URL failed. Ex=", e2.getMessage());
            new MetricsRecorderCall(m708c() + ".failed").m973d();
        }
    }

    protected String m708c() {
        return "GetAmazonURLService";
    }

    public String m709d() {
        String str = null;
        String a = this.a.m795a();
        if (a == null) {
            Log.m1013a(f1159b, "Could not complete service call " + m708c());
        } else {
            bm bmVar = new bm();
            if (bmVar.m806c(a) != AMQP.REPLY_SUCCESS) {
                Log.m1013a(f1159b, "Error in getting popover template");
            } else {
                try {
                    str = bmVar.m802a("templateURL", a);
                } catch (Exception e) {
                    Log.m1016b(f1159b, "Parsing reponse failed. Ex=" + e);
                }
            }
        }
        return str;
    }
}
