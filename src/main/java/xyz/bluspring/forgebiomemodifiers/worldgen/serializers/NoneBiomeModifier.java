package xyz.bluspring.forgebiomemodifiers.worldgen.serializers;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifier;
import xyz.bluspring.forgebiomemodifiers.worldgen.BiomeModifiers;
import xyz.bluspring.forgebiomemodifiers.worldgen.ModifiableBiomeInfo;

public class NoneBiomeModifier implements BiomeModifier
{
    public static final NoneBiomeModifier INSTANCE = new NoneBiomeModifier();

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
    {
        // NOOP - intended for datapack makers who want to disable a biome modifier
    }

    @Override
    public Codec<? extends BiomeModifier> codec()
    {
        return BiomeModifiers.NONE_BIOME_MODIFIER_TYPE.get();
    }
}
