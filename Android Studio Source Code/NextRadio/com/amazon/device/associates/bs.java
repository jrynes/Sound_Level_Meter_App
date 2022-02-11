package com.amazon.device.associates;

import java.io.Serializable;
import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: PopoverData */
final class bs implements Serializable {
    private String f1286a;
    private String f1287b;
    private String f1288c;
    private String f1289d;
    private double f1290e;
    private String f1291f;
    private String f1292g;
    private String f1293h;
    private boolean f1294i;

    public bs(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        this.f1286a = Stomp.EMPTY;
        this.f1287b = Stomp.EMPTY;
        this.f1288c = Stomp.EMPTY;
        this.f1289d = Stomp.EMPTY;
        this.f1290e = 0.0d;
        this.f1291f = Stomp.EMPTY;
        this.f1292g = Stomp.EMPTY;
        this.f1293h = Stomp.EMPTY;
        this.f1294i = false;
        try {
            this.f1286a = str;
            this.f1287b = str2;
            this.f1288c = str3;
            this.f1289d = m914a(str4);
            if (!(str6 == null || str6.trim().equals(Stomp.EMPTY))) {
                this.f1291f = "(" + str6.trim() + ")";
            }
            this.f1292g = str7;
            this.f1293h = str8;
            if (str5 != null && !str5.trim().equals(Stomp.EMPTY)) {
                this.f1290e = Double.parseDouble(str5);
            }
        } catch (NumberFormatException e) {
            Log.m1016b("PopoverDataLog", "Failed to load data.");
        }
    }

    private String m914a(String str) {
        if (str != null) {
            try {
                if (!str.trim().equals(Stomp.EMPTY) && str.length() > 2) {
                    if (Double.parseDouble(str.trim().substring(1).replace(Stomp.COMMA, Stomp.EMPTY).replace(" ", Stomp.EMPTY)) < 0.0d) {
                        return str;
                    }
                    this.f1294i = true;
                    return str;
                }
            } catch (Exception e) {
                Log.m1021d("PopoverData", "Parsing of price failed, invalidating the data", e);
                this.f1294i = false;
                return Stomp.EMPTY;
            }
        }
        if (str != null && !str.trim().equals(Stomp.EMPTY)) {
            return str;
        }
        this.f1294i = true;
        return Stomp.EMPTY;
    }

    public String m915a() {
        return this.f1287b;
    }

    public String m916b() {
        return this.f1288c;
    }

    public String m917c() {
        return this.f1289d;
    }

    public double m918d() {
        return this.f1290e;
    }

    public String m919e() {
        return this.f1291f;
    }

    public String m920f() {
        return this.f1292g;
    }

    public boolean m921g() {
        return this.f1294i;
    }

    public String m922h() {
        return this.f1293h;
    }

    public String toString() {
        return "PopoverData [asin=" + this.f1286a + ", iconPath=" + this.f1287b + ", title=" + this.f1288c + ", price=" + this.f1289d + ", rating=" + this.f1290e + ", reviewersCount=" + this.f1291f + ", detailPageURL=" + this.f1292g + ", category=" + this.f1293h + ", valid=" + this.f1294i + "]";
    }
}
