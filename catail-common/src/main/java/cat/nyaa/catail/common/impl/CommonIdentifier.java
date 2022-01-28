package cat.nyaa.catail.common.impl;

import cat.nyaa.catail.common.Identifier;
import java.util.Objects;

public record CommonIdentifier(String namespace, String key) implements Identifier {
    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        if (Identifier.class.isAssignableFrom(obj.getClass())) {
            Identifier objId = (Identifier) obj;
            return objId.getKey().equals(getKey()) && objId.getNamespace().equals(getNamespace());
        }
        return false;
    }

    // same as @see net.minecraft.util.Identifier.hashCode
    @Override
    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.key.hashCode();
    }
}
