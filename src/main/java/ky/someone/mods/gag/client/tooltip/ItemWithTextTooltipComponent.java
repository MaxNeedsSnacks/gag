package ky.someone.mods.gag.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public record ItemWithTextTooltipComponent(ItemStack stack, @Nullable Component text) implements ClientTooltipComponent {
	public ItemWithTextTooltipComponent(ItemStack stack) {
		this(stack, null);
	}

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

		if (!stack.isEmpty()) {
			graphics.pose().pushPose();

			var text = this.text;
			if (text == null && stack.getCount() != 1) {
				text = Component.literal(String.valueOf(stack.getCount()));
			}

			if (text != null) {
				graphics.pose().translate(0.0F, 0.0F, 200.0F);
				graphics.drawString(font, text, x + 19 - 2 - font.width(text), y + 6 + 3, 16777215, true);
			}

			graphics.pose().popPose();
		}
	}
}
