package org.simpleframework.xml.stream;

import org.simpleframework.xml.util.Cache;
import org.simpleframework.xml.util.ConcurrentCache;

class Builder implements Style {
    private final Cache<String> attributes;
    private final Cache<String> elements;
    private final Style style;

    public Builder(Style style) {
        this.attributes = new ConcurrentCache();
        this.elements = new ConcurrentCache();
        this.style = style;
    }

    public String getAttribute(String name) {
        String value = (String) this.attributes.fetch(name);
        if (value != null) {
            return value;
        }
        value = this.style.getAttribute(name);
        if (value != null) {
            this.attributes.cache(name, value);
        }
        return value;
    }

    public String getElement(String name) {
        String value = (String) this.elements.fetch(name);
        if (value != null) {
            return value;
        }
        value = this.style.getElement(name);
        if (value != null) {
            this.elements.cache(name, value);
        }
        return value;
    }

    public void setAttribute(String name, String value) {
        this.attributes.cache(name, value);
    }

    public void setElement(String name, String value) {
        this.elements.cache(name, value);
    }
}
