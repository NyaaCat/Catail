package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.BlockDataRegistry;
import cat.nyaa.catail.common.Identifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.Switch;

public class BukkitBlockDataRegistry implements BlockDataRegistry {

    private static final BukkitBlockDataRegistry instance = new BukkitBlockDataRegistry();

    private final Map<NamespacedKey, Collection<BukkitBlockData>> registry = new ConcurrentHashMap<>(
        Map.ofEntries(
            Map.entry(
                Material.LEVER.getKey(),
                Set.of(
                    new BukkitBlockData(
                        "minecraft:lever[powered=true]",
                        Bukkit.getServer().createBlockData("minecraft:lever[powered=true]")
                    ),
                    new BukkitBlockData(
                        "minecraft:lever[powered=false]",
                        Bukkit.getServer().createBlockData("minecraft:lever[powered=false]")
                    )
                )
            ),
            Map.entry(
                Material.REPEATER.getKey(),
                Stream
                    .of((Repeater) Bukkit.getServer().createBlockData(Material.REPEATER))
                    .mapMulti((Repeater repeaterData, Consumer<BukkitBlockData> commit) -> {
                        for (int i = repeaterData.getMinimumDelay(); i <= repeaterData.getMaximumDelay(); i++) {
                            commit.accept(
                                new BukkitBlockData(
                                    "minecraft:repeater[delay=" + i + ",locked=false]",
                                    Bukkit
                                        .getServer()
                                        .createBlockData("minecraft:repeater[delay=" + i + ",locked=false]")
                                )
                            );
                            commit.accept(
                                new BukkitBlockData(
                                    "minecraft:repeater[delay=" + i + ",locked=true]",
                                    Bukkit
                                        .getServer()
                                        .createBlockData("minecraft:repeater[delay=" + i + ",locked=true]")
                                )
                            );
                        }
                    })
                    .collect(Collectors.toSet())
            ),
            Map.entry(
                Material.COMPARATOR.getKey(),
                Arrays
                    .stream(Comparator.Mode.values())
                    .map(mode ->
                        new BukkitBlockData(
                            "minecraft:comparator[mode=" + mode.name().toLowerCase(Locale.ROOT) + "]",
                            Bukkit
                                .getServer()
                                .createBlockData(
                                    "minecraft:comparator[mode=" + mode.name().toLowerCase(Locale.ROOT) + "]"
                                )
                        )
                    )
                    .collect(Collectors.toSet())
            )
        )
    );

    private final Map<NamespacedKey, BiPredicate<org.bukkit.block.data.BlockData, BlockState>> matchers = new ConcurrentHashMap<>(
        Map.ofEntries(
            Map.entry(Material.LEVER.getKey(), BukkitBlockDataRegistry::leverMatcher),
            Map.entry(Material.REPEATER.getKey(), BukkitBlockDataRegistry::repeaterMatcher),
            Map.entry(Material.COMPARATOR.getKey(), BukkitBlockDataRegistry::comparatorMatcher)
        )
    );

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
        BiPredicate<org.bukkit.block.data.BlockData, BlockState> matcher = matchers.get(id);
        if (Objects.isNull(knownStates)) {
            return null;
        }
        for (BukkitBlockData knownState : knownStates) {
            org.bukkit.block.data.BlockData data = knownState.getBlockData();
            if (matcher.test(data, blockState)) {
                return knownState;
            }
        }
        return null;
    }

    public static Boolean leverMatcher(org.bukkit.block.data.BlockData current, BlockState expected) {
        Switch lever = (Switch) current;
        Switch expectedLever = (Switch) expected.getBlockData();
        return expectedLever.isPowered() == lever.isPowered();
    }

    public static Boolean repeaterMatcher(org.bukkit.block.data.BlockData current, BlockState expected) {
        Repeater repeater = (Repeater) current;
        Repeater expectedRepeater = (Repeater) expected.getBlockData();
        return (
            (expectedRepeater.getDelay() == repeater.getDelay()) && (expectedRepeater.isLocked() == repeater.isLocked())
        );
    }

    public static Boolean comparatorMatcher(org.bukkit.block.data.BlockData current, BlockState expected) {
        Comparator comparator = (Comparator) current;
        Comparator expectedComparator = (Comparator) expected.getBlockData();
        return (expectedComparator.getMode() == comparator.getMode());
    }
}
