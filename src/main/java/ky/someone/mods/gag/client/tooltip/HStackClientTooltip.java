package ky.someone.mods.gag.client.tooltip;

import ky.someone.mods.gag.util.VerticalAlignment;
import ky.someone.mods.gag.util.tooltip.HStackTooltipComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

import java.util.List;

public record HStackClientTooltip(
		List<ClientTooltipComponent> components,
		int padding,
		VerticalAlignment align
) implements ClientTooltipComponent {

	public static HStackClientTooltip of(HStackTooltipComponent data) {
		var sink = new ClientTooltipBuilder();
		data.build(sink);

		return new HStackClientTooltip(sink.build(), data.padding, data.alignment);
	}

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
		int h = getHeight();

		for (var component : components) {
			int dy = align.yOffset(component.getHeight(), h);

			component.renderText(font, x + dx, y + dy, pose, bufferSource);
			dx += component.getWidth(font) + padding;
		}
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
		int dx = 0;
		int h = getHeight();

		for (var component : components) {
			int dy = align.yOffset(component.getHeight(), h);

			component.renderImage(font, x + dx, y + dy, graphics);
			dx += component.getWidth(font) + padding;
		}
	}
}
