package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifiers;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(method = "initServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/server/IntegratedServer;initializeKeyPair()V"))
    public void lc$loadBiomeModifiers(CallbackInfoReturnable<Boolean> cir) {
        BiomeModifiers.runModifiers((MinecraftServer) (Object) this);
    }
}
