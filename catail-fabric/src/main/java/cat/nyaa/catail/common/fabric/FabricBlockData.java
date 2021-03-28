package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockType;
import com.mojang.serialization.DataResult;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

public class FabricBlockData implements BlockData {
    private final String name;
    private final BlockState blockState;

    public FabricBlockData(String name, BlockState blockState) {
        this.name = name;
        this.blockState = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
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
        return tagDataResult.getOrThrow(false, (String err) -> {
        }).asString();
    }
}
