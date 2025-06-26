package ky.someone.mods.gag.client.tooltip;


import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public record EmptyTooltipComponent(int width, int height) implements ClientTooltipComponent {
	public static EmptyTooltipComponent x(int value) {
		return new EmptyTooltipComponent(value, 0);
	}

	public static EmptyTooltipComponent y(int value) {
		return new EmptyTooltipComponent(0, value);
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
