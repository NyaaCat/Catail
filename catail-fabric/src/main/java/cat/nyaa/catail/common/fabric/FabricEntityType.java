package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.ElementType;
import cat.nyaa.catail.common.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public class FabricEntityType<E extends Entity> implements ElementType<E> {

    private static final Map<EntityType<?>, FabricEntityType<?>> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <E extends Entity> FabricEntityType<E> get(EntityType<E> entityType) {
        return (FabricEntityType<E>) cache.computeIfAbsent(entityType, FabricEntityType::new);
    }

    private final EntityType<E> entityType;

    protected FabricEntityType(EntityType<E> entityType) {
        this.entityType = entityType;
    }

    @Override
    public Identifier getIdentifier() {
        return FabricIdentifier.get(Registry.ENTITY_TYPE.getId(entityType));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<E> getBaseType() {
        return (Class<E>) entityType.getBaseClass();
    }

    public EntityType<E> getEntityType() {
        return entityType;
    }
}
