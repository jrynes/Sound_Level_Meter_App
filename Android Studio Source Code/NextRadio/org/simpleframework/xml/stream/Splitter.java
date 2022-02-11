package org.simpleframework.xml.stream;

abstract class Splitter {
    protected StringBuilder builder;
    protected int count;
    protected int off;
    protected char[] text;

    protected abstract void commit(char[] cArr, int i, int i2);

    protected abstract void parse(char[] cArr, int i, int i2);

    public Splitter(String source) {
        this.builder = new StringBuilder();
        this.text = source.toCharArray();
        this.count = this.text.length;
    }

    public String process() {
        while (this.off < this.count) {
            while (this.off < this.count && isSpecial(this.text[this.off])) {
                this.off++;
            }
            if (!acronym()) {
                token();
                number();
            }
        }
        return this.builder.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void token() {
        /*
        r5 = this;
        r1 = r5.off;
    L_0x0002:
        r2 = r5.count;
        if (r1 >= r2) goto L_0x0010;
    L_0x0006:
        r2 = r5.text;
        r0 = r2[r1];
        r2 = r5.isLetter(r0);
        if (r2 != 0) goto L_0x002d;
    L_0x0010:
        r2 = r5.off;
        if (r1 <= r2) goto L_0x002a;
    L_0x0014:
        r2 = r5.text;
        r3 = r5.off;
        r4 = r5.off;
        r4 = r1 - r4;
        r5.parse(r2, r3, r4);
        r2 = r5.text;
        r3 = r5.off;
        r4 = r5.off;
        r4 = r1 - r4;
        r5.commit(r2, r3, r4);
    L_0x002a:
        r5.off = r1;
        return;
    L_0x002d:
        r2 = r5.off;
        if (r1 <= r2) goto L_0x0037;
    L_0x0031:
        r2 = r5.isUpper(r0);
        if (r2 != 0) goto L_0x0010;
    L_0x0037:
        r1 = r1 + 1;
        goto L_0x0002;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.simpleframework.xml.stream.Splitter.token():void");
    }

    private boolean acronym() {
        int mark = this.off;
        int size = 0;
        while (mark < this.count && isUpper(this.text[mark])) {
            size++;
            mark++;
        }
        if (size > 1) {
            if (mark < this.count && isUpper(this.text[mark - 1])) {
                mark--;
            }
            commit(this.text, this.off, mark - this.off);
            this.off = mark;
        }
        if (size > 1) {
            return true;
        }
        return false;
    }

    private boolean number() {
        int mark = this.off;
        int size = 0;
        while (mark < this.count && isDigit(this.text[mark])) {
            size++;
            mark++;
        }
        if (size > 0) {
            commit(this.text, this.off, mark - this.off);
        }
        this.off = mark;
        return size > 0;
    }

    private boolean isLetter(char ch) {
        return Character.isLetter(ch);
    }

    private boolean isSpecial(char ch) {
        return !Character.isLetterOrDigit(ch);
    }

    private boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    private boolean isUpper(char ch) {
        return Character.isUpperCase(ch);
    }

    protected char toUpper(char ch) {
        return Character.toUpperCase(ch);
    }

    protected char toLower(char ch) {
        return Character.toLowerCase(ch);
    }
}
