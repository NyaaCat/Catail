package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.block.BlockState;

public class BukkitBlock implements Block {

    private static final Map<org.bukkit.block.Block, BukkitBlock> cache = new ConcurrentHashMap<>();

    public static BukkitBlock get(org.bukkit.block.Block block) {
        return cache.computeIfAbsent(block, BukkitBlock::new);
    }

    private final org.bukkit.block.Block block;

    protected BukkitBlock(org.bukkit.block.Block block) {
        this.block = block;
    }

    @Override
    public BlockData getState() {
        throw new IllegalStateException();
    }

    @Override
    public double getX() {
        return block.getX();
    }

    @Override
    public double getY() {
        return block.getY();
    }

    @Override
    public double getZ() {
        return block.getZ();
    }

    @Override
    public void setState(BlockData state) {
        block.setBlockData(block.getBlockData().merge(((BukkitBlockData) state).getBlockData()));
    }

    public BlockState getBlockState() {
        return block.getState();
    }
}
