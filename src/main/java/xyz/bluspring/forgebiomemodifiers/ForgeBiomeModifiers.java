package xyz.bluspring.forgebiomemodifiers;

import io.github.fabricators_of_create.porting_lib.registries.RegistryEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.bluspring.forgebiomemodifiers.mixin.BiomeAccessor;
import xyz.bluspring.forgebiomemodifiers.mixin.BiomeSelectionContextImplAccessor;
import xyz.bluspring.forgebiomemodifiers.mixin.MobSpawnSettingsAccessor;
import xyz.bluspring.forgebiomemodifiers.structures.StructureModifier;
import xyz.bluspring.forgebiomemodifiers.structures.StructureModifiers;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifier;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifiers;
import xyz.bluspring.forgebiomemodifiers.worldgen.ModifiableBiomeInfo;

public class ForgeBiomeModifiers implements ModInitializer {
    @Override
    public void onInitialize() {
        RegistryEvents.NEW_DATAPACK_REGISTRY.register(registry -> {
            registry.register(new RegistryDataLoader.RegistryData<>(BiomeModifiers.BIOME_MODIFIER_KEY, BiomeModifier.DIRECT_CODEC));
            registry.register(new RegistryDataLoader.RegistryData<>(StructureModifiers.STRUCTURE_MODIFIER_KEY, StructureModifier.DIRECT_CODEC));
        });

        BiomeModifications.create(new ResourceLocation("forge_biome_modifier", "biome_modifier"))
            .add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), (selection, modification) -> {
                var biome = selection.getBiome();
                var info = runAllModifiers(selection, BiomeModifier.Phase.ADD);

                // carvers
                for (GenerationStep.Carving value : GenerationStep.Carving.values()) {
                    var carvers = info.generationSettings().getCarvers(value);
                    var original = biome.getGenerationSettings().getCarvers(value);

                    for (Holder<ConfiguredWorldCarver<?>> carver : carvers) {
                        var key = carver.unwrapKey().orElseThrow();
                        if (original instanceof HolderSet<ConfiguredWorldCarver<?>> holderSet && holderSet.stream().anyMatch(e -> e.is(key)))
                            continue;

                        modification.getGenerationSettings().addCarver(value, key);
                    }
                }

                // features
                for (HolderSet<PlacedFeature> feature : info.generationSettings().features()) {
                    for (GenerationStep.Decoration value : GenerationStep.Decoration.values()) {
                        if (value.ordinal() >= feature.size())
                            break;

                        if (biome.getGenerationSettings().hasFeature(feature.get(value.ordinal()).value()))
                            continue;

                        modification.getGenerationSettings().addFeature(value, feature.get(value.ordinal()).unwrapKey().orElseThrow());
                    }
                }

                // TODO: flowers?

                // mob spawning
                modification.getSpawnSettings().setCreatureSpawnProbability(info.mobSpawnSettings().getCreatureProbability());

                for (MobCategory value : MobCategory.values()) {
                    var list = info.mobSpawnSettings().getMobs(value).unwrap();

                    for (MobSpawnSettings.SpawnerData spawnerData : list) {
                        modification.getSpawnSettings().addSpawn(value, spawnerData);
                    }
                }

                ((MobSpawnSettingsAccessor) info.mobSpawnSettings()).getMobSpawnCosts().forEach((entityType, mobSpawnCost) -> {
                    modification.getSpawnSettings().setSpawnCost(entityType, mobSpawnCost.charge(), mobSpawnCost.energyBudget());
                });
            })
            .add(ModificationPhase.POST_PROCESSING, BiomeSelectors.all(), (selection, modification) -> {
                var info = runAllModifiers(selection, BiomeModifier.Phase.AFTER_EVERYTHING);

                // TODO: everything else
            })
            .add(ModificationPhase.REMOVALS, BiomeSelectors.all(), (selection, modification) -> {
                var info = runAllModifiers(selection, BiomeModifier.Phase.REMOVE);

                // TODO: everything else
            })
            .add(ModificationPhase.REPLACEMENTS, BiomeSelectors.all(), (selection, modification) -> {
                var info = runAllModifiers(selection, BiomeModifier.Phase.MODIFY);

                // TODO: everything else
            });

        StructureModifiers.STRUCTURE_MODIFIER_SERIALIZERS.register();
        BiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register();
    }

    private ModifiableBiomeInfo.BiomeInfo runAllModifiers(BiomeSelectionContext selection, BiomeModifier.Phase phase) {
        var registryAccess = ((BiomeSelectionContextImplAccessor) selection).getDynamicRegistries();
        var registry = registryAccess.registryOrThrow(BiomeModifiers.BIOME_MODIFIER_KEY);

        var biomeHolder = selection.getBiomeRegistryEntry();
        var biome = selection.getBiome();
        var builder = ModifiableBiomeInfo.BiomeInfo.Builder.copyOf(new ModifiableBiomeInfo.BiomeInfo(((BiomeAccessor) (Object) biome).getClimateSettings(), biome.getSpecialEffects(), biome.getGenerationSettings(), biome.getMobSettings()));

        for (BiomeModifier modifier : registry) {
            modifier.modify(biomeHolder, phase, builder);
        }

        return builder.build();
    }
}
