package xyz.bluspring.forgebiomemodifiers.mixin;

import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.registries.RegistryEvents;
import net.minecraft.resources.RegistryDataLoader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RegistryEvents.NewDatapackRegistry.class)
public class NewDatapackRegistryMixin {
    @Inject(method = "register(Lnet/minecraft/resources/RegistryDataLoader$RegistryData;Lcom/mojang/serialization/Codec;)V", at = @At("TAIL"))
    private <T> void addDynamicRegistry(RegistryDataLoader.RegistryData<T> registryData, @Nullable Codec<T> networkCodec, CallbackInfo ci) {
        RegistryDataLoader.WORLDGEN_REGISTRIES.add(registryData);
    }
}
