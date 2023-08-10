package xyz.bluspring.forgebiomemodifiers.worldgen.serializers;


import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifier;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifiers;
import xyz.bluspring.forgebiomemodifiers.worldgen.MobSpawnSettingsBuilder;
import xyz.bluspring.forgebiomemodifiers.worldgen.ModifiableBiomeInfo;

import java.util.List;

/**
 * <p>Stock biome modifier that adds a mob spawn to a biome. Has the following json format:</p>
 * <pre>
 * {
 *   "type": "forge:add_spawns", // Required
 *   "biomes": "#namespace:biome_tag", // Accepts a biome id, [list of biome ids], or #namespace:biome_tag
 *   "spawners":
 *   {
 *     "type": "namespace:entity_type", // Type of mob to spawn
 *     "weight": 100, // int, spawn weighting
 *     "minCount": 1, // int, minimum pack size
 *     "maxCount": 4, // int, maximum pack size
 *   }
 * }
 * </pre>
 * <p>Optionally accepts a list of spawner objects instead of a single spawner:</p>
 * <pre>
 * {
 *   "type": "forge:add_spawns", // Required
 *   "biomes": "#namespace:biome_tag", // Accepts a biome id, [list of biome ids], or #namespace:biome_tag
 *   "spawners":
 *   [
 *     {
 *       "type": "namespace:entity_type", // Type of mob to spawn
 *       "weight": 100, // int, spawn weighting
 *       "minCount": 1, // int, minimum pack size
 *       "maxCount": 4, // int, maximum pack size
 *     },
 *     {
 *       // additional spawner object
 *     }
 *   ]
 * }
 * </pre>
 *
 * @param biomes Biomes to add mob spawns to.
 * @param spawners List of SpawnerDatas specifying EntityType, weight, and pack size.
 */
public record AddSpawnsBiomeModifier(HolderSet<Biome> biomes, List<MobSpawnSettings.SpawnerData> spawners) implements BiomeModifier
{
    /**
     * Convenience method for using a single spawn data.
     * @param biomes Biomes to add mob spawns to.
     * @param spawner SpawnerData specifying EntityTYpe, weight, and pack size.
     * @return AddSpawnsBiomeModifier that adds a single spawn entry to the specified biomes.
     */
    public static AddSpawnsBiomeModifier singleSpawn(HolderSet<Biome> biomes, MobSpawnSettings.SpawnerData spawner)
    {
        return new AddSpawnsBiomeModifier(biomes, List.of(spawner));
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
    {
        if (phase == Phase.ADD && this.biomes.contains(biome))
        {
            MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            for (MobSpawnSettings.SpawnerData spawner : this.spawners)
            {
                EntityType<?> type = spawner.type;
                spawns.addSpawn(type.getCategory(), spawner);
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec()
    {
        return BiomeModifiers.ADD_SPAWNS_BIOME_MODIFIER_TYPE.get();
    }
}