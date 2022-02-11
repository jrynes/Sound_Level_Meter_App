package com.nextradioapp.core.objects;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class CardButton {
    @Element(required = false)
    public String action;
    @Element(required = false)
    public CardPropertyMap data;
    @Element(required = false)
    public String icon;
    @Element(required = false)
    public String label;
    @Element(required = false)
    public String trackingID;
}
