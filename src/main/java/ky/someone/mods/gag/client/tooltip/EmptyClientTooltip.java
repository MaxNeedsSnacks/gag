package ky.someone.mods.gag.client.tooltip;


import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public record EmptyClientTooltip(int width, int height) implements ClientTooltipComponent {
	public static EmptyClientTooltip x(int value) {
		return new EmptyClientTooltip(value, 0);
	}

	public static EmptyClientTooltip y(int value) {
		return new EmptyClientTooltip(0, value);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth(Font font) {
		return width;
	}
}
