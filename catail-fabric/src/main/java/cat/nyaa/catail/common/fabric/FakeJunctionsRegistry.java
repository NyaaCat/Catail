package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Identifier;
import cat.nyaa.catail.common.Junctions;
import cat.nyaa.catail.common.JunctionsRegistry;
import java.util.Map;
import net.minecraft.server.world.ServerWorld;

public class FakeJunctionsRegistry implements JunctionsRegistry {

    private final Map<Identifier, Junctions> registry;

    public FakeJunctionsRegistry(ServerWorld world) {
        registry = Map.of(FabricIdentifier.get("catail", "fake"), new FakeJunctions(world));
    }

    @Override
    public Junctions getById(Identifier id) {
        return registry.get(id);
    }

    @Override
    public Map<Identifier, Junctions> byId() {
        return registry;
    }
}
