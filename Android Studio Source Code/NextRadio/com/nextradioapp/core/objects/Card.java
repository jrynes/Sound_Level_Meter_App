package com.nextradioapp.core.objects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Card {
    @ElementArray(required = false)
    public CardButton[] buttons;
    @Attribute(required = true)
    public String cardID;
    @Attribute(required = true)
    public String cardStyle;
    @ElementArray(required = false)
    public CardData[] data;
    @Attribute(required = false)
    public boolean forceRefresh;
    @Element(required = false)
    public String imageurl;
    @Element(required = false)
    public String subtitle;
    @Element(required = false)
    public String title;
}
