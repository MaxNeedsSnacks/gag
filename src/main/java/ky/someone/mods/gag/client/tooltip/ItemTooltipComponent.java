package ky.someone.mods.gag.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ItemTooltipComponent(ItemStack stack) implements ClientTooltipComponent {
	@Override
	public int getHeight() {
		return 18;
	}

	@Override
	public int getWidth(Font font) {
		return 18;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
		graphics.renderItem(stack, x + 1, y + 1);
	}
}
