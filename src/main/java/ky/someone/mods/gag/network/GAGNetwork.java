package ky.someone.mods.gag.network;

import ky.someone.mods.gag.GAGUtil;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public interface GAGNetwork {
	private static <T extends CustomPacketPayload> Type<T> type(String id) {
		return new Type<>(GAGUtil.id(id));
	}

	Type<RenameItemPayload> LABELER_TRY_RENAME = type("rename_item");

	Type<FishsplosionPayload> FISHSPLOSION = type("fishsplosion");

	Type<ServerConfigPayload> SERVER_CONFIG_SYNC = type("server_config_sync");

	@SubscribeEvent
	static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
		var reg = event.registrar("1");

		reg.playToServer(LABELER_TRY_RENAME, RenameItemPayload.CODEC, RenameItemPayload::handle);

		reg.playToClient(FISHSPLOSION, FishsplosionPayload.CODEC, FishsplosionPayload::handle);

		reg.configurationToClient(SERVER_CONFIG_SYNC, ServerConfigPayload.CODEC, ServerConfigPayload::handle);
	}
}
