package xyz.bluspring.forgebiomemodifiers;

import net.fabricmc.api.ModInitializer;
import xyz.bluspring.forgebiomemodifiers.events.RegisterDatapackRegistryCallback;
import xyz.bluspring.forgebiomemodifiers.registries.DatapackRegistryInfo;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifier;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifiers;

public class ForgeBiomeModifiers implements ModInitializer {
    @Override
    public void onInitialize() {
        RegisterDatapackRegistryCallback.EVENT.register((lists) -> {
            lists.add(new DatapackRegistryInfo(BiomeModifiers.BIOME_MODIFIER_KEY, BiomeModifier.DIRECT_CODEC));
        });
    }
}
