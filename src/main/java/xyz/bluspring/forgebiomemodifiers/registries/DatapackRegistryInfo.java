package xyz.bluspring.forgebiomemodifiers.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record DatapackRegistryInfo<E>(
        ResourceKey<? extends Registry<E>> registryKey,
        Codec<E> codec
) {
}
