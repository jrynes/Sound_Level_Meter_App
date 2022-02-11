package org.simpleframework.xml.stream;

public class HyphenStyle implements Style {
    private final Builder builder;
    private final Style style;

    public HyphenStyle() {
        this.style = new HyphenBuilder();
        this.builder = new Builder(this.style);
    }

    public String getAttribute(String name) {
        return this.builder.getAttribute(name);
    }

    public void setAttribute(String name, String value) {
        this.builder.setAttribute(name, value);
    }

    public String getElement(String name) {
        return this.builder.getElement(name);
    }

    public void setElement(String name, String value) {
        this.builder.setElement(name, value);
    }
}
