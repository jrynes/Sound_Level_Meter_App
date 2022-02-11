package org.apache.activemq.selector;

import java.io.Serializable;

public class Token implements Serializable {
    private static final long serialVersionUID = 1;
    public int beginColumn;
    public int beginLine;
    public int endColumn;
    public int endLine;
    public String image;
    public int kind;
    public Token next;
    public Token specialToken;

    public Object getValue() {
        return null;
    }

    public Token(int kind) {
        this(kind, null);
    }

    public Token(int kind, String image) {
        this.kind = kind;
        this.image = image;
    }

    public String toString() {
        return this.image;
    }

    public static Token newToken(int ofKind, String image) {
        return new Token(ofKind, image);
    }

    public static Token newToken(int ofKind) {
        return newToken(ofKind, null);
    }
}
