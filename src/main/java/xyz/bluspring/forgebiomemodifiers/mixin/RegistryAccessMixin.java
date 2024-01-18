package xyz.bluspring.forgebiomemodifiers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bluspring.forgebiomemodifiers.events.RegisterDatapackRegistryCallback;

import java.util.ArrayList;
import java.util.List;

@Mixin(RegistryDataLoader.class)
public class RegistryAccessMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/List;of([Ljava/lang/Object;)Ljava/util/List;"))
    private static List<RegistryDataLoader.RegistryData<?>> lc$appendCustomRegistries(Object[] dataList, Operation<List<RegistryDataLoader.RegistryData<?>>> original) {
        List<RegistryDataLoader.RegistryData<?>> list = new ArrayList<>(List.of((RegistryDataLoader.RegistryData<?>[]) dataList));

        RegisterDatapackRegistryCallback.EVENT.invoker().registerDatapacks(list);

        return list;
    }
}
