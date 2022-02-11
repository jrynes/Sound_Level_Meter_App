package com.nextradioapp.core.serialization;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Impression {
    @Attribute
    public String ImpressionCount;
    @Attribute
    public String InventoryID;

    public Impression(String inventoryID, String impressionCount) {
        this.InventoryID = inventoryID;
        this.ImpressionCount = impressionCount;
    }
}
