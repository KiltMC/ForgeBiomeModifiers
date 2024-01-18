package xyz.bluspring.forgebiomemodifiers.worldgen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.bluspring.forgebiomemodifiers.extensions.ModifiableBiomeExtension;
import xyz.bluspring.forgebiomemodifiers.worldgen.serializers.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class BiomeModifiers {
    public static final ResourceKey<Registry<BiomeModifier>> BIOME_MODIFIER_KEY = ResourceKey.createRegistryKey(new ResourceLocation("forge", "forge/biome_modifier"));
    //public static final LazyRegistrar<BiomeModifier> BIOME_MODIFIERS = LazyRegistrar.create(BIOME_MODIFIER_KEY, "forge");

    public static final ResourceKey<Registry<Codec<? extends BiomeModifier>>> BIOME_MODIFIER_SERIALIZER_KEY = ResourceKey.createRegistryKey(new ResourceLocation("forge", "forge/biome_modifier_serializers"));
    public static final LazyRegistrar<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = LazyRegistrar.create(BIOME_MODIFIER_SERIALIZER_KEY, "forge");
    public static final Supplier<Registry<Codec<? extends BiomeModifier>>> BIOME_MODIFIER_SERIALIZER_REGISTRY = BIOME_MODIFIER_SERIALIZERS.makeRegistry();

    public static void runModifiers(MinecraftServer server) {
        var registries = server.registryAccess();

        final List<BiomeModifier> biomeModifiers = registries.registryOrThrow(BIOME_MODIFIER_KEY)
                .holders()
                .map(Holder::value)
                .toList();

        registries.registryOrThrow(Registries.BIOME).holders().forEach(biomeHolder ->
        {
            ((ModifiableBiomeExtension) (Object) biomeHolder.value()).modifiableBiomeInfo().applyBiomeModifiers(biomeHolder, biomeModifiers);
        });
    }

    /**
     * Noop biome modifier. Can be used in a biome modifier json with "type": "forge:none".
     */
    public static final RegistryObject<Codec<NoneBiomeModifier>> NONE_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("none", () -> Codec.unit(NoneBiomeModifier.INSTANCE));

    /**
     * Stock biome modifier for adding features to biomes.
     */
    public static final RegistryObject<Codec<AddFeaturesBiomeModifier>> ADD_FEATURES_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_features", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddFeaturesBiomeModifier::biomes),
                    PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeaturesBiomeModifier::features),
                    GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddFeaturesBiomeModifier::step)
            ).apply(builder, AddFeaturesBiomeModifier::new))
    );

    /**
     * Stock biome modifier for removing features from biomes.
     */
    public static final RegistryObject<Codec<RemoveFeaturesBiomeModifier>> REMOVE_FEATURES_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("remove_features", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(RemoveFeaturesBiomeModifier::biomes),
                    PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(RemoveFeaturesBiomeModifier::features),
                    new ExtraCodecs.EitherCodec<>(GenerationStep.Decoration.CODEC.listOf(), GenerationStep.Decoration.CODEC).xmap(
                            either -> either.map(Set::copyOf, Set::of), // convert list/singleton to set when decoding
                            set -> set.size() == 1 ? Either.right(set.toArray(GenerationStep.Decoration[]::new)[0]) : Either.left(List.copyOf(set))
                    ).optionalFieldOf("steps", EnumSet.allOf(GenerationStep.Decoration.class)).forGetter(RemoveFeaturesBiomeModifier::steps)
            ).apply(builder, RemoveFeaturesBiomeModifier::new))
    );

    /**
     * Stock biome modifier for adding mob spawns to biomes.
     */
    public static final RegistryObject<Codec<AddSpawnsBiomeModifier>> ADD_SPAWNS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_spawns", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddSpawnsBiomeModifier::biomes),
                    // Allow either a list or single spawner, attempting to decode the list format first.
                    // Uses the better EitherCodec that logs both errors if both formats fail to parse.
                    new ExtraCodecs.EitherCodec<>(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of), // convert list/singleton to list when decoding
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list) // convert list to singleton/list when encoding
                    ).fieldOf("spawners").forGetter(AddSpawnsBiomeModifier::spawners)
            ).apply(builder, AddSpawnsBiomeModifier::new))
    );

    /**
     * Stock biome modifier for removing mob spawns from biomes.
     */
    public static final RegistryObject<Codec<RemoveSpawnsBiomeModifier>> REMOVE_SPAWNS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("remove_spawns", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(RemoveSpawnsBiomeModifier::biomes),
                    RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entity_types").forGetter(RemoveSpawnsBiomeModifier::entityTypes)
            ).apply(builder, RemoveSpawnsBiomeModifier::new))
    );
}
