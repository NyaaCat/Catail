package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Identifier;
import cat.nyaa.catail.common.Junctions;
import cat.nyaa.catail.common.JunctionsRegistry;
import cat.nyaa.catail.common.impl.CommonIdentifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FakeJunctionsRegistry implements JunctionsRegistry {

    private final Map<Identifier, Junctions> registry = new HashMap<>(Map.of(new CommonIdentifier("catail", "fake"), new FakeJunctions()));

    @Override
    public Junctions getById(Identifier id) {
        return registry.get(id);
    }

    @Override
    public Map<Identifier, Junctions> byId() {
        return Collections.unmodifiableMap(registry);
    }
}
