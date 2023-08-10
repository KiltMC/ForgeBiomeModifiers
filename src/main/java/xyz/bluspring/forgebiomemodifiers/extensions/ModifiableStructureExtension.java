package xyz.bluspring.forgebiomemodifiers.extensions;

import net.minecraft.world.level.levelgen.structure.Structure;
import xyz.bluspring.forgebiomemodifiers.structures.ModifiableStructureInfo;

public interface ModifiableStructureExtension {
    ModifiableStructureInfo modifiableStructureInfo();
    Structure.StructureSettings getModifiedStructureSettings();
}
