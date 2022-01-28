package cat.nyaa.catail.common.impl;

import cat.nyaa.catail.common.Element;
import cat.nyaa.catail.common.ElementData;
import cat.nyaa.catail.common.ElementDataGroup;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CommonElementDataGroup implements ElementDataGroup {

    @SuppressWarnings("rawtypes")
    private final Map<Element, ElementData> blockStateMap = new ConcurrentHashMap<>();

    public CommonElementDataGroup(Map<Element<?>, ElementData<?>> stateMap) {
        blockStateMap.putAll(stateMap);
    }

    public CommonElementDataGroup(Collection<Entry<Element<?>, ElementData<?>>> blockStatePairs) {
        blockStatePairs.forEach(e -> blockStateMap.put(e.getKey(), e.getValue()));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<Element, ElementData> getElementDataMap() {
        return Collections.unmodifiableMap(blockStateMap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyAllBlockState() {
        blockStateMap.forEach(Element::setState);
    }
}
