package ky.someone.mods.gag;

import com.mojang.logging.LogUtils;
import ky.someone.mods.gag.block.NoSolicitorsSign;
import ky.someone.mods.gag.client.GAGClient;
import ky.someone.mods.gag.effect.RepellingEffect;
import ky.someone.mods.gag.item.EnergizedHearthstoneItem;
import ky.someone.mods.gag.network.GAGNetwork;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import org.slf4j.Logger;

@Mod(GAGUtil.MOD_ID)
@EventBusSubscriber
public class GAG {
	public static final Logger LOGGER = LogUtils.getLogger();

	public GAG(IEventBus bus) {
		bus.addListener((FMLCommonSetupEvent event) -> {
			GAGNetwork.init(); // todo: move to neo networking
		});

		if (FMLEnvironment.dist == Dist.CLIENT) {
			GAGClient.init(bus);
		}
	}

	@SubscribeEvent
	public void checkSpawn(FinalizeSpawnEvent event) {
		if (RepellingEffect.applyRepel(event.getEntity(), event.getLevel(), event.getX(), event.getY(), event.getZ())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void entityLightning(EntityStruckByLightningEvent event) {
		if (EnergizedHearthstoneItem.lightningStrike(event.getLightning(), event.getEntity())) {
			event.setCanceled(true);
		}
	}

	// This might be too aggressive, since it also blocks manual summons,
	// but... it should be okay? See if anyone complains about it down the line lol
	@SubscribeEvent
	public void onEntityJoinLevel(EntityJoinLevelEvent event) {
		var entity = event.getEntity();
		var type = entity.getType();
		if ((type == EntityType.WANDERING_TRADER || type == EntityType.TRADER_LLAMA)
		    && event.getLevel() instanceof ServerLevel level) {
			if (NoSolicitorsSign.blockWandererSpawn(level, entity.getOnPos())) {
				event.setCanceled(true);
			}
		}
	}
}
