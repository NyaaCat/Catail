package cat.nyaa.catail.common;

import java.util.List;

public interface Track extends IdNamed {
    List<Station> getStations();
}
