package cat.nyaa.catail.common;

import java.util.Map;

public interface ElementDataGroup {
    @SuppressWarnings("rawtypes")
    Map<Element, ElementData> getElementDataMap();

    void applyAllBlockState();
}
