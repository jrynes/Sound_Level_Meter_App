package com.nextradioapp.core.objects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;

@Element(name = "Ad", required = false)
public class Enhancement {
    @ElementArray(required = false)
    public String[] datafields;
    @Attribute(required = true)
    public String type;

    public Enhancement(String Type, String Field0, String Field1, String Field2, String Field3, String Field4, String Field5) {
        this.type = Type;
        this.datafields = new String[]{Field0, Field1, Field2, Field3, Field4, Field5};
    }
}
