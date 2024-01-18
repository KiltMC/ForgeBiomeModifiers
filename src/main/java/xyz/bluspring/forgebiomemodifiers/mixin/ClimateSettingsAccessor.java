package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Biome.ClimateSettings.class)
public interface ClimateSettingsAccessor {
    @Invoker("<init>")
    static Biome.ClimateSettings createClimateSettings(boolean hasPrecipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall) {
        throw new UnsupportedOperationException();
    }
}
