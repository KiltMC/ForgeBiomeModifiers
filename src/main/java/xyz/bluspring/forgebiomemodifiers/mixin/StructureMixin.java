package xyz.bluspring.forgebiomemodifiers.mixin;

import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.bluspring.forgebiomemodifiers.extensions.ModifiableStructureExtension;
import xyz.bluspring.forgebiomemodifiers.structures.ModifiableStructureInfo;

@Mixin(Structure.class)
public class StructureMixin implements ModifiableStructureExtension {
    @Shadow @Final protected Structure.StructureSettings settings;

    @Unique
    private final ModifiableStructureInfo modifiableStructureInfo = new ModifiableStructureInfo(new ModifiableStructureInfo.StructureInfo(this.settings));

    @Override
    public ModifiableStructureInfo modifiableStructureInfo() {
        return this.modifiableStructureInfo;
    }

    @Override
    public Structure.StructureSettings getModifiedStructureSettings() {
        return this.modifiableStructureInfo().get().structureSettings();
    }
}
