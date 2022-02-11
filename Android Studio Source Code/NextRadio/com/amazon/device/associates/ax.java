package com.amazon.device.associates;

import android.util.Xml;
import com.admarvel.android.ads.Constants;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: SearchCategoryXMLParser */
public class ax extends bm {
    private static final String f1205b;
    private bz f1206a;

    public ax() {
        this.f1206a = new bz();
    }

    public /* bridge */ /* synthetic */ String m814a(String str, String str2) {
        return super.m802a(str, str2);
    }

    static {
        f1205b = null;
    }

    private void m807b(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, f1205b, "sortParamterMap");
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("entry")) {
                    m809d(xmlPullParser);
                } else {
                    m804a(xmlPullParser);
                }
            }
        }
    }

    private void m808c(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, f1205b, "searchAliasMap");
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("entry")) {
                    m810e(xmlPullParser);
                } else {
                    m804a(xmlPullParser);
                }
            }
        }
    }

    private void m809d(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String str = null;
        xmlPullParser.require(2, f1205b, "entry");
        String str2 = null;
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals(Constants.NATIVE_AD_KEY_ELEMENT)) {
                    str2 = m803a(xmlPullParser, name);
                } else if (name.equals(Constants.NATIVE_AD_VALUE_ELEMENT)) {
                    str = m803a(xmlPullParser, name);
                } else {
                    m804a(xmlPullParser);
                }
            }
        }
        this.f1206a.m938a(str2, str);
    }

    private void m810e(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String str = null;
        xmlPullParser.require(2, f1205b, "entry");
        String str2 = null;
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals(Constants.NATIVE_AD_KEY_ELEMENT)) {
                    str2 = m803a(xmlPullParser, name);
                } else if (name.equals(Constants.NATIVE_AD_VALUE_ELEMENT)) {
                    str = m803a(xmlPullParser, name);
                } else {
                    m804a(xmlPullParser);
                }
            }
        }
        this.f1206a.m939b(str2, str);
    }

    public bz m813a(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser newPullParser = Xml.newPullParser();
            newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            newPullParser.setInput(inputStream, null);
            newPullParser.nextTag();
            m811f(newPullParser);
            bz bzVar = this.f1206a;
            return bzVar;
        } finally {
            inputStream.close();
        }
    }

    private void m811f(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, f1205b, "GetCategorySearchDetailsOutput");
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("sortParamterMap")) {
                    m807b(xmlPullParser);
                } else if (name.equals("searchAliasMap")) {
                    m808c(xmlPullParser);
                } else {
                    m804a(xmlPullParser);
                }
            }
        }
    }
}
