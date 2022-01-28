package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Element;
import cat.nyaa.catail.common.ElementData;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;

public class FabricEntity<E extends Entity> implements Element<E> {

    private static final Map<UUID, FabricEntity<Entity>> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Entity> FabricEntity<T> get(ServerWorld world, UUID uuid, T entity, EntityType<T> type) {
        return (FabricEntity<T>) cache.computeIfAbsent(
            uuid,
            p -> (FabricEntity<Entity>) new FabricEntity<>(world, uuid, entity, type)
        );
    }

    private final ServerWorld world;

    private final UUID uuid;

    private final AtomicReference<E> entity = new AtomicReference<>();

    private final EntityType<E> type;

    protected FabricEntity(ServerWorld world, UUID uuid, E entity, EntityType<E> type) {
        this.world = world;
        this.uuid = uuid;
        this.entity.set(entity);
        this.type = type;
    }

    public EntityType<E> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public E getEntity() {
        return entity.updateAndGet(e -> {
            if (Objects.nonNull(e) && e.isAlive()) {
                return e;
            }
            return (E) world.getEntity(uuid);
        });
    }

    @Override
    public ElementData<E> getState() {
        return FabricElementDataRegistry.getInstance().match(this);
    }

    @Override
    public void setState(ElementData<E> state) {
        FabricEntityData<E> entityData = (FabricEntityData<E>) state;
        E e = getEntity();
        if (Objects.nonNull(e) && !entityData.match(e)) {
            entityData.getApplier().accept(world, e);
        }
    }

    @Override
    public double getX() {
        return Optional.ofNullable(entity.get()).map(Entity::getX).orElse(0d);
    }

    @Override
    public double getY() {
        return Optional.ofNullable(entity.get()).map(Entity::getY).orElse(0d);
    }

    @Override
    public double getZ() {
        return Optional.ofNullable(entity.get()).map(Entity::getZ).orElse(0d);
    }
}
