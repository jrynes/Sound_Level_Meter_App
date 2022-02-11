package com.amazon.device.associates;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Error;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* compiled from: XMLParser */
class bm {
    private static final String f1204a;

    bm() {
    }

    static {
        f1204a = null;
    }

    protected String m803a(XmlPullParser xmlPullParser, String str) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, f1204a, str);
        String b = m800b(xmlPullParser);
        xmlPullParser.require(3, f1204a, str);
        return b;
    }

    private String m800b(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String str = Stomp.EMPTY;
        if (xmlPullParser.next() != 4) {
            return str;
        }
        str = xmlPullParser.getText();
        xmlPullParser.nextTag();
        return str;
    }

    protected void m804a(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() != 2) {
            throw new IllegalStateException();
        }
        int i = 1;
        while (i != 0) {
            switch (xmlPullParser.next()) {
                case Zone.SECONDARY /*2*/:
                    i++;
                    break;
                case Protocol.GGP /*3*/:
                    i--;
                    break;
                default:
                    break;
            }
        }
    }

    public String m802a(String str, String str2) {
        InputStream byteArrayInputStream;
        Throwable th;
        String str3 = null;
        if (!(str == null || str2 == null || Stomp.EMPTY.equals(str) || Stomp.EMPTY.equals(str2))) {
            try {
                byteArrayInputStream = new ByteArrayInputStream(str2.getBytes(HttpRequest.CHARSET_UTF8));
                try {
                    XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
                    newInstance.setNamespaceAware(true);
                    XmlPullParser newPullParser = newInstance.newPullParser();
                    newPullParser.setInput(byteArrayInputStream, null);
                    int eventType = newPullParser.getEventType();
                    while (eventType != 1) {
                        if (eventType == 2 && str.equals(newPullParser.getName())) {
                            newPullParser.next();
                            str3 = newPullParser.getText();
                            if (byteArrayInputStream != null) {
                                try {
                                    byteArrayInputStream.close();
                                } catch (IOException e) {
                                }
                            }
                        } else {
                            eventType = newPullParser.next();
                        }
                    }
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                } catch (Exception e3) {
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e4) {
                byteArrayInputStream = null;
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (IOException e5) {
                    }
                }
                return str3;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                byteArrayInputStream = null;
                th = th4;
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        }
        return str3;
    }

    public int m806c(String str) {
        try {
            String a = new bm().m802a("statusCode", str);
            if (a != null) {
                return Integer.parseInt(a);
            }
        } catch (Exception e) {
            Log.m1014a("XMLParser", "StatusCode parsing seems to be failed. Ex=", e);
        }
        return -1;
    }

    public String m805b(String str) {
        try {
            return new bm().m802a(Error.MESSAGE, str);
        } catch (Exception e) {
            return "Unknown Service Response";
        }
    }

    public long m801a(String str) {
        try {
            String a = new bm().m802a("cacheRefreshRate", str);
            if (a != null) {
                Log.m1018c("XMLParser", "CacheRefreshRate returned by service:" + a + "secs");
                return Long.parseLong(a) * 1000;
            }
        } catch (Exception e) {
            Log.m1014a("XMLParser", "CacheRefreshRate parsing seems to be failed. Ex=", e);
        }
        Log.m1013a("XMLParser", "CacheRefreshRate parsing seems to be failed. Hence returning -1");
        return -1;
    }
}
