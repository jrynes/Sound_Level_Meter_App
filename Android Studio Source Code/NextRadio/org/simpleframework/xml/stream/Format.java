package org.simpleframework.xml.stream;

public class Format {
    private final int indent;
    private final String prolog;
    private final Style style;
    private final Verbosity verbosity;

    public Format() {
        this(3);
    }

    public Format(int indent) {
        this(indent, null, new IdentityStyle());
    }

    public Format(String prolog) {
        this(3, prolog);
    }

    public Format(int indent, String prolog) {
        this(indent, prolog, new IdentityStyle());
    }

    public Format(Verbosity verbosity) {
        this(3, verbosity);
    }

    public Format(int indent, Verbosity verbosity) {
        this(indent, new IdentityStyle(), verbosity);
    }

    public Format(Style style) {
        this(3, style);
    }

    public Format(Style style, Verbosity verbosity) {
        this(3, style, verbosity);
    }

    public Format(int indent, Style style) {
        this(indent, null, style);
    }

    public Format(int indent, Style style, Verbosity verbosity) {
        this(indent, null, style, verbosity);
    }

    public Format(int indent, String prolog, Style style) {
        this(indent, prolog, style, Verbosity.HIGH);
    }

    public Format(int indent, String prolog, Style style, Verbosity verbosity) {
        this.verbosity = verbosity;
        this.prolog = prolog;
        this.indent = indent;
        this.style = style;
    }

    public int getIndent() {
        return this.indent;
    }

    public String getProlog() {
        return this.prolog;
    }

    public Style getStyle() {
        return this.style;
    }

    public Verbosity getVerbosity() {
        return this.verbosity;
    }
}
