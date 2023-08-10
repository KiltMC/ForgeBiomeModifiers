package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(MobSpawnSettings.Builder.class)
public interface MSSBuilderAccessor {
    @Accessor
    Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners();

    @Mutable
    @Accessor
    void setSpawners(Map<MobCategory, List<MobSpawnSettings.SpawnerData>> spawners);

    @Accessor
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts();

    @Mutable
    @Accessor
    void setMobSpawnCosts(Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts);

    @Accessor
    float getCreatureGenerationProbability();

    @Accessor
    void setCreatureGenerationProbability(float creatureGenerationProbability);
}
