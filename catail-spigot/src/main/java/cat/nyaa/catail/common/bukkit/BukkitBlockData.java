package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockType;

public class BukkitBlockData implements BlockData {

    private final org.bukkit.block.data.BlockData blockData;
    private final String name;

    public BukkitBlockData(String name, org.bukkit.block.data.BlockData blockData) {
        this.blockData = blockData;
        this.name = name;
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

    public org.bukkit.block.data.BlockData getBlockData() {
        return blockData;
    }
}
