package ky.someone.mods.gag;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LightningEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;

import static dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil.CONFIG_DIR;

public class GAG {
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final CreativeModeTab CREATIVE_TAB = CreativeTabRegistry.create(new ResourceLocation(GAGUtil.MOD_ID, GAGUtil.MOD_ID),
			() -> ItemRegistry.HEARTHSTONE.get().getDefaultInstance());

	public GAG() {
		// curiously, our config needs to be loaded first
		// because items and such will register immediately on Fabric
		ConfigUtil.loadDefaulted(GAGConfig.init(), CONFIG_DIR, GAGUtil.MOD_ID);

		BlockRegistry.BLOCKS.register();
		ItemRegistry.ITEMS.register();
		EntityTypeRegistry.ENTITIES.register();
		EffectRegistry.EFFECTS.register();
		ParticleTypeRegistry.PARTICLE_TYPES.register();
		MenuTypeRegistry.MENUS.register();
		GAGRecipeSerializers.RECIPE_SERIALIZERS.register();

		EntityEvent.LIVING_CHECK_SPAWN.register(RepellingEffect::applyRepel);
		LightningEvent.STRIKE.register(EnergizedHearthstoneItem::lightningStrike);
		// This might be too aggressive, since it also blocks manual summons,
		// but... it should be okay? See if anyone complains about it down the line lol
		EntityEvent.ADD.register(NoSolicitorsSign::notBuyingYourStuff);

		CommandRegistrationEvent.EVENT.register(GAGCommands::register);
		LifecycleEvent.SETUP.register(GAGNetwork::init);
	}

	public void init() {
		EnvExecutor.runInEnv(Env.CLIENT, () -> GAGClient::init);
	}
}
