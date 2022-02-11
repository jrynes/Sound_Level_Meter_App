package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Format;

class VersionLabel extends TemplateLabel {
    private Decorator decorator;
    private Introspector detail;
    private Format format;
    private Version label;
    private String name;
    private Expression path;
    private boolean required;
    private Class type;

    public VersionLabel(Contact contact, Version label, Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
        this.name = label.name();
        this.format = format;
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Converter getConverter(Context context) throws Exception {
        String ignore = getEmpty(context);
        Type type = getContact();
        if (context.isFloat(type)) {
            return new Primitive(context, type, ignore);
        }
        throw new AttributeException("Cannot use %s to represent %s", this.label, type);
    }

    public String getEmpty(Context context) {
        return null;
    }

    public String getName() throws Exception {
        return this.format.getStyle().getAttribute(this.detail.getName());
    }

    public String getPath() throws Exception {
        return getExpression().getAttribute(getName());
    }

    public Expression getExpression() throws Exception {
        if (this.path == null) {
            this.path = this.detail.getExpression();
        }
        return this.path;
    }

    public Annotation getAnnotation() {
        return this.label;
    }

    public String getOverride() {
        return this.name;
    }

    public Contact getContact() {
        return this.detail.getContact();
    }

    public Class getType() {
        return this.type;
    }

    public boolean isAttribute() {
        return true;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isData() {
        return false;
    }

    public String toString() {
        return this.detail.toString();
    }
}
