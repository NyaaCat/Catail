package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Location;
import cat.nyaa.catail.common.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.PlayerEntity;

public class FabricPlayer implements Player {

    private static final Map<PlayerEntity, FabricPlayer> cache = new ConcurrentHashMap<>();

    public static Player get(PlayerEntity player) {
        return cache.computeIfAbsent(player, FabricPlayer::new);
    }

    private final PlayerEntity player;

    protected FabricPlayer(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public UUID getUuid() {
        return player.getUuid();
    }

    @Override
    public String getName() {
        return player.getName().asString();
    }

    @Override
    public String getDisplayName() {
        return player.getDisplayName().asString();
    }

    @Override
    public Location getLocation() {
        return FabricLocation.get(player.getPos());
    }
}
