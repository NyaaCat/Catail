package cat.nyaa.catail;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import cat.nyaa.catail.common.*;
import cat.nyaa.catail.common.fabric.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainFabric implements ModInitializer {

    private final Logger logger = LogManager.getLogger();

    private JunctionsRegistry registry;

    @Override
    public void onInitialize() {
        logger.info("Catail onInitialize");
        ServerWorldEvents.LOAD.register((s, w) -> {
            if (w.isClient) return;
            if (
                w
                    .getDimension()
                    .equals(
                        w
                            .getServer()
                            .getRegistryManager()
                            .get(Registry.DIMENSION_TYPE_KEY)
                            .get(DimensionType.OVERWORLD_REGISTRY_KEY)
                    )
            ) {
                registry = new FakeJunctionsRegistry(w);
            }
        });

        PlayerBlockBreakEvents.BEFORE.register((w, player,pos,state,blockEntity) -> false);

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
                                    Identifier id = ctx.getArgument("id", Identifier.class);
                                    logger.info(id);
                                    try {
                                        logger.info(registry.byId().get(FabricIdentifier.get("catail", "fake")));
                                        Junctions junctions = registry.getById(FabricIdentifier.get(id));
                                        logger.info(junctions);
                                        logger.info(FabricIdentifier.dumpCache());
                                        junctions.getJunctionStates().get("fake").applyAllBlockState();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(
                    literal("test")
                        .then(
                            argument("uuid", UuidArgumentType.uuid())
                                .executes(context -> {
                                    UUID uuid = UuidArgumentType.getUuid(context, "uuid");
                                    @SuppressWarnings("unchecked")
                                    FabricEntity<Entity> itemFrame = FabricEntity.get(
                                        context.getSource().getWorld(),
                                        uuid,
                                        null,
                                        (EntityType<Entity>) Registry.ENTITY_TYPE.get(
                                            (
                                                (FabricIdentifier) (
                                                    FabricIdentifier.get(Identifier.DEFAULT_NAMESPACE, "item_frame")
                                                )
                                            ).getIdentifier()
                                        )
                                    );
                                    logger.info(itemFrame.getEntity());
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
        registry
            .byId()
            .keySet()
            .stream()
            .map(i -> i.getNamespace() + ":" + i.getKey())
            .map(str -> str.toLowerCase(Locale.ROOT))
            .filter(str -> str.startsWith(remaining))
            .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
