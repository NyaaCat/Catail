package cat.nyaa.catail.common.impl;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockGroupState;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonBlockGroupState implements BlockGroupState {

    private final Map<Block, BlockData> blockStateMap = new ConcurrentHashMap<>();

    public CommonBlockGroupState(Map<Block, BlockData> stateMap) {
        blockStateMap.putAll(stateMap);
    }

    public CommonBlockGroupState(Collection<Map.Entry<Block, BlockData>> blockStatePairs) {
        blockStatePairs.forEach(e -> blockStateMap.put(e.getKey(), e.getValue()));
    }

    @Override
    public Map<Block, BlockData> getBlockStateMap() {
        return Collections.unmodifiableMap(blockStateMap);
    }

    @Override
    public void applyAllBlockState() {
        blockStateMap.forEach(Block::setState);
    }
}
