package cat.nyaa.catail.common.bukkit;

import cat.nyaa.catail.common.Location;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;

public class BukkitLocation implements Location {

    private static final CacheLoader<org.bukkit.Location, Location> loader = new CacheLoader<org.bukkit.Location, Location>() {
        public Location load(org.bukkit.Location key) {
            return new BukkitLocation(key);
        }
    };
    private static final LoadingCache<org.bukkit.Location, Location> cache = CacheBuilder
        .newBuilder()
        .maximumSize(8192)
        .build(loader);

    public static Location get(org.bukkit.Location location) {
        try {
            return cache.get(location);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private final org.bukkit.Location location;

    protected BukkitLocation(org.bukkit.Location location) {
        this.location = location;
    }

    @Override
    public double getX() {
        return location.getX();
    }

    @Override
    public double getY() {
        return location.getY();
    }

    @Override
    public double getZ() {
        return location.getZ();
    }
}
