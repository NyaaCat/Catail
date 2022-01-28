package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.ElementData;
import cat.nyaa.catail.common.ElementType;

public class BukkitElementData implements ElementData {

    private final org.bukkit.block.data.BlockData blockData;
    private final String name;

    public BukkitElementData(String name, org.bukkit.block.data.BlockData blockData) {
        this.blockData = blockData;
        this.name = name;
    }

    @Override
    public ElementType getElementType() {
        return BukkitElementType.get(blockData.getMaterial());
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
