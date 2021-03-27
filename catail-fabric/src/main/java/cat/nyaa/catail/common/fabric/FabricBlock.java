package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FabricBlock implements Block {
    private final BlockPos pos;
    private final World world;

    protected FabricBlock(World world, BlockPos pos) {
        this.pos = pos;
        this.world = world;
    }

    @Override
    public BlockData getState() {
        throw new IllegalStateException();
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
        world.setBlockState(pos, ((FabricBlockData)state).getBlockState());
    }
}
