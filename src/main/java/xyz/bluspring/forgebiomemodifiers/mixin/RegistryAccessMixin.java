package xyz.bluspring.forgebiomemodifiers.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bluspring.forgebiomemodifiers.events.RegisterDatapackRegistryCallback;
import xyz.bluspring.forgebiomemodifiers.registries.DatapackRegistryInfo;

import java.util.ArrayList;

@Mixin(RegistryAccess.class)
public interface RegistryAccessMixin {
    @Shadow
    private static <E> void put(ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> builder, ResourceKey registryKey, Codec elementCodec) {
    }

    @ModifyReceiver(method = "method_30531", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"))
    private static ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> lc$appendCustomRegistries(ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> builder) {
        var registries = new ArrayList<DatapackRegistryInfo<?>>();

        RegisterDatapackRegistryCallback.EVENT.invoker().registerDatapacks(registries);

        for (DatapackRegistryInfo<?> registry : registries) {
            put(builder, registry.registryKey(), registry.codec());
        }

        return builder;
    }
}
