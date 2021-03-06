package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockType;
import com.mojang.serialization.DataResult;
import java.util.Collection;
import java.util.function.UnaryOperator;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.state.property.Property;

public class FabricBlockData implements BlockData {

    private final String name;
    private final UnaryOperator<BlockState> transformer;
    private final BlockState blockState;
    private final Collection<Property<?>> properties;

    protected FabricBlockData(
        String name,
        BlockState blockState,
        UnaryOperator<BlockState> transformer,
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
        DataResult<NbtElement> tagDataResult = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, blockState);
        return tagDataResult.getOrThrow(false, (String err) -> {}).asString();
    }

    public UnaryOperator<BlockState> getTransformer() {
        return transformer;
    }

    public Collection<Property<?>> getProperties() {
        return properties;
    }

    public BlockState getBlockState() {
        return blockState;
    }
}
