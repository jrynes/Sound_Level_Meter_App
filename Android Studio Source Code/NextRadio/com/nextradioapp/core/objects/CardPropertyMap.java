package com.nextradioapp.core.objects;

import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

@Element(name = "actionData")
public class CardPropertyMap {
    @ElementMap(attribute = true, entry = "data", inline = true, key = "key", required = false)
    private Map<String, String> map;

    public Map<String, String> getMap() {
        if (this.map == null) {
            this.map = new HashMap();
        }
        return this.map;
    }
}
