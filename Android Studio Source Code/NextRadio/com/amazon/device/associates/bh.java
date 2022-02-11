package com.amazon.device.associates;

import java.io.UnsupportedEncodingException;

/* compiled from: ImpressionRecorderCall */
class bh extends ag {
    private static final String f1262b;
    private static String f1263c;
    private String f1264d;
    private String f1265e;
    private String f1266f;
    private String f1267g;

    static {
        f1262b = bh.class.getName();
        f1263c = "e/ir";
    }

    public bh(String str, String str2, String str3) {
        this.f1265e = "1";
        this.f1264d = str;
        this.f1266f = str3;
        this.f1267g = str2;
    }

    protected void m880a() {
        this.a = new au(new StringBuilder("http://ir-na.amazon-adsystem.com/" + f1263c).toString());
        this.a.m797a("t", this.f1264d);
        this.a.m797a("o", this.f1265e);
        this.a.m797a("l", this.f1267g);
        this.a.m797a("a", this.f1266f);
    }

    protected synchronized void m881b() {
        try {
            this.a.m796a(RequestMethod.GET);
        } catch (UnsupportedEncodingException e) {
            Log.m1019c(f1262b, "Call to ImpressionRecorderCall URL failed. Ex=", e.getMessage());
        } catch (Exception e2) {
            Log.m1019c(f1262b, "Call to get ImpressionRecorderCall URL failed. Ex=", e2.getMessage());
        }
    }

    protected String m882c() {
        return "ImpressionRecorderCall";
    }
}
