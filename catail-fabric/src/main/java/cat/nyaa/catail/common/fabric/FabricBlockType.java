package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.ElementType;
import cat.nyaa.catail.common.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

public class FabricBlockType<T extends Block> implements ElementType<T> {

    private static final Map<Block, FabricBlockType<?>> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Block> FabricBlockType<T> get(T block) {
        return (FabricBlockType<T>) cache.computeIfAbsent(block, FabricBlockType::new);
    }

    private final T block;

    protected FabricBlockType(T block) {
        this.block = block;
    }

    @Override
    public Identifier getIdentifier() {
        return FabricIdentifier.get(Registry.BLOCK.getId(block));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getBaseType() {
        return (Class<T>) block.getClass();
    }

    public Block getBlock() {
        return block;
    }
}
