package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.OptionalInt;

@Mixin(BiomeSpecialEffects.Builder.class)
public interface BSEBuilderAccessor {
    @Accessor
    OptionalInt getFogColor();

    @Accessor
    void setFogColor(OptionalInt fogColor);

    @Accessor
    OptionalInt getWaterColor();

    @Accessor
    void setWaterColor(OptionalInt waterColor);

    @Accessor
    OptionalInt getWaterFogColor();

    @Accessor
    void setWaterFogColor(OptionalInt waterFogColor);

    @Accessor
    OptionalInt getSkyColor();

    @Accessor
    void setSkyColor(OptionalInt skyColor);

    @Accessor
    Optional<Integer> getFoliageColorOverride();

    @Accessor
    void setFoliageColorOverride(Optional<Integer> foliageColorOverride);

    @Accessor
    Optional<Integer> getGrassColorOverride();

    @Accessor
    void setGrassColorOverride(Optional<Integer> grassColorOverride);

    @Accessor
    BiomeSpecialEffects.GrassColorModifier getGrassColorModifier();

    @Accessor
    void setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier grassColorModifier);

    @Accessor
    Optional<AmbientParticleSettings> getAmbientParticle();

    @Accessor
    void setAmbientParticle(Optional<AmbientParticleSettings> ambientParticle);

    @Accessor
    Optional<SoundEvent> getAmbientLoopSoundEvent();

    @Accessor
    void setAmbientLoopSoundEvent(Optional<SoundEvent> ambientLoopSoundEvent);

    @Accessor
    Optional<AmbientMoodSettings> getAmbientMoodSettings();

    @Accessor
    void setAmbientMoodSettings(Optional<AmbientMoodSettings> ambientMoodSettings);

    @Accessor
    Optional<AmbientAdditionsSettings> getAmbientAdditionsSettings();

    @Accessor
    void setAmbientAdditionsSettings(Optional<AmbientAdditionsSettings> ambientAdditionsSettings);

    @Accessor
    Optional<Music> getBackgroundMusic();

    @Accessor
    void setBackgroundMusic(Optional<Music> backgroundMusic);
}
