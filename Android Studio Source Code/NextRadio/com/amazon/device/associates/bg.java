package com.amazon.device.associates;

import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: RetailTemplateXMLParser */
public class bg extends bm {
    private static final String f1260b;
    private RetailURLTemplates f1261a;

    public bg() {
        this.f1261a = new RetailURLTemplates();
    }

    public /* bridge */ /* synthetic */ String m877a(String str, String str2) {
        return super.m802a(str, str2);
    }

    static {
        f1260b = null;
    }

    private void m872b(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, f1260b, "templateList");
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("member")) {
                    m873c(xmlPullParser);
                } else {
                    m804a(xmlPullParser);
                }
            }
        }
    }

    private void m873c(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String str = null;
        xmlPullParser.require(2, f1260b, "member");
        String str2 = null;
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("templateURL")) {
                    str2 = m803a(xmlPullParser, name);
                } else if (name.equals("templateName")) {
                    str = m803a(xmlPullParser, name);
                } else {
                    m804a(xmlPullParser);
                }
            }
        }
        this.f1261a.m975a(str, str2);
    }

    public RetailURLTemplates m876a(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser newPullParser = Xml.newPullParser();
            newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            newPullParser.setInput(inputStream, null);
            newPullParser.nextTag();
            m874d(newPullParser);
            RetailURLTemplates retailURLTemplates = this.f1261a;
            return retailURLTemplates;
        } finally {
            inputStream.close();
        }
    }

    private void m874d(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, f1260b, "URLTemplatesOutput");
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("templateList")) {
                    m872b(xmlPullParser);
                } else {
                    m804a(xmlPullParser);
                }
            }
        }
    }
}
