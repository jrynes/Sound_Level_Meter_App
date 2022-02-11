package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.apache.activemq.transport.stomp.Stomp;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Format;

class TextLabel extends TemplateLabel {
    private Contact contact;
    private boolean data;
    private Introspector detail;
    private String empty;
    private Text label;
    private Expression path;
    private boolean required;
    private Class type;

    public TextLabel(Contact contact, Text label, Format format) {
        this.detail = new Introspector(contact, this, format);
        this.required = label.required();
        this.type = contact.getType();
        this.empty = label.empty();
        this.data = label.data();
        this.contact = contact;
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return null;
    }

    public Converter getConverter(Context context) throws Exception {
        String ignore = getEmpty(context);
        Type type = getContact();
        if (context.isPrimitive(type)) {
            return new Primitive(context, type, ignore);
        }
        throw new TextException("Cannot use %s to represent %s", type, this.label);
    }

    public String getEmpty(Context context) {
        if (this.detail.isEmpty(this.empty)) {
            return null;
        }
        return this.empty;
    }

    public String getPath() throws Exception {
        return getExpression().getPath();
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

    public Contact getContact() {
        return this.contact;
    }

    public String getName() {
        return Stomp.EMPTY;
    }

    public String getOverride() {
        return this.contact.toString();
    }

    public Class getType() {
        return this.type;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isData() {
        return this.data;
    }

    public boolean isText() {
        return true;
    }

    public boolean isInline() {
        return true;
    }

    public String toString() {
        return this.detail.toString();
    }
}
