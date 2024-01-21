package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RegistryDataLoader.class)
public interface RegistryDataLoaderAccessor {
    @Accessor("WORLDGEN_REGISTRIES")
    static List<RegistryDataLoader.RegistryData<?>> getWorldGenRegistries() {
        throw new UnsupportedOperationException();
    }
}
