package com.nextradioapp.core.serialization;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Interaction {
    @Attribute
    public String InteractionType;
    @Attribute
    public String InventoryID;

    public Interaction(String inventoryID, String interactionType) {
        this.InventoryID = inventoryID;
        this.InteractionType = interactionType;
    }
}
