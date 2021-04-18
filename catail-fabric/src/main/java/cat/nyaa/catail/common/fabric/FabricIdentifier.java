package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricIdentifier implements Identifier {

    private static final Map<net.minecraft.util.Identifier, FabricIdentifier> cache = new ConcurrentHashMap<>();

    public static Identifier get(net.minecraft.util.Identifier identifier) {
        return cache.computeIfAbsent(identifier, FabricIdentifier::new);
    }

    public static Identifier get(String namespace, String key) {
        return get(new net.minecraft.util.Identifier(namespace, key));
    }

    private final net.minecraft.util.Identifier identifier;

    protected FabricIdentifier(net.minecraft.util.Identifier identifier) {
        this.identifier = identifier;
    }

    public net.minecraft.util.Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getNamespace() {
        return identifier.getNamespace();
    }

    @Override
    public String getKey() {
        return identifier.getPath();
    }
}
