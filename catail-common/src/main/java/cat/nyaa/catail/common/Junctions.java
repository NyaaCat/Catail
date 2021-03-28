package cat.nyaa.catail.common;

import java.util.Map;
import java.util.Set;

public interface Junctions extends IdNamed, Location {
    Set<Block> getJunctionBlocks();

    Map<String, BlockGroupState> getJunctionStates();

    Map<String, String> getJunctionStateNames();

    void addJunctionState(String id, String name, BlockGroupState state);

    void removeJunctionState(String id, String name, BlockGroupState state);

    String getDefaultState();
}
