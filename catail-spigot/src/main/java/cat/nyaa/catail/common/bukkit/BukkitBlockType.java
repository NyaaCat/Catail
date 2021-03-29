package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.BlockType;
import cat.nyaa.catail.common.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;

public class BukkitBlockType implements BlockType {

    private static final Map<Material, BukkitBlockType> cache = new ConcurrentHashMap<>();

    public static BukkitBlockType get(Material material) {
        return cache.computeIfAbsent(material, BukkitBlockType::new);
    }

    private final Material material;

    protected BukkitBlockType(Material material) {
        this.material = material;
    }

    @Override
    public Identifier getIdentifier() {
        return BukkitIdentifier.get(material.getKey());
    }
}
