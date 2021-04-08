package cat.nyaa.catail;

import static net.minecraft.block.Blocks.LEVER;

import cat.nyaa.catail.common.BlockData;
import cat.nyaa.catail.common.fabric.FabricBlock;
import cat.nyaa.catail.common.fabric.FabricBlockDataRegistry;
import cat.nyaa.catail.common.fabric.FabricBlockType;
import cat.nyaa.catail.common.fabric.FabricIdentifier;
import java.util.Objects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;

public class MainFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("Catail onInitialize");
        AttackBlockCallback.EVENT.register(
            (player, world, hand, pos, direction) -> {
                FabricBlock fabricBlock = FabricBlock.get(world, pos);
                BlockData state = fabricBlock.getState();
                if (Objects.isNull(state)) {
                    return ActionResult.PASS;
                }
                FabricBlockType blockType = ((FabricBlockType) state.getBlockType());
                if (blockType.getBlock().equals(LEVER)) {
                    System.out.println("Catail lever!");
                    BlockData data = FabricBlockDataRegistry
                        .getInstance()
                        .get(FabricIdentifier.get(Registry.BLOCK.getId(LEVER)), "minecraft:lever[powered=false]");
                    System.out.println(data);
                    if (Objects.nonNull(data)) {
                        System.out.println(data.getAsString());
                        fabricBlock.setState(data);
                    }
                }
                return ActionResult.PASS;
            }
        );
    }
}
