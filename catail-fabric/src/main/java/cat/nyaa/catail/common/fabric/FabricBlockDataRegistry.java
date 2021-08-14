package cat.nyaa.catail.common.fabric;

import static net.minecraft.block.Blocks.*;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockDataRegistry;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FabricBlockDataRegistry implements BlockDataRegistry {

    private static final FabricBlockDataRegistry instance = new FabricBlockDataRegistry();

    private final Map<Identifier, Collection<FabricBlockData>> registry = new ConcurrentHashMap<>() {
        {
            this.put(
                    Registry.BLOCK.getId(LEVER),
                    new HashSet<>() {
                        {
                            this.add(
                                    new FabricBlockData(
                                        "minecraft:lever[powered=true]",
                                        LEVER.getDefaultState(),
                                        blockState -> blockState.with(LeverBlock.POWERED, true),
                                        Collections.singleton(LeverBlock.POWERED)
                                    )
                                );
                            this.add(
                                    new FabricBlockData(
                                        "minecraft:lever[powered=false]",
                                        LEVER.getDefaultState(),
                                        blockState -> blockState.with(LeverBlock.POWERED, false),
                                        Collections.singleton(LeverBlock.POWERED)
                                    )
                                );
                        }
                    }
                );
            this.put(
                    Registry.BLOCK.getId(REPEATER),
                    new HashSet<>() {
                        {
                            for (int delay : RepeaterBlock.DELAY.getValues()) {
                                this.add(
                                        new FabricBlockData(
                                            "minecraft:repeater[delay=" + delay + ",locked=false]",
                                            REPEATER.getDefaultState(),
                                            blockState ->
                                                blockState
                                                    .with(RepeaterBlock.DELAY, delay)
                                                    .with(RepeaterBlock.LOCKED, false),
                                            new HashSet<>() {
                                                {
                                                    this.add(RepeaterBlock.DELAY);
                                                    this.add(RepeaterBlock.LOCKED);
                                                }
                                            }
                                        )
                                    );
                                this.add(
                                        new FabricBlockData(
                                            "minecraft:repeater[delay=" + delay + ",locked=true]",
                                            REPEATER.getDefaultState(),
                                            blockState ->
                                                blockState
                                                    .with(RepeaterBlock.DELAY, delay)
                                                    .with(RepeaterBlock.LOCKED, true),
                                            new HashSet<>() {
                                                {
                                                    this.add(RepeaterBlock.DELAY);
                                                    this.add(RepeaterBlock.LOCKED);
                                                }
                                            }
                                        )
                                    );
                            }
                        }
                    }
                );
            this.put(
                    Registry.BLOCK.getId(COMPARATOR),
                    new HashSet<>() {
                        {
                            for (ComparatorMode mode : ComparatorBlock.MODE.getValues()) {
                                this.add(
                                        new FabricBlockData(
                                            "minecraft:comparator[mode=" + mode.name().toLowerCase(Locale.ROOT) + "]",
                                            COMPARATOR.getDefaultState(),
                                            blockState -> blockState.with(ComparatorBlock.MODE, mode),
                                            Collections.singleton(ComparatorBlock.MODE)
                                        )
                                    );
                            }
                        }
                    }
                );
        }
    };

    public static FabricBlockDataRegistry getInstance() {
        return FabricBlockDataRegistry.instance;
    }

    protected FabricBlockDataRegistry() {}

    @Override
    public BlockData get(cat.nyaa.catail.common.Identifier key, String name) {
        Collection<FabricBlockData> blockData = registry.get(((FabricIdentifier) key).getIdentifier());
        if (Objects.isNull(blockData)) {
            return null;
        }
        return blockData.stream().filter(d -> d.getStateName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public BlockData match(Block commonBlock) {
        FabricBlock block = (FabricBlock) commonBlock;
        BlockState blockState = block.getBlockState();
        Identifier id = Registry.BLOCK.getId(blockState.getBlock());
        Collection<FabricBlockData> knownStates = registry.get(id);
        if (Objects.isNull(knownStates)) {
            return null;
        }
        for (FabricBlockData knownState : knownStates) {
            BlockState state = knownState.getBlockState();
            if (
                knownState
                    .getProperties()
                    .stream()
                    .allMatch(
                        property ->
                            Objects.nonNull(blockState.get(property)) &&
                            blockState.get(property).equals(state.get(property))
                    )
            ) {
                return knownState;
            }
        }
        return null;
    }
}
