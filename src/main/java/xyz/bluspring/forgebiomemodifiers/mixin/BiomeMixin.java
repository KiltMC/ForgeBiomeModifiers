package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bluspring.forgebiomemodifiers.extensions.ModifiableBiomeExtension;
import xyz.bluspring.forgebiomemodifiers.worldgen.ModifiableBiomeInfo;

@Mixin(Biome.class)
public class BiomeMixin implements ModifiableBiomeExtension {
    @Unique
    private ModifiableBiomeInfo modifiableBiomeInfo;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void lc$initBiomeInfo(Biome.ClimateSettings climateSettings, BiomeSpecialEffects specialEffects, BiomeGenerationSettings generationSettings, MobSpawnSettings mobSettings, CallbackInfo ci) {
        modifiableBiomeInfo = new ModifiableBiomeInfo(new ModifiableBiomeInfo.BiomeInfo(climateSettings, specialEffects, generationSettings, mobSettings));
    }

    @Inject(method = "getMobSettings", at = @At("RETURN"), cancellable = true)
    public void lc$replaceMobSettings(CallbackInfoReturnable<MobSpawnSettings> cir) {
        if (!cir.isCancelled())
            cir.setReturnValue(modifiableBiomeInfo.get().mobSpawnSettings());
    }

    @Inject(method = "getGenerationSettings", at = @At("RETURN"), cancellable = true)
    public void lc$replaceGenSettings(CallbackInfoReturnable<BiomeGenerationSettings> cir) {
        if (!cir.isCancelled())
            cir.setReturnValue(modifiableBiomeInfo.get().generationSettings());
    }


    @Override
    public ModifiableBiomeInfo modifiableBiomeInfo() {
        return modifiableBiomeInfo;
    }

    @Override
    public Biome.ClimateSettings getModifiedClimateSettings() {
        return modifiableBiomeInfo.getModifiedBiomeInfo().climateSettings();
    }

    @Override
    public BiomeSpecialEffects getModifiedSpecialEffects() {
        return modifiableBiomeInfo.getModifiedBiomeInfo().effects();
    }
}
