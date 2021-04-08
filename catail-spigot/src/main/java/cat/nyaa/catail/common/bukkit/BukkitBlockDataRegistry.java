package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockDataRegistry;
import cat.nyaa.catail.common.Identifier;
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

    private static final BukkitBlockDataRegistry instance = new BukkitBlockDataRegistry();

    private final Map<NamespacedKey, Collection<BukkitBlockData>> registry = new ConcurrentHashMap<NamespacedKey, Collection<BukkitBlockData>>() {
        {
            this.put(
                    Material.LEVER.getKey(),
                    new HashSet<BukkitBlockData>() {
                        {
                            this.add(
                                    new BukkitBlockData(
                                        "minecraft:lever[powered=true]",
                                        Bukkit.getServer().createBlockData("minecraft:lever[powered=true]")
                                    )
                                );
                            this.add(
                                    new BukkitBlockData(
                                        "minecraft:lever[powered=false]",
                                        Bukkit.getServer().createBlockData("minecraft:lever[powered=false]")
                                    )
                                );
                        }
                    }
                );
            this.put(
                    Material.REPEATER.getKey(),
                    new HashSet<BukkitBlockData>() {
                        {
                            Repeater repeaterData = (Repeater) Bukkit.getServer().createBlockData(Material.REPEATER);
                            for (int i = repeaterData.getMinimumDelay(); i <= repeaterData.getMaximumDelay(); i++) {
                                this.add(
                                        new BukkitBlockData(
                                            "minecraft:repeater[delay=" + i + ",locked=false]",
                                            Bukkit
                                                .getServer()
                                                .createBlockData("minecraft:repeater[delay=" + i + ",locked=false]")
                                        )
                                    );
                                this.add(
                                        new BukkitBlockData(
                                            "minecraft:repeater[delay=" + i + ",locked=true]",
                                            Bukkit
                                                .getServer()
                                                .createBlockData("minecraft:repeater[delay=" + i + ",locked=true]")
                                        )
                                    );
                            }
                        }
                    }
                );
            this.put(
                    Material.COMPARATOR.getKey(),
                    new HashSet<BukkitBlockData>() {
                        {
                            for (Comparator.Mode mode : Comparator.Mode.values()) {
                                this.add(
                                        new BukkitBlockData(
                                            "minecraft:comparator[mode=" + mode.name().toLowerCase(Locale.ROOT) + "]",
                                            Bukkit
                                                .getServer()
                                                .createBlockData(
                                                    "minecraft:comparator[mode=" +
                                                    mode.name().toLowerCase(Locale.ROOT) +
                                                    "]"
                                                )
                                        )
                                    );
                            }
                        }
                    }
                );
        }
    };

    private final Map<NamespacedKey, BiFunction<org.bukkit.block.data.BlockData, BlockState, Boolean>> matchers = new ConcurrentHashMap<NamespacedKey, BiFunction<org.bukkit.block.data.BlockData, BlockState, Boolean>>() {
        {
            this.put(Material.LEVER.getKey(), BukkitBlockDataRegistry::LeverMatcher);
            this.put(Material.REPEATER.getKey(), BukkitBlockDataRegistry::RepeaterMatcher);
            this.put(Material.COMPARATOR.getKey(), BukkitBlockDataRegistry::ComparatorMatcher);
        }
    };

    public static BukkitBlockDataRegistry getInstance() {
        return BukkitBlockDataRegistry.instance;
    }

    protected BukkitBlockDataRegistry() {}

    @Override
    public BlockData get(Identifier key, String name) {
        Collection<BukkitBlockData> blockData = registry.get(((BukkitIdentifier) key).getNamespacedKey());
        if (Objects.isNull(blockData)) {
            return null;
        }
        return blockData.stream().filter(d -> d.getStateName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public BlockData match(Block commonBlock) {
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
