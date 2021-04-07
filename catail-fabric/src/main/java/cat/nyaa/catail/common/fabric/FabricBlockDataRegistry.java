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
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FabricBlockDataRegistry implements BlockDataRegistry {

    private static final Map<Identifier, Collection<FabricBlockData>> registry = new ConcurrentHashMap<Identifier, Collection<FabricBlockData>>() {
        {
            this.put(
                    Registry.BLOCK.getId(LEVER),
                    new HashSet<FabricBlockData>() {
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
                    new HashSet<FabricBlockData>() {
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
                                            new HashSet<Property<?>>() {
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
                                            new HashSet<Property<?>>() {
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
                    new HashSet<FabricBlockData>() {
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
