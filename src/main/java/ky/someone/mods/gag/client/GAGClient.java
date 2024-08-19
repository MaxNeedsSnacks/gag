package ky.someone.mods.gag.client;

import com.google.common.collect.Iterables;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.client.render.TimeAcceleratorEntityRenderer;
import ky.someone.mods.gag.client.screen.LabelingMenuScreen;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import ky.someone.mods.gag.item.GAGItem;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.particle.client.MagicParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface GAGClient {
	static void init(IEventBus bus) {
		bus.register(GAGClient.class);
	}

	@SubscribeEvent
	static void setup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> ItemProperties.register(GAGRegistry.PIGMENT_JAR.asItem(), GAGUtil.id("pigment_amount"),
				(stack, level, entity, seed) -> PigmentJarItem.getColorAmount(stack) / (float) PigmentJarItem.MAX_AMOUNT));
	}

	@SubscribeEvent
	static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
		event.register(GAGRegistry.LABELING_MENU.get(), LabelingMenuScreen::new);
	}

	@SubscribeEvent
	static void registerColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, index) -> index == 0 ? 0xFF000000 | PigmentJarItem.getRgbColor(stack) : -1, GAGRegistry.PIGMENT_JAR);
	}

	@SubscribeEvent
	static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(GAGRegistry.MAGIC_PARTICLE.get(), MagicParticle.Provider::new);
	}

	@SubscribeEvent
	static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(GAGRegistry.TIME_ACCELERATOR.get(), TimeAcceleratorEntityRenderer::new);
		event.registerEntityRenderer(GAGRegistry.MINING_DYNAMITE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(GAGRegistry.FISHING_DYNAMITE.get(), ThrownItemRenderer::new);
	}

	static void renderHUD(RenderGuiEvent.Post event) {
		var graphics = event.getGuiGraphics();
		var mc = Minecraft.getInstance();

		if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR) {
			return;
		}

		var level = mc.level;
		var player = mc.player;

		if (level == null || player == null) {
			return;
		}

		if (mc.hitResult instanceof BlockHitResult blockHit) {
			var pos = blockHit.getBlockPos();
			var block = level.getBlockState(pos).getBlock();

			var accelerator = Iterables.getFirst(level.getEntitiesOfClass(TimeAcceleratorEntity.class, new AABB(pos)), null);
			if (accelerator != null) {
				var accelSpeed = accelerator.getTimesAccelerated();
				var timeLeft = accelerator.getTicksRemaining() / 20d;

				if (accelSpeed == 0) return;

				renderHudTooltip(mc, graphics, List.of(
						block.getName(),
						Component.translatable("info.gag.time_sand_tooltip_mult",
								GAGUtil.asStyledValue(accelSpeed, GAGConfig.temporalPouch.maxRate(), Integer.toString(1 << accelSpeed))),
						Component.translatable("info.gag.time_sand_tooltip_time",
								GAGUtil.asStyledValue(timeLeft, GAGConfig.temporalPouch.durationPerUse(), String.format("%.2f", timeLeft)))
				));

				return;
			}
		}

		var stack = player.getUseItem();
		List<Component> tooltip = List.of();

		if (!stack.isEmpty() && stack.getItem() instanceof GAGItem item) {
			tooltip = item.getUsingTooltip(player, stack, player.getTicksUsingItem());
		} else if ((stack = player.getMainHandItem()).getItem() instanceof GAGItem item) {
			tooltip = item.getHoldingTooltip(player, stack);
		} else if ((stack = player.getOffhandItem()).getItem() instanceof GAGItem item) {
			tooltip = item.getHoldingTooltip(player, stack);
		}

		if (!tooltip.isEmpty()) {
			renderHudTooltip(mc, graphics, tooltip);
		}
	}

	private static void renderHudTooltip(Minecraft mc, GuiGraphics graphics, List<Component> text) {
		if (mc.screen != null) return;
		var x = mc.getWindow().getGuiScaledWidth() / 2;
		var y = mc.getWindow().getGuiScaledHeight() / 2;
		graphics.renderComponentTooltip(mc.font, text, x + 10, y);
	}
}
