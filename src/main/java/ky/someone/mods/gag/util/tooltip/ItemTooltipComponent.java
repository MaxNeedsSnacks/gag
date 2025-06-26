package ky.someone.mods.gag.util.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public record ItemTooltipComponent(ItemStack stack, boolean showCount, @Nullable Component customText) implements TooltipComponent {
	public ItemTooltipComponent(ItemStack stack) {
		this(stack, false, null);
	}
}
