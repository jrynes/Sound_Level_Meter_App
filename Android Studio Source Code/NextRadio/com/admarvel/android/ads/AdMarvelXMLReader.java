package com.admarvel.android.ads;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;
import javax.xml.parsers.SAXParserFactory;
import org.apache.activemq.filter.DestinationFilter;
import org.apache.activemq.transport.stomp.Stomp;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class AdMarvelXMLReader extends DefaultHandler {
    private AdMarvelXMLElement f431a;
    private Stack<AdMarvelXMLElement> f432b;

    public AdMarvelXMLReader() {
        this.f431a = null;
        this.f432b = null;
    }

    private static String chkAndReplace(String value, String splChar, String replacedValue) {
        StringBuffer stringBuffer;
        if (value != null) {
            try {
                stringBuffer = new StringBuffer(value);
            } catch (Exception e) {
                Exception e2 = e;
                StringBuffer stringBuffer2 = null;
                e2.printStackTrace();
                stringBuffer = stringBuffer2;
                return stringBuffer != null ? stringBuffer.toString() : null;
            }
        } else {
            stringBuffer = new StringBuffer(Stomp.EMPTY);
        }
        int i = 0;
        while (true) {
            try {
                i = stringBuffer.toString().indexOf(splChar, i);
                if (i == -1) {
                    break;
                }
                stringBuffer.replace(i, splChar.length() + i, replacedValue);
                i++;
            } catch (Exception e3) {
                Exception exception = e3;
                stringBuffer2 = stringBuffer;
                e2 = exception;
            }
        }
        if (stringBuffer != null) {
        }
    }

    static String xmlDecode(String value) {
        return value != null ? chkAndReplace(chkAndReplace(chkAndReplace(chkAndReplace(value, "&amp;", "&"), "&quot;", "\""), "&gt;", DestinationFilter.ANY_DESCENDENT), "&lt;", "<") : null;
    }

    static String xmlEncode(String value) {
        return value != null ? chkAndReplace(chkAndReplace(chkAndReplace(chkAndReplace(value, "&", "&amp;"), "\"", "&quot;"), "<", "&lt;"), DestinationFilter.ANY_DESCENDENT, "&gt;") : null;
    }

    public void characters(char[] ch, int start, int length) {
        ((AdMarvelXMLElement) this.f432b.peek()).appendData(new String(ch).substring(start, start + length));
    }

    public void endElement(String uri, String name, String qName) {
        this.f432b.pop();
    }

    public AdMarvelXMLElement getParsedXMLData() {
        return this.f431a;
    }

    public void parseXMLString(String xmlString) {
        this.f432b = new Stack();
        XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        xMLReader.setContentHandler(this);
        xMLReader.parse(new InputSource(new StringReader(xmlString)));
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (atts != null) {
            int length = atts.getLength();
            for (int i = 0; i < length; i++) {
                linkedHashMap.put(atts.getLocalName(i), atts.getValue(i));
            }
        }
        AdMarvelXMLElement adMarvelXMLElement = new AdMarvelXMLElement(name, linkedHashMap);
        if (this.f432b.empty()) {
            this.f431a = adMarvelXMLElement;
        } else {
            AdMarvelXMLElement adMarvelXMLElement2 = (AdMarvelXMLElement) this.f432b.peek();
            ArrayList arrayList = (ArrayList) adMarvelXMLElement2.getChildren().get(name);
            if (arrayList == null) {
                arrayList = new ArrayList();
                adMarvelXMLElement2.getChildren().put(name, arrayList);
            }
            arrayList.add(adMarvelXMLElement);
        }
        this.f432b.push(adMarvelXMLElement);
    }
}
