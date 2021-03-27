package cat.nyaa.catail.common;

import java.util.UUID;

public interface Player {
    UUID getUuid();
    String getName();
    String getDisplayName();
    Location getLocation();
}
