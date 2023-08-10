package xyz.bluspring.forgebiomemodifiers.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import xyz.bluspring.forgebiomemodifiers.registries.DatapackRegistryInfo;

import java.util.List;

public interface RegisterDatapackRegistryCallback {
    Event<RegisterDatapackRegistryCallback> EVENT = EventFactory.createArrayBacked(RegisterDatapackRegistryCallback.class, callbacks -> (list) -> {
        for (RegisterDatapackRegistryCallback callback : callbacks) {
            callback.registerDatapacks(list);
        }
    });

    void registerDatapacks(List<DatapackRegistryInfo<?>> infos);
}
