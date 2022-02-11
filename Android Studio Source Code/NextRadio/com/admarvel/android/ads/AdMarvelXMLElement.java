package com.admarvel.android.ads;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.apache.activemq.filter.DestinationFilter;

public class AdMarvelXMLElement {
    private LinkedHashMap<String, String> attributes;
    private LinkedHashMap<String, ArrayList<AdMarvelXMLElement>> children;
    private StringBuilder data;
    private String name;

    AdMarvelXMLElement(String name) {
        this.name = null;
        this.attributes = null;
        this.data = new StringBuilder();
        this.children = new LinkedHashMap();
        this.name = name;
    }

    AdMarvelXMLElement(String name, LinkedHashMap<String, String> attributes) {
        this.name = null;
        this.attributes = null;
        this.data = new StringBuilder();
        this.children = new LinkedHashMap();
        this.name = name;
        this.attributes = attributes;
    }

    public void appendData(String append) {
        this.data.append(append);
    }

    public LinkedHashMap<String, String> getAttributes() {
        return this.attributes;
    }

    public LinkedHashMap<String, ArrayList<AdMarvelXMLElement>> getChildren() {
        return this.children;
    }

    public String getData() {
        return this.data == null ? null : this.data.toString();
    }

    public String getName() {
        return this.name;
    }

    public void setChildren(LinkedHashMap<String, ArrayList<AdMarvelXMLElement>> children) {
        this.children = children;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<");
        stringBuilder.append(this.name);
        if (this.attributes != null) {
            for (String str : this.attributes.keySet()) {
                stringBuilder.append(" ");
                stringBuilder.append(str);
                stringBuilder.append("=");
                stringBuilder.append("\"");
                stringBuilder.append(AdMarvelXMLReader.xmlEncode((String) this.attributes.get(str)));
                stringBuilder.append("\"");
            }
        }
        stringBuilder.append(DestinationFilter.ANY_DESCENDENT);
        for (ArrayList it : this.children.values()) {
            Iterator it2 = it.iterator();
            while (it2.hasNext()) {
                stringBuilder.append(((AdMarvelXMLElement) it2.next()).toString());
            }
        }
        stringBuilder.append(AdMarvelXMLReader.xmlEncode(this.data.toString()));
        stringBuilder.append("</");
        stringBuilder.append(this.name);
        stringBuilder.append(DestinationFilter.ANY_DESCENDENT);
        return stringBuilder.toString();
    }
}
