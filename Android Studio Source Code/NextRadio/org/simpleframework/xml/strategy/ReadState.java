package org.simpleframework.xml.strategy;

import org.simpleframework.xml.util.WeakCache;

class ReadState extends WeakCache<ReadGraph> {
    private final Contract contract;
    private final Loader loader;

    public ReadState(Contract contract) {
        this.loader = new Loader();
        this.contract = contract;
    }

    public ReadGraph find(Object map) throws Exception {
        ReadGraph read = (ReadGraph) fetch(map);
        return read != null ? read : create(map);
    }

    private ReadGraph create(Object map) throws Exception {
        ReadGraph read = (ReadGraph) fetch(map);
        if (read != null) {
            return read;
        }
        read = new ReadGraph(this.contract, this.loader);
        cache(map, read);
        return read;
    }
}
