package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {
    @Inject(method = "initServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/SkullBlockEntity;setup(Lnet/minecraft/server/Services;Ljava/util/concurrent/Executor;)V"))
    public void lc$setupBiomeModifiers(CallbackInfoReturnable<Boolean> cir) {

    }
}
