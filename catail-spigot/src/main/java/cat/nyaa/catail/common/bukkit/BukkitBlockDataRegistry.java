package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockDataRegistry;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.Switch;

public class BukkitBlockDataRegistry implements BlockDataRegistry {

    private static final Map<NamespacedKey, Collection<BukkitBlockData>> registry = new ConcurrentHashMap<>() {
        {
            this.put(
                    Material.LEVER.getKey(),
                    new HashSet<>() {
                        {
                            this.add(
                                    new BukkitBlockData(
                                        "minecraft:lever[powered=true]",
                                        Bukkit
                                            .getServer()
                                            .createBlockData(
                                                Material.LEVER,
                                                blockData -> ((Switch) blockData).setPowered(true)
                                            )
                                    )
                                );
                            this.add(
                                    new BukkitBlockData(
                                        "minecraft:lever[powered=false]",
                                        Bukkit
                                            .getServer()
                                            .createBlockData(
                                                Material.LEVER,
                                                blockData -> ((Switch) blockData).setPowered(false)
                                            )
                                    )
                                );
                        }
                    }
                );
            this.put(
                    Material.REPEATER.getKey(),
                    new HashSet<>() {
                        {
                            Repeater repeaterData = (Repeater) Bukkit.getServer().createBlockData(Material.REPEATER);
                            for (int i = repeaterData.getMinimumDelay(); i <= repeaterData.getMaximumDelay(); i++) {
                                int delay = i;
                                this.add(
                                        new BukkitBlockData(
                                            "minecraft:repeater[delay=" + delay + ",locked=false]",
                                            Bukkit
                                                .getServer()
                                                .createBlockData(
                                                    Material.REPEATER,
                                                    blockData -> {
                                                        ((Repeater) blockData).setDelay(delay);
                                                        ((Repeater) blockData).setLocked(false);
                                                    }
                                                )
                                        )
                                    );
                                this.add(
                                        new BukkitBlockData(
                                            "minecraft:repeater[delay=" + delay + ",locked=true]",
                                            Bukkit
                                                .getServer()
                                                .createBlockData(
                                                    Material.REPEATER,
                                                    blockData -> {
                                                        ((Repeater) blockData).setDelay(delay);
                                                        ((Repeater) blockData).setLocked(true);
                                                    }
                                                )
                                        )
                                    );
                            }
                        }
                    }
                );
            this.put(
                    Material.COMPARATOR.getKey(),
                    new HashSet<>() {
                        {
                            for (Comparator.Mode mode : Comparator.Mode.values()) {
                                this.add(
                                        new BukkitBlockData(
                                            "minecraft:comparator[mode=" + mode.name().toLowerCase(Locale.ROOT) + "]",
                                            Bukkit
                                                .getServer()
                                                .createBlockData(
                                                    Material.COMPARATOR,
                                                    blockData -> ((Comparator) blockData).setMode(mode)
                                                )
                                        )
                                    );
                            }
                        }
                    }
                );
        }
    };
    private static final Map<NamespacedKey, BiFunction<org.bukkit.block.data.BlockData, BlockState, Boolean>> matchers = new ConcurrentHashMap<>() {
        {
            this.put(Material.LEVER.getKey(), BukkitBlockDataRegistry::LeverMatcher);
            this.put(Material.REPEATER.getKey(), BukkitBlockDataRegistry::RepeaterMatcher);
            this.put(Material.COMPARATOR.getKey(), BukkitBlockDataRegistry::ComparatorMatcher);
        }
    };

    @Override
    public BlockData Match(Block commonBlock) {
        BukkitBlock block = (BukkitBlock) commonBlock;
        BlockState blockState = block.getBlockState();
        NamespacedKey id = blockState.getBlockData().getMaterial().getKey();
        Collection<BukkitBlockData> knownStates = registry.get(id);
        BiFunction<org.bukkit.block.data.BlockData, BlockState, Boolean> matcher = matchers.get(id);
        if (Objects.isNull(knownStates)) {
            return null;
        }
        for (BukkitBlockData knownState : knownStates) {
            org.bukkit.block.data.BlockData data = knownState.getBlockData();
            if (matcher.apply(data, blockState)) {
                return knownState;
            }
        }
        return null;
    }

    public static Boolean LeverMatcher(org.bukkit.block.data.BlockData current, BlockState expected) {
        Switch lever = (Switch) current;
        Switch expectedLever = (Switch) expected.getBlockData();
        return expectedLever.isPowered() == lever.isPowered();
    }

    public static Boolean RepeaterMatcher(org.bukkit.block.data.BlockData current, BlockState expected) {
        Repeater repeater = (Repeater) current;
        Repeater expectedRepeater = (Repeater) expected.getBlockData();
        return (
            (expectedRepeater.getDelay() == repeater.getDelay()) && (expectedRepeater.isLocked() == repeater.isLocked())
        );
    }

    public static Boolean ComparatorMatcher(org.bukkit.block.data.BlockData current, BlockState expected) {
        Comparator comparator = (Comparator) current;
        Comparator expectedComparator = (Comparator) expected.getBlockData();
        return (expectedComparator.getMode() == comparator.getMode());
    }
}
