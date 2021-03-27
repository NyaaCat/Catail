package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Location;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.util.math.Position;

import java.util.concurrent.ExecutionException;

public class FabricLocation implements Location {
    private final Position position;
    private static final CacheLoader<Position, Location> loader = new CacheLoader<Position, Location>() {
        public Location load(Position key) {
            return new FabricLocation(key);
        }
    };
    private static final LoadingCache<Position, Location> cache = CacheBuilder.newBuilder().maximumSize(8192).build(loader);

    public static Location get(Position position) {
        try {
            return cache.get(position);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    protected FabricLocation(Position position) {
        this.position = position;
    }

    @Override
    public double getX() {
        return position.getX();
    }

    @Override
    public double getY() {
        return position.getY();
    }

    @Override
    public double getZ() {
        return position.getZ();
    }
}
