package xyz.bluspring.forgebiomemodifiers.worldgen;


import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.bluspring.forgebiomemodifiers.mixin.BGSBuilderAccessor;
import xyz.bluspring.forgebiomemodifiers.mixin.BiomeGenerationSettingsAccessor;

import java.util.ArrayList;
import java.util.List;

public class BiomeGenerationSettingsBuilder extends BiomeGenerationSettings.PlainBuilder
{
    public BiomeGenerationSettingsBuilder(BiomeGenerationSettings orig)
    {
        var carvers = self().getCarvers();
        ((BiomeGenerationSettingsAccessor) orig).getCarvers().keySet().forEach(k -> {
            carvers.put(k, new ArrayList<>());
            orig.getCarvers(k).forEach(v -> carvers.get(k).add(v));
        });
        orig.features().forEach(l -> {
            final ArrayList<Holder<PlacedFeature>> featureList = new ArrayList<>();
            l.forEach(featureList::add);
            self().getFeatures().add(featureList);
        });
    }

    private BGSBuilderAccessor self() {
        return ((BGSBuilderAccessor) this);
    }

    public List<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration stage) {
        self().callAddFeatureStepsUpTo(stage.ordinal());
        return self().getFeatures().get(stage.ordinal());
    }

    public List<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving stage) {
        return self().getCarvers().computeIfAbsent(stage, key -> new ArrayList<>());
    }
}