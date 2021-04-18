package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Location;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;

public class FabricLocation implements Location {

    private static final CacheLoader<BlockPos, Location> loader = new CacheLoader<BlockPos, Location>() {
        public Location load(BlockPos key) {
            return new FabricLocation(key);
        }
    };

    private static final LoadingCache<BlockPos, Location> cache = CacheBuilder
        .newBuilder()
        .maximumSize(8192)
        .build(loader);

    public static Location get(Position position) {
        try {
            return cache.get(new BlockPos(position));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Location get(BlockPos position) {
        try {
            return cache.get(position);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private final BlockPos position;

    protected FabricLocation(BlockPos position) {
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
