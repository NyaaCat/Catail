package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Identifier;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    public static String dumpCache() {
        return cache
            .entrySet()
            .stream()
            .map(i -> i.getKey().toString() + " -> " + i.getValue().toString())
            .collect(Collectors.joining("\n"));
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

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
