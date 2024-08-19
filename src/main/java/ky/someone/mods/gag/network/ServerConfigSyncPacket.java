package ky.someone.mods.gag.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import ky.someone.mods.gag.GAG;
import ky.someone.mods.gag.config.GAGConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class ServerConfigSyncPacket extends BaseS2CMessage {

	private final FriendlyByteBuf synced;

	public ServerConfigSyncPacket(FriendlyByteBuf buf) {
		this.synced = buf;
	}

	@Override
	public MessageType getType() {
		return GAGNetwork.SERVER_CONFIG_SYNC;
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeBytes(synced);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		// we *should* be fine just overriding this stuff since the config
		// is reloaded once they join another server anyways?
		context.queue(() -> {
			GAG.LOGGER.info("GAG config synchronised from server");
			GAGConfig.handleSync(synced);
		});
	}
}
