package xyz.bluspring.forgebiomemodifiers.worldgen;


import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import xyz.bluspring.forgebiomemodifiers.mixin.BSEBuilderAccessor;

import java.util.Optional;

/**
 * Extension of the vanilla builder but also provides read access and a copy-from-existing-data helper.
 * Also, the base builder crashes if certain values aren't specified on build, so this enforces the setting of those.
 */
public class BiomeSpecialEffectsBuilder extends BiomeSpecialEffects.Builder
{
    public static BiomeSpecialEffectsBuilder copyOf(BiomeSpecialEffects baseEffects)
    {
        BiomeSpecialEffectsBuilder builder = BiomeSpecialEffectsBuilder.create(baseEffects.getFogColor(), baseEffects.getWaterColor(), baseEffects.getWaterFogColor(), baseEffects.getSkyColor());
        ((BSEBuilderAccessor) builder).setGrassColorModifier(baseEffects.getGrassColorModifier());
        baseEffects.getFoliageColorOverride().ifPresent(builder::foliageColorOverride);
        baseEffects.getGrassColorOverride().ifPresent(builder::grassColorOverride);
        baseEffects.getAmbientParticleSettings().ifPresent(builder::ambientParticle);
        baseEffects.getAmbientLoopSoundEvent().ifPresent(builder::ambientLoopSound);
        baseEffects.getAmbientMoodSettings().ifPresent(builder::ambientMoodSound);
        baseEffects.getAmbientAdditionsSettings().ifPresent(builder::ambientAdditionsSound);
        baseEffects.getBackgroundMusic().ifPresent(builder::backgroundMusic);
        return builder;
    }

    public static BiomeSpecialEffectsBuilder create(int fogColor, int waterColor, int waterFogColor, int skyColor)
    {
        return new BiomeSpecialEffectsBuilder(fogColor, waterColor, waterFogColor, skyColor);
    }

    protected BiomeSpecialEffectsBuilder(int fogColor, int waterColor, int waterFogColor, int skyColor)
    {
        super();
        this.fogColor(fogColor);
        this.waterColor(waterColor);
        this.waterFogColor(waterFogColor);
        this.skyColor(skyColor);
    }

    private BSEBuilderAccessor self() {
        return (BSEBuilderAccessor) this;
    }

    public int getFogColor()
    {
        return self().getFogColor().getAsInt();
    }

    public int waterColor()
    {
        return self().getWaterColor().getAsInt();
    }

    public int getWaterFogColor()
    {
        return self().getWaterFogColor().getAsInt();
    }

    public int getSkyColor()
    {
        return self().getSkyColor().getAsInt();
    }

    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier()
    {
        return self().getGrassColorModifier();
    }

    public Optional<Integer> getFoliageColorOverride()
    {
        return self().getFoliageColorOverride();
    }

    public Optional<Integer> getGrassColorOverride()
    {
        return self().getGrassColorOverride();
    }

    public Optional<AmbientParticleSettings> getAmbientParticle()
    {
        return self().getAmbientParticle();
    }

    public Optional<SoundEvent> getAmbientLoopSound()
    {
        return self().getAmbientLoopSoundEvent();
    }

    public Optional<AmbientMoodSettings> getAmbientMoodSound()
    {
        return self().getAmbientMoodSettings();
    }

    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound()
    {
        return self().getAmbientAdditionsSettings();
    }

    public Optional<Music> getBackgroundMusic()
    {
        return self().getBackgroundMusic();
    }
}
