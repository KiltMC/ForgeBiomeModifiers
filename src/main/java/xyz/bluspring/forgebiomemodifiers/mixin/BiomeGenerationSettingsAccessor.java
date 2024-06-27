package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.class)
public interface BiomeGenerationSettingsAccessor {
    @Accessor
    Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>> getCarvers();

    @Mutable
    @Accessor
    void setFlowerFeatures(Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures);
}
