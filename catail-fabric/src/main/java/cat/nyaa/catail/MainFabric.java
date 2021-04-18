package cat.nyaa.catail;

import static net.minecraft.block.Blocks.LEVER;

import cat.nyaa.catail.common.Area;
import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.LocaleFallback;
import cat.nyaa.catail.common.Localizer;
import cat.nyaa.catail.common.fabric.*;
import cat.nyaa.catail.common.impl.CommonArea;
import cat.nyaa.catail.common.impl.CommonLocaleFallback;
import cat.nyaa.catail.common.impl.CommonLocalizer;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.PositionImpl;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.level.ServerWorldProperties;

public class MainFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("Catail onInitialize");
        ServerWorldEvents.LOAD.register(
            (s, w) -> {
                LocaleFallback localeFallback = new CommonLocaleFallback(
                    Locale.SIMPLIFIED_CHINESE,
                    Collections.emptyMap()
                );
                Localizer localizer = new CommonLocalizer(localeFallback);
                Area area = new CommonArea(
                    FabricIdentifier.get(
                        "catail",
                        "area" +
                        Objects
                            .requireNonNull(
                                w.getServer().getRegistryManager().getDimensionTypes().getId(w.getDimension())
                            )
                            .getPath()
                    ),
                    FabricLocation.get(new PositionImpl(100, 100, 100)),
                    FabricLocation.get(new PositionImpl(-100, -100, -100)),
                    Collections.singletonMap(
                        Locale.SIMPLIFIED_CHINESE,
                        "地区 " +
                        ((ServerWorldProperties) w.getLevelProperties()).getLevelName() +
                        " " +
                        Objects
                            .requireNonNull(
                                w.getServer().getRegistryManager().getDimensionTypes().getId(w.getDimension())
                            )
                            .getPath()
                    ),
                    localizer
                );
                PlayerBlockBreakEvents.AFTER.register(
                    (world, player, pos, state, entity) -> {
                        if (world.equals(w)) {
                            if (area.isInArea(FabricLocation.get(pos))) {
                                player.sendMessage(new LiteralText(area.getName(Locale.TRADITIONAL_CHINESE)), true);
                            }
                        }
                    }
                );
            }
        );

        AttackBlockCallback.EVENT.register(
            (player, world, hand, pos, direction) -> {
                FabricBlock fabricBlock = FabricBlock.get(world, pos);
                BlockData state = fabricBlock.getState();
                if (Objects.isNull(state)) {
                    return ActionResult.PASS;
                }
                FabricBlockType blockType = ((FabricBlockType) state.getBlockType());
                if (blockType.getBlock().equals(LEVER)) {
                    System.out.println("Catail lever!");
                    BlockData data = FabricBlockDataRegistry
                        .getInstance()
                        .get(FabricIdentifier.get(Registry.BLOCK.getId(LEVER)), "minecraft:lever[powered=false]");
                    System.out.println(data);
                    if (Objects.nonNull(data)) {
                        System.out.println(data.getAsString());
                        fabricBlock.setState(data);
                    }
                }
                return ActionResult.PASS;
            }
        );
    }
}
