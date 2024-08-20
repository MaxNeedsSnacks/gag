package ky.someone.mods.gag.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface PayloadWithHandler extends CustomPacketPayload {
	@Override
	Type<? extends CustomPacketPayload> type();

	void handle(IPayloadContext ctx);
}
