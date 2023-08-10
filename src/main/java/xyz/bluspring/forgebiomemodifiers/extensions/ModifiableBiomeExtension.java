package xyz.bluspring.forgebiomemodifiers.extensions;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import xyz.bluspring.forgebiomemodifiers.worldgen.ModifiableBiomeInfo;

public interface ModifiableBiomeExtension {
    ModifiableBiomeInfo modifiableBiomeInfo();

    Biome.ClimateSettings getModifiedClimateSettings();

    BiomeSpecialEffects getModifiedSpecialEffects();
}
