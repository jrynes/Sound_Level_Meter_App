package com.amazon.device.associates;

import java.util.HashMap;

/* renamed from: com.amazon.device.associates.k */
class RetailURLTemplates extends bl {
    public static String f1342a;
    public static String f1343b;
    public static String f1344c;
    public static String f1345d;
    public static String f1346e;
    public static String f1347f;
    public static String f1348g;
    public static String f1349h;
    public static String f1350i;
    private HashMap<String, String> f1351m;

    RetailURLTemplates() {
        this.f1351m = new HashMap();
    }

    static {
        f1342a = "AsinURL";
        f1343b = "HomePage";
        f1344c = "Parameter";
        f1345d = "SearchCategory";
        f1346e = "PreviewPage";
        f1347f = "UdpDetailPage";
        f1348g = "UdpMP3AlbumDetailPage";
        f1349h = "UdpMP3TrackDetailPage";
        f1350i = "GenericDetailPage";
    }

    public void m975a(String str, String str2) {
        this.f1351m.put(str, str2);
    }

    public HashMap<String, String> m974a() {
        return this.f1351m;
    }
}
