package xyz.bluspring.forgebiomemodifiers.worldgen.serializers;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeGenerationSettingsBuilder;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifier;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifiers;
import xyz.bluspring.forgebiomemodifiers.worldgen.ModifiableBiomeInfo;

/**
 * <p>Stock biome modifier that adds features to biomes. Has the following json format:</p>
 * <pre>
 * {
 *   "type": "forge:add_features", // required
 *   "biomes": "#namespace:your_biome_tag" // accepts a biome id, [list of biome ids], or #namespace:biome_tag
 *   "features": "namespace:your_feature", // accepts a placed feature id, [list of placed feature ids], or #namespace:feature_tag
 *   "step": "underground_ores" // accepts a Decoration enum name
 * }
 * </pre>
 * <p>Be wary of using this to add vanilla PlacedFeatures to biomes, as doing so may cause a feature cycle violation.</p>
 *
 * @param biomes Biomes to add features to.
 * @param features PlacedFeatures to add to biomes.
 * @param step Decoration step to run features in.
 */
public record AddFeaturesBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, GenerationStep.Decoration step) implements BiomeModifier
{
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
    {
        if (phase == Phase.ADD && this.biomes.contains(biome))
        {
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec()
    {
        return BiomeModifiers.ADD_FEATURES_BIOME_MODIFIER_TYPE.get();
    }
}
