package ky.someone.mods.gag.network;

import io.netty.buffer.ByteBuf;
import ky.someone.mods.gag.GAG;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.config.GAGConfig.RopeConfig;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static ky.someone.mods.gag.config.GAGConfig.HearthstoneConfig;

public record ServerConfigPayload(HearthstoneConfig hearthstone, RopeConfig escapeRope) implements PayloadWithHandler {

	public static final StreamCodec<ByteBuf, ServerConfigPayload> CODEC = StreamCodec.composite(
			HearthstoneConfig.CODEC,
			ServerConfigPayload::hearthstone,
			RopeConfig.CODEC,
			ServerConfigPayload::escapeRope,
			ServerConfigPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return GAGNetwork.SERVER_CONFIG_SYNC;
	}

	@Override
	public void handle(IPayloadContext ctx) {
		// we *should* be fine just overriding this stuff since the config
		// is reloaded once they join another server anyways?
		ctx.enqueueWork(() -> {
			GAG.LOGGER.info("GAG config synchronised from server");
			GAGConfig.handleSync(hearthstone, escapeRope);
		});
	}
}
