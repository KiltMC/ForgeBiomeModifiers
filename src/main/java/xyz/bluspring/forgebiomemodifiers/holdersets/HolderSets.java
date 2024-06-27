package xyz.bluspring.forgebiomemodifiers.holdersets;

import com.mojang.serialization.DataResult;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import xyz.bluspring.forgebiomemodifiers.mixin.RegistryOpsAccessor;

public class HolderSets {
    public static final ResourceKey<Registry<HolderSetType>> HOLDER_SET_TYPES_KEY = ResourceKey.createRegistryKey(new ResourceLocation("forge:holder_set_type"));
    public static final LazyRegistrar<HolderSetType> HOLDER_SET_TYPES = LazyRegistrar.create(HOLDER_SET_TYPES_KEY, "forge");
    public static final Registry<HolderSetType> HOLDER_SET_TYPES_REGISTRY = HOLDER_SET_TYPES.makeRegistry().get();

    /**
     * Stock holder set type that represents any/all values in a registry. Can be used in a holderset object with {@code { "type": "forge:any" }}
     */
    public static final RegistryObject<HolderSetType> ANY_HOLDER_SET = HOLDER_SET_TYPES.register("any", () -> AnyHolderSet::codec);

    /**
     * Stock holder set type that represents an intersection of other holdersets. Can be used in a holderset object with {@code { "type": "forge:and", "values": [list of holdersets] }}
     */
    public static final RegistryObject<HolderSetType> AND_HOLDER_SET = HOLDER_SET_TYPES.register("and", () -> AndHolderSet::codec);

    /**
     * Stock holder set type that represents a union of other holdersets. Can be used in a holderset object with {@code { "type": "forge:or", "values": [list of holdersets] }}
     */
    public static final RegistryObject<HolderSetType> OR_HOLDER_SET = HOLDER_SET_TYPES.register("or", () -> OrHolderSet::codec);

    /**
     * <p>Stock holder set type that represents all values in a registry except those in another given set.
     * Can be used in a holderset object with {@code { "type": "forge:not", "value": holderset }}</p>
     */
    public static final RegistryObject<HolderSetType> NOT_HOLDER_SET = HOLDER_SET_TYPES.register("not", () -> NotHolderSet::codec);

    public static <T> SerializationType getSerializationType(HolderSet<T> holderSet) {
        // handle vanilla holderset types
        return holderSet instanceof HolderSet.ListBacked<T> listBacked
            ? listBacked.unwrap().map(
            // serializes as tag name if this holderset is named
            tag -> SerializationType.STRING,
            list -> list.size() == 1
                // if list has exactly one element then we have to check what kind, otherwise it's a list
                ? list.get(0).unwrap().map(
                // if holder has a key bound then it's serialized as that string, otherwise it's inlined as an object
                key -> key == null ? SerializationType.OBJECT : SerializationType.STRING,
                value -> SerializationType.OBJECT)
                : SerializationType.LIST)
            :
            holderSet instanceof ICustomHolderSet<T> customHolderSet ?
                customHolderSet.serializationType()
            : SerializationType.UNKNOWN; // unsupported holderset impl, could be anything
    }

    public static <E> com.mojang.serialization.MapCodec<HolderLookup.RegistryLookup<E>> retrieveRegistryLookup(ResourceKey<? extends Registry<? extends E>> resourceKey) {
        return ExtraCodecs.retrieveContext(ops -> {
            if (!(ops instanceof RegistryOps<?> registryOps))
                return DataResult.error(() -> "Not a registry ops");

            return ((RegistryOpsAccessor) registryOps).getLookupProvider().lookup(resourceKey).map(registryInfo -> {
                if (!(registryInfo.owner() instanceof HolderLookup.RegistryLookup<E> registryLookup))
                    return DataResult.<HolderLookup.RegistryLookup<E>>error(() -> "Found holder getter but was not a registry lookup for " + resourceKey);

                return DataResult.success(registryLookup, registryInfo.elementsLifecycle());
            }).orElseGet(() -> DataResult.error(() -> "Unknown registry: " + resourceKey));
        });
    }
}
