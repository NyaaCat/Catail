package cat.nyaa.catail.common;

import java.util.Map;

public interface Station extends IdNamed {
    Map<Track, Location> getEndpoints();
}
