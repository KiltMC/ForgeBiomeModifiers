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

import java.util.EnumSet;
import java.util.Set;

/**
 * <p>Stock biome modifier that removes features from biomes. Has the following json format:</p>
 * <pre>
 * {
 *   "type": "forge:removefeatures", // required
 *   "biomes": "#namespace:your_biome_tag", // accepts a biome id, [list of biome ids], or #namespace:biome_tag
 *   "features": "namespace:your_feature", // accepts a placed feature id, [list of placed feature ids], or #namespace:feature_tag
 *   "steps": "underground_ores" OR ["underground_ores", "vegetal_decoration"] // one or more decoration steps; optional field, defaults to all steps if not specified
 * }
 * </pre>
 *
 * @param biomes Biomes to remove features from.
 * @param features PlacedFeatures to remove from biomes.
 * @param steps Decoration steps to remove features from.
 */
public record RemoveFeaturesBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, Set<GenerationStep.Decoration> steps) implements BiomeModifier
{
    /**
     * Creates a modifier that removes the given features from all decoration steps in the given biomes.
     * @param biomes Biomes to remove features from.
     * @param features PlacedFeatures to remove from biomes.
     */
    public static RemoveFeaturesBiomeModifier allSteps(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features)
    {
        return new RemoveFeaturesBiomeModifier(biomes, features, EnumSet.allOf(GenerationStep.Decoration.class));
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
    {
        if (phase == Phase.REMOVE && this.biomes.contains(biome))
        {
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            for (GenerationStep.Decoration step : this.steps)
            {
                generationSettings.getFeatures(step).removeIf(this.features::contains);
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec()
    {
        return BiomeModifiers.REMOVE_FEATURES_BIOME_MODIFIER_TYPE.get();
    }
}