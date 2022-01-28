package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Element;
import cat.nyaa.catail.common.ElementDataGroup;
import cat.nyaa.catail.common.Identifier;
import cat.nyaa.catail.common.Junctions;
import cat.nyaa.catail.common.impl.CommonElementDataGroup;
import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class FakeJunctions implements Junctions {

    private final Map<String, ElementDataGroup> state;
    private final FabricBlock<Block> block1;
    private final FabricBlock<Block> block2;
    private final FabricBlock<Block> block3;
    private final FabricEntity<Entity> entity;

    @SuppressWarnings("unchecked")
    public FakeJunctions(ServerWorld world) {
        this.block1 = FabricBlock.get(world, BlockPos.ORIGIN.add(0, 100, 0));
        this.block2 = FabricBlock.get(world, BlockPos.ORIGIN.add(14, 103, 5));
        this.block3 = FabricBlock.get(world, BlockPos.ORIGIN.add(-7, 101, 5));
        Identifier lever = FabricIdentifier.get(net.minecraft.util.Identifier.DEFAULT_NAMESPACE, "lever");
        Identifier comparator = FabricIdentifier.get(net.minecraft.util.Identifier.DEFAULT_NAMESPACE, "comparator");
        Identifier itemFrame = FabricIdentifier.get(net.minecraft.util.Identifier.DEFAULT_NAMESPACE, "item_frame");
        this.entity =
            FabricEntity.get(
                world,
                UUID.fromString("37cfb8f7-63d0-48d6-b93d-9a0070a7af91"),
                null,
                (EntityType<Entity>) Registry.ENTITY_TYPE.get(((FabricIdentifier) (itemFrame)).getIdentifier())
            );

        state =
            Map.of(
                "fake",
                new CommonElementDataGroup(
                    Map.of(
                        block1,
                        FabricElementDataRegistry.getInstance().get(lever, "minecraft:lever[powered=true]"),
                        block2,
                        FabricElementDataRegistry.getInstance().get(lever, "minecraft:lever[powered=false]"),
                        block3,
                        FabricElementDataRegistry.getInstance().get(comparator, "minecraft:comparator[mode=subtract]"),
                        entity,
                        FabricElementDataRegistry.getInstance().get(itemFrame, "minecraft:item_frame{ItemRotation:2b}")
                    )
                )
            );
    }

    @Override
    public Identifier getId() {
        return FabricIdentifier.get("catail", "fake");
    }

    @Override
    public String getName(Locale locale) {
        return "fake";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Element<?>> getJunctionElements() {
        return Set.of(block1, block2, block3, entity);
    }

    @Override
    public Map<String, ElementDataGroup> getJunctionStates() {
        return state;
    }

    @Override
    public Map<String, String> getJunctionStateNames() {
        return Map.of("fake", "fake");
    }

    @Override
    public void addJunctionState(String id, String name, ElementDataGroup state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeJunctionState(String id, String name, ElementDataGroup state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getZ() {
        return 100;
    }
}
