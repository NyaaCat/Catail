package cat.nyaa.catail.fabric.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class ServerWorldMixin {
    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("HEAD"), method = "breakBlock(Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/entity/Entity;I)Z", cancellable = true)
    private void breakBlock(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        if (ServerWorld.class.isAssignableFrom(this.getClass())) {
            System.out.printf("breakBlock: %s %b %s %d %n", pos, drop, breakingEntity, maxUpdateDepth);
            new RuntimeException().printStackTrace();
            cir.setReturnValue(false);
        }
    }
}
