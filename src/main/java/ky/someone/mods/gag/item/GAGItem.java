package ky.someone.mods.gag.item;

import ky.someone.mods.gag.util.tooltip.TooltipSink;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract class for all items registered by this mod.
 * Contains convenience methods.
 */
public abstract class GAGItem extends Item implements ItemWithSubsets {
	public GAGItem(Properties properties) {
		super(properties);
	}

	public boolean shouldDamage(@Nullable Player player, ItemStack stack) {
		return player == null || !player.isCreative();
	}

	public void getHoldingTooltip(Player player, ItemStack stack, TooltipSink sink) {
	}

	public void getUsingTooltip(Player player, ItemStack stack, int useTicks, TooltipSink sink) {
	}
}
