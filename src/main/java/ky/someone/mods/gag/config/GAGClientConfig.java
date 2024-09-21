package ky.someone.mods.gag.config;

import dev.shadowsoffire.placebo.config.ConfigCategory;
import dev.shadowsoffire.placebo.config.Configuration;
import dev.shadowsoffire.placebo.events.ResourceReloadEvent;
import ky.someone.mods.gag.GAGUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = GAGUtil.MOD_ID)
public class GAGClientConfig {
	public static boolean hearthstoneHidePosition;

	public static void load() {
		Configuration config = new Configuration(GAGUtil.MOD_ID + "-client");
		config.setTitle("Client-side configuration for Gadgets Against Grind");

		{
			var misc = config.getCategory("misc");

			{
				var hearth = new ConfigCategory("hearthstone", misc);

				hearthstoneHidePosition = config.getBoolean("hide_position", hearth.getQualifiedName(), false,
						"""
								Whether the target position of hearthstones should be hidden from GUIs.
								This may be useful for streamers who don't want to expose their base coordinates, for example.
								(Note this may be replaced with a different mechanic in the future!)
								""");
			}
		}

		if (config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public static void onClientPlayerDisconnect(ResourceReloadEvent event) {
		if (event.getSide().isClient()) {
			load();
		}
	}
}
