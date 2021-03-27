package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockType;

public class BukkitBlockData implements BlockData {
    private final org.bukkit.block.data.BlockData blockData;
    private final String name;

    public BukkitBlockData(org.bukkit.block.data.BlockData blockData, String name) {
        this.blockData = blockData;
        this.name = name;
    }

    public org.bukkit.block.data.BlockData getBlockData() {
        return blockData;
    }

    @Override
    public BlockType getBlockType() {
        return BukkitBlockType.get(blockData.getMaterial());
    }

    @Override
    public String getStateName() {
        return name;
    }

    @Override
    public String getAsString() {
        return blockData.getAsString();
    }
}
