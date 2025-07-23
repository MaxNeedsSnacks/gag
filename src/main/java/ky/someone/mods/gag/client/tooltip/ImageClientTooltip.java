package ky.someone.mods.gag.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

public record ImageClientTooltip(
		ResourceLocation texture,
		int u, int v,
		int width, int height,
		int regionWidth, int regionHeight,
		int textureWidth, int textureHeight
) implements ClientTooltipComponent {

	public ImageClientTooltip(ResourceLocation texture, int u, int v, int width, int height) {
		this(texture, u, v, width, height, width, height, 256, 256);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth(Font font) {
		return width;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
		RenderSystem.enableBlend();
		graphics.blit(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
		RenderSystem.disableBlend();
	}
}