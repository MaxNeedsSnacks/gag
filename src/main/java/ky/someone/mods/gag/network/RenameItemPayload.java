package ky.someone.mods.gag.network;

import io.netty.buffer.ByteBuf;
import ky.someone.mods.gag.menu.LabelingMenu;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringUtil;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RenameItemPayload(String name) implements PayloadWithHandler {
	public static final StreamCodec<ByteBuf, RenameItemPayload> CODEC = ByteBufCodecs.STRING_UTF8
			.map(RenameItemPayload::new, RenameItemPayload::name);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return GAGNetwork.LABELER_TRY_RENAME;
	}

	// TODO: rework how renaming works (not content with it)
	@Override
	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player().containerMenu instanceof LabelingMenu menu) {
				var name = StringUtil.filterText(this.name);
				if (name.length() <= 50) {
					menu.setName(name);
				}
			}
		});
	}
}
