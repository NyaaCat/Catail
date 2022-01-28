package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.ElementData;
import cat.nyaa.catail.common.ElementType;
import cat.nyaa.catail.common.TriConsumer;
import com.mojang.serialization.DataResult;
import java.util.Collection;
import java.util.Objects;
import java.util.function.UnaryOperator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FabricBlockState<T extends Block> implements ElementData<T> {

    private final String name;
    private final UnaryOperator<BlockState> transformer;
    private final TriConsumer<World, BlockPos, BlockState> applier;
    private final BlockState blockState;
    private final Collection<Property<?>> properties;

    protected FabricBlockState(
        String name,
        BlockState blockState,
        UnaryOperator<BlockState> transformer,
        TriConsumer<World, BlockPos, BlockState> applier,
        Collection<Property<?>> properties
    ) {
        this.name = name;
        this.transformer = transformer;
        this.applier = applier;
        this.properties = properties;
        BlockState bs = transformer.apply(blockState);
        if (Objects.isNull(bs)) {
            bs = blockState;
        }
        this.blockState = bs;
    }

    @Override
    public ElementType getElementType() {
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

    public TriConsumer<World, BlockPos, BlockState> getApplier() {
        return applier;
    }

    public Collection<Property<?>> getProperties() {
        return properties;
    }

    public BlockState getBlockState() {
        return blockState;
    }
}
