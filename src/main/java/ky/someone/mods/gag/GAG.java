package ky.someone.mods.gag;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LightningEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil;
import ky.someone.mods.gag.block.NoSolicitorsSign;
import ky.someone.mods.gag.client.GAGClient;
import ky.someone.mods.gag.command.GAGCommands;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.effect.RepellingEffect;
import ky.someone.mods.gag.item.EnergizedHearthstoneItem;
import ky.someone.mods.gag.network.GAGNetwork;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

import static dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil.CONFIG_DIR;

@Mod(GAGUtil.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class GAG {
	public static final Logger LOGGER = LogUtils.getLogger();

	public GAG(IEventBus bus) {
		GAGConfig.init();
		LifecycleEvent.SERVER_BEFORE_START.register((server) -> ConfigUtil.loadDefaulted(GAGConfig.CONFIG, CONFIG_DIR, GAGUtil.MOD_ID));
		PlayerEvent.PLAYER_JOIN.register(GAGConfig::syncConfigTo);

		EntityEvent.LIVING_CHECK_SPAWN.register(RepellingEffect::applyRepel);
		LightningEvent.STRIKE.register(EnergizedHearthstoneItem::lightningStrike);
		// This might be too aggressive, since it also blocks manual summons,
		// but... it should be okay? See if anyone complains about it down the line lol
		EntityEvent.ADD.register(NoSolicitorsSign::notBuyingYourStuff);

		CommandRegistrationEvent.EVENT.register(GAGCommands::register);
		LifecycleEvent.SETUP.register(GAGNetwork::init);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			GAGClient.init(bus);
		}
	}

	@SubscribeEvent
	public static void replaceTiabMapping(RegisterEvent event) {
		// register all elements from the registry helper
		GAGRegistry.HELPER.register(event);

		var reg = event.getRegistry(Registries.ITEM);
		if (reg == null) return;
		// remap "tiab:time_in_a_bottle" to "gag:temporal_pouch" if TIAB Standalone is missing
		//  (requested by people wanting to transition from TIAB Standalone to GAG)
		reg.addAlias(ResourceLocation.parse("tiab:time_in_a_bottle"), GAGRegistry.TIME_SAND_POUCH.getId());
	}
}
