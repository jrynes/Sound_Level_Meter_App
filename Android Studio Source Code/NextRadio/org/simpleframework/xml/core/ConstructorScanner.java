package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

class ConstructorScanner {
    private Signature primary;
    private ParameterMap registry;
    private List<Signature> signatures;
    private Support support;

    public ConstructorScanner(Detail detail, Support support) throws Exception {
        this.signatures = new ArrayList();
        this.registry = new ParameterMap();
        this.support = support;
        scan(detail);
    }

    public Signature getSignature() {
        return this.primary;
    }

    public List<Signature> getSignatures() {
        return new ArrayList(this.signatures);
    }

    public ParameterMap getParameters() {
        return this.registry;
    }

    private void scan(Detail detail) throws Exception {
        Constructor[] array = detail.getConstructors();
        if (detail.isInstantiable()) {
            for (Constructor factory : array) {
                if (!detail.isPrimitive()) {
                    scan(factory);
                }
            }
            return;
        }
        throw new ConstructorException("Can not construct inner %s", detail);
    }

    private void scan(Constructor factory) throws Exception {
        SignatureScanner scanner = new SignatureScanner(factory, this.registry, this.support);
        if (scanner.isValid()) {
            for (Signature signature : scanner.getSignatures()) {
                if (signature.size() == 0) {
                    this.primary = signature;
                }
                this.signatures.add(signature);
            }
        }
    }
}
