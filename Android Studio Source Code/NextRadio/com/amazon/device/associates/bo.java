package com.amazon.device.associates;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/* compiled from: GetCategorySearchTemplatesCall */
class bo extends ag {
    private static String f1274b;

    bo() {
    }

    static {
        f1274b = "getCategorySearchDetails";
    }

    protected void m894a() {
        this.a = new au(new StringBuilder("http://assoc-msdk-us.amazon-adsystem.com/" + f1274b).toString());
        this.a.m797a("MarketplaceID", "ATVPDKIKX0DER");
    }

    protected synchronized void m895b() {
        try {
            this.a.m796a(RequestMethod.GET);
        } catch (UnsupportedEncodingException e) {
            new MetricsRecorderCall(m896c() + ".failed", e.getClass().getSimpleName()).m973d();
        } catch (Exception e2) {
            new MetricsRecorderCall(m896c() + ".failed", e2.getClass().getSimpleName()).m973d();
        }
    }

    protected String m896c() {
        return "getCategorySearchDetails";
    }

    public bz m897d() {
        bz bzVar = null;
        String a = this.a.m795a();
        if (a == null) {
            Log.m1013a("getCategorySearchDetails", "DirectionShopping Response:Could not complete service call");
        } else {
            ax axVar = new ax();
            int c = axVar.m816c(a);
            if (c == -1) {
                Log.m1013a("getCategorySearchDetails", "DirectionShopping Response:Could not complete service call");
            } else {
                Log.m1013a("getCategorySearchDetails", "DirectionShopping Response:" + axVar.m815b(a));
                try {
                    bzVar = axVar.m813a(new ByteArrayInputStream(a.getBytes(HttpRequest.CHARSET_UTF8)));
                    bzVar.m890b(System.currentTimeMillis());
                } catch (Exception e) {
                    Log.m1013a("getCategorySearchDetails", "Parsing reponse failed. Ex=" + e);
                }
                bzVar.m892c(axVar.m812a(a));
                bzVar.m937a(bp.m901b());
                bzVar.m887a((long) c);
            }
        }
        return bzVar;
    }
}
