package cat.nyaa.catail;

import static net.minecraft.block.Blocks.LEVER;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import cat.nyaa.catail.common.*;
import cat.nyaa.catail.common.fabric.*;
import cat.nyaa.catail.common.impl.CommonArea;
import cat.nyaa.catail.common.impl.CommonLocaleFallback;
import cat.nyaa.catail.common.impl.CommonLocalizer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.PositionImpl;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.level.ServerWorldProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainFabric implements ModInitializer {

    private final Logger logger = LogManager.getLogger();

    private final JunctionsRegistry registry = new FakeJunctionsRegistry();

    @Override
    public void onInitialize() {
        logger.info("Catail onInitialize");
        ServerWorldEvents.LOAD.register((s, w) -> {
            if (w.isClient) return;
            LocaleFallback localeFallback = new CommonLocaleFallback(Locale.SIMPLIFIED_CHINESE, Collections.emptyMap());
            Localizer localizer = new CommonLocalizer(localeFallback);
            Area area = new CommonArea(
                FabricIdentifier.get(
                    "catail",
                    "area" +
                    Objects
                        .requireNonNull(
                            w.getServer().getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).getId(w.getDimension())
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
                            w.getServer().getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).getId(w.getDimension())
                        )
                        .getPath()
                ),
                localizer
            );
            PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
                if (world.equals(w) && area.isInArea(FabricLocation.get(pos))) {
                    player.sendMessage(new LiteralText(area.getName(Locale.TRADITIONAL_CHINESE)), true);
                }
            });
        });

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (world.isClient) return ActionResult.PASS;
            FabricBlock fabricBlock = FabricBlock.get(world, pos);
            BlockData state = fabricBlock.getState();
            if (Objects.isNull(state)) {
                return ActionResult.PASS;
            }
            FabricBlockType blockType = ((FabricBlockType) state.getBlockType());
            if (blockType.getBlock().equals(LEVER)) {
                logger.info("Catail lever!");
                BlockData data = FabricBlockDataRegistry
                    .getInstance()
                    .get(FabricIdentifier.get(Registry.BLOCK.getId(LEVER)), "minecraft:lever[powered=false]");
                logger.info(data);
                if (Objects.nonNull(data)) {
                    logger.warn(data.getAsString());
                    fabricBlock.setState(data);
                }
            }
            return ActionResult.PASS;
        });

        CommandRegistrationCallback.EVENT.register(this::command);
    }

    private void command(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        // A command that exists on both types of servers
        dispatcher.register(
            literal("catail")
                .then(
                    literal("reset")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(
                            argument("id", IdentifierArgumentType.identifier())
                                .suggests(this::suggestions)
                                .executes(ctx -> {
                                    logger.info(ctx.getArgument("id", Identifier.class));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }

    private CompletableFuture<Suggestions> suggestions(
        final CommandContext<ServerCommandSource> context,
        final SuggestionsBuilder builder
    ) {
        String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
        registry.byId().keySet().stream()
                .map(Identifier::toString)
                .map(str -> str.toLowerCase(Locale.ROOT))
                .filter(str -> str.startsWith(remaining))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
