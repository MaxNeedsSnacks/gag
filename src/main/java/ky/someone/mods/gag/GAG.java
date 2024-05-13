package ky.someone.mods.gag;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.*;
import dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil;
import ky.someone.mods.gag.block.BlockRegistry;
import ky.someone.mods.gag.block.NoSolicitorsSign;
import ky.someone.mods.gag.client.GAGClient;
import ky.someone.mods.gag.command.GAGCommands;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.effect.EffectRegistry;
import ky.someone.mods.gag.effect.RepellingEffect;
import ky.someone.mods.gag.entity.EntityTypeRegistry;
import ky.someone.mods.gag.item.EnergizedHearthstoneItem;
import ky.someone.mods.gag.item.ItemRegistry;
import ky.someone.mods.gag.menu.MenuTypeRegistry;
import ky.someone.mods.gag.network.GAGNetwork;
import ky.someone.mods.gag.particle.ParticleTypeRegistry;
import ky.someone.mods.gag.recipe.GAGRecipeSerializers;
import ky.someone.mods.gag.sound.GAGSounds;
import ky.someone.mods.gag.tab.GAGCreativeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

import static dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil.CONFIG_DIR;
import static net.neoforged.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod(GAGUtil.MOD_ID)
@Mod.EventBusSubscriber(bus = MOD)
public class GAG {
	public static final Logger LOGGER = LogUtils.getLogger();

	public GAG(IEventBus bus) {
		BlockRegistry.BLOCKS.register(bus);
		ItemRegistry.ITEMS.register(bus);
		EntityTypeRegistry.ENTITIES.register(bus);
		EffectRegistry.EFFECTS.register(bus);
		ParticleTypeRegistry.PARTICLE_TYPES.register(bus);
		MenuTypeRegistry.MENUS.register(bus);
		GAGSounds.SOUND_EVENTS.register(bus);
		GAGCreativeTabs.TABS.register(bus);
		GAGRecipeSerializers.RECIPE_SERIALIZERS.register(bus);

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
		var reg = event.getRegistry(Registries.ITEM);
		if (reg == null) return;
		// remap "tiab:time_in_a_bottle" to "gag:temporal_pouch" if TIAB Standalone is missing
		//  (requested by people wanting to transition from TIAB Standalone to GAG)
		reg.addAlias(new ResourceLocation("tiab:time_in_a_bottle"), ItemRegistry.TIME_SAND_POUCH.getId());
	}
}
