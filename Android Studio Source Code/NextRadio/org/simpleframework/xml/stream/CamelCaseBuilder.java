package org.simpleframework.xml.stream;

class CamelCaseBuilder implements Style {
    protected final boolean attribute;
    protected final boolean element;

    private class Attribute extends Splitter {
        private boolean capital;

        private Attribute(String source) {
            super(source);
        }

        protected void parse(char[] text, int off, int len) {
            if (CamelCaseBuilder.this.attribute || this.capital) {
                text[off] = toUpper(text[off]);
            }
            this.capital = true;
        }

        protected void commit(char[] text, int off, int len) {
            this.builder.append(text, off, len);
        }
    }

    private class Element extends Attribute {
        private boolean capital;

        private Element(String source) {
            super(source, null);
        }

        protected void parse(char[] text, int off, int len) {
            if (CamelCaseBuilder.this.element || this.capital) {
                text[off] = toUpper(text[off]);
            }
            this.capital = true;
        }
    }

    public CamelCaseBuilder(boolean element, boolean attribute) {
        this.attribute = attribute;
        this.element = element;
    }

    public String getAttribute(String name) {
        if (name != null) {
            return new Attribute(name, null).process();
        }
        return null;
    }

    public String getElement(String name) {
        if (name != null) {
            return new Element(name, null).process();
        }
        return null;
    }
}
