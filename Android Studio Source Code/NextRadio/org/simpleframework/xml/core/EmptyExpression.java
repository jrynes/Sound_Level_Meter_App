package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.Style;

class EmptyExpression implements Expression {
    private final List<String> list;
    private final Style style;

    public EmptyExpression(Format format) {
        this.list = new LinkedList();
        this.style = format.getStyle();
    }

    public Iterator<String> iterator() {
        return this.list.iterator();
    }

    public int getIndex() {
        return 0;
    }

    public String getPrefix() {
        return null;
    }

    public String getFirst() {
        return null;
    }

    public String getLast() {
        return null;
    }

    public String getPath() {
        return Stomp.EMPTY;
    }

    public String getElement(String name) {
        return this.style.getElement(name);
    }

    public String getAttribute(String name) {
        return this.style.getAttribute(name);
    }

    public Expression getPath(int from) {
        return null;
    }

    public Expression getPath(int from, int trim) {
        return null;
    }

    public boolean isAttribute() {
        return false;
    }

    public boolean isPath() {
        return false;
    }

    public boolean isEmpty() {
        return true;
    }
}
