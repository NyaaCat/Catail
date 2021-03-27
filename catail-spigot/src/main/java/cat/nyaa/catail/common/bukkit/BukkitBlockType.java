package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.BlockType;
import cat.nyaa.catail.common.Identifier;
import org.bukkit.Material;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitBlockType implements BlockType {
    private final Material material;

    private static final Map<Material, BukkitBlockType> cache = new ConcurrentHashMap<>();

    public static BukkitBlockType get(Material material) {
        return cache.computeIfAbsent(material, BukkitBlockType::new);
    }

    protected BukkitBlockType(Material material) {
        this.material = material;
    }

    @Override
    public Identifier getIdentifier() {
        return BukkitIdentifier.get(material.getKey());
    }
}
