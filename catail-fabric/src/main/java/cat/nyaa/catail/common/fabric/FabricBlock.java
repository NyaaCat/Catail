package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FabricBlock implements Block {

    private static final Map<BlockPos, Map<World, FabricBlock>> cache = new ConcurrentHashMap<>();

    public static FabricBlock get(World world, BlockPos pos) {
        return cache
            .computeIfAbsent(pos, p -> new ConcurrentHashMap<>())
            .computeIfAbsent(world, w -> new FabricBlock(w, pos));
    }

    private final BlockPos pos;
    private final World world;

    protected FabricBlock(World world, BlockPos pos) {
        this.pos = pos;
        this.world = world;
    }

    @Override
    public BlockData getState() {
        return FabricBlockDataRegistry.getInstance().match(this);
    }

    @Override
    public double getX() {
        return pos.getX();
    }

    @Override
    public double getY() {
        return pos.getY();
    }

    @Override
    public double getZ() {
        return pos.getZ();
    }

    @Override
    public void setState(BlockData state) {
        BlockState blockState = world.getBlockState(pos);
        BlockState newBlockState = ((FabricBlockData) state).getConsumer().apply(blockState);
        world.setBlockState(pos, newBlockState);
    }

    public World getWorld() {
        return world;
    }

    public BlockState getBlockState() {
        return world.getBlockState(pos);
    }
}
