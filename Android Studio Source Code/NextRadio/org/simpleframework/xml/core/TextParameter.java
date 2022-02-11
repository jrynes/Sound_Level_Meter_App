package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import org.apache.activemq.transport.stomp.Stomp;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.stream.Format;

class TextParameter extends TemplateParameter {
    private final Contact contact;
    private final Expression expression;
    private final int index;
    private final Object key;
    private final Label label;
    private final String name;
    private final String path;
    private final Class type;

    private static class Contact extends ParameterContact<Text> {
        public Contact(Text label, Constructor factory, int index) {
            super(label, factory, index);
        }

        public String getName() {
            return Stomp.EMPTY;
        }
    }

    public TextParameter(Constructor factory, Text value, Format format, int index) throws Exception {
        this.contact = new Contact(value, factory, index);
        this.label = new TextLabel(this.contact, value, format);
        this.expression = this.label.getExpression();
        this.path = this.label.getPath();
        this.type = this.label.getType();
        this.name = this.label.getName();
        this.key = this.label.getKey();
        this.index = index;
    }

    public Object getKey() {
        return this.key;
    }

    public String getPath() {
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public String getPath(Context context) {
        return getPath();
    }

    public String getName(Context context) {
        return getName();
    }

    public Class getType() {
        return this.type;
    }

    public Annotation getAnnotation() {
        return this.contact.getAnnotation();
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isRequired() {
        return this.label.isRequired();
    }

    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }

    public boolean isText() {
        return true;
    }

    public String toString() {
        return this.contact.toString();
    }
}
