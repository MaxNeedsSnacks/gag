package ky.someone.mods.gag.client;

import com.google.common.collect.Iterables;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.client.render.TimeAcceleratorEntityRenderer;
import ky.someone.mods.gag.client.screen.LabelingMenuScreen;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import ky.someone.mods.gag.item.GAGItem;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.particle.client.MagicParticle;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface GAGClient {

	static void init(IEventBus bus) {
		registerEntityRenderers();

		ClientLifecycleEvent.CLIENT_SETUP.register(GAGClient::setup);
		ClientGuiEvent.RENDER_HUD.register(GAGClient::renderHUD);

		bus.addListener(GAGClient::registerParticles);
	}

	static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(GAGRegistry.MAGIC_PARTICLE.get(), MagicParticle.Provider::new);
	}

	static void registerEntityRenderers() {
		EntityRendererRegistry.register(GAGRegistry.TIME_ACCELERATOR, TimeAcceleratorEntityRenderer::new);
		EntityRendererRegistry.register(GAGRegistry.MINING_DYNAMITE, ThrownItemRenderer::new);
		EntityRendererRegistry.register(GAGRegistry.FISHING_DYNAMITE, ThrownItemRenderer::new);
	}

	static void renderHUD(GuiGraphics graphics, DeltaTracker partialTicks) {
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
								GAGUtil.asStyledValue(accelSpeed, GAGConfig.SandsOfTime.MAX_RATE.get(), Integer.toString(1 << accelSpeed))),
						Component.translatable("info.gag.time_sand_tooltip_time",
								GAGUtil.asStyledValue(timeLeft, GAGConfig.SandsOfTime.DURATION_PER_USE.get(), String.format("%.2f", timeLeft)))
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

	static void setup(Minecraft minecraft) {
		RenderTypeRegistry.register(RenderType.cutoutMipped(), GAGRegistry.NO_SOLICITORS_SIGN.get());
		MenuRegistry.registerScreenFactory(GAGRegistry.LABELING_MENU.get(), LabelingMenuScreen::new);

		ColorHandlerRegistry.registerItemColors((stack, index) -> index == 0 ? PigmentJarItem.getRgbColor(stack) : -1, GAGRegistry.PIGMENT_JAR.get());

		ItemPropertiesRegistry.register(GAGRegistry.PIGMENT_JAR.get(), GAGUtil.id("pigment_amount"),
				(stack, level, entity, seed) -> PigmentJarItem.getColorAmount(stack) / (float) PigmentJarItem.MAX_AMOUNT);
	}
}
