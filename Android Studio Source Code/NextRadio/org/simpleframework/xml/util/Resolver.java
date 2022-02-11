package org.simpleframework.xml.util;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.xbill.DNS.KEYRecord.Flags;

public class Resolver<M extends Match> extends AbstractSet<M> {
    protected final Cache cache;
    protected final Stack stack;

    private class Cache extends LimitedCache<List<M>> {
        public Cache() {
            super(Flags.FLAG5);
        }
    }

    private class Stack extends LinkedList<M> {

        private class Sequence implements Iterator<M> {
            private int cursor;

            public Sequence() {
                this.cursor = Stack.this.size();
            }

            public M next() {
                if (!hasNext()) {
                    return null;
                }
                Stack stack = Stack.this;
                int i = this.cursor - 1;
                this.cursor = i;
                return (Match) stack.get(i);
            }

            public boolean hasNext() {
                return this.cursor > 0;
            }

            public void remove() {
                Stack.this.purge(this.cursor);
            }
        }

        private Stack() {
        }

        public void push(M match) {
            Resolver.this.cache.clear();
            addFirst(match);
        }

        public void purge(int index) {
            Resolver.this.cache.clear();
            remove(index);
        }

        public Iterator<M> sequence() {
            return new Sequence();
        }
    }

    public Resolver() {
        this.stack = new Stack();
        this.cache = new Cache();
    }

    public M resolve(String text) {
        List<M> list = (List) this.cache.get(text);
        if (list == null) {
            list = resolveAll(text);
        }
        if (list.isEmpty()) {
            return null;
        }
        return (Match) list.get(0);
    }

    public List<M> resolveAll(String text) {
        List<M> list = (List) this.cache.get(text);
        if (list != null) {
            return list;
        }
        char[] array = text.toCharArray();
        if (array == null) {
            return null;
        }
        return resolveAll(text, array);
    }

    private List<M> resolveAll(String text, char[] array) {
        List<M> list = new ArrayList();
        Iterator i$ = this.stack.iterator();
        while (i$.hasNext()) {
            Match match = (Match) i$.next();
            if (match(array, match.getPattern().toCharArray())) {
                this.cache.put(text, list);
                list.add(match);
            }
        }
        return list;
    }

    public boolean add(M match) {
        this.stack.push((Match) match);
        return true;
    }

    public Iterator<M> iterator() {
        return this.stack.sequence();
    }

    public boolean remove(M match) {
        this.cache.clear();
        return this.stack.remove(match);
    }

    public int size() {
        return this.stack.size();
    }

    public void clear() {
        this.cache.clear();
        this.stack.clear();
    }

    private boolean match(char[] text, char[] wild) {
        return match(text, 0, wild, 0);
    }

    private boolean match(char[] text, int off, char[] wild, int pos) {
        while (pos < wild.length && off < text.length) {
            if (wild[pos] == '*') {
                while (wild[pos] == '*') {
                    pos++;
                    if (pos >= wild.length) {
                        return true;
                    }
                }
                if (wild[pos] == '?') {
                    pos++;
                    if (pos >= wild.length) {
                        return true;
                    }
                }
                while (off < text.length) {
                    if (text[off] == wild[pos] || wild[pos] == '?') {
                        if (wild[pos - 1] == '?') {
                            break;
                        } else if (match(text, off, wild, pos)) {
                            return true;
                        }
                    }
                    off++;
                }
                if (text.length == off) {
                    return false;
                }
            }
            int off2 = off + 1;
            int pos2 = pos + 1;
            if (text[off] == wild[pos] || wild[pos2 - 1] == '?') {
                pos = pos2;
                off = off2;
            } else {
                pos = pos2;
                off = off2;
                return false;
            }
        }
        if (wild.length != pos) {
            while (wild[pos] == '*') {
                pos++;
                if (pos >= wild.length) {
                    return true;
                }
            }
            return false;
        } else if (text.length != off) {
            return false;
        } else {
            return true;
        }
    }
}
