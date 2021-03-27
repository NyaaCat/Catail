package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.Location;
import cat.nyaa.catail.common.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitPlayer implements Player {
    private final org.bukkit.entity.Player player;

    private static final Map<org.bukkit.entity.Player, BukkitPlayer> cache = new ConcurrentHashMap<>();

    public static Player get(org.bukkit.entity.Player player) {
        return cache.computeIfAbsent(player, BukkitPlayer::new);
    }

    protected BukkitPlayer(org.bukkit.entity.Player player) {
        this.player = player;
    }

    @Override
    public UUID getUuid() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public String getDisplayName() {
        return player.getDisplayName();
    }

    @Override
    public Location getLocation() {
        return BukkitLocation.get(player.getLocation());
    }
}
