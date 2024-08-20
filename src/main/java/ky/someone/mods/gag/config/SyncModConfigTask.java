package ky.someone.mods.gag.config;

import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.network.ServerConfigPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ConfigurationTask;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;

import java.util.function.Consumer;

public record SyncModConfigTask(ServerConfigurationPacketListener listener) implements ICustomConfigurationTask {

	public static final Type TYPE = new ConfigurationTask.Type(GAGUtil.id("sync_mod_config"));

	@Override
	public void run(Consumer<CustomPacketPayload> sender) {
		sender.accept(new ServerConfigPayload(GAGConfig.hearthstone, GAGConfig.escapeRope));
		listener.finishCurrentTask(type());
	}

	@Override
	public Type type() {
		return TYPE;
	}
}
