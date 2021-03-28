package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.BlockType;
import cat.nyaa.catail.common.Identifier;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricBlockType implements BlockType {
    private final Block block;

    private static final Map<Block, FabricBlockType> cache = new ConcurrentHashMap<>();

    public static FabricBlockType get(Block block) {
        return cache.computeIfAbsent(block, FabricBlockType::new);
    }

    protected FabricBlockType(Block block) {
        this.block = block;
    }

    @Override
    public Identifier getIdentifier() {
        return FabricIdentifier.get(Registry.BLOCK.getId(block));
    }
}
