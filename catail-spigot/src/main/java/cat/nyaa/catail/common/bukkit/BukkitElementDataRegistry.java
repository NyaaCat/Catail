package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.Element;
import cat.nyaa.catail.common.ElementData;
import cat.nyaa.catail.common.ElementDataRegistry;
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

public class BukkitElementDataRegistry implements ElementDataRegistry {

    private static final BukkitElementDataRegistry instance = new BukkitElementDataRegistry();

    private final Map<NamespacedKey, Collection<BukkitElementData>> registry = new ConcurrentHashMap<>(
        Map.ofEntries(
            Map.entry(
                Material.LEVER.getKey(),
                Set.of(
                    new BukkitElementData(
                        "minecraft:lever[powered=true]",
                        Bukkit.getServer().createBlockData("minecraft:lever[powered=true]")
                    ),
                    new BukkitElementData(
                        "minecraft:lever[powered=false]",
                        Bukkit.getServer().createBlockData("minecraft:lever[powered=false]")
                    )
                )
            ),
            Map.entry(
                Material.REPEATER.getKey(),
                Stream
                    .of((Repeater) Bukkit.getServer().createBlockData(Material.REPEATER))
                    .mapMulti((Repeater repeaterData, Consumer<BukkitElementData> commit) -> {
                        for (int i = repeaterData.getMinimumDelay(); i <= repeaterData.getMaximumDelay(); i++) {
                            commit.accept(
                                new BukkitElementData(
                                    "minecraft:repeater[delay=" + i + ",locked=false]",
                                    Bukkit
                                        .getServer()
                                        .createBlockData("minecraft:repeater[delay=" + i + ",locked=false]")
                                )
                            );
                            commit.accept(
                                new BukkitElementData(
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
                        new BukkitElementData(
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
            Map.entry(Material.LEVER.getKey(), BukkitElementDataRegistry::leverMatcher),
            Map.entry(Material.REPEATER.getKey(), BukkitElementDataRegistry::repeaterMatcher),
            Map.entry(Material.COMPARATOR.getKey(), BukkitElementDataRegistry::comparatorMatcher)
        )
    );

    public static BukkitElementDataRegistry getInstance() {
        return BukkitElementDataRegistry.instance;
    }

    protected BukkitElementDataRegistry() {}

    @Override
    public ElementData get(Identifier key, String name) {
        Collection<BukkitElementData> blockData = registry.get(((BukkitIdentifier) key).getNamespacedKey());
        if (Objects.isNull(blockData)) {
            return null;
        }
        return blockData.stream().filter(d -> d.getStateName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public ElementData match(Element commonBlock) {
        BukkitBlock block = (BukkitBlock) commonBlock;
        BlockState blockState = block.getBlockState();
        NamespacedKey id = blockState.getBlockData().getMaterial().getKey();
        Collection<BukkitElementData> knownStates = registry.get(id);
        BiPredicate<org.bukkit.block.data.BlockData, BlockState> matcher = matchers.get(id);
        if (Objects.isNull(knownStates)) {
            return null;
        }
        for (BukkitElementData knownState : knownStates) {
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
