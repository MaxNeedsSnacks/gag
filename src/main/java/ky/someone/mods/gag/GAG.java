package ky.someone.mods.gag;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LightningEvent;
import dev.architectury.event.events.common.PlayerEvent;
import ky.someone.mods.gag.block.NoSolicitorsSign;
import ky.someone.mods.gag.client.GAGClient;
import ky.someone.mods.gag.command.GAGCommands;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.effect.RepellingEffect;
import ky.someone.mods.gag.item.EnergizedHearthstoneItem;
import ky.someone.mods.gag.network.GAGNetwork;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(GAGUtil.MOD_ID)
public class GAG {
	public static final Logger LOGGER = LogUtils.getLogger();

	public GAG(IEventBus bus) {
		LifecycleEvent.SERVER_BEFORE_START.register(GAGConfig::load);
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
}
