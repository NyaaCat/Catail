package cat.nyaa.catail.common;

import java.util.Map;
import java.util.Set;

public interface Junctions extends IdNamed, Location {
    Set<Element<?>> getJunctionElements();

    Map<String, ElementDataGroup> getJunctionStates();

    Map<String, String> getJunctionStateNames();

    void addJunctionState(String id, String name, ElementDataGroup state);

    void removeJunctionState(String id, String name, ElementDataGroup state);

    String getDefaultState();
}
