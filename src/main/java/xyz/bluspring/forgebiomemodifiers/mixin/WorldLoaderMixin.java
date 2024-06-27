package xyz.bluspring.forgebiomemodifiers.mixin;

import io.github.fabricators_of_create.porting_lib.registries.DynamicRegistryHandler;
import net.minecraft.server.WorldLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(WorldLoader.class)
public class WorldLoaderMixin {
    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/RegistryLayer;createRegistryAccess()Lnet/minecraft/core/LayeredRegistryAccess;", shift = At.Shift.BEFORE))
    private static <D, R> void loadPortingLibRegistriesEarlier(WorldLoader.InitConfig initConfig, WorldLoader.WorldDataSupplier<D> worldDataSupplier, WorldLoader.ResultFactory<D, R> resultFactory, Executor backgroundExecutor, Executor executor, CallbackInfoReturnable<CompletableFuture<R>> cir) {
        DynamicRegistryHandler.loadDynamicRegistries();
    }
}
