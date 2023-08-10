package xyz.bluspring.forgebiomemodifiers.worldgen;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.Nullable;
import xyz.bluspring.forgebiomemodifiers.mixin.MSSBuilderAccessor;
import xyz.bluspring.forgebiomemodifiers.mixin.MobSpawnSettingsAccessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MobSpawnSettingsBuilder extends MobSpawnSettings.Builder
{
    private final Set<MobCategory> typesView = Collections.unmodifiableSet(this.self().getSpawners().keySet());
    private final Set<EntityType<?>> costView = Collections.unmodifiableSet(this.self().getMobSpawnCosts().keySet());

    public MobSpawnSettingsBuilder(MobSpawnSettings orig)
    {
        ((MobSpawnSettingsAccessor) orig).getSpawners().keySet().forEach(k -> {
            self().getSpawners().get(k).clear();
            self().getSpawners().get(k).addAll(orig.getMobs(k).unwrap());
        });
        ((MobSpawnSettingsAccessor) orig).getMobSpawnCosts().keySet().forEach(k -> self().getMobSpawnCosts().put(k, orig.getMobSpawnCost(k)));
        self().setCreatureGenerationProbability(orig.getCreatureProbability());
    }

    private MSSBuilderAccessor self() {
        return (MSSBuilderAccessor) this;
    }

    public Set<MobCategory> getSpawnerTypes()
    {
        return this.typesView;
    }

    public List<MobSpawnSettings.SpawnerData> getSpawner(MobCategory type)
    {
        return this.self().getSpawners().get(type);
    }

    public Set<EntityType<?>> getEntityTypes()
    {
        return this.costView;
    }

    @Nullable
    public MobSpawnSettings.MobSpawnCost getCost(EntityType<?> type)
    {
        return this.self().getMobSpawnCosts().get(type);
    }

    public float getProbability()
    {
        return this.self().getCreatureGenerationProbability();
    }

    public MobSpawnSettingsBuilder disablePlayerSpawn()
    {
        return this;
    }
}
