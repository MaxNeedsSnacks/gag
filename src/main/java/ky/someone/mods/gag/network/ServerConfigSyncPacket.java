package ky.someone.mods.gag.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.snbt.SNBTNet;
import ky.someone.mods.gag.GAG;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.menu.LabelingMenu;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class ServerConfigSyncPacket extends BaseS2CMessage {

	private final SNBTCompoundTag values;

	public ServerConfigSyncPacket(FriendlyByteBuf buf) {
		this(SNBTNet.readCompound(buf));
	}

	public ServerConfigSyncPacket(SNBTCompoundTag values) {
		this.values = values;
	}

	@Override
	public MessageType getType() {
		return GAGNetwork.SERVER_CONFIG_SYNC;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		SNBTNet.write(buf, values);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		// we *should* be fine just overriding this stuff since the config
		// is reloaded once they join another server anyways?
		context.queue(() -> {
			GAG.LOGGER.info("GAG config synchronised from server");
			GAGConfig.CONFIG.read(values);
		});
	}
}
