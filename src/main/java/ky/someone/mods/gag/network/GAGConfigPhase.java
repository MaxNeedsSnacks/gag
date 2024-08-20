package ky.someone.mods.gag.network;

import ky.someone.mods.gag.config.SyncModConfigTask;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;

public interface GAGConfigPhase {
	@SubscribeEvent
	private static void configureModdedClient(RegisterConfigurationTasksEvent event) {
		var listener = event.getListener();
		if (listener.hasChannel(GAGNetwork.SERVER_CONFIG_SYNC)) {
			event.register(new SyncModConfigTask(event.getListener()));
		}
	}
}
