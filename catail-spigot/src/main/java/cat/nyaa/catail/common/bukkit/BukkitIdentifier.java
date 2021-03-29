package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.NamespacedKey;

public class BukkitIdentifier implements Identifier {

    private final NamespacedKey namespacedKey;
    private static final Map<NamespacedKey, BukkitIdentifier> cache = new ConcurrentHashMap<>();

    public static Identifier get(NamespacedKey key) {
        return cache.computeIfAbsent(key, BukkitIdentifier::new);
    }

    protected BukkitIdentifier(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    @Override
    public String getNamespace() {
        return namespacedKey.getNamespace();
    }

    @Override
    public String getKey() {
        return namespacedKey.getKey();
    }
}
