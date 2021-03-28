package cat.nyaa.catail.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface Route extends IdNamed {
    List<Route> getSubRoute();

    Map<Station, Track> getDepartureTracks();

    Map<Station, Track> getDestinationTracks();

    LinkedHashMap<Junctions, String> getRequiredJunctionState();

    default String getJunctionState(Junctions junction) {
        String state = this.getRequiredJunctionState().get(junction);
        if (state != null) {
            return state;
        }
        for (Route route : this.getSubRoute()) {
            state = route.getJunctionState(junction);
            if (state != null) {
                return state;
            }
        }
        return null;
    }
}
