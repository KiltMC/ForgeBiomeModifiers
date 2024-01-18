package xyz.bluspring.forgebiomemodifiers.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.RegistryDataLoader;

import java.util.List;

public interface RegisterDatapackRegistryCallback {
    Event<RegisterDatapackRegistryCallback> EVENT = EventFactory.createArrayBacked(RegisterDatapackRegistryCallback.class, callbacks -> (list) -> {
        for (RegisterDatapackRegistryCallback callback : callbacks) {
            callback.registerDatapacks(list);
        }
    });

    void registerDatapacks(List<RegistryDataLoader.RegistryData<?>> infos);
}
