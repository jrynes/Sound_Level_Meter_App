package org.simpleframework.xml.stream;

import com.rabbitmq.client.impl.AMQImpl.Basic;
import java.io.BufferedWriter;
import java.io.Writer;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

class Formatter {
    private static final char[] AND;
    private static final char[] CLOSE;
    private static final char[] DOUBLE;
    private static final char[] GREATER;
    private static final char[] LESS;
    private static final char[] NAMESPACE;
    private static final char[] OPEN;
    private static final char[] SINGLE;
    private OutputBuffer buffer;
    private Indenter indenter;
    private Tag last;
    private String prolog;
    private Writer result;

    private enum Tag {
        COMMENT,
        START,
        TEXT,
        END
    }

    static {
        NAMESPACE = new char[]{'x', 'm', 'l', 'n', 's'};
        LESS = new char[]{'&', 'l', 't', ';'};
        GREATER = new char[]{'&', 'g', 't', ';'};
        DOUBLE = new char[]{'&', 'q', 'u', 'o', 't', ';'};
        SINGLE = new char[]{'&', 'a', 'p', 'o', 's', ';'};
        AND = new char[]{'&', 'a', 'm', 'p', ';'};
        OPEN = new char[]{'<', '!', '-', '-', ' '};
        CLOSE = new char[]{' ', '-', '-', '>'};
    }

    public Formatter(Writer result, Format format) {
        this.result = new BufferedWriter(result, Flags.FLAG5);
        this.indenter = new Indenter(format);
        this.buffer = new OutputBuffer();
        this.prolog = format.getProlog();
    }

    public void writeProlog() throws Exception {
        if (this.prolog != null) {
            write(this.prolog);
            write(Stomp.NEWLINE);
        }
    }

    public void writeComment(String comment) throws Exception {
        String text = this.indenter.top();
        if (this.last == Tag.START) {
            append('>');
        }
        if (text != null) {
            append(text);
            append(OPEN);
            append(comment);
            append(CLOSE);
        }
        this.last = Tag.COMMENT;
    }

    public void writeStart(String name, String prefix) throws Exception {
        String text = this.indenter.push();
        if (this.last == Tag.START) {
            append('>');
        }
        flush();
        append(text);
        append('<');
        if (!isEmpty(prefix)) {
            append(prefix);
            append(':');
        }
        append(name);
        this.last = Tag.START;
    }

    public void writeAttribute(String name, String value, String prefix) throws Exception {
        if (this.last != Tag.START) {
            throw new NodeException("Start element required");
        }
        write(' ');
        write(name, prefix);
        write('=');
        write('\"');
        escape(value);
        write('\"');
    }

    public void writeNamespace(String reference, String prefix) throws Exception {
        if (this.last != Tag.START) {
            throw new NodeException("Start element required");
        }
        write(' ');
        write(NAMESPACE);
        if (!isEmpty(prefix)) {
            write(':');
            write(prefix);
        }
        write('=');
        write('\"');
        escape(reference);
        write('\"');
    }

    public void writeText(String text) throws Exception {
        writeText(text, Mode.ESCAPE);
    }

    public void writeText(String text, Mode mode) throws Exception {
        if (this.last == Tag.START) {
            write('>');
        }
        if (mode == Mode.DATA) {
            data(text);
        } else {
            escape(text);
        }
        this.last = Tag.TEXT;
    }

    public void writeEnd(String name, String prefix) throws Exception {
        String text = this.indenter.pop();
        if (this.last == Tag.START) {
            write('/');
            write('>');
        } else {
            if (this.last != Tag.TEXT) {
                write(text);
            }
            if (this.last != Tag.START) {
                write('<');
                write('/');
                write(name, prefix);
                write('>');
            }
        }
        this.last = Tag.END;
    }

    private void write(char ch) throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        this.result.write(ch);
    }

    private void write(char[] plain) throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        this.result.write(plain);
    }

    private void write(String plain) throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        this.result.write(plain);
    }

    private void write(String plain, String prefix) throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        if (!isEmpty(prefix)) {
            this.result.write(prefix);
            this.result.write(58);
        }
        this.result.write(plain);
    }

    private void append(char ch) throws Exception {
        this.buffer.append(ch);
    }

    private void append(char[] plain) throws Exception {
        this.buffer.append(plain);
    }

    private void append(String plain) throws Exception {
        this.buffer.append(plain);
    }

    private void data(String value) throws Exception {
        write("<![CDATA[");
        write(value);
        write("]]>");
    }

    private void escape(String value) throws Exception {
        int size = value.length();
        for (int i = 0; i < size; i++) {
            escape(value.charAt(i));
        }
    }

    private void escape(char ch) throws Exception {
        char[] text = symbol(ch);
        if (text != null) {
            write(text);
        } else {
            write(ch);
        }
    }

    public void flush() throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        this.result.flush();
    }

    private String unicode(char ch) {
        return Integer.toString(ch);
    }

    private boolean isEmpty(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return false;
    }

    private boolean isText(char ch) {
        switch (ch) {
            case Service.DISCARD /*9*/:
            case Protocol.BBN_RCC_MON /*10*/:
            case Service.DAYTIME /*13*/:
            case Protocol.MERIT_INP /*32*/:
                return true;
            default:
                if (ch <= ' ' || ch > '~') {
                    return false;
                }
                if (ch != '\u00f7') {
                    return true;
                }
                return false;
        }
    }

    private char[] symbol(char ch) {
        switch (ch) {
            case Type.ATMA /*34*/:
                return DOUBLE;
            case Type.A6 /*38*/:
                return AND;
            case Service.RLP /*39*/:
                return SINGLE;
            case Basic.INDEX /*60*/:
                return LESS;
            case Protocol.CFTP /*62*/:
                return GREATER;
            default:
                return null;
        }
    }
}
