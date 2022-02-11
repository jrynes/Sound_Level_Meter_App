package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;

abstract class TemplateLabel implements Label {
    private final KeyBuilder builder;

    protected TemplateLabel() {
        this.builder = new KeyBuilder(this);
    }

    public Type getType(Class type) throws Exception {
        return getContact();
    }

    public Label getLabel(Class type) throws Exception {
        return this;
    }

    public String[] getNames() throws Exception {
        String path = getPath();
        String name = getName();
        return new String[]{path, name};
    }

    public String[] getPaths() throws Exception {
        return new String[]{getPath()};
    }

    public Object getKey() throws Exception {
        return this.builder.getKey();
    }

    public String getEntry() throws Exception {
        return null;
    }

    public Type getDependent() throws Exception {
        return null;
    }

    public boolean isAttribute() {
        return false;
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isInline() {
        return false;
    }

    public boolean isText() {
        return false;
    }

    public boolean isTextList() {
        return false;
    }

    public boolean isUnion() {
        return false;
    }
}
