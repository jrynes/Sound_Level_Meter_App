package com.nextradioapp.core.objects;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class CardData {
    @Element(required = false)
    public String action;
    @Element(required = false)
    public CardPropertyMap actionData;
    @Element(required = false)
    public String icon;
    @Element(required = false)
    public String imageURL;
    @Element(required = false)
    public String subtitle;
    @Element(required = false)
    public String text;
    @Element(required = false)
    public String title;
    @Element(required = false)
    public String trackingID;
}
