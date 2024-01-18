package xyz.bluspring.forgebiomemodifiers.structures;

import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import xyz.bluspring.forgebiomemodifiers.extensions.ModifiableStructureExtension;
import xyz.bluspring.forgebiomemodifiers.structures.serializers.NoneStructureModifier;

import java.util.function.Supplier;

public class StructureModifiers {
    public static final ResourceKey<Registry<StructureModifier>> STRUCTURE_MODIFIER_KEY = ResourceKey.createRegistryKey(new ResourceLocation("forge", "forge/structure_modifier"));
    public static final LazyRegistrar<StructureModifier> STRUCTURE_MODIFIERS = LazyRegistrar.create(STRUCTURE_MODIFIER_KEY, "forge");
    public static final Supplier<Registry<StructureModifier>> STRUCTURE_MODIFIER_REGISTRY = STRUCTURE_MODIFIERS.makeRegistry();

    public static final ResourceKey<Registry<Codec<? extends StructureModifier>>> STRUCTURE_MODIFIER_SERIALIZER_KEY = ResourceKey.createRegistryKey(new ResourceLocation("forge", "forge/structure_modifier_serializers"));
    public static final LazyRegistrar<Codec<? extends StructureModifier>> STRUCTURE_MODIFIER_SERIALIZERS = LazyRegistrar.create(STRUCTURE_MODIFIER_SERIALIZER_KEY, "forge");
    public static final Supplier<Registry<Codec<? extends StructureModifier>>> STRUCTURE_MODIFIER_SERIALIZER_REGISTRY = STRUCTURE_MODIFIER_SERIALIZERS.makeRegistry();

    public static void runModifiers(MinecraftServer server) {
        var registries = server.registryAccess();

        var structureModifiers = registries.registryOrThrow(STRUCTURE_MODIFIER_KEY)
                .holders()
                .map(Holder::value)
                .toList();

        registries.registryOrThrow(Registries.STRUCTURE).holders().forEach((structureHolder) -> {
            ((ModifiableStructureExtension) structureHolder.value()).modifiableStructureInfo().applyStructureModifiers(structureHolder, structureModifiers);
        });
    }


    /**
     * Noop structure modifier. Can be used in a structure modifier json with "type": "forge:none".
     */
    public static final RegistryObject<Codec<NoneStructureModifier>> NONE_STRUCTURE_MODIFIER_TYPE = STRUCTURE_MODIFIER_SERIALIZERS.register("none", () -> Codec.unit(NoneStructureModifier.INSTANCE));
}
