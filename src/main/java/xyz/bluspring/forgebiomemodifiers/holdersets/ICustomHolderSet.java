package xyz.bluspring.forgebiomemodifiers.holdersets;

import net.minecraft.core.HolderSet;

public interface ICustomHolderSet<T> extends HolderSet<T> {
    /**
     * {@return HolderSetType registered to {@link HolderSets.HOLDER_SET_TYPES}}
     */
    HolderSetType type();

    default SerializationType serializationType()
    {
        return SerializationType.OBJECT;
    }
}
