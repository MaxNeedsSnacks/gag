package ky.someone.mods.gag.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import ky.someone.mods.gag.util.GAGUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

public class TimeArrowClientTooltip implements ClientTooltipComponent {
	static final ResourceLocation ARROW = GAGUtil.id("textures/gui/time_arrow.png");

	private final TimeAcceleratorEntity accelerator;

	public TimeArrowClientTooltip(TimeAcceleratorEntity accelerator) {
		this.accelerator = accelerator;
	}

	@Override
	public int getHeight() {
		return 18;
	}

	@Override
	public int getWidth(Font font) {
		return 24;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
		// time in millis for the animation
		var time = 3000 / (int) (Math.pow(accelerator.getTimesAccelerated(), 1.25));
		var entityTicks = accelerator.tickCount;

		int width = 24, regionWidth = 24;
		int height = 18, regionHeight = 18;

		//int subTime = (int) (System.currentTimeMillis() % time);
		var subTime = (int) ((entityTicks * 50f) % time);

		RenderSystem.enableBlend();
		// empty
		graphics.blit(ARROW, x, y, width, height, 0, 18, regionWidth, regionHeight, 24, 36);
		// filled
		graphics.blit(ARROW, x, y, width * subTime / time, height, 0, 0, regionWidth * subTime / time, regionHeight, 24, 36);
		RenderSystem.disableBlend();
		//graphics.blit(ARROW, x, y, 24, 18, 0, 0, 24, 18, 24, 36);
	}
}
