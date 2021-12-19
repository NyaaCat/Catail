package cat.nyaa.catail.common;

import java.util.Map;

public interface JunctionsRegistry {
    Junctions getById(Identifier id);

    Map<Identifier, Junctions> byId();
}
