package xyz.bluspring.forgebiomemodifiers;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.RegistryDataLoader;
import xyz.bluspring.forgebiomemodifiers.events.RegisterDatapackRegistryCallback;
import xyz.bluspring.forgebiomemodifiers.structures.StructureModifier;
import xyz.bluspring.forgebiomemodifiers.structures.StructureModifiers;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifier;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifiers;

public class ForgeBiomeModifiers implements ModInitializer {
    @Override
    public void onInitialize() {
        RegisterDatapackRegistryCallback.EVENT.register((lists) -> {
            lists.add(new RegistryDataLoader.RegistryData<>(BiomeModifiers.BIOME_MODIFIER_KEY, BiomeModifier.DIRECT_CODEC));
            lists.add(new RegistryDataLoader.RegistryData<>(StructureModifiers.STRUCTURE_MODIFIER_KEY, StructureModifier.DIRECT_CODEC));
        });

        StructureModifiers.STRUCTURE_MODIFIER_SERIALIZERS.register();
        StructureModifiers.STRUCTURE_MODIFIERS.register();
        BiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register();
        BiomeModifiers.BIOME_MODIFIERS.register();
    }
}
