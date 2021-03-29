package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Location;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import net.minecraft.util.math.Position;

public class FabricLocation implements Location {

    private static final CacheLoader<Position, Location> loader = new CacheLoader<>() {
        public Location load(Position key) {
            return new FabricLocation(key);
        }
    };

    private static final LoadingCache<Position, Location> cache = CacheBuilder
        .newBuilder()
        .maximumSize(8192)
        .build(loader);

    public static Location get(Position position) {
        try {
            return cache.get(position);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private final Position position;

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
