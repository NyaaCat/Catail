package cat.nyaa.catail.common.impl;

import cat.nyaa.catail.common.Identifier;

public record CommonIdentifier(String namespace, String key) implements Identifier {

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getKey() {
        return key;
    }
}
