package xyz.bluspring.forgebiomemodifiers.structures;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.function.Function;

/**
 * JSON-serializable structure modifier.
 * Requires a {@link Codec} to deserialize structure modifiers from structure modifier jsons.
 * <p>
 * Structure modifier jsons have the following json format:
 * <pre>
 * {
 *   "type": "yourmod:yourserializer", // Indicates a registered structure modifier serializer
 *   // Additional fields can be specified here according to the codec
 * }
 * </pre>
 * <p>
 * Datapacks can also disable a structure modifier by overriding the json and using {@code "type": "forge:none"}.</p>
 */
public interface StructureModifier
{
    /**
     * Codec for (de)serializing structure modifiers inline.
     * Mods can use this for data generation.
     */
    Codec<StructureModifier> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> StructureModifiers.STRUCTURE_MODIFIER_SERIALIZER_REGISTRY.get().byNameCodec())
            .dispatch(StructureModifier::codec, Function.identity());

    /**
     * Codec for referring to structure modifiers by id in other datapack registry files.
     * Can only be used with {@link RegistryOps}.
     */
    Codec<Holder<StructureModifier>> REFERENCE_CODEC = RegistryFileCodec.create(StructureModifiers.STRUCTURE_MODIFIER_KEY, DIRECT_CODEC);

    /**
     * Codec for referring to structure modifiers by id, list of id, or tags.
     * Can only be used with {@link RegistryOps}.
     */
    Codec<HolderSet<StructureModifier>> LIST_CODEC = RegistryCodecs.homogeneousList(StructureModifiers.STRUCTURE_MODIFIER_KEY, DIRECT_CODEC);

    /**
     * Modifies the information via the provided structure builder.
     * Allows mob spawns and world-gen features to be added or removed,
     * and climate and client effects to be modified.
     *
     * @param structure the named structure being modified (with original data readable).
     * @param phase structure modification phase. Structure modifiers apply in each phase in order of the enum constants.
     * @param builder mutable structure info builder. Apply changes to this.
     */
    void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder);

    /**
     * @return the codec which serializes and deserializes this structure modifier
     */
    Codec<? extends StructureModifier> codec();

    enum Phase
    {
        /**
         * Catch-all for anything that needs to run before standard phases.
         */
        BEFORE_EVERYTHING,
        /**
         * Additional mob spawns, etc.
         */
        ADD,
        /**
         * Removal of mob spawns, etc.
         */
        REMOVE,
        /**
         * Alteration of values.
         */
        MODIFY,
        /**
         * Catch-all for anything that needs to run after standard phases.
         */
        AFTER_EVERYTHING
    }
}

