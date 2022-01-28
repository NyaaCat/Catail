package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.ElementData;
import cat.nyaa.catail.common.ElementType;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class FabricEntityData<E extends Entity> implements ElementData<E> {

    private final String name;
    private final BiConsumer<World, E> applier;
    private final Predicate<E> matcher;
    private final EntityType<E> entityType;

    protected FabricEntityData(
        String name,
        BiConsumer<World, E> applier,
        Predicate<E> matcher,
        EntityType<E> entityType
    ) {
        this.name = name;
        this.applier = applier;
        this.matcher = matcher;
        this.entityType = entityType;
    }

    @Override
    public ElementType<E> getElementType() {
        return FabricEntityType.get(entityType);
    }

    @Override
    public String getStateName() {
        return name;
    }

    @Override
    public String getAsString() {
        return name;
    }

    public BiConsumer<World, E> getApplier() {
        return applier;
    }

    public EntityType<E> getEntityType() {
        return entityType;
    }

    public boolean match(E entity) {
        return matcher.test(entity);
    }
}
