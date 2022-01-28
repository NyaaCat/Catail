package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Element;
import cat.nyaa.catail.common.ElementData;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FabricBlock<T extends Block> implements Element<T> {

    private static final Map<BlockPos, Map<World, FabricBlock<Block>>> cache = new ConcurrentHashMap<>();

    public static FabricBlock<Block> get(World world, BlockPos pos) {
        return cache
            .computeIfAbsent(pos, p -> new ConcurrentHashMap<>())
            .computeIfAbsent(world, w -> new FabricBlock<>(w, pos));
    }

    private final BlockPos pos;
    private final World world;

    protected FabricBlock(World world, BlockPos pos) {
        this.pos = pos;
        this.world = world;
    }

    @Override
    public ElementData<T> getState() {
        return FabricElementDataRegistry.getInstance().match(this);
    }

    @Override
    public double getX() {
        return pos.getX();
    }

    @Override
    public double getY() {
        return pos.getY();
    }

    @Override
    public double getZ() {
        return pos.getZ();
    }

    @Override
    public void setState(ElementData<T> state) {
        BlockState blockState = world.getBlockState(pos);
        System.out.println("setState " + pos.toShortString() + " " + state.getAsString() + " " + blockState);
        BlockState newBlockState = ((FabricBlockState<T>) state).getTransformer().apply(blockState);
        if (Objects.nonNull(newBlockState)) {
            System.out.println(
                "newBlockState " + pos.toShortString() + " " + state.getAsString() + " " + newBlockState
            );
            ((FabricBlockState<T>) state).getApplier().accept(world, pos, newBlockState);
        }
    }

    public World getWorld() {
        return world;
    }

    public BlockState getBlockState() {
        return world.getBlockState(pos);
    }
}
