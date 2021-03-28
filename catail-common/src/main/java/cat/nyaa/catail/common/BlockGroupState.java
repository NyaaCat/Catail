package cat.nyaa.catail.common;

import java.util.Map;

public interface BlockGroupState {
    Map<Block, BlockData> getBlockStateMap();

    void applyAllBlockState();
}
