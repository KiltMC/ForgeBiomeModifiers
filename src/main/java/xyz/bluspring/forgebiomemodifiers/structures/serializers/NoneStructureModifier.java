package xyz.bluspring.forgebiomemodifiers.structures.serializers;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.Structure;
import xyz.bluspring.forgebiomemodifiers.structures.ModifiableStructureInfo;
import xyz.bluspring.forgebiomemodifiers.structures.StructureModifier;
import xyz.bluspring.forgebiomemodifiers.structures.StructureModifiers;

public class NoneStructureModifier implements StructureModifier
{
    public static final NoneStructureModifier INSTANCE = new NoneStructureModifier();

    @Override
    public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder)
    {
        // NOOP - intended for datapack makers who want to disable a structure modifier
    }

    @Override
    public Codec<? extends StructureModifier> codec()
    {
        return StructureModifiers.NONE_STRUCTURE_MODIFIER_TYPE.get();
    }
}

