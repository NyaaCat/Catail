package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.ElementType;
import cat.nyaa.catail.common.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;

public class BukkitElementType implements ElementType {

    private static final Map<Material, BukkitElementType> cache = new ConcurrentHashMap<>();

    public static BukkitElementType get(Material material) {
        return cache.computeIfAbsent(material, BukkitElementType::new);
    }

    private final Material material;

    protected BukkitElementType(Material material) {
        this.material = material;
    }

    @Override
    public Identifier getIdentifier() {
        return BukkitIdentifier.get(material.getKey());
    }
}
