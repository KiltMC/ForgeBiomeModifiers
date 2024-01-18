package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Map;

@Mixin(BiomeGenerationSettings.PlainBuilder.class)
public interface BGSBuilderAccessor {
    @Accessor
    Map<GenerationStep.Carving, List<Holder<ConfiguredWorldCarver<?>>>> getCarvers();

    @Invoker
    void callAddFeatureStepsUpTo(int step);

    @Accessor
    List<List<Holder<PlacedFeature>>> getFeatures();
}
