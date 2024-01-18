package xyz.bluspring.forgebiomemodifiers.worldgen.serializers;


import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifier;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifiers;
import xyz.bluspring.forgebiomemodifiers.worldgen.MobSpawnSettingsBuilder;
import xyz.bluspring.forgebiomemodifiers.worldgen.ModifiableBiomeInfo;

import java.util.List;

/**
 * <p>Stock biome modifier that removes mob spawns from a biome. Has the following json format:</p>
 * <pre>
 * {
 *   "type": "forge:add_spawns", // Required
 *   "biomes": "#namespace:biome_tag", // Accepts a biome id, [list of biome ids], or #namespace:biome_tag
 *   "entity_types": #namespace:entitytype_tag // Accepts an entity type, [list of entity types], or #namespace:entitytype_tag
 * }
 * </pre>
 *
 * @param biomes Biomes to add mob spawns to.
 * @param entityTypes EntityTypes to remove from spawn lists.
 */
public record RemoveSpawnsBiomeModifier(HolderSet<Biome> biomes, HolderSet<EntityType<?>> entityTypes) implements BiomeModifier
{
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
    {
        if (phase == Phase.REMOVE && this.biomes.contains(biome))
        {
            MobSpawnSettingsBuilder spawnBuilder = builder.getMobSpawnSettings();
            for (MobCategory category : MobCategory.values())
            {
                List<MobSpawnSettings.SpawnerData> spawns = spawnBuilder.getSpawner(category);
                spawns.removeIf(spawnerData -> this.entityTypes.contains(BuiltInRegistries.ENTITY_TYPE.getHolder(BuiltInRegistries.ENTITY_TYPE.getResourceKey(spawnerData.type).get()).get()));
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec()
    {
        return BiomeModifiers.REMOVE_SPAWNS_BIOME_MODIFIER_TYPE.get();
    }
}