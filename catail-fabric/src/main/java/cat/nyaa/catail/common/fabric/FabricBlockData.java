package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockType;
import com.mojang.serialization.DataResult;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.state.property.Property;

public class FabricBlockData implements BlockData {

    private final String name;
    private final Function<BlockState, BlockState> transformer;
    private final BlockState blockState;
    private final Collection<Property<?>> properties;

    protected FabricBlockData(
        String name,
        BlockState blockState,
        Function<BlockState, BlockState> transformer,
        Collection<Property<?>> properties
    ) {
        this.name = name;
        this.transformer = transformer;
        this.properties = properties;
        this.blockState = transformer.apply(blockState);
    }

    @Override
    public BlockType getBlockType() {
        return FabricBlockType.get(blockState.getBlock());
    }

    @Override
    public String getStateName() {
        return name;
    }

    @Override
    public String getAsString() {
        DataResult<Tag> tagDataResult = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, blockState);
        return tagDataResult.getOrThrow(false, (String err) -> {}).asString();
    }

    public Function<BlockState, BlockState> getTransformer() {
        return transformer;
    }

    public Collection<Property<?>> getProperties() {
        return properties;
    }

    public BlockState getBlockState() {
        return blockState;
    }
}
