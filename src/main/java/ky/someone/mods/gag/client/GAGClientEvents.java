package ky.someone.mods.gag.client;

import com.google.common.collect.Iterables;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.client.tooltip.ClientTooltipBuilder;
import ky.someone.mods.gag.client.tooltip.EmptyClientTooltip;
import ky.someone.mods.gag.client.tooltip.TimeArrowClientTooltip;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import ky.someone.mods.gag.item.GAGItem;
import ky.someone.mods.gag.util.Tooltips;
import ky.someone.mods.gag.util.VerticalAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
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

				var speed = Tooltips.asStyledValue(accelSpeed, GAGConfig.temporalPouch.maxRate(), Integer.toString(1 << accelSpeed));

				var text = Tooltips.FLAVOUR
						.literal("🚀 x")
						.append(speed).append("   ")
						.append("⏳ ")
						.append(Tooltips.asStyledValue(timeLeft, GAGConfig.temporalPouch.durationPerUse(), String.format("%.2f", timeLeft)))
						.append("s");

				renderHudTooltip(mc, graphics, new ClientTooltipBuilder()
						.text(block.getName())
						.hstack(new ClientTooltipBuilder()
								.item(GAGRegistry.TIME_SAND_POUCH)
								.add(new TimeArrowClientTooltip(accelerator))
								.item(block)
								.build(), 1, VerticalAlignment.CENTER)
						.add(EmptyClientTooltip.y(4))
						.text(text)
						.build());

				return;
			}
		}

		var stack = player.getUseItem();
		var builder = new ClientTooltipBuilder();

		if (!stack.isEmpty() && stack.getItem() instanceof GAGItem item) {
			item.getUsingTooltip(player, stack, player.getTicksUsingItem(), builder);
		} else if ((stack = player.getMainHandItem()).getItem() instanceof GAGItem item) {
			item.getHoldingTooltip(player, stack, builder);
		} else if ((stack = player.getOffhandItem()).getItem() instanceof GAGItem item) {
			item.getHoldingTooltip(player, stack, builder);
		}

		var tooltip = builder.build();

		if (!tooltip.isEmpty()) {
			renderHudTooltip(mc, graphics, tooltip);
		}
	}

	private static void renderHudTooltip(Minecraft mc, GuiGraphics graphics, List<ClientTooltipComponent> tooltip) {
		if (mc.screen != null) return;
		var x = mc.getWindow().getGuiScaledWidth() / 2;
		var y = mc.getWindow().getGuiScaledHeight() / 2;
		graphics.renderTooltipInternal(mc.font, tooltip, x + 10, y, DefaultTooltipPositioner.INSTANCE);
	}
}
