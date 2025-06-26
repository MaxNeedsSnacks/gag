package ky.someone.mods.gag.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

import java.util.List;

public record HStackTooltipComponent(List<ClientTooltipComponent> components, int padding) implements ClientTooltipComponent {
	@Override
	public int getHeight() {
		int height = 0;
		for (var component : components) {
			int h = component.getHeight();
			if (h > height) {
				height = h;
			}
		}
		return height;
	}

	@Override
	public int getWidth(Font font) {
		int sum = 0;
		for (var component : components) {
			int width = component.getWidth(font);
			sum += width;
		}
		return sum + (components.size() - 1) * padding;
	}

	@Override
	public void renderText(Font font, int x, int y, Matrix4f pose, MultiBufferSource.BufferSource bufferSource) {
		int dx = 0;
		for (var component : components) {
			component.renderText(font, x + dx, y, pose, bufferSource);
			dx += component.getWidth(font) + padding;
		}
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
		int dx = 0;
		for (var component : components) {
			component.renderImage(font, x + dx, y, graphics);
			dx += component.getWidth(font) + padding;
		}
	}
}
