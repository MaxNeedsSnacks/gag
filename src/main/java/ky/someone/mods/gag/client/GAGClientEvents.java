package ky.someone.mods.gag.client;

import com.google.common.collect.Iterables;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import ky.someone.mods.gag.item.GAGItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.List;

public interface GAGClientEvents {
	@SubscribeEvent
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
