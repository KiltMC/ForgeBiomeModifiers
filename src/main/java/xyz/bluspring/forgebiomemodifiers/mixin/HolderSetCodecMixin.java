package xyz.bluspring.forgebiomemodifiers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bluspring.forgebiomemodifiers.holdersets.HolderSets;
import xyz.bluspring.forgebiomemodifiers.holdersets.ICustomHolderSet;

import java.util.List;
import java.util.function.Function;

@Mixin(HolderSetCodec.class)
public class HolderSetCodecMixin<E> {
    @Shadow @Final private Codec<Either<TagKey<E>, List<Holder<E>>>> registryAwareCodec;
    @Unique private Codec<ICustomHolderSet<E>> customDispatchCodec;
    @Unique private Codec<Either<ICustomHolderSet<E>, Either<TagKey<E>, List<Holder<E>>>>> combinedCodec;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomCombinedCodecs(ResourceKey<? extends Registry<E>> registryKey, Codec<Holder<E>> elementCodec, boolean disallowInline, CallbackInfo ci) {
        this.customDispatchCodec = ExtraCodecs.lazyInitializedCodec(HolderSets.HOLDER_SET_TYPES_REGISTRY::byNameCodec)
            .dispatch(ICustomHolderSet::type, type -> type.makeCodec(registryKey, elementCodec, disallowInline));

        this.combinedCodec = new ExtraCodecs.EitherCodec<>(this.customDispatchCodec, this.registryAwareCodec);
    }

    @ModifyReceiver(method = "decode", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;decode(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"))
    private Codec useCustomDispatchCodecForDecoding(Codec instance, DynamicOps dynamicOps, Object o) {
        return this.combinedCodec;
    }

    @Redirect(method = "method_40385", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Either;map(Ljava/util/function/Function;Ljava/util/function/Function;)Ljava/lang/Object;"))
    private static <T, E> Object mapToCombinedCodec(Either<ICustomHolderSet<E>, Either<TagKey<E>, List<Holder<E>>>> instance, Function<? super TagKey<E>, ? extends T> function, Function<? super List<Holder<E>>, ? extends T> function2) {
        return instance.map(Function.identity(), tagOrList -> tagOrList.map(function, function2));
    }

    @Inject(method = "encode(Lnet/minecraft/core/HolderSet;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;encode(Ljava/lang/Object;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", shift = At.Shift.BEFORE), cancellable = true)
    private <T> void encodeUsingCustomCodec(HolderSet<E> input, DynamicOps<T> ops, T prefix, CallbackInfoReturnable<DataResult<T>> cir) {
        if (input instanceof ICustomHolderSet<E> customHolderSet)
            cir.setReturnValue(this.customDispatchCodec.encode(customHolderSet, ops, prefix));
    }
}
