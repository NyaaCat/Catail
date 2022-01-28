package cat.nyaa.catail.common.fabric;

import static net.minecraft.block.Blocks.*;

import cat.nyaa.catail.common.Element;
import cat.nyaa.catail.common.ElementData;
import cat.nyaa.catail.common.ElementDataRegistry;
import cat.nyaa.catail.common.TriConsumer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.block.*;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("java:S3011") // Some reflect is not avoidable in Minecraft
public class FabricElementDataRegistry implements ElementDataRegistry {

    private <T extends Comparable<T>, V extends T> UnaryOperator<BlockState> withProps(Property<T> prop, V value) {
        return blockState -> {
            if (Objects.equals(blockState.get(prop), value)) {
                return null;
            }
            return blockState.with(prop, value);
        };
    }

    @SuppressWarnings("SameParameterValue")
    private <
        T1 extends Comparable<T1>, V1 extends T1, T2 extends Comparable<T2>, V2 extends T2
    > UnaryOperator<BlockState> withProps(Property<T1> prop1, V1 value1, Property<T2> prop2, V2 value2) {
        return blockState -> {
            BlockState newBlockState = blockState;
            boolean prop1Updated = Objects.equals(blockState.get(prop1), value1);
            boolean prop2Updated = Objects.equals(blockState.get(prop2), value2);
            if (prop1Updated && prop2Updated) {
                return null;
            }
            if (prop1Updated) {
                newBlockState = blockState.with(prop1, value1);
            }
            if (prop2Updated) {
                newBlockState = blockState.with(prop2, value2);
            }
            return newBlockState;
        };
    }

    private static final TriConsumer<World, BlockPos, BlockState> leverApplier = (world, pos, blockState) -> {
        world.setBlockState(pos, blockState);
        LeverBlock leverBlock = (LeverBlock) blockState.getBlock();

        // see leverBlock.onUse() for logic below
        world.updateNeighborsAlways(pos, leverBlock);

        Direction direction =
            switch (blockState.get(WallMountedBlock.FACE)) {
                case CEILING -> Direction.DOWN;
                case FLOOR -> Direction.UP;
                default -> blockState.get(HorizontalFacingBlock.FACING);
            };

        world.updateNeighborsAlways(pos.offset(direction.getOpposite()), leverBlock);
        float pitch = blockState.get(LeverBlock.POWERED) ? 0.6f : 0.5f;
        world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.6f, pitch);
        world.emitGameEvent(
            null,
            blockState.get(LeverBlock.POWERED) ? GameEvent.BLOCK_SWITCH : GameEvent.BLOCK_UNSWITCH,
            pos
        );
    };

    private static final TriConsumer<World, BlockPos, BlockState> repeaterApplier = World::setBlockState;

    private static final Method comparatorBlockUpdate;

    static {
        try {
            comparatorBlockUpdate =
                ComparatorBlock.class.getDeclaredMethod("update", World.class, BlockPos.class, BlockState.class);
            comparatorBlockUpdate.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final TriConsumer<World, BlockPos, BlockState> comparatorApplier = (world, pos, blockState) -> {
        ComparatorBlock comparatorBlock = (ComparatorBlock) blockState.getBlock();
        // see comparatorBlock.onUse() for logic below

        float pitch = blockState.get(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT ? 0.55f : 0.5f;
        world.playSound(null, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3f, pitch);
        world.setBlockState(pos, blockState, net.minecraft.block.Block.NOTIFY_LISTENERS);
        try {
            comparatorBlockUpdate.invoke(comparatorBlock, world, pos, blockState);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    };
    private final Map<EntityType<? extends Entity>, Collection<FabricEntityData<? extends Entity>>> entityStateRegistry = new ConcurrentHashMap<>(
        Map.ofEntries(
            Map.entry(
                EntityType.ITEM_FRAME,
                IntStream
                    .rangeClosed(0, 7)
                    .mapToObj(rotate ->
                        new FabricEntityData<>(
                            "minecraft:item_frame{ItemRotation:" + rotate + "b}",
                            (
                                (world, itemFrameEntity) -> {
                                    itemFrameEntity.playSound(itemFrameEntity.getRotateItemSound(), 2.0f, 1.0f);
                                    itemFrameEntity.setRotation(rotate);
                                }
                            ),
                            (itemFrameEntity -> itemFrameEntity.getRotation() == rotate),
                            EntityType.ITEM_FRAME
                        )
                    )
                    .collect(Collectors.toSet())
            ),
            Map.entry(
                EntityType.GLOW_ITEM_FRAME,
                IntStream
                    .rangeClosed(0, 7)
                    .mapToObj(rotate ->
                        new FabricEntityData<>(
                            "minecraft:glow_item_frame{ItemRotation:" + rotate + "}",
                            (
                                (world, itemFrameEntity) -> {
                                    itemFrameEntity.playSound(itemFrameEntity.getRotateItemSound(), 2.0f, 1.0f);
                                    itemFrameEntity.setRotation(rotate);
                                }
                            ),
                            (itemFrameEntity -> itemFrameEntity.getRotation() == rotate),
                            EntityType.GLOW_ITEM_FRAME
                        )
                    )
                    .collect(Collectors.toSet())
            )
        )
    );

    private final Map<Identifier, Collection<FabricBlockState<? extends Block>>> blockStateRegistry = new ConcurrentHashMap<>(
        Map.ofEntries(
            Map.entry(
                Registry.BLOCK.getId(LEVER),
                Set.of(
                    new FabricBlockState<LeverBlock>(
                        "minecraft:lever[powered=true]",
                        LEVER.getDefaultState(),
                        withProps(LeverBlock.POWERED, true),
                        leverApplier,
                        Collections.singleton(LeverBlock.POWERED)
                    ),
                    new FabricBlockState<LeverBlock>(
                        "minecraft:lever[powered=false]",
                        LEVER.getDefaultState(),
                        withProps(LeverBlock.POWERED, false),
                        leverApplier,
                        Collections.singleton(LeverBlock.POWERED)
                    )
                )
            ),
            Map.entry(
                Registry.BLOCK.getId(REPEATER),
                RepeaterBlock.DELAY
                    .getValues()
                    .stream()
                    .mapMulti((Integer delay, Consumer<FabricBlockState<RepeaterBlock>> commit) -> {
                        commit.accept(
                            new FabricBlockState<>(
                                "minecraft:repeater[delay=" + delay + ",locked=false]",
                                REPEATER.getDefaultState(),
                                withProps(RepeaterBlock.DELAY, delay, RepeaterBlock.LOCKED, false),
                                repeaterApplier,
                                Set.of(RepeaterBlock.DELAY, RepeaterBlock.LOCKED)
                            )
                        );
                        commit.accept(
                            new FabricBlockState<>(
                                "minecraft:repeater[delay=" + delay + ",locked=true]",
                                REPEATER.getDefaultState(),
                                withProps(RepeaterBlock.DELAY, delay, RepeaterBlock.LOCKED, true),
                                repeaterApplier,
                                Set.of(RepeaterBlock.DELAY, RepeaterBlock.LOCKED)
                            )
                        );
                    })
                    .collect(Collectors.toSet())
            ),
            Map.entry(
                Registry.BLOCK.getId(COMPARATOR),
                ComparatorBlock.MODE
                    .getValues()
                    .stream()
                    .map(mode ->
                        new FabricBlockState<ComparatorBlock>(
                            "minecraft:comparator[mode=" + mode.name().toLowerCase(Locale.ROOT) + "]",
                            COMPARATOR.getDefaultState(),
                            withProps(ComparatorBlock.MODE, mode),
                            comparatorApplier,
                            Collections.singleton(ComparatorBlock.MODE)
                        )
                    )
                    .collect(Collectors.toSet())
            )
        )
    );

    public static FabricElementDataRegistry getInstance() {
        return FabricElementDataRegistry.instance;
    }

    protected FabricElementDataRegistry() {}

    @SuppressWarnings("unchecked")
    @Override
    public ElementData<?> get(cat.nyaa.catail.common.Identifier key, String name) {
        Collection<FabricBlockState<?>> blockData = blockStateRegistry.get(((FabricIdentifier) key).getIdentifier());
        if (Objects.nonNull(blockData)) {
            return blockData.stream().filter(d -> d.getStateName().equals(name)).findFirst().orElse(null);
        }
        Collection<FabricEntityData<? extends Entity>> entityData = entityStateRegistry.get(
            Registry.ENTITY_TYPE.get(((FabricIdentifier) key).getIdentifier())
        );
        if (Objects.nonNull(entityData)) {
            return entityData.stream().filter(d -> d.getStateName().equals(name)).findFirst().orElse(null);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ElementData<T> match(Element<T> element) {
        if (element instanceof FabricBlock block) {
            return matchBlock(block);
        } else if (element instanceof FabricEntity entity) {
            return matchEntity(entity);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <E extends Entity> ElementData<E> matchEntity(FabricEntity<E> entity) {
        EntityType<E> type = entity.getType();
        E e = entity.getEntity();
        Collection<FabricEntityData<? extends Entity>> knownStates = entityStateRegistry.get(type);
        for (FabricEntityData<? extends Entity> knownState : knownStates) {
            FabricEntityData<E> entityData = (FabricEntityData<E>) knownState;
            if (Objects.nonNull(e) && entityData.match(e)) {
                return entityData;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T extends Block> ElementData<T> matchBlock(FabricBlock<T> block) {
        BlockState blockState = block.getBlockState();
        Identifier id = Registry.BLOCK.getId(blockState.getBlock());
        Collection<FabricBlockState<? extends Block>> knownStates = blockStateRegistry.get(id);
        if (Objects.isNull(knownStates)) {
            return null;
        }
        for (FabricBlockState<? extends Block> knownState : knownStates) {
            BlockState state = knownState.getBlockState();
            if (
                knownState
                    .getProperties()
                    .stream()
                    .allMatch(property ->
                        Objects.nonNull(blockState.get(property)) &&
                        blockState.get(property).equals(state.get(property))
                    )
            ) {
                return (ElementData<T>) knownState;
            }
        }
        return null;
    }

    private static final FabricElementDataRegistry instance = new FabricElementDataRegistry();
}
